package com.newthread.medicinebox.Adapter;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import com.newthread.medicinebox.R;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.util.List;

/**
 * Created by 张浩 on 2016/3/18.
 */
public class SelectorImagesAdapter extends RecyclerView.Adapter<SelectorImagesAdapter.ViewHolder> {
    private Context context;
    public int mGridWidth;
    private List<String> FileList;
    public SelectorImagesAdapter(Context context,List<String> list){
        this.context=context;
        this.FileList=list;
        initImageSize();
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder=new ViewHolder(LayoutInflater.from(context).
                inflate(R.layout.imageselector_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String path=FileList.get(position);
        File file=new File(path);
        if (file.exists()){
            Picasso.with(context)
                    .load(file)
                    .placeholder(R.drawable.default_error)
                    .resize(mGridWidth, mGridWidth)
                    .centerCrop()
                    .into(holder.imageView);
        }
    }
    @Override
    public int getItemCount() {
        return FileList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.select_image_item);
        }
    }

    public void initImageSize(){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            wm.getDefaultDisplay().getSize(size);
            width = size.x;
        }else{
            width = wm.getDefaultDisplay().getWidth();
        }
        mGridWidth = width / 3;
    }
}
