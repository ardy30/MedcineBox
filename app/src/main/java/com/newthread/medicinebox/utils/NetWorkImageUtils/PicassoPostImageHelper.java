package com.newthread.medicinebox.utils.NetWorkImageUtils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.newthread.medicinebox.R;
import com.newthread.medicinebox.utils.ApiUtils;
import com.newthread.medicinebox.utils.ConsUtils;
import com.newthread.medicinebox.utils.ScreenUtils.DensityUtil;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by 张浩 on 2016/3/23.
 */
public class PicassoPostImageHelper {
//    private int mGridWidth;//post首页的list图片中的gridView宽度
//    private int mGridWidthDetail;//post详情页的宽度
    static  DensityUtil densityUtil;
    public static PicassoPostImageHelper helper;
    public static synchronized PicassoPostImageHelper getInstance(){
        if (helper==null)
            helper= new PicassoPostImageHelper();
        return helper;
    }

    public static void  init(Context context){
        densityUtil=new DensityUtil(context);
    }
    /**
     *加载帖子的图片post首页
     * @param context
     * @param imageView
     * @param path
     */
    public void LoadPostImages(Context context, ImageView imageView,int mGridWidth, String path){
            //Log.d("mGridWidth", String.valueOf(mGridWidth));
            Picasso.with(context)
                    .load(ApiUtils.getPostImages+path)
                    .placeholder(R.drawable.default_error)
                    .resize(mGridWidth, mGridWidth)
                    .centerCrop()
                    .into(imageView);
    }



    public void LoadPostDetailImages(Context context, ImageView imageView,int mGridWidth,String path){

        Picasso.with(context)
                .load(ApiUtils.getPostImages + path)
                .placeholder(R.drawable.default_error)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .resize(mGridWidth, mGridWidth)
                .centerCrop()
                .into(imageView);
    }

    /**
     * 在每条帖子中加载用户的头像
     * @param context
     * @param imageView
     * @param path
     */
    public void LoadUserHeadImg(Context context,ImageView imageView, String path){
        int size= densityUtil.dip2px(60);
        Picasso.with(context)
                .load(path)
                .centerCrop()
                /*.memoryPolicy(MemoryPolicy.NO_STORE)//没有缓存*/
                .placeholder(R.drawable.img_login1)
                .resize(size, size)
                .into(imageView);
    }

    /**
     * 根据屏幕的分辨率大小调整图片的显示大小（post首页）
     * @param context
     */
    public static int initImgListSize(Context context){
        float width= densityUtil.getScreenWidth();
        float dp=densityUtil.px2dip(width);
        float px=densityUtil.dip2px((dp-80));
        return  (int) px/3;
    }

    /**
     * post详情页
     * @param context
     */
    public static int  initImgDetailListSize(Context context){
        float width=densityUtil.getScreenWidth();
        float dp=densityUtil.px2dip(width);
        float px=densityUtil.dip2px((dp-10));
        return (int) px/3;
    }


    //清除指定缓存
    public static void ClearImgByUrl(Context context,String img_path){
        Picasso.with(context).invalidate(img_path);
        Log.d("clear","图片缓存被清除了");
    }

    /**
     * 点赞的处理
     * @param imageView
     * @param Res
     */
    public void SetUpZanImg(Context context,ImageView imageView,int Res){
            Picasso.with(context)
                    .load(Res)
                    .into(imageView);
            Log.d("zan", "赞执行了!!!!");

    }


}
