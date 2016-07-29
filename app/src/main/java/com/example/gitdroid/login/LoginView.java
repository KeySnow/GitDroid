package com.example.gitdroid.login;

/**
 * Created by 93432 on 2016/7/29.
 */
public interface LoginView {

    //显示进度
    void showProgress();

    void showMessage(String msg);

    //重置WebView
    void resetWeb();

    //导航切换至Main页面
    void navigateToMain();
}
