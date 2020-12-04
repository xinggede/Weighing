package com.xing.weight.base.mvp;


public interface IPresenter<V extends IView> {

    void attachView(V view);

    void detachView();

    void startView();

    void stopView();
}
