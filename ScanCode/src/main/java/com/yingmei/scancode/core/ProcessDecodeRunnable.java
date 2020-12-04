package com.yingmei.scancode.core;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.lang.ref.WeakReference;

class ProcessDecodeRunnable implements Runnable {

    private byte[] mData;
    private boolean mIsPortrait;
    private WeakReference<QRCodeView> mQRCodeViewRef;
    private int width, height;
    private Handler handler;
    private boolean isRetry;

    ProcessDecodeRunnable(int width, int height, byte[] data, QRCodeView qrCodeView, boolean isPortrait, Handler handler, boolean isRetry) {
        mData = data;
        mQRCodeViewRef = new WeakReference<>(qrCodeView);
        mIsPortrait = isPortrait;
        this.width = width;
        this.height = height;
        this.handler = handler;
        this.isRetry = isRetry;
    }

    private ScanResult processData(QRCodeView qrCodeView) {
        if (mData == null) {
            return null;
        }
        byte[] data;
        try {
            long stime = System.currentTimeMillis();
            if (mIsPortrait) {
                data = new byte[mData.length];
                for (int y = 0; y < height; y++) {
                    int yw = y * width;
                    int hym = height - y - 1;
                    for (int x = 0; x < width; x++) {
                        data[x * height + hym] = mData[x + yw];
                    }
                }
                int tmp = width;
                width = height;
                height = tmp;
            } else {
                data = mData;
            }
            handler.sendEmptyMessage(1);
            loge("processData:" + (System.currentTimeMillis() - stime));
            return qrCodeView.processData(data, width, height, isRetry);
        } catch (Exception e1) {
            e1.printStackTrace();
            loge("识别失败");
            return null;
        }
    }

    @Override
    public void run() {
        QRCodeView qrCodeView = mQRCodeViewRef.get();
        if (qrCodeView == null) {
            handler.sendEmptyMessage(0);
            return;
        }

        long startTime = System.currentTimeMillis();

        ScanResult scanResult = processData(qrCodeView);
        mData = null;
        mQRCodeViewRef.clear();
        mQRCodeViewRef = null;
        if (BGAQRCodeUtil.isDebug()) {
            long time = System.currentTimeMillis() - startTime;
            if (scanResult != null && !TextUtils.isEmpty(scanResult.result)) {
                loge("识别成功时间为：" + time);
            } else {
                loge("识别失败时间为：" + time);
            }
        }
        if (scanResult == null) {
            handler.sendEmptyMessage(0);
        } else {
            Message.obtain(handler, 2, scanResult).sendToTarget();
        }
    }

    public static void loge(String msg) {
        Log.e("xing", Thread.currentThread().getName() + ":" + msg);
    }
}
