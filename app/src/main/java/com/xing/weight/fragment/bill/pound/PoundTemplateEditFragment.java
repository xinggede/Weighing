package com.xing.weight.fragment.bill.pound;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qmuiteam.qmui.arch.effect.MapEffect;
import com.qmuiteam.qmui.arch.effect.QMUIFragmentMapEffectHandler;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.base.Constants;
import com.xing.weight.bean.PoundItemInfo;
import com.xing.weight.bean.StyleInfo;
import com.xing.weight.bean.TemplateInfo;
import com.xing.weight.fragment.bill.mode.BillContract;
import com.xing.weight.fragment.bill.mode.BillPresenter;
import com.xing.weight.fragment.main.manage.MyCustomAddFragment;
import com.xing.weight.fragment.main.manage.StyleChooseFragment;
import com.xing.weight.view.CusTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.OnClick;

public class PoundTemplateEditFragment extends BaseFragment<BillPresenter> implements BillContract.View {

    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.et_model_name)
    EditText etModelName;
    @BindView(R.id.ck_code)
    CheckBox ckCode;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.ck_company_name)
    CheckBox ckCompanyName;
    @BindView(R.id.tv_company_name)
    CusTextView tvCompanyName;
    @BindView(R.id.et_company_name)
    EditText etCompanyName;
    @BindView(R.id.ck_company_charge)
    CheckBox ckCompanyCharge;
    @BindView(R.id.tv_company_charge)
    TextView tvCompanyCharge;
    @BindView(R.id.et_company_charge)
    EditText etCompanyCharge;
    @BindView(R.id.ck_company_phone)
    CheckBox ckCompanyPhone;
    @BindView(R.id.tv_company_phone)
    TextView tvCompanyPhone;
    @BindView(R.id.et_company_phone)
    EditText etCompanyPhone;
    @BindView(R.id.ck_company_address)
    CheckBox ckCompanyAddress;
    @BindView(R.id.tv_company_address)
    TextView tvCompanyAddress;
    @BindView(R.id.et_company_address)
    EditText etCompanyAddress;
    @BindView(R.id.ck_goods_type)
    CheckBox ckGoodsType;
    @BindView(R.id.tv_goods_type)
    TextView tvGoodsType;
    @BindView(R.id.et_goods_type)
    EditText etGoodsType;
    @BindView(R.id.ck_car_weight)
    CheckBox ckCarWeight;
    @BindView(R.id.tv_car_weight)
    TextView tvCarWeight;
    @BindView(R.id.et_car_weight)
    EditText etCarWeight;
    @BindView(R.id.ck_in_time)
    CheckBox ckInTime;
    @BindView(R.id.tv_in_time)
    TextView tvInTime;
    @BindView(R.id.et_in_time)
    EditText etInTime;
    @BindView(R.id.ck_total_weight)
    CheckBox ckTotalWeight;
    @BindView(R.id.tv_total_weight)
    CusTextView tvTotalWeight;
    @BindView(R.id.et_total_weight)
    EditText etTotalWeight;
    @BindView(R.id.ck_out_time)
    CheckBox ckOutTime;
    @BindView(R.id.tv_out_time)
    TextView tvOutTime;
    @BindView(R.id.et_out_time)
    EditText etOutTime;
    @BindView(R.id.ck_real_weight)
    CheckBox ckRealWeight;
    @BindView(R.id.tv_real_weight)
    TextView tvRealWeight;
    @BindView(R.id.et_real_weight)
    TextView etRealWeight;
    @BindView(R.id.ck_discount)
    CheckBox ckDiscount;
    @BindView(R.id.tv_discount)
    TextView tvDiscount;
    @BindView(R.id.et_discount)
    EditText etDiscount;
    @BindView(R.id.ck_price)
    CheckBox ckPrice;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.ck_total_price)
    CheckBox ckTotalPrice;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.et_total_price)
    EditText etTotalPrice;
    @BindView(R.id.ck_person_weigher)
    CheckBox ckPersonWeigher;
    @BindView(R.id.tv_person_weigher)
    CusTextView tvPersonWeigher;
    @BindView(R.id.et_person_weigher)
    EditText etPersonWeigher;
    @BindView(R.id.ck_receive_name)
    CheckBox ckReceiveName;
    @BindView(R.id.tv_receive_name)
    TextView tvReceiveName;
    @BindView(R.id.et_receive_name)
    EditText etReceiveName;
    @BindView(R.id.ck_driver)
    CheckBox ckDriver;
    @BindView(R.id.tv_driver)
    CusTextView tvDriver;
    @BindView(R.id.et_driver)
    EditText etDriver;
    @BindView(R.id.ck_remarks)
    CheckBox ckRemarks;
    @BindView(R.id.tv_remarks)
    TextView tvRemarks;
    @BindView(R.id.et_remarks)
    EditText etRemarks;
    @BindView(R.id.tv_model_style)
    TextView tvModelStyle;
    private StyleInfo styleInfo;

    TemplateInfo templateInfo;

    public PoundTemplateEditFragment(TemplateInfo templateInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.DATA, templateInfo);
        setArguments(bundle);
    }

    @Override
    protected BillPresenter onLoadPresenter() {
        return new BillPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pound_template_edit;
    }

    @Override
    protected void initView(View view) {
        templateInfo = getArguments().getParcelable(Constants.DATA);
        topbar.setTitle("磅单定制");
        topbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        if(templateInfo != null){
            etModelName.setText(templateInfo.name);
            etModelName.setSelection(etModelName.length());
            tvModelStyle.setText(templateInfo.stylename);

            styleInfo = new StyleInfo();
            styleInfo.id = templateInfo.styleid;
            styleInfo.name = templateInfo.stylename;
        }
    }

    @OnClick(R.id.iv_choose_style)
    public void onChooseStyle() {
        callback();
        startFragment(new StyleChooseFragment(1));
    }

    private void callback() {
        registerEffect(this, new QMUIFragmentMapEffectHandler() {
            @Override
            public boolean shouldHandleEffect(@NonNull MapEffect effect) {
                return effect.getValue(StyleChooseFragment.class.getName()) != null;
            }

            @Override
            public void handleEffect(@NonNull MapEffect effect) {  //该方法只会在界面显示的时候才调用（主线程）
                styleInfo = (StyleInfo) effect.getValue(StyleChooseFragment.class.getName());
                if (styleInfo !=  null) {
                    tvModelStyle.setText(styleInfo.name);
                }
            }
        });
    }

    @OnClick(R.id.bt_save)
    public void onViewClicked() {
        String modelName = etModelName.getText().toString();
        if (TextUtils.isEmpty(modelName)) {
            showToast(R.string.pls_input_model_name);
            return;
        }

        if (styleInfo == null) {
            showToast(R.string.pls_choose_model_style);
            return;
        }

        PoundItemInfo poundItemInfo;
        List<PoundItemInfo> list = new ArrayList<>();
        if (ckCode.isChecked()) {
            poundItemInfo = new PoundItemInfo(tvCode.getText().toString());
            poundItemInfo.type = PoundItemInfo.PoundType.CODE;
            poundItemInfo.hint = etCode.getText().toString();
            list.add(poundItemInfo);
        }

        if (ckCompanyName.isChecked()) {
            poundItemInfo = new PoundItemInfo(tvCompanyName.getText().toString());
            poundItemInfo.type = PoundItemInfo.PoundType.CNAME;
            poundItemInfo.hint = etCompanyName.getText().toString();
            list.add(poundItemInfo);
        }

        if (ckCompanyCharge.isChecked()) {
            poundItemInfo = new PoundItemInfo(tvCompanyCharge.getText().toString());
            poundItemInfo.type = PoundItemInfo.PoundType.CBOSS;
            poundItemInfo.hint = etCompanyCharge.getText().toString();
            list.add(poundItemInfo);
        }

        if (ckCompanyPhone.isChecked()) {
            poundItemInfo = new PoundItemInfo(tvCompanyPhone.getText().toString());
            poundItemInfo.type = PoundItemInfo.PoundType.CPHONE;
            poundItemInfo.hint = etCompanyPhone.getText().toString();
            poundItemInfo.inputType = 2;
            list.add(poundItemInfo);
        }

        if (ckCompanyAddress.isChecked()) {
            poundItemInfo = new PoundItemInfo(tvCompanyAddress.getText().toString());
            poundItemInfo.type = PoundItemInfo.PoundType.CADDRESS;
            poundItemInfo.hint = etCompanyAddress.getText().toString();
            list.add(poundItemInfo);
        }

        if (ckGoodsType.isChecked()) {
            poundItemInfo = new PoundItemInfo(tvGoodsType.getText().toString());
            poundItemInfo.type = PoundItemInfo.PoundType.GTYPE;
            poundItemInfo.hint = etGoodsType.getText().toString();
            list.add(poundItemInfo);
        }

        if (ckCarWeight.isChecked()) {
            poundItemInfo = new PoundItemInfo(tvCarWeight.getText().toString());
            poundItemInfo.type = PoundItemInfo.PoundType.CARWEIGHT;
            poundItemInfo.hint = etCarWeight.getText().toString();
            poundItemInfo.inputType = 1;
            poundItemInfo.lenght = 10;
            list.add(poundItemInfo);
        }

        if (ckInTime.isChecked()) {
            poundItemInfo = new PoundItemInfo(tvInTime.getText().toString());
            poundItemInfo.type = PoundItemInfo.PoundType.INTIME;
            poundItemInfo.hint = etInTime.getText().toString();
            list.add(poundItemInfo);
        }

        if (ckTotalWeight.isChecked()) {
            poundItemInfo = new PoundItemInfo(tvTotalWeight.getText().toString());
            poundItemInfo.type = PoundItemInfo.PoundType.TOTALWEIGHT;
            poundItemInfo.hint = etTotalWeight.getText().toString();
            poundItemInfo.inputType = 1;
            poundItemInfo.lenght = 10;
            list.add(poundItemInfo);
        }

        if (ckOutTime.isChecked()) {
            poundItemInfo = new PoundItemInfo(tvOutTime.getText().toString());
            poundItemInfo.type = PoundItemInfo.PoundType.OUTTIME;
            poundItemInfo.hint = etOutTime.getText().toString();
            list.add(poundItemInfo);
        }

        if (ckRealWeight.isChecked()) {
            poundItemInfo = new PoundItemInfo(tvRealWeight.getText().toString());
            poundItemInfo.type = PoundItemInfo.PoundType.REALWEIGHT;
            poundItemInfo.hint = etRealWeight.getText().toString();
            poundItemInfo.inputType = 1;
            poundItemInfo.lenght = 10;
            list.add(poundItemInfo);
        }

        if (ckDiscount.isChecked()) {
            poundItemInfo = new PoundItemInfo(tvDiscount.getText().toString());
            poundItemInfo.type = PoundItemInfo.PoundType.DISCOUNT;
            poundItemInfo.hint = etDiscount.getText().toString();
            poundItemInfo.inputType = 1;
            poundItemInfo.lenght = 10;
            list.add(poundItemInfo);
        }

        if (ckPrice.isChecked()) {
            poundItemInfo = new PoundItemInfo(tvPrice.getText().toString());
            poundItemInfo.type = PoundItemInfo.PoundType.PRICE;
            poundItemInfo.hint = etPrice.getText().toString();
            poundItemInfo.inputType = 1;
            poundItemInfo.lenght = 10;
            list.add(poundItemInfo);
        }

        if (ckTotalPrice.isChecked()) {
            poundItemInfo = new PoundItemInfo(tvTotalPrice.getText().toString());
            poundItemInfo.type = PoundItemInfo.PoundType.TOTALPRICE;
            poundItemInfo.hint = etTotalPrice.getText().toString();
            poundItemInfo.inputType = 1;
            poundItemInfo.lenght = 10;
            list.add(poundItemInfo);
        }

        if (ckPersonWeigher.isChecked()) {
            poundItemInfo = new PoundItemInfo(tvPersonWeigher.getText().toString());
            poundItemInfo.type = PoundItemInfo.PoundType.PERSON;
            poundItemInfo.hint = etPersonWeigher.getText().toString();
            list.add(poundItemInfo);
        }

        if (ckReceiveName.isChecked()) {
            poundItemInfo = new PoundItemInfo(tvReceiveName.getText().toString());
            poundItemInfo.type = PoundItemInfo.PoundType.RECEIVENAME;
            poundItemInfo.hint = etReceiveName.getText().toString();
            list.add(poundItemInfo);
        }

        if (ckDriver.isChecked()) {
            poundItemInfo = new PoundItemInfo(tvDriver.getText().toString());
            poundItemInfo.type = PoundItemInfo.PoundType.DRIVER;
            poundItemInfo.hint = etDriver.getText().toString();
            list.add(poundItemInfo);
        }

        if (ckRemarks.isChecked()) {
            poundItemInfo = new PoundItemInfo(tvRemarks.getText().toString());
            poundItemInfo.type = PoundItemInfo.PoundType.REMARKS;
            poundItemInfo.hint = etRemarks.getText().toString();
            poundItemInfo.inputType = 3;
            poundItemInfo.lenght = 100;
            list.add(poundItemInfo);
        }
        if(templateInfo == null){
            TemplateInfo info = new TemplateInfo();
            info.name = modelName;
            info.contList = list;
            info.styleid = styleInfo.id;
            info.stylename = styleInfo.name;
            info.type = 1;
            mPresenter.addTemplate(info,true);
        } else {
            templateInfo.name = modelName;
            templateInfo.contList = list;
            templateInfo.type = 1;
            templateInfo.stylename = styleInfo.name;
            templateInfo.styleid = styleInfo.id;
            mPresenter.addTemplate(templateInfo,false);
        }
    }

    @Override
    public void onHttpResult(boolean success, int code, Object data) {
        if(success){
            Map<String,Object> map = new HashMap<>();
            map.put(getClass().getName(), true);
            notifyEffect(new MapEffect(map));
            popBackStack();
        }
    }
}
