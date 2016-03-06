package com.newthread.medicinebox.ui.activity;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.newthread.medicinebox.R;
import com.newthread.medicinebox.theme.StatusBarCompat;
import com.newthread.medicinebox.ui.Fragment.BackHandledFragment;
import com.newthread.medicinebox.ui.Fragment.HomeFragment;
import com.newthread.medicinebox.ui.Fragment.LoginFragment;
import com.newthread.medicinebox.ui.codescan.CaptureActivity;
import com.newthread.medicinebox.ui.find.FindFragment;
import com.newthread.medicinebox.ui.help.HelpFragment;
import com.newthread.medicinebox.ui.history.HistoryFragment;
import com.newthread.medicinebox.ui.home.SearchMedicineActivity;
import com.newthread.medicinebox.ui.remind.MyRemindListActivity;
import com.newthread.medicinebox.ui.remind.Remind;
import com.newthread.medicinebox.ui.user.LoginActivity;
import com.newthread.medicinebox.ui.user.MeActivity;
import com.newthread.medicinebox.ui.view.CircleImage;
import com.newthread.medicinebox.utils.BitmapUtils;
import com.newthread.medicinebox.utils.ComUtils;
import com.newthread.medicinebox.utils.ConsUtils;
import com.newthread.medicinebox.utils.FileUtils;
import com.newthread.medicinebox.utils.EventBusUtils.MyEventLogin;
import com.newthread.medicinebox.utils.AlarmUtils.RemindUtils;
import com.newthread.medicinebox.utils.NetWorkImageUtils.BitmapLruCache;
import com.newthread.medicinebox.utils.PermissionsChecker;
import com.newthread.medicinebox.utils.SpUtils.SpFirst;
import com.newthread.medicinebox.utils.UserUtils.CurrentUserSp;
import com.newthread.medicinebox.utils.NetWorkImageUtils.HeadImageHelper;
import org.litepal.tablemanager.Connector;
import java.util.ArrayList;
import java.util.Map;
import java.util.zip.Inflater;

import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;
    private BackHandledFragment selectedFragment;
    private long exitTime = 0;
    private TextView toolbar_title;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private CircleImageView headImage;
    public  TextView head_name,head_tips;
    private String nickname;
    private int age;
    private String url_HeadImg;
    CurrentUserSp sp;
    FragmentManager fragmentManager;
    Fragment HomeFragment;
    Fragment FindFragment;
    Fragment HistoryFragment;
    Fragment HelpFragment;
    Fragment LoginFragment;
    Bitmap ImgBitmap;
    View headerView;
    LinearLayout Login_Lin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGuide();
        sp=new CurrentUserSp(this);
        initDataBase();
        initReceiver();
        initView();
        if (initLoginState()){
            getUserData();
            if(FileUtils.fileIsExists(ConsUtils.path_img)){
                ImgBitmap=BitmapUtils.getLoacalBitmap(ConsUtils.path_img);
                if (ImgBitmap!=null){
                    Log.d("bitmap","得到bitmap");
                }
                //circleImage.setImageBitmap(ImgBitmap);
                headImage.setImageBitmap(ImgBitmap);
            }
            head_tips.setVisibility(View.GONE);
            head_name.setText(nickname);
        }else {
            headImage.setImageResource(R.drawable.img_login1);
        }
        switchtoHomeBegin();
        EventBus.getDefault().register(this);
    }


    private void initGuide() {
        SpFirst first=new SpFirst(MainActivity.this);
        if (!first.getFirst()){
            Log.d("first", String.valueOf(false));
            startActivity(new Intent(this, GuideActivity.class));
            finish();
            return;
        }
        Log.d("first", String.valueOf(true));

    }

    /*
    * 初始化登录状态
    * */
    private boolean initLoginState() {
        return sp.getLoginState();
    }
    /*
    * 初始化登录信息
    *
    * */
    private void getUserData(){
        nickname=sp.getNickName();
        age=sp.getAge();
        System.out.println(nickname + "----" + age);
    }
    /*
    * 判断是否登录页面的跳转
    * */
    private void GoToLogin(){
        Intent intent;
        if (initLoginState()){
            intent=new Intent(MainActivity.this, MeActivity.class);
            startActivity(intent);
        }else {
         intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    }
    /*
    * 启动广播
    * */
    public void initReceiver() {
        ArrayList<Map<String,Object>> list= RemindUtils.getReminds();
        Map<String,Object> map;
        Remind remind=new Remind(this);
        for (int i=0;i<list.size();i++){
            map=list.get(i);
            if ((boolean)map.get("open")){
                remind.turnAlarm(map,true);
            }
        }
    }

    /*
    * 初始化（建立）数据库
    * */
    private void initDataBase() {
        SQLiteDatabase database= Connector.getDatabase();
    }

    /*
    * 初始化控件
    * */
    private void initView() {
        initToolBar();

        coordinatorLayout= (CoordinatorLayout) findViewById(R.id.main_content);
        toolbar_title= (TextView) findViewById(R.id.toolbar_title);
        drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView= (NavigationView) findViewById(R.id.navigation_view);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);
        if (ComUtils.CheckBuildVision()){
            drawerLayout.setFitsSystemWindows(true);
            //drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        }
        setupDrawerContent(navigationView);

        headerView=LayoutInflater.from(this).inflate(R.layout.navigation_header,null);
        navigationView.addHeaderView(headerView);
        head_name= (TextView) headerView.findViewById(R.id.Head_Name);
        head_tips= (TextView)headerView. findViewById(R.id.Head_Name_Tip);
        Login_Lin= (LinearLayout) headerView.findViewById(R.id.login_lin);
        headImage= (CircleImageView) headerView.findViewById(R.id.headImage_drawer);
        headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToLogin();
            }
        });
    }
    /*
    *
    * navigation中的drawer的菜单的切换
    * */
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_home:
                        switchtoHome();
                        break;
                    case R.id.navigation_item_find:
                        switchtoFind();
                        break;
                    case R.id.navigation_item_history:
                        switchtoHistory();
                        break;
                    case R.id.navigation_item_help:
                        switchtoHelp();
                        break;
                    case R.id.navigation_item_QR_scan:
                        initPermission();
                        break;
                    case R.id.navigation_item_search:
                        startActivity(new Intent(MainActivity.this, SearchMedicineActivity.class));
                        break;
                    default:
                        startActivity(new Intent(MainActivity.this,DeveloperActivity.class));
                        break;
                }
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    //权限的处理
    private void initPermission() {
                PermissionGen.needPermission(MainActivity.this, 100,
                        new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.FLASHLIGHT
                        });
    }


    /**
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    /**
     * 权限获取成功
     */
    @PermissionSuccess(requestCode = 100)
    public void getPerMissionSuccess(){
            startActivity(new Intent(MainActivity.this, CaptureActivity.class));
        //Snackbar.make(coordinatorLayout,"权限获取成功！",Snackbar.LENGTH_SHORT).show();
    }
    @PermissionFail(requestCode = 100)
    public void getPerMissionFailed(){
        AlertDialog dialog=new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.lackPermission)
                .setMessage(R.string.lackPermission_message)
                .setCancelable(false)
                .setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .show();
    }

    //
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(ConsUtils.PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }
    /*
    * 设置Fragment
    * */
    public void setSelectedFragment(int index){
        fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index){
            case 0:
                if (HomeFragment==null){
                    HomeFragment=new HomeFragment();
                    transaction.add(R.id.frame_content,HomeFragment);
                }else{
                    transaction.show(HomeFragment);
                }
                break;
            case 1:
                if (FindFragment==null){
                    FindFragment=new FindFragment();
                    transaction.add(R.id.frame_content,FindFragment);
                }else{
                    transaction.show(FindFragment);
                }
                break;
            case 2:
                if (sp.getLoginState()){
                    if (HelpFragment==null){
                        HelpFragment=new HelpFragment();
                        transaction.add(R.id.frame_content,HelpFragment);
                    }else{
                        transaction.show(HelpFragment);
                    }
                }else{
                    if (LoginFragment==null){
                        LoginFragment=new LoginFragment();
                        transaction.add(R.id.frame_content,LoginFragment);
                    }else{
                        transaction.show(LoginFragment);
                    }
                }

                break;
            case 3:
                if (HistoryFragment==null){
                    HistoryFragment=new HistoryFragment();
                    transaction.add(R.id.frame_content,HistoryFragment);
                }else{
                    transaction.show(HistoryFragment);
                }
                break;
        }
        transaction.commit();

    }

    /*
    * 隐藏fragment
    * */
    public void hideFragments(FragmentTransaction fragmentTransaction){
        if (HomeFragment!=null){
            fragmentTransaction.hide(HomeFragment);
        }
        if (FindFragment!=null){
            fragmentTransaction.hide(FindFragment);
        }
        if (HelpFragment!=null){
            fragmentTransaction.hide(HelpFragment);
        }
        if (HistoryFragment!=null){
            fragmentTransaction.hide(HistoryFragment);
        }
        if (LoginFragment!=null){
            fragmentTransaction.hide(LoginFragment);
        }
    }

    /*
    * 历史
    * */
    private void switchtoHistory() {
        setSelectedFragment(3);
        toolbar_title.setVisibility(View.GONE);
        toolbar.setTitle(R.string.navigation_history);
    }

    /*
    * 发现
    * */
    private void switchtoFind() {
        setSelectedFragment(1);
        toolbar_title.setVisibility(View.GONE);
        toolbar.setTitle(R.string.navigation_find);
    }

    /*
    *首页
    * */
    private void switchtoHome() {
        setSelectedFragment(0);
        toolbar_title.setVisibility(View.VISIBLE);
        toolbar.setTitle("");
    }

    /*
    * 帮助
    * */
    private void switchtoHelp() {
        setSelectedFragment(2);
        toolbar_title.setVisibility(View.GONE);
        toolbar.setTitle(R.string.navigation_help);
    }

    /*
    * s首页
    * */
    private void switchtoHomeBegin() {
        setSelectedFragment(0);
    }

    /*
    * 初始化Toolbar
    * */
    private void initToolBar() {
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (Build.VERSION.SDK_INT<21){
            StatusBarCompat.compat(this,getResources().getColor(R.color.colorPrimaryDark));
        }else{
//            StatusBarCompat.compat(this);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
       switch (item.getItemId())
       {
           case R.id.remind:
               intent=new Intent(MainActivity.this, MyRemindListActivity.class);
               startActivity(intent);
               break;
           case R.id.message:
               switchtoHelp();
               break;
       }

        return super.onOptionsItemSelected(item);
    }

    /*
    *
    * */
    @Override
    public void onClick(View v) {

    }


    /*
    *对他其他activity传回的消息进行接收
    * */
    public void onEventMainThread(MyEventLogin event) {
            String url=event.getUrl();
            String nickname=event.getName();
            if (url!=null){
                HeadImageHelper utils=new HeadImageHelper(this);
                utils.getHead_Img(url,headImage,R.drawable.img_login1,R.drawable.img_login1);
            }
            Log.d("name", event.getName() + "MainActivity");
            if (nickname==null){
                head_name.setText("");
                head_tips.setVisibility(View.VISIBLE);
                if (event.getResId()!=0){
                    headImage.setImageResource(event.getResId());
                    Log.d("id", String.valueOf(event.getResId()));
                }

            }else{
                head_name.setText(nickname);
                head_tips.setVisibility(View.GONE);
            }
            if (event.getIsLogin()){
                Log.d("islogin", String.valueOf(event.getIsLogin()));
//                FragmentTransaction transaction=fragmentManager.beginTransaction();
//                transaction.hide(LoginFragment);
//                HelpFragment=new HelpFragment();
//                transaction.add(R.id.frame_content,HelpFragment);
            }
        }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Snackbar.make(coordinatorLayout,R.string.sure_exit,Snackbar.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (selectedFragment == null || !selectedFragment.onBackPressed()) {
            if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                drawerLayout.closeDrawer(Gravity.LEFT);
            } else {
                doExitApp();
            }
        }

    }
}
