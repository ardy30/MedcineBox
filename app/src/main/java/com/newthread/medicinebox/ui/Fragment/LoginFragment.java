package com.newthread.medicinebox.ui.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.newthread.medicinebox.R;
import com.newthread.medicinebox.ui.user.LoginActivity;
import com.newthread.medicinebox.ui.user.RegistActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 张浩 on 2016/2/17.
 */
public class LoginFragment extends Fragment {
    @Bind(R.id.button_regist)
    Button buttonRegist;
    @Bind(R.id.button_login)
    Button buttonLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.loginfragment, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
        buttonRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), RegistActivity.class));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("destory","销毁了");
        ButterKnife.unbind(this);
    }
}
