package com.newthread.medicinebox.utils.FindUtils;

import com.newthread.medicinebox.bean.FindBean;
import com.newthread.medicinebox.utils.JsoupUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by 张浩 on 2016/2/11.
 */
public class FindUrlConUtils {
    /*
    * getList
    * */
    public void getListResult(String Url, ArrayList<FindBean> list)
    {
        String Result = null;
        HttpURLConnection connection=null;
        try {
            URL url=new URL(Url);
            connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(20 * 1000);
            connection.setRequestProperty("Host","www.jianke.com");
            connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.109 Safari/537.36");
            InputStream result=connection.getInputStream();
            StringBuilder response=new StringBuilder();
            BufferedReader reader=new BufferedReader(new InputStreamReader(result));
            String line="";
            while ((line=reader.readLine())!=null)
            {
                response.append(line);
            }
            Result=response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsoupUtils.ParseFindListHtml(Result, list);
    }


    public String getResult(String Url)
    {
        String Result = null;
        HttpURLConnection connection=null;
        try {
            URL url=new URL(Url);
            connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(20 * 1000);
            connection.setRequestProperty("Host","www.jianke.com");
            connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.109 Safari/537.36");
            InputStream result=connection.getInputStream();
            StringBuilder response=new StringBuilder();
            BufferedReader reader=new BufferedReader(new InputStreamReader(result));
            String line="";
            while ((line=reader.readLine())!=null)
            {
                response.append(line);
            }
            Result=response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result;
    }
}
