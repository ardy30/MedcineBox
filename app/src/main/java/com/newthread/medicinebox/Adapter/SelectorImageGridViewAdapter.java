package com.newthread.medicinebox.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.newthread.medicinebox.R;
import com.newthread.medicinebox.bean.Image;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 张浩 on 2016/3/24.
 */
public class SelectorImageGridViewAdapter extends BaseAdapter {
    public GridView.LayoutParams mLayoutParams=
            new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT,
                    GridView.LayoutParams.MATCH_PARENT);
    public List<String> pathList;
    public int mGridWidth;
    public int mWidth=0;
    public Context context;
    public SelectorImageGridViewAdapter(Context context,List<String> list){
        this.context=context;
        this.pathList=list;
    }

    @Override
    public int getCount() {
        return pathList.size();
    }

    public void setItemSize(int columnWidth) {
        if (mGridWidth == columnWidth) {
            return;
        }
        mGridWidth = columnWidth;
        mLayoutParams = new GridView.LayoutParams(mGridWidth, mGridWidth);
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        initImageSize();
        ImageView imageView;
        String url=pathList.get(position);
        if (convertView==null){
         imageView=new ImageView(context);
         imageView.setLayoutParams(mLayoutParams);
        }else{
            imageView= (ImageView) convertView;
        }
        if (pathList!=null&&pathList.size()>0){
            Log.d("testUrl",url);
            Picasso.with(context)
                    .load(url)
                    .placeholder(R.drawable.default_error)
                    .resize(mGridWidth, mGridWidth)
                    .centerCrop()
                    .into(imageView);
        }else{

            //此处32是 R.id.viewpager_bbs_main的16*2
            final int width = mWidth - 32;
            final int desireSize = context.getResources().getDimensionPixelOffset(R.dimen.image_size);
            final int numCount = width / desireSize;
            final int columnSpace = context.getResources().getDimensionPixelOffset(R.dimen.space_size);
            int columnWidth = (width - columnSpace * (numCount - 1)) / numCount;
            // 显示图片
            Picasso.with(context)
                    .load(url)
                    .placeholder(R.drawable.default_error)
                    .resize(columnWidth, columnWidth)
                    .centerCrop()
                    .into(imageView);
        }
        GridView.LayoutParams lp= (GridView.LayoutParams) imageView.
                getLayoutParams();
        if (lp.height!=mGridWidth){
            imageView.setLayoutParams(mLayoutParams);
        }
        return imageView;
    }






    public void initImageSize(){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            wm.getDefaultDisplay().getSize(size);
            mWidth = size.x;
        }else{
            mWidth = wm.getDefaultDisplay().getWidth();
        }
        mGridWidth = mWidth / 4;
    }
}
