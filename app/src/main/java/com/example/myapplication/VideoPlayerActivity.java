package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.dueeeke.videoplayer.player.VideoView;

/**
 * @author: ycy
 * @date: 2020/11/8
 */
public class VideoPlayerActivity extends AppCompatActivity {
    VideoView videoView;
    ImageView back;
    String videoPath = "";
    public static void start(Context context,String videoPath){
        Intent intent = new Intent(context,VideoPlayerActivity.class);
        intent.putExtra("videoPath",videoPath);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        videoView = findViewById(R.id.videoView);
        back = findViewById(R.id.back);
        if (getIntent().hasExtra("videoPath")) {
            videoPath = getIntent().getStringExtra("videoPath");
            videoView.setUrl(videoPath);
            videoView.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.release();
    }
}
