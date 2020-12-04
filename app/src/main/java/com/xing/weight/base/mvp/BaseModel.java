package com.xing.weight.base.mvp;


import android.text.TextUtils;

import com.xing.weight.base.Constants;
import com.xing.weight.base.Prefs;

public class BaseModel implements BaseContract.Model {

    protected Prefs prefs;
    private final String IS_FIRST = "isFirst";

    public BaseModel() {
        prefs = Prefs.getInstance();
    }

    @Override
    public boolean isFirst() {
        return prefs.getBoolean(IS_FIRST, true);
    }

    @Override
    public void setNotFirst() {
        prefs.saveBoolean(IS_FIRST, false);
    }

    @Override
    public String getLoginPhone() {
        return prefs.getString(Constants.PHONE_NUMBER);
    }

    @Override
    public void saveLoginPhone(String phone) {
        prefs.saveString(Constants.PHONE_NUMBER, phone);
    }

    @Override
    public boolean isLogin() {
        return !TextUtils.isEmpty(prefs.getString(Constants.ACCESS_TOKEN));
    }
}
