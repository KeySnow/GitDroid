package com.example.gitdroid.github.hotuser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gitdroid.R;
import com.example.gitdroid.commons.ActivityUtils;
import com.example.gitdroid.components.FooterView;
import com.example.gitdroid.github.login.modle.User;
import com.mugen.Mugen;
import com.mugen.MugenCallbacks;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

/**
 * Created by 93432 on 2016/8/3.
 */
public class HotUserFragment extends Fragment implements HotUserPresenter.HotUsersView{

    private ActivityUtils activityUtils;

    @BindView(R.id.lvRepos)
    ListView listView;//用来展示数据的列表视图
    @BindView(R.id.errorView)
    TextView errorView;//数据获取时的错误视图
    @BindView(R.id.emptyView)
    TextView emptyView;//没有更多数据时的空视图
    @BindView(R.id.ptrClassicFrameLayout)
    PtrClassicFrameLayout ptrFrameLayout;

    private FooterView footerView;
    private HotUserPresenter presenter;
    private HotUserAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hot_user, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        activityUtils = new ActivityUtils(this);
        presenter = new HotUserPresenter(this);

        adapter = new HotUserAdapter();
        listView.setAdapter(adapter);

        //初始化下拉刷新
        initPullToRefresh();
        //初始化上拉加载更多
        initLoadMoreScroll();
        //如果当前页面没有数据，开始自动刷新
        if (adapter.getCount() == 0){
            ptrFrameLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ptrFrameLayout.autoRefresh();
                }
            }, 200);
        }

    }

    private void initPullToRefresh() {
        //使用当前对象作为key，来记录上一次的刷新时间，如果两次下拉时间太近，将不会触发新刷新
        ptrFrameLayout.setLastUpdateTimeRelateObject(this);
        //关闭header所用时长
        ptrFrameLayout.setDurationToCloseHeader(1500);
        //下拉刷新监听处理
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            //当你"下拉时"，将触发此方法
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //去做数据的加载，做具体的业务
                presenter.refresh();
            }
        });
        //以下代码只是修改了header样式
        StoreHouseHeader header = new StoreHouseHeader(getContext());
        header.initWithString("I LIKE " + "JAVA");
        header.setPadding(0, 60, 0, 60);
        //修改Ptr的HeaderView效果
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.setBackgroundResource(R.color.colorRefresh);
    }

    private void initLoadMoreScroll() {
        footerView = new FooterView(getContext());
        Mugen.with(listView, new MugenCallbacks() {
            //listview滚动到底部，将触发此方法
            @Override
            public void onLoadMore() {
                //执行上拉加载数据的业务处理
                presenter.loadMore();
            }

            //是否正在加载中
            // 其内部将用此方法来判断是否触发onLoadMore
            @Override
            public boolean isLoading() {
                return listView.getFooterViewsCount() > 0 && footerView.isLoading();
            }

            //是否已加载完成所有数据
            // 其内部将用此方法来判断是否触发onLoadMore
            @Override
            public boolean hasLoadedAllItems() {
                return listView.getFooterViewsCount() > 0 && footerView.isComplete();
            }
        }).start();
    }
    //下拉刷新视图实现--------------------------------
    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

    @Override
    public void showRefreshView() {
        ptrFrameLayout.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void showErrorView(String errorMsg) {
        ptrFrameLayout.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void hideRefreshView() {
        ptrFrameLayout.refreshComplete();
    }

    @Override
    public void refreshData(List<User> data) {
        adapter.clear();
        adapter.addAll(data);
    }

    //上拉加载更多视图实现----------------------------------------
    @Override
    public void showLoadMoreLoading() {
        if (listView.getFooterViewsCount() == 0){
            listView.addFooterView(footerView);
        }
        footerView.showLoading();
    }

    @Override
    public void hideLoadMoreLoading() {
        listView.removeFooterView(footerView);
    }

    @Override
    public void showLoadMoreErro(String erroMsg) {
        if (listView.getFooterViewsCount() == 0){
            listView.addFooterView(footerView);
        }
        footerView.showError(erroMsg);
    }

    @Override
    public void addMoreData(List<User> datas) {
        adapter.addAll(datas);
    }
}
