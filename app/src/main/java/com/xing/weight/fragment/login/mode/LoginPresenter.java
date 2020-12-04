package com.xing.weight.fragment.login.mode;

import com.xing.weight.base.mvp.BasePresenter;

public class LoginPresenter extends BasePresenter<LoginContract.View, LoginContract.Model> {

    @Override
    protected LoginContract.Model createModel() {
        return new LoginModel();
    }


}
