package com.newthread.medicinebox.ui.remind;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.newthread.medicinebox.R;
import com.newthread.medicinebox.theme.StatusBarCompat;
import com.newthread.medicinebox.ui.activity.SwipeBackActivity;
import com.newthread.medicinebox.utils.ConsUtils;

import java.io.IOException;

/**
 * Created by 张浩 on 2016/1/29.
 */
public class RingSetActivity extends SwipeBackActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private ListView ringList;
    private String[] ringName=new String[]{"MiMix","Freedom","Game","GuitarClassic","GuitarPop",
            "MorningSunshine","MusicBox","Orange","Morning","Raindrops","Thinker"};
    private String[] songId=new String[]{"MiMix.ogg","Freedom.ogg","Game.ogg","GuitarClassic.ogg","GuitarPop.ogg",
            "MorningSunshine.ogg","MusicBox.ogg","Orange.ogg",
            "Morning.ogg","Raindrops.ogg","Thinker.ogg"};
    private int currentItem;
    private MyAdapter mAdapter;
    private MediaPlayer mPlayer;
    private Boolean isPlaying;
    private String serRingName;//最终选定的名字
    private String setRingId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ring_set);
        initView();
        initAdapter();
        initListener();
    }
    /*
    * 初始化适配器
    * */
    private void initAdapter() {
        mAdapter=new MyAdapter();
        ringList.setAdapter(mAdapter);
    }

    private void initView() {
        initToolBar();
        ringList= (ListView) findViewById(R.id.ringList);
        serRingName="MiMix.ogg";
        currentItem=0;
        setRingId=songId[0];
        Log.d("alarm", "默认得到的id" + setRingId);
        isPlaying=false;
    }

    private void initToolBar() {
        StatusBarCompat.compat(RingSetActivity.this,getResources().getColor(R.color.colorPrimaryDark));
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar_title= (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setVisibility(View.GONE);
        toolbar.setTitle(R.string.chooseRingtone);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTheSong();
                setResult(ConsUtils.RING_SET_CANCEL,new Intent());
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopTheSong();
        setResult(ConsUtils.RING_SET_CANCEL, new Intent());
    }

    /*
        *
        *
        * */
    private void initListener() {
        ringList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                serRingName = ringName[position];
                setRingId = songId[position];
                currentItem = position;
                mAdapter.notifyDataSetChanged();
                if (isPlaying) {
                    stopTheSong();
                }
                ringTheSong(position);
            }
        });
    }
    /*
    *
    * 播放铃声
    * */
    private void ringTheSong(int position) {
        AssetFileDescriptor assetFileDescriptor= null;
        try {
            mPlayer=new MediaPlayer();
            assetFileDescriptor = this.getAssets().openFd(songId[position]);
            mPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            mPlayer.setLooping(true);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isPlaying=true;

    }

    /*
    * 停止播放
    * */
    private void stopTheSong() {
        if(mPlayer!=null){
            mPlayer.stop();
            mPlayer.release();
            isPlaying=false;
        }
    }
    /*
    * 完成设置
    * */
    public void doneRing(){
        stopTheSong();
        Intent intent=new Intent();
        intent.putExtra("songName", serRingName);
        intent.putExtra("songId", setRingId);
        setResult(ConsUtils.RING_SET_DONG, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_regist:
               doneRing();
                break;
        }
        return super.onOptionsItemSelected(item);
    }




    /*
    * 适配器
    * */
    class MyAdapter extends BaseAdapter{
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null){
                holder=new ViewHolder();
                convertView=View.inflate(RingSetActivity.this,R.layout.ringset_item,null);
                holder.Name= (TextView) convertView.findViewById(R.id.tv_name_ring);
                holder.Radio= (RadioButton) convertView.findViewById(R.id.rb_check_ring);
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }
            holder.Name.setText(ringName[position]);
            if(position==currentItem){
                holder.Radio.setChecked(true);
            }else{
                holder.Radio.setChecked(false);
            }
            return convertView;
        }
        @Override
        public int getCount() {
            return ringName.length;
        }

        @Override
        public Object getItem(int position) {
            return ringName[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    static class ViewHolder{
        TextView Name;
        RadioButton Radio;
    }


    @Override
    public void onPanelOpened(View view) {
        super.onPanelOpened(view);
        stopTheSong();
    }
}
