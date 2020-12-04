package com.xing.weight.base.mvp;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;


public abstract class BasePresenter<V extends BaseContract.View, M extends BaseContract.Model> implements BaseContract.Presenter<V> {

    //    protected CompositeDisposable mCompositeDisposable;
    protected M mModel;
    protected Reference<V> mView;

    public BasePresenter() {
        mModel = createModel();
    }

    protected abstract M createModel();

    @Override
    public void attachView(V view) {
        mView = new WeakReference<>(view);
    }

    @Override
    public void detachView() {
//        unDispose();
        if (mView != null) {
            mView.clear();
            mView = null;
        }
        this.mModel = null;
//        this.mCompositeDisposable = null;
    }

    @Override
    public void startView() {

    }

    @Override
    public void stopView() {

    }

    public V getView() {
        return mView != null ? mView.get() : null;
    }

    /*public void setTimer(int time, Consumer<? super Long> onNext) {
        Disposable disposable = Observable.timer(time, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(onNext);
        addDispose(disposable);
    }

    public void addDispose(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);//将所有 Disposable 放入容器集中处理
    }

    */

    /**
     * 停止集合中正在执行的 RxJava 任务
     *//*
    public void unDispose() {
        Tools.loge("unDispose: " + mCompositeDisposable);
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();//保证 Activity 结束时取消所有正在执行的订阅
        }
    }*/
    public boolean isFirst() {
        return mModel.isFirst();
    }

    public void setNotFirst() {
        mModel.setNotFirst();
    }

    public String getLoginPhone() {
        return mModel.getLoginPhone();
    }

    public void saveLoginPhone(String phone) {
        mModel.saveLoginPhone(phone);
    }

    public boolean isLogin() {
        return mModel.isLogin();
    }

}
