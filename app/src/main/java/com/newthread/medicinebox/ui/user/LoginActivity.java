package com.newthread.medicinebox.ui.user;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.newthread.medicinebox.R;
import com.newthread.medicinebox.bean.UserInfo;
import com.newthread.medicinebox.bean.mUser;
import com.newthread.medicinebox.theme.StatusBarCompat;
import com.newthread.medicinebox.ui.activity.SwipeBackActivity;
import com.newthread.medicinebox.utils.ApiUtils;
import com.newthread.medicinebox.utils.ComUtils;
import com.newthread.medicinebox.utils.ConsUtils;
import com.newthread.medicinebox.utils.EventBusUtils.MyEventLogin;
import com.newthread.medicinebox.utils.UserUtils.CurrentUserSp;
import com.newthread.medicinebox.utils.UserUtils.Login;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * 登录的Activity
 * Created by 张浩 on 2016/1/19.
 */
public class LoginActivity extends SwipeBackActivity implements View.OnClickListener {
    @Bind(R.id.my_account)
    EditText myAccount;
    @Bind(R.id.my_password)
    EditText myPassword;
    public static LoginActivity a;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.button_regist)
    Button buttonRegist;
    @Bind(R.id.button_login)
    Button buttonLogin;
    private String Account;
    private String Password;
    private String NickName="";
    private String Age="";
    private String SessionId;
    private String url;
    private String HeadImgUrl="";
    private mUser user;
    @Bind(R.id.LoginLin)
    LinearLayout LoginLin;
    ProgressDialog dialog;
    CurrentUserSp sp;
    Login login;
    UserInfo info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        init();
        initView();
        sp = new CurrentUserSp(this);
        a = this;
        initPermission();
    }

    private void init() {
        login=Login.getInstance();
        info=login.getInfo();
    }

    /**
     * 处理权限啊
     */
    private void initPermission() {
        PermissionGen.with(LoginActivity.this)
                .addRequestCode(100)
                .permissions(
                        //读取手机的权限
                        Manifest.permission.READ_PHONE_STATE
                )
                .request();
//        PermissionGen.needPermission(LoginActivity.this,100,
//                new String[]{
//                        Manifest.permission.READ_PHONE_STATE
//                });
    }

    /**
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    /**
     * 权限获取成功
     */
    @PermissionSuccess(requestCode = 100)
    public void getPerMissionSuccess() {
        //Snackbar.make(LoginLin,"权限获取成功！",Snackbar.LENGTH_SHORT).show();
    }

    @PermissionFail(requestCode = 100)
    public void getPerMissionFailed() {
        AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this)
                .setTitle(R.string.lackPermission)
                .setMessage(R.string.lackPermission_message)
                .setCancelable(false)
                .setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .show();
    }

    //
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(ConsUtils.PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }

    private void initView() {
        toolbar.setTitle(R.string.login);
        setUpToolBar(toolbar, true, true);
        buttonLogin.setOnClickListener(this);
        buttonRegist.setOnClickListener(this);
    }

    @Override
    public void onPanelOpened(View view) {
        super.onPanelOpened(view);
        ComUtils.hideInput(LoginActivity.this, LoginLin);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("test", "xiaohuile");
    }

    private void showDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("登录...");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void DismissDialog() {
        dialog.dismiss();
    }


    /*
    * 获取登录数据
    * */
    private void getLoginData() {
        Account = myAccount.getText().toString();
        Password = myPassword.getText().toString();
    }

    /*
    * 登录的方法
    * */
    private void Login() {
        showDialog();
        getLoginData();
        if (!isEmpty()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    BeginLogin();
                }
            }).start();
        } else {
            handler.sendEmptyMessage(ConsUtils.EDIT_COMPLETE);
        }
    }

    //beginLogin
    public void BeginLogin() {
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("account", Account);
            jsonParam.put("password", Password);
            login.UrlLogin(ApiUtils.LoginUrl, jsonParam);
            login.LoginUpEd();//不可少
            Log.d("test", String.valueOf(info.isLogin()));
            if (info.isLogin()) {
                SessionId = info.getSessionId();
                getCurrentUserInfo();
                handler.sendEmptyMessage(ConsUtils.LOGIN_SUCCESS);
            }else{
                handler.sendEmptyMessage(ConsUtils.LOGIN_FAILED);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //获取当前用户信息
    public void getCurrentUserInfo() throws JSONException {
        JSONObject userInfo = new JSONObject();
        userInfo.put("account", Account);
        userInfo.put("sessionID", SessionId);
        login.getCurrentUserInfo(ApiUtils.GetUserInfoUrl, userInfo);
        NickName = info.getNickname();
        Age = info.getAge();
        Log.d("login", NickName + Age + HeadImgUrl);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConsUtils.LOGIN_SUCCESS:
                    DismissDialog();
                    EventBus.getDefault().post(new MyEventLogin(true, NickName));
                    sp.saveCurrentUser(Account, NickName, Age, true, info.getSessionId());
                    Intent intent = new Intent(LoginActivity.this, MeActivity.class);
                    HeadImgUrl=ApiUtils.GetUserImg+Account+".jpg";
                    intent.putExtra("url", HeadImgUrl);
                    startActivity(intent);
                    finish();
                    break;
                case ConsUtils.LOGIN_FAILED:
                    DismissDialog();
                    Snackbar.make(LoginLin, "登录失败！账号或密码错误", Snackbar.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    /*
    * 判断是否账号密码是否为空
    * */
    private boolean isEmpty() {
        return Account == null || Password == null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_regist:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.button_login:
                ComUtils.hideInput(this,LoginLin);
                Login();
                break;
        }
    }

}
