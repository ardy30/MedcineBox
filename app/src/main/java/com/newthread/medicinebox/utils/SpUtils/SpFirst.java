package com.newthread.medicinebox.utils.SpUtils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 张浩 on 2016/2/21.
 */
public class SpFirst {
    public Context context;
    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;
    public SpFirst(Context context){
        this.context=context;
        editor=context.getSharedPreferences("firstUse",Context.MODE_PRIVATE).edit();
        preferences=context.getSharedPreferences("firstUse", Context.MODE_PRIVATE);
    }

    public void saveFirst(Boolean first){
        editor.putBoolean("first",first);
        editor.commit();
    }
    public Boolean getFirst(){
        return  preferences.getBoolean("first",false);
    }
}
