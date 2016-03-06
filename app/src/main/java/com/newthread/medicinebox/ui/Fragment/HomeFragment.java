package com.newthread.medicinebox.ui.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.newthread.medicinebox.R;
import com.newthread.medicinebox.ui.home.Security;
import com.newthread.medicinebox.ui.home.Service;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

/**
 * Created by 张浩 on 2016/1/15.
 */
public class HomeFragment extends Fragment {
    private View view;
    private ViewPager viewPager;
    private SmartTabLayout viewPagerTab ;
    private FragmentPagerItemAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.frg_home,container,false);
        initPagerAdapter();
        initView();
        return  view;
    }
    /*
    * 初始化适配器
    * */
    private void initPagerAdapter() {
        adapter = new FragmentPagerItemAdapter(
                getChildFragmentManager(), FragmentPagerItems.with(getActivity())
                .add(R.string.title_recommend, Security.class)
                .add(R.string.title_service, Service.class)
                .create());

    }

    /*
    * 初始化控件
    * */
    private void initView() {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPagerTab = (SmartTabLayout) view.findViewById(R.id.viewpagertab);
        viewPager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewPager);

    }

}
