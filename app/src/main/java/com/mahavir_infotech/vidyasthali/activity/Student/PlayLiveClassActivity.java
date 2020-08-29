package com.mahavir_infotech.vidyasthali.activity.Student;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayLiveClassActivity extends BaseActivity {
    String API_Key = "AIzaSyA2Zqd6LuuT62ip4pCnBd2sl1t1S9AfkJE";
    int orientation;
    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.tool_barLayout)
    RelativeLayout toolBarLayout;
    private String videoId = "";
    private YouTubePlayer youTubePlayer;
    private static final int RECOVERY_REQUEST = 1;

    @Override
    protected int getContentResId() {
        return R.layout.activity_play_live_class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        try {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } catch (Exception e) {
        }*/
        titleTxt.setText("Live Class");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String currentString = bundle.getString("URL");
            try{
            if (currentString.contains("v=")) {
                String[] separated = currentString.split("v=");
                videoId = separated[1];
            } else {
                videoId = currentString.substring(currentString.lastIndexOf("/")).replaceAll("/", "");
                ErrorMessage.E("videoId" + videoId);
            }}catch (Exception e){}
        }
        // youTubeView = (YouTubePlayerView) findViewById(R.id.player);
        YouTubePlayerSupportFragment youTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
        //youTubePlayerFragment.initialize(API_Key, this);

       /* int display_mode = getResources().getConfiguration().orientation;
        if (display_mode == Configuration.ORIENTATION_LANDSCAPE) {
            toolBarLayout.setVisibility(View.GONE); //<< this
        } else {
            toolBarLayout.setVisibility(View.VISIBLE);
        }*/

        if (youTubePlayerFragment == null) return;
        youTubePlayerFragment.initialize(API_Key, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                if (!wasRestored) {
                    youTubePlayer = player;
                    //set the player style default
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                    youTubePlayer.setShowFullscreenButton(true);
                    //cue the 1st video by default
                    youTubePlayer.cueVideo(videoId);
                    youTubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                        @Override
                        public void onPlaying() {

                        }

                        @Override
                        public void onPaused() {

                        }

                        @Override
                        public void onStopped() {

                        }

                        @Override
                        public void onBuffering(boolean b) {

                        }

                        @Override
                        public void onSeekTo(int i) {

                        }
                    });
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {

                if (arg1.isUserRecoverableError()) {
                    arg1.getErrorDialog(PlayLiveClassActivity.this, RECOVERY_REQUEST).show();
                } else {
                    String error = String.format("Error a rahi he", arg1.toString());
                    Toast.makeText(PlayLiveClassActivity.this, error, Toast.LENGTH_LONG).show();
                }
            }
        });


        try {
            orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // In landscape
                toolBarLayout.setVisibility(View.GONE);
                // moreBtn.setVisibility(View.VISIBLE);
                ErrorMessage.E("check>>" + "landscape");
            } else {
                // In portrait
                toolBarLayout.setVisibility(View.VISIBLE);
                ErrorMessage.E("check>>" + "portrait");
            }
        } catch (Exception e) {
        }

    }
}
