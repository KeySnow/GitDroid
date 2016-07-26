package com.example.gitdroid.splash;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.example.gitdroid.splash.pager.Pager0;
import com.example.gitdroid.splash.pager.Pager1;
import com.example.gitdroid.splash.pager.Pager2;

/**
 * Created by 93432 on 2016/7/26.
 */
public class SplashPagerAdapter extends PagerAdapter {

    private final View[] views;

    public SplashPagerAdapter(Context context) {
        views = new View[]{
                new Pager0(context),
                new Pager1(context),
                new Pager2(context)
        };
    }

    public View getView(int position){
        return views[position];
    }

    @Override
    public int getCount() {
        return views.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views[position], 0);
        return views[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
