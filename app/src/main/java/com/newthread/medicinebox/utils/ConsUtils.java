package com.newthread.medicinebox.utils;

/**
 * Created by 张浩 on 2016/1/29.
 */
public class ConsUtils {
    //请求码与返回码
    public static final int SET_ALARM_DONE=0;//闹钟设置成功
    public static final int SET_ALARM_CANCEL=1;//闹钟设置取消
    public static final int UPDATE_ALARM_DONE=2;//闹钟设置成功
    public static final int UPDATE_ALARM_CANCEL=3;//闹钟修改取消
    public static final int ADD_ALARM=0;//跳转到闹钟添加
    public static final int ASK_FOR_RING=5;//跳转到闹钟添加
    public static final int RING_SET_DONG=6;//跳转到闹钟添加
    public static final int RING_SET_CANCEL=7;//跳转到闹钟添加
    public static final int UPDATE_ALARM=8;//修改闹钟
    public static final int SIGN_UP_SUCCESS=10;//注册成功
    public static final int EDIT_COMPLETE=11;//填写完整
    public static final int RIGHT_EMAIL=12;//正确邮箱
    public static final int SAME_PASSWORD = 13;//密码相同
    public static final int LOGIN_SUCCESS= 14;//登录成功
    public static final int LOGIN_FAILED = 15;//登录失败
    public static final int SET_INFORMATION = 16;//设置个人信息
    public static final int SET_INFORMATION_DONE=17;//设置完成
    public static final int COMPLETE_NAME = 20;//相机请求
    public static final int RIGHT_AGE = 21;//正确的年龄
    public static final int CAMERA_REQUEST_CODE = 18;//相机请求
    public static final int PICK_PICTURE = 19;//相机请求
    public static final int REFRESHED = 22;//刷新完成
    public static final int FIRST_LOADED = 23;//进入加载
    public static final int LOAD_MORE=24;//加载更多_
    public static final int LOAD_FINISH=25;//加载完成
    public static final int LOAD_LOCAL=26;//加载缓存
    public static final int LOAD_INS=27;//说明书加载完成
    public static final int GET_IMAGE_PIC=28;//图片
    public static final int GET_IMAGE_CRA=29;//相机
    public static final int PARSE_FINISH=30;//解析数据完成



    //文件lujing
    public static String path="data/data/com.newthread.medicinebox/cache/cropped.png";
    public static String path_cache ="data/data/com.newthread.medicinebox/cache/";
    public static String path_img="data/data/com.newthread.medicinebox/cache/headimg";
    public static String path_sp="data/data/com.newthread.medicinebox/shared_prefs/data.xml";


    //
    public static final String PACKAGE_URL_SCHEME = "package:";
}
