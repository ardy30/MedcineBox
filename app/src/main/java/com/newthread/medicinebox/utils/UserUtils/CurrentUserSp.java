package com.newthread.medicinebox.utils.UserUtils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 张浩 on 2016/2/2.
 */
public class CurrentUserSp {
    public Context context;
    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;
    /*public static String NickName = "";
    public static int Age=0;*/
    public CurrentUserSp(Context context){
        this.context =context;
        editor=context.getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        preferences=context.getSharedPreferences("data",Context.MODE_PRIVATE);
    }
    /*
    * 保存数据
    * */
    public void saveCurrentUser(String account,String NickName,String age,boolean isLogin,String session){
        editor.putString("account",account);
        editor.putBoolean("login", isLogin);
        editor.putString("nickname", NickName);
        editor.putString("session",session);
        editor.putString("age", age);
        editor.commit();
    }
    /*
    * 单独改变登录状态的方法
    * */
    public void ChangeLoginState(boolean loginState){
        editor.putBoolean("login", loginState);
        editor.commit();
    }

    /*
    * 获取用户信息
    * */
    public boolean getLoginState(){
       return preferences.getBoolean("login",false);
    }
    public String getNickName(){
        //NickName=preferences.getString("nickname","");
        return  preferences.getString("nickname", "");
    }
    public String getAge(){
        //Age=preferences.getInt("age",0);
        return preferences.getString("age","222");
    }

    public String getSessionId(){
        return preferences.getString("session","");
    }
    public String getAccount(){
        return preferences.getString("account","");
    }
}
