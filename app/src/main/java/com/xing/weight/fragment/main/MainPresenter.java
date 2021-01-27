package com.xing.weight.fragment.main;

import com.xing.weight.base.mvp.BasePresenter;

public class MainPresenter extends BasePresenter<MainContract.View, MainContract.Model> {

    @Override
    protected MainContract.Model createModel() {
        return new MainModel();
    }

    public boolean isCompany(){
        return mModel.isCompany();
    }

    public void checkVersion(long verCode) {
        requestHttp(mModel.getMainApi().version(),
                versionInfo -> {
                    if(versionInfo.versionCode > verCode){
                        getView().onAppUpdate(versionInfo.url);
                    } else {
                        getView().onAppUpdate(null);
                    }
                }, e->{

                },false);
    }
}
