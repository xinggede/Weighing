package com.yingmei.scancode.core;

import android.os.Handler;
import android.os.Message;

import com.yingmei.scancode.zxing.QRCodeDecoder;

class DecodeImageRunnable implements Runnable {

    private Handler handler;
    private String filePath;

    public DecodeImageRunnable(String filePath, Handler handler) {
        this.filePath = filePath;
        this.handler = handler;
    }


    @Override
    public void run() {
        String result = QRCodeDecoder.syncDecodeQRCode(filePath);
        handler.sendMessageDelayed(Message.obtain(handler, 3, result), 1000);
    }

}
