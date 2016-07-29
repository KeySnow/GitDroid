package com.example.gitdroid.login;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.gitdroid.MainActivity;
import com.example.gitdroid.R;
import com.example.gitdroid.commons.ActivityUtils;
import com.example.gitdroid.network.GitHubApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;

/**
 * 授权登陆
 */
public class LoginActivity extends AppCompatActivity implements LoginView{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.gifImageView)
    GifImageView gifImageView;

    private LoginPresenter presenter;
    private ActivityUtils activityUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter = new LoginPresenter(this);
        initWebView();
    }

    private void initWebView() {
        //删除所有的Cookie，主要为了清除以前的登陆记录
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        //授权登陆URL
        webView.loadUrl(GitHubApi.AUTH_RUL);
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        //主要为了监听进度
        webView.setWebChromeClient(webChromeClient);
        //监听webview（url会刷新的）
        webView.setWebViewClient(webViewClient);
    }

    private WebChromeClient webChromeClient = new WebChromeClient(){
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress >= 100){
                gifImageView.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }
        }
    };

    private WebViewClient webViewClient = new WebViewClient(){
        //每当webview刷新时，此方法将触发（密码输错了时！输对了时！等等情况web页面都会刷新变化的）
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //检测是不是我们的CALL_BACK
            Uri uri = Uri.parse(url);
            if (GitHubApi.CALL_BACK.equals(uri.getScheme())){
                //获取code
                String code = uri.getQueryParameter("code");
                //用code做登陆业务工作
                presenter.login(code);
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    };

    @Override
    public void showProgress() {
        gifImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

    @Override
    public void resetWeb() {
        initWebView();
    }

    @Override
    public void navigateToMain() {
        activityUtils.startActivity(MainActivity.class);
        finish();
    }
}
