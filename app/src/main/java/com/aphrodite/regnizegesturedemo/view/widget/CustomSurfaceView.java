package com.aphrodite.regnizegesturedemo.view.widget;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.aphrodite.regnizegesturedemo.util.CameraUtils;

import java.io.IOException;

/**
 * Created by Aphrodite on 2019/12/11.
 */
public class CustomSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private Context mContext;
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private Camera.PictureCallback mPictureCallback;
    private int mWidth = 1920;
    private int mHeight = 2448;

    public CustomSurfaceView(Context context, int cameraId, Camera.PreviewCallback callback, Camera.PictureCallback pictureCallback) {
        super(context);
        init(context, cameraId, callback, pictureCallback);
    }

    private void init(Context context, int cameraId, Camera.PreviewCallback callback, Camera.PictureCallback pictureCallback) {
        this.mContext = context;
        this.mPictureCallback = pictureCallback;

        mCamera = Camera.open(cameraId);
        mCamera.setPreviewCallback(callback);
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (null == mCamera) {
            return;
        }

        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.setDisplayOrientation(90);

            Camera.Parameters parameters = mCamera.getParameters();
            parameters.set("orientation", "portrait");
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

//            Camera.Size size = CameraUtils.getBestPreviewSize(parameters, mWidth, mHeight);
//            if (null != size) {
//                parameters.setPreviewSize(size.width, size.height);
//            }

            mCamera.setParameters(parameters);

            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

    /**
     * 释放相机
     */
    public void releaseCamera() {
        if (null != mCamera) {
            try {
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);
                mCamera.release();
                mCamera = null;
            } catch (Exception e) {

            }
        }
    }

    public void stopPreview() {
        if (null != mCamera) {
            mCamera.stopPreview();
        }
    }

    public void refreshCamera(int cameraId) {
        stopPreview();

        mCamera = Camera.open(cameraId);
        mCamera.startPreview();
    }

    public void takePhoto() {
        if (null == mCamera) {
            return;
        }

        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        Camera.Size size = CameraUtils.getBestPictureSize(parameters, mWidth, mHeight);
        if (null != size) {
            parameters.setPictureSize(size.width, size.height);
        }
        mCamera.setParameters(parameters);

        mCamera.takePicture(new Camera.ShutterCallback() {
            @Override
            public void onShutter() {
                //按下快门
            }
        }, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

            }
        }, mPictureCallback);
    }

    public void startPreview() {
        if (null == mCamera) {
            return;
        }

        mCamera.startPreview();
    }

    public void setmWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public void setmHeight(int mHeight) {
        this.mHeight = mHeight;
    }
}
