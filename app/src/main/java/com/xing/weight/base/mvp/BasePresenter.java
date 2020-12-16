package com.xing.weight.base.mvp;

import com.xing.weight.server.http.exception.ApiException;
import com.xing.weight.server.http.exception.CustomException;
import com.xing.weight.server.http.response.BasicResponse;
import com.xing.weight.server.http.response.ResponseTransformer;
import com.xing.weight.server.http.schedulers.RetryWithDelay;
import com.xing.weight.server.http.schedulers.SchedulerProvider;
import com.xing.weight.util.Tools;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


public abstract class BasePresenter<V extends BaseContract.View, M extends BaseContract.Model> implements BaseContract.Presenter<V> {

    protected CompositeDisposable mCompositeDisposable;
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
        unDispose();
        if (mView != null) {
            mView.clear();
            mView = null;
        }
        this.mModel = null;
        this.mCompositeDisposable = null;
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

    public void setTimer(int time, Consumer<? super Long> onNext) {
        Disposable disposable = Observable.timer(time, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(onNext);
        addDispose(disposable);
    }

    public void addDispose(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);//将所有 Disposable 放入容器集中处理
    }



    /**
     * 停止集合中正在执行的 RxJava 任务
     */
    public void unDispose() {
        Tools.loge("unDispose: " + mCompositeDisposable);
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();//保证 Activity 结束时取消所有正在执行的订阅
        }
    }

    protected ApiException onError(Throwable e) {
        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            Tools.loge("onError: " + apiException.getDisplayMessage());
            if (getView() != null) {
                if (apiException.getCode() == CustomException.TOKEN_ERROR) {
                    mModel.exit();
                    getView().onTokenError();
                } else {
                    getView().showToast(apiException.getDisplayMessage());
                }
            }
            return apiException;
        } else {
            Tools.loge("onError: " + e.getMessage());
            return new ApiException(CustomException.UNKNOWN, e.getMessage());
        }
    }

    protected void onComplete(boolean isShow) {
        Tools.logd("onComplete: " + isShow);
        if (isShow) {
            if (getView() != null) {
                getView().hideLoading();
            }
        }
    }

    public <T> void requestHttp(Observable<BasicResponse<T>> observable, Consumer<? super T> onNext) {
        requestHttp(observable, onNext, true);
    }

    public <T> void requestHttp(Observable<BasicResponse<T>> observable, Consumer<? super T> onNext, boolean show) {
        requestHttp(observable, onNext, error -> {
            onError(error);
            onComplete(show);
        }, () -> {
            onComplete(show);
        }, disposable -> {
            if (show) {
                if (getView() != null) {
                    getView().showLoading();
                }
            }
        });
    }

    public <T> void requestHttp(Observable<BasicResponse<T>> observable, Consumer<? super T> onNext, Consumer<? super Throwable> onError, boolean show) {
        requestHttp(observable, onNext, onError, () -> {
            onComplete(show);
        }, disposable -> {
            if (show) {
                if (getView() != null) {
                    getView().showLoading();
                }
            }
        });
    }

    public <T> void requestHttp(Observable<BasicResponse<T>> observable, Consumer<? super T> onNext, Consumer<? super Throwable> onError,
                                Action onComplete, Consumer<? super Disposable> onSubscribe) {
        Disposable disposable = observable
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .retryWhen(new RetryWithDelay(2, 3000))
                .subscribe(onNext, onError, onComplete, onSubscribe);
        addDispose(disposable);
    }

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
