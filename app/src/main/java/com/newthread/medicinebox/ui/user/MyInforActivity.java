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
import com.newthread.medicinebox.bean.UserInfo;
import com.newthread.medicinebox.ui.activity.SwipeBackActivity;
import com.newthread.medicinebox.utils.ApiUtils;
import com.newthread.medicinebox.utils.BitmapUtils;
import com.newthread.medicinebox.utils.ConsUtils;
import com.newthread.medicinebox.utils.EventBusUtils.MyEventLogin;
import com.newthread.medicinebox.utils.FileUtils;
import com.newthread.medicinebox.utils.NetWorkImageUtils.PicassoPostImageHelper;
import com.newthread.medicinebox.utils.UrlConnectionPost.UrlConnectionFilePost;
import com.newthread.medicinebox.utils.UserUtils.CurrentUserSp;
import com.newthread.medicinebox.utils.UserUtils.UpdateUserInfo;
import com.soundcloud.android.crop.Crop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * 个人详细信息
 * Created by 张浩 on 2016/1/21.
 */
public class MyInforActivity extends SwipeBackActivity {
    @Bind(R.id.toolbar_title)
    TextView toolbar_title;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private LinearLayout myInfo;
    private CircleImageView circleImage;
    private String[] items = {"相册", "拍照"};
    private String nickname;
    private String age;
    private EditText NickName;
    private EditText Age;
    private ProgressDialog dialog;
    private String account;
    private String imgPath;
    private String sessionId;
    private Uri photo;
    CurrentUserSp sp;
    UpdateUserInfo updateUserInfo;
    UserInfo info;
    File file=new File(ConsUtils.path);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfo);
        ButterKnife.bind(this);
        init();
        initUserData();
        initView();
        initHeadImg();
    }

    private void init() {
        sp = new CurrentUserSp(this);
        updateUserInfo = UpdateUserInfo.getInstance();
        info = updateUserInfo.getInfo();
        sessionId = sp.getSessionId();
    }

    /*
    * 初始化头像
    * */
    private void initHeadImg() {
        if (FileUtils.fileIsExists(ConsUtils.path_img)) {
            circleImage.setImageBitmap(BitmapUtils.getLoacalBitmap(ConsUtils.path_img));
        }
    }

    /*
    *
    * 获取传过来的数据
    * */
    private void initUserData() {
        nickname = sp.getNickName();
        age = sp.getAge();
        account = sp.getAccount();
    }

    /*
    * */
    private void initView() {
        toolbar.setTitle("");
        setUpToolBar(toolbar, true, true);
        toolbar_title.setText(R.string.detail_info);
        myInfo = (LinearLayout) findViewById(R.id.myinfo_lin);
        circleImage = (CircleImageView) findViewById(R.id.myInfo_head_img);
        NickName = (EditText) findViewById(R.id.info_name);
        Age = (EditText) findViewById(R.id.info_age);
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
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_regist:
                ShowUpLoadDia();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (file.exists()){
                            imgPath=ApiUtils.GetUserImg+sp.getAccount()+".jpg";
                            UpLoadHeadFile();
                        }else{
                            UpdateUserInfo();
                        }

                    }
                }).start();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 上传头像
     */
    private void UpLoadHeadFile(){
        Map<String,String> param=new HashMap<>();
        param.put("account",sp.getAccount());
        param.put("sessionId",sp.getSessionId());
        UrlConnectionFilePost filePost=new UrlConnectionFilePost();
        try {
            filePost.upLoadFile(param, "file", file, "", ApiUtils.UpLoadUserPic, new UrlConnectionFilePost.HttpCallBackListener() {
                @Override
                public void onSuccess(String response) {
                    UpdateUserInfo updated=new UpdateUserInfo();
                    UserInfo info=updated.getInfo();
                    try {
                        updated.FileUpLoadEd(response);
                        if (info.isFileUpLoaded()){
                            UpdateUserInfo();
                        }else{
                            handler.sendEmptyMessage(ConsUtils.SET_INFORMATION_FAILED);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println("4444444" + response);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
    * 跟新用户信息
    * */
    private void UpdateUserInfo() {
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("account", account);
            jsonParam.put("sessionID", sessionId);
            jsonParam.put("userVirtualName", NickName.getText());
            jsonParam.put("userAge", Age.getText());
            jsonParam.put("userPosition", "湖北武汉");
            Log.d("updateinfo", String.valueOf(jsonParam));
            updateUserInfo.UrlUpdateUserInfo(ApiUtils.UpdateUserInfo, jsonParam);
            updateUserInfo.UpdateEd();
            if (info.isUpdated()) {
                handler.sendEmptyMessage(ConsUtils.SET_INFORMATION_DONE);
            } else {
                handler.sendEmptyMessage(ConsUtils.SET_INFORMATION_FAILED);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /*
    * 显示上传的dia
    * */
    private void ShowUpLoadDia() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("上传中...");
        dialog.setCancelable(false);
        dialog.show();
    }

    /*
    * dismissdia
    * */
    private void DisMissDia() {
        dialog.dismiss();
    }


    /*
    * handler
    * */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConsUtils.SET_INFORMATION_DONE:
                    DisMissDia();
                    Intent intent = new Intent();
                    intent.setData(photo);
                    intent.putExtra("nickname", NickName.getText().toString());
                    intent.putExtra("age", Age.getText().toString());
                    intent.putExtra("url", imgPath);
                    sp.saveCurrentUser(account, NickName.getText().toString(), Age.getText().toString(), true, sessionId);
                    setResult(ConsUtils.SET_INFORMATION_DONE, intent);
                    if (imgPath!=null){
                        PicassoPostImageHelper helper=new PicassoPostImageHelper();
                        helper.ClearImgByUrl(MyInforActivity.this,ApiUtils.GetUserImg+imgPath);
                        EventBus.getDefault().post(new MyEventLogin(NickName.getText().toString(), imgPath));
                        Log.d("name", NickName.getText().toString()+"myInFo+url!=null");
                    }
                    Log.d("name", NickName.getText().toString() + "myInFo");
                    finish();
                    break;
                case ConsUtils.RIGHT_AGE:
                    DisMissDia();
                    Snackbar.make(myInfo, "请输入正确年龄", Snackbar.LENGTH_SHORT).show();
                    break;
                case ConsUtils.COMPLETE_NAME:
                    DisMissDia();
                    Snackbar.make(myInfo, "其输入名字", Snackbar.LENGTH_SHORT).show();
                    break;
                case ConsUtils.SET_INFORMATION_FAILED:
                    DisMissDia();
                    Snackbar.make(myInfo, "更新失败!", Snackbar.LENGTH_SHORT).show();
                    break;
            }

        }
    };


    /*
    * 设置头像的dialog
    * */
    private void showDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
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
                                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case Crop.REQUEST_PICK:
                    beginCrop(data.getData());
                    break;
                case Crop.REQUEST_CROP:
                    handleCrop(resultCode, data);
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
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped.jpg"));
        Crop.of(source, destination).asSquare().start(this);
    }

    /*
    * 裁剪
    * */
    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            photo = Crop.getOutput(result);
            circleImage.setImageURI(photo);
            FileUtils.delete("sdcard/crop");
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
