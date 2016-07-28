package com.example.gitdroid.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by 93432 on 2016/7/28.
 */
public class GitHubClient {

    private GitHubApi gitHubApi;

    public GitHubClient(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(okHttpClient)
                .build();
        //构建API
        gitHubApi = retrofit.create(GitHubApi.class);
    }

    public GitHubApi getGitHubApi(){
        return gitHubApi;
    }
}
