package com.newthread.medicinebox.ui.user;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newthread.medicinebox.R;
import com.newthread.medicinebox.theme.StatusBarCompat;
import com.newthread.medicinebox.ui.activity.DeveloperActivity;
import com.newthread.medicinebox.ui.activity.SwipeBackActivity;
import com.newthread.medicinebox.ui.remind.MyRemindListActivity;
import com.newthread.medicinebox.utils.BitmapUtils;
import com.newthread.medicinebox.utils.ConsUtils;
import com.newthread.medicinebox.utils.UserUtils.CurrentUserSp;
import com.newthread.medicinebox.utils.FileUtils;
import com.newthread.medicinebox.utils.EventBusUtils.MyEventLogin;
import com.newthread.medicinebox.utils.NetWorkImageUtils.HeadImageHelper;

import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 张浩 on 2016/1/20.
 */
public class MeActivityBmob extends SwipeBackActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private RelativeLayout goto_more,goto_remind;
    private String nickname;
    private String age;
    private TextView NickName;
    private TextView Age;
    private CircleImageView image;
    private String url;
    CurrentUserSp sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me);
        sp=new CurrentUserSp(this);
        initLoginData();
        initView();
        //LoadHeadImg();
        initHeadImg();
        //initNavigationHead(nickname,url);
    }

    /*
    *
    * 初始化头像
    * */
    private void initHeadImg() {
        if (FileUtils.fileIsExists(ConsUtils.path_img)){
            image.setImageBitmap(BitmapUtils.getLoacalBitmap(ConsUtils.path_img));
        }

    }


    /*
    * 初始化登录数据库
    * */
    private void initLoginData() {
        nickname=sp.getNickName();
        age=sp.getAge();
        Log.d("userinfo",nickname+age);
        EventBus.getDefault().post(new MyEventLogin(true,nickname));
    }

    private void LoadHeadImg(){
        Intent intent=getIntent();
        url=intent.getStringExtra("url");
        if (!(url==null)) {
            //缓存头像,登录的状态
            HeadImageHelper utils = new HeadImageHelper(this);
            utils.getHead_Img(url, image, R.drawable.img_login1, R.drawable.img_login1);

        }else{
            //已经登录了
            Log.d("login","已登录，不需要缓存");
        }
    }

    /*
    * 在直接登录后缓存头像后，给MainActivity传值，改变NavigationHead的头像
    * EventBus
    * */
    private void initNavigationHead(String name,String url) {
        Log.d("test",name+url+"11111111111111");
        MyEventLogin event=new MyEventLogin(name,url);
        EventBus.getDefault().post(event);
    }


    /*
    * */
    private void initView() {
        initToolBar();
        NickName= (TextView) findViewById(R.id.NickName);
        image= (CircleImageView) findViewById(R.id.head_image_me_1);
        Age= (TextView) findViewById(R.id.Age);
        NickName.setText("昵称:"+nickname);
        Age.setText("年龄:"+age);
        goto_more= (RelativeLayout) findViewById(R.id.goto_more);
        goto_more.setOnClickListener(this);
        goto_remind= (RelativeLayout) findViewById(R.id.goto_remind);
        goto_remind.setOnClickListener(this);
        StatusBarCompat.compat(this, getResources().getColor(R.color.colorPrimaryDark));
    }
    /*
    * */
    private void initToolBar() {
        toolbar_title= (TextView) findViewById(R.id.toolbar_title);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar_title.setText(R.string.me);
        toolbar.setTitle("");
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
    * 各种点击事件
    * */
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.goto_more:
                intent=new Intent(MeActivityBmob.this,MyInforActivityBmob.class);
                intent.putExtra("nickname",nickname);
                intent.putExtra("age",age);
                startActivityForResult(intent, ConsUtils.SET_INFORMATION);
                break;
            case R.id.goto_remind:
                startActivity(new Intent(MeActivityBmob.this, MyRemindListActivity.class));
                break;
            default:
                startActivity(new Intent(this, DeveloperActivity.class));
                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==ConsUtils.SET_INFORMATION_DONE)
        {
            image.setImageURI(data.getData());
            String name=data.getStringExtra("nickname");
            NickName.setText("昵称:"+name);
            Age.setText("年龄:" + data.getStringExtra("age"));
            String url=data.getStringExtra("url");
            if (url!=null){
                Log.d("url+1", url);
                HeadImageHelper utils=new HeadImageHelper(this);
                utils.getHead_Img(url, image, R.drawable.img_login1, R.drawable.img_login1);
            }
            FileUtils.delete(ConsUtils.path);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_signout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.SignOut){
           showDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog(){
        AlertDialog dialog=new AlertDialog.Builder(this)
                .setMessage(R.string.LoginOut)
                .setCancelable(true)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SignOut();
                        dialog.dismiss();
                        finish();
                    }
                }).show();

    }

    /*
    * 注销登录的方法
    *
    * */
    private void SignOut(){
        sp.ChangeLoginState(false);
        FileUtils.delete(ConsUtils.path_sp);
        FileUtils.delete(ConsUtils.path_img);
        EventBus.getDefault().post(new MyEventLogin(null,R.drawable.img_login1));
    }
}
