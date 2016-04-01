package com.newthread.medicinebox.utils.UrlConnectionPost;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by 张浩 on 2016/3/13.
 */
public class UrlConnectionFilePost {
    public static UrlConnectionFilePost filePost;
    public  static UrlConnectionFilePost getInstance(){
        if (filePost==null)
           filePost=new UrlConnectionFilePost();
        return filePost;
    }
    public interface HttpCallBackListener{
        void onSuccess(String response);
        void onError(Exception e);
    }

    private static final  String BOUNDARY="----WebKitFormBoundarycsTWA9fhN6VJINxC";
     String response="";
     InputStream inputStream = null;
     InputStream inputStreamResponse=null;
     HttpURLConnection urlConnection=null;
     BufferedReader reader=null;



    public String upLoadFile(Map<String, String> param, String fileFormName,
                           File upLoadFile, String newFileName, String urlStr,HttpCallBackListener Listener) throws IOException {

        if (newFileName == null || newFileName.trim().equals("")) {
            newFileName = upLoadFile.getName();
        }

        StringBuilder stringBuilder = new StringBuilder();
        /*
        * 普通的表单数据
        * */
        if (param != null)
            for (String key : param.keySet()) {
                stringBuilder.append("--" + BOUNDARY + "\r\n");
                stringBuilder.append("Content-Disposition: form-data; name=\"" + key
                        + "\"" + "\r\n");
                stringBuilder.append("\r\n");
                stringBuilder.append(param.get(key) + "\r\n");
            }
     /*
     * 上传文件的头
     * */
        stringBuilder.append("--" + BOUNDARY + "\r\n");
        stringBuilder.append("Content-Disposition: form-data; name=\"" + fileFormName
                + "\"; filename=\"" + newFileName + "\"" + "\r\n");
        stringBuilder.append("\r\n");

        byte[] headerInfo = stringBuilder.toString().getBytes("UTF-8");
        byte[] endInfo=("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");
        System.out.println(stringBuilder.toString());
        URL url=new URL(urlStr);
        urlConnection= (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "multipart/form-data; " +
                "boundary=" + BOUNDARY);
        urlConnection.setRequestProperty("Content-Length", String.valueOf(headerInfo.length
                + upLoadFile.length() + endInfo.length));
        urlConnection.setDoOutput(true);
        OutputStream outputStream=urlConnection.getOutputStream();
        inputStream=new FileInputStream(upLoadFile);
        outputStream.write(headerInfo);
        byte [] buffer=new byte[1024];
        int len;
        while ((len=inputStream.read(buffer))!=-1)
            outputStream.write(buffer,0,len);
        outputStream.write(endInfo);
        inputStream.close();
        outputStream.close();
        Log.d("code", String.valueOf(urlConnection.getResponseCode()));
        if (urlConnection.getResponseCode()==200){
            inputStreamResponse = new BufferedInputStream(urlConnection.getInputStream());
            reader = new BufferedReader(new InputStreamReader(inputStreamResponse));
            StringBuilder builder = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            response = builder.toString();
            Log.d("response",response);
        }
        if (inputStreamResponse!=null){
            inputStreamResponse.close();
        }
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                if (Listener!=null){
                    Listener.onError(e);
                }
                e.printStackTrace();
            }
        }
        if (Listener!=null){
            Listener.onSuccess(response);
        }
        return response;
    }




















//    public void upLoadFileToServer(String Url,String account,String sessionId,String files){
//        try {
//            URL url=new URL(Url);
//            urlConnection= (HttpURLConnection) url.openConnection();
//            urlConnection.setConnectTimeout(10 * 10000);
//            urlConnection.setRequestMethod("POST");
//            urlConnection.setRequestProperty("Connection", "keep-alive");
//            urlConnection.setRequestProperty("Content-Type", "multipart/form-data" + ";boundary" + boundary);
//            urlConnection.setDoOutput(true);
//            if (files!=null){
//                OutputStream outputStream=urlConnection.getOutputStream();
//                DataOutputStream dataOutputStream=new DataOutputStream(outputStream);
//                StringBuilder stringBuilder=new StringBuilder();
//                stringBuilder.append(PREFIX);
//                stringBuilder.append(boundary);
//                stringBuilder.append(LINE_END);
//                stringBuilder.append("Content-Disposition:form-data;name=\"account \"\n"+account
//                +"name=\"sessionId \"\n"+sessionId+"name=\"file\";filename="+files);
//                stringBuilder.append(LINE_END);
//                dataOutputStream.write(stringBuilder.toString().getBytes());
//                InputStream inputStream=new FileInputStream(String.valueOf(files));
//                byte [] bytes=new byte[1024];
//                int len = 0;
//                while((len=inputStream.read(bytes))!=-1)
//                {
//                    dataOutputStream.write(bytes, 0, len);
//                }
//                inputStream.close();
//                dataOutputStream.write(LINE_END.getBytes());
//                byte[] end_data = (PREFIX+boundary+PREFIX+LINE_END).getBytes();
//                dataOutputStream.write(end_data);
//                dataOutputStream.flush();
//                Log.d("responsecode", String.valueOf(urlConnection.getResponseCode()));
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
