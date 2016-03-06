package com.newthread.medicinebox.utils.NetWorkImageUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.newthread.medicinebox.R;
import com.newthread.medicinebox.utils.ConsUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by 张浩 on 2016/2/4.
 */
public class HeadImageHelper {
    public  Context context;
    public Bitmap myBitmap;
    RequestQueue mQueue;
    public HeadImageHelper(Context context){
        this.context=context;
        mQueue = Volley.newRequestQueue(context);
        Log.d("chuangjian", "VolleyImageUtils被创建了");
    }
    public void getHead_Img(String url,ImageView imageView, int ResId, int ResId1) {
        RequestQueue mQueue = Volley.newRequestQueue(context);
        ImageLoader loader = new ImageLoader(mQueue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String s) {
                return null;
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {
                saveBitmap(bitmap);
                myBitmap=bitmap;
            }
        });
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,
                ResId, ResId1);
        loader.get(url, listener);
    }

    /*
    * 保存bitmap为本地文件
    * */
    private void saveBitmap(Bitmap bitmap) {
        File f = new File(ConsUtils.path_cache, "headimg");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
    * getBitmap
    * */
    public Bitmap getMyBitmap(){
        return myBitmap;
    }

}
