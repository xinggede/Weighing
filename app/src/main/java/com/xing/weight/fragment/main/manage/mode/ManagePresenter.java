package com.xing.weight.fragment.main.manage.mode;

import com.xing.weight.base.mvp.BasePresenter;

public class ManagePresenter extends BasePresenter<ManageContract.View, ManageContract.Model> {

    @Override
    protected ManageContract.Model createModel() {
        return new ManageModel();
    }


}
