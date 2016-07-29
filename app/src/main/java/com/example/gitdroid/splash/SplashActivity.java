package com.example.gitdroid.splash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.gitdroid.MainActivity;
import com.example.gitdroid.R;
import com.example.gitdroid.commons.ActivityUtils;
import com.example.gitdroid.login.LoginActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页面，第一次启动时进入的页面
 */
public class SplashActivity extends AppCompatActivity {

    private ActivityUtils activityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        activityUtils = new ActivityUtils(this);
        ButterKnife.bind(this);
    }

    //登陆
    @OnClick(R.id.btnLogin)
    public void login(){
        activityUtils.startActivity(LoginActivity.class);
        finish();
    }

    //直接进入
    @OnClick(R.id.btnEnter)
    public void enter(){
        activityUtils.startActivity(MainActivity.class);
        finish();
    }
}
