package com.newthread.medicinebox.ui.remind;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leaking.slideswitch.SlideSwitch;
import com.newthread.medicinebox.R;
import com.newthread.medicinebox.bean.RemindBean;
import com.newthread.medicinebox.ui.activity.SwipeBackActivity;
import com.newthread.medicinebox.utils.AlarmUtils.RemindUtils;
import com.newthread.medicinebox.utils.ComUtils;
import com.newthread.medicinebox.utils.ConsUtils;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 张浩 on 2016/1/24.
 */
public class AddRemindActivity extends SwipeBackActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private LinearLayout set_time;
    private TextView time_setted, songName;
    private Calendar now;
    private SlideSwitch slideSwitch;
    private RelativeLayout chooseRingtone;
    private RemindBean remind = new RemindBean();
    private int hour, min;
    private String Medicine, Tips;
    private EditText e_Medicine, e_tips;
    private CheckBox mDay1, mDay2, mDay3, mDay4, mDay5, mDay6, mDay7;
    private String RingName;
    private String RingId;
    private String RepeatDay;
    private String AlarmId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addremind);
        ButterKnife.bind(this);
        initView();
        saveVibrate();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        String medicine = intent.getStringExtra("medicine");
        if (!medicine.equals("") || !medicine.equals(null)) {
            e_Medicine.setText(medicine);
        }

    }

    private void initView() {
        ComUtils.adjustPan(this);
        toolbar.setTitle("");
        setUpToolBar(toolbar, true, true);
        initCheckBox();
        e_Medicine = (EditText) findViewById(R.id.medicine);
        e_tips = (EditText) findViewById(R.id.tips);
        slideSwitch = (SlideSwitch) findViewById(R.id.vibrate);
        time_setted = (TextView) findViewById(R.id.my_set_time);
        set_time = (LinearLayout) findViewById(R.id.set_time);
        songName = (TextView) findViewById(R.id.songName);
        set_time.setOnClickListener(this);
        chooseRingtone = (RelativeLayout) findViewById(R.id.chooseRingtone);
        chooseRingtone.setOnClickListener(this);
    }

    /*
    * */
    private void initCheckBox() {
        mDay1 = (CheckBox) findViewById(R.id.cb_day_1);
        mDay2 = (CheckBox) findViewById(R.id.cb_day_2);
        mDay3 = (CheckBox) findViewById(R.id.cb_day_3);
        mDay4 = (CheckBox) findViewById(R.id.cb_day_4);
        mDay5 = (CheckBox) findViewById(R.id.cb_day_5);
        mDay6 = (CheckBox) findViewById(R.id.cb_day_6);
        mDay7 = (CheckBox) findViewById(R.id.cb_day_7);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
        Intent intent = new Intent(AddRemindActivity.this, RingSetActivity.class);
        startActivityForResult(intent, ConsUtils.ASK_FOR_RING);
    }


    /*
    * 铃声选择Activity请求码后的结果处理
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case ConsUtils.RING_SET_CANCEL:
                break;
            case ConsUtils.RING_SET_DONG:
                RingName = data.getStringExtra("songName");
                if (data.getStringExtra("songId") != null) {
                    RingId = data.getStringExtra("songId");
                }
                songName.setText(RingName);
                break;
        }
    }

    /*
            * 显示MaterialTimePiker
            * */
    public void showDialog() {
        now = Calendar.getInstance();
        TimePickerDialog dialog = TimePickerDialog.newInstance(
                AddRemindActivity.this,
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
        hour = hourOfDay;
        min = minute;
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String time = hourString + ":" + minuteString;
        time_setted.setText(time);
        //开始设置闹钟
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
           /*
           * 进行闹钟信息的保存
           * */
            case R.id.menu_regist:
                saveRemind();
                Intent intent = new Intent();
                AddRemindActivity.this.setResult(RESULT_OK, intent);
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
        ArrayList<Map<String, Object>> list = RemindUtils.getReminds();
        Map<String, Object> map;
        Remind remind = new Remind(this);
        for (int i = 0; i < list.size(); i++) {
            map = list.get(i);
            if ((boolean) map.get("open")) {
                remind.turnAlarm(map, true);
            }
        }
    }


    /*
    * 保存闹钟信息
    * */
    private void saveRemind() {
        Medicine = e_Medicine.getText().toString();
        Tips = e_tips.getText().toString();
        RepeatDay = RemindUtils.getDataDayofWeek(getRepeatDay());
        AlarmId = hour + "" + min + "" + getSec();
        Log.d("day", RepeatDay);
        remind.setTime_hour(hour);
        remind.setTime_min(min);
        remind.setOpen(true);
        remind.setMedicine(Medicine);
        remind.setTips(Tips);
        remind.setRingtone(RingName);
        remind.setRingtone_id(RingId);
        remind.setRepeat(RepeatDay);
        remind.setAlarmId(AlarmId);
        remind.save();
    }

    /*
        /*
        * 保存是否震动
        * */
    private void saveVibrate() {
        slideSwitch.setSlideListener(new SlideSwitch.SlideListener() {
            @Override
            public void open() {
                remind.setVibrator(true);
            }

            @Override
            public void close() {
                remind.setVibrator(false);
            }
        });
    }

    /*
    * 保存重复的天数
    * */
    private int[] getRepeatDay() {
        String dayRepeat = "";
        if (mDay1.isChecked()) {
            dayRepeat += "1" + " ";
        }
        if (mDay2.isChecked()) {
            dayRepeat += "2" + " ";
        }
        if (mDay3.isChecked()) {
            dayRepeat += "3" + " ";
        }
        if (mDay4.isChecked()) {
            dayRepeat += "4" + " ";
        }
        if (mDay5.isChecked()) {
            dayRepeat += "5" + " ";
        }
        if (mDay6.isChecked()) {
            dayRepeat += "6" + " ";
        }
        if (mDay7.isChecked()) {
            dayRepeat += "7" + " ";
        }
        if (dayRepeat.equals("")) {
            dayRepeat = "0 ";
        }
        return RemindUtils.getAlarmDayofWeek(dayRepeat);
    }

    /*
    * 获取秒数，，主要的为了有不同的闹钟id；
    * */
    public int getSec() {
        now = Calendar.getInstance();
        int s = now.getTime().getSeconds();
        return s;
    }
}
