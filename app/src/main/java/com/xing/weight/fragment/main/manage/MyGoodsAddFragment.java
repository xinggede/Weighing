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
import com.xing.weight.bean.GoodsDetail;
import com.xing.weight.fragment.main.manage.mode.ManageContract;
import com.xing.weight.fragment.main.manage.mode.ManagePresenter;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class MyGoodsAddFragment extends BaseFragment<ManagePresenter> implements ManageContract.View {


    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.et_goods_code)
    EditText etGoodsCode;
    @BindView(R.id.et_goods_name)
    EditText etGoodsName;
    @BindView(R.id.et_goods_model)
    EditText etGoodsModel;
    @BindView(R.id.et_goods_specs)
    EditText etGoodsSpecs;
    @BindView(R.id.et_goods_price)
    EditText etGoodsPrice;
    @BindView(R.id.et_goods_describe)
    EditText etGoodsDescribe;

    GoodsDetail goodsDetail;

    public MyGoodsAddFragment(GoodsDetail goodsDetail) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.DATA, goodsDetail);
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
        goodsDetail = getArguments().getParcelable(Constants.DATA);
        topbar.addLeftBackImageButton().setOnClickListener(v -> popBackStack());
        if (goodsDetail == null) {
            topbar.setTitle("添加货品信息");
        } else {
            topbar.setTitle("编辑货品信息");
            etGoodsCode.setText(goodsDetail.code);
            etGoodsName.setText(goodsDetail.name);
            etGoodsModel.setText(goodsDetail.type);
            etGoodsSpecs.setText(goodsDetail.model);
            etGoodsPrice.setText(goodsDetail.pricebuy);
            etGoodsDescribe.setText(goodsDetail.remark);
        }
    }

    @OnClick(R.id.bt_save)
    public void onViewClicked() {
        String code = etGoodsCode.getText().toString();
        if(TextUtils.isEmpty(code)){
            showToast(R.string.pls_input_goods_code);
            etGoodsCode.requestFocus();
            return;
        }
        String name = etGoodsName.getText().toString();
        if(TextUtils.isEmpty(name)){
            showToast(R.string.pls_input_goods_name);
            etGoodsName.requestFocus();
            return;
        }
        String model = etGoodsModel.getText().toString();
        if(TextUtils.isEmpty(model)){
            showToast(R.string.pls_input_goods_model);
            etGoodsModel.requestFocus();
            return;
        }
        String specs = etGoodsSpecs.getText().toString();
        if(TextUtils.isEmpty(specs)){
            showToast(R.string.pls_input_goods_specs);
            etGoodsSpecs.requestFocus();
            return;
        }
        String price = etGoodsPrice.getText().toString();
        if(TextUtils.isEmpty(price)){
            showToast(R.string.pls_input_goods_price);
            etGoodsPrice.requestFocus();
            return;
        }
        String describe = etGoodsDescribe.getText().toString();
        if(goodsDetail == null){
            GoodsDetail detail = new GoodsDetail();
            detail.code = code;
            detail.name = name;
            detail.type = model;
            detail.model = specs;
            detail.pricebuy = price;
            detail.remark = describe;

            mPresenter.addGoods(detail);
        } else {
            goodsDetail.code = code;
            goodsDetail.name = name;
            goodsDetail.type = model;
            goodsDetail.model = specs;
            goodsDetail.pricebuy = price;
            goodsDetail.remark = describe;
            mPresenter.updateGoods(goodsDetail);
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
