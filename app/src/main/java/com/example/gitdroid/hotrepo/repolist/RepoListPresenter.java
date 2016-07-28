package com.example.gitdroid.hotrepo.repolist;

import android.os.AsyncTask;
import android.util.Log;

import com.example.gitdroid.hotrepo.repolist.view.RepoListView;
import com.example.gitdroid.network.GitHubApi;
import com.example.gitdroid.network.GitHubClient;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 93432 on 2016/7/27.
 */
public class RepoListPresenter {

    private RepoListView repoListView;
    private int count;

    public RepoListPresenter(RepoListView repoListView) {
        this.repoListView = repoListView;
    }

    //下拉刷新处理
    public void refresh() {
        GitHubClient gitHubClient = new GitHubClient();
        GitHubApi gitHubApi = gitHubClient.getGitHubApi();
        Call<ResponseBody> call = gitHubApi.getRetrofitContributors();
        call.enqueue(refreshCallback);//异步执行
    }

    private final Callback<ResponseBody> refreshCallback = new Callback<ResponseBody>() {
        //响应
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            repoListView.stopRefresh();
            //成功200-299
            if (response.isSuccessful()) {
                try {
                    ResponseBody body = response.body();
                    if (body == null) {
                        repoListView.showMessage("未知错误");
                        return;
                    }
                    //content：就是从服务器拿到的响应体数据
                    String content = body.string();
                    Log.d("TAG", content);
                } catch (IOException e) {
                    e.printStackTrace();
                    onFailure(call, e);
                }
            } else {//其他code  --401：一般指未授权
                repoListView.showMessage("code:" + response.code());
            }
        }

        //失败
        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            repoListView.stopRefresh();
            repoListView.showMessage(t.getMessage());
            repoListView.showContentView();
        }
    };

    //上拉加载更多处理
    public void loadMore() {
        repoListView.showLoadMoreLoading();
        new LoadMoreTask().execute();
    }

    final class LoadMoreTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ArrayList<String> datas = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                datas.add("测试数据" + (count++));
            }
            repoListView.addMoreData(datas);
            repoListView.hideLoadMore();
        }
    }

    class RefreshTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ArrayList<String> datas = new ArrayList<String>();
            for (int i = 0; i < 20; i++) {
                datas.add("测试数据" + (count++));
            }
            repoListView.stopRefresh();
            repoListView.refreshData(datas);
            repoListView.showContentView();
        }
    }
}


