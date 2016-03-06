package com.newthread.medicinebox.utils.EventBusUtils;

/**
 * Created by 张浩 on 2016/2/18.
 */
public class MyEventHelp {
    public MyEventHelp(Boolean added){
        Added=added;
    }
    public boolean isAdded() {
        return Added;
    }

    public void setAdded(boolean added) {
        Added = added;
    }

    public boolean Added;
}
