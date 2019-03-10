package alserdar.casavoice;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.media.AudioAttributesCompat;
import android.util.Log;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ExoRadioPlayerFactory {


    private static ConcurrentHashMap<String,ExoPlayer> playersMap = new ConcurrentHashMap<>(5);
private static Set<String> set = Collections.synchronizedSet(new HashSet<String>());

    private ExoRadioPlayerFactory(){

    }

    public static @Nullable ExoPlayer createPlayer(Uri streamUrl)
    {
        if (set.contains(streamUrl.toString())){
            return null;
        }

        set.add(streamUrl.toString());


        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(getApplicationContext());
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter(); //Provides estimates of the currently available bandwidth.
        AdaptiveTrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        DefaultLoadControl loadControl = new DefaultLoadControl();

        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
        player.addListener(new Player.EventListener() {



            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onSeekProcessed() {

            }


            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }
        });


        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), "casavoice");
        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        Handler mainHandler = new Handler();
        ExtractorMediaSource mediaSource = new ExtractorMediaSource(streamUrl,
                dataSourceFactory,
                extractorsFactory,
                mainHandler,
                null);
        player.setRepeatMode(ExoPlayer.REPEAT_MODE_OFF);
        player.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributesCompat.CONTENT_TYPE_SPEECH).setUsage(AudioAttributesCompat.USAGE_VOICE_COMMUNICATION).setFlags(AudioAttributesCompat.FLAG_AUDIBILITY_ENFORCED).build());

        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
        playersMap.put(streamUrl.toString(),player);
        return player;
    }


    public static @Nullable SimpleExoPlayer createHLSExoPlayer(final Context context, final Uri streamUri){

        if (set.contains(streamUri.toString())){
            return null;
        }

        set.add(streamUri.toString());

        Handler handler = new Handler();
        BandwidthMeter meter = new DefaultBandwidthMeter();
        TrackSelection.Factory audioTrackSelectionFacory = new AdaptiveTrackSelection.Factory(meter);
        TrackSelector trackSelection = new DefaultTrackSelector(audioTrackSelectionFacory);
        LoadControl loadControl = new DefaultLoadControl();
        SimpleExoPlayer exoPlayer = ExoPlayerFactory.newSimpleInstance(context,trackSelection);
      RtmpDataSourceFactory rtmpDataSourceFactory = new RtmpDataSourceFactory();

        MediaSource  mediaSource = new ExtractorMediaSource.Factory(rtmpDataSourceFactory).createMediaSource(streamUri);
                    exoPlayer.addListener(new Player.EventListener() {
                        @Override
                        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

                        }

                        @Override
                        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                        }

                        @Override
                        public void onLoadingChanged(boolean isLoading) {
                            Log.d("ExoPlayer","loadingState"+ isLoading);
                        }

                        @Override
                        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                                  Log.d("EXOPlayerFactory","exo player is running==>" + streamUri);

                        }

                        @Override
                        public void onRepeatModeChanged(int repeatMode) {

                        }

                        @Override
                        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                        }

                        @Override
                        public void onPlayerError(ExoPlaybackException error) {
                                                    error.printStackTrace();
                        }

                        @Override
                        public void onPositionDiscontinuity(int reason) {

                        }

                        @Override
                        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                        }

                        @Override
                        public void onSeekProcessed() {

                        }
                    });
        exoPlayer.setRepeatMode(ExoPlayer.REPEAT_MODE_OFF);
        exoPlayer.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributesCompat.CONTENT_TYPE_SPEECH).setUsage(AudioAttributesCompat.USAGE_VOICE_COMMUNICATION).setFlags(AudioAttributesCompat.FLAG_AUDIBILITY_ENFORCED).build());

        exoPlayer.prepare(mediaSource);
        playersMap.put(streamUri.toString(),exoPlayer);
        exoPlayer.setPlayWhenReady(true);
        return exoPlayer;
    }

    public static  void stopStream (String streamUri)
    {
            if(playersMap.containsKey(streamUri)){
                ExoPlayer player = playersMap.get(streamUri);
                if(player == null){
                    throw new RuntimeException("Player cannot be null");
                }
                player.stop();
                player.release();
                playersMap.remove(streamUri,player);
            }
    }


    public  static boolean hasOnlinePlayer(){
        return playersMap.size() >0;
    }
    public static void releaseAll(){
      for (ExoPlayer p:  playersMap.values()){
          p.stop();
          p.release();
      }
      playersMap.clear();
    }

}
