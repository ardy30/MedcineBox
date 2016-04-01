package com.newthread.medicinebox.ui.remind;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leaking.slideswitch.SlideSwitch;
import com.newthread.medicinebox.R;
import com.newthread.medicinebox.bean.RemindBean;
import com.newthread.medicinebox.theme.StatusBarCompat;
import com.newthread.medicinebox.ui.activity.SwipeBackActivity;
import com.newthread.medicinebox.ui.view.DividerItemDecoration;
import com.newthread.medicinebox.utils.AlarmUtils.RemindUtils;
import com.newthread.medicinebox.utils.ConsUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 用药提醒的Activity
 * Created by 张浩 on 2016/1/21.
 */
public class MyRemindListActivity extends SwipeBackActivity {
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private FloatingActionButton add_alarm;
    private RecyclerView recyclerView;
    public static final int ADD_REMIND = 1;
    private MyRemindsAdapter adapter;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remind);
        ButterKnife.bind(this);
        initView();
    }

    /*
    *
    * */
    private void initView() {
        toolbar.setTitle(R.string.yongyaotixing);
        setUpToolBar(toolbar, true, true);
        recyclerView = (RecyclerView) findViewById(R.id.my_remind);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        setAdapter();
        add_alarm = (FloatingActionButton) findViewById(R.id.add_alarm);
        StatusBarCompat.compat(this, getResources().getColor(R.color.colorPrimaryDark));
        add_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyRemindListActivity.this, AddRemindActivity.class);
                intent.putExtra("medicine", "");
                startActivityForResult(intent, ConsUtils.ADD_ALARM);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setAdapter();
        }
    }


    /*
    *适配器
    * */
    public static class MyRemindsAdapter extends RecyclerView.Adapter<MyRemindsAdapter.ViewHolder> {
        private ArrayList<Map<String, Object>> list = RemindUtils.getReminds();
        private Map<String, Object> map;

        public interface OnItemClickListener {
            void onItemClick(View view, int position);

            void onItemLongClick(View view, int position);
        }

        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder holder = new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.reminditem, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            map = list.get(position);
            int h = (int) map.get("time_hour");
            int m = (int) map.get("time_min");
            String medicine = (String) map.get("medicine");
            String tips = (String) map.get("tips");
            final String id = (String) map.get("id");
            final Boolean open = (Boolean) map.get("open");
            String time = timeExchange(h, m);
            Calendar calendar = Calendar.getInstance();
            holder.time.setText(time);
            holder.medicine.setText(medicine);
            holder.tips.setText(tips);
            holder.slideSwitch.setState(open);
            holder.slideSwitch.setSlideListener(new SlideSwitch.SlideListener() {
                @Override
                public void open() {
                    Log.d("close", "开启了闹钟" + id);
                    ContentValues values = new ContentValues();
                    values.put("open", true);
                    DataSupport.updateAll(RemindBean.class, values, "alarmid=?", id);
                }

                @Override
                public void close() {
                    Log.d("close", "关闭了闹钟" + id);
                    ContentValues values = new ContentValues();
                    values.put("open", false);
                    DataSupport.updateAll(RemindBean.class, values, "alarmid=?", id);
                }
            });
            /*
            * onItemClickListener
            * */
            if (onItemClickListener != null) {

            /*
            * onItemClickListener
            * */
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.itemView, pos);
                    }
                });
           /*
            * onItemLongClickListener
            * */
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = holder.getLayoutPosition();
                        onItemClickListener.onItemLongClick(holder.itemView, pos);
                        return false;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView time, medicine, tips;
            SlideSwitch slideSwitch;

            public ViewHolder(View itemView) {
                super(itemView);
                time = (TextView) itemView.findViewById(R.id.item_time);
                medicine = (TextView) itemView.findViewById(R.id.item_medicine);
                tips = (TextView) itemView.findViewById(R.id.item_tips);
                slideSwitch = (SlideSwitch) itemView.findViewById(R.id.item_open);
            }
        }

        public void onRemove(int position) {
            list.remove(position);
            notifyItemRemoved(position);
        }
    }


    /*
    * 时间的转化
    * */
    private static String timeExchange(int h, int m) {
        String hourString = h < 10 ? "0" + h : "" + h;
        String minuteString = m < 10 ? "0" + m : "" + m;
        String time = hourString + ":" + minuteString;
        return time;
    }

    /* *//*
    * 获取数据
    * *//*
    private static void getData(Map<String,Object> map){
        medicine= (String) map.get("medicine");
        tips= (String) map.get("tips");
        h= (int) map.get("time_hour");
        m= (int) map.get("time_min");
        vibrator= (boolean) map.get("vibrator");
        ringtone= (String) map.get("ringtone");
        open=(Boolean) map.get("open");
        alarmid= (String) map.get("id");
        Log.d("alarm",h+"---"+m+medicine+tips+ringtone+vibrator+alarmid);
    }*/
    /*
    * setAdapter
    * */
    private void setAdapter() {
        adapter = new MyRemindsAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MyRemindsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MyRemindListActivity.this, EditRemindActivity.class);
                String id = getPosition(position);
                intent.putExtra("id", id);
                startActivityForResult(intent,
                        ConsUtils.UPDATE_ALARM);
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                dialog = new AlertDialog.Builder(MyRemindListActivity.this)
                        .setMessage(R.string.suredelete)
                        .setCancelable(true)
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ArrayList<Map<String, Object>> list = RemindUtils.getReminds();
                                Map<String, Object> map = list.get(position);
                                String id = (String) map.get("id");
                                adapter.onRemove(position);
                                System.out.println("test" + position + "----" + list.size());
                                DataSupport.deleteAll(RemindBean.class, "alarmid=?", id);
                                dialog.dismiss();
                            }
                        }).show();
            }
        });


    }


    public String getPosition(int position) {
        Map<String, Object> map = RemindUtils.getReminds().get(position);
        String id = (String) map.get("id");
        /*System.out.println(map);
        System.out.println("11111111111111111---"+id);*/
        return id;
    }
}
