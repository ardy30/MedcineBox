package com.newthread.medicinebox.ui.help;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.newthread.medicinebox.R;
import com.newthread.medicinebox.bean.HelpBean;
import com.newthread.medicinebox.bean.mUser;
import com.newthread.medicinebox.theme.StatusBarCompat;
import com.newthread.medicinebox.ui.activity.SwipeBackActivity;
import com.newthread.medicinebox.utils.ConsUtils;
import com.newthread.medicinebox.utils.EventBusUtils.MyEventHelp;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import de.greenrobot.event.EventBus;

/**
 * Created by 张浩 on 2016/2/17.
 */
public class AddQuestionActivity extends SwipeBackActivity implements View.OnClickListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.medicinename)
    EditText medicinename;
    @Bind(R.id.medcinequestion)
    EditText medcinequestion;
    String medicine;
    String question;
    @Bind(R.id.add_help_lay)
    LinearLayout addHelpLay;
    String AppID = "3a5d16ce5d7a8c551fd43fbdbe90532d";
    @Bind(R.id.helpimg)
    ImageView HelpImg;
    @Bind(R.id.getHelpImage)
    ImageView GetHelpImage;
    private String[] items={"相册","拍照"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addqusetion);
        ButterKnife.bind(this);
        initView();
    }


    private void getEditData() {
        medicine = medicinename.getText().toString();
        question = medcinequestion.getText().toString();
    }

    private void initView() {
        initToolBar();
        GetHelpImage.setOnClickListener(this);
    }

    private void initToolBar() {
        toolbar.setTitle(R.string.addhelp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        StatusBarCompat.compat(this, getResources().getColor(R.color.colorPrimaryDark));
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
                EventBus.getDefault().post(new MyEventHelp(true));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveHelp() {
        getEditData();
        if (medicine.equals("") || question.equals("")) {
            Snackbar.make(addHelpLay, "请将信息填完整", Snackbar.LENGTH_SHORT).show();
        } else {
            HelpBean bean = new HelpBean();
            mUser user = BmobUser.getCurrentUser(this, mUser.class);
            bean.setMedicine(medicine);
            bean.setQuestion(question);
            bean.setUser(user);
            bean.save(this, new SaveListener() {
                @Override
                public void onSuccess() {
                    Log.d("save", "success");
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.d("save", "failed");
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.getHelpImage:
                showDialog();
                break;
        }
    }



    private void showDialog() {
        AlertDialog alertDialog=new AlertDialog.Builder(this)
                .setTitle(R.string.select)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent= new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*"); // 设置文件类型
                                startActivityForResult(intent,ConsUtils.GET_IMAGE_PIC);
                                break;
                            case 1:
                               /* Intent camera= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File("sdcard", "crop")));
                                startActivityForResult(camera, ConsUtils.CAMERA_REQUEST_CODE);*/
                                dialog.dismiss();
                                break;
                        }
                    }
                }).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            HelpImg.setImageURI(data.getData());
        }
    }
}
