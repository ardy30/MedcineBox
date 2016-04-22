package com.newthread.medicinebox.ui.help;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.newthread.medicinebox.Adapter.PostCommentAdapter;
import com.newthread.medicinebox.Adapter.PostImagesAdapter;
import com.newthread.medicinebox.R;
import com.newthread.medicinebox.bean.PostCommentBean;
import com.newthread.medicinebox.ui.activity.ImageDisplayActivity;
import com.newthread.medicinebox.ui.activity.SwipeBackActivity;
import com.newthread.medicinebox.ui.view.DividerGridItemDecoration;
import com.newthread.medicinebox.utils.ApiUtils;
import com.newthread.medicinebox.utils.ComUtils;
import com.newthread.medicinebox.utils.ConsUtils;
import com.newthread.medicinebox.utils.NetWorkImageUtils.PicassoPostImageHelper;
import com.newthread.medicinebox.utils.PostUtils.PostCommentListHelper;
import com.newthread.medicinebox.utils.PostUtils.PostHelper;
import com.newthread.medicinebox.utils.UserUtils.CurrentUserSp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 张浩 on 2016/3/27.
 */
public class PostDetailActivity extends SwipeBackActivity implements View.OnClickListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.post_head_img)
    ImageView postHeadImg;
    @Bind(R.id.post_detail_nickname)
    TextView postNickname;
    @Bind(R.id.post_detail_content)
    TextView postDetailContent;
    @Bind(R.id.post_detail_content_images)
    RecyclerView postDetailContentImages;
    @Bind(R.id.post_detail_content_comments)
    RecyclerView postDetailContentComments;
    String NickName, UserHeadImgUrl, PostTopic, PostContent;
    ArrayList<String> postImages;
    @Bind(R.id.post_detail_topic)
    TextView postDetailTopic;
    @Bind(R.id.lin_like_detail)
    LinearLayout Lin_Zan;
    @Bind(R.id.post_zan_detail)
    ImageView ZanImageView;
    @Bind(R.id.lin_zhuangfa_detail)
    LinearLayout linZhuangfaDetail;
    @Bind(R.id.post_comment_edit)
    EditText postCommentEdit;
    @Bind(R.id.post_comment_send)
    ImageView postCommentSend;
    @Bind(R.id.post_content)
    LinearLayout linearLayout;
    PicassoPostImageHelper helper;
    CurrentUserSp currentUserSp;
    String SessionId;
    String account;
    PostHelper postHelper;
    int communicateId;
    String comments;
    PostCommentAdapter adapter;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postdetail);
        ButterKnife.bind(this);
        getIntentData();
        init();
        initView();
        initComment();
    }

    private void initComment() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PostCommentListHelper postCommentListHelper = new PostCommentListHelper();
                List<PostCommentBean.CommentContent> list =
                        postCommentListHelper.getCommentList(communicateId, 1, 5);
                if (list != null) {
                    adapter = new PostCommentAdapter(PostDetailActivity.this, list);
                    handler.sendEmptyMessage(ConsUtils.LOAD_POST_COMMENTS_SUCCEESS);
                } else
                    handler.sendEmptyMessage(ConsUtils.LOAD_POST_COMMENTS_FAILED);


            }
        }).start();

    }

    private void init() {
        helper = PicassoPostImageHelper.getInstance();
        currentUserSp = new CurrentUserSp(this);
        postHelper = new PostHelper();
        SessionId = currentUserSp.getSessionId();
        account = currentUserSp.getAccount();
    }

    private void initView() {
        initToolBar();
        postNickname.setText(NickName);
        postDetailTopic.setText("话题:" + PostTopic);
        postDetailContent.setText("内容:" + PostContent);
        Lin_Zan.setOnClickListener(this);
        postCommentSend.setOnClickListener(this);
        helper.LoadUserHeadImg(this, postHeadImg, ApiUtils.getPostUserPic + UserHeadImgUrl);
        if (postImages != null) {
            SetUpPostContentRecyclerView();
        }
//        postDetailContentComments.addItemDecoration(new DividerItemDecoration(this,
//                DividerItemDecoration.VERTICAL_LIST));
        postDetailContentComments.setLayoutManager(new LinearLayoutManager(this));
    }

    /*
    * 设置图图片的adapter
    * */
    private void SetUpPostContentRecyclerView() {
        postDetailContentImages.addItemDecoration(new DividerGridItemDecoration());
        postDetailContentImages.setLayoutManager(new GridLayoutManager(this, 3));
        int width = PicassoPostImageHelper.initImgDetailListSize(this);
        PostImagesAdapter adapter = new PostImagesAdapter(this, helper, width, postImages);
        postDetailContentImages.setAdapter(adapter);
        adapter.setOnItemClickListenter(new PostImagesAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Intent intent = new Intent(PostDetailActivity.this, ImageDisplayActivity.class);
                intent.putExtra("position", position);
                Log.d("position", String.valueOf(position));
                intent.putStringArrayListExtra("url", postImages);
                startActivity(intent);
            }
        });
    }

    private void initToolBar() {
        toolbar.setTitle(R.string.detail);
        setUpToolBar(toolbar, true, true);
    }

    /*
    * 获取传过来的值
    * */
    private void getIntentData() {
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle = intent.getExtras();
        NickName = bundle.getString("UserNickName");
        UserHeadImgUrl = bundle.getString("UserHeadImgUrl");
        PostTopic = bundle.getString("PostTopic");
        PostContent = bundle.getString("PostContent");
        postImages = bundle.getStringArrayList("PostImages");
        communicateId = bundle.getInt("communicateId");
        //System.out.println(NickName+"\n"+UserHeadImgUrl+"\n"+PostContent+"\n"+PostTopic+"\n"+postImages);
    }


    /**
     * gridview的item
     * 判断图片的数量来定
     *
     * @param list
     * @return
     */
    public int JudgeItems(ArrayList<String> list) {
        switch (list.size()) {
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return 3;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_like_detail:
              /*  new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PostZan();
                    }
                }).start();*/
                break;
            case R.id.post_comment_send:
                ComUtils.hideInput(this,linearLayout);
                showDialog();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        comments = postCommentEdit.getText().toString().trim();
                        if (!comments.isEmpty())
                            PostComment();
                        else
                            handler.sendEmptyMessage(ConsUtils.ADD_COMMENT_FAILED);
                    }
                }).start();

                break;
        }
    }


    //提交评论
    private void PostComment() {
        JSONObject param = new JSONObject();
        try {
            param.put("account", account);
            param.put("sessionID", SessionId);
            param.put("communicateId", communicateId);
            param.put("commentContent", comments);
            postHelper.SendPost(ApiUtils.AddComment, param, new PostHelper.OnCommentListener() {
                @Override
                public void OnAddCommnetSuccess(String success) {
                    Message message = new Message();
                    message.what = ConsUtils.ADD_COMMENT_SUCCESS;
                    message.obj = success;
                    handler.sendMessage(message);
                }

                @Override
                public void OnAddCommnetFailed(String fail) {
                    Message message = new Message();
                    message.what = ConsUtils.ADD_COMMENT_FAILED;
                    message.obj = fail;
                    handler.sendMessage(message);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //提交赞；
    private void PostZan() {
        JSONObject param = new JSONObject();
        try {
            param.put("account", account);
            param.put("sessionID", SessionId);
            param.put("communicateId", communicateId);
            Log.d("param", String.valueOf(param));
            postHelper.SendZan(ApiUtils.Zan, param, new PostHelper.OnLikedListener() {
                @Override
                public void OnLikedSuccess(String success) {
                    Log.d("success", success);
                    Message message = new Message();
                    message.obj = success;
                    message.what = 200;
                    handler.sendMessage(message);
                }

                @Override
                public void OnLikedFailed(String fail) {
                    Log.d("success", fail);
                    Message message = new Message();
                    message.obj = fail;
                    message.what = 400;
                    handler.sendMessage(message);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConsUtils.ADD_COMMENT_SUCCESS:
                    postCommentEdit.setText("");
                    ReLoadComment();
                    DismissDialog();
                    Toast.makeText(PostDetailActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case ConsUtils.ADD_COMMENT_FAILED:
                    DismissDialog();
                    if (msg.obj != null)
                        Toast.makeText(PostDetailActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case ConsUtils.LOAD_POST_COMMENTS_SUCCEESS:
                    postDetailContentComments.setAdapter(adapter);
                    break;
                case ConsUtils.LOAD_POST_COMMENTS_FAILED:
                    postDetailContentComments.setAdapter(null);
                    break;
            }
        }
    };

    //评论成功后再加载一次
    private void ReLoadComment() {
       initComment();
    }


    //显示对话框
    private void showDialog(){
        dialog=new ProgressDialog(this);
        dialog.setMessage("");
        dialog.setCancelable(true);
        dialog.show();
    }

    //消除对话框
    private void DismissDialog(){
        if (dialog!=null){
            dialog.dismiss();
        }
    }
}
