package com.example.gitdroid.github.repoinfo;

/**
 * 获取readme响应结果
 * Created by 93432 on 2016/8/1.
 */
public class RepoContentResult {
    //    {
//        "encoding": "base64",
//            "content": "encoded content ..."
//    }
    private String content;
    private String encoding;

    public String getContent() {
        return content;
    }

    public String getEncoding() {
        return encoding;
    }
}
