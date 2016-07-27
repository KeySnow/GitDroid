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
public class RepoListFragment extends Fragment {

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repo_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        presenter = new RepoListPresenter(this);
        adapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_list_item_1,
                new ArrayList<String>()
        );
        listView.setAdapter(adapter);

        initPullToRefresh();
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

    //刷新的方法
    //显示内容 or 错误 or 空白，三选一
    public void showContentView(){
        ptrFrameLayout.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
    }
    public void showErrorView(){
        ptrFrameLayout.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
    }
    public void showEmptyView(){
        ptrFrameLayout.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }

    //显示提示信息
    public void showMessage(String msg){

    }

    public void stopRefresh(){
        ptrFrameLayout.refreshComplete();
    }

    //刷新数据
    //将后台线程更新加载到的数据，刷新显示到视图(ListView)上来显示给用户看
    public void refreshData(List<String> data){
        adapter.clear();
        adapter.addAll(data);
        adapter.notifyDataSetChanged();
    }
}
