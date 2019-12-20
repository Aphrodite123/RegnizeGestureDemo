package com.aphrodite.regnizegesturedemo.config;

/**
 * Created by Aphrodite on 2019/12/6.
 */
public class AppConfig {
    /**
     * 权限
     */
    public interface PermissionType {
        int BASE = 0x1110;

        int WRITE_EXTERNAL_PERMISSION = BASE + 1;

        int REQUEST_CALL_PERMISSION = BASE + 2;

        int OVERLAY_PERMISSION = BASE + 3;

        int CAMERA_PERMISSION = BASE + 4;
    }

}
