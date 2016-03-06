package com.newthread.medicinebox.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 张浩 on 2016/1/5.
 */
public class UrlConnectionUtils {

    public static String getResult(String Url)
    {
        String Result = null;
        HttpURLConnection connection=null;
        try {
            URL url=new URL(Url);
            connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(20*1000);
            InputStream result=connection.getInputStream();
            StringBuilder response=new StringBuilder();
            BufferedReader reader=new BufferedReader(new InputStreamReader(result));
            String line;
            while ((line=reader.readLine())!=null)
            {
                response.append(line);
            }
            Result=response.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("do some test"+Result);
        return Result;
    }

    /*
    * 转化为Json
    * */
    public static JSONObject getJSON(String sb) throws JSONException {
        return new JSONObject(sb);
    }

}
