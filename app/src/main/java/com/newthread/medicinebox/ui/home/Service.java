package com.newthread.medicinebox.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.newthread.medicinebox.R;
import com.newthread.medicinebox.ui.activity.DeveloperActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 张浩 on 2016/1/16.
 */
public class Service extends Fragment {
    private View view;
    private GridView gridView,gridView1;
    private List<Map<String,Object>> list,list1;
    private SimpleAdapter adapter,adapter1;
    private String[] item_Name={"优惠","配送服务","售后服务",
            "购药咨询"
    };
    private String [] item_Name1={"私人医生","预约挂号","电话咨询",
            "视频咨询"};
    private int[] item_Image={R.drawable.img_youhui,R.drawable.img_peisongfuwu,
            R.drawable.img_shouhoufuwu,R.drawable.img_gouyaozixun
    };
    private int []item_Image1={R.drawable.img_mydoctor,R.drawable.img_yuyueguahao,
            R.drawable.img_dianhuazixun,R.drawable.img_shipzixun};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.service,container,false);
        initView();
        return view;
    }


    /*
    * 获取数据
    * */
    private List<Map<String,Object>> getData(){
        list=new ArrayList<>();
        for (int i=0;i<item_Name.length;i++)
        {
            Map<String,Object> map=new HashMap<>();
            map.put("item_image",item_Image[i]);
            map.put("item_name",item_Name[i]);
            list.add(map);
        }
        return list;
    }

    private List<Map<String,Object>> getData1(){
        list1=new ArrayList<>();
        for (int i=0;i<item_Name1.length;i++){
            Map<String,Object> map=new HashMap<>();
            map.put("item_image1",item_Image1[i]);
            map.put("item_name1",item_Name1[i]);
            list1.add(map);
        }
        return list1;
    }

    /*
    *
    * */
    private void initView() {
        gridView= (GridView) view.findViewById(R.id.service_gridview);
        gridView1= (GridView) view.findViewById(R.id.service_gridview1);
        initAdapter();
    }
    /*
    * 初始化设配器
    * */
    private void initAdapter() {
        adapter=new SimpleAdapter(getActivity(),getData(),
                R.layout.security_item,new String[]{"item_image","item_name"}
                ,new int[]{R.id.image_item,R.id.text_item});
        adapter1=new SimpleAdapter(getActivity(),getData1(),
                R.layout.security_item,new String[]{"item_image1","item_name1"}
                ,new int[]{R.id.image_item,R.id.text_item});
        gridView.setAdapter(adapter);
        gridView1.setAdapter(adapter1);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getContext(),DeveloperActivity.class));
            }
        });
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getContext(),DeveloperActivity.class));
            }
        });
    }
}
