package com.newthread.medicinebox.ui.help;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.newthread.medicinebox.Adapter.PostAdapter;
import com.newthread.medicinebox.R;
import com.newthread.medicinebox.bean.PostBean;
import com.newthread.medicinebox.utils.ApiUtils;
import com.newthread.medicinebox.utils.ConsUtils;
import com.newthread.medicinebox.utils.EventBusUtils.MyEventHelp;
import com.newthread.medicinebox.utils.HttpUtils.UrlConnectionUtils;
import com.newthread.medicinebox.utils.JsonUtils.JsonHelper;
import com.newthread.medicinebox.utils.NetWorkHelper;
import com.newthread.medicinebox.utils.NetWorkImageUtils.PicassoPostImageHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import uk.co.senab.photoview.log.LoggerDefault;

/**
 * Created by 张浩 on 2016/1/16.
 */
public class PostFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.help_question)
    XRecyclerView postRecyclerView;
    @Bind(R.id.refresh_help)
    SwipeRefreshLayout refreshHelp;
    @Bind(R.id.progress_view_help)
    CircularProgressView progressViewHelp;
    @Bind(R.id.try_load_help)
    LinearLayout try_load_help;
    private View view;
    @Bind(R.id.add_help_question)
    FloatingActionButton actionButton;
    @Bind(R.id.help_progress)
    FrameLayout frameLayout;
    @Bind(R.id.coordinatlayout_post)
    CoordinatorLayout coordinatorLayout;
    ArrayList<Map<String, Object>> HelpLists;
    NotificationCompat.Builder notification;
    PostBean postBean;
    List<PostBean.content> list;
    PostAdapter adapter;
    int MAX_COUNT=0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.help, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initView();
        initLoad();
        return view;
    }



    /*
    *
    * */
    private void initLoad() {
        NetWorkHelper helper = new NetWorkHelper(getContext());
        if (helper.isOpenNetWork()) {
            try_load_help.setVisibility(View.GONE);
            initData();
        } else {
            actionButton.setVisibility(View.GONE);
            refreshHelp.setRefreshing(false);
        }
    }

    /*
    * 加载数据
    * */
    private void initData() {
        progressViewHelp.startAnimation();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getPostListSize();
                getPostData(1, 8, MAX_COUNT);
                Log.d("MAX_COUNT", String.valueOf(MAX_COUNT));
                handler.sendEmptyMessage(ConsUtils.LOAD_FINISH);
            }
        }).start();

    }

    /*
    * 获取帖子的页数MAXCount
    * */
    private void getPostListSize() {
        UrlConnectionUtils.getResult(ApiUtils.getPostMaxCount, new UrlConnectionUtils.OnResultListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = JsonHelper.getJSON(result);
                    getMaxAccount(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    //解析获得最大数
    private void getMaxAccount(JSONObject object) throws JSONException {
        if (object.getString("code").equals("N01")){
            MAX_COUNT=object.getInt("contents");
            Log.d("count:", String.valueOf(MAX_COUNT));
        }
        else
            Log.d("count","查询失败！！！");
    }

    /*
    * 获取帖子数据
    * */
    private void getPostData(int page,int count,int maxCount) {
        UrlConnectionUtils.getResult(ApiUtils.GetPostItem(page,count,maxCount), new UrlConnectionUtils.OnResultListener() {
            @Override
            public void onSuccess(String result) {
                Log.d("result", result);
                Gson gson = new Gson();
                postBean = gson.fromJson(result, PostBean.class);
                list=postBean.getContents();
                //adapter=new PostAdapter(getContext(),list);
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    private void initView() {
        postRecyclerView.setPullRefreshEnabled(false);
        postRecyclerView.setItemAnimator(new DefaultItemAnimator());
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.BallClipRotateMultiple);
        initLoadMore();
        refreshHelp.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary);
        refreshHelp.setOnRefreshListener(this);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddQuestionActivity.class));
            }
        });
    }


    //swipeRefreshLayout刷新
    @Override
    public void onRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getPostListSize();
                getPostData(1, 8, MAX_COUNT);
                Log.d("MAX_COUNT", String.valueOf(MAX_COUNT));
                handler.sendEmptyMessage(ConsUtils.REFRESHED);
            }
        }).start();

    }


    //xRecyclerView的加载的回调方法
    private void initLoadMore() {
        postRecyclerView.setLoadingMoreEnabled(true);
        postRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //不做，使用SwipeRefreshLayout
            }

            @Override
            public void onLoadMore() {
                //实现加载更多
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LoadMoreData();
                    }
                }).start();
            }
        });
    }

    private void LoadMoreData() {
        getPostListSize();
        final int page = (list.size() / 8) + 1;
        if(page<=(MAX_COUNT/8+1)){
            Log.d("page", String.valueOf(page));
            UrlConnectionUtils.getResult(ApiUtils.GetPostItem(page, 8, MAX_COUNT), new UrlConnectionUtils.OnResultListener() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    postBean = gson.fromJson(result, PostBean.class);
                    List<PostBean.content> ListMore=postBean.getContents();
                    list.addAll(ListMore);
                    adapter=new PostAdapter(getContext(),list);
                    handler.sendEmptyMessage(ConsUtils.LOAD_MORE_FINISH);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }else
        handler.sendEmptyMessage(ConsUtils.NO_MORE_DATA);



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            refreshHelp.setRefreshing(false);
            frameLayout.setVisibility(View.GONE);
            switch (msg.what) {
                case ConsUtils.LOAD_FINISH:
                    setPostAdapter();
                    break;
                case ConsUtils.LOAD_MORE_FINISH:
                    postRecyclerView.loadMoreComplete();
                    adapter.notifyDataSetChanged();
                    refreshHelp.setRefreshing(false);
                    break;
                case ConsUtils.ADD_FINISH:
                    setPostAdapter();
                    break;
                case ConsUtils.REFRESHED:
                    postRecyclerView.removeAllViews();
                    setPostAdapter();
                    break;
                case ConsUtils.NO_MORE_DATA:
                    Snackbar.make(coordinatorLayout,"无更多数据!",Snackbar.LENGTH_SHORT).show();
                    postRecyclerView.loadMoreComplete();
                    break;
            }
        }
    };




    public void setPostAdapter(){
        adapter=new PostAdapter(getContext(),list);
        postRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new PostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PostBean.content ItemContent = list.get(position-1);
                Bundle bundle=new Bundle();
                bundle.putString("UserNickName",ItemContent.userInfo.getUserVirtualName());
                bundle.putString("UserHeadImgUrl",ItemContent.userInfo.getUserPicture());
                bundle.putString("PostTopic",ItemContent.getCommunicateTopic());
                bundle.putString("PostContent", ItemContent.getCommunitcateContent());
                bundle.putInt("communicateId",ItemContent.getCommunicateId());
                if (ItemContent.getPictureList()!=null){

                    for(String url:ItemContent.getPictureList()){
                        PicassoPostImageHelper.ClearImgByUrl(getContext(),url);
                    }
                    bundle.putStringArrayList("PostImages", (ArrayList<String>) ItemContent.getPictureList());
                }

                Intent intent=new Intent(getActivity(),PostDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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

    public void onEventMainThread(MyEventHelp eventHelp) {
        Log.d("add", "11111");
        boolean Added = eventHelp.isAdded();
        if (Added) {
            refreshHelp.setRefreshing(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getPostListSize();
                    Log.d("MAX_COUNT添加一条", String.valueOf(MAX_COUNT));
                    getPostData(1, 8, MAX_COUNT);
                    handler.sendEmptyMessage(ConsUtils.ADD_FINISH);
                }
            }).start();

        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

//
//    *
//     * 显示一个notification来提示发帖内容
//     *
//
//    public void setNotification(){
//        Log.d("test","执行了");
//        notification=new NotificationCompat.Builder(getContext())
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("药品盒子")
//                .setContentText("发送中...");
//        Intent intent =new Intent(getContext(), MainActivity.class);
//        PendingIntent pendingIntent=
//                PendingIntent.getActivity(getContext(),0,intent,0);
//        notification.setContentIntent(pendingIntent);
//        NotificationManager mNotificationManager= (NotificationManager) getContext()
//                .getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify(0,notification.build());
//    }




}
