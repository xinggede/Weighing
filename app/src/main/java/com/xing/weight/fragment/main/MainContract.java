package com.xing.weight.fragment.main;


import com.xing.weight.base.mvp.BaseContract;

public class MainContract {

    public interface View extends BaseContract.View {


    }

    interface Model extends BaseContract.Model {

        boolean isCompany();

    }

}
