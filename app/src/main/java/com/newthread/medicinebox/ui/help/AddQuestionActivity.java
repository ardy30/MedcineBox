package com.newthread.medicinebox.ui.help;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.newthread.medicinebox.Adapter.SelectorImagesAdapter;
import com.newthread.medicinebox.R;
import com.newthread.medicinebox.ui.activity.BaseActivity;
import com.newthread.medicinebox.ui.view.DividerGridItemDecoration;
import com.newthread.medicinebox.utils.ApiUtils;
import com.newthread.medicinebox.utils.ConsUtils;
import com.newthread.medicinebox.utils.EventBusUtils.MyEventHelp;
import com.newthread.medicinebox.utils.HttpUtils.UrlConnectionUtils;
import com.newthread.medicinebox.utils.JsonUtils.JsonHelper;
import com.newthread.medicinebox.utils.UrlConnectionPost.UrlConnectionFilePost;
import com.newthread.medicinebox.utils.UrlConnectionPost.UrlConnectionJsonPost;
import com.newthread.medicinebox.utils.UserUtils.CurrentUserSp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;


/**
 * Created by 张浩 on 2016/2/17.
 */
public class AddQuestionActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.help_topic)
    TextInputEditText helpTopic;
    @Bind(R.id.medcinequestion)
    EditText TopicContentEdit;
    String TopicContent;
    @Bind(R.id.add_help_lay)
    LinearLayout addHelpLay;
    @Bind(R.id.getHelpImage)
    ImageView GetHelpImage;
    @Bind(R.id.select_images_gridView)
    RecyclerView SelectorImagesRecyclerView;
    List<String> path;
    UrlConnectionFilePost filePost;
    CurrentUserSp currentUserSp;
    String ResultCode = "";
    Map<String, String> param;
    JSONArray ResultPath;
    ProgressDialog dialog;
    String Topic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addqusetion);
        ButterKnife.bind(this);
        initView();
        init();
    }

    private void init() {
        currentUserSp = new CurrentUserSp(this);
        param = new HashMap<>();
        param.put("account", currentUserSp.getAccount());
        param.put("sessionId", currentUserSp.getSessionId());
    }


    private void getEditData() {
        Topic = helpTopic.getText().toString();
        TopicContent = TopicContentEdit.getText().toString();
    }

    private void initView() {
        toolbar.setTitle(R.string.addhelp);
        setUpToolBar(toolbar, true, true);
        GetHelpImage.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_add:
                saveHelp();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveHelp() {
        getEditData();
        if (TopicContent == null || TopicContent.trim().equals("")) {
            Snackbar.make(addHelpLay, "请将信息填完整", Snackbar.LENGTH_SHORT).show();
        } else {
            new PostHelp().execute(ApiUtils.UpLoadHelpPic, ApiUtils.UpLoadHelpPostCont);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getHelpImage:
                SelectPic();
                break;
        }
    }

    private void SelectPic() {
        Intent intent = new Intent(this, MyImageSelectorActivity.class);
// 是否显示调用相机拍照
        intent.putExtra(MyImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
// 最大图片选择数量
        intent.putExtra(MyImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
// 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MyImageSelectorActivity.EXTRA_SELECT_MODE, MyImageSelectorActivity.MODE_MULTI);
// 默认选择图片,回填选项(支持String ArrayList)
        ArrayList<String> defaultDataArray = new ArrayList<>();
        intent.putStringArrayListExtra(MyImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, defaultDataArray);
        startActivityForResult(intent, ConsUtils.IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConsUtils.IMAGE_REQUEST)
            if (resultCode == RESULT_OK) {
                path = data.getStringArrayListExtra(MyImageSelectorActivity.EXTRA_RESULT);
                setUpSelectedImages(path);
                Log.d("path", String.valueOf(path));
            }
    }

    private void setUpSelectedImages(List<String> path) {
        SelectorImagesAdapter adapter = new SelectorImagesAdapter(this, path);
        SelectorImagesRecyclerView.addItemDecoration(DividerGridItemDecoration.getInstance(this));
        SelectorImagesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        SelectorImagesRecyclerView.setAdapter(adapter);
    }

    class PostHelp extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AddQuestionActivity.this);
            dialog.setMessage("发送中...");
            dialog.setCancelable(false);
            dialog.show();
        }

        /**
         * doInBackGround返回的值
         *
         * @param aBoolean
         */
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                if (dialog != null) {
                    dialog.dismiss();
                    EventBus.getDefault().post(new MyEventHelp(true));
                    finish();
                }
            } else {
                if (dialog != null) {
                    dialog.dismiss();
                }
                Snackbar.make(addHelpLay, "发送失败！", Snackbar.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String upFileUrl = params[0];
            String PostHelpUrl = params[1];
            //上传文件
            if (path != null) {
                filePost = UrlConnectionFilePost.getInstance();
                ResultPath = new JSONArray();
                for (int i = 0; i < path.size(); i++) {
                    File file = new File(path.get(i));
                    UpLoadFiles(filePost, param, "file", file, upFileUrl);
                }
            }
            //长传完了发送帖子内容
            Log.d("posturl", PostHelpUrl);
            SendPost(PostHelpUrl);
            return ResultCode.equals("N01");
        }
    }


    /**
     * 上传帖子的图片到到服务器
     *
     * @param filePost
     * @param param
     * @param name
     * @param file
     * @param url
     * @throws IOException
     */
    public void UpLoadFiles(UrlConnectionFilePost filePost, Map<String, String> param, String name, File file,
                            String url) {
        try {
            filePost.upLoadFile(param, name, file, "", url, new UrlConnectionFilePost.HttpCallBackListener() {
                @Override
                public void onSuccess(String response) {
                    try {
                        ParseJsonResponse(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Exception e) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件上传后的id，加入ResultPath中
     *
     * @param response
     * @throws JSONException
     */
    public void ParseJsonResponse(String response) throws JSONException {
        JSONObject fileJson = UrlConnectionUtils.getJSON(response);
        switch (fileJson.getString("code")) {
            case "N01":
                JSONObject PathObject = fileJson.getJSONObject("contents");
                String path = PathObject.getString("fileSubPath");
                ResultPath.put(path);
                break;
            case "E02":
                Log.d("status", fileJson.getString("message"));
                break;
        }
    }

    /**
     * 发送帖子文字内容
     *
     * @param url
     */

    public void SendPost(String url) {
        UrlConnectionJsonPost jsonPost = new UrlConnectionJsonPost();
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("account", currentUserSp.getAccount());
            jsonParam.put("sessionID", currentUserSp.getSessionId());
            jsonParam.put("communicateTopic", Topic);
            jsonParam.put("communitcateContent", TopicContent);
            jsonParam.put("pictureList", ResultPath);
            Log.d("muinfo", String.valueOf(jsonParam));
            String result = jsonPost.UrlPostJson(url, jsonParam);
            Log.d("postcontent", result);
            ParseSendPostResult(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /*
    * 帖子内容发送完后消息的处理
    * */
    public void ParseSendPostResult(String Result) {
        JsonHelper helper = JsonHelper.getInstance();
        helper.getSendPostResult(Result, new JsonHelper.OnSendPostListener() {
            @Override
            public void onSuccess(String success) {
                ResultCode = success;
            }

            @Override
            public void onError(String error) {
                ResultCode = error;
            }
        });
    }


}
