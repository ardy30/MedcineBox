package com.newthread.medicinebox.utils.UserUtils;

import android.util.Log;

import com.newthread.medicinebox.bean.UserInfo;
import com.newthread.medicinebox.utils.HttpUtils.UrlConnectionUtils;
import com.newthread.medicinebox.utils.UrlConnectionPost.UrlConnectionFilePost;
import com.newthread.medicinebox.utils.UrlConnectionPost.UrlConnectionJsonPost;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * 更新用户信息
 * Created by 张浩 on 2016/3/9.
 */
public class UpdateUserInfo {
    String response="";
    String fileResponse="";
    UrlConnectionJsonPost jsonPost=new UrlConnectionJsonPost();
    UrlConnectionFilePost filePost=new UrlConnectionFilePost();
    UserInfo info = new UserInfo();
    public static UpdateUserInfo UpdateInstance;
    public static UpdateUserInfo getInstance(){
        if (UpdateInstance==null)
            UpdateInstance=new UpdateUserInfo();
        return UpdateInstance;
    }
    /**
     * 更新用户信息
     * @param url
     * @param jsonObject
     */
    public void UrlUpdateUserInfo(String url,JSONObject jsonObject){
        response=jsonPost.UrlPostJson(url,jsonObject);
        //Log.d("response",response);
    }

    /**
     *
     * @param param //表单参数
     * @param fileFormName
     * @param upLoadFile
     * @param newFileName
     * @param urlStr
     * @throws IOException
     */
    public void UrlFileUpLoad(Map<String, String> param, String fileFormName,
                              File upLoadFile, String newFileName, String urlStr) throws IOException {
       fileResponse=filePost.upLoadFile(param, fileFormName, upLoadFile, newFileName, urlStr, new UrlConnectionFilePost.HttpCallBackListener() {
           @Override
           public void onSuccess(String response) {

           }

           @Override
           public void onError(Exception e) {

           }
       });
    }


    /*
    * 更新的判断
    * */
    public void UpdateEd()throws JSONException {
        JSONObject updateJson= UrlConnectionUtils.getJSON(response);
        //Log.d("updateinfo", String.valueOf(updateJson));
        switch (updateJson.getString("code")){
            case "N01":
                info.setUpdated(true);
                break;
            case "E02":
                info.setUpdated(false);
                break;
        }
    }

        /*
      * 上传头像文件的response的解析
      *
      *
              *
              *     {
                "code": "N01",
                "message": "上传成功",
                "contents": {
                    "fileSubPath": "1457869808834.281079145@qq.com.jpg"
                }
            }
      *
      *
      * */
    public void FileUpLoadEd(String fileResponse) throws JSONException {
        JSONObject fileJson=UrlConnectionUtils.getJSON(fileResponse);
        switch (fileJson.getString("code")){
            case "N01":
                Log.d("status",fileJson.getString("message"));
                info.setFileUpLoaded(true);
                break;
            case "E02":
                info.setFileUpLoaded(false);
                Log.d("status",fileJson.getString("message"));
                break;
        }

    }
    public UserInfo getInfo() {
        return info;
    }




}
