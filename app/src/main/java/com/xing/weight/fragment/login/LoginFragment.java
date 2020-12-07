package com.xing.weight.fragment.login;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.qmuiteam.qmui.span.QMUITouchableSpan;
import com.qmuiteam.qmui.widget.textview.QMUISpanTouchFixTextView;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.fragment.login.mode.LoginContract;
import com.xing.weight.fragment.login.mode.LoginPresenter;
import com.xing.weight.util.Tools;
import butterknife.BindView;
import butterknife.OnClick;

public class LoginFragment extends BaseFragment<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.et_phone)
    TextInputEditText etPhone;
    @BindView(R.id.layout_phone)
    TextInputLayout layoutPhone;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.layout_pwd)
    TextInputLayout layoutPwd;
    @BindView(R.id.tv_register)
    QMUISpanTouchFixTextView tvRegister;

    @Override
    protected LoginPresenter onLoadPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView(View view) {
        tvRegister.setMovementMethodDefault();
        SpannableString sp = new SpannableString("没有账号？注册");
        sp.setSpan(new QMUITouchableSpan(tvRegister,
                R.attr.app_primary_color,
                R.attr.app_skin_span_pressed_text_color,
                R.attr.app_skin_span_normal_bg_color,
                R.attr.app_skin_span_pressed_bg_color) {

            @Override
            public void onSpanClick(View widget) {
                startFragment(new RegisterFragment());
            }
        }, 5, 7, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvRegister.setText(sp);
    }

    private void login(){
        String phone = etPhone.getText().toString();
        if(TextUtils.isEmpty(phone)){
            etPhone.requestFocus();
            return;
        }
//        if(!Tools.isMobile(phone)){
//            layoutPhone.setError("手机号码不正确");
//            return;
//        }
//        layoutPhone.setErrorEnabled(false);
        String pwd = etPwd.getText().toString();
        if(TextUtils.isEmpty(pwd)){
            etPwd.requestFocus();
            return;
        }
//        layoutPwd.setErrorEnabled(false);
        mPresenter.login(phone, pwd);
    }

    @OnClick({R.id.tv_forget, R.id.bt_login, R.id.iv_wx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_forget:
                startFragment(new ResetPwdFragment());
                break;

            case R.id.bt_login:
                login();
                break;

            case R.id.iv_wx:

                break;
        }
    }

    @Override
    public Object onLastFragmentFinish() {
        return null;
    }
}
