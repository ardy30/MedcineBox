package com.newthread.medicinebox.utils.HttpUtils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by 张浩 on 2016/2/18.
 */
public class VolleyJsonHelper {
    public Context context;
    public String url;
    public VolleyJsonHelper(Context context,String url){
        this.context=context;
        this.url=url;
    }
    public void getMedicineData(){
        RequestQueue queue= Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                System.out.println(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(jsonObjectRequest);
    }

}
