package com.newthread.medicinebox.ui.user;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newthread.medicinebox.R;
import com.newthread.medicinebox.bean.mUser;
import com.newthread.medicinebox.theme.StatusBarCompat;
import com.newthread.medicinebox.ui.activity.SwipeBackActivity;
import com.newthread.medicinebox.utils.ConsUtils;
import com.newthread.medicinebox.utils.UserUtils.CurrentUserSp;
import com.newthread.medicinebox.utils.EventBusUtils.MyEventLogin;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.listener.SaveListener;
import de.greenrobot.event.EventBus;

/**
 * 注册Activity
 * Created by 张浩 on 2016/1/20.
 */
public class RegistActivity extends SwipeBackActivity {
    @Bind(R.id.my_account)
    EditText myAccount;
    @Bind(R.id.my_name)
    EditText myNickName;
    @Bind(R.id.my_password)
    EditText myPassword;
    @Bind(R.id.my_password_ins)
    EditText myPasswordIns;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.SignUpLin)
    LinearLayout SignUpLin;
    private String account;
    private String NickName;
    private String password;
    private String passwordIns;
    ProgressDialog dialog;
    CurrentUserSp sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT==Build.VERSION_CODES.KITKAT){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist);
        ButterKnife.bind(this);
        sp=new CurrentUserSp(this);
        initView();
    }

    /*
    * */
    private void initView() {
        initToolBar();
        StatusBarCompat.compat(this,getResources().getColor(R.color.colorPrimaryDark));
    }

    /*
    *显示dialog
    * */
    private void showDialog(){
        dialog=new ProgressDialog(this);
        dialog.setMessage("注册...");
        dialog.setCancelable(false);
        dialog.show();

    }
    private void DismissDia(){
        dialog.dismiss();
    }

    private void initToolBar() {
        toolbarTitle.setVisibility(View.GONE);
        toolbar.setTitle(R.string.regist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /*
    *
    * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_regist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_regist:
                showDialog();
                SignUp();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /*
    * 注册用户的方法；
    * */
    private void SignUp() {
        final mUser user = new mUser();
        getSignUpData();
        if (!isEmpty()){
            if(isEmail(account)){
                if(isSamePassWord()){
                    user.setUsername(account);
                    user.setPassword(passwordIns);
                    user.setNickName(NickName);
                    user.setAge(0);
                    user.setHeadimg("");
                    user.signUp(this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            handler.sendEmptyMessage(ConsUtils.SIGN_UP_SUCCESS);
                        }
                        @Override
                        public void onFailure(int i, String s) {
                            DismissDia();
                            Snackbar.make(SignUpLin, s, Snackbar.LENGTH_SHORT).show();
                        }
                    });
                }else {handler.sendEmptyMessage(ConsUtils.SAME_PASSWORD);}
            }else {handler.sendEmptyMessage(ConsUtils.RIGHT_EMAIL);}
                }else{handler.sendEmptyMessage(ConsUtils.EDIT_COMPLETE);}
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ConsUtils.SIGN_UP_SUCCESS:
                    sp.saveCurrentUser(account,NickName,0,true);
                    EventBus.getDefault().post(new MyEventLogin(NickName));
                   /* MainActivity.head_tips.setVisibility(View.GONE);
                    MainActivity.head_name.setText(NickName);*/
                    DismissDia();
                    LoginActivity.a.finish();
                    finish();
                    break;
                case ConsUtils.EDIT_COMPLETE:
                    DismissDia();
                    Snackbar.make(SignUpLin,"请填完信息",Snackbar.LENGTH_SHORT).show();
                    break;
                case ConsUtils.RIGHT_EMAIL:
                    DismissDia();
                    Snackbar.make(SignUpLin,"请填写正确邮箱",Snackbar.LENGTH_SHORT).show();
                    break;
                case ConsUtils.SAME_PASSWORD:
                    DismissDia();
                    Snackbar.make(SignUpLin,"两个密码不一致",Snackbar.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    /*
    * 获取注册的信息
    * */
    private void getSignUpData() {
        account = myAccount.getText().toString();
        password = myPassword.getText().toString();
        NickName = myNickName.getText().toString();
        passwordIns = myPasswordIns.getText().toString();
    }

    /*
    * 判断邮箱格式是否正确
    * */
    private boolean isEmail(String email) {
        if (email == null || "".equals(email)) {
            return false;
        }
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /*
    * 判断是否为空
    * */
    private boolean isEmpty() {
        return account == null || account.equals("") || password == null ||
                password.equals("") || NickName == null || NickName.equals("")
                || passwordIns == null || passwordIns.equals("");
    }

    /*
    * 判断两次密码是否相同
    * */
    private boolean isSamePassWord() {
        return Objects.equals(password, passwordIns);
    }

}
