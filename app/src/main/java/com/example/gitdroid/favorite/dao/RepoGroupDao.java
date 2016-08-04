package com.example.gitdroid.favorite.dao;

import com.example.gitdroid.favorite.model.RepoGroup;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * 仓库类别表的DAO
 * Created by 93432 on 2016/8/4.
 */
public class RepoGroupDao {

    private Dao<RepoGroup, Long> dao;

    public RepoGroupDao(DBHelp dbHelp){
        try {
            //创建出仓库类别表的Dao
            dao = dbHelp.getDao(RepoGroup.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加和更新仓库类别表
     * @param repoGroup
     */
    public void createOrUpdate(RepoGroup repoGroup){
        try {
            dao.createOrUpdate(repoGroup);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加和更新仓库类别表
     * @param repoGroups
     */
    public void createOrUpdate(List<RepoGroup> repoGroups){
        for (RepoGroup repoGroup : repoGroups){
            createOrUpdate(repoGroup);
        }
    }

    /**
     * 查询指定ID仓库类别
     * @param id
     * @return
     */
    public RepoGroup queryForId(long id){
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询所有的仓库类别
     * @return
     */
    public List<RepoGroup> queryForAll(){
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
