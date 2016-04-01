package com.newthread.medicinebox.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.newthread.medicinebox.R;
import com.newthread.medicinebox.utils.NetWorkImageUtils.PicassoPostImageHelper;

import java.util.List;

/**
 * Created by 张浩 on 2016/3/23.
 */
public class PostImagesAdapter extends RecyclerView.Adapter<PostImagesAdapter.ViewHolder>{
    protected  Context context;
    protected String url;
    protected List<String> Img_List;
    protected   PicassoPostImageHelper picassoImageHelper;
    protected int width;
    public OnItemClickListener listener;
    public PostImagesAdapter(Context context,PicassoPostImageHelper helper, int width,List<String> list){
        this.picassoImageHelper=helper;
        this.context=context;
        this.Img_List=list;
        this.width=width;
    }
    public interface OnItemClickListener{
        void OnItemClick(View view,int position);
    }
    public void setOnItemClickListenter(OnItemClickListener listenter){
        this.listener=listenter;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.postimagesitem,
                parent,false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        url = Img_List.get(position);
        holder.imageView.setTag(url);
        Log.d("url---",url);
        if (holder.imageView.getTag()==url) {
            Log.d("url---",url);
            picassoImageHelper. LoadPostImages(context, holder.imageView,width, url);
        }

        if (listener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=holder.getLayoutPosition();
                    listener.OnItemClick(v,pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        Log.d("Imagesize", String.valueOf(Img_List.size()));
        return Img_List.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.postImage_item);
        }
    }
}
