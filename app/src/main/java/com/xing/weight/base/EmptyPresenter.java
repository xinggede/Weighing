package com.xing.weight.base;


import com.xing.weight.base.mvp.BaseContract;
import com.xing.weight.base.mvp.BaseModel;
import com.xing.weight.base.mvp.BasePresenter;

public class EmptyPresenter extends BasePresenter<BaseContract.View, BaseContract.Model> {

    @Override
    protected BaseContract.Model createModel() {
        return new BaseModel();
    }
}
