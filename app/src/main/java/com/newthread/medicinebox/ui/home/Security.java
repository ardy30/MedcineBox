package com.newthread.medicinebox.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.newthread.medicinebox.R;
import com.newthread.medicinebox.ui.activity.DeveloperActivity;
import com.newthread.medicinebox.ui.activity.MainActivity;
import com.newthread.medicinebox.ui.view.NetworkImageHolderView;
import com.newthread.medicinebox.utils.ConsUtils;
import com.newthread.medicinebox.utils.HttpUtils.UrlConnectionUtils;
import com.newthread.medicinebox.utils.JsoupUtils;
import com.newthread.medicinebox.utils.FindUtils.FindUrlConUtils;
import com.newthread.medicinebox.utils.NetWorkHelper;
import com.newthread.medicinebox.utils.SpHelper;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 张浩 on 2016/1/16.
 */
public class Security extends Fragment{
    private String Result;
    private View view;
    private ConvenientBanner my_convenientBanner;
    private ArrayList<String> NetWorkImages;
    private ArrayList<String> localImage;
    private FrameLayout security_refresh;
    SpHelper helper;
    private GridView gridview,gridView1;
    private List<Map<String,Object>> list,list1;
    private SimpleAdapter adapter,adapter1;
    private String [] imgname={"image1","image2","image3","image4","image5"};
    private String[] item_Name={"药品搜索","禁忌","不良反应","警示",
            "相互作用", "疾病专题","健康养生",
            "药监动态"
    };
    private String [] item_Name1={"居家必备","肠胃疾病","皮肤用药",
            "神经系统","妇科用药","男科用药","补益安神","抗生素类","更多"};
    private String []item_Code={"010101","010205","010301","010501","010801","011101","011401","011701"
    };
    private int[] item_Image={R.drawable.img_search,R.drawable.img_jinji,R.drawable.img_jinji,
            R.drawable.img_jingshi,R.drawable.img_xainghuzuoyong, R.drawable.img_jibinzhuangti,
            R.drawable.img_yiliaochangshi,R.drawable.img_yaojiandongtai
    };
    private int []item_Image1={R.drawable.img_jujiabibei,R.drawable.img_jiankangyangsheng,
            R.drawable.img_changjianbin, R.drawable.img_manxinbing,
            R.drawable.img_muying,R.drawable.img_zhongyao,R.drawable.img_liangxing,
            R.drawable.img_xinxingyaoping,R.drawable.img_more};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.security,container,false);
        helper=new SpHelper(getContext());
        getLocalList();
        initLoaclImage();
        initView();
        initImage();
        return view;
    }
    /*
    * 初始化头部的图片
    * */
    private void initImage() {
        NetWorkHelper helper=new NetWorkHelper(getContext());
        if (helper.isOpenNetWork()){
            loadImages();
        }
    }
    //
    public void initLoaclImage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LoadLocalImg();
                    Thread.sleep(500);
                    handler.sendEmptyMessage(ConsUtils.LOAD_LOCAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    /*
    * getLocalImageList
    * */
    public void getLocalList(){
        localImage=new ArrayList<>();
        for (String anImgname : imgname) {
            localImage.add(helper.getImgList(anImgname));
        }
    }
    /*
    * 加载本地缓存
    * */
    public void LoadLocalImg(){
        initImageLoader();
        my_convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>(){
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        },localImage).setPageIndicator(new int[]{R.drawable.ic_page_indicator,
                R.drawable.ic_page_indicator_focused});
    }


    /*
    * 初始化本地图片测试用
    * */
    private void loadImages() {
        new Thread(new Runnable() {
            FindUrlConUtils urlConUtils=new FindUrlConUtils();
            @Override
            public void run() {
                Result=urlConUtils.getResult("http://www.jianke.com/");
                handler.sendEmptyMessage(ConsUtils.LOAD_FINISH);
            }
        }).start();
//
//        localImages=new ArrayList<>();
//        //本地图片集合
//        for (int position = 1; position <5; position++)
//            localImages.add(getResId("image" + position, R.drawable.class));

    }
    /**
     * 通过文件名获取资源id 例子：getResId("icon", R.drawable.class);
     *
     */
//    public static int getResId(String variableName, Class<?> c) {
//        try {
//            Field idField = c.getDeclaredField(variableName);
//            return idField.getInt(idField);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return -1;
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
        my_convenientBanner.startTurning(3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        my_convenientBanner.stopTurning();
    }

    //初始化网络图片缓存库
    private void initImageLoader(){
        //网络图片例子,结合常用的图片缓存库UIL,你可以根据自己需求自己换其他网络图片库
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
                showImageForEmptyUri(R.drawable.ic_default_adimage)
                .cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getContext()).defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
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
            * 初始化
            * */
    private void initView() {
        my_convenientBanner= (ConvenientBanner) view.findViewById(R.id.my_convenientBanner);
        security_refresh= (FrameLayout) view.findViewById(R.id.security_refresh);
        gridview= (GridView) view.findViewById(R.id.security_gridview);
        gridView1= (GridView) view.findViewById(R.id.security_gridview1);
        initAdapter();
    }

    private void initAdapter() {
        adapter=new SimpleAdapter(getActivity(),getData(),
                R.layout.security_item,new String[]{"item_image","item_name"}
                ,new int[]{R.id.image_item,R.id.text_item});
        adapter1=new SimpleAdapter(getActivity(),getData1(),
                R.layout.security_item,new String[]{"item_image1","item_name1"}
                ,new int[]{R.id.image_item,R.id.text_item});
        gridview.setAdapter(adapter);
        gridView1.setAdapter(adapter1);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Menu(position);
            }
        });
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Menu1(position);
            }
        });
    }

    /*
    *
    * */
    private void Menu(int position) {
        Intent intent;
        switch (position){
            case 0:
                intent=new Intent(getContext(),SearchMedicineActivity.class);
                startActivity(intent);
                break;
            default:
                intent=new Intent(getContext(),DeveloperActivity.class);
                startActivity(intent);
                break;

        }
    }
    private void Menu1(int position) {
        switch (position){
            case 8:
                startActivity(new Intent(getContext(),DeveloperActivity.class));
                break;
            default:
                Intent intent = new Intent(getContext(), PushMedicineActity.class);
                intent.putExtra("toolbarname", item_Name1[position]);
                intent.putExtra("urlcode", item_Code[position]);
                startActivity(intent);
                break;
        }
    }


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ConsUtils.LOAD_FINISH:
                    JsoupUtils jsoupUtils = new JsoupUtils();
                    NetWorkImages = new ArrayList<>();
                    if (Result!=null){
                        NetWorkImages = jsoupUtils.ParseImageListUrl(Result);
                        for (int i = 0; i < NetWorkImages.size(); i++) {
                            saveImgUrl(i, NetWorkImages.get(i));
                        }
                    }
                    setImages();
                    security_refresh.setVisibility(View.GONE);
                    break;
                case ConsUtils.LOAD_LOCAL:
                    Log.d("test", "2222");
                    security_refresh.setVisibility(View.GONE);
                    break;
            }
        }
    };

    private void saveImgUrl(int a,String imgurl) {
        Log.d("urlimg",imgurl);
        helper.saveImgList(imgname[a], imgurl);
    }

    private void setImages() {
        initImageLoader();
        my_convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>(){
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        },NetWorkImages).setPageIndicator(new int[]{R.drawable.ic_page_indicator,
                R.drawable.ic_page_indicator_focused});
    }


}
