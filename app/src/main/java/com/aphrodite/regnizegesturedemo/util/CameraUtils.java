package com.aphrodite.regnizegesturedemo.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

import java.util.List;

/**
 * Created by Aphrodite on 2019/12/11.
 */
public class CameraUtils {

    /**
     * 检测是否有摄像头
     *
     * @param context
     * @return
     */
    public static boolean hasCamera(Context context) {
        if (null == context) {
            return false;
        }

        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        }

        return false;
    }

    /**
     * 查找前置摄像头
     *
     * @return default:-1
     */
    public static int findFrontFacingCamera(Context context) {
        int cameraId = -1;
        if (null == context) {
            return cameraId;
        }

        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (Camera.CameraInfo.CAMERA_FACING_FRONT == info.facing) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    /**
     * 查找后置摄像头
     *
     * @return default:-1
     */
    public static int findBackFacingCamera(Context context) {
        int cameraId = -1;
        if (null == context) {
            return cameraId;
        }

        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (Camera.CameraInfo.CAMERA_FACING_BACK == info.facing) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    public static Camera.Size getBestPreviewSize(Camera.Parameters parameters, int width, int height) {
        Camera.Size result = null;
        if (null == parameters) {
            return result;
        }

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea > resultArea) {
                        result = size;
                    }
                }
            }
        }

        return result;
    }

    public static Camera.Size getBestPictureSize(Camera.Parameters parameters, int width, int height) {
        Camera.Size result = null;
        if (null == parameters) {
            return result;
        }

        for (Camera.Size size : parameters.getSupportedPictureSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea > resultArea) {
                        result = size;
                    }
                }
            }
        }

        return result;
    }

}
