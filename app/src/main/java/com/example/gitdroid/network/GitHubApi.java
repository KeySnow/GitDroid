package com.example.gitdroid.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Retrofit能将标准的reset接口，用java接口来描述（通过注解）
 * 通过Retrofit的create方法，去创建Call模型
 * Created by 93432 on 2016/7/28.
 */
public interface GitHubApi {

    // 获取square公司retrofit仓库的所有参与者信息
    // 请求方式:Get
    // 请求路径:repos/square/retrofit/contributors
    // 请求参数：无
    // 请求头：无(其实OKHTTP内部会帮我们做一些基本数据补全)
    // 最终首次构建完成了一个Call模型
    @GET("repos/square/retrofit/contributors")
    Call<ResponseBody> getRetrofitContributors();
}
