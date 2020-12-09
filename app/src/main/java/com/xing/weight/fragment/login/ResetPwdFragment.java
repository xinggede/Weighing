package com.xing.weight.fragment.login;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.fragment.login.mode.LoginContract;
import com.xing.weight.fragment.login.mode.LoginPresenter;
import com.xing.weight.view.CusEditText;

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
    QMUIRoundButton btGetCaptcha;
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

    @OnClick({R.id.bt_get_captcha, R.id.bt_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_get_captcha:
                break;
            case R.id.bt_register:
                break;
        }
    }

    @Override
    public void onHttpResult(int code, Object o) {

    }
}
