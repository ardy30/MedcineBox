package com.newthread.medicinebox.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newthread.medicinebox.R;
import com.newthread.medicinebox.ui.view.RoundImageView;
import com.newthread.medicinebox.ui.view.MyCircleImage;
import com.newthread.medicinebox.utils.NetWorkImageUtils.VolleyImageCacheManager;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by 张浩 on 2016/2/19.
 */
public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.ViewHolder> {
    public ArrayList<Map<String,Object>> list;
    public Map<String,Object> map;
    public Context context;
    public HelpAdapter(Context context,ArrayList<Map<String,Object>> list){
        this.context=context;
        this.list=list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder=new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.helpitem,parent,false));
        return  holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        map=list.get(position);
        holder.nickname.setText(map.get("nickname").toString());
        holder.date.setText(map.get("date").toString());
        holder.topic.setText("主题:  "+map.get("medicine").toString());
        holder.content.setText("内容:\n"+map.get("question").toString());
        String img=map.get("img_url").toString();
        if (img!=null&&!img.equals("")){
            holder.circleImage.setImageUrl(img,VolleyImageCacheManager.getInstance().getImageLoader());
        }else{
            holder.circleImage.setImageResource(R.drawable.img_login1);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nickname,date,topic,content;
        MyCircleImage circleImage;
        public ViewHolder(View itemView) {
            super(itemView);
            nickname= (TextView) itemView.findViewById(R.id.help_nickname);
            date= (TextView) itemView.findViewById(R.id.help_time);
            topic= (TextView) itemView.findViewById(R.id.help_topic);
            content= (TextView) itemView.findViewById(R.id.help_content);
            circleImage= (MyCircleImage) itemView.findViewById(R.id.help_head_img);
        }
    }
}
