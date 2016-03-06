package com.newthread.medicinebox.utils.okHttpUtils;

import android.os.Environment;

import com.newthread.medicinebox.utils.ConsUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by 张浩 on 2016/3/5.
 */
public class OkHttpTest  {
    File file=new File(ConsUtils.path_img);
    RequestBody fileBody=RequestBody.create(MediaType.
            parse("multipart/form-data"), file);
    RequestBody requestBody=new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("","",fileBody)
            .build();


    public Map<String,String> setMap(){
        Map<String, String> params = new HashMap<>();
        params.put("text", "张鸿洋");
        params.put("password", "123");
        return params;
    }

}
