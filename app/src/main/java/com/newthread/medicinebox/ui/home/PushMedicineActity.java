package com.newthread.medicinebox.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.newthread.medicinebox.Adapter.MyMedicineAdapter;
import com.newthread.medicinebox.R;
import com.newthread.medicinebox.bean.RecMedicineBean;
import com.newthread.medicinebox.theme.StatusBarCompat;
import com.newthread.medicinebox.ui.activity.SwipeBackActivity;
import com.newthread.medicinebox.utils.ComUtils;
import com.newthread.medicinebox.utils.ConsUtils;
import com.newthread.medicinebox.utils.FindUtils.FindUrlConUtils;
import com.newthread.medicinebox.utils.JsoupUtils;
import com.newthread.medicinebox.utils.NetWorkHelper;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 张浩 on 2016/2/16.
 */
public class PushMedicineActity extends SwipeBackActivity implements View.OnClickListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
//    @Bind(R.id.push_medicine)
//    RecyclerView recyclerView;
    String toolbar_name;
    String url_code;
    String url;
    ArrayList<RecMedicineBean> list = new ArrayList<>();
    ArrayList<RecMedicineBean> listmore=new ArrayList<>();
    @Bind(R.id.medicine_progress)
    FrameLayout medicineFrame;
    @Bind(R.id.progress_view_medicine)
    CircularProgressView progressView;
    @Bind(R.id.try_load_medicine)
    LinearLayout try_load;
    MyMedicineAdapter adapter;
    FindUrlConUtils urlConUtils = new FindUrlConUtils();
    JsoupUtils jsoupUtils = new JsoupUtils();
    View diaView;
    FrameLayout medicinedia;
    TextView Instruction;
    String instructions;
    Dialog dialog;
    LayoutInflater layoutInflater;
    @Bind(R.id.push_medicine_x)
    XRecyclerView xrecyclerview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pushmedicine);
        ButterKnife.bind(this);
        initData();
        initView();
        initIfLoad();
    }
    private void initIfLoad() {
        NetWorkHelper helper = new NetWorkHelper(this);
        if (helper.isOpenNetWork()) {
            getMedicineData();
        } else {
            progressView.setVisibility(View.GONE);
            try_load.setVisibility(View.VISIBLE);
            try_load.setOnClickListener(this);
        }
    }

    private void getMedicineData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String s = urlConUtils.getResult(url);
                jsoupUtils.ParseMedicines(s, list);
                handler.sendEmptyMessage(ConsUtils.LOAD_FINISH);
            }
        }).start();
    }

    private void initData() {
        Intent intent = getIntent();
        toolbar_name = intent.getStringExtra("toolbarname");
        url_code = intent.getStringExtra("urlcode");
        url = "http://www.jianke.com/list-" + url_code + ".html";//010101
    }

    private void initView() {
        layoutInflater = LayoutInflater.from(this);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        xrecyclerview.setLayoutManager(layoutManager);
        xrecyclerview.setPullRefreshEnabled(false);
        xrecyclerview.setLaodingMoreProgressStyle(ProgressStyle.LineScalePulseOutRapid);
        medicineFrame.setVisibility(View.VISIBLE);
        progressView.setVisibility(View.VISIBLE);
        initLoad();
        initToolBar();
    }


    private void initLoad() {
        xrecyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                final int nextPage=(list.size()/24)+1;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("nexpage", String.valueOf(nextPage));
                        String url="http://www.jianke.com/list-"+url_code+"-0-"+nextPage+
                                "-0-1-0-0-0-0-0.html";
                        jsoupUtils.ParseMedicines(urlConUtils.getResult(url),listmore);
                        list.addAll(listmore);
                        Log.d("url",url);
                        handler.sendEmptyMessage(ConsUtils.LOAD_MORE);
                    }
                }).start();
            }
        });
    }

    private void initToolBar() {
        toolbar.setTitle(toolbar_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (ComUtils.CheckBuildVision()) {
            StatusBarCompat.compat(this, getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConsUtils.LOAD_FINISH:
                    medicineFrame.setVisibility(View.GONE);
                    adapter = new MyMedicineAdapter(getApplicationContext(), list);
                    initItemListener();
                    //recyclerView.setAdapter(adapter);
                    xrecyclerview.setAdapter(adapter);
                    break;
                case ConsUtils.LOAD_INS:
                    medicinedia.setVisibility(View.GONE);
                    Instruction.setText(instructions);
                    break;
                case ConsUtils.LOAD_MORE:
                    xrecyclerview.loadMoreComplete();
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    private void initItemListener() {
        adapter.setOnItemClickListener(new MyMedicineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                RecMedicineBean bean = list.get(position);
                initAlrtDia(bean.getMedicineUrl());
            }
        });
    }

    private void initAlrtDia(String url) {
        Log.d("url", url);
        diaView = layoutInflater.inflate(R.layout.medicine_instructions, null);
        medicinedia = (FrameLayout) diaView.findViewById(R.id.medicine_dia_frame);
        Instruction = (TextView) diaView.findViewById(R.id.instruction);
        dialog = new AlertDialog.Builder(this)
                .setTitle("说明书")
                .setView(diaView)
                .setCancelable(true)
                .show();
        initInstructions(url);

    }

    private void initInstructions(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = urlConUtils.getResult(url);
                instructions = jsoupUtils.ParseMedicineInstruction(result);
                handler.sendEmptyMessage(ConsUtils.LOAD_INS);
            }
        }).start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.try_load:
                NetWorkHelper helper = new NetWorkHelper(this);
                if (helper.isOpenNetWork()) {
                    Log.d("test", "onclick");
                    try_load.setVisibility(View.GONE);
                    progressView.setVisibility(View.VISIBLE);
                    getMedicineData();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
