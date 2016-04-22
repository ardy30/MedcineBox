package com.newthread.medicinebox.utils.PostUtils;

import android.util.Log;

import com.google.gson.Gson;
import com.newthread.medicinebox.bean.PostCommentBean;
import com.newthread.medicinebox.utils.ApiUtils;
import com.newthread.medicinebox.utils.HttpUtils.UrlConnectionUtils;
import com.newthread.medicinebox.utils.UserUtils.Login;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by 张浩 on 2016/4/1.
 */
public class PostCommentListHelper {
    public int MAX_COMMENT_SIZE;
    PostCommentBean commentBean;
    List<PostCommentBean.CommentContent> CommentList;

    /**
     * 返回一个评论的List
     * @param communicateid
     * @param page
     * @param items
     * @return
     */
    public List<PostCommentBean.CommentContent> getCommentList(final int communicateid,int page,int items){
        getCommentMaxCount(communicateid);
        UrlConnectionUtils.getResult(ApiUtils.GetPostComment(communicateid, MAX_COMMENT_SIZE, page, items), new UrlConnectionUtils.OnResultListener() {
            @Override
            public void onSuccess(String result) {
                if (!result.isEmpty()){
                    Gson gson=new Gson();
                    commentBean=gson.fromJson(result, PostCommentBean.class);
                    CommentList=commentBean.getContents();
                    PostCommentBean.CommentContent commentContent=new PostCommentBean.CommentContent();
                    Log.d("commemt",commentContent.toString());
                }
            }
            @Override
            public void onError(Exception e) {

            }
        });
        return CommentList;
    }


    /**
     * 获取最大数
     * @param communicateid
     * @return
     */
    private int getCommentMaxCount(int communicateid){
        UrlConnectionUtils.getResult(ApiUtils.GetCommentMaxCount+communicateid, new UrlConnectionUtils.OnResultListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object= UrlConnectionUtils.getJSON(result);
                    getMaxSize(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Exception e) {

            }
        });
        return 0;
    }

    /**
     * 获取最大的评论数
     * @param object
     */
    private void getMaxSize(JSONObject object) {
        try {
            MAX_COMMENT_SIZE=object.getInt("contents");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
