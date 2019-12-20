package com.aphrodite.regnizegesturedemo.view.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.aphrodite.framework.model.network.api.RetrofitInitial;
import com.aphrodite.framework.utils.ObjectUtils;
import com.aphrodite.framework.utils.PathUtils;
import com.aphrodite.framework.utils.ToastUtils;
import com.aphrodite.regnizegesturedemo.BuildConfig;
import com.aphrodite.regnizegesturedemo.R;
import com.aphrodite.regnizegesturedemo.application.FrameApplication;
import com.aphrodite.regnizegesturedemo.model.bean.GestureDetailBean;
import com.aphrodite.regnizegesturedemo.model.bean.GestureTypesBean;
import com.aphrodite.regnizegesturedemo.model.bean.HandBean;
import com.aphrodite.regnizegesturedemo.model.network.api.RequestApi;
import com.aphrodite.regnizegesturedemo.model.network.response.GestureResponse;
import com.aphrodite.regnizegesturedemo.util.CameraUtils;
import com.aphrodite.regnizegesturedemo.view.widget.ARRecordButton;
import com.aphrodite.regnizegesturedemo.view.widget.CustomSurfaceView;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
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
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Aphrodite on 2019/12/11.
 */
public class CameraActivity extends AppCompatActivity {
    private static final String TAG = CameraActivity.class.getSimpleName();

    @BindView(R.id.camera_root)
    FrameLayout mCameraRoot;
    @BindArray(R.array.gesture_type)
    String[] mGestureTypes;

    private ARRecordButton mTakePhotoBtn;

    private CustomSurfaceView mSurfaceView;
    private int mCameraId = -1;

    private Unbinder mUnbinder;

    private RetrofitInitial mRetrofitInit;
    private RequestApi mRequestApi;

    private static final int TIME_INTERVAL = 5 * 1000;
    private long mPreviousTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initConfig();
        initView();
        initData();
    }

    private void initConfig() {
        //绑定Activity(注:必须在setContentView之后)
        mUnbinder = ButterKnife.bind(this);
    }

    private void initView() {
        mCameraId = CameraUtils.findBackFacingCamera(this);
        mSurfaceView = new CustomSurfaceView(this, mCameraId, new CustomPreviewCallback(), new CustomPictureCallback());
        mCameraRoot.addView(mSurfaceView);

        View view = LayoutInflater.from(this).inflate(R.layout.layout_camera_control, null);
        mTakePhotoBtn = view.findViewById(R.id.take_photo_btn);
        mTakePhotoBtn.setEnabled(true);
        mTakePhotoBtn.setListener(arRecordListener);
        mCameraRoot.addView(view);
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

    private void processFrame(byte[] data, Camera camera) {
        if (null == data || data.length <= 0 || null == camera) {
            return;
        }

        Camera.Size size = camera.getParameters().getPreviewSize();
        if (null == size) {
            return;
        }

        ByteArrayOutputStream stream = null;
        try {
            YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
            if (null == image) {
                return;
            }

            stream = new ByteArrayOutputStream();
            image.compressToJpeg(new Rect(0, 0, size.width, size.height), 100, stream);
            Bitmap bitmap = rotateMyBitmap(BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size()));
            queryByPicture(bitmapToFile(bitmap));
        } catch (Exception e) {

        } finally {
            if (null != stream) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 旋转
     *
     * @param bmp
     */
    private Bitmap rotateMyBitmap(Bitmap bmp) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
    }

    private File bitmapToFile(Bitmap bitmap) {
        if (null == bitmap) {
            return null;
        }

        File file = new File(PathUtils.getExternalFileDir(this) + "/temp.jpg");
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
        return file;
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
                        ToastUtils.showMessage("识别失败");
                        mSurfaceView.startPreview();
                    }

                    @Override
                    public void onComplete() {
                        mSurfaceView.startPreview();
                    }
                });
    }

    private void processResult(GestureResponse gestureResponse) {
        if (ObjectUtils.isEmpty(gestureResponse)) {
            return;
        }

        List<HandBean> hands = gestureResponse.getHands();
        if (ObjectUtils.isEmpty(hands)) {
            return;
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
                ToastUtils.showMessage("识别结果为：" + mGestureTypes[bean.getId()] + ",识别率为：" + bean.getPercent());
            }
        }

    }

    private void takePhoto() {
        mSurfaceView.takePhoto();
    }

    private void processPhoto(byte[] data, Camera camera) {
        if (null == data || data.length <= 0 || null == camera) {
            return;
        }

        Bitmap bitmap = null;
        try {
            bitmap = rotateMyBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
            queryByPicture(bitmapToFile(bitmap));
        } catch (OutOfMemoryError e) {
            return;
        }
        data = null;
    }

    private class CustomPreviewCallback implements Camera.PreviewCallback {

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            //20191220 取消一直识别模式
//            if (System.currentTimeMillis() - mPreviousTime >= TIME_INTERVAL) {
//                processFrame(data, camera);
//
//                mPreviousTime = System.currentTimeMillis();
//            }
        }
    }

    private ARRecordButton.OnARRecordListener arRecordListener = new ARRecordButton.OnARRecordListener() {
        @Override
        public void onShot() {
            takePhoto();
        }

        @Override
        public boolean onRecord() {
            return false;
        }

        @Override
        public boolean onRecordStop() {
            return true;
        }
    };

    private class CustomPictureCallback implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            processPhoto(data, camera);
        }
    }

}
