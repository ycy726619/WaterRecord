# WaterRecord
 Camera2全屏预览,最佳分辨率录制及给使用ffmpeg给本地视频添加图片水印

使用
layout
   
  
	<com.example.myapplication.util.RecordCameraView
        android:id="@+id/recordCameraView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
				
activity onCreate


	RecordCameraView recordCameraView = findViewById(R.id.recordCameraView);
	
activity onPause

 
        
    recordCameraView.onPause();
		
		
activity onResume
   
	 recordCameraView.onResume();
    
开始录制
   
	 recordCameraView.startRecordingVideo();
	 
	 
结束录制

	 recordCameraView.stopRecordingVideo();
	 
添加水印

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
		
详情请参考Demo
