package com.newthread.medicinebox.Adapter;

import android.content.Context;
import android.util.Log;

import com.newthread.medicinebox.utils.NetWorkImageUtils.PicassoPostImageHelper;
import com.newthread.medicinebox.utils.UserUtils.Login;

import java.util.List;

/**
 * Created by 张浩 on 2016/3/29.
 */
public class PostDeatilImagesAdapter extends PostImagesAdapter {


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        picassoImageHelper.LoadPostDetailImages(context,holder.imageView,width,url);
    }

    public PostDeatilImagesAdapter(Context context, PicassoPostImageHelper helper, int width, List<String> list) {
        super(context, helper, width, list);
    }
}
