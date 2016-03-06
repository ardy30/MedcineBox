package com.newthread.medicinebox.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.newthread.medicinebox.R;
import com.newthread.medicinebox.bean.MedicineBean;
import com.newthread.medicinebox.utils.ApiUtils;
import com.newthread.medicinebox.utils.NetWorkImageUtils.HeadImageHelper;
import com.newthread.medicinebox.utils.NetWorkImageUtils.VolleyImageCacheManager;

import java.util.ArrayList;

/**
 * Created by 张浩 on 2016/2/20.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    public ArrayList<MedicineBean> list;
    public Context context;
    public SearchAdapter(Context context,ArrayList<MedicineBean> list){
        this.context=context;
        this.list=list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       ViewHolder holder=new ViewHolder(LayoutInflater.from(parent.getContext()).
               inflate(R.layout.searchitem, parent, false));
              return  holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MedicineBean bean=list.get(position);
        holder.medicineName.setText("【通用名】"+bean.getName());
        holder.medicinePro.setText("【厂    商】"+bean.getProduct());
        holder.medicineSize.setText("【规    格】"+bean.getSize());
        holder.medicineSym.setText("【适应症】" + bean.getSympthon());
        Log.d("url",bean.getImageurl());
        holder.medicineImg.setImageUrl(ApiUtils.imgUrl+bean.getImageurl(), VolleyImageCacheManager.getInstance().getImageLoader());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        NetworkImageView medicineImg;
        TextView medicineName,medicinePro,medicineSize,medicineSym;
        public ViewHolder(View itemView) {
            super(itemView);
            medicineImg= (NetworkImageView) itemView.findViewById(R.id.search_img);
            medicineName= (TextView) itemView.findViewById(R.id.search_name);
            medicinePro= (TextView) itemView.findViewById(R.id.search_product);
            medicineSize= (TextView) itemView.findViewById(R.id.search_size);
            medicineSym= (TextView) itemView.findViewById(R.id.search_Sympton);
        }
    }
}
