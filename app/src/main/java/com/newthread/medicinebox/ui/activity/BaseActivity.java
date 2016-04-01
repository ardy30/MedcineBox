package com.newthread.medicinebox.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.newthread.medicinebox.R;
import com.newthread.medicinebox.theme.StatusBarCompat;
import com.newthread.medicinebox.utils.ComUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 张浩 on 2016/3/10.
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base);
    }
    public void setUpToolBar(Toolbar toolbar,boolean HomeButtonEnable,boolean HomeAsUpEnable){
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(HomeButtonEnable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(HomeAsUpEnable);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (ComUtils.CheckBuildVision()){
            StatusBarCompat.compat(this, getResources().getColor(R.color.colorPrimaryDark));
        }
    }
}
