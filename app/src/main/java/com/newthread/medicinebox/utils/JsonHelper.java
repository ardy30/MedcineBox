package com.newthread.medicinebox.utils;

import com.newthread.medicinebox.bean.MedicineBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 张浩 on 2016/2/19.
 */
public class JsonHelper {

    public ArrayList<MedicineBean> list;


    public ArrayList<MedicineBean> getMedicineInfo(JSONObject jsonObject){
        list=new ArrayList<>();
        try {
            String resultCode=jsonObject.getString("message");
            switch (resultCode){
                case "查询成功":
                        JSONObject object=jsonObject.getJSONObject("contents");
                        MedicineBean bean=new MedicineBean();
                        bean.setName(object.getString("medicineName"));
                        bean.setProduct(object.getString("medicineProduct"));
                        bean.setImageurl(object.getString("medicinePicture"));
                        bean.setSympthon(object.getString("medicineSympton"));
                        bean.setSize(object.getString("medicineSize"));
                        bean.setConsume(object.getString("medicineConsume"));
                        list.add(bean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }



    public ArrayList<MedicineBean> getMedicineSearchInfo(JSONObject jsonObject) throws JSONException {
        list=new ArrayList<>();
        String resultCode=jsonObject.getString("message");
        switch (resultCode){
            case "查询成功":
                JSONArray array=jsonObject.getJSONArray("contents");
                for (int i=0;i<array.length();i++){
                    JSONObject object=array.getJSONObject(i);
                    MedicineBean bean=new MedicineBean();
                    bean.setName(object.getString("medicineName"));
                    bean.setProduct(object.getString("medicineProduct"));
                    bean.setImageurl(object.getString("medicinePicture"));
                    bean.setSympthon(object.getString("medicineSympton"));
                    bean.setSize(object.getString("medicineSize"));
                    bean.setConsume(object.getString("medicineConsume"));
                    list.add(bean);
                }
                break;
        }
        return list;
    }
}
