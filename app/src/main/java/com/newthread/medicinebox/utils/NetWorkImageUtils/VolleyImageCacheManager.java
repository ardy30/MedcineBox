package com.newthread.medicinebox.utils.NetWorkImageUtils;

import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by 张浩 on 2016/2/29.
 */
public class VolleyImageCacheManager {
    public static VolleyImageCacheManager mInstance;
    public static ImageLoader mImageLoader;
    public ImageLoader.ImageCache mImageCache;
    /**
     * instance of the cache manager
     * @return
     */
    public static VolleyImageCacheManager getInstance(){
        if(mInstance == null)
            mInstance = new VolleyImageCacheManager();
        return mInstance;
    }
    /**
     *
     */
    public static void init(){
        BitmapLruCache cache=new BitmapLruCache();
        mImageLoader=new ImageLoader(VolleyRequestManager.getRequestQueue(),cache);
    }

    /**
     *
     * @param url
     * @return
     */
    public Bitmap getBitmap(String url) {
        try {
            return mImageCache.getBitmap(createKey(url));
        } catch (NullPointerException e) {
            throw new IllegalStateException("Disk Cache Not initialized");
        }
    }

    /**
     *
     * @param url
     * @param bitmap
     */
    public void putBitmap(String url, Bitmap bitmap) {
        try {
            mImageCache.putBitmap(createKey(url), bitmap);
        } catch (NullPointerException e) {
            throw new IllegalStateException("Disk Cache Not initialized");
        }
    }

    /**
     * 	Executes and image load
     * @param url
     * 		location of image
     * @param listener
     * 		Listener for completion
     */
    public void getImage(String url, ImageLoader.ImageListener listener){
        mImageLoader.get(url, listener);
    }

    /**
     * @return
     * 		instance of the image loader
     */
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    /**
     * Creates a unique cache key based on a url value
     * @param url
     * 		url to be used in key creation
     * @return
     * 		cache key value
     */
    private String createKey(String url){
        return String.valueOf(url.hashCode());
    }
}
