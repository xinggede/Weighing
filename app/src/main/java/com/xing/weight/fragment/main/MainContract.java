package com.xing.weight.fragment.main;


import com.xing.weight.base.mvp.BaseContract;

public class MainContract {

    public interface View extends BaseContract.View {

        void onAppUpdate(String url);
    }

    interface Model extends BaseContract.Model {

        boolean isCompany();

    }

}
