package com.xing.weight.fragment.main.manage;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.qmuiteam.qmui.layout.QMUILinearLayout;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.base.Constants;
import com.xing.weight.bean.CustomInfo;
import com.xing.weight.fragment.main.manage.mode.ManageContract;
import com.xing.weight.fragment.main.manage.mode.ManagePresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class MyGoodsAddFragment extends BaseFragment<ManagePresenter> implements ManageContract.View {


    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.et_goods_name)
    EditText etGoodsName;
    @BindView(R.id.et_goods_model)
    EditText etGoodsModel;
    @BindView(R.id.et_goods_specs)
    EditText etGoodsSpecs;
    @BindView(R.id.et_goods_price)
    EditText etGoodsPrice;
    @BindView(R.id.et_goods_address)
    EditText etGoodsAddress;
    @BindView(R.id.lin_top)
    QMUILinearLayout linTop;

    public MyGoodsAddFragment(CustomInfo customInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.DATA, customInfo);
        setArguments(bundle);
    }

    @Override
    protected ManagePresenter onLoadPresenter() {
        return new ManagePresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_goods_add;
    }

    @Override
    protected void initView(View view) {
        CustomInfo customInfo = getArguments().getParcelable(Constants.DATA);
        topbar.addLeftBackImageButton().setOnClickListener(v -> popBackStack());
        if (customInfo == null) {
            topbar.setTitle("添加货品信息");
        } else {
            topbar.setTitle("编辑货品信息");
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
