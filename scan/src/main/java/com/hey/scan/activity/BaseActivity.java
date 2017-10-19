package com.hey.scan.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.DisplayMetrics;
import android.view.View;


/**
 * Created by Hey on 16/12/15.
 */


public abstract  class BaseActivity extends AppCompatActivity implements View.OnClickListener{


    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //锁定竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        init();
    }



    @Override
    protected void onResume() {
        super.onResume();
        ActivityManager.getInstance().addActivity(this);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().removeActivity(this);
    }


    protected void init(){
        initView();
        initData();
        initEvent();
    }



    
    protected  abstract  void initView();
    protected  abstract  void initData();
    protected  abstract  void initEvent();



    public void Navigation(Class activity){
        Intent intent=new Intent(this,activity);
        startActivity(intent);
    }

    public void Navigation(Intent intent){
        startActivity(intent);
    }





    /**
     * 获取屏幕的宽度(px)
     * @return width
     */
    public int getScreenWidth(){
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 获取屏幕的高度(px)
     * @return height
     */
    public int getScreenHeight(){
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * 获取网络连接状态
     * @return
     */
    public int getNetWorkState() {
        ConnectivityManager manager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager.getActiveNetworkInfo()!=null&&manager.getActiveNetworkInfo().isAvailable()) {
            return manager.getActiveNetworkInfo().getType();
        }
        return 0;
    }



    protected <T extends View> T f(int id) {
        return (T) super.findViewById(id);
    }



}
