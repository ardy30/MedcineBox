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
import android.widget.TextView;

import com.newthread.medicinebox.R;
import com.newthread.medicinebox.bean.mUser;
import com.newthread.medicinebox.theme.StatusBarCompat;
import com.newthread.medicinebox.ui.activity.SwipeBackActivity;
import com.newthread.medicinebox.utils.ConsUtils;
import com.newthread.medicinebox.utils.UserUtils.CurrentUserSp;
import com.newthread.medicinebox.utils.EventBusUtils.MyEventLogin;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import de.greenrobot.event.EventBus;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * 登录的Activity
 * Created by 张浩 on 2016/1/19.
 */
public class LoginActivityBmob extends SwipeBackActivity implements View.OnClickListener {
    @Bind(R.id.my_account)
    EditText myAccount;
    @Bind(R.id.my_password)
    EditText myPassword;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button button_login, button_regist;
    public static LoginActivityBmob a;
    private String account;
    private String password;
    private String NickName;
    private int Age;
    private String url;
    private  mUser user;
    @Bind(R.id.LoginLin)
    LinearLayout LoginLin;
    ProgressDialog dialog;
    CurrentUserSp sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        initView();
        sp=new CurrentUserSp(this);
        a = this;
        initPermission();
    }

    /**
     * 处理权限啊
     */
    private void initPermission() {
        PermissionGen.with(LoginActivityBmob.this)
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
     *
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
    public void getPerMissionSuccess(){
        //Snackbar.make(LoginLin,"权限获取成功！",Snackbar.LENGTH_SHORT).show();
    }
    @PermissionFail(requestCode = 100)
    public void getPerMissionFailed(){
        AlertDialog dialog=new AlertDialog.Builder(LoginActivityBmob.this)
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
        initToolBar();
        button_regist = (Button) findViewById(R.id.button_regist);
        button_login = (Button) findViewById(R.id.button_login);
        button_login.setOnClickListener(this);
        button_regist.setOnClickListener(this);
        StatusBarCompat.compat(this, getResources().getColor(R.color.colorPrimaryDark));
    }

    /*
    *
    * */
    private void initToolBar() {
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setVisibility(View.GONE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.login);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("test", "xiaohuile");
    }
    private void showDialog(){
        dialog=new ProgressDialog(this);
        dialog.setMessage("登录...");
        dialog.setCancelable(false);
        dialog.show();
    }
    private void DismissDialog(){
        dialog.dismiss();
    }


    /*
    * 获取登录数据
    * */
    private void getLoginData(){
        account=myAccount.getText().toString();
        password=myPassword.getText().toString();
    }

    /*
    * 登录的方法
    * */
    private void Login(){
       showDialog();
       getLoginData();
        if (!isEmpty()){
           new Thread(new Runnable() {
               @Override
               public void run() {
                   user=new mUser();
                   user.setUsername(account);
                   user.setPassword(password);
                   Log.d("test",account+password);
                   user.login(LoginActivityBmob.this, new SaveListener() {
                       @Override
                       public void onSuccess() {
                           getCurrentUser();//得到当前用户
                           handler.sendEmptyMessage(ConsUtils.LOGIN_SUCCESS);

                       }

                       @Override
                       public void onFailure(int i, String s) {
                           handler.sendEmptyMessage(ConsUtils.LOGIN_FAILED);
                       }
                   });
               }
           }).start();
        }else{
            handler.sendEmptyMessage(ConsUtils.EDIT_COMPLETE);
        }
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ConsUtils.LOGIN_SUCCESS:
                    DismissDialog();
                    EventBus.getDefault().post(new MyEventLogin(true,NickName));
                    sp.saveCurrentUser(account,NickName,"",true,"");
                    Intent intent=new Intent(LoginActivityBmob.this, MeActivityBmob.class);
                    intent.putExtra("url",url);
                    startActivity(intent);
                    finish();
                    break;
                case ConsUtils.LOGIN_FAILED:
                    DismissDialog();
                    Snackbar.make(LoginLin,"账号或密码错误",Snackbar.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    /*
    * 判断是否账号密码是否为空
    * */
    private boolean isEmpty(){
        return account == null || password == null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_regist:
                Intent intent = new Intent(LoginActivityBmob.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.button_login:
                Login();
                break;
        }
    }


    /*
    * bmob的获取当前用户的方法，和Sp一样
    * */
    private void getCurrentUser(){
        mUser user1=BmobUser.getCurrentUser(LoginActivityBmob.this,mUser.class);
        NickName=user1.getNickName();
        Age=user1.getAge();
        account=user1.getUsername();
        url=user1.getHeadimg();
        System.out.println("+++++++++"+NickName+"+++++"+Age);
    }

}
