package com.example.gitdroid.login.modle;

import com.google.gson.annotations.SerializedName;

/**
 * 授权登陆响应结果
 * Created by 93432 on 2016/7/29.
 */
public class AccessTokenResult {
    //    Accept: application/json
//    {
//        "access_token":"e72e16c7e42f292c6912e7710c838347ae178b4a", "scope":"repo,gist",
//            "token_type":"bearer"
//    }
    @SerializedName("access_token")
    private String accessToken;
    private String scope;
    @SerializedName("token_type")
    private String tokenType;

    public String getTokenType() {
        return tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getScope() {
        return scope;
    }
}
