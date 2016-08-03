package com.example.gitdroid.github.hotrepo.repolist;

import com.example.gitdroid.github.hotrepo.Language;
import com.example.gitdroid.github.hotrepo.repolist.modle.Repo;
import com.example.gitdroid.github.hotrepo.repolist.modle.RepoResult;
import com.example.gitdroid.github.hotrepo.repolist.view.RepoListView;
import com.example.gitdroid.github.network.GitHubClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 93432 on 2016/7/27.
 */
public class RepoListPresenter {

    //视图的接口
    private RepoListView repoListView;
    private Language language;

    private Call<RepoResult> repoCall;
    private int nextPage = 0;

    public RepoListPresenter(RepoListView repoListView, Language language) {
        this.repoListView = repoListView;
        this.language = language;
    }

    //下拉刷新处理
    public void refresh() {
        //隐藏loadmore
        repoListView.hideLoadMore();
        repoListView.showContentView();
        nextPage = 1;//永远刷新最新数据
        repoCall = GitHubClient.getInstance().searchRepo("language:" + language.getPath(), nextPage);
        repoCall.enqueue(repoCallback);
    }

    //上拉加载更多处理
    public void loadMore() {
        repoListView.showLoadMoreLoading();
        repoCall = GitHubClient.getInstance().searchRepo("language:" + language.getPath(), nextPage);
        repoCall.enqueue(loadMoreCallback);
    }

    private Callback<RepoResult> loadMoreCallback = new Callback<RepoResult>() {
        @Override
        public void onResponse(Call<RepoResult> call, Response<RepoResult> response) {
            //隐藏加载更多视图
            repoListView.hideLoadMore();
            //得到响应结果
            RepoResult repoResult = response.body();
            if (repoResult == null){
                repoListView.showLoadMoreError("结果为空!");
                return;
            }
            //取出当前语言下的所有仓库
            List<Repo> repoList = repoResult.getRepoList();
            repoListView.addMoreData(repoList);
            nextPage++;
        }

        @Override
        public void onFailure(Call<RepoResult> call, Throwable t) {
            //隐藏加载更多视图
            repoListView.hideLoadMore();
            repoListView.showMessage("loadMoreCallback onFailure" + t.getMessage());
        }
    };

    private Callback<RepoResult> repoCallback = new Callback<RepoResult>() {
        @Override
        public void onResponse(Call<RepoResult> call, Response<RepoResult> response) {
            //停止视图刷新
            repoListView.stopRefresh();
            //得到响应结果
            RepoResult repoResult = response.body();
            if (repoResult == null) {
                repoListView.showErrorView("结果为空！");
                return;
            }
            //当前搜索的语言，没有仓库
            if (repoResult.getTotalCount() <= 0) {
                repoListView.refreshData(null);
                repoListView.showEmptyView();
                return;
            }
            //取出当前语言下的所有仓库
            List<Repo> repoList = repoResult.getRepoList();
            repoListView.refreshData(repoList);
            //下拉刷新成功(1)，下一面则更新为2
            nextPage = 2;
        }

        @Override
        public void onFailure(Call<RepoResult> call, Throwable t) {
            //视图停止刷新
            repoListView.stopRefresh();
            repoListView.showMessage("repoCallback onFailure" + t.getMessage());
        }
    };

}


