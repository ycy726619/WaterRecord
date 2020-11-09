package com.example.myapplication.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;

import com.coder.ffmpeg.call.IFFmpegCallBack;
import com.coder.ffmpeg.jni.FFmpegCommand;
import com.example.myapplication.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author: ycy
 * @date: 2020/11/8
 */
public class WaterMarkUtils {
    static String WATER_NAME = "water_mark.png";
    static String WATER_MARK_PATH = Environment.getExternalStoragePublicDirectory("") + "/waterMark/";

    public static void addWaterMark(final String srcFile, final String outPutFile, final IFFmpegCallBack callBack) {
        FFmpegCommand.runAsync(getWaterCommands(srcFile, getWaterPath(), outPutFile), callBack);
    }

    public static String getWaterPath() {
        return WATER_MARK_PATH + WATER_NAME;
    }

    public static String getVideoOutPutFilePath(Context context) {
        final File dir = context.getExternalFilesDir(null);
        return (dir == null ? "" : (dir.getAbsolutePath() + "/output"))
                + System.currentTimeMillis() + ".mp4";
    }

    @SuppressLint("ResourceType")
    public static void initWaterFile(Context context) {
        Resources r = context.getResources();
        InputStream is = r.openRawResource(R.drawable.water_mark);
        BitmapDrawable bmpDraw = new BitmapDrawable(is);
        Bitmap bmp = bmpDraw.getBitmap();
        File dir = new File(WATER_MARK_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(WATER_MARK_PATH, WATER_NAME);
        try {
            if (file.createNewFile()) {
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 添加水印ffmpeg命令
     * @param videoUrl
     * @param imageUrl
     * @param outputUrl
     * @return
     */
    private static String[] getWaterCommands(String videoUrl, String imageUrl, String outputUrl) {
        String[] commands = new String[9];
        commands[0] = "ffmpeg";
        //输入
        commands[1] = "-i";
        commands[2] = videoUrl;
        //水印
        commands[3] = "-i";
        commands[4] = imageUrl;
        commands[5] = "-filter_complex";
        //水印位置  main_w-overlay_w 视频宽度 | main_h-overlay_h 视频高度
        commands[6] = "overlay=20:(main_h-overlay_h)-20";
        //覆盖输出
        //直接覆盖输出文件
        commands[7] = "-y";
        //输出文件
        commands[8] = outputUrl;
        return commands;
    }

}
