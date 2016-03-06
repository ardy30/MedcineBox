package com.newthread.medicinebox.Adapter;

import android.content.ContentValues;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leaking.slideswitch.SlideSwitch;
import com.newthread.medicinebox.R;
import com.newthread.medicinebox.bean.RemindBean;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by 张浩 on 2016/2/6.
 */
 /*
    *适配器
    * */
public class MyRemindsAdapter extends RecyclerView.Adapter<MyRemindsAdapter.ViewHolder>{
    private ArrayList<Map<String,Object>> mList;
    private Map<String,Object> mMap;
    private int h;
    private int m;
    private String medicine;
    private String tips;
    private Boolean open;
    private String id;
    public  MyRemindsAdapter(ArrayList<Map<String,Object>> list,Map<String,Object> map){
        mList=list;
        mMap=map;

    }

    /*
    * 获取数据
    * */
    private void getData(Map<String,Object> map) {
        id= (String) mMap.get("id");
        h= (int) mMap.get("time_hour");
        m=(int)mMap.get("time_min");
        medicine=(String) mMap.get("medicine");
        tips=(String) mMap.get("tips");
        open= (Boolean) mMap.get("open");

    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    @Override
    public MyRemindsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder=new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminditem,parent,false));
        return  holder;
    }

    @Override
    public void onBindViewHolder(final MyRemindsAdapter.ViewHolder holder, final int position) {
        mMap=mList.get(position);
        getData(mMap);
        String time=timeExchange(h,m);
        holder.time.setText(time);
        holder.medicine.setText(medicine);
        holder.tips.setText(tips);
        holder.slideSwitch.setState(open);
        holder.slideSwitch.setSlideListener(new SlideSwitch.SlideListener() {
            @Override
            public void open() {
                Log.d("close", "开启了闹钟" + h+""+m);
                ContentValues values=new ContentValues();
                values.put("open",true);
                DataSupport.updateAll(RemindBean.class, values, "alarmid=?",h+""+m);
            }

            @Override
            public void close() {
                Log.d("close", "关闭了闹钟"+h+""+m);
                ContentValues values=new ContentValues();
                values.put("open",false);
                DataSupport.updateAll(RemindBean.class,values,"alarmid=?",h+""+m);
            }
        });
            /*
            * onItemClickListener
            * */
        if(onItemClickListener!=null){

            /*
            * onItemClickListener
            * */
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView,pos);
                }
            });
           /*
            * onItemLongClickListener
            * */
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos=holder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(holder.itemView,pos);
                    return false;
                }
            });
        }
    }

    private String timeExchange(int h, int m) {
        String hourString = h < 10 ? "0"+h : ""+h;
        String minuteString = m < 10 ? "0"+m : ""+m;
        String time = hourString+":"+minuteString;
        return  time;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time,medicine,tips;
        SlideSwitch slideSwitch;
        public ViewHolder(View itemView) {
            super(itemView);
            time= (TextView) itemView.findViewById(R.id.item_time);
            medicine= (TextView)itemView. findViewById(R.id.item_medicine);
            tips= (TextView) itemView.findViewById(R.id.item_tips);
            slideSwitch= (SlideSwitch) itemView.findViewById(R.id.item_open);
        }
    }
}
