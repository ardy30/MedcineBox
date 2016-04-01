package com.newthread.medicinebox.ui.help;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.newthread.medicinebox.Adapter.PostDeatilImagesAdapter;
import com.newthread.medicinebox.Adapter.PostImagesAdapter;
import com.newthread.medicinebox.R;
import com.newthread.medicinebox.bean.Image;
import com.newthread.medicinebox.ui.activity.ImageDisplayActivity;
import com.newthread.medicinebox.ui.activity.SwipeBackActivity;
import com.newthread.medicinebox.ui.view.DividerGridItemDecoration;
import com.newthread.medicinebox.utils.ApiUtils;
import com.newthread.medicinebox.utils.JsonUtils.JsonHelper;
import com.newthread.medicinebox.utils.NetWorkImageUtils.PicassoPostImageHelper;
import com.newthread.medicinebox.utils.PostUtils.PostHelper;
import com.newthread.medicinebox.utils.UserUtils.CurrentUser;
import com.newthread.medicinebox.utils.UserUtils.CurrentUserSp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    PicassoPostImageHelper helper;
    CurrentUserSp currentUserSp;
    String SessionId;
    String account;
    PostHelper postHelper;
    int communicateId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postdetail);
        ButterKnife.bind(this);
        getIntentData();
        init();
        initView();
    }

    private void init() {
        helper= PicassoPostImageHelper.getInstance();
        currentUserSp=new CurrentUserSp(this);
        postHelper=new PostHelper();
        SessionId=currentUserSp.getSessionId();
        account=currentUserSp.getAccount();
    }

    private void initView() {
        initToolBar();
        postNickname.setText(NickName);
        postDetailTopic.setText("话题:"+PostTopic);
        postDetailContent.setText("内容:"+PostContent);
        Lin_Zan.setOnClickListener(this);
        helper.LoadUserHeadImg(this, postHeadImg, ApiUtils.getPostUserPic + UserHeadImgUrl);
        if (postImages!=null){
            SetUpPostContentRecyclerView();
        }
    }

    /*
    * 设置图图片的adapter
    * */
    private void SetUpPostContentRecyclerView() {
        postDetailContentImages.addItemDecoration(new DividerGridItemDecoration());
        postDetailContentImages.setLayoutManager(new GridLayoutManager(this, 3));
        int width=PicassoPostImageHelper.initImgDetailListSize(this);
        PostImagesAdapter adapter=new PostImagesAdapter(this,helper,width,postImages);
        postDetailContentImages.setAdapter(adapter);
        adapter.setOnItemClickListenter(new PostImagesAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Intent intent = new Intent(PostDetailActivity.this, ImageDisplayActivity.class);
                intent.putExtra("position", position);
                Log.d("position", String.valueOf(position));
                intent.putStringArrayListExtra("url", postImages);
                startActivity(intent);
                ;
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
        communicateId=bundle.getInt("communicateId");
        //System.out.println(NickName+"\n"+UserHeadImgUrl+"\n"+PostContent+"\n"+PostTopic+"\n"+postImages);
    }



    /**gridview的item
     * 判断图片的数量来定
     * @param list
     * @return
     */
    public int JudgeItems(ArrayList<String> list){
        switch (list.size()){
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
        switch (v.getId()){
            case R.id.lin_like_detail:
                Log.d("zan","---------------------");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PostZan();
                    }
                });
                break;
        }
    }
    //提交赞；
    private void PostZan(){
        JSONObject param=new JSONObject();
        try {
            param.put("account",account);
            param.put("sessionID",SessionId);
            param.put("communicateId",communicateId);
            postHelper.SendZan(ApiUtils.Zan, param, new PostHelper.OnLikedListener() {
                @Override
                public String OnLikedSuccess(String success) {
                    Message message=new Message();
                    message.obj=success;
                    message.what=200;
                    handler.sendMessage(message);
                    return null;
                }
                @Override
                public String OnLikedFailed(String fail) {
                    Message message=new Message();
                    message.obj=fail;
                    message.what=400;
                    handler.sendMessage(message);
                    return null;
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }




    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    ZanImageView.setImageResource(R.drawable.img_like_pressed);
                    break;
                case 400:
                    Toast.makeText(PostDetailActivity.this,"失败，请重新登录",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
