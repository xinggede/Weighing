package com.xing.weight.fragment.login.mode;


import com.xing.weight.base.Constants;
import com.xing.weight.base.mvp.BaseModel;
import com.xing.weight.bean.LoginResultInfo;
import com.xing.weight.server.http.RetrofitManager;

public class LoginModel extends BaseModel implements LoginContract.Model {

    @Override
    public void saveData(LoginResultInfo resultInfo) {
        prefs.saveString(Constants.TOKEN, resultInfo.getToken());
        RetrofitManager.resetToken();
        LoginResultInfo.BdUserBean bean = resultInfo.getBdUser();
        if(bean != null){
            prefs.saveString(Constants.PHONE_NUMBER, bean.getPhoneno());
            prefs.saveString(Constants.USER_NAME, bean.getUsername());
            prefs.saveString(Constants.USER_HEAD, bean.getImgurl());
            prefs.saveString(Constants.COMPANY_NAME, bean.getComname());
            prefs.saveString(Constants.DUE_DATE, bean.getDuedate());
            prefs.saveInt(Constants.COMPANY_ID, bean.getComid());

        }
    }

}
