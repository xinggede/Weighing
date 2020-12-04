package com.xing.weight.fragment.main.my;

import android.view.View;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.fragment.main.MainContract;
import com.xing.weight.fragment.main.MainPresenter;
import com.xing.weight.view.CircleImageView;

import butterknife.BindView;
import butterknife.OnClick;

public class MyFragment extends BaseFragment<MainPresenter> implements MainContract.View {


    @BindView(R.id.iv_head)
    CircleImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_days)
    TextView tvDays;
    @BindView(R.id.bt_exit)
    QMUIRoundButton btExit;

    @Override
    protected MainPresenter onLoadPresenter() {
        return new MainPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView(View view) {

    }

    @OnClick({R.id.re_company_info,R.id.re_print_manage, R.id.re_term_of_validity, R.id.re_about, R.id.bt_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.re_company_info:
                startFragment(new CompanyInfoFragment());
                break;
            case R.id.re_print_manage:
                startFragment(new PrintManageFragment());
                break;
            case R.id.re_term_of_validity:
                break;
            case R.id.re_about:
                break;
            case R.id.bt_exit:
                break;
        }
    }
}
