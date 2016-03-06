package com.newthread.medicinebox.bean;

/**
 * Created by 张浩 on 2016/2/19.
 */
public class MedicineBean {
    private String name;
    private String product;
    private String imageurl;

    public String getSympthon() {
        return Sympthon;
    }

    public void setSympthon(String sympthon) {
        Sympthon = sympthon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getConsume() {
        return consume;
    }

    public void setConsume(String consume) {
        this.consume = consume;
    }

    private String Sympthon;
    private String size;
    private String consume;
}
