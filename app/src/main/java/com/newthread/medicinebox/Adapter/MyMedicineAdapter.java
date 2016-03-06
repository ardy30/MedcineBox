package com.newthread.medicinebox.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.newthread.medicinebox.R;
import com.newthread.medicinebox.bean.RecMedicineBean;
import com.newthread.medicinebox.utils.NetWorkImageUtils.VolleyImageCacheManager;

import java.util.ArrayList;

/**
 * Created by 张浩 on 2016/2/16.
 */
public class MyMedicineAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {
    public ArrayList<RecMedicineBean> list;
    public Context context;
    public MyMedicineAdapter(Context context, ArrayList<RecMedicineBean> list){
        this.context=context;
        this.list=list;
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
            ItemViewHolder holder = new ItemViewHolder(LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.medicineitem, parent, false));
            return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        RecMedicineBean bean=list.get(position);
        ((ItemViewHolder)holder).content.setText(bean.getMedicineSummary());
         String img_url=bean.getMedicineImg();
         String img_type=bean.getMedicineType();
         setImg(img_url,((ItemViewHolder)holder).medicine);
         setImg(img_type,((ItemViewHolder)holder).medicineType);
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

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setImg(String url,NetworkImageView imageView){
       imageView.setImageUrl(url, VolleyImageCacheManager.getInstance().getImageLoader());
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        NetworkImageView medicine,medicineType;
        public ItemViewHolder(View itemView) {
            super(itemView);
            content= (TextView) itemView.findViewById(R.id.medicine_content);
            medicine= (NetworkImageView) itemView.findViewById(R.id.medicine_img);
            medicineType= (NetworkImageView) itemView.findViewById(R.id.medicine_img_type);

        }
    }

}
