package com.newthread.medicinebox.ui.remind;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leaking.slideswitch.SlideSwitch;
import com.newthread.medicinebox.R;
import com.newthread.medicinebox.bean.RemindBean;
import com.newthread.medicinebox.theme.StatusBarCompat;
import com.newthread.medicinebox.ui.activity.SwipeBackActivity;
import com.newthread.medicinebox.utils.ConsUtils;
import com.newthread.medicinebox.utils.AlarmUtils.RemindUtils;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by 张浩 on 2016/1/24.
 */
public class EditRemindActivity extends SwipeBackActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private LinearLayout set_time;
    private TextView time_setted,songName;
    private Calendar now;
    private SlideSwitch slideSwitch;
    private RelativeLayout chooseRingtone;
    private RemindBean remind=new RemindBean();
    private int hour,min;
    private String Medicine,Tips;
    private EditText e_Medicine,e_tips;
    private CheckBox mDay1,mDay2,mDay3,mDay4,mDay5,mDay6,mDay7;
    private String RingName;
    private String RingId;
    private String RepeatDay;
    private String alarmId;
    private Boolean vibrator;
    ContentValues CV=new ContentValues();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT==Build.VERSION_CODES.KITKAT){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addremind);
        initView();
        initData();
        saveVibrate();
    }

    /*
    *得到数据
    * */
    private void initData() {
        Intent intent =getIntent();
        alarmId=intent.getStringExtra("id");
        ArrayList<Map<String,Object>> list=RemindUtils.getDetailReMinds(alarmId);
        System.out.println("222"+list);
        Map<String,Object> map=list.get(0);
        hour= (int) map.get("time_hour");
        min= (int) map.get("time_min");
        Medicine= (String) map.get("medicine");
        Tips= (String) map.get("tips");
        RingName= (String) map.get("ringtone");
        vibrator= (Boolean) map.get("vibrator");
        initRemindInFo();
    }

    private void initRemindInFo() {
        e_Medicine.setText(Medicine);
        e_tips.setText(Tips);
        time_setted.setText(ChangeTime(hour,min));
        songName.setText(RingName);
        slideSwitch.setState(vibrator);
    }

    private void initView() {
        initToolBar();
        initCheckBox();
        e_Medicine= (EditText) findViewById(R.id.medicine);
        e_tips= (EditText) findViewById(R.id.tips);
        slideSwitch= (SlideSwitch) findViewById(R.id.vibrate);
        time_setted= (TextView) findViewById(R.id.my_set_time);
        set_time= (LinearLayout) findViewById(R.id.set_time);
        songName= (TextView) findViewById(R.id.songName);
        set_time.setOnClickListener(this);
        chooseRingtone= (RelativeLayout) findViewById(R.id.chooseRingtone);
        chooseRingtone.setOnClickListener(this);
        StatusBarCompat.compat(EditRemindActivity.this, getResources().getColor(R.color.colorPrimaryDark));
    }

    /*
    * */
    private void initCheckBox() {
        mDay1= (CheckBox) findViewById(R.id.cb_day_1);
        mDay2= (CheckBox) findViewById(R.id.cb_day_2);
        mDay3= (CheckBox) findViewById(R.id.cb_day_3);
        mDay4= (CheckBox) findViewById(R.id.cb_day_4);
        mDay5= (CheckBox) findViewById(R.id.cb_day_5);
        mDay6= (CheckBox) findViewById(R.id.cb_day_6);
        mDay7= (CheckBox) findViewById(R.id.cb_day_7);
    }


    private void initToolBar() {
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar_title= (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setVisibility(View.GONE);
        toolbar.setTitle(R.string.update);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               onBackPressed();
           }
       });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.set_time:
                showDialog();
                break;
            case R.id.chooseRingtone:
                doPickRingtone();
                break;
        }
    }

    /*
    * 选择铃声
    * */
    private void doPickRingtone() {
        Intent intent=new Intent(EditRemindActivity.this,RingSetActivity.class);
        startActivityForResult(intent, ConsUtils.ASK_FOR_RING);
    }


    /*
    * 铃声选择Activity请求码后的结果处理
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case ConsUtils.RING_SET_CANCEL:
                break;
            case ConsUtils.RING_SET_DONG:
                RingName=data.getStringExtra("songName");
                if (data.getStringExtra("songId")!=null){
                    RingId=data.getStringExtra("songId");
                }
                songName.setText(RingName);
            break;
        }
    }

    /*
            * 显示MaterialTimePiker
            * */
    public void showDialog(){
        now=Calendar.getInstance();
        TimePickerDialog dialog=TimePickerDialog.newInstance(
                EditRemindActivity.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true//是否为24小时制
        );
        dialog.setThemeDark(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        dialog.show(getFragmentManager(), "TimePiker");

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        hour=hourOfDay;
        min=minute;
        time_setted.setText(ChangeTime(hourOfDay,minute));
        //开始设置闹钟
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu_regist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId())
       {
           /*
           * 进行闹钟信息的保存
           * */
           case R.id.menu_regist:
               saveRemind();
               Intent intent=new Intent();
               EditRemindActivity.this.setResult(RESULT_OK,intent);
               startRemind();
               finish();
               break;
       }
        return super.onOptionsItemSelected(item);
    }

    /*
    * 进行闹钟设置后在执行一次开启新的闹钟
    * */
    private void startRemind() {
        ArrayList<Map<String,Object>> list= RemindUtils.getReminds();
        Map<String,Object> map;
        Remind remind=new Remind(this);
        for (int i=0;i<list.size();i++){
            map=list.get(i);
            if ((boolean)map.get("open")){
                remind.turnAlarm(map,true);
            }
        }
    }


    /*
    * 保存闹钟信息
    * */
    private void saveRemind(){
       /* RemindBean remind= DataSupport.find(RemindBean.class,Position+1);
        Medicine=e_Medicine.getText().toString();
        Tips=e_tips.getText().toString();
        RepeatDay=RemindUtils.getDataDayofWeek(getRepeatDay());
        AlarmId=hour+""+min+"";
        Log.d("day",RepeatDay);
        remind.setTime_hour(hour);
        remind.setTime_min(min);
        remind.setOpen(true);
        remind.setMedicine(Medicine);
        remind.setTips(Tips);
        remind.setRingtone(RingName);
        remind.setRingtone_id(RingId);
        remind.setRepeat(RepeatDay);
        remind.setAlarmId(AlarmId);
        remind.save();*/
        ContentValues values=new ContentValues();
        values.put("medicine",e_Medicine.getText().toString());
        values.put("tips",e_tips.getText().toString());
        values.put("ringtone",RingName);
        values.put("ringtone_id",RingName+".ogg");
        values.put("time_hour",hour);
        values.put("time_min",min);
        values.put("open",true);
        DataSupport.updateAll(RemindBean.class, values, "alarmid=?", alarmId);
       /* ContentValues values=new ContentValues();
        values.put("open",false);
        DataSupport.updateAll(RemindBean.class,values,"alarmid=?",id);*/
    }

/*
    /*
    * 保存是否震动
    * */
    private void saveVibrate(){
        slideSwitch.setSlideListener(new SlideSwitch.SlideListener() {
            @Override
            public void open() {
               CV.put("vibrator",true);
               DataSupport.updateAll(RemindBean.class,CV,"alarmid=?",alarmId);
            }

            @Override
            public void close() {
                CV.put("vibrator",false);
                DataSupport.updateAll(RemindBean.class, CV, "alarmid=?", alarmId);
            }
        });
    }

    /*
    * 保存重复的天数
    * */
    private int[] getRepeatDay(){
        String dayRepeat="";
        if(mDay1.isChecked()){
            dayRepeat+="1"+" ";
        }
        if(mDay2.isChecked()){
            dayRepeat+="2"+" ";
        }
        if(mDay3.isChecked()){
            dayRepeat+="3"+" ";
        }
        if(mDay4.isChecked()){
            dayRepeat+="4"+" ";
        }
        if(mDay5.isChecked()){
            dayRepeat+="5"+" ";
        }
        if(mDay6.isChecked()){
            dayRepeat+="6"+" ";
        }
        if(mDay7.isChecked()){
            dayRepeat+="7"+" ";
        }
        if(dayRepeat.equals("")){
            dayRepeat="0 ";
        }
        return RemindUtils.getAlarmDayofWeek(dayRepeat);
    }
    /*
    * 显示的转化
    * */
    private String ChangeTime(int h,int m){
        String hourString = h < 10 ? "0"+h : ""+h;
        String minuteString = m < 10 ? "0"+m : ""+m;
        String time = hourString+":"+minuteString;
        return time;
    }
}
