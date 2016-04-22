package com.newthread.medicinebox.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newthread.medicinebox.R;
import com.newthread.medicinebox.bean.PostCommentBean;

import java.util.List;

/**
 * post评论的adapter
 * Created by 张浩 on 2016/4/1.
 */
public class PostCommentAdapter extends RecyclerView.Adapter<PostCommentAdapter.ViewHolder> {
    private List<PostCommentBean.CommentContent> list;
    private Context context;
    public PostCommentAdapter(Context context,List<PostCommentBean.CommentContent> list){
        this.context=context;
        this.list=list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.postcommentitem, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PostCommentBean.CommentContent content=list.get(position);
        holder.commentText.setText(content.getCommentContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView commentText;
        public ViewHolder(View itemView) {
            super(itemView);
            commentText= (TextView) itemView.findViewById(R.id.post_comment_content);
        }
    }
}
