package com.newthread.medicinebox.ui.codescan;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import com.dtr.zbar.build.ZBarDecoder;
import com.newthread.medicinebox.R;
import com.newthread.medicinebox.theme.StatusBarCompat;
import com.newthread.medicinebox.utils.ConsUtils;
/**
 * Created by 张浩 on 2016/1/30.
 */import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class CaptureActivity extends AppCompatActivity {
    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;
    private CameraManager mCameraManager;
    private FrameLayout scanPreview;
    private RelativeLayout scanContainer;
    private RelativeLayout scanCropView;
    private ImageView scanLine;
    private FloatingActionButton button_photo,button_light;
    private Rect mCropRect = null;
    private boolean barcodeScanned = false;
    private boolean previewing = true;
    private boolean LIGHT=false;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        findViewById();
        addEvents();
        initViews();
    }
    private void findViewById() {
        scanPreview = (FrameLayout) findViewById(R.id.capture_preview);
        scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
        scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);
        button_light= (FloatingActionButton) findViewById(R.id.open_light);
        button_photo= (FloatingActionButton) findViewById(R.id.open_picture);
    }

    private void addEvents() {
        button_photo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*"); // 设置文件类型
                startActivityForResult(intent, ConsUtils.PICK_PICTURE);
            }
        });
        button_light.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!LIGHT) {
                    TurnOnLight();
                } else {
                    TurnOffLight();
                }

            }
        });
    }

    /*
    * 的到数据
    * */
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }



    /*
        * 关闭闪光灯
        * */
    private void TurnOffLight() {
        Camera.Parameters parameter = mCamera.getParameters();
        parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(parameter);
        LIGHT=false;
        button_light.setImageResource(R.drawable.img_turnon_light);
    }

    /*
    * 打开闪关灯
    * */
    private void TurnOnLight() {
        mCamera.startPreview();
        Camera.Parameters parameter = mCamera.getParameters();
        parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        mCamera.setParameters(parameter);
        LIGHT=true;
        button_light.setImageResource(R.drawable.img_turnoff_light);
    }

    private void initViews() {
        StatusBarCompat.compat(CaptureActivity.this,getResources().getColor(R.color.colorPrimaryDark));
        autoFocusHandler = new Handler();
        mCameraManager = new CameraManager(this);
        try {
            mCameraManager.openDriver();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mCamera = mCameraManager.getCamera();
        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        scanPreview.addView(mPreview);

        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.85f);
        animation.setDuration(3000);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.REVERSE);
        scanLine.startAnimation(animation);
    }

    public void onPause() {
        super.onPause();
        //releaseCamera();
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    PreviewCallback previewCb = new PreviewCallback() {
         public void onPreviewFrame(byte[] data, Camera camera) {
            Size size = camera.getParameters().getPreviewSize();
             // 这里需要将获取的data翻转一下，因为相机默认拿的的横屏的数据
             byte[] rotatedData = new byte[data.length];
             for (int y = 0; y < size.height; y++) {
                 for (int x = 0; x < size.width; x++)
                     rotatedData[x * size.height + size.height - y - 1] = data[x + y * size.width];
             }
             // 宽高也要调整
             int tmp = size.width;
             size.width = size.height;
             size.height = tmp;
             initCrop();
             ZBarDecoder zBarDecoder = new ZBarDecoder();
             String result = zBarDecoder.decodeCrop(rotatedData, size.width, size.height, mCropRect.left, mCropRect.top, mCropRect.width(), mCropRect.height());
             if (!TextUtils.isEmpty(result)) {
                 previewing = false;
                 mCamera.setPreviewCallback(null);
                 mCamera.stopPreview();
                 gotoResult(result);
                 //barcodeScanned = true;
             }
        }
    };

    /*
    * 开始扫描
    * */



    /*
    * 扫描结果
    * */
    private void gotoResult(String result) {
        Intent intent=new Intent(CaptureActivity.this,ScanResultActivity.class);
        intent.putExtra("result",result);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        releaseCamera();
    }

    // Mimic continuous auto-focusing
    AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = mCameraManager.getCameraResolution().y;
        int cameraHeight = mCameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;
        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
