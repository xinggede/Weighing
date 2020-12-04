package com.xing.weight;

import android.os.Bundle;

import com.qmuiteam.qmui.arch.annotation.DefaultFirstFragment;
import com.qmuiteam.qmui.arch.annotation.FirstFragments;
import com.xing.weight.base.BaseActivity;
import com.xing.weight.base.EmptyPresenter;
import com.xing.weight.base.mvp.BaseContract;
import com.xing.weight.fragment.login.LoginFragment;

@FirstFragments(
        value = {
                LoginFragment.class,
        })
@DefaultFirstFragment(LoginFragment.class)
public class LoginActivity extends BaseActivity<EmptyPresenter> implements BaseContract.View {

    @Override
    protected EmptyPresenter onLoadPresenter() {
        return new EmptyPresenter();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {

    }

}
