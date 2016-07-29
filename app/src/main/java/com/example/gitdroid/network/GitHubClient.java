package com.example.gitdroid.network;

import com.example.gitdroid.login.modle.AccessTokenResult;
import com.example.gitdroid.login.modle.User;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;

/**
 * Created by 93432 on 2016/7/28.
 */
public class GitHubClient implements GitHubApi{

    private GitHubApi gitHubApi;

    private static GitHubClient gitHubClient;

    public static GitHubClient getInstance(){
        if (gitHubClient == null){
            gitHubClient = new GitHubClient();
        }
        return gitHubClient;
    }

    private GitHubClient(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //添加token拦截器
                .addInterceptor(new TokenInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(okHttpClient)
                //Gson转换器
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //构建API
        gitHubApi = retrofit.create(GitHubApi.class);
    }

    @Override
    public Call<AccessTokenResult> getOAuthToken(@Field("client_id") String client, @Field("client_secret") String clientSecret, @Field("code") String code) {
        return gitHubApi.getOAuthToken(client,clientSecret,code);
    }

    @Override
    public Call<User> getUserInfo() {
        return gitHubApi.getUserInfo();
    }
}
