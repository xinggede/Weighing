package com.xing.weight.base.mvp;


import com.xing.weight.server.http.api.LoginApi;
import com.xing.weight.server.http.api.MainApi;

import okhttp3.RequestBody;

public interface BaseContract {

    interface View extends IView {

    }

    interface Model extends IModel {

        boolean isFirst();

        void setNotFirst();

        String getLoginPhone();

        void saveLoginPhone(String phone);

        boolean isLogin();

        void exit();

        MainApi getMainApi();

        LoginApi getLoginApi();

        RequestBody getRequestBody(String json);

    }

    interface Presenter<T extends View> extends IPresenter<T> {

    }
}
