package com.newthread.medicinebox.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.newthread.medicinebox.R;
import com.newthread.medicinebox.utils.ApiUtils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by 张浩 on 2016/3/29.
 */
public class ImageDisplayActivity extends AppCompatActivity {
    @Bind(R.id.images_viewPage)
    ViewPager viewPager;

    private ArrayList<String> urlList;
    private int ListPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagedisplay);
        ButterKnife.bind(this);
        getData();
        setContentView(viewPager);
        viewPager.setAdapter(new ImagePagerAdapter());
    }

    private void getData() {
        Intent intent=getIntent();
        urlList=intent.getStringArrayListExtra("url");
        ListPosition=intent.getIntExtra("position",0);
    }

    class ImagePagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return urlList.size();
        }

        @Override
        public View instantiateItem(ViewGroup viewGroup,int position){
            List<PhotoView> photoViewList=new ArrayList<>();
            List<String> TestUrlList=new ArrayList<>();
            for(String url:urlList){
                PhotoView photoView=new PhotoView(viewGroup.getContext());
                Log.d("url",url);
                TestUrlList.add(url);
                PicassoSetUpImage(photoView, url);
                viewGroup.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                photoViewList.add(photoView);
            }
            Log.d("testurl", TestUrlList.get(position));
            return photoViewList.get(position);
//            PhotoView photoView=new PhotoView(viewGroup.getContext());
//            PicassoSetUpImage(photoView,urlList.get(position));
//            viewGroup.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            return viewGroup.get(ListPosition);

        }

        private void PicassoSetUpImage(PhotoView photoView,String url) {
            Picasso.with(viewPager.getContext())
                    .load(ApiUtils.getPostImages+url)
                    .into(photoView);
        }

        @Override
        public void destroyItem(ViewGroup viewGroup,int position,Object object){
            viewGroup.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
