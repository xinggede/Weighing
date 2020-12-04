package com.xing.weight.fragment.main.manage;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.base.Constants;
import com.xing.weight.bean.CustomInfo;
import com.xing.weight.fragment.main.manage.mode.ManageContract;
import com.xing.weight.fragment.main.manage.mode.ManagePresenter;

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

    public MyCustomAddFragment(CustomInfo customInfo) {
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
        CustomInfo customInfo = getArguments().getParcelable(Constants.DATA);
        topbar.addLeftBackImageButton().setOnClickListener(v->popBackStack());
        if(customInfo == null){
            topbar.setTitle("添加客户信息");
        } else {
            topbar.setTitle("编辑客户信息");
        }
    }

    @OnClick(R.id.bt_save)
    public void onViewClicked() {
        CustomInfo customInfo = new CustomInfo();
        customInfo.name = "哈哈哈哈哈哈哈哈哈";
        notifyEffect(customInfo);
        popBackStack();
    }
}
