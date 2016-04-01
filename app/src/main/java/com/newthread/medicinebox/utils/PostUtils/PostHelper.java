package com.newthread.medicinebox.utils.PostUtils;

import android.util.Log;

import com.newthread.medicinebox.utils.JsonUtils.JsonHelper;
import com.newthread.medicinebox.utils.UrlConnectionPost.UrlConnectionJsonPost;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 张浩 on 2016/3/30.
 */
public class PostHelper {
    UrlConnectionJsonPost jsonPost=new UrlConnectionJsonPost();
    //点赞的回调接口
    public interface OnLikedListener{
        String OnLikedSuccess(String success);
        String OnLikedFailed(String fail);
    }

    /**
     * 点赞接口
     * @param url
     * @param jsonObject
     * @param listener
     * @throws JSONException
     */
    public void SendZan(String url,JSONObject jsonObject ,OnLikedListener listener) throws JSONException {
       String Result=jsonPost.UrlPostJson(url, jsonObject);
        Log.d("zan",Result);
        if (Result!=null){
            JSONObject object= JsonHelper.getJSON(Result);
            if (object.getString("code").equals("N01")){
                if (listener!=null){
                    listener.OnLikedSuccess(object.getString("message"));
                }
            }else {
                if (listener!=null){
                    listener.OnLikedFailed(object.getString("message"));
                }
            }
        }

    }
}
