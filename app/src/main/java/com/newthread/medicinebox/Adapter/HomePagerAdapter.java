package com.newthread.medicinebox.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.newthread.medicinebox.bean.BaseEntity;

import java.util.List;

/**
 * Created by 张浩 on 2016/1/16.
 */
public class HomePagerAdapter extends FragmentPagerAdapter {

    private List<BaseEntity> mlist=null;

    public HomePagerAdapter(FragmentManager fm,List<BaseEntity> list) {
        super(fm);
        mlist=list;
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
