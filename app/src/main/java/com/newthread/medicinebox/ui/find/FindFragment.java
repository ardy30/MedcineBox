package com.newthread.medicinebox.ui.find;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import android.widget.LinearLayout;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.newthread.medicinebox.Adapter.MyFindAdapter;
import com.newthread.medicinebox.R;
import com.newthread.medicinebox.bean.FindBean;
import com.newthread.medicinebox.utils.ConsUtils;
import com.newthread.medicinebox.utils.FindUtils.FindUrlConUtils;
import com.newthread.medicinebox.utils.NetWorkHelper;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 张浩 on 2016/1/18.
 */
public class FindFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, MyFindAdapter.OnItemClickListener {
    @Bind(R.id.my_find)
    RecyclerView recyclerView;
    @Bind(R.id.refresh_find)
    SwipeRefreshLayout refreshFind;
    @Bind(R.id.find_progress)
    FrameLayout findProgress;
    @Bind(R.id.progress_view)
    CircularProgressView progressView;
    @Bind(R.id.try_load)
    LinearLayout tryLoad;
    @Bind(R.id.find_lin)
    LinearLayout findLin;
    private View view;
    private int lastVisibleItem;
    private LinearLayoutManager layoutManager;
    ArrayList<FindBean> list;
    ArrayList<FindBean> listmore;
    FindUrlConUtils urlConUtils;
    NetWorkHelper helper;
    MyFindAdapter adapter;
    String url = "http://www.jianke.com/xwpd/yiyaozixun/";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.find, container, false);
        ButterKnife.bind(this, view);
        helper = new NetWorkHelper(getContext());
        initRefresh();
        initView();
        return view;
    }

    /*
    *
    * */
    private void initView() {
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == adapter.getItemCount()) {
                    getMore();
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
        tryLoad.setOnClickListener(this);
    }

    /*
    * 加载更多的方法
    * */
    private void getMore() {
        final int nextPage=(list.size()/10)+1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("nexpage", String.valueOf(nextPage));
                String url="http://www.jianke.com/xwpd/yiyaozixun/index-"+nextPage+".html";
                listmore=new ArrayList<>();
                urlConUtils = new FindUrlConUtils();
                urlConUtils.getListResult(url, listmore);
                handler.sendEmptyMessage(ConsUtils.LOAD_MORE);
            }
        }).start();

    }


    @Override
    public void onItemClick(View view, int position) {
        Intent intent=new Intent(getContext(),FindDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.try_load:
                LoadAgain();
                break;
        }
    }

    /*
    * 重新加载
    * */
    private void LoadAgain() {
        if (helper.isOpenNetWork()) {
            tryLoad.setVisibility(View.GONE);
            getData();
        }
    }

    /*
    * 初始化刷新
    * */
    private void initRefresh() {
        if (helper.isOpenNetWork()) {
            tryLoad.setVisibility(View.GONE);
            getData();
        }
        refreshFind.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary);
        refreshFind.setOnRefreshListener(this);

    }

    /*
   * 实现的接口
   * */
    @Override
    public void onRefresh() {
        //dataUtils.DeleteDataBase();
        if (helper.isOpenNetWork()) {
            RefreshToGetData();
        } else {
            Snackbar.make(findLin,"无网络,请检查网络连接!",Snackbar.LENGTH_SHORT).show();
            refreshFind.setRefreshing(false);
        }
    }

    /*
    * 刷新获取数据
    * */
    private void RefreshToGetData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getLatestNews(url);
                handler.sendEmptyMessage(ConsUtils.REFRESHED);
            }
        }).start();
    }

    /*
    * 获取数据
    * */
    private void getData() {
        progressView.startAnimation();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getLatestNews(url);
                handler.sendEmptyMessage(ConsUtils.FIRST_LOADED);
            }
        }).start();

    }

    /*
   * 联网获取新闻
   * */
    private void getLatestNews(String url) {
        list=new ArrayList<>();
        urlConUtils = new FindUrlConUtils();
        urlConUtils.getListResult(url, list);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConsUtils.FIRST_LOADED:
                    findProgress.setVisibility(View.GONE);
                    setAdapter();
                    break;
                case ConsUtils.REFRESHED:
                    refreshFind.setRefreshing(false);
                    recyclerView.removeAllViews();
                    recyclerView.setAdapter(adapter);
                    break;
                case ConsUtils.LOAD_MORE:
                    list.addAll(listmore);
                    Log.d("item", String.valueOf(adapter.getItemCount()));
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    public void setAdapter(){
        adapter=new MyFindAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MyFindAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (helper.isOpenNetWork()){
                    FindBean bean=list.get(position);
                    Intent intent=new Intent(getContext(),FindDetailActivity.class);
                    intent.putExtra("img_url",bean.getImg_url());
                    intent.putExtra("detail_url",bean.getDetail_url());
                    intent.putExtra("title",bean.getTitle());
                    startActivity(intent);
                }else{
                    Snackbar.make(findLin,"无网络,请检查网络连接!",Snackbar.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
