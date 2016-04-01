package com.newthread.medicinebox.utils;

import android.util.Log;

import com.newthread.medicinebox.bean.FindBean;
import com.newthread.medicinebox.bean.RecMedicineBean;
import com.newthread.medicinebox.utils.FindUtils.FindDataUtils;
import com.newthread.medicinebox.utils.HomeUtils.HomeDataUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * 发现版块信息抓取
 * 等待后台开发结束就换成后台的代码
 * Created by 张浩 on 2016/2/9.
 */
public class JsoupUtils {
    public static FindDataUtils dataUtils=new FindDataUtils();
    /*
    * 解析列表
    * */
    public static void ParseFindListHtml(String s, ArrayList<FindBean> list) {
        Document doc = Jsoup.parse(s, "utf-8");
        Elements elements=doc.getElementsByClass("article");
        for (int i=0;i<elements.size();i++){
            String lable="";
            String img_url=elements.get(i).select("a>img").attr("src");
            String detail_url=elements.get(i).select("a").attr("href");
            String title=elements.get(i).getElementsByClass("text").select("h2>a").text();
            String date=elements.get(i).getElementsByClass("text").select("span").text();
            String content=elements.get(i).getElementsByClass("text").select("p").text();
            Elements lableElements=elements.get(i).getElementsByClass("text").select("div");
            for (int j=0;j<lableElements.size();j++){
               lable=lableElements.get(1).select("a").text();
            }
           System.out.println("imagurl----------------"+img_url);
            dataUtils.setDataBean(list, title, content, detail_url, img_url, date, lable);
        }

    }
    /*
    * 解析内容
    *
    * */
    public String ParseFindContentHtml(String s) {
        String p="";
        Document doc = Jsoup.parse(s, "utf-8");
        Element element=doc.getElementsByClass("article").first().getElementsByClass("art_cont").first();
        Elements elements=element.getElementsByTag("p");
        System.out.println(elements.size());
        for (int i=0;i<elements.size();i++){
            p+=elements.get(i).text()+"\n\n";
        }
        return p;
    }

    /*
    * 抓取首页图片
    * */
    public ArrayList<String> ParseImageListUrl(String s){
        ArrayList<String> list=null;
        Document doc=Jsoup.parse(s,"utf-8");
        Element element=doc.getElementsByClass("play").first().getElementsByTag("ul").first();
        Elements elements=element.getElementsByTag("li");
        Log.d("sizeImg",String.valueOf(elements.size()));
        list=new ArrayList<>();
        for (int i=0;i<elements.size();i++){
            String img_url=elements.get(i).getElementsByClass("jk_1210").attr("src");
             Log.d("imgurl",img_url);
            list.add(img_url);
        }
        return list;
    }

    /*
    * 推荐药品解析
    * */
    public void ParseMedicines(String s,ArrayList<RecMedicineBean> list){
        HomeDataUtils dataUtils=new HomeDataUtils();
        Document doc=Jsoup.parse(s,"utf-8");
        Element element=doc.getElementsByClass("main").first().getElementsByClass("pro-area").first().getElementsByClass("pro-con").first();
        Elements elements=element.getElementsByTag("li");
        for (int i=0;i<elements.size();i++){
            String medicineimg=elements.get(i).getElementsByClass("imgbig").select("img").attr("src");
            String summary=elements.get(i).getElementsByClass("pro-botxt").select("p>a").text();
            String medicineurl=elements.get(i).getElementsByClass("pro-botxt").select("p>a").attr("href");
            String medicinetype=elements.get(i).getElementsByClass("pro-botxt").select("p>a>img").attr("src");
            System.out.println("nimahhh"+medicineimg);
            System.out.println(summary);
            System.out.println(medicineurl);
            System.out.println(medicinetype);
            dataUtils.setDataBean(list, medicineimg, summary, medicinetype, medicineurl);
        }
    }
    /*
    *
    * 解析药品说明书
    * */
    public String ParseMedicineInstruction(String s){
        String instruction="";
        Document document=Jsoup.parse(s,"utf-8");
        Element element=document.getElementsByClass("main").first().getElementsByClass("bigfont").first();
        Elements elements=element.getElementsByTag("p");
        for (int i=0;i<elements.size();i++){
            instruction+=elements.get(i).text()+"\n";
        }
        return instruction;
    }
}