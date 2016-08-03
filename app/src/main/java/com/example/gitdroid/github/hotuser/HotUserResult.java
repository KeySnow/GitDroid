package com.example.gitdroid.github.hotuser;

import com.example.gitdroid.github.login.modle.User;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 93432 on 2016/8/3.
 */
public class HotUserResult {

//    "total_count": 603,
//            "incomplete_results": false,
//            "items": [

    @SerializedName("total_count")
    private int totalCount;

    @SerializedName("incomplete_results")
    private boolean incompleteResults;

    @SerializedName("items")
    private List<User> userList;

    public int getTotalCount() {
        return totalCount;
    }

    public boolean isIncompleteResults() {
        return incompleteResults;
    }

    public List<User> getUserList() {
        return userList;
    }
}
