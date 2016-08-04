package com.example.gitdroid.favorite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.gitdroid.R;
import com.example.gitdroid.commons.ActivityUtils;
import com.example.gitdroid.favorite.dao.DBHelp;
import com.example.gitdroid.favorite.dao.LocalRepoDao;
import com.example.gitdroid.favorite.dao.RepoGroupDao;
import com.example.gitdroid.favorite.model.RepoGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 本地收藏页面Fragment
 * Created by 93432 on 2016/8/4.
 */
public class FavoriteFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {

    @BindView(R.id.tvGroupType)
    TextView tvGroupType;//用来显示当前类别的文本
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.btnFilter)
    ImageButton btnFilter;//用来切换类别的按钮

    private ActivityUtils activityUtils;

    //仓库类别DAO（数据的增删改查）
    private RepoGroupDao repoGroupDao;
    //本地仓库DAO（数据的增删改查）
    private LocalRepoDao localRepoDao;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        repoGroupDao = new RepoGroupDao(DBHelp.getInstance(getContext()));
        localRepoDao = new LocalRepoDao(DBHelp.getInstance(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        activityUtils.showToast("size = " + localRepoDao.queryForAll().size());
    }

    //按下按钮弹出类别菜单
    @OnClick(R.id.btnFilter)
    public void showPopupMenu(View view){
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.setOnMenuItemClickListener(this);
        //menu项（注意，上面只有全部和未分类）
        popupMenu.inflate(R.menu.menu_popup_repo_groups);
        //向menu上添加我们自己的各类别
        //1.拿到menu
        Menu menu = popupMenu.getMenu();
        //2.拿到数据
        List<RepoGroup> repoGroups = repoGroupDao.queryForAll();
        for (RepoGroup repoGroup : repoGroups){
            menu.add(Menu.NONE, repoGroup.getId(), Menu.NONE, repoGroup.getName());
        }
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        String title = item.getTitle().toString();
        tvGroupType.setText(title);
        setData(item.getItemId());
        return true;
    }

    private void setData(int groupId) {
        switch (groupId){
            case R.id.repo_group_all:

                break;
            case R.id.repo_group_no:

                break;
            default:

                break;
        }
    }
}
