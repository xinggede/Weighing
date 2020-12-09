package com.xing.weight.fragment.main.my;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.bean.CompanyInfo;
import com.xing.weight.fragment.main.MainContract;
import com.xing.weight.fragment.main.MainPresenter;
import com.xing.weight.fragment.main.my.mode.MyContract;
import com.xing.weight.fragment.main.my.mode.MyPresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class CompanyInfoFragment extends BaseFragment<MyPresenter> implements MyContract.View {


    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.et_company_name)
    EditText etCompanyName;
    @BindView(R.id.et_company_code)
    EditText etCompanyCode;
    @BindView(R.id.et_company_charge)
    EditText etCompanyCharge;
    @BindView(R.id.et_company_phone)
    EditText etCompanyPhone;
    @BindView(R.id.et_company_address)
    EditText etCompanyAddress;

    @Override
    protected MyPresenter onLoadPresenter() {
        return new MyPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_company_info;
    }

    @Override
    protected void initView(View view) {
        topbar.addLeftBackImageButton().setOnClickListener(v->popBackStack());
        topbar.setTitle(R.string.company_info);
        mPresenter.getCompanyInfo();
    }

    @OnClick(R.id.bt_save)
    public void onViewClicked() {
        String name = etCompanyName.getText().toString();
        if(TextUtils.isEmpty(name)){
            showToast(R.string.pls_input_company_name);
            etCompanyName.requestFocus();
            return;
        }
        String code = etCompanyCode.getText().toString();
        if(TextUtils.isEmpty(code)){
            showToast(R.string.pls_input_company_code);
            etCompanyCode.requestFocus();
            return;
        }
        String charge = etCompanyCharge.getText().toString();
        if(TextUtils.isEmpty(charge)){
            showToast(R.string.pls_input_company_charge);
            etCompanyCharge.requestFocus();
            return;
        }

        String phone = etCompanyPhone.getText().toString();
        if(TextUtils.isEmpty(phone)){
            showToast(R.string.pls_input_company_phone);
            etCompanyPhone.requestFocus();
            return;
        }

        String address = etCompanyAddress.getText().toString();
        if(TextUtils.isEmpty(address)){
            showToast(R.string.pls_input_company_address);
            etCompanyAddress.requestFocus();
            return;
        }
        CompanyInfo companyInfo = new CompanyInfo();
        companyInfo.comcode = code;
        companyInfo.comname = name;
        companyInfo.boss = charge;
        companyInfo.phone = phone;
        companyInfo.address = address;
        mPresenter.addCompanyInfo(companyInfo);
    }

    @Override
    public void onHttpResult(boolean success, int code, Object data) {
        if(success){
            if(code == 0){
                CompanyInfo companyInfo = (CompanyInfo) data;
                etCompanyName.setText(companyInfo.comname);
                etCompanyCode.setText(companyInfo.comcode);
                etCompanyCharge.setText(companyInfo.boss);
                etCompanyPhone.setText(companyInfo.phone);
                etCompanyAddress.setText(companyInfo.address);
            }
        }
    }
}
