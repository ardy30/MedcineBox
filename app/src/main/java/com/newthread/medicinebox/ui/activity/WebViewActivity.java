package com.newthread.medicinebox.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.newthread.medicinebox.R;
import com.newthread.medicinebox.ui.view.ProgressWebView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 张浩 on 2016/2/19.
 */
public class WebViewActivity extends AppCompatActivity {
    @Bind(R.id.webView)
    ProgressWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        webView.loadUrl("http://www.yongyao.net/new/yhyyjsycxb.aspx");
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(webView.canGoBack()){
            webView.goBack();
        }

    }


    /* @Override
    public boolean onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
            return true;
        }

        return false;
    }*/
}
