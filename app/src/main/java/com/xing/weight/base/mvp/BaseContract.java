package com.xing.weight.base.mvp;


public interface BaseContract {

    interface View extends IView {

    }

    interface Model extends IModel {

        boolean isFirst();

        void setNotFirst();

        String getLoginPhone();

        void saveLoginPhone(String phone);

        boolean isLogin();

    }

    interface Presenter<T extends View> extends IPresenter<T> {

    }
}
