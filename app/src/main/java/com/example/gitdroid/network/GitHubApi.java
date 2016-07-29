package com.example.gitdroid.network;

import com.example.gitdroid.login.modle.AccessTokenResult;
import com.example.gitdroid.login.modle.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Retrofit能将标准的reset接口，用java接口来描述（通过注解）
 * 通过Retrofit的create方法，去创建Call模型
 * Created by 93432 on 2016/7/28.
 */
public interface GitHubApi {
    //GitHub开发者，申请时填写的（重定向返回时的一个标记）
    String CALL_BACK = "zhuoxin";
    //GitHub开发者，申请就行
    String CLIENT_ID = "8fb66290e1a9ce5b0421";
    String CLIENT_SECRET = "231a3d58bbd0aa13663050acb1b9883cb5f42f7a";
    //授权时申请的可访问域
    String AUTH_SCOPE = "user,public_repo,repo";
    //授权登陆页面（用webview来加载）
    String AUTH_RUL = "https://github.com/login/oauth/authorize?client_id=" + CLIENT_ID + "&scope=" + AUTH_SCOPE;

    //获取访问令牌API
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("https://github.com/login/oauth/access_token")
    Call<AccessTokenResult> getOAuthToken(
            @Field("client_id") String client,
            @Field("client_secret") String clientSecret,
            @Field("code") String code
    );

    @GET("user")
    Call<User> getUserInfo();
}
