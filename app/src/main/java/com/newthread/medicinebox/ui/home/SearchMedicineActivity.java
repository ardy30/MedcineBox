package com.newthread.medicinebox.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.newthread.medicinebox.Adapter.SearchAdapter;
import com.newthread.medicinebox.R;
import com.newthread.medicinebox.bean.MedicineBean;
import com.newthread.medicinebox.theme.StatusBarCompat;
import com.newthread.medicinebox.ui.activity.SwipeBackActivity;
import com.newthread.medicinebox.utils.ApiUtils;
import com.newthread.medicinebox.utils.ComUtils;
import com.newthread.medicinebox.utils.ConsUtils;
import com.newthread.medicinebox.utils.HttpUtils.UrlConnectionUtils;
import com.newthread.medicinebox.utils.JsonUtils.JsonHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 张浩 on 2016/2/16.
 */
public class SearchMedicineActivity extends SwipeBackActivity {
    String name;
    @Bind(R.id.search_recycler)
    RecyclerView searchRecycler;
    JSONObject object;
    ArrayList<MedicineBean> mList;
    @Bind(R.id.search_progress)
    FrameLayout searchProgress;
    @Bind(R.id.NoSearchResult)
    FrameLayout NoResultTips;
    SearchAdapter adapter;
    @Bind(R.id.search_view)
    MaterialSearchView searchView;
    @Bind(R.id.search_toolbar)
    Toolbar searchToolbar;
    @Bind(R.id.container)
    CoordinatorLayout container;
    String[] a = {"1111", "22222"};
    JsonHelper helper;
    MedicineBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initToolBar();
        searchView.setVoiceSearch(true);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                try {
                    name = URLEncoder.encode(query, "utf-8");
                    String url = ApiUtils.SearchUrl + name;
                    Log.d("urltest",url);
                    if (mList == null) {
                        Log.d("query", query);
                        getData(url);
                    } else {
                        Log.d("size", String.valueOf(mList.size()));
                        adapter.notifyItemRangeRemoved(0, mList.size());
                        mList.clear();
                        getData(url);
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
        searchRecycler.setItemAnimator(new DefaultItemAnimator());
        searchRecycler.setLayoutManager(new LinearLayoutManager(this));
        searchProgress.setVisibility(View.GONE);
    }


    private void initToolBar() {
        if (ComUtils.CheckBuildVision()){
            StatusBarCompat.compat(SearchMedicineActivity.this,getResources().getColor(R.color.colorPrimaryDark));
        }
        searchToolbar.setTitle(R.string.searchmedicine);
        setSupportActionBar(searchToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
    * 查询药品
    * */
    private void getData(final String url) {
        NoResultTips.setVisibility(View.GONE);
        searchProgress.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = UrlConnectionUtils.getResult(url);
                if (result != null) {
                    try {
                        SearchResult(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    handler.sendEmptyMessage(ConsUtils.BAD_NETWORK);

                }

            }
        }).start();
    }

    public void SearchResult(String result) throws JSONException {
        helper=JsonHelper.getInstance();
        object = UrlConnectionUtils.getJSON(result);
        helper.getMedicineSearchInfo(object, new JsonHelper.OnSearchListener() {
            @Override
            public void onSuccess(ArrayList<MedicineBean> list) {
                mList=list;
                adapter = new SearchAdapter(SearchMedicineActivity.this, list);
                handler.sendEmptyMessage(ConsUtils.LOAD_FINISH);
            }
            @Override
            public void onError(String error) {
                handler.sendEmptyMessage(ConsUtils.NO_SEARCH_RESULT);
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConsUtils.LOAD_FINISH:
                    searchRecycler.setAdapter(adapter);
                    searchProgress.setVisibility(View.GONE);
                    break;
                case ConsUtils.NO_SEARCH_RESULT:
                    searchProgress.setVisibility(View.GONE);
                    NoResultTips.setVisibility(View.VISIBLE);
                    break;
                case ConsUtils.BAD_NETWORK:
                    Snackbar.make(container,"网络异常！",Snackbar.LENGTH_SHORT).show();
                    searchProgress.setVisibility(View.GONE);
                    break;
            }
        }
    };
}
