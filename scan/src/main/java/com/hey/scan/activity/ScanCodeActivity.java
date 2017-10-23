package com.hey.scan.activity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.zxing.Result;
import com.hey.scan.R;
import com.hey.scan.camera.CameraManager;
import com.hey.scan.camera.CaptureActivityHandler;
import com.hey.scan.camera.decoding.InactivityTimer;
import com.hey.scan.tools.RxBeepTool;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class ScanCodeActivity extends BaseActivity implements CaptureActivityHandler.decodeListener {

    private InactivityTimer inactivityTimer;

    //整体根布局
    private RelativeLayout mContainer;
    //扫描框根布局
    private LinearLayout mCropLayout;

    private ImageView mScanLine;

    private CaptureActivityHandler handler;
    private SurfaceView surfaceView;
    private boolean hasSurface;//是否有预览
    private int mCropWidth = 0;//扫描边界的宽度
    private int mCropHeight = 0;//扫描边界的高度

    //闪光灯开启状态
    private boolean mFlashing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);
    }

    @Override
    protected void initView() {
        mContainer=f(R.id.capture_container);
        mCropLayout=f(R.id.capture_crop_layout);
        mScanLine=f(R.id.capture_scan_line);
        surfaceView=f(R.id.capture_preview);
    }

    @Override
    protected void initData() {
        AnimationUpDown(mScanLine);
        inactivityTimer = new InactivityTimer(this);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (!hasSurface) {
                    hasSurface = true;
                    initCamera(holder);
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                hasSurface = false;

            }
        });
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onClick(View view) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (hasSurface) {
            initCamera(surfaceView.getHolder());//Camera初始化
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.getInstance(this).closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    public void clickEvent(View view){
        switch (view.getId()){
                case R.id.top_back:
                finish();
                break;
                case R.id.top_light:
                light();
                break;
        }

    }


    private void initCamera(SurfaceHolder surfaceHolder){
        try {
            CameraManager.getInstance(this).openDriver(surfaceHolder);
            Point point = CameraManager.getInstance(this).getCameraResolution();
            AtomicInteger width = new AtomicInteger(point.y);
            AtomicInteger height = new AtomicInteger(point.x);
            int cropWidth = mCropLayout.getWidth() * width.get() / mContainer.getWidth();
            int cropHeight = mCropLayout.getHeight() * height.get() / mContainer.getHeight();
            setCropWidth(cropWidth);
            setCropHeight(cropHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (handler==null){
            handler = new CaptureActivityHandler(this);
            handler.setDecodeListener(this);
        }
    }



    public void setCropWidth(int cropWidth) {
        mCropWidth = cropWidth;
        CameraManager.FRAME_WIDTH = mCropWidth;

    }


    public void setCropHeight(int cropHeight) {
        this.mCropHeight = cropHeight;
        CameraManager.FRAME_HEIGHT = mCropHeight;
    }


    private void light(){
        if (mFlashing) {
            //关
            CameraManager.getInstance(this).offLight();
            mFlashing=false;
        } else {
            // 开
            CameraManager.getInstance(this).openLight();
            mFlashing=true;
        }
    }


    public void AnimationUpDown(View view ){
        ScaleAnimation animation=new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(1200);
        view.startAnimation(animation);
    }

    @Override
    public void success(Result result) {
        inactivityTimer.onActivity();
        RxBeepTool.playBeep(this, true);
        Intent intent=new Intent();
        intent.putExtra("result",result.getText());
        setResult(RESULT_OK,intent);
        finish();
    }
}
