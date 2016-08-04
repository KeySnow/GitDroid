package com.example.gitdroid.favorite.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 仓库类别
 * Created by 93432 on 2016/8/4.
 */
@DatabaseTable(tableName = "repostiory_group")
public class RepoGroup {
    //主键
    @DatabaseField(id = true)
    private int id;

    @DatabaseField(columnName = "NAME")
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    private static List<RepoGroup> defaultGroups;

    public static List<RepoGroup> getDefaultGroups(Context context){
        if (defaultGroups != null) return defaultGroups;
        try {
            InputStream inputStream = context.getAssets().open("repogroup.json");
            String content = IOUtils.toString(inputStream);
            Gson gson = new Gson();
            defaultGroups = gson.fromJson(content, new TypeToken<List<RepoGroup>>(){}.getType());
            return defaultGroups;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
