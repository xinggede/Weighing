package com.xing.weight.fragment.main;


import com.xing.weight.base.Constants;
import com.xing.weight.base.mvp.BaseModel;

public class MainModel extends BaseModel implements MainContract.Model {

    @Override
    public boolean isCompany() {
        return prefs.getInt(Constants.COMPANY_ID, -1) != -1;
    }
}
