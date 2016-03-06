package com.newthread.medicinebox.ui.remind;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.newthread.medicinebox.bean.RemindBean;
import com.newthread.medicinebox.reciver.AlarmReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by 张浩 on 2016/1/29.
 */
public class Remind {

    private Context context;
    private String id;
    private String medicine;
    private String tips;
    private int time_hour;
    private int time_min;
    private boolean vibrator;
    private String ringtone;
    public Remind(Context context){
        this.context=context;
        }

    public void getData(Map<String,Object> map){
        id= (String) map.get("id");
        medicine= (String) map.get("medicine");
        tips= (String) map.get("tips");
        time_hour= (int) map.get("time_hour");
        time_min= (int) map.get("time_min");
        vibrator= (boolean) map.get("vibrator");
        ringtone= (String) map.get("ringtone");
        System.out.println("第："+id+"闹钟"+"\n"+medicine+tips+"\n"+time_hour+":"+time_min+"震动"+vibrator+"铃声"+ringtone);
    }
    public void turnAlarm(Map<String,Object> map,Boolean isOpen){
        getData(map);
        AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("id",id);
        intent.putExtra("medicine",medicine);
        intent.putExtra("tips",tips);
        intent.putExtra("time_hour",time_hour);
        intent.putExtra("time_min",time_min);
        intent.putExtra("vibrator",vibrator);
        intent.putExtra("ringtone",ringtone);
        PendingIntent pi= PendingIntent.getBroadcast(context, Integer.parseInt(id), intent, 0);
        if(isOpen){
            startAlarm(mAlarmManager,pi);
        }else{
            cancelAlarm(intent);
        }
    }

    /*
    *
    * */
    private void cancelAlarm(Intent intent) {
        Log.d("alarm","取消闹钟");
        intent.putExtra("cancel",true);
        context.sendBroadcast(intent);
    }

    /*
    * 开始闹钟
    * */
    public void startAlarm(AlarmManager mAlarmManager, PendingIntent pi){
        Log.d("alarm", "启动闹钟");
        Calendar c=Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,time_hour);
        c.set(Calendar.MINUTE,time_min);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND, 0);
        if(c.getTimeInMillis()<System.currentTimeMillis()){
            if(Build.VERSION.SDK_INT>=19)
            {
                mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() + 24 * 60 * 60 * 1000, pi);
            }else{
                mAlarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() + 24 * 60 * 60 * 1000, pi);
            }
        }else{
            if(Build.VERSION.SDK_INT>=19)
            {
                Log.d("alarm","执行定时任务");
                Date date=c.getTime();
                Log.d("alarm","定时的时间是"+date.toString());
                mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
            }else{
                mAlarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
            }
        }
    }
}
