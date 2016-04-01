package com.newthread.medicinebox.utils.JsonUtils;

import com.newthread.medicinebox.bean.MedicineBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 张浩 on 2016/2/19.
 */
public class JsonHelper {
    public interface OnSearchListener{
        void onSuccess(ArrayList<MedicineBean> list);
        void onError(String error);
    }
    public interface OnSendPostListener{
        void onSuccess(String success);
        void onError(String error);
    }
    static JsonHelper jsonHelper;
    public static JsonHelper getInstance(){
        if (jsonHelper==null)
            jsonHelper=new JsonHelper();
        return jsonHelper;
    }
    public ArrayList<MedicineBean> list;

    MedicineBean bean;

    /**
     * 二维码获取药品信息
     * @param jsonObject
     * @return
     */
    public ArrayList<MedicineBean> getMedicineInfo(JSONObject jsonObject){
        list=new ArrayList<>();
        bean=new MedicineBean();
        try {
            String resultCode=jsonObject.getString("message");
            switch (resultCode){
                case "查询成功":
                        JSONObject object=jsonObject.getJSONObject("contents");
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


    /**
     * 查询药品信息
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    public void getMedicineSearchInfo(JSONObject jsonObject,OnSearchListener listener){
        list=new ArrayList<>();
        String resultCode= null;
        try {
            resultCode = jsonObject.getString("message");

        switch (resultCode){
            case "查询成功":
                JSONArray array=jsonObject.getJSONArray("contents");
                for (int i=0;i<array.length();i++){
                    bean=new MedicineBean();
                    JSONObject object=array.getJSONObject(i);
                    bean.setName(object.getString("medicineName"));
                    bean.setProduct(object.getString("medicineProduct"));
                    bean.setImageurl(object.getString("medicinePicture"));
                    bean.setSympthon(object.getString("medicineSympton"));
                    bean.setSize(object.getString("medicineSize"));
                    bean.setConsume(object.getString("medicineConsume"));
                    list.add(bean);
                }
                if (listener!=null){
                    listener.onSuccess(list);
                }
                break;
            case "查询失败":
                if (listener!=null){
                    listener.onError("查询失败");
                }
                break;
        }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //return list;
    }

    /*
      * 发送帖子后的结果的判断
      *
      * */
    public void getSendPostResult(String Result,OnSendPostListener listener){
        try {

            JSONObject object=getJSON(Result);
            switch (object.getString("code")){
                case "N01":
                    if (listener!=null)
                        listener.onSuccess("N01");
                    break;
                case "E01":
                    if (listener!=null)
                        listener.onError("E01");
                    break;
                case "E02":
                    if (listener!=null)
                        listener.onError("E02");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    /*
  * 转化为Json
  * */
    public static JSONObject getJSON(String sb) throws JSONException {
        return new JSONObject(sb);
    }




}
