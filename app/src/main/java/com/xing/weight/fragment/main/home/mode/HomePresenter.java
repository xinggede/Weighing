package com.xing.weight.fragment.main.home.mode;

import com.xing.weight.base.mvp.BasePresenter;

public class HomePresenter extends BasePresenter<HomeContract.View, HomeContract.Model> {

    @Override
    protected HomeContract.Model createModel() {
        return new HomeModel();
    }


}
