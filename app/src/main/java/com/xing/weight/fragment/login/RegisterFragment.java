package com.xing.weight.fragment.login;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.qmuiteam.qmui.arch.effect.MapEffect;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.base.Constants;
import com.xing.weight.fragment.login.mode.LoginContract;
import com.xing.weight.fragment.login.mode.LoginPresenter;
import com.xing.weight.util.Tools;
import com.xing.weight.view.CusEditText;
import com.xing.weight.view.DelayButton;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterFragment extends BaseFragment<LoginPresenter> implements LoginContract.View {


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
    @BindView(R.id.checkbox)
    CheckBox checkbox;

    @Override
    protected LoginPresenter onLoadPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    protected void initView(View view) {
        topbar.setTitle(R.string.register);
        topbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
    }

    @OnClick({R.id.bt_get_captcha, R.id.bt_ok})
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
            case R.id.bt_ok:
                register();
                break;
        }
    }

    private void register() {
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
        mPresenter.register(phone, code, pwd);
    }

    @Override
    public void onHttpResult(boolean success, int code, Object o) {
        if (code == 0) {
            if (success) {
                showMessage("注册成功");
                Map<String, Object> map = new HashMap<>();
                map.put(Constants.PHONE_NUMBER, etPhone.getText().toString());
                notifyEffect(new MapEffect(map));
                popBackStack();
            }
        } else if(code == 1){
            if(success){
                showMessage("验证码发送成功，请注意查收");
                btGetCaptcha.setEnabled(false);
                btGetCaptcha.start();
                etCaptcha.requestFocus();
                etCaptcha.setSelection(etCaptcha.length());
            } else {
                btGetCaptcha.setEnabled(true);
                btGetCaptcha.reset();
            }
        }
    }
}
