package com.newthread.medicinebox.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 张浩 on 2016/2/17.
 */
public class HelpBean extends BmobObject {

    public String getMedicine() {
        return Medicine;
    }

    public void setMedicine(String medicine) {
        Medicine = medicine;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public mUser getUser() {
        return user;
    }

    public void setUser(mUser user) {
        this.user = user;
    }

    private mUser user;
    private String Medicine;
    private String Question;

    public BmobFile getImage() {
        return Image;
    }

    public void setImage(BmobFile image) {
        Image = image;
    }

    private BmobFile Image;//帖子图片
}
