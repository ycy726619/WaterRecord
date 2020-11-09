package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.myapplication.util.WaterMarkUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * @author: ycy
 * @date: 2020/11/8
 */
public class LaunchActivity extends AppCompatActivity {

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new RxPermissions(LaunchActivity.this)
                .request(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (!aBoolean) {
                        return;
                    }
                    /**
                     * 初始化水印图片
                     */
                    WaterMarkUtils.initWaterFile(this);
                    startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                });

    }
}
