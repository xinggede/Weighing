package com.xing.weight.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.AttributeSet;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import androidx.annotation.NonNull;

/***
 * 延迟的button，默认延迟120秒，用于获取验证码按钮上提示
 *
 */
public class DelayButton extends QMUIRoundButton {
    public static final int MSG_GO = 0X0001;
    /**
     * 设置的冷却时长
     */
    private int setDuration = 120;
    /**
     * 当前剩余冷却时长
     */
    private int remainTime;
    private String text;
    private WakeLock wakeLock;
    private Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == MSG_GO) {
                if (remainTime > 0) {
                    setText(String.valueOf(remainTime));
                    remainTime--;
                    mHandler.sendEmptyMessageDelayed(MSG_GO, 1000);
                } else {
                    reset();
                }
                return true;
            }
            return false;
        }
    });


    public DelayButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DelayButton(Context context) {
        super(context);
        init(context);
    }

    public DelayButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        PowerManager pw = (PowerManager) context
                .getSystemService(Context.POWER_SERVICE);
        wakeLock = pw.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "weight:captcha");
        text = getText().toString();
        reset();
    }

    /**
     * 重置状态
     */
    public void reset() {
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
        mHandler.removeMessages(MSG_GO);
        mHandler.removeCallbacksAndMessages(null);

        remainTime = setDuration;
        setText(text);
        setEnabled(true);
    }

    public void start() {
        wakeLock.acquire(60 * 1000);
        setEnabled(false);
        mHandler.sendEmptyMessage(MSG_GO);
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (remainTime < setDuration) {
            return;
        }
        super.setEnabled(enabled);
    }

    /**
     * 获取按钮点击的冷却时长
     */
    public int getDuration() {
        return remainTime;
    }

    /**
     * 设置按钮冷却时间, 默认冷却时间180秒 , 需要在点击之前设置时长。
     *
     * @param duration 冷却时长，单位毫秒
     */
    public void setDuration(int duration) {
        if (this.remainTime == setDuration){
            this.remainTime = duration;
        }
        this.setDuration = duration;
    }

    @Override
    protected void onDetachedFromWindow() {
        mHandler.removeCallbacksAndMessages(null);
        remainTime = setDuration;
        super.onDetachedFromWindow();
    }

}
