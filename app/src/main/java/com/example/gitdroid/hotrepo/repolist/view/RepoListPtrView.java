package com.example.gitdroid.hotrepo.repolist.view;

import com.example.gitdroid.hotrepo.repolist.modle.Repo;

import java.util.List;

/**
 * Created by 93432 on 2016/7/28.
 */
public interface RepoListPtrView {

    void showContentView();

    void showErrorView(String errorMsg);

    void showEmptyView();

    void showMessage(String mgs);

    void stopRefresh();

    void refreshData(List<Repo> datas);
}
