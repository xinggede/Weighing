package com.xing.weight.fragment.main;

import com.xing.weight.base.mvp.BasePresenter;

public class MainPresenter extends BasePresenter<MainContract.View, MainContract.Model> {

    @Override
    protected MainContract.Model createModel() {
        return new MainModel();
    }


}
