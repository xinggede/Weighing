package com.xing.weight.fragment.main.manage;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.qmuiteam.qmui.arch.effect.MapEffect;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.base.Constants;
import com.xing.weight.bean.CustomerInfo;
import com.xing.weight.bean.GoodsDetail;
import com.xing.weight.fragment.main.manage.mode.ManageContract;
import com.xing.weight.fragment.main.manage.mode.ManagePresenter;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class MyCustomAddFragment extends BaseFragment<ManagePresenter> implements ManageContract.View {


    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.et_company_name)
    EditText etCompanyName;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_company_address)
    EditText etCompanyAddress;
    @BindView(R.id.et_company_describe)
    EditText etCompanyDescribe;

    CustomerInfo customInfo;

    public MyCustomAddFragment(CustomerInfo customInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.DATA,customInfo);
        setArguments(bundle);
    }

    @Override
    protected ManagePresenter onLoadPresenter() {
        return new ManagePresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_customer_add;
    }

    @Override
    protected void initView(View view) {
        customInfo = getArguments().getParcelable(Constants.DATA);
        topbar.addLeftBackImageButton().setOnClickListener(v->popBackStack());
        if(customInfo == null){
            topbar.setTitle("添加客户信息");
        } else {
            topbar.setTitle("编辑客户信息");
            etCompanyName.setText(customInfo.comname);
            etName.setText(customInfo.name);
            etPhone.setText(customInfo.phone);
            etCompanyAddress.setText(customInfo.address);
            etCompanyDescribe.setText(customInfo.remark);
        }
    }

    @OnClick(R.id.bt_save)
    public void onViewClicked() {
        String companyName = etCompanyName.getText().toString();
        if(TextUtils.isEmpty(companyName)){
            showToast(R.string.pls_input_company_name);
            etCompanyName.requestFocus();
            return;
        }
        String name = etName.getText().toString();
        if(TextUtils.isEmpty(name)){
            showToast(R.string.pls_input_company_charge);
            etName.requestFocus();
            return;
        }
        String phone = etPhone.getText().toString();
        if(TextUtils.isEmpty(phone)){
            showToast(R.string.pls_input_company_phone);
            etPhone.requestFocus();
            return;
        }
        String address = etCompanyAddress.getText().toString();
        if(TextUtils.isEmpty(address)){
            showToast(R.string.pls_input_company_address);
            etCompanyAddress.requestFocus();
            return;
        }

        String describe = etCompanyDescribe.getText().toString();
        if(customInfo == null){
            CustomerInfo info = new CustomerInfo();
            info.comname = companyName;
            info.name = name;
            info.phone = phone;
            info.address = address;
            info.remark = describe;
            mPresenter.addCustom(info);
        } else {
            customInfo.comname = companyName;
            customInfo.name = name;
            customInfo.phone = phone;
            customInfo.address = address;
            customInfo.remark = describe;
            mPresenter.updateCustom(customInfo);
        }
    }

    @Override
    public void onHttpResult(boolean success, int code, Object data) {
        Map<String,Object> map = new HashMap<>();
        map.put(getClass().getName(), true);
        notifyEffect(new MapEffect(map));
        popBackStack();
    }
}
