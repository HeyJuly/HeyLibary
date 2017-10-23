package com.hey.scan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hey.scan.activity.BaseActivity;
import com.hey.scan.activity.ScanCodeActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        Intent intent=new Intent(this, ScanCodeActivity.class);
        startActivityForResult(intent,01110);
    }

    @Override
    public void onClick(View view) {

    }
}
