package com.newthread.medicinebox.utils.EventBusUtils;

import java.util.List;

/**
 * Created by 张浩 on 2016/2/18.
 */
public class MyEventHelp {
    public boolean isAdded() {
        return Added;
    }

    public boolean Added;
    public List<String> getFilepath() {
        return filepath;
    }
    public List<String> filepath;
    public MyEventHelp(List<String> path){
        filepath=path;
    }

    public MyEventHelp(boolean added){
        Added=added;

    }
}
