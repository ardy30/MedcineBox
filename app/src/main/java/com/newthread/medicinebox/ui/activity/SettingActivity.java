package com.newthread.medicinebox.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import com.newthread.medicinebox.R;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 张浩 on 2016/3/31.
 */
public class SettingActivity extends BaseActivity {
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        intiToolbar();
    }
    private void intiToolbar() {
        toolbar.setTitle(R.string.settings);
        setUpToolBar(toolbar,true,true);
    }
}
