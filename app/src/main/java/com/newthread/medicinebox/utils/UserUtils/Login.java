package com.newthread.medicinebox.utils.UserUtils;

import android.util.Log;

import com.newthread.medicinebox.bean.UserInfo;
import com.newthread.medicinebox.utils.JsonUtils.JsonHelper;
import com.newthread.medicinebox.utils.UrlConnectionPost.UrlConnectionJsonPost;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 张浩 on 2016/3/8.
 */
public class Login {
    String response="";
    String UserInfoJson;
    public UserInfo getInfo() {
        return info;
    }
    UserInfo info = new UserInfo();
    UrlConnectionJsonPost jsonPost = new UrlConnectionJsonPost();
    public static Login LoginInstance;
    public static Login getInstance() {
        Log.d("test","被创建");
        if (LoginInstance == null)
            LoginInstance = new Login();
        return LoginInstance;
    }

    /**
     * 登录客户端
     * @param Url
     * @param object
     */
    public void UrlLogin(String Url,JSONObject object){
        response=jsonPost.UrlPostJson(Url,object);
       // Log.d("responselogin",response);
    }


    /**
     *得到当前用户信息
     * @param url
     * @param object
     */
    public void getCurrentUserInfo(String url,JSONObject object) throws JSONException {
        UserInfoJson=jsonPost.UrlPostJson(url,object);
        if (!UserInfoJson.equals("")){
            JSONObject userObject=JsonHelper.getJSON(UserInfoJson);
            Log.d("jsonObject", String.valueOf(userObject));
            Log.d("test", String.valueOf(userObject));
            switch (userObject.getString("code")){
                case "N01":
                    info.setLoginMessage(userObject.getString("message"));
                    JSONObject userInfo=userObject.getJSONObject("contents");
                    info.setNickname(userInfo.getString("userVirtualName"));
                    info.setHeadImgUrl(userInfo.getString("userPicture"));
                    Log.d("test11111",userInfo.getString("userAge"));
                    if (userInfo.getString("userAge").equals("null")){
                        info.setAge(" ");
                        Log.d("test","222");
                    }else{
                        Log.d("test","333");
                        info.setAge(userInfo.getString("userAge"));
                    }
                    break;
                case "E02":
                    info.setLoginMessage(userObject.getString("message"));
                    break;
            }
        }else{
            info.setLoginMessage("404");
        }
    }

    //判断是否注册成功
    public void LoginUpEd(){
        if (!response.equals("")){
            try {
                JSONObject object= JsonHelper.getJSON(response);
                switch (object.getString("message")){
                    case "登录成功":
                        Log.d("te", "登录成功");
                        JSONObject object1=object.getJSONObject("contents");
                        info.setLogin(true);
                        info.setSessionId(object1.getString("sessionID"));
                        break;
                    case "密码错误":
                        info.setLogin(false);
                        Log.d("te", "密码错误");
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }






}
