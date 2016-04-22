package com.newthread.medicinebox.utils;

/**
 * Created by 张浩 on 2016/2/19.
 */
public class ApiUtils {
    public static String ServerAddress="http://drugbox.imcloud.link/";//服务器地址
    public static String QrScanUrl=ServerAddress+"medicine/medicineinfo.do?id=";//二维码扫描
    public static String imgUrl =ServerAddress+"medicine/";//二维码扫描图片
    public static String SearchUrl=ServerAddress+"medicine/findmedicinelist.do?name=";//药品搜索地址
    public static String SignUpUrl=ServerAddress+"user/register.do";//注册
    public static String LoginUrl=ServerAddress+"user/login.do";//登录
    public static String GetUserInfoUrl=ServerAddress+"user/userinfo.do";//获取用户信息
    public static String UpdateUserInfo=ServerAddress+"user/updateinfo.do";//跟新用户信息
    public static String UpLoadUserPic =ServerAddress+"user/uploaduserpic.do";//跟新用户头像
    public static String GetUserImg=ServerAddress+"user/User_Pciture/";//下载用户头像
    public static String UpLoadHelpPic=ServerAddress+"forum/uploadpic.do";//上传帮助帖子图片
    public static String UpLoadHelpPostCont=ServerAddress+"forum/addcommunication.do";//上传帖子内容
    public static String getPostMaxCount=ServerAddress+"forum/communicationmaxcount.do";//获取帖子最大数量
    public static String getPostImages=ServerAddress+"forum/Communication_Picture/";//获取帖子图片
    public static String getPostUserPic=ServerAddress+"user/";//获取帖子用户头像
    public static String Zan=ServerAddress+"forum/zan.do";//点赞接口
    public static String AddComment=ServerAddress+"forum/addcomment.do";//添加评论
    public static String GetCommentMaxCount=ServerAddress+"forum/commentinfomaxcount.do?communicateid=";//获取评论最大数
    /**
     * 获取帮助帖子首页数据
     * @param page
     * @param capacity
     * @param maxCount
     * @return
     */
    public static String GetPostItem(int page,int capacity,int maxCount){
      return ServerAddress+"forum/communicationtitlelist.do?"+"page="+page+"&capacity="+capacity
              +"&start="+maxCount;
    }

    /**
     * 获取帮助评论列表
     * @param communicateid
     * @param start
     * @param page
     * @param capacity
     * @return
     */
    public static String GetPostComment(int communicateid,int start,int page,int capacity){
        return ServerAddress+"forum/commentinfolist.do?communicateid="+communicateid+
                "&start="+start+"&page="+page+"&capacity="+capacity;
    }
}
