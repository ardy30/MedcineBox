package com.newthread.medicinebox.utils.FindUtils;
import android.content.Context;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;


/**
 * 发现版块网络连接
 * Created by 张浩 on 2016/2/8.
 */
public class FindVolleyUtils {
    public Context mContext;
    public FindVolleyUtils(Context context){
        this.mContext=context;
    }
    public void getHtmlString(String url){
        RequestQueue queue= Volley.newRequestQueue(mContext);
        StringRequest request=new StringRequest(Request.Method.GET, url,new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("TAG",volleyError.getMessage(),volleyError);
                    }
                }){
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Host","toutiao.com");
                headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.103 Safari/537.36");
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }


}
