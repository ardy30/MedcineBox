package com.newthread.medicinebox.utils.HelpUtils;

import android.content.Context;
import android.util.Log;

import com.newthread.medicinebox.bean.HelpBean;
import com.newthread.medicinebox.bean.mUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 张浩 on 2016/2/18.
 */
public class HelpDataHelper {
    public Context context;
    public mUser user;
    public HelpDataHelper(Context context){
        this.context=context;
        user= BmobUser.getCurrentUser(context,mUser.class);
    }
//    public void getHelpData(final ArrayList<Map<String,Object>> HelpLists){
//        BmobQuery<HelpBean> query=new BmobQuery<>();
//        query.findObjects(context, new FindListener<HelpBean>() {
//            @Override
//            public void onSuccess(List<HelpBean> list) {
//                for (HelpBean bean:list){
//                    Map<String ,Object> map=new HashMap<>();
//                    map.put("medicine",bean.getMedicine());
//                    map.put("question",bean.getQuestion());
//                    map.put("time",bean.getUpdatedAt());
//                   // map.put("usename",bean.getUsername());
//                    HelpLists.add(map);
//                 /*   System.out.println("-----------------------");
//                    System.out.println(HelpLists);
//                    System.out.println(HelpLists.size());*/
//                }
//
//
//            }
//
//            @Override
//            public void onError(int i, String s) {
//                Log.d("error",s);
//            }
//        });
//    }



  /*  public String getUserData(String username,
                              final ArrayList<Map<String,Object>> HelpLists){
        BmobQuery<mUser> query=new BmobQuery<>();
        query.addWhereEqualTo("username",username);
        query.findObjects(context, new FindListener<mUser>() {
            @Override
            public void onSuccess(List<mUser> list) {
                for (mUser user:list){
                    Map<String,Object> map=new HashMap<>();
                    map.put("userimg",user.getHeadimg());
                    map.put("nickname", user.getHeadimg());
                     HelpLists.add(map);
                }
            }
            @Override
            public void onError(int i, String s) {
            }
        });
        return "";
    }*/
}
