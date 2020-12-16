package com.xing.weight.fragment.bill.pound;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.bean.CustomerInfo;
import com.xing.weight.bean.GoodsDetail;
import com.xing.weight.bean.PoundInfo;
import com.xing.weight.fragment.bill.mode.BillContract;
import com.xing.weight.fragment.bill.mode.BillPresenter;
import com.xing.weight.util.Tools;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;

public class WeightInputFragment extends BaseFragment<BillPresenter> implements BillContract.View {


    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_enter_time)
    TextView tvEnterTime;
    @BindView(R.id.et_empty_weight)
    EditText etEmptyWeight;
    @BindView(R.id.tv_out_time)
    TextView tvOutTime;
    @BindView(R.id.et_max_weight)
    EditText etMaxWeight;
    @BindView(R.id.et_loss_weight)
    EditText etLossWeight;
    @BindView(R.id.et_real_weight)
    TextView etRealWeight;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.et_person_weigher)
    EditText etPersonWeigher;
    @BindView(R.id.tv_shipping_unit)
    TextView tvShippingUnit;
    @BindView(R.id.et_person_in_charge)
    EditText etPersonInCharge;
    @BindView(R.id.et_driver)
    EditText etDriver;
    @BindView(R.id.et_remarks)
    EditText etRemarks;

    GoodsDetail detail;
    CustomerInfo info;

    private QMUIPopup chooseGoodsPopup, chooseCustomPopup;

    @Override
    protected BillPresenter onLoadPresenter() {
        return new BillPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_weight_input;
    }

    @Override
    protected void initView(View view) {
        topbar.setTitle("入场录单");
        topbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        topbar.addRightImageButton(R.mipmap.icon_menu, R.id.topbar_right_menu_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startFragment(new PoundRecordFragment());
            }
        });
    }


    private void showChooseGoods(View v) {
        if (chooseGoodsPopup == null) {
            ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.list_item_choose, mPresenter.getSaveGoods());
            AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (chooseGoodsPopup != null) {
                        chooseGoodsPopup.dismiss();
                    }
                    detail = (GoodsDetail) adapterView.getItemAtPosition(i);
                    tvName.setText(detail.name);
                    etPrice.setText(String.valueOf(detail.price));
                    etRemarks.setText(detail.remark);
                }
            };
            chooseGoodsPopup = QMUIPopups.listPopup(getContext(),
                    QMUIDisplayHelper.dp2px(getContext(), 200),
                    QMUIDisplayHelper.dp2px(getContext(), 300),
                    adapter,
                    onItemClickListener)
                    .bgColor(ContextCompat.getColor(getContext(), R.color.tab_bj))
                    .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                    .preferredDirection(QMUIPopup.DIRECTION_TOP)
                    .shadow(true)
                    .offsetYIfTop(QMUIDisplayHelper.dp2px(getContext(), 5));
        }
        chooseGoodsPopup.show(v);
    }

    private void showChooseCustom(View v) {
        if (chooseCustomPopup == null) {
            ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.list_item_choose, mPresenter.getSaveCustomer());
            AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (chooseCustomPopup != null) {
                        chooseCustomPopup.dismiss();
                    }
                    info = (CustomerInfo) adapterView.getItemAtPosition(i);
                    tvShippingUnit.setText(info.comname);
                    etPersonInCharge.setText(info.name);
                }
            };
            chooseCustomPopup = QMUIPopups.listPopup(getContext(),
                    QMUIDisplayHelper.dp2px(getContext(), 200),
                    QMUIDisplayHelper.dp2px(getContext(), 300),
                    adapter,
                    onItemClickListener)
                    .bgColor(ContextCompat.getColor(getContext(), R.color.tab_bj))
                    .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                    .preferredDirection(QMUIPopup.DIRECTION_TOP)
                    .shadow(true)
                    .offsetYIfTop(QMUIDisplayHelper.dp2px(getContext(), 5));
        }
        chooseCustomPopup.show(v);
    }

    @OnClick({R.id.tv_name, R.id.bt_print, R.id.tv_shipping_unit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_name:
                if(mPresenter.getSaveGoods().isEmpty()){
                    mPresenter.getGoods();
                } else {
                    showChooseGoods(view);
                }
                break;
            case R.id.tv_shipping_unit:
                if(mPresenter.getSaveCustomer().isEmpty()){
                    mPresenter.getCustom();
                } else {
                    showChooseCustom(view);
                }
                break;
            case R.id.bt_print:
                save();
                break;
        }
    }

    private void save(){
        if(detail == null){
            showToast(R.string.text2);
            return;
        }

        String emptyWeight = etEmptyWeight.getText().toString();
        if(TextUtils.isEmpty(emptyWeight)){
            showToast(R.string.text4);
            etEmptyWeight.requestFocus();
            return;
        }

        PoundInfo poundInfo = new PoundInfo();
        poundInfo.code = tvNumber.getText().toString();
        poundInfo.cargotype = detail.name;
        poundInfo.indate = Tools.getCurrentDate();
        poundInfo.truckweight = Tools.stringToDouble2(emptyWeight);

    }

    @Override
    public void onHttpResult(boolean success, int code, Object data) {
        if(success){
            if(code == 0){
                showChooseGoods(tvName);
            } else if(code == 1){
                showChooseCustom(tvShippingUnit);
            }
        }
    }
}
