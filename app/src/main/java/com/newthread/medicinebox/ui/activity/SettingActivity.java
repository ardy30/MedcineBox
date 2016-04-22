package com.newthread.medicinebox.ui.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.newthread.medicinebox.R;
import com.stylingandroid.prism.Prism;
import com.stylingandroid.prism.filter.Filter;
import com.stylingandroid.prism.filter.TintFilter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 张浩 on 2016/3/31.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private static final float TINT_FACTOR_50_PERCENT = 0.5f;
    private static final int RED = Color.RED;
    private static final int GREEN = Color.GREEN;
    private static final int BLUE = Color.BLUE;
    private Prism prism;
    private int currentColour = RED;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.floatAction_test)
    FloatingActionButton floatActionTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        ButterKnife.bind(this);
        initView();
        initColorTest();
    }


    private void initColorTest() {
        Filter tint=new TintFilter(TINT_FACTOR_50_PERCENT);
        prism=Prism.Builder.newInstance()
                .background(appbar)
                .background(getWindow())
                .build();
        floatActionTest.setOnClickListener(this);
        setColor(currentColour);
    }

    /**
     *
     * @param currentColour
     */
    private void setColor(int currentColour) {
        if (prism!=null){
            prism.setColor(currentColour);
        }
    }

    private void initView() {
        intiToolbar();
    }

    private void intiToolbar() {
        toolbar.setTitle(R.string.settings);
        setUpToolBar(toolbar, true, true);
    }

    @Override
    public void onClick(View v) {
        switch (currentColour){
            case RED:
                currentColour=GREEN;
                break;
            case GREEN:
                currentColour=BLUE;
                break;
            case BLUE:
             default:
                 currentColour=RED;
                 break;
        }
        setColor(currentColour);
    }


    @Override
    protected void onDestroy() {
        if (prism!=null){
            prism.destroy();
        }
        super.onDestroy();
    }
}
