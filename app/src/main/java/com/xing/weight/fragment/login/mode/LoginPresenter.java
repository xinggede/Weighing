package com.xing.weight.fragment.login.mode;

import com.xing.weight.base.mvp.BasePresenter;
import com.xing.weight.bean.LoginResultInfo;
import com.xing.weight.bean.request.RequestLogin;
import com.xing.weight.server.http.response.ResponseTransformer;
import com.xing.weight.server.http.schedulers.RetryWithDelay;
import com.xing.weight.server.http.schedulers.SchedulerProvider;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import okhttp3.RequestBody;

public class LoginPresenter extends BasePresenter<LoginContract.View, LoginContract.Model> {

    @Override
    protected LoginContract.Model createModel() {
        return new LoginModel();
    }

    public void getRegisterSms(String phone) {
//        String json = "{\"phoneNumber\":\"" + phone + "\"}";
//        RequestBody body = mModel.getRequestBody(json);
//        requestHttp(mModel.getLoginApi().getRegisterSMS(body), s -> {
//            getView().smsGetOk();
//        }, e -> {
//            onComplete(true);
//            onError(e);
//            getView().smsGetFailed();
//        }, true);
    }

    public void login(String phone, String pwd) {
        RequestLogin requestLogin = new RequestLogin();
        requestLogin.username = phone;
        requestLogin.password = pwd;
        getView().showLoading();
        String json = "{\"username\":\"" + phone + "\", \"password\":\""+pwd+"\"}";

        Disposable disposable = mModel.getLoginApi().login(mModel.getRequestBody(json))
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .retryWhen(new RetryWithDelay(2, 3000))
                .flatMap(new Function<LoginResultInfo, ObservableSource<LoginResultInfo>>() {
                    @Override
                    public ObservableSource<LoginResultInfo> apply(LoginResultInfo userInfo) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<LoginResultInfo>() {
                            @Override
                            public void subscribe(ObservableEmitter<LoginResultInfo> emitter) throws Exception {
//                                mModel.saveData(userInfo);
                                emitter.onNext(userInfo);
                                emitter.onComplete();
                            }
                        }).compose(SchedulerProvider.getInstance().applySchedulers());
                    }
                }).subscribe(userInfo -> {
//                    getView().loginSuccess(userInfo);
                }, e -> {
                    onError(e);
                    onComplete(true);
                }, () -> {
                    onComplete(true);
                });
        addDispose(disposable);
    }

}
