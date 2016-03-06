package com.newthread.medicinebox.ui.user;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.newthread.medicinebox.R;
import com.newthread.medicinebox.bean.mUser;
import com.newthread.medicinebox.theme.StatusBarCompat;
import com.newthread.medicinebox.ui.activity.SwipeBackActivity;
import com.newthread.medicinebox.ui.view.CircleImage;
import com.newthread.medicinebox.utils.BitmapUtils;
import com.newthread.medicinebox.utils.ConsUtils;
import com.newthread.medicinebox.utils.UserUtils.CurrentUserSp;
import com.newthread.medicinebox.utils.FileUtils;
import com.newthread.medicinebox.utils.EventBusUtils.MyEventLogin;
import com.soundcloud.android.crop.Crop;
import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * 个人详细信息
 * Created by 张浩 on 2016/1/21.
 */
public class MyInforActivity extends SwipeBackActivity {
    private LinearLayout myInfo;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private CircleImageView circleImage;
    private String[] items={"相册","拍照"};
    private String nickname;
    private int age;
    private EditText NickName;
    private EditText Age;
    private ProgressDialog dialog;
    private String account;
    private String imgPath;
    private Uri photo;
    CurrentUserSp sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfo);
        sp=new CurrentUserSp(this);
        initUserData();
        initView();
        initHeadImg();
    }

    /*
    * 初始化头像
    * */
    private void initHeadImg() {
        if (FileUtils.fileIsExists(ConsUtils.path_img)){
            circleImage.setImageBitmap(BitmapUtils.getLoacalBitmap(ConsUtils.path_img));
        }
    }

    /*
    *
    * 获取传过来的数据
    * */
    private void initUserData() {
        Intent intent=getIntent();
        nickname=intent.getStringExtra("nickname");
        age=intent.getIntExtra("age", 0);
    }
    /*
    * */
    private void initView() {
        initToolbar();
        StatusBarCompat.compat(this, getResources().getColor(R.color.colorPrimaryDark));
        myInfo= (LinearLayout) findViewById(R.id.myinfo_lin);
        circleImage= (CircleImageView) findViewById(R.id.myInfo_head_img);
        NickName= (EditText) findViewById(R.id.info_name);
        Age= (EditText) findViewById(R.id.info_age);
        NickName.setText(nickname);
        Age.setText(String.valueOf(age));
        circleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_regist, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_regist:
                ShowUpLoadDia();
                UpLoadImg();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    * 显示上传的dia
    * */
    private void ShowUpLoadDia(){
        dialog=new ProgressDialog(this);
        dialog.setMessage("上传中...");
        dialog.setCancelable(false);
        dialog.show();
    }
    /*
    * dismissdia
    * */
    private void DisMissDia(){
        dialog.dismiss();
    }

    /*
    * 保存用户信息
    * */
    private void UpLoadUser(){
        mUser newUser=new mUser();
        mUser user=BmobUser.getCurrentUser(this,mUser.class);
        account=user.getUsername();
        newUser.setHeadimg(imgPath);
        if (Integer.parseInt(Age.getText().toString())<200){
            newUser.setAge(Integer.parseInt(Age.getText().toString()));
        }else{
            handler.sendEmptyMessage(ConsUtils.RIGHT_AGE);
        }
        if (NickName.getText().toString()!=""){
            newUser.setNickName(NickName.getText().toString());
        }else{
            handler.sendEmptyMessage(ConsUtils.COMPLETE_NAME);
        }


        newUser.update(this, user.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                handler.sendEmptyMessage(ConsUtils.SET_INFORMATION_DONE);
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });

    }

    /*
    * 上传头像
    * */
    private void UpLoadImg(){
        final BmobFile file=new BmobFile(new File(ConsUtils.path));
        if (FileUtils.fileIsExists(ConsUtils.path)){
            file.uploadblock(this, new UploadFileListener() {
                @Override
                public void onSuccess() {
                    System.out.println(file.getFileUrl(MyInforActivity.this));
                    imgPath= file.getFileUrl(MyInforActivity.this);
                    UpLoadUser();
                }
                @Override
                public void onFailure(int i, String s) {

                }
            });
        }else{
            UpLoadUser();
        }

    }

    /*
    * handler
    * */
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ConsUtils.SET_INFORMATION_DONE:
                    DisMissDia();
                    Intent intent=new Intent();
                    intent.setData(photo);
                    intent.putExtra("nickname", NickName.getText().toString());
                    intent.putExtra("age", Age.getText().toString());
                    intent.putExtra("url", imgPath);
                    sp.saveCurrentUser(account, NickName.getText().toString(), Integer.parseInt(Age.getText().toString()), true);
                    setResult(ConsUtils.SET_INFORMATION_DONE, intent);
                    if (imgPath!=null){
                        EventBus.getDefault().post(new MyEventLogin(NickName.getText().toString(),imgPath));
                        Log.d("name", NickName.getText().toString()+"myInFo+url!=null");
                    }
                    Log.d("name",NickName.getText().toString()+"myInFo");
                    EventBus.getDefault().post(new MyEventLogin(NickName.getText().toString()));
                    finish();
                    break;
                case ConsUtils.RIGHT_AGE:
                    DisMissDia();
                    Snackbar.make(myInfo,"请输入正确年龄",Snackbar.LENGTH_SHORT).show();
                    break;
                case ConsUtils.COMPLETE_NAME:
                    DisMissDia();
                    Snackbar.make(myInfo,"其输入名字",Snackbar.LENGTH_SHORT).show();
                    break;
            }

        }
    };


            /*
            * 设置头像的dialog
            * */
    private void showDialog() {
        AlertDialog alertDialog=new AlertDialog.Builder(this)
                .setTitle(R.string.sethead)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Crop.pickImage(MyInforActivity.this);
                                dialog.dismiss();
                                break;
                            case 1:
                                Intent camera= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File("sdcard", "crop")));
                                startActivityForResult(camera, ConsUtils.CAMERA_REQUEST_CODE);
                                dialog.dismiss();
                                break;
                        }
                    }
                }).show();
    }


    /*
    * 裁剪的过程
    * */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!=RESULT_CANCELED){
            switch (requestCode)
            {
                case Crop.REQUEST_PICK:
                    beginCrop(data.getData());
                    break;
                case Crop.REQUEST_CROP:
                    handleCrop(resultCode,data);
                    break;
                case ConsUtils.CAMERA_REQUEST_CODE:
                    File f = new File("sdcard", "crop");
                    beginCrop(Uri.fromFile(f));
                    break;
            }
        }
    }

    /*
    * 开始裁剪
    * */
    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped.png"));
        Crop.of(source, destination).asSquare().start(this);
    }
    /*
    * 裁剪
    * */
    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            photo=Crop.getOutput(result);
            circleImage.setImageURI(photo);
            FileUtils.delete("sdcard/crop");
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /*
        *
        * */
    private void initToolbar() {
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar_title= (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setVisibility(View.GONE);
        toolbar.setTitle(R.string.detail_info);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
