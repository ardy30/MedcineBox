package com.newthread.medicinebox.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.newthread.medicinebox.R;
import com.newthread.medicinebox.bean.FindBean;
import com.newthread.medicinebox.utils.NetWorkImageUtils.HeadImageHelper;
import com.newthread.medicinebox.utils.NetWorkImageUtils.VolleyImageCacheManager;

import java.util.ArrayList;

/**
 * Created by 张浩 on 2016/2/11.
 */
public class  MyFindAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {
    private static final int ANIMATED_ITEMS_COUNT = 3;
    private boolean animateItems = false;
    private int lastAnimatedPosition = -1;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    public ArrayList<FindBean> list;
    public Context context;
    public HeadImageHelper utils;
    public MyFindAdapter(Context context,ArrayList<FindBean> list){
        this.context=context;
        this.list=list;
        this.utils=new HeadImageHelper(this.context);
    }
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==TYPE_ITEM){
            ItemViewHolder holder = new ItemViewHolder(LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.finditem, parent, false));
            return holder;
        }
        else if (viewType==TYPE_FOOTER){
            FooterHolder footer=new FooterHolder(LayoutInflater.from(parent.getContext()).
            inflate(R.layout.footerview,parent,false));
            return footer;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder){
            FindBean findBean = list.get(position);
            String title=findBean.getTitle();
            String list_content=findBean.getContent();
            String date=findBean.getDate();
            String img_url=findBean.getImg_url();
            String detail_url=findBean.getDetail_url();
            String lable=findBean.getLable();
            ((ItemViewHolder)holder).imageView.setTag(img_url);
            ((ItemViewHolder)holder).title.setText(title);
            ((ItemViewHolder)holder).list_content.setText(list_content);
            ((ItemViewHolder)holder).date.setText(date);
            ((ItemViewHolder)holder).lable.setText("标签:" + lable);
            ((ItemViewHolder)holder).imageView.setImageUrl(img_url, VolleyImageCacheManager.getInstance().getImageLoader());
            if(onItemClickListener!=null){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos=holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.itemView,pos);
                    }
                });

            }
        }

    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position+1==getItemCount()){
            return TYPE_FOOTER;
        }else {
            return TYPE_ITEM;
        }
    }

    public class FooterHolder extends RecyclerView.ViewHolder{

        public FooterHolder(View itemView) {
            super(itemView);
        }
    }



    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView title, list_content,date,lable;
        NetworkImageView imageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_find);
            list_content = (TextView) itemView.findViewById(R.id.content_find);
            date = (TextView) itemView.findViewById(R.id.date_find);
            lable= (TextView) itemView.findViewById(R.id.lable_find);
            imageView= (NetworkImageView) itemView.findViewById(R.id.find_imageView);
        }
    }

}
