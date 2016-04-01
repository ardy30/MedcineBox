package com.newthread.medicinebox.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 张浩 on 2016/2/17.
 */
public class HelpBean extends UserInfo{
    private int communicateId;
    private String TopicContent;
    private List<String> picList;
    private int communicateZhuan;

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public int getCommunicateZhuan() {
        return communicateZhuan;
    }

    public void setCommunicateZhuan(int communicateZhuan) {
        this.communicateZhuan = communicateZhuan;
    }

    public int getCommunicateId() {
        return communicateId;
    }

    public void setCommunicateId(int communicateId) {
        this.communicateId = communicateId;
    }

    public String getTopicContent() {
        return TopicContent;
    }

    public void setTopicContent(String topicContent) {
        TopicContent = topicContent;
    }

    public List<String> getPicList() {
        return picList;
    }

    public void setPicList(List<String> picList) {
        this.picList = picList;
    }

    private String CreateTime;
}
