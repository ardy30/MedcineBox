package com.newthread.medicinebox.bean;

/**
 * Created by 张浩 on 2016/1/16.
 */
public class BaseEntity {
    private String id ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String name;
}
