package com.example.gitdroid.github.hotuser;

import android.support.annotation.NonNull;

import com.example.gitdroid.github.login.modle.User;
import com.example.gitdroid.github.network.GitHubClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 热门开发者业务
 * Created by 93432 on 2016/8/3.
 */
public class HotUserPresenter {
    /**
     * 热门开发者业务视图接口
     */
    public interface HotUsersView {
        /**
         * 在执行业务时将触发，用来显示一些提示信息
         */
        void showMessage(String msg);
        // --------------------------------------------------------

        /**
         * 在开始refresh业务时，将触发，用来显示下拉刷新时的内容视图
         */
        void showRefreshView();

        /**
         * 在执行refresh业务时，出现错误将触发,用来显示下拉刷新时的错误视图
         */
        void showErrorView(String errorMsg);

        /**
         * 在结束refresh业务时，将触发，用来隐藏下拉刷新时的内容视图
         */
        void hideRefreshView();

        /**
         * 在结束refresh业务，成功获取到数据后将触发，用来交付业务数据，让视图去显示数据
         */
        void refreshData(List<User> data);
        // --------------------------------------------------------

        /**
         * 在开始loadMore业务时，将触发，用来显示上拉加载时的加载中视图
         */
        void showLoadMoreLoading();

        /**
         * 在结束loadMore业务时，将触发，隐藏上拉加载时的加载中视图
         */
        void hideLoadMoreLoading();

        /**
         * 显示上拉加载时的错误视图
         */
        void showLoadMoreErro(String erroMsg);

        /**
         * 在结束loadmore业务，成功获取到数据后将触发，用来交付业务数据，让视图去显示数据
         */
        void addMoreData(List<User> datas);
    }

    //用户列表视图接口对象
    private HotUsersView hotUsersView;

    Call<HotUserResult> usersCall;
    private int nextPage = 0;

    public HotUserPresenter(@NonNull HotUsersView hotUsersView) {
        this.hotUsersView = hotUsersView;
    }

    //下拉刷新处理
    public void refresh() {
        hotUsersView.hideLoadMoreLoading();
        hotUsersView.showRefreshView();
        nextPage = 1;//永远刷新最新数据
        if (usersCall != null) usersCall.cancel();
        usersCall = GitHubClient.getInstance().searchUsers("followers:" + ">1000", nextPage);
        usersCall.enqueue(ptrCallback);
    }

    //上拉加载更多处理
    public void loadMore() {
        hotUsersView.showLoadMoreLoading();
        if (usersCall != null) usersCall.cancel();
        usersCall = GitHubClient.getInstance().searchUsers("followers:" + ">1000", nextPage);
        usersCall.enqueue(loadMoreCallback);
    }

    private Callback<HotUserResult> loadMoreCallback = new Callback<HotUserResult>() {
        @Override
        public void onResponse(Call<HotUserResult> call, Response<HotUserResult> response) {
            //隐藏加载更多视图
            hotUsersView.hideLoadMoreLoading();
            HotUserResult hotUserResult = response.body();
            if (hotUserResult == null) {
                hotUsersView.showErrorView("结果为空！");
                return;
            }
            //取出搜索到的所有用户
            List<User> userList = hotUserResult.getUserList();
            hotUsersView.addMoreData(userList);
            nextPage++;
        }

        @Override
        public void onFailure(Call<HotUserResult> call, Throwable t) {
            //隐藏加载更多视图
            hotUsersView.hideLoadMoreLoading();
            hotUsersView.showMessage("loadMoreCallback onFailure "+t.getMessage());
        }
    };

    private Callback<HotUserResult> ptrCallback = new Callback<HotUserResult>() {
        @Override
        public void onResponse(Call<HotUserResult> call, Response<HotUserResult> response) {
            //视图停止刷新
            hotUsersView.hideRefreshView();
            HotUserResult hotUserResult = response.body();
            if (hotUserResult == null) {
                hotUsersView.showErrorView("结果为空！");
                return;
            }
            //取出搜索到的所有用户
            List<User> userList = hotUserResult.getUserList();
            hotUsersView.refreshData(userList);
            //下拉刷新成功，下一面则更新为2
            nextPage = 2;
        }

        @Override
        public void onFailure(Call<HotUserResult> call, Throwable t) {
            //视图停止刷新
            hotUsersView.hideRefreshView();
            hotUsersView.showMessage("ptrCallback onFailure " + t.getMessage());
        }
    };
}
