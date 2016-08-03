package com.example.gitdroid.github.hotrepo.repolist.view;

import com.example.gitdroid.github.hotrepo.repolist.modle.Repo;

import java.util.List;

/**
 * 加载更多的视图抽象
 * Created by 93432 on 2016/7/28.
 */
public interface RepoListLoadMoreView {

    void showLoadMoreLoading();

    void hideLoadMore();

    void showLoadMoreError(String errorMsg);

    void addMoreData(List<Repo> datas);
}
