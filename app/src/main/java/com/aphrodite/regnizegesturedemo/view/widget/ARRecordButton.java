package com.aphrodite.regnizegesturedemo.view.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.aphrodite.framework.utils.UIUtils;
import com.aphrodite.regnizegesturedemo.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhangjingming on 2017/7/12.
 */

public class ARRecordButton extends View {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float angle = 0f;
    private int progressSize;
    private int progressColor;
    private int pressColor;
    private int normalColor;
    private int bgColor;
    private boolean isRecording = false;
    private boolean isPress = false;
    private int minSize;
    private int maxSize;
    private int currentSize;
    private int centerSize;
    private int recordTime = 0;
    private OnARRecordListener listener;
    private Animator animator;
    private Timer timer = null;
    private TimerTask timerTask = null;
    private long lastTouchTime = 0;

    private final static float MAX_RECORD_TIME = 10f * 1000;

    final static int START_RECORD = 0;
    final static int STOP_RECORD = 1;
    final static int SHOT = 2;
    final static int RECORDING = 3;

    public interface OnARRecordListener {
        void onShot();

        boolean onRecord();

        boolean onRecordStop();
    }

    public ARRecordButton(Context context) {
        this(context, null);
    }

    public ARRecordButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ARRecordButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        progressSize = UIUtils.dip2px(context, 4);
        centerSize = UIUtils.dip2px(context, 23.5f);
        bgColor = 0xdde0e0e0;
        progressColor = context.getResources().getColor(R.color.theme_color);
        normalColor = context.getResources().getColor(R.color.white);
        pressColor = context.getResources().getColor(R.color.theme_color);
        minSize = UIUtils.dip2px(context, 32.5f);
        maxSize = UIUtils.dip2px(context, 41.5f);
        currentSize = minSize;
    }

    public void setCurrentSize(int currentSize) {
        this.currentSize = currentSize;
        invalidate();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setFocusableInTouchMode(enabled);
        setAlpha(enabled ? 1f : 0.3f);
    }

    public void setListener(OnARRecordListener listener) {
        this.listener = listener;
    }

    public void release() {
        stopTimer();
    }

    private void zoomIn() {
        animator = ObjectAnimator.ofInt(this, "currentSize", minSize, maxSize);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(300);
        animator.setStartDelay(200);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                animator = null;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    private void zoomOut() {
        if (currentSize == minSize)
            return;

        animator = ObjectAnimator.ofInt(this, "currentSize", currentSize, minSize);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(300);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                animator = null;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    private void startTimer() {
        stopTimer();
        recordTime = 0;
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                recordTime += 100;
                recordHandler.removeMessages(RECORDING);
                recordHandler.sendEmptyMessage(RECORDING);
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 100);
    }

    private void stopTimer() {
        recordHandler.removeMessages(RECORDING);
        if (null != timerTask) {
            timerTask.cancel();
            timerTask = null;
        }
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (!isFocusableInTouchMode())
            return true;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (System.currentTimeMillis() - lastTouchTime < 1000)
                    return true;

                recordHandler.removeMessages(STOP_RECORD);
                recordHandler.sendEmptyMessageDelayed(START_RECORD, 800);
                zoomIn();
                isPress = true;
                invalidate();
                return true;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastTouchTime < 1000)
                    return true;

                lastTouchTime = currentTime;
                if (null != animator)
                    animator.cancel();
                zoomOut();
                recordTime = 0;
                angle = 0;
                isPress = false;
                invalidate();
                if (isRecording) {
                    isRecording = false;
                    recordHandler.sendEmptyMessage(STOP_RECORD);
                } else {
                    recordHandler.removeMessages(START_RECORD);
                    recordHandler.removeMessages(SHOT);
                    recordHandler.sendEmptyMessageDelayed(SHOT, 50);
                }
                return true;

            default:
                return super.dispatchTouchEvent(event);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        if (0 == width)
            return;

        int centerPoint = width >> 1;
        drawBg(canvas, centerPoint);
        drawCenter(canvas, centerPoint);
        drawProgress(canvas, centerPoint);
    }

    private void drawBg(Canvas canvas, int centerPoint) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        paint.setColor(bgColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerPoint, centerPoint, currentSize, paint);
    }

    private void drawCenter(Canvas canvas, int centerPoint) {
        if (isPress)
            paint.setColor(pressColor);
        else
            paint.setColor(normalColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerPoint, centerPoint, centerSize, paint);
    }

    private void drawProgress(Canvas canvas, int centerPoint) {
        if (angle <= 0)
            return;

        int radius = centerPoint - (progressSize >> 1);
        int start = centerPoint - radius;
        int end = centerPoint + radius;
        RectF rectF = new RectF(start, start, end, end);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(progressColor);
        paint.setStrokeWidth(progressSize);
        canvas.drawArc(rectF, 270, angle, false, paint);
    }

    private final Handler recordHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (null == listener)
                return;

            switch (msg.what) {
                case START_RECORD:
                    isRecording = true;
                    if (listener.onRecord()) {
                        startTimer();
                    }
                    break;

                case STOP_RECORD:
                    if (listener.onRecordStop())
                        stopTimer();
                    break;

                case SHOT:
                    listener.onShot();
                    break;

                case RECORDING:
                    if (recordTime >= MAX_RECORD_TIME) {
                        if (null != animator)
                            animator.cancel();
                        zoomOut();
                        stopTimer();
                        angle = 0;
                        isPress = false;
                        invalidate();
                        if (null != listener)
                            listener.onRecordStop();
                    } else {
                        angle = recordTime * 360 / MAX_RECORD_TIME;
                        invalidate();
                    }
                    break;

                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };
}
