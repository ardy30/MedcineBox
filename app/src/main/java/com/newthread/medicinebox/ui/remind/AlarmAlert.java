package com.newthread.medicinebox.ui.remind;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.newthread.medicinebox.R;
import com.newthread.medicinebox.bean.RemindBean;
import com.newthread.medicinebox.theme.StatusBarCompat;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

/**
 * Created by 张浩 on 2016/1/31.
 */
public class AlarmAlert  extends AppCompatActivity {
    private String id;
    private String medicine;
    private String tips;
    private String ringtone;
    private Boolean vibrator;
    private MediaPlayer mPlayer;
    private Vibrator vib;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        StatusBarCompat.compat(this,getResources().getColor(R.color.colorPrimaryDark));
        QueryData();
        StartRing();
    }

    /*
    * 开始响铃
    * */
    private void StartRing() {
        initAlertDialog();
        if (vibrator){
            startVibrator();
        }
        if (ringtone!=null){
            ringTheAlarm(ringtone);
        }
    }

    /*
    * 消息提示框
    * */
    private void initAlertDialog() {
        AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle(R.string.eat)
                .setMessage(medicine + "\n" + tips)
                .setCancelable(false)
                .setPositiveButton(R.string.readed, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (mPlayer != null) {
                            mPlayer.stop();
                        }
                        if (vib != null) {
                            vib.cancel();
                        }
                        finish();
                    }
                })
                .show();
    }

    /*
    * 进行数据查询
    * */
    private void QueryData() {
        List<RemindBean> list= DataSupport.select("ringtone_id", "medicine", "tips",
                "vibrator").where("alarmid=?", id).find(RemindBean.class);
        for(RemindBean bean:list){
            ringtone=bean.getRingtone_id();
            medicine=bean.getMedicine();
            tips=bean.getTips();
            vibrator=bean.isVibrator();
        }
    }

    /*
      *
      * 播放铃声
      * */
    private void ringTheAlarm(String song) {
        AssetFileDescriptor assetFileDescriptor= null;
        try {
            mPlayer=new MediaPlayer();
            assetFileDescriptor = this.getAssets().openFd(song);
            mPlayer.reset();
            mPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            mPlayer.setLooping(true);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * 震动
    * */
    private void startVibrator(){
        vib= (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long [] pattern={300,600,300,600};
        vib.vibrate(pattern,2);
    }
}
