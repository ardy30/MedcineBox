package com.newthread.medicinebox.utils.HomeUtils;

import com.newthread.medicinebox.bean.RecMedicineBean;

import java.util.ArrayList;

/**
 * Created by 张浩 on 2016/2/16.
 */
public class HomeDataUtils {
    public RecMedicineBean bean;
    public String content;
    /*
    * set药品信息信息列表
    * */
    public void setDataBean(ArrayList<RecMedicineBean> medicine_list,String MedicineImage,String MedicineSummary,
                            String MedicineType,String MedicineUrl)
    {
        bean=new RecMedicineBean();
        bean.setMedicineImg(MedicineImage);
        bean.setMedicineSummary(MedicineSummary);
        bean.setMedicineType(MedicineType);
        bean.setMedicineUrl(MedicineUrl);
        medicine_list.add(bean);
    }
}
