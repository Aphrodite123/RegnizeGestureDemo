package com.aphrodite.regnizegesturedemo.util;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

/**
 * Created by Aphrodite on 2019/12/9.
 */
public class ThumbnailUtils {
    private static final String TAG = ThumbnailUtils.class.getSimpleName();

    public static String extractMetadata(String filePath, int keyCode) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        String result = null;

        try {
            retriever.setDataSource(filePath);
            result = retriever.extractMetadata(keyCode);
        } catch (IllegalArgumentException e) {

        } catch (RuntimeException e) {

        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }

        return result;
    }

    public static Bitmap createVideoThumbnail(String filePath, long frameTime) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime(frameTime);
        } catch (IllegalArgumentException e) {

        } catch (RuntimeException e) {

        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        return bitmap;
    }

}
