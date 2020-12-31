package com.xing.weight.fragment.main.my.mode;


import com.xing.weight.base.Constants;
import com.xing.weight.base.mvp.BaseModel;
import com.xing.weight.bean.CompanyInfo;

public class MyModel extends BaseModel implements MyContract.Model {

    @Override
    public int getCompanyId() {
        return prefs.getInt(Constants.COMPANY_ID, -1);
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
    public CompanyInfo getCompanyInfo() {
        CompanyInfo companyInfo = new CompanyInfo();
        companyInfo.id = prefs.getInt(Constants.COMPANY_ID, -1);
        companyInfo.comcode = prefs.getString(Constants.COMPANY_CODE);
        companyInfo.comname = prefs.getString(Constants.COMPANY_NAME);
        companyInfo.boss = prefs.getString(Constants.COMPANY_BOSS);
        companyInfo.phone = prefs.getString(Constants.COMPANY_PHONE);
        companyInfo.address = prefs.getString(Constants.COMPANY_ADDRESS);
        return companyInfo;
    }

    @Override
    public String getUserName() {
        return prefs.getString(Constants.USER_NAME);
    }

    @Override
    public String getUserHead() {
        return prefs.getString(Constants.USER_HEAD);
    }

    @Override
    public String getDueDate() {
        return prefs.getString(Constants.DUE_DATE);
    }
}
