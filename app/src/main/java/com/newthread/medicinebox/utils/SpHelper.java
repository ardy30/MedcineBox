package com.newthread.medicinebox.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 张浩 on 2016/2/15.
 */
public class SpHelper {
    public Context context;
    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;
    public SpHelper(Context context){
        this.context=context;
        editor=context.getSharedPreferences("ImgUrl",Context.MODE_PRIVATE).edit();
        preferences=context.getSharedPreferences("ImgUrl", Context.MODE_PRIVATE);
    }
    public SpHelper(Context context,String spName){
        this.context=context;
        editor=context.getSharedPreferences(spName,Context.MODE_PRIVATE).edit();
        preferences=context.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }
    /*
    * saveImgList
    * */
    public void saveImgList(String page,String imgurl){
        editor.putString(page,imgurl);
        editor.commit();
    }
    /*
    * getImgList
    * */
    public String getImgList(String page){
        return preferences.getString(page,"");
    }

    /**
     * 搜索数据保存
     * @param num
     * @param query
     */
    public void saveSearchList(String num,String query){
        editor.putString(num,query);
        editor.commit();
    }

    /**
     * 搜索数据取出
     * @param num
     * @return
     */
    public String getSearchStr(String num){
        return preferences.getString(num,"");
    }
}
