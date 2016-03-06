package com.newthread.medicinebox.utils.FindUtils;

import com.newthread.medicinebox.bean.FindBean;

import org.litepal.crud.DataSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发现版块数据库工具类
 * Created by 张浩 on 2016/2/9.
 */
public class FindDataUtils {
    public FindBean bean;
    public String content;
    /*
    * set信息列表
    * */
    public void setDataBean(ArrayList<FindBean> find_news,String title,String content,String detail_url,String img_url,String date,String lable)
    {
        bean=new FindBean();
        bean.setTitle(title);
        bean.setContent(content);
        bean.setDate(date);
        bean.setImg_url(img_url);
        bean.setDetail_url(detail_url);
        bean.setLable(lable);
        find_news.add(bean);
    }

    public void saveContent(String content) {

    }

    /*
    * 储存信息
    * */
//    public void saveData(String Title, String List_content, String List_url, String ImageList, String Read, String Comment, String Date) {
//        FindBean bean = new FindBean();
//        bean.setTitle(Title);
//        bean.setList_content(List_content);
//        bean.setList_url(List_url);
//        if (ImageList != null) {
//            bean.setImage_list(ImageList);
//        } else {
//            bean.setImage_list("");
//        }
//        bean.setRead(Read);
//        bean.setComment(Comment);
//        bean.setDate(Date);
//        bean.save();
//    }

    /*
    *
    * 获取信息
    * */
//    public ArrayList<Map<String, Object>> getNewData() {
//        find_news = new ArrayList<>();
//        List<FindBean> list = DataSupport.select("title", "list_url", "list_content", "image_list",
//                "date", "comment", "read").find(FindBean.class);
//        for (FindBean bean : list) {
//            map = new HashMap<>();
//            map.put("title", bean.getTitle());
//            map.put("list_url", bean.getList_url());
//            map.put("list_content", bean.getList_content());
//            map.put("image_list", bean.getImage_list());
//            map.put("date", bean.getDate());
//            map.put("comment", bean.getComment());
//            map.put("read", bean.getRead());
//            find_news.add(map);
//        }
//        return find_news;
//    }

    /*
    *删除数据库
    * */
//    public void DeleteDataBase(){
//        DataSupport.deleteAll(FindBean.class);
//    }

}
