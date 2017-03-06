package com.mediaplayer.demo;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by ljz on 17-2-9.
 */

public class VideoActivity extends Activity {

    private static final String TAGG = VideoActivity.class.getSimpleName();
    private Button mButtonsurface;
    private SurfaceView surface;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        surface = (SurfaceView) findViewById(R.id.surfaceview);
        Button start = (Button) findViewById(R.id.start);
        Button pause = (Button) findViewById(R.id.pause);
        final TextView subtitleView = (TextView) findViewById(R.id.subtitle);

        Intent intent = getIntent();
        final String videopath = intent.getStringExtra("videopath");
        if (TextUtils.isEmpty(videopath)) {
            finish();
            return;
        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                }
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(videopath);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置播放声音类型
                    mediaPlayer.setDisplay(surface.getHolder());//设置在surfaceView上播放
                    mediaPlayer.prepare();
                    mediaPlayer.setOnTimedTextListener(new MediaPlayer.OnTimedTextListener() {
                        @Override
                        public void onTimedText(MediaPlayer mp, TimedText text) {
                            if (text != null) {
                                Log.d(TAGG, "onTimedText:  " + text.getText());
                                subtitleView.setText(text.getText());
                            }else{
                                subtitleView.setText(" ");
                            }
                        }
                    });
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();//准备需要一段时间，所以用监听

                            android.media.MediaPlayer.TrackInfo[] trackInfo = mp.getTrackInfo();
                            if (trackInfo != null) {
                                // Display the film contains all the subtitles number
                                if (trackInfo != null) {
                                    for (int i=0;i<trackInfo.length;i++) {
                                        int type = trackInfo[i].getTrackType();
                                        Log.i(TAGG,"InnerSub track type:"+String.valueOf(type));
                                        if(type == android.media.MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT) {
                                            mp.selectTrack(i);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
