package com.xing.weight.fragment.main.my.mode;


import com.xing.weight.base.mvp.BaseContract;
import com.xing.weight.bean.CompanyInfo;

public class MyContract {

    public interface View extends BaseContract.View {

        void onHttpResult(boolean success, int code, Object data);

    }

    interface Model extends BaseContract.Model {

        int getCompanyId();

        void saveCompanyInfo(CompanyInfo companyInfo);

        CompanyInfo getCompanyInfo();

        String getUserName();

        String getUserHead();

        String getDueDate();
    }

}
