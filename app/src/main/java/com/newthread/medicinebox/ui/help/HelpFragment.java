package com.newthread.medicinebox.ui.help;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.newthread.medicinebox.Adapter.HelpAdapter;
import com.newthread.medicinebox.R;
import com.newthread.medicinebox.bean.HelpBean;
import com.newthread.medicinebox.utils.ConsUtils;
import com.newthread.medicinebox.utils.EventBusUtils.MyEventHelp;
import com.newthread.medicinebox.utils.NetWorkHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import de.greenrobot.event.EventBus;

/**
 * Created by 张浩 on 2016/1/16.
 */
public class HelpFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.help_question)
    RecyclerView recyclerview;
    @Bind(R.id.refresh_help)
    SwipeRefreshLayout refreshHelp;
    FrameLayout isConHelpFra;
    private View view;
    @Bind(R.id.add_help_question)
    FloatingActionButton actionButton;
    @Bind(R.id.help_progress)
    FrameLayout frameLayout;
    ArrayList<Map<String, Object>> HelpLists;
    HelpAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.help, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initView();
        initLoad();
        return view;
    }

    /*
    *
    * */
    private void initLoad() {
        NetWorkHelper helper = new NetWorkHelper(getContext());
        if (helper.isOpenNetWork()) {
            initData();
        } else {
            actionButton.setVisibility(View.GONE);
            refreshHelp.setRefreshing(false);
        }
    }

    /*
    * 获取数据
    * */
    private void initData() {
        HelpLists = new ArrayList<>();
        BmobQuery<HelpBean> query = new BmobQuery<>();
        query.include("user");
        query.findObjects(getContext(), new FindListener<HelpBean>() {
            @Override
            public void onSuccess(List<HelpBean> list) {
                for (HelpBean bean : list) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("nickname", bean.getUser().getNickName());
                    map.put("img_url", bean.getUser().getHeadimg());
                    map.put("medicine", bean.getMedicine());
                    map.put("question", bean.getQuestion());
                    map.put("date", bean.getCreatedAt());
//                    System.out.println(bean.getUser().getAge());
//                    System.out.println(bean.getUser().getNickName());
//                    System.out.println(bean.getUser().getHeadimg());
//                    System.out.println(bean.getMedicine());
//                    System.out.println(bean.getQuestion());
                    HelpLists.add(map);
                    Collections.reverse(HelpLists);
                    adapter = new HelpAdapter(getContext(), HelpLists);
                    handler.sendEmptyMessage(ConsUtils.LOAD_FINISH);
                }

                System.out.println(HelpLists);
            }

            @Override
            public void onError(int i, String s) {
                System.out.println(s);
            }
        });

    }


    private void initView() {
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshHelp.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary);
        refreshHelp.setOnRefreshListener(this);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddQuestionActivity.class));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onRefresh() {
        initData();
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConsUtils.LOAD_FINISH:
                    refreshHelp.setRefreshing(false);
                    frameLayout.setVisibility(View.GONE);
                    recyclerview.setAdapter(adapter);
                    break;

            }
        }
    };


    public void onEventMainThread(MyEventHelp eventHelp) {
        Log.d("add", "11111");
        boolean Added = eventHelp.isAdded();
        if (Added) {
            Log.d("add", String.valueOf(Added));
            refreshHelp.setRefreshing(true);
            initData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
