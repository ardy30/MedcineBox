package com.newthread.medicinebox.utils.AlarmUtils;

import android.util.Log;

import com.newthread.medicinebox.bean.RemindBean;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 张浩 on 2016/1/27.
 */
public class RemindUtils {
    public static ArrayList<Map<String,Object>> reminds;
    public static Map<String,Object> map;
    /*
    * 返回一个提醒的ArrayList
    * */
    public static ArrayList<Map<String,Object>> getReminds(){
        List<RemindBean> list= DataSupport.select("medicine","tips","ringtone","vibrator",
                "time_hour","time_min","open","alarmid").find(RemindBean.class);
        reminds=new ArrayList<>();
        for (RemindBean bean:list){
            map=new HashMap<>();
            map.put("ringtone",bean.getRingtone());
            map.put("medicine",bean.getMedicine());
            map.put("tips",bean.getTips());
            map.put("time_hour",bean.getTime_hour());
            map.put("time_min",bean.getTime_min());
            map.put("open",bean.isOpen());
            map.put("id",bean.getAlarmId());
            map.put("vibrator",bean.isVibrator());
            reminds.add(map);
        }
        return reminds;

    }

    /*
    * 根据条件返回ArrayList
    * */
    public static ArrayList<Map<String,Object>> getDetailReMinds(String id){
        List<RemindBean> list=DataSupport.select("medicine","tips","ringtone","time_hour","time_min",
                "vibrator").where("alarmid=?", id).find(RemindBean.class);
        reminds=new ArrayList<>();
        for (RemindBean bean:list){
            map=new HashMap<>();
            map.put("medicine",bean.getMedicine());
            map.put("tips",bean.getTips());
            map.put("time_hour",bean.getTime_hour());
            map.put("time_min",bean.getTime_min());
            map.put("ringtone",bean.getRingtone());
            map.put("vibrator",bean.isVibrator());
            reminds.add(map);
        }
        return reminds;
    }




    /*
    *
    * */
    public static int[] getAlarmDayofWeek(String dayOfWeek) {
        String[] change= dayOfWeek.split(" ");
        int[] Day=new int[change.length];
        for (int i=0;i<change.length;i++){
            Day[i]=Integer.parseInt(change[i]);
        }
        return Day;
    }

    /*
    * 将重复的天数从数组变为字符串
    * */
    public static String getDataDayofWeek(int[] Day) {
        String dayOfWeek = "";
        for (int i = 0; i < Day.length; i++) {
            int day = Day[i];
            if (i == Day.length - 1) {
                dayOfWeek = dayOfWeek + day;
            } else {
                dayOfWeek = dayOfWeek + day + "";
            }
        }
        return dayOfWeek;
    }

}
