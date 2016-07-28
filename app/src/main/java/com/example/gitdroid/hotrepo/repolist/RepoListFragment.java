package com.example.gitdroid.hotrepo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gitdroid.R;
import com.example.gitdroid.commons.ActivityUtils;
import com.example.gitdroid.components.FooterView;
import com.example.gitdroid.hotrepo.repolist.RepoListPresenter;
import com.example.gitdroid.hotrepo.repolist.view.RepoListView;
import com.mugen.Mugen;
import com.mugen.MugenCallbacks;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

/**
 * 仓库列表
 * 将显示当前语言的所有仓库，有下拉刷新，上拉加载更多的效果
 * Created by 93432 on 2016/7/27.
 */
public class RepoListFragment extends Fragment implements RepoListView {

    @BindView(R.id.lvRepos)
    ListView listView;
    @BindView(R.id.emptyView)
    TextView emptyView;
    @BindView(R.id.errorView)
    TextView errorView;
    @BindView(R.id.ptrClassicFrameLayout)
    PtrClassicFrameLayout ptrFrameLayout;

    private ArrayAdapter<String> adapter;
    //用来做当前页面业务逻辑及视图更新的
    private RepoListPresenter presenter;
    //上拉加载更多的视图
    private FooterView footerView;

    private ActivityUtils activityUtils;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repo_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        activityUtils = new ActivityUtils(this);
        presenter = new RepoListPresenter(this);
        adapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_list_item_1,
                new ArrayList<String>()
        );
        listView.setAdapter(adapter);
        //初始下拉刷新
        initPullToRefresh();
        //初始上拉加载更多
        initLoadMoreScroll();
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
            //其内部将用此方法来判断是否触发onLoadMore
            @Override
            public boolean isLoading() {
                return listView.getFooterViewsCount() > 0 && footerView.isLoading();
            }

            //是否已加载完成所有数据
            //其内部将用此方法来判断是否触发onLoadMore
            @Override
            public boolean hasLoadedAllItems() {
                return listView.getFooterViewsCount() > 0 && footerView.isComplete();
            }
        }).start();
    }

    private void initPullToRefresh() {
        //使用当前对象作为key，来记录上一次的刷新时间，如果两次下拉时间太近，将不会触发新刷新
        ptrFrameLayout.setLastUpdateTimeRelateObject(this);
        //关闭header所用时长
        ptrFrameLayout.setDurationToCloseHeader(1500);
        //下拉刷新监听处理
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            //当你"下拉"时，将触发此方法
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //去做数据的加载，做具体的业务
                presenter.refresh();
            }
        });
        //以下代码只是修改了header样式
        StoreHouseHeader header = new StoreHouseHeader(getContext());
        header.initWithString("I LIKE " + " JAVA");
        header.setPadding(0,60,0,60);
        //修改Ptr的HeaderView效果
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.setBackgroundResource(R.color.colorRefresh);
    }

    //下拉刷新视图实现---------------------------------------
    //显示内容 or 错误 or 空白，三选一
    @Override
    public void showContentView(){
        ptrFrameLayout.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
    }

    @Override
    public void showErrorView(String errorMsg){
        ptrFrameLayout.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmptyView(){
        ptrFrameLayout.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }

    //显示提示信息
    @Override
    public void showMessage(String msg){
        activityUtils.showToast(msg);
    }

    @Override
    public void stopRefresh(){
        ptrFrameLayout.refreshComplete();
    }

    //刷新数据
    //将后台线程更新加载到的数据，刷新显示到视图(ListView)上来显示给用户看
    @Override
    public void refreshData(List<String> data){
        adapter.clear();
        adapter.addAll(data);
        adapter.notifyDataSetChanged();
    }

    //上拉加载更多视图实现----------------------------------
    @Override
    public void showLoadMoreLoading() {
        if (listView.getFooterViewsCount() == 0){
            listView.addFooterView(footerView);
        }
        footerView.showLoading();
    }

    @Override
    public void hideLoadMore() {
        listView.removeFooterView(footerView);
    }

    @Override
    public void showLoadMoreError(String errorMsg) {
        if (listView.getFooterViewsCount() == 0){
            listView.addFooterView(footerView);
        }
        footerView.showError(errorMsg);
    }

    @Override
    public void addMoreData(List<String> datas) {
        adapter.addAll(datas);
        adapter.notifyDataSetChanged();
    }
}
