package com.xing.weight.fragment.main.manage.mode;


import com.xing.weight.base.mvp.BaseContract;

public class ManageContract {

    public interface View extends BaseContract.View {

        void onHttpResult(boolean success, int code, Object data);

    }

    interface Model extends BaseContract.Model {


    }

}
