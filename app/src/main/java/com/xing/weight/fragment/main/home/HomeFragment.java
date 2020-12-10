package com.xing.weight.fragment.main.home;

import android.view.View;
import android.widget.EditText;

import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.fragment.bill.OutboundInputFragment;
import com.xing.weight.fragment.bill.pound.WeightInputFragment;
import com.xing.weight.fragment.main.home.mode.HomeContract;
import com.xing.weight.fragment.main.home.mode.HomePresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment<HomePresenter> implements HomeContract.View {

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.bt_weight_print)
    QMUIRoundButton btWeightPrint;
    @BindView(R.id.bt_outbound_print)
    QMUIRoundButton btOutboundPrint;

    @Override
    protected HomePresenter onLoadPresenter() {
        return new HomePresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view) {
        btWeightPrint.setChangeAlphaWhenPress(true);
    }


    @OnClick({R.id.bt_weight_print, R.id.bt_outbound_print, R.id.ib_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_weight_print:
                startFragment(new WeightInputFragment());
                break;

            case R.id.bt_outbound_print:
                startFragment(new OutboundInputFragment());
                break;

            case R.id.ib_menu:

                break;
        }
    }
}
