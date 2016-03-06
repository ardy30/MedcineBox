package com.newthread.medicinebox.utils.EventBusUtils;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;

/**
 * Created by 张浩 on 2016/2/6.
 */
public class MyEventLogin {
    private  String nickname;
    private String Url;
    private int ResId;
    private boolean isLogin;
    public MyEventLogin(String name){
        nickname=name;
    }
    public MyEventLogin(String name, String url){
        nickname=name;
        Url=url;
    }
    public MyEventLogin(String name, int id){
        nickname=name;
        ResId=id;
    }
    public MyEventLogin(boolean Login){
        isLogin=Login;
    }
    public String getName(){
        return nickname;
    }
    public String getUrl(){
        return Url;
    }
    public int getResId(){
        return  ResId;
    }
    public boolean getIsLogin(){
        return isLogin;
    }
}
