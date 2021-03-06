package com.xing.weight;

import android.app.Application;

import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;
import com.xing.weight.base.CrashHandler;
import com.xing.weight.util.Tools;

import androidx.appcompat.app.AppCompatDelegate;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;


public class CPApp extends Application {

    private static CPApp _instance;

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        CrashHandler.getInstance().init(this);
        setRxJavaErrorHandler();
        QMUISwipeBackActivityManager.init(this);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //关闭暗黑模式
    }

    public static CPApp getInstance() {
        return _instance;
    }

    private void setRxJavaErrorHandler() {
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Tools.loge("rxJavaErrorHandler: " + throwable.getMessage());
            }
        });
    }

}
