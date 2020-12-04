package com.xing.weight.base.mvp;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;

public interface IView {

    void showLoading();

    /**
     * 显示加载
     */
    default void showLoading(String msg) {

    }

    /**
     * 隐藏加载
     */
    default void hideLoading() {

    }

    /**
     * 显示信息
     *
     * @param message 消息内容, 不能为 {@code null}
     */
    void showMessage(@NonNull String message);

    /**
     * 显示信息
     *
     * @param message 消息内容, 不能为 {@code null}
     */
    void showToast(@NonNull String message);

    void showToast(int id);

    void onTokenError();

    /**
     * 跳转 {@link Activity}
     *
     * @param intent {@code intent} 不能为 {@code null}
     */
    default void launchActivity(@NonNull Intent intent) {
    }

    /**
     * 杀死自己
     */
    void closeActivity();
}
