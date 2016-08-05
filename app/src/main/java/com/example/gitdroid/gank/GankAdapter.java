package com.example.gitdroid.gank;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gitdroid.R;
import com.example.gitdroid.gank.model.GankItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 93432 on 2016/8/5.
 */
public class GankAdapter extends BaseAdapter {

    private final ArrayList<GankItem> datas;

    public GankAdapter() {
        datas = new ArrayList<GankItem>();
    }

    public void setDatas(List<GankItem> data) {
        datas.clear();
        datas.addAll(data);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public GankItem getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.layout_item_gank, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.gank_item.setText(getItem(position).getDesc());
        return convertView;
    }

    static class ViewHolder {
        private TextView gank_item;

        ViewHolder(View view) {
            gank_item = (TextView) view.findViewById(R.id.gank_item);
        }
    }
}
