package com.newthread.medicinebox.Adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.newthread.medicinebox.R;
import com.newthread.medicinebox.bean.PostBean;
import com.newthread.medicinebox.ui.view.DividerGridItemDecoration;
import com.newthread.medicinebox.utils.ApiUtils;
import com.newthread.medicinebox.utils.NetWorkImageUtils.PicassoPostImageHelper;

import java.util.List;

/**
 * Created by 张浩 on 2016/2/19.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private List<PostBean.content> contentList;
    public Context context;
    public static boolean zaned=false;
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public interface  OnLikeClickListener{
        void onLikeClick(View view,int position);
        boolean onLiked(boolean liked);
    }
    public OnItemClickListener onItemClickListener;
    public OnLikeClickListener onLikeClickListener;
    public PostAdapter(Context context, List<PostBean.content> list){
        this.contentList=list;
        this.context=context;
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener=listener;
    }
    public void setOnLikeClickListener(OnLikeClickListener likeClickListener){
        this.onLikeClickListener=likeClickListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.helpitem,parent,false));
    }
    //
//    public void test(){
//        List<PostBean.content> list=postBean.getContents();
//        for (int i=0;i<list.size();i++){
//            PostBean.content Contentlist=list.get(i);
//            List<String> imgList=Contentlist.getPictureList();
//            PostBean.content.CurrentUser currentUser=Contentlist.userInfo;
//            System.out.println(Contentlist.getCommunicateId() + "\n" + Contentlist.getCommunicateTime() + "\n"
//                    + Contentlist.getCommunicateZhuan() + "\n" + Contentlist.getCommunicateTopic()+"\n"
//                            +currentUser.getUserAge()+currentUser.getUserName()+currentUser.getUserPicture());
//            if (imgList!=null){
//                System.out.println(imgList.size());
//                for (String imgUrl:imgList)
//                System.out.println("--->"+imgUrl);
//            }
//        }
//
//    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final PicassoPostImageHelper imageHelper= PicassoPostImageHelper.getInstance();
        PostBean.content Contents;
        List<String> imgList;
        PostBean.content.CurrentUser currentUser;
        Contents=contentList.get(position);
        currentUser=Contents.userInfo;
        holder.nickname.setText(currentUser.getUserVirtualName());
        holder.date.setText(Contents.getCommunicateTime());
        holder.topic.setText("#"+Contents.getCommunicateTopic()+"#");
        holder.content.setText(Contents.getCommunitcateContent());
        imgList=Contents.getPictureList();
        System.out.println(Contents.getPictureList());
        String head_img_url=ApiUtils.getPostUserPic+currentUser.getUserPicture();
        imageHelper.LoadUserHeadImg(context, holder.roundedImageView, head_img_url);
        if (imgList==null){
            holder.img_recyclerView.setAdapter(null);
            Log.d("nopic","没有图片加载！！！");
        }
        else{
            holder.img_recyclerView.setLayoutManager(new GridLayoutManager(context, JudgeItems(imgList)));
            int width=PicassoPostImageHelper.initImgListSize(context);
            holder.img_recyclerView.setAdapter(new PostImagesAdapter(context,imageHelper,width,imgList));
        }
        /**
         * onItemClickListener的设置
         */
        if (onItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView,pos);
                }
            });
        }

        /**
         * 点赞接口
         */
        if(onLikeClickListener!=null){
//            holder.LinLike.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos=holder.getLayoutPosition();
//                    onLikeClickListener.onLikeClick(holder.LinLike,pos);
//                    imageHelper.SetUpZanImg(holder.Zan,R.drawable.img_like_pressed);
//                }
//            });
            holder.LinLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
      return contentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nickname,date,topic,content;
        RoundedImageView roundedImageView;
        RecyclerView img_recyclerView;
        LinearLayout LinLike;
        public ImageView Zan;
        boolean ZanEd=false;
        public ViewHolder(View itemView) {
            super(itemView);
            nickname= (TextView) itemView.findViewById(R.id.post_nickname);
            date= (TextView) itemView.findViewById(R.id.post_time);
            topic= (TextView) itemView.findViewById(R.id.post_topic);
            content= (TextView) itemView.findViewById(R.id.post_contents);
            roundedImageView= (RoundedImageView) itemView.findViewById(R.id.post_head_img);
            img_recyclerView= (RecyclerView) itemView.findViewById(R.id.post_images_recyclerview);
            LinLike= (LinearLayout) itemView.findViewById(R.id.lin_like);
            Zan= (ImageView) itemView.findViewById(R.id.post_zan);
            DividerGridItemDecoration gridItemDecoration=DividerGridItemDecoration.getInstance(context);
            img_recyclerView.addItemDecoration(gridItemDecoration);
        }
    }
    /**gridview的item
     * 判断图片的数量来定
     * @param list
     * @return
     */
    public int JudgeItems(List<String> list){
        switch (list.size()){
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return 3;
        }
    }


}
