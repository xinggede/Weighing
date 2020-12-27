package com.xing.weight.fragment.login;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.qmuiteam.qmui.arch.effect.MapEffect;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.fragment.login.mode.LoginContract;
import com.xing.weight.fragment.login.mode.LoginPresenter;
import com.xing.weight.util.Tools;
import com.xing.weight.view.CusEditText;
import com.xing.weight.view.DelayButton;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ResetPwdFragment extends BaseFragment<LoginPresenter> implements LoginContract.View {


    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_captcha)
    EditText etCaptcha;
    @BindView(R.id.bt_get_captcha)
    DelayButton btGetCaptcha;
    @BindView(R.id.et_pwd)
    CusEditText etPwd;

    @Override
    protected LoginPresenter onLoadPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reset_pwd;
    }

    @Override
    protected void initView(View view) {
        topbar.setTitle(R.string.reset_pwd);
        topbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
    }

    @OnClick({R.id.bt_get_captcha, R.id.bt_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_get_captcha:
                String phone = etPhone.getText().toString();
                if (!Tools.isMobile(phone)) {
                    showToast("手机号码不正确");
                    etPhone.requestFocus();
                    etPhone.setSelection(etPhone.length());
                    return;
                }
                mPresenter.getSmsCode(phone);
                break;
            case R.id.bt_confirm:
                reset();
                break;
        }
    }

    private void reset() {
        String phone = etPhone.getText().toString();
        if (!Tools.isMobile(phone)) {
            showToast("手机号码不正确");
            etPhone.requestFocus();
            etPhone.setSelection(etPhone.length());
            return;
        }
        String code = etCaptcha.getText().toString();
        if (Tools.isMobile(code)) {
            showToast(R.string.pls_input_captcha);
            etCaptcha.requestFocus();
            return;
        }
        String pwd = etPwd.getText().toString();
        if (pwd.length() < 6) {
            showToast("密码长度至少6位");
            etPwd.requestFocus();
            etPwd.setSelection(etPwd.length());
            return;
        }
        mPresenter.resetPwd(phone, code, pwd);
    }

    @Override
    public void onHttpResult(boolean success, int code, Object o) {
        if (code == 0) {
            if (success) {
                Map<String, Object> map = new HashMap<>();
                map.put(getClass().getName(), etPhone.getText().toString());
                notifyEffect(new MapEffect(map));
                popBackStack();
            }
        } else if(code == 1){
            if(success){
                btGetCaptcha.setEnabled(false);
                btGetCaptcha.start();
            } else {
                btGetCaptcha.setEnabled(true);
                btGetCaptcha.reset();
            }
        }
    }
}
