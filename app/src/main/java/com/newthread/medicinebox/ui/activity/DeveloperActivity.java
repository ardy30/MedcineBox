package com.newthread.medicinebox.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import com.newthread.medicinebox.R;
import com.newthread.medicinebox.utils.ScreenUtils.DensityUtil;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 张浩 on 2016/2/12.
 */
public class DeveloperActivity extends BaseActivity {
//    @Bind(R.id.toolbar_title)
//    TextView toolbarTitle;
//    @Bind(R.id.toolbar)
//    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postdetail);
//        ButterKnife.bind(this);
//        toolbar.setTitle("");
//        setUpToolBar(toolbar, true, true);
    }
}
