package com.mahavir_infotech.vidyasthali.activity.Student;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayerBridge;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.Collection;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayVideoActivity extends AppCompatActivity {

    @BindView(R.id.title_txt)
    TextView titleTextTv;
    private String videoId = "";
    YouTubePlayerView youTubePlayerView;
   /*https://www.youtube.com/watch?v=hoNb6HuNmU0*/
    private static final String TAG = "PlayVideoActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        getApplicationContext().getSystemService(Context.WINDOW_SERVICE);*/
        setContentView(R.layout.activity_play_video);

        ButterKnife.bind(this);
        titleTextTv.setText("Video");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String currentString = bundle.getString("URL");
            ErrorMessage.E("currentString"+currentString);
            if (currentString.contains("v=")) {
                String[] separated = currentString.split("v=");
                videoId = separated[1];
            } else  if (currentString.contains("/")){
                videoId = currentString.substring(currentString.lastIndexOf("/")).replaceAll("/", "");
                ErrorMessage.E("videoId" + videoId);
            }else {
                videoId=currentString;
            }
        }
       /* String currentString = "https://www.youtube.com/watch?v=edgycGRGCOI";
        if (currentString.contains("v=")) {
            String[] separated = currentString.split("v=");
            videoId = separated[1];
        } else {
            videoId = currentString.substring(currentString.lastIndexOf("/")).replaceAll("/", "");
            ErrorMessage.E("videoId" + videoId);
        }*/
       youTubePlayerView = findViewById(R.id.youtube_player_view);
       getLifecycle().addObserver(youTubePlayerView);
       youTubePlayerView.setEnableAutomaticInitialization(false);
       youTubePlayerView.enableBackgroundPlayback(true);

       /*youTubePlayerView.isSoundEffectsEnabled();
       youTubePlayerView.getPlayerUiController().showMenuButton(false);
       youTubePlayerView.setMotionEventSplittingEnabled(true);*/

        YouTubePlayerBridge.YouTubePlayerBridgeCallbacks youTubePlayerBridgeCallbacks = new YouTubePlayerBridge.YouTubePlayerBridgeCallbacks() {
            @Override
            public YouTubePlayer getInstance() {
                return null;
            }

            @Override
            public Collection<YouTubePlayerListener> getListeners() {
                return null;
            }

            @Override
            public void onYouTubeIFrameAPIReady() {
            }
        };
       /* try {
            YouTubePlayerBridge youTubePlayerBridge=  new YouTubePlayerBridge(youTubePlayerBridgeCallbacks);
            youTubePlayerBridge.sendPlaybackRateChange(String.valueOf(PlayerConstants.PlaybackRate.RATE_2));
        }catch (Exception e){
            ErrorMessage.E("Exception"+e.toString());
        }*/

        youTubePlayerView.initialize(new YouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                youTubePlayer.cueVideo(videoId, 0);

            }

            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState playerState) {

            }

            @Override
            public void onPlaybackQualityChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackQuality playbackQuality) {

            }

            @Override
            public void onPlaybackRateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackRate playbackRate) {
                ErrorMessage.E("onPlaybackRateChange" +playbackRate.toString());

            }

            @Override
            public void onError(YouTubePlayer youTubePlayer, PlayerConstants.PlayerError playerError) {

            }

            @Override
            public void onCurrentSecond(YouTubePlayer youTubePlayer, float v) {

            }

            @Override
            public void onVideoDuration(YouTubePlayer youTubePlayer, float v) {

            }

            @Override
            public void onVideoLoadedFraction(YouTubePlayer youTubePlayer, float v) {

            }

            @Override
            public void onVideoId(YouTubePlayer youTubePlayer, String s) {

            }

            @Override
            public void onApiChange(YouTubePlayer youTubePlayer) {

            }
        });


        /*Intent intent = YouTubeStandalonePlayer.createVideoIntent(PlayVideoActivity.this, "AIzaSyA2Zqd6LuuT62ip4pCnBd2sl1t1S9AfkJE", videoId, 5, false, false);
        startActivity(intent);*/

        /*youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.cueVideo(videoId, 2);

            }
        });
        youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> {
            // do stuff with it
            youTubePlayer.cueVideo(videoId,2);
        });*/

        youTubePlayerView.addYouTubePlayerListener(new YouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                youTubePlayer.cueVideo(videoId, 0);

            }

            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState playerState) {

            }

            @Override
            public void onPlaybackQualityChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackQuality playbackQuality) {

            }

            @Override
            public void onPlaybackRateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackRate playbackRate) {
                ErrorMessage.E("onPlaybackRateChange" +playbackRate.toString());
                youTubePlayer.cueVideo(videoId, 0);
            }

            @Override
            public void onError(YouTubePlayer youTubePlayer, PlayerConstants.PlayerError playerError) {
                ErrorMessage.E("onError"+playerError.toString());
                youTubePlayer.loadVideo(videoId,0);
            }

            @Override
            public void onCurrentSecond(YouTubePlayer youTubePlayer, float v) {

            }

            @Override
            public void onVideoDuration(YouTubePlayer youTubePlayer, float v) {

            }

            @Override
            public void onVideoLoadedFraction(YouTubePlayer youTubePlayer, float v) {

            }

            @Override
            public void onVideoId(YouTubePlayer youTubePlayer, String s) {
                ErrorMessage.E("onVideoId"+s.toString());
            }

            @Override
            public void onApiChange(YouTubePlayer youTubePlayer) {

            }
        });



    }



}
