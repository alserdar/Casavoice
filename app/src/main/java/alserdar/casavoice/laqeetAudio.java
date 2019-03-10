package alserdar.casavoice;

import android.annotation.SuppressLint;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;

import com.octiplex.android.rtmp.AACAudioFrame;
import com.octiplex.android.rtmp.AACAudioHeader;
import com.octiplex.android.rtmp.RtmpConnectionListener;
import com.octiplex.android.rtmp.RtmpMuxer;
import com.octiplex.android.rtmp.Time;

import java.io.FileInputStream;
import java.io.IOException;

public class laqeetAudio {



    //=========================== record rtmpMuxer
    static class AudioRecordingActivity  implements Runnable, RtmpConnectionListener
    {
        /**
         * Instance of media recorder used to record audio
         */
        private MediaRecorder mediaRecorder;
        /**
         * File descriptors used to extract data from the {@link #mediaRecorder}
         */
        private ParcelFileDescriptor[] fileDescriptors;
        /**
         * Thread that will handle aac parsing using {@link #fileDescriptors} output
         */
        private Thread aacParsingThread;
        /**
         * Has AAC header been send yet
         */
        private boolean headerSent;
        /**
         * Already started rtmpMuxer.
         */
        private RtmpMuxer rtmpMuxer;


        private long startTime;

        @SuppressLint("StaticFieldLeak")
        private void initMuxer()
        {
            rtmpMuxer = new RtmpMuxer("35.185.85.100", 1935, new Time()
            {
                @Override
                public long getCurrentTimestamp()
                {
                    return System.currentTimeMillis();
                }
            });
            rtmpMuxer.setConnectTimeout(10000);
            // Always call start method from a background thread.
            new AsyncTask<Void, Void, Void>()
            {
                @Override
                protected Void doInBackground(Void... params)
                {
                    rtmpMuxer.start(AudioRecordingActivity.this, "live", "rtmp://35.185.85.100:1935", null);

                    return null;
                }
            }.execute();
        }

        @SuppressLint("StaticFieldLeak")
        @Override
        public void onConnected()
        {

            // Muxer is connected to the RTMP server, you can create a stream to publish data
            new AsyncTask<Void, Void, Void>()
            {
                @Override
                protected Void doInBackground(Void... params)
                {
                    try {
                        rtmpMuxer.createStream("demo");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        }

        @Override
        public void onReadyToPublish()
        {
            // Muxer is connected to the server and ready to receive data
            try {
                configure();
            } catch (IOException e) {
                e.printStackTrace();
            }
            startAudio();
        }

        @Override
        public void onConnectionError(IOException e)
        {
            e.printStackTrace();
            // Error while connecting to the server
        }

        public void configure() throws IOException
        {
            fileDescriptors = ParcelFileDescriptor.createPipe();
            aacParsingThread = new Thread(this);
            headerSent = false;

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // If you want to use the camera's microphone
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setOutputFile(fileDescriptors[1].getFileDescriptor());
            mediaRecorder.setAudioSamplingRate(44100);
            mediaRecorder.setAudioChannels(2);
            mediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                @Override
                public void onError(MediaRecorder mediaRecorder, int i, int i1) {
                    Log.e("mdiaRecorder","error code" + i + ":" +i1);
                }
            });
            mediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                @Override
                public void onInfo(MediaRecorder mediaRecorder, int i, int i1) {
                    Log.i("mediaRecorder","info" + i + ":" + i1);
                }
            });
            mediaRecorder.prepare();


        }

        public void startAudio()
        {
            mediaRecorder.start();
            startTime = SystemClock.elapsedRealtime();
            aacParsingThread.start();
        }

        @SuppressLint("StaticFieldLeak")
        @Override
        public void run()
        {



            try
            {

                final FileInputStream is  = new FileInputStream(fileDescriptors[0].getFileDescriptor());

                while (true)
                {
                    // TODO parse AAC: This sample doesn't provide AAC extracting complete method since it's not the purpose of this repository.

                    if( !headerSent )
                    {
                        // TODO extract header data

                        int numberOfChannel;
                        int sampleSizeIndex;

                        rtmpMuxer.setAudioHeader(new AACAudioHeader()
                        {
                            @NonNull
                            @Override
                            public byte[] getData()
                            {
                                byte[] aacHeader = new byte[9];
                                try {
                                    is.read(aacHeader);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return aacHeader;
                            }

                            @Override
                            public int getNumberOfChannels()
                            {
                                return 2;
                            }

                            @Override
                            public int getSampleSizeIndex()
                            {
                                return 4;
                            }
                        });

                        headerSent = true;
                    }


                    // TODO extract frame data

                    final byte[] aacData = new byte[1024];
                    final long timestamp;
                    try
                    {
                        rtmpMuxer.postAudio(new AACAudioFrame()
                        {
                            @Override
                            public long getTimestamp()
                            {
                                return SystemClock.elapsedRealtime() - startTime;
                            }

                            @NonNull
                            @Override
                            public byte[] getData()
                            {

                                try {
                                    is.read(aacData);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return aacData;
                            }
                        });
                    }
                    catch(IOException e)
                    {
                        // An error occured while sending the audio frame to the server
                    }


                }
            }
            catch (Exception e)
            {
                // TODO handle error
                e.printStackTrace();
            }
            finally
            {

            }
        }
    }
}
