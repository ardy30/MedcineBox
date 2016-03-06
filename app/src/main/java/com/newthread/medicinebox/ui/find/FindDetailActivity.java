package com.newthread.medicinebox.ui.find;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;
import com.newthread.medicinebox.R;
import com.newthread.medicinebox.theme.SlidingActivity;
import com.newthread.medicinebox.theme.StatusBarCompat;
import com.newthread.medicinebox.theme.SwipeActivity;
import com.newthread.medicinebox.ui.activity.SwipeBackActivity;
import com.newthread.medicinebox.utils.ComUtils;
import com.newthread.medicinebox.utils.ConsUtils;
import com.newthread.medicinebox.utils.JsoupUtils;
import com.newthread.medicinebox.utils.FindUtils.FindUrlConUtils;
import com.newthread.medicinebox.utils.NetWorkImageUtils.HeadImageHelper;
import com.newthread.medicinebox.utils.NetWorkImageUtils.VolleyImageCacheManager;

/**
 * Created by 张浩 on 2016/2/11.
 */
public class FindDetailActivity extends SwipeBackActivity{
    private Toolbar toolbar;
    private String img_url;
    private String detail_url;
    private String title;
    private String content;
    private NetworkImageView find_head_img;
    private TextView find_content;
    private FrameLayout Load;
    private CardView ConCardView;
    private HeadImageHelper imageUtils;
    private CollapsingToolbarLayout toolbarLayout;
    private FindUrlConUtils urlConUtils;
    private JsoupUtils jsoupUtils;
    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton share_Button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finddetail);
        initFindData();
        initView();
        initContent();
    }

    /*
    * 加载文章内容
    * */
    private void initContent() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getDetailData();
                handler.sendEmptyMessage(ConsUtils.LOAD_FINISH);
            }
        }).start();

    }

    /*
    *
    *
    * */
    private void getDetailData() {
        urlConUtils=new FindUrlConUtils();
        jsoupUtils=new JsoupUtils();
        String result=urlConUtils.getResult(detail_url);
        content=jsoupUtils.ParseFindContentHtml(result);

    }

    private void initFindData() {
        Intent intent=getIntent();
        img_url=intent.getStringExtra("img_url");
        detail_url=intent.getStringExtra("detail_url");
        title=intent.getStringExtra("title");
    }

    private void initView() {
        initToolBar();
        share_Button= (FloatingActionButton) findViewById(R.id.find_share);
        find_content= (TextView) findViewById(R.id.find_detail_content);
        toolbarLayout= (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        find_head_img= (NetworkImageView) findViewById(R.id.find_head_img);
        ConCardView= (CardView) findViewById(R.id.find_cardView);
        Load= (FrameLayout) findViewById(R.id.find_content_progress);
        SetFindHeadImg(img_url);
    }

    /*
    * 进行图像设置
    * */
    private void SetFindHeadImg(String url) {
        find_head_img.setImageUrl(url, VolleyImageCacheManager.getInstance().getImageLoader());
    }

    private void initToolBar() {
        toolbar= (Toolbar) findViewById(R.id.find_toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (ComUtils.CheckBuildVision()){
            coordinatorLayout= (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
            coordinatorLayout.setFitsSystemWindows(true);
            StatusBarCompat.compat(this,getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ConsUtils.LOAD_FINISH:
                    find_content.setText(content);
                    Load.setVisibility(View.GONE);
                    ConCardView.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

}
