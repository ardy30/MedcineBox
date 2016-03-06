package com.newthread.medicinebox.ui.codescan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.newthread.medicinebox.R;
import com.newthread.medicinebox.bean.MedicineBean;
import com.newthread.medicinebox.theme.StatusBarCompat;
import com.newthread.medicinebox.ui.activity.SwipeBackActivity;
import com.newthread.medicinebox.ui.remind.AddRemindActivity;
import com.newthread.medicinebox.utils.ComUtils;
import com.newthread.medicinebox.utils.ConsUtils;
import com.newthread.medicinebox.utils.HttpUtils.UrlConnectionUtils;
import com.newthread.medicinebox.utils.JsonHelper;
import com.newthread.medicinebox.utils.ApiUtils;
import com.newthread.medicinebox.utils.NetWorkImageUtils.VolleyImageCacheManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 张浩 on 2016/1/31.
 */
public class ScanResultActivity extends SwipeBackActivity {
    @Bind(R.id.qr_medicineImg)
    NetworkImageView qrMedicineImg;
    @Bind(R.id.qr_medicinename)
    TextView qrMedicinename;
    @Bind(R.id.qr_medicineSympton)
    TextView qrMedicineSympton;
    @Bind(R.id.qr_medicineConsume)
    TextView qrMedicineConsume;
    @Bind(R.id.qr_medicineSize)
    TextView qrMedicineSize;
    @Bind(R.id.qr_medicineProduct)
    TextView qrMedicineProduct;
    @Bind(R.id.qr_progress)
    FrameLayout qrProgress;
    @Bind(R.id.coordinatorLayout_qr)
    CoordinatorLayout coordinatorLayout;
    private Toolbar toolbar;
    private Intent intent;
    private String url;
    private JSONObject MedicineJson;
    private ArrayList<MedicineBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanresult);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        intent = getIntent();
        String result = intent.getStringExtra("result");
        url = ApiUtils.QrScanUrl + result;
        getData(url);
    }

    private void getData(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = UrlConnectionUtils.getResult(url);
                JsonHelper helper = new JsonHelper();
                try {
                    list = helper.getMedicineInfo(UrlConnectionUtils.getJSON(result));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(ConsUtils.PARSE_FINISH);
            }
        }).start();

    }

    private void initView() {
        initToolBar();
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.QrScan_toolbar);
        toolbar.setTitle(R.string.scanresult);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (ComUtils.CheckBuildVision()){
            coordinatorLayout.setFitsSystemWindows(false);
            StatusBarCompat.compat(ScanResultActivity.this,getResources().getColor(R.color.colorPrimaryDark));
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConsUtils.PARSE_FINISH:
                    qrMedicineImg.setImageUrl(ApiUtils.imgUrl+list.get(0).getImageurl(), VolleyImageCacheManager.
                            getInstance().getImageLoader());
                    qrMedicinename.setText("【药品名称】" + list.get(0).getName());
                    qrMedicineSympton.setText("【主治功能】" + list.get(0).getConsume());
                    qrMedicineConsume.setText("【用量用法】" + list.get(0).getConsume());
                    qrMedicineSize.setText("【规格】" + list.get(0).getSize());
                    qrMedicineProduct.setText("【生厂商】" + list.get(0).getProduct());
                    qrProgress.setVisibility(View.GONE);
                    break;
            }
        }
    };

    private void showDialog(){
        AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("添加")
                .setMessage("是否添加用药提醒")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(ScanResultActivity.this,AddRemindActivity.class);
                        intent.putExtra("medicine",list.get(0).getName());
                        startActivity(intent);
                        dialog.dismiss();
                        finish();
                    }
                })
                .setCancelable(true)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_add:
                showDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
