package com.example.myapplication;


import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.coder.ffmpeg.call.IFFmpegCallBack;
import com.example.myapplication.util.RecordCameraView;
import com.example.myapplication.util.WaterMarkUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;


/**
 * @author: ycy
 * @date: 2020/11/8
 */
@SuppressLint("HandlerLeak")
public class MainActivity extends AppCompatActivity {

    private final int CANCEL = 100;
    private final int ERROR = 101;
    private final int COMPLETE = 102;
    private final int START = 103;

    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CANCEL:
                case ERROR:

                    Toast.makeText(MainActivity.this,"添加水印异常",Toast.LENGTH_SHORT).show();
                    tvRecord.setText("开始录制");
                    break;
                case COMPLETE:
                    VideoPlayerActivity.start(MainActivity.this, outPutPath);
                    finish();
                    break;
                case START:
                    tvRecord.setText("水印添加中...");
                    break;

            }
        }
    };
    private RecordCameraView recordCameraView;
    private TextView tvRecord;
    private boolean isRecord = false;
    private long record;
    private String outPutPath = "";
    private final String TAG = "MainActivityTAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recordCameraView = findViewById(R.id.recordCameraView);

        tvRecord = findViewById(R.id.tvRecord);
        tvRecord.setOnClickListener(v -> {
            if (System.currentTimeMillis() - record < 2000) {
                Toast.makeText(MainActivity.this, "录制时间太短!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isRecord) {
                isRecord = false;
                recordCameraView.stopRecordingVideo();
                startAddWaterMaker();
            } else {
                isRecord = true;
                record = System.currentTimeMillis();
                recordCameraView.startRecordingVideo();
                tvRecord.setText("停止录制");
            }

        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        recordCameraView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        recordCameraView.onResume();
    }

    private void startAddWaterMaker() {
        handler.sendEmptyMessage(START);
        outPutPath = WaterMarkUtils.getVideoOutPutFilePath(MainActivity.this);
        String path = recordCameraView.getVideoAbsolutePath();
        if (TextUtils.isEmpty(path)) {
            Toast.makeText(MainActivity.this,"视频文件保存失败",Toast.LENGTH_SHORT).show();
            return;
        }
        WaterMarkUtils.addWaterMark(path, outPutPath, new IFFmpegCallBack() {
            @Override
            public void onCancel() {
                Log.e(TAG, "onCancel: ");
                handler.sendEmptyMessage(CANCEL);
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG, "onError: " + t.getMessage());
                handler.sendEmptyMessage(ERROR);
            }

            @Override
            public void onProgress(int progress) {
                Log.e(TAG, "onProgress: progress = " + progress);

            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: ");
                handler.sendEmptyMessage(COMPLETE);
            }

            @Override
            public void onStart() {
                Log.e(TAG, "onStart: ");

            }
        });
    }
}