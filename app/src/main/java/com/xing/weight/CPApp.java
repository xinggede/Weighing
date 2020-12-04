package com.xing.weight;

import android.app.Application;

import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;
import com.xing.weight.base.CrashHandler;


public class CPApp extends Application {

    private static CPApp _instance;

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        CrashHandler.getInstance().init(this);
        setRxJavaErrorHandler();
        QMUISwipeBackActivityManager.init(this);
    }

    public static CPApp getInstance() {
        return _instance;
    }

    private void setRxJavaErrorHandler() {
       /* RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Tools.loge("rxJavaErrorHandler: " + throwable.getMessage());
            }
        });*/
    }

}
