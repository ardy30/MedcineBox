package com.newthread.medicinebox.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by 张浩 on 2016/2/16.
 */
public class RecMedicineBean extends DataSupport {
    public String getMedicineImg() {
        return MedicineImg;
    }

    public void setMedicineImg(String medicineImg) {
        MedicineImg = medicineImg;
    }

    public String getMedicineSummary() {
        return MedicineSummary;
    }

    public void setMedicineSummary(String medicineSummary) {
        MedicineSummary = medicineSummary;
    }

    public String getMedicineType() {
        return MedicineType;
    }

    public void setMedicineType(String medicineType) {
        MedicineType = medicineType;
    }

    public String getMedicineUrl() {
        return MedicineUrl;
    }

    public void setMedicineUrl(String medicineUrl) {
        MedicineUrl = medicineUrl;
    }

    private String MedicineImg;
    private String MedicineSummary;
    private String MedicineType;
    private String MedicineUrl;
}
