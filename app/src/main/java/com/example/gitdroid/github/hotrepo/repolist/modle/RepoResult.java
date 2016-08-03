package com.example.gitdroid.github.hotrepo.repolist.modle;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 搜索仓库响应结果
 * Created by 93432 on 2016/8/1.
 */
public class RepoResult {
//    "total_count": 2074901,
//            "incomplete_results": false,
//            "items": [{}]
    @SerializedName("total_count")
    private int totalCount;

    @SerializedName("incomplete_results")
    private boolean incompleteResults;

    @SerializedName("items")
    private List<Repo> repoList;

    public int getTotalCount() {
        return totalCount;
    }

    public boolean isIncompleteResults() {
        return incompleteResults;
    }

    public List<Repo> getRepoList() {
        return repoList;
    }
}
