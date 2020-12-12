package com.xing.weight.base.mvp;


import android.text.TextUtils;

import com.xing.weight.base.Constants;
import com.xing.weight.base.Prefs;
import com.xing.weight.server.http.RetrofitManager;
import com.xing.weight.server.http.api.LoginApi;
import com.xing.weight.server.http.api.MainApi;

import okhttp3.MediaType;
import okhttp3.RequestBody;

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
        return !TextUtils.isEmpty(prefs.getString(Constants.TOKEN));
    }

    @Override
    public void exit() {
        prefs.remove(Constants.TOKEN);

        prefs.remove(Constants.COMPANY_ID);
        prefs.remove(Constants.COMPANY_NAME);
        prefs.remove(Constants.COMPANY_BOSS);
        prefs.remove(Constants.COMPANY_PHONE);
        prefs.remove(Constants.COMPANY_CODE);
        prefs.remove(Constants.COMPANY_ADDRESS);

        RetrofitManager.resetToken();
    }

    @Override
    public MainApi getMainApi() {
        return RetrofitManager.create(MainApi.class);
    }

    @Override
    public LoginApi getLoginApi() {
        return RetrofitManager.create(LoginApi.class);
    }

    @Override
    public RequestBody getRequestBody(String json) {
        return RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
    }
}
