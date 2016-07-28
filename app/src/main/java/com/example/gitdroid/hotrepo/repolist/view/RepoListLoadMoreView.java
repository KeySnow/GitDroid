package com.example.gitdroid.hotrepo.repolist.view;

import java.util.List;

/**
 * 加载更多的视图抽象
 * Created by 93432 on 2016/7/28.
 */
public interface RepoListLoadMoreView {

    void showLoadMoreLoading();

    void hideLoadMore();

    void showLoadMoreError(String errorMsg);

    void addMoreData(List<String> datas);
}
