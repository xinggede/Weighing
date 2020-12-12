package com.xing.weight.fragment.main.my.mode;


import com.xing.weight.base.Constants;
import com.xing.weight.base.mvp.BaseModel;
import com.xing.weight.bean.CompanyInfo;

public class MyModel extends BaseModel implements MyContract.Model {

    @Override
    public int getCompanyId() {
        return prefs.getInt(Constants.COMPANY_ID, 0);
    }

    @Override
    public void saveCompanyInfo(CompanyInfo companyInfo) {
        prefs.saveInt(Constants.COMPANY_ID, companyInfo.id);
        prefs.saveString(Constants.COMPANY_NAME, companyInfo.comname);
        prefs.saveString(Constants.COMPANY_BOSS, companyInfo.boss);
        prefs.saveString(Constants.COMPANY_PHONE, companyInfo.phone);
        prefs.saveString(Constants.COMPANY_CODE, companyInfo.comcode);
        prefs.saveString(Constants.COMPANY_ADDRESS, companyInfo.address);
    }

    @Override
    public String getUserName() {
        return prefs.getString(Constants.USER_NAME);
    }

    @Override
    public String getUserHead() {
        return prefs.getString(Constants.USER_HEAD);
    }
}
