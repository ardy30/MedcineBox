package com.newthread.medicinebox.utils.NetWorkImageUtils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by 张浩 on 2016/2/29.
 */
public class BitmapLruCache implements ImageLoader.ImageCache {
    private LruCache<String, Bitmap> mCache;
    public BitmapLruCache() {
        int maxSize = 10 * 1024 * 1024;
        mCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }
    @Override
    public Bitmap getBitmap(String url) {
        Log.d("bitmap", "get执行了");
        return mCache.get(url);
    }
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        Log.d("bitmap","put执行了");
        if (getBitmap(url)==null){
            mCache.put(url, bitmap);
        }
    }
}
