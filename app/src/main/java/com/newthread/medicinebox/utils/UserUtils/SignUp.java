package com.newthread.medicinebox.utils.UserUtils;

import android.util.Log;
import com.newthread.medicinebox.utils.ApiUtils;
import com.newthread.medicinebox.utils.JsonUtils.JsonHelper;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 张浩 on 2016/3/7.
 */
public class SignUp {
    boolean  SignUp;
    boolean  isSignUpEd;
    InputStream inputStream = null;
    HttpURLConnection urlConnection = null;
    BufferedReader reader=null;
    String response;
    public static SignUp SignUpInstance;
     public static  SignUp  getInstance(){
         if (SignUpInstance==null)
             SignUpInstance=new SignUp();
         return SignUpInstance;
     }

    /**
     *
     * @param object
     * @throws IOException
     */
    public void UrlRegister(JSONObject object) throws IOException {
        try {
            URL url = new URL(ApiUtils.SignUpUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(20 * 1000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);
            String json = String.valueOf(object);
            //Log.d("json",json);
            // Log.d("json", String.valueOf(Charset.forName("utf-8").encode(json)));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            bw.write(json);
            bw.flush();
            bw.close();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                response = builder.toString();
                Log.d("response", response);
            } else {
                Log.d("response", urlConnection.getRequestMethod());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                reader.close();
            }

        }
    }

    //判断是否注册成功
    public void SignUpEd(){
        try {
            if (response!=null){
                JSONObject object= JsonHelper.getJSON(response);
                switch (object.getString("message")){
                    case "注册成功":
                        Log.d("te","注册成功");
                        setSignUp(true);
                        break;
                    case "已注册":
                        Log.d("te","已注册");
                        setIsSignUpEd(true);
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //登录状态的选择
    public boolean isSignUp() {
        return SignUp;
    }

    public void setSignUp(boolean signUp) {
        SignUp = signUp;
    }

    public boolean isSignUpEd() {
        return isSignUpEd;
    }

    public void setIsSignUpEd(boolean isSignUpEd) {
        this.isSignUpEd = isSignUpEd;
    }
}
