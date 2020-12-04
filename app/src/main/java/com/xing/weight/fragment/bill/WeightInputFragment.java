package com.xing.weight.fragment.bill;

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
import com.xing.weight.fragment.main.MainContract;
import com.xing.weight.fragment.main.MainPresenter;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;

public class WeightInputFragment extends BaseFragment<MainPresenter> implements MainContract.View {


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
    @BindView(R.id.et_shipping_unit)
    EditText etShippingUnit;
    @BindView(R.id.et_person_in_charge)
    EditText etPersonInCharge;
    @BindView(R.id.et_driver)
    EditText etDriver;
    @BindView(R.id.et_remarks)
    EditText etRemarks;

    private QMUIPopup choosePopup;

    @Override
    protected MainPresenter onLoadPresenter() {
        return new MainPresenter();
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
                startFragment(new BillRecordFragment());
            }
        });
    }


    private void showChooseName(View v) {
        List<String> data = new ArrayList<>();
        data.add("矿砂");
        data.add("山石");
        data.add("土方");

        ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.list_item_choose, data);

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tvName.setText(adapterView.getItemAtPosition(i).toString());
                if (choosePopup != null) {
                    choosePopup.dismiss();
                }
            }
        };

        if (choosePopup == null) {
            choosePopup = QMUIPopups.listPopup(getContext(),
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
        choosePopup.show(v);
    }

    @OnClick({R.id.tv_name, R.id.bt_print})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_name:
                showChooseName(view);
                break;
            case R.id.bt_print:
                break;
        }
    }
}
