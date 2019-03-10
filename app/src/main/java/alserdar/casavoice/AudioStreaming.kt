package alserdar.casavoice

import android.media.*
import android.media.audiofx.NoiseSuppressor
import android.os.Build
import android.util.Log
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean

class AudioStreaming:Thread{

    val TAG = "AudioStreaming"


    //parameters for encoder
    val AUDIO_MIME_TYPE = MediaFormat.MIMETYPE_AUDIO_AAC
    val AUDIO_CHANNEL_COUNT =1
    val AUDIO_MAX_INPUT_SIZE=8820
    val AUDIO_RECORD_FORMAT= AudioFormat.ENCODING_PCM_16BIT
    val AUDIO_CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO



    val quit = AtomicBoolean(false)
    var bufferInfo = MediaCodec.BufferInfo()

    var listener: AudioStreaming.ScreenRecordListener

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
    constructor( audioSampleRate:Int,
                audioBitRate:Int,  listener: AudioStreaming.ScreenRecordListener){

        this.audioSampleRate = audioSampleRate
        this.audioBitRate = audioBitRate
        this.listener = listener

    }

    fun forceQuit(){
        quit.set(true)
    }

    override fun run() {
        try {
            try {
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
        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            release()
        }
    }

  private  fun recordAudio(){
      Log.i("AudioStreaming","audioMicStarted")
        while (!quit.get()){
            if(startTime == 0L ){
                startTime = bufferInfo.presentationTimeUs /1000
            }
            var timestamp = bufferInfo.presentationTimeUs /1000 - startTime


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


    interface ScreenRecordListener {
       fun OnReceiveAudioRecordData(bufferInfo: MediaCodec.BufferInfo, isHeader: Boolean, timestamp: Long, data: ByteArray, numberOfChannel: Int, sampleSizeIndex: Int)
    }
}