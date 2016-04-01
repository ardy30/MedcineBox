package com.newthread.medicinebox.ui.user;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newthread.medicinebox.R;
import com.newthread.medicinebox.bean.UserInfo;
import com.newthread.medicinebox.ui.activity.SwipeBackActivity;
import com.newthread.medicinebox.utils.ApiUtils;
import com.newthread.medicinebox.utils.ComUtils;
import com.newthread.medicinebox.utils.ConsUtils;
import com.newthread.medicinebox.utils.EventBusUtils.MyEventLogin;
import com.newthread.medicinebox.utils.UserUtils.CurrentUserSp;
import com.newthread.medicinebox.utils.UserUtils.Login;
import com.newthread.medicinebox.utils.UserUtils.SignUp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 注册Activity
 * Created by 张浩 on 2016/1/20.
 */
public class RegisterActivity extends SwipeBackActivity {
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
    private String sessionId;
    ProgressDialog dialog;
    CurrentUserSp sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist);
        ButterKnife.bind(this);
        sp=new CurrentUserSp(this);
        initView();
    }

    /*
    * */
    private void initView() {
        ComUtils.adjustPan(this);
        toolbar.setTitle(R.string.regist);
        setUpToolBar(toolbar,true,true);
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

    /*
    *
    * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_regist:
                ComUtils.hideInput(this,SignUpLin);
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
        getSignUpData();
        if (!isEmpty()) {
            if (isEmail(account)) {
                if (isSamePassWord()) {
                    Register();
                } else {
                    handler.sendEmptyMessage(ConsUtils.SAME_PASSWORD);
                }
            } else {
                handler.sendEmptyMessage(ConsUtils.RIGHT_EMAIL);
            }
        } else {
            handler.sendEmptyMessage(ConsUtils.EDIT_COMPLETE);
        }
    }

    /*
    * 进行注册的方法
    * */
    public void Register() {
        final JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("account", account);
            jsonParam.put("password", passwordIns);
            jsonParam.put("userVirtualName", NickName);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        SignUp signUp = SignUp.getInstance();
                        signUp.UrlRegister(jsonParam);
                        signUp.SignUpEd();
                        Log.d("signup", String.valueOf(signUp.isSignUp()));
                        Log.d("signup", String.valueOf(signUp.isSignUpEd()));
                        if (signUp.isSignUp()) {
                            Login();
                        }else{
                            handler.sendEmptyMessage(ConsUtils.SIGN_UP_FAILED);
                        }
                        if (signUp.isSignUpEd()) {
                            handler.sendEmptyMessage(ConsUtils.SIGN_UP_ED);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /*
    *进行登录的方法
    * */
    public void Login(){
        Login login=Login.getInstance();
        UserInfo userInfo=login.getInfo();
        JSONObject jsonParam=new JSONObject();
        try {
            jsonParam.put("account",account);
            jsonParam.put("password", passwordIns);
            login.UrlLogin(ApiUtils.LoginUrl,jsonParam);
            login.LoginUpEd();
            if (userInfo.isLogin()){
                sessionId=userInfo.getSessionId();
                Log.d("session",sessionId);
                handler.sendEmptyMessage(ConsUtils.SIGN_UP_SUCCESS);
            }else{
                handler.sendEmptyMessage(ConsUtils.LOGIN_FAILED);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ConsUtils.SIGN_UP_SUCCESS:
                    sp.saveCurrentUser(account,NickName,"",true,sessionId);
                    EventBus.getDefault().post(new MyEventLogin(NickName));
                    DismissDia();
                    LoginActivity.a.finish();
                    finish();
                    break;
                case ConsUtils.SIGN_UP_ED:
                    DismissDia();
                    Snackbar.make(SignUpLin,"账号已经被注册",Snackbar.LENGTH_SHORT).show();
                    break;
                case ConsUtils.SIGN_UP_FAILED:
                    DismissDia();
                    Snackbar.make(SignUpLin,"注册失败",Snackbar.LENGTH_SHORT).show();
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
                case ConsUtils.LOGIN_FAILED:
                    DismissDia();
                    Snackbar.make(SignUpLin,"登录失败",Snackbar.LENGTH_SHORT).show();
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



    @Override
    public void onPanelOpened(View view) {
        super.onPanelOpened(view);
        ComUtils.hideInput(this, SignUpLin);
        finish();
    }


}
