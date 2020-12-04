package com.xing.weight.fragment.main.my;

import android.view.View;
import android.widget.EditText;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.fragment.main.MainContract;
import com.xing.weight.fragment.main.MainPresenter;
import butterknife.BindView;
import butterknife.OnClick;

public class CompanyInfoFragment extends BaseFragment<MainPresenter> implements MainContract.View {


    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.et_company_name)
    EditText etCompanyName;
    @BindView(R.id.et_company_charge)
    EditText etCompanyCharge;
    @BindView(R.id.et_company_phone)
    EditText etCompanyPhone;
    @BindView(R.id.et_company_address)
    EditText etCompanyAddress;

    @Override
    protected MainPresenter onLoadPresenter() {
        return new MainPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_company_info;
    }

    @Override
    protected void initView(View view) {
        topbar.addLeftBackImageButton().setOnClickListener(v->popBackStack());
        topbar.setTitle(R.string.company_info);
    }

    @OnClick(R.id.bt_save)
    public void onViewClicked() {

    }
}
