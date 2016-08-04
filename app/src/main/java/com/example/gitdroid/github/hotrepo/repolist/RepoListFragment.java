package com.example.gitdroid.github.hotrepo.repolist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gitdroid.R;
import com.example.gitdroid.commons.ActivityUtils;
import com.example.gitdroid.components.FooterView;
import com.example.gitdroid.favorite.dao.DBHelp;
import com.example.gitdroid.favorite.dao.LocalRepoDao;
import com.example.gitdroid.favorite.model.LocalRepo;
import com.example.gitdroid.favorite.model.RepoConverter;
import com.example.gitdroid.github.hotrepo.Language;
import com.example.gitdroid.github.hotrepo.repolist.modle.Repo;
import com.example.gitdroid.github.hotrepo.repolist.view.RepoListView;
import com.example.gitdroid.github.repoinfo.RepoInfoActivity;
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

    private RepoListAdapter adapter;
    //用来做当前页面业务逻辑及视图更新的
    private RepoListPresenter presenter;
    //上拉加载更多的视图
    private FooterView footerView;

    private ActivityUtils activityUtils;

    private static final String KEY_LANGUAGE = "kay_language";

    public static RepoListFragment getInstance(Language language){
        RepoListFragment fragment = new RepoListFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_LANGUAGE, language);
        fragment.setArguments(args);
        return fragment;
    }

    private Language getLanguage(){
        return (Language)getArguments().getSerializable(KEY_LANGUAGE);
    }

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
        presenter = new RepoListPresenter(this, getLanguage());
        adapter = new RepoListAdapter();
        listView.setAdapter(adapter);
        //按下某个仓库后，进入此仓库详情
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RepoInfoActivity.open(getContext(), adapter.getItem(position));
            }
        });
        //长按某个仓库后，加入收藏
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //热门仓库列表上的Repo
                Repo repo = adapter.getItem(position);
                LocalRepo localRepo = RepoConverter.convert(repo);
                //添加到本地仓库中去（只认本地仓库实体LocalRepo）
                new LocalRepoDao(DBHelp.getInstance(getContext())).createOrUpdate(localRepo);
                activityUtils.showToast("收藏成功！");
                return true;
            }
        });
        //初始下拉刷新
        initPullToRefresh();
        //初始上拉加载更多
        initLoadMoreScroll();
        // 如果当前页面没有数据，开始自动刷新
        if (adapter.getCount() == 0){
            ptrFrameLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ptrFrameLayout.autoRefresh();
                }
            }, 200);
        }
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
        header.initWithString("I LIKE " + getLanguage().getName());
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
    public void refreshData(List<Repo> data){
        adapter.clear();
        adapter.addAll(data);
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
    public void addMoreData(List<Repo> datas) {
        adapter.addAll(datas);
    }
}
