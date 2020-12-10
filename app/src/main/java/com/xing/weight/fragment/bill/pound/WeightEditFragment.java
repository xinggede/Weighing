package com.xing.weight.fragment.bill.pound;

import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.bean.PoundModel;
import com.xing.weight.fragment.main.MainContract;
import com.xing.weight.fragment.main.MainPresenter;
import com.xing.weight.view.CusTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class WeightEditFragment extends BaseFragment<MainPresenter> implements MainContract.View {


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
    TextView etCompanyCharge;
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

    @Override
    protected MainPresenter onLoadPresenter() {
        return new MainPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_weight_edit;
    }

    @Override
    protected void initView(View view) {
        topbar.setTitle("磅单定制");
        topbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
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

        PoundModel poundModel;
        List<PoundModel> list = new ArrayList<>();
        if (ckCode.isChecked()) {
            poundModel = new PoundModel(tvCode.getText().toString());
            poundModel.hint = etCode.getText().toString();
            list.add(poundModel);
        }

        if (ckCompanyName.isChecked()) {
            poundModel = new PoundModel(tvCompanyName.getText().toString());
            poundModel.hint = etCompanyName.getText().toString();
            list.add(poundModel);
        }

        if (ckCompanyCharge.isChecked()) {
            poundModel = new PoundModel(tvCompanyCharge.getText().toString());
            poundModel.hint = etCompanyCharge.getText().toString();
            list.add(poundModel);
        }

        if (ckCompanyPhone.isChecked()) {
            poundModel = new PoundModel(tvCompanyPhone.getText().toString());
            poundModel.hint = etCompanyPhone.getText().toString();
            poundModel.type = 2;
            list.add(poundModel);
        }

        if (ckCompanyAddress.isChecked()) {
            poundModel = new PoundModel(tvCompanyAddress.getText().toString());
            poundModel.hint = etCompanyAddress.getText().toString();
            list.add(poundModel);
        }

        if (ckGoodsType.isChecked()) {
            poundModel = new PoundModel(tvGoodsType.getText().toString());
            poundModel.hint = etGoodsType.getText().toString();
            list.add(poundModel);
        }

        if (ckCarWeight.isChecked()) {
            poundModel = new PoundModel(tvCarWeight.getText().toString());
            poundModel.hint = etCarWeight.getText().toString();
            poundModel.type = 1;
            poundModel.lenght = 10;
            list.add(poundModel);
        }

        if (ckInTime.isChecked()) {
            poundModel = new PoundModel(tvInTime.getText().toString());
            poundModel.hint = etInTime.getText().toString();
            list.add(poundModel);
        }

        if (ckTotalWeight.isChecked()) {
            poundModel = new PoundModel(tvTotalWeight.getText().toString());
            poundModel.hint = etTotalWeight.getText().toString();
            poundModel.type = 1;
            poundModel.lenght = 10;
            list.add(poundModel);
        }

        if (ckOutTime.isChecked()) {
            poundModel = new PoundModel(tvOutTime.getText().toString());
            poundModel.hint = etOutTime.getText().toString();
            list.add(poundModel);
        }

        if (ckRealWeight.isChecked()) {
            poundModel = new PoundModel(tvRealWeight.getText().toString());
            poundModel.hint = etRealWeight.getText().toString();
            poundModel.type = 1;
            poundModel.lenght = 10;
            list.add(poundModel);
        }

        if (ckDiscount.isChecked()) {
            poundModel = new PoundModel(tvDiscount.getText().toString());
            poundModel.hint = etDiscount.getText().toString();
            poundModel.type = 1;
            poundModel.lenght = 10;
            list.add(poundModel);
        }

        if (ckPrice.isChecked()) {
            poundModel = new PoundModel(tvPrice.getText().toString());
            poundModel.hint = etPrice.getText().toString();
            poundModel.type = 1;
            poundModel.lenght = 10;
            list.add(poundModel);
        }

        if (ckTotalPrice.isChecked()) {
            poundModel = new PoundModel(tvTotalPrice.getText().toString());
            poundModel.hint = etTotalPrice.getText().toString();
            poundModel.type = 1;
            poundModel.lenght = 10;
            list.add(poundModel);
        }

        if (ckPersonWeigher.isChecked()) {
            poundModel = new PoundModel(tvPersonWeigher.getText().toString());
            poundModel.hint = etPersonWeigher.getText().toString();
            list.add(poundModel);
        }

        if (ckReceiveName.isChecked()) {
            poundModel = new PoundModel(tvReceiveName.getText().toString());
            poundModel.hint = etReceiveName.getText().toString();
            list.add(poundModel);
        }

        if (ckDriver.isChecked()) {
            poundModel = new PoundModel(tvDriver.getText().toString());
            poundModel.hint = etDriver.getText().toString();
            list.add(poundModel);
        }

        if (ckRemarks.isChecked()) {
            poundModel = new PoundModel(tvRemarks.getText().toString());
            poundModel.hint = etRemarks.getText().toString();
            poundModel.lenght = 100;
            list.add(poundModel);
        }
    }
}
