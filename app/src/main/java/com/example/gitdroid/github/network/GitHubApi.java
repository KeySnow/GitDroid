package com.example.gitdroid.github.network;

import com.example.gitdroid.github.hotrepo.repolist.modle.RepoResult;
import com.example.gitdroid.github.hotuser.HotUserResult;
import com.example.gitdroid.github.login.modle.AccessTokenResult;
import com.example.gitdroid.github.login.modle.User;
import com.example.gitdroid.github.repoinfo.RepoContentResult;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    /**
     * 获取用户信息
     * @return
     */
    @GET("user")
    Call<User> getUserInfo();

    /**
     * 获取仓库
     * @param query 查询参数（language:java）
     * @param pageId 查询页参数（从1开始）
     * @return
     */
    @GET("/search/repositories")
    Call<RepoResult> searchRepo(@Query("q") String query, @Query("page") int pageId);

    /**
     * 获取readme
     * @param owner 仓库拥有者
     * @param repo 仓库名称
     * @return 仓库的readme页面内容，将是markdown格式且做了Base64处理
     */
    @GET("/repos/{owner}/{repo}/readme")
    Call<RepoContentResult> getReadme(@Path("owner") String owner, @Path("repo") String repo);

    /**
     * 获取一个markdown内容对应的html页面
     * @param body 请求体，内容来自getReadme后的RepoContentResult
     * @return
     */
    @Headers({"Content-Type: text/plain"})
    @POST("/markdown/raw")
    Call<ResponseBody> markDown(@Body RequestBody body);

    /**
     * 获取用户列表
     * @param query 查询参数(followers:>1000)
     * @param pageId 查询页数据(从1开始)
     * @return
     */
    @GET("/search/users")
    Call<HotUserResult> searchUsers(@Query("q") String query, @Query("page") int pageId);

    /**
     * 获取用户
     * @param login 请求路径，用户名
     * @return
     */
    @GET("/users/{login}")
    Call<User> getUser(@Path("login") String login);
}
