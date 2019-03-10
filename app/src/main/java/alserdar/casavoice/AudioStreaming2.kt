package alserdar.casavoice

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.*
import android.media.audiofx.NoiseSuppressor
import android.os.Build
import android.util.Log

import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean


class AudioStreaming2:Thread{

    val TAG = "AudioStreaming"
    private val   MIME_TYPE = "video/avc"
    private val BIT_RATE = 16000000
    private val FRAME_RATE = 30 // Frames per second
    private val I_FRAME_INTERVAL = 1
    val TIMEOUT_USEC = 10000L
    //parameters for encoder
    val AUDIO_MIME_TYPE = MediaFormat.MIMETYPE_AUDIO_AAC
    val AUDIO_CHANNEL_COUNT =1
    val AUDIO_MAX_INPUT_SIZE=8820
    val AUDIO_RECORD_FORMAT= AudioFormat.ENCODING_PCM_16BIT
    val AUDIO_CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
    private val mWidth: Int = 256
    private val mHeight: Int = 144
    var mediaCodec: MediaCodec? = null
    var mediaMuxer:MediaMuxer?=null

    val quit = AtomicBoolean(false)
    var mBufferInfo = MediaCodec.BufferInfo()

    var listener: AudioStreaming2.ScreenRecordListener

     var audioRecord: AudioRecord?=null
    lateinit var audioBuffer:ByteArray
     var audioEncoder: MediaCodec? = null
    val audioBufferInfo = MediaCodec.BufferInfo()
    var audioSampleRate:Int
    var audioBitRate:Int
    var noiseSuspender:NoiseSuppressor? = null

    //recording path
    var startTime = 0L
    val bufferSize  = AudioRecord.getMinBufferSize(44100,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT)*2
var context:Context
    constructor( audioSampleRate:Int,
                audioBitRate:Int,  listener: AudioStreaming2.ScreenRecordListener,context: Context){

        this.audioSampleRate = audioSampleRate
        this.audioBitRate = audioBitRate
        this.listener = listener
        this.context = context
    }

    fun forceQuit(){
        quit.set(true)
    }

    override fun run() {
        try {
            try {
                prepareVideoEncoder()
                prepareAudioEncoder()

            } catch (e: IOException) {
                throw  RuntimeException(e)
            }



            var minBufferSize = AudioRecord.getMinBufferSize(audioSampleRate, AUDIO_CHANNEL_CONFIG, AUDIO_RECORD_FORMAT)
            audioRecord = AudioRecord(MediaRecorder.AudioSource.MIC, audioSampleRate, AUDIO_CHANNEL_CONFIG, AUDIO_RECORD_FORMAT, bufferSize )
            audioBuffer = ByteArray(bufferSize)
            if (audioRecord != null && audioRecord?.state == AudioRecord.STATE_INITIALIZED) {
                audioRecord?.startRecording()
            }
            if(Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN){
                noiseSuspender = NoiseSuppressor.create(audioRecord!!.audioSessionId)
            }
            recordAudio()
         //   recordVideo()


        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            release()
        }
    }


    private  fun recordVideo(){
        while (!quit.get()) {

            var generateIndex = 0
            var  mTrackIndex=-1;
            var inputBufIndex = mediaCodec!!.dequeueInputBuffer(TIMEOUT_USEC);
            var ptsUsec = computePresentationTime(generateIndex);
            if (inputBufIndex!! >= 0) {
                var image = loadBitmapFromView();
              //  image = Bitmap.createScaledBitmap(image, mWidth, mHeight, false);
                var input = getNV21(mWidth,mHeight , image);
              var inputBuffer = mediaCodec?.inputBuffers!![inputBufIndex]
                inputBuffer?.clear();
                inputBuffer?.put(input);
                mediaCodec?.queueInputBuffer(inputBufIndex!!, 0, input.size, ptsUsec, 0);
                generateIndex++;
                encodeToVideo()
            }


        }
    }

    private fun loadBitmapFromView(): Bitmap {
       return  BitmapFactory.decodeResource(context.resources,R.drawable.live)
    }


    private  fun recordAudio(){
      Log.i("AudioStreaming","audioMicStarted")
        while (!quit.get()){
            if(startTime == 0L ){
                startTime = audioBufferInfo.presentationTimeUs /1000
            }
            var timestamp = audioBufferInfo.presentationTimeUs /1000 - startTime


            var generateIndex = 0
            var  mTrackIndex=-1;
            var inputBufIndex = mediaCodec!!.dequeueInputBuffer(TIMEOUT_USEC);
            var ptsUsec = computePresentationTime(generateIndex);
           // if (inputBufIndex!! >= 0) {
                var image = loadBitmapFromView();
                //  image = Bitmap.createScaledBitmap(image, mWidth, mHeight, false);
                var input = getNV21(mWidth,mHeight , image);
                var inputBuffer = mediaCodec?.inputBuffers!![inputBufIndex]
                inputBuffer?.clear();
                inputBuffer?.put(input);
                mediaCodec?.queueInputBuffer(inputBufIndex!!, 0, input.size, ptsUsec, 0);
                generateIndex++;
                encodeToVideo()
          //  }




            if (audioRecord != null && audioRecord?.state == AudioRecord.STATE_INITIALIZED && audioEncoder != null) {
                // Read audio data from recorder then write to encoder


                    val audioInputBufferIndex = audioEncoder?.dequeueInputBuffer(10000)

                    if (audioInputBufferIndex!! >= 0) {
                      //  val inputBuffers = audioEncoder.inputBuffers[audioInputBufferIndex]
                        val inputBuffer = audioEncoder!!.inputBuffers[audioInputBufferIndex]
                              inputBuffer.clear()

                        val size = audioRecord?.read(audioBuffer, 0, audioBuffer.size)
                        inputBuffer.position(0)
                        inputBuffer.put(audioBuffer,0,audioBuffer.size)
            if(size ==AudioRecord.ERROR_BAD_VALUE || size ==AudioRecord.ERROR_INVALID_OPERATION){
                Log.e(TAG,"bad input value")
            }else{
                audioEncoder!!.queueInputBuffer(audioInputBufferIndex, 0, size!!, System.nanoTime()/1000, 0)
            }


                    }


                val audioOutputBufferIndex = audioEncoder!!.dequeueOutputBuffer(audioBufferInfo, 10000)
                if (audioOutputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    Log.d(TAG, "Audio Format changed " + audioEncoder!!.outputFormat)
                    //resetAudioOutputFormat();
                } else if (audioOutputBufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
                    // Ignore
                } else if (audioOutputBufferIndex >= 0) {
                    encodeToAudioTrack(timestamp, audioOutputBufferIndex)

                    audioEncoder!!.releaseOutputBuffer(audioOutputBufferIndex, false)
                }
            }
        }
    }


    private fun encodeToVideo(){

        var encoderStatus = mediaCodec?.dequeueOutputBuffer(mBufferInfo, TIMEOUT_USEC);

        if (encoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED)
        {
            // not expected for an encoder
      //      encoderOutputBuffers = mediaCodec?.getOutputBuffers();
        }else if(encoderStatus!! <0){
            Log.d("audioSteraming 2 ","video encoding queue is empty")
        }
        else {
            if (mBufferInfo.size != 0) {
                var isHeader = (mBufferInfo.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0;

                var isKeyframe = (mBufferInfo.flags and MediaCodec.BUFFER_FLAG_KEY_FRAME) != 0 && !isHeader;

                val timestamp: Long = mBufferInfo.presentationTimeUs;

                var encodedData = mediaCodec?.getOutputBuffers()!![encoderStatus!!];
                encodedData.position(mBufferInfo.offset);
                encodedData.limit(mBufferInfo.offset + mBufferInfo.size);

                // Extract video data
                var b = ByteArray(mBufferInfo.size);
                encodedData.get(b);

                listener?.OnReceiveVideoRecordData(mBufferInfo,isHeader,timestamp,b,isKeyframe)
             //   mediaCodec?.releaseOutputBuffer(encoderStatus, false);

            }

        }
    }
   private fun encodeToAudioTrack(timestamp:Long,index:Int){
        var encodedataArray = audioEncoder!!.outputBuffers
        var encodedData = encodedataArray[index]

        if( (audioBufferInfo.flags and  MediaCodec.BUFFER_FLAG_CODEC_CONFIG)!=0 ){
            encodedData.position(audioBufferInfo.offset)
            encodedData.limit(audioBufferInfo.offset + audioBufferInfo.size)
            val bytes = ByteArray(encodedData.remaining())
            encodedData.get(bytes)
            if(listener !=null){
                listener.OnReceiveAudioRecordData(audioBufferInfo,true,timestamp,bytes,AUDIO_CHANNEL_COUNT,1)
            }
            audioBufferInfo.size=0
        }

        if(audioBufferInfo.size>0){
            encodedData.position(audioBufferInfo.offset)
            encodedData.limit(audioBufferInfo.offset + audioBufferInfo.size)
            val bytes = ByteArray(encodedData.remaining())
            encodedData.get(bytes)
            if(listener!=null){
                listener.OnReceiveAudioRecordData(audioBufferInfo,false, timestamp,bytes,AUDIO_CHANNEL_COUNT,1)
            }
        }
    }

    private  fun prepareVideoEncoder(){


        mediaCodec = MediaCodec.createEncoderByType(MIME_TYPE)
      var  mediaFormat = MediaFormat.createVideoFormat(MIME_TYPE, mWidth, mHeight)
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, BIT_RATE)
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, FRAME_RATE)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar)
        } else {
            mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible)
        }
        //2130708361, 2135033992, 21
        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, I_FRAME_INTERVAL)


        mediaCodec?.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        mediaCodec?.start()
    }

  private  fun prepareAudioEncoder(){
        var bufferSize  = AudioRecord.getMinBufferSize(audioSampleRate,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT)*2
        val format = MediaFormat.createAudioFormat(AUDIO_MIME_TYPE, audioSampleRate, AUDIO_CHANNEL_COUNT)
        format.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC)
        format.setInteger(MediaFormat.KEY_BIT_RATE, audioBitRate)
        format.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, bufferSize)

        audioEncoder = MediaCodec.createEncoderByType(AUDIO_MIME_TYPE)
        audioEncoder!!.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        audioEncoder!!.start()
    }
   private fun release(){

       if (noiseSuspender != null){
           noiseSuspender?.release()
       }
        if(audioEncoder !=null ){
            try{
                audioEncoder?.stop()

            }catch (e:IllegalStateException){
                        e.printStackTrace()
            }finally {
                audioEncoder?.release()
                audioEncoder = null
            }
        }
        if(audioRecord !=null){
            audioRecord?.stop()
            audioRecord = null
        }

        Log.i("AudioStreaming","AudioStreaming Stopped!!!!!")


    }

    private fun computePresentationTime(frameIndex: Int): Long {
        return 132 + frameIndex * 1000000 / FRAME_RATE * 1L
    }

    private fun getNV21(inputWidth: Int, inputHeight: Int, scaled: Bitmap): ByteArray {

        val argb = IntArray(inputWidth * inputHeight)

        scaled.getPixels(argb, 0, inputWidth, 0, 0, inputWidth, inputHeight)

        val yuv = ByteArray(inputWidth * inputHeight * 3 / 2)
        encodeYUV420SP(yuv, argb, inputWidth, inputHeight)

        scaled.recycle()

        return yuv
    }

    private fun encodeYUV420SP(yuv420sp: ByteArray, argb: IntArray, width: Int, height: Int) {
        val frameSize = width * height

        var yIndex = 0
        var uvIndex = frameSize

        var a: Int
        var R: Int
        var G: Int
        var B: Int
        var Y: Int
        var U: Int
        var V: Int
        var index = 0
        for (j in 0 until height) {
            for (i in 0 until width) {

                a = argb[index] and -0x1000000 shr 24 // a is not used obviously
                R = argb[index] and 0xff0000 shr 16
                G = argb[index] and 0xff00 shr 8
                B = argb[index] and 0xff shr 0


                Y = (66 * R + 129 * G + 25 * B + 128 shr 8) + 16
                U = (-38 * R - 74 * G + 112 * B + 128 shr 8) + 128
                V = (112 * R - 94 * G - 18 * B + 128 shr 8) + 128


                yuv420sp[yIndex++] = (if (Y < 0) 0 else if (Y > 255) 255 else Y).toByte()
                if (j % 2 == 0 && index % 2 == 0) {
                    yuv420sp[uvIndex++] = (if (U < 0) 0 else if (U > 255) 255 else U).toByte()
                    yuv420sp[uvIndex++] = (if (V < 0) 0 else if (V > 255) 255 else V).toByte()

                }

                index++
            }
        }
    }


    interface ScreenRecordListener {
        fun OnReceiveVideoRecordData(bufferInfo: MediaCodec.BufferInfo, isHeader: Boolean, timestamp: Long, data: ByteArray,  iKeyFrane: Boolean)
       fun OnReceiveAudioRecordData(bufferInfo: MediaCodec.BufferInfo, isHeader: Boolean, timestamp: Long, data: ByteArray, numberOfChannel: Int, sampleSizeIndex: Int)
    }
}