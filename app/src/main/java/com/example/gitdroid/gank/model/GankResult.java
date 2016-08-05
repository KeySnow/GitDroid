package com.example.gitdroid.gank.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 93432 on 2016/8/5.
 */
@SuppressWarnings("unused")
public class GankResult {

    private List<String> category;

    private boolean error;

    private Results results;

    public List<String> getCategory() {
        return category;
    }

    public boolean isError() {
        return error;
    }

    public Results getResults() {
        return results;
    }

    public static class Results {

        @SerializedName("Android")
        private List<GankItem> androidItems;

        public List<GankItem> getAndroidItems() {
            return androidItems;
        }
    }
}
