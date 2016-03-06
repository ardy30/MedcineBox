package com.newthread.medicinebox.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by 张浩 on 2016/1/27.
 */
public class RemindBean extends DataSupport{
    private int time_hour;
    public String getMedicine() {
        return medicine;
    }
    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }
    private String medicine;
    public String getTips() {
        return tips;
    }
    public void setTips(String tips) {
        this.tips = tips;
    }
    private String tips;

    public int getTime_min() {
        return time_min;
    }

    public void setTime_min(int time_min) {
        this.time_min = time_min;
    }
    public int getTime_hour() {
        return time_hour;
    }
    public void setTime_hour(int time_hour) {
        this.time_hour = time_hour;
    }
    private int time_min;
    private String Ringtone;
    private boolean vibrator;
    public String getRingtone_id() {
        return Ringtone_id;
    }

    public void setRingtone_id(String ringtone_id) {
        Ringtone_id = ringtone_id;
    }

    private String Ringtone_id;

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isVibrator() {
        return vibrator;
    }

    public void setVibrator(boolean vibrator) {
        this.vibrator = vibrator;
    }

    public String getRingtone() {
        return Ringtone;
    }

    public void setRingtone(String ringtone) {
        Ringtone = ringtone;
    }

    private boolean open;

    public String getRepeat() {
        return Repeat;
    }

    public void setRepeat(String repeat) {
        Repeat = repeat;
    }

    private String Repeat;

    public String getAlarmId() {
        return AlarmId;
    }

    public void setAlarmId(String alarmId) {
        AlarmId = alarmId;
    }

    private String AlarmId;
}
