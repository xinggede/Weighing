package com.xing.weight.fragment.login.mode;


import com.xing.weight.base.mvp.BaseContract;
import com.xing.weight.bean.LoginResultInfo;

public class LoginContract {

    public interface View extends BaseContract.View {

        void onHttpResult(int code, Object o);

    }

    interface Model extends BaseContract.Model {

        void saveData(LoginResultInfo resultInfo);

    }

}
