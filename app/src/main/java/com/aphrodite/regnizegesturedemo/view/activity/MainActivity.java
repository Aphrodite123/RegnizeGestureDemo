package com.aphrodite.regnizegesturedemo.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.aphrodite.framework.model.network.api.RetrofitInitial;
import com.aphrodite.framework.utils.ObjectUtils;
import com.aphrodite.framework.utils.PathUtils;
import com.aphrodite.framework.utils.ToastUtils;
import com.aphrodite.regnizegesturedemo.BuildConfig;
import com.aphrodite.regnizegesturedemo.R;
import com.aphrodite.regnizegesturedemo.application.FrameApplication;
import com.aphrodite.regnizegesturedemo.config.AppConfig;
import com.aphrodite.regnizegesturedemo.model.bean.GestureDetailBean;
import com.aphrodite.regnizegesturedemo.model.bean.GestureTypesBean;
import com.aphrodite.regnizegesturedemo.model.bean.HandBean;
import com.aphrodite.regnizegesturedemo.model.network.api.RequestApi;
import com.aphrodite.regnizegesturedemo.model.network.response.GestureResponse;
import com.aphrodite.regnizegesturedemo.util.ThumbnailUtils;
import com.sh.shvideolibrary.VideoInputActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.image)
    ImageView mImageView;
    @BindView(R.id.identify_result)
    TextView mIdentifyResult;
    @BindArray(R.array.gesture_type)
    String[] mGestureTypes;

    private Unbinder mUnbinder;

    private static final int REQUEST_CODE_FOR_RECORD_VIDEO = 5230;
    private String mVideoPath;

    private RetrofitInitial mRetrofitInit;
    private RequestApi mRequestApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initConfig();
        initData();
    }

    private void initConfig() {
        //绑定Activity(注:必须在setContentView之后)
        mUnbinder = ButterKnife.bind(this);
    }

    private void initData() {
        mRetrofitInit = FrameApplication.getApplication().getRetrofitInit(true, BuildConfig.SERVER_URL, null);
        mRequestApi = mRetrofitInit.getRetrofit().create(RequestApi.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mUnbinder) {
            mUnbinder.unbind();
        }
    }

    @TargetApi(23)
    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(23)
    private void requestCameraPermission() {
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        };
        ActivityCompat.requestPermissions(this, permissions, AppConfig.PermissionType.CAMERA_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case AppConfig.PermissionType.CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    VideoInputActivity.startActivityForResult(MainActivity.this, REQUEST_CODE_FOR_RECORD_VIDEO, VideoInputActivity.Q720);
                    Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtils.showMessage(R.string.permission_denied);
                }
                break;
        }
    }

    /**
     * 录制视频回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_FOR_RECORD_VIDEO && resultCode == RESULT_OK) {
            String path = data.getStringExtra(VideoInputActivity.INTENT_EXTRA_VIDEO_PATH);
            //根据视频地址获取缩略图
            this.mVideoPath = path;
//            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
//            mImageView.setImageBitmap(bitmap);
//            mIdentifyResult.setText("正在识别，请稍后...");
//
//            File file = new File(PathUtils.getExternalFileDir(this) + "/temp.jpg");
//            BufferedOutputStream bos = null;
//            try {
//                bos = new BufferedOutputStream(new FileOutputStream(file));
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//                bos.flush();
//                bos.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            queryByPicture(file);
            FrameThread frameThread = new FrameThread(path);
            frameThread.run();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void queryByPicture(File piture) {
        if (ObjectUtils.isEmpty(piture)) {
            return;
        }

        //1.创建MultipartBody.Builder对象
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        // 上传文件参数
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), piture);
        builder.addFormDataPart("picture", piture.getName(), body);
        List<MultipartBody.Part> parts = builder.build().parts();

        mRequestApi.queryGesture(parts)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GestureResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GestureResponse gestureResponse) {
                        processResult(gestureResponse);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void processResult(GestureResponse gestureResponse) {
        if (ObjectUtils.isEmpty(gestureResponse)) {
            mIdentifyResult.setText("识别失败");
            return;
        }

        List<HandBean> hands = gestureResponse.getHands();
        if (ObjectUtils.isEmpty(hands)) {
            mIdentifyResult.setText("识别失败");
        }

        HandBean handBean = null;
        GestureDetailBean detailBean = null;
        List<GestureTypesBean> percent = new ArrayList<>();
        for (int i = 0; i < hands.size(); i++) {
            handBean = hands.get(i);
            if (ObjectUtils.isEmpty(handBean)) {
                continue;
            }

            detailBean = handBean.getGesture();
            if (ObjectUtils.isEmpty(detailBean)) {
                continue;
            }

            percent.add(new GestureTypesBean(0, detailBean.getBeg()));
            percent.add(new GestureTypesBean(1, detailBean.getBig_v()));
            percent.add(new GestureTypesBean(2, detailBean.getDouble_finger_up()));
            percent.add(new GestureTypesBean(3, detailBean.getFist()));
            percent.add(new GestureTypesBean(4, detailBean.getHand_open()));
            percent.add(new GestureTypesBean(5, detailBean.getHeart_a()));
            percent.add(new GestureTypesBean(6, detailBean.getHeart_b()));
            percent.add(new GestureTypesBean(7, detailBean.getHeart_c()));
            percent.add(new GestureTypesBean(8, detailBean.getHeart_d()));
            percent.add(new GestureTypesBean(9, detailBean.getIndex_finger_up()));
            percent.add(new GestureTypesBean(10, detailBean.getNamaste()));
            percent.add(new GestureTypesBean(11, detailBean.getOk()));
            percent.add(new GestureTypesBean(12, detailBean.getPalm_up()));
            percent.add(new GestureTypesBean(13, detailBean.getPhonecall()));
            percent.add(new GestureTypesBean(14, detailBean.getRock()));
            percent.add(new GestureTypesBean(15, detailBean.getThanks()));
            percent.add(new GestureTypesBean(16, detailBean.getThumb_down()));
            percent.add(new GestureTypesBean(17, detailBean.getThumb_up()));
            percent.add(new GestureTypesBean(18, detailBean.getUnknown()));
            percent.add(new GestureTypesBean(19, detailBean.getVictory()));

            Collections.sort(percent);

            GestureTypesBean bean = percent.get(0);
            if (!ObjectUtils.isEmpty(bean)) {
                mIdentifyResult.setText("识别结果为：" + mGestureTypes[bean.getId()] + ",识别率为：" + bean.getPercent());
            }
        }

    }

    @OnClick(R.id.record_btn)
    public void onRecordEvent() {
        if (hasCameraPermission()) {
//            VideoInputActivity.startActivityForResult(MainActivity.this, REQUEST_CODE_FOR_RECORD_VIDEO, VideoInputActivity.Q720);
            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
            startActivity(intent);
        } else {
            requestCameraPermission();
        }
    }

    private class FrameThread implements Runnable {
        private String filePath;
        private long time;
        private Bitmap bitmap;

        public FrameThread(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public void run() {
            try {
                while (time <= Long.parseLong(ThumbnailUtils.extractMetadata(filePath, MediaMetadataRetriever.METADATA_KEY_DURATION))) {
                    bitmap = ThumbnailUtils.createVideoThumbnail(filePath, time);
                    Message message = Message.obtain();
                    message.obj = bitmap;
                    handler.sendMessage(message);
                    time = time + 1000;
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bitmap bitmap = (Bitmap) msg.obj;

            File file = new File(PathUtils.getExternalFileDir(MainActivity.this) + "/temp.jpg");
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            queryByPicture(file);

        }
    };

}
