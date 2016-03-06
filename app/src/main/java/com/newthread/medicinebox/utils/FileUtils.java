package com.newthread.medicinebox.utils;

import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 张浩 on 2015/10/25.
 */
public class FileUtils {
    //判断文件是否存在
    public static  boolean fileIsExists(String strFile)
    {
        try
        {
            File f=new File(strFile);
            if(!f.exists())
            {
                return false;
            }
        }
        catch (Exception e)
        {return false;
        }
        return true;
    }

    /*
    * 文件删除
    * */
    public static void delete(String strFile)
    {
        File f=new File(strFile);
        f.delete();
    }

    //读取指定文件的字符串
    public static  String readFile(String filename) {
        String reads="";
        try {
            FileInputStream fis=new FileInputStream(filename);
            byte[] b = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while (fis.read(b) != -1) {
                baos.write(b, 0, b.length);
            }
            baos.close();
            fis.close();
            reads = baos.toString();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return reads;
    }

    //将string字符串保存到SD卡指定位置
    public static void saveFile(String str,String filePath,String NEWS_FILE_NAME) {
        //判断SD卡是否存在
        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (hasSDCard) {
            //filePath += "hello.txt";
        }
        try {
            File file = new File(filePath,NEWS_FILE_NAME);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(str.getBytes());
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
