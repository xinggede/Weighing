package com.xing.weight.fragment.main.home;

import android.view.View;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.fragment.bill.pound.PoundAddFragment;
import com.xing.weight.fragment.main.home.mode.HomeContract;
import com.xing.weight.fragment.main.home.mode.HomePresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment<HomePresenter> implements HomeContract.View {

    @BindView(R.id.bt_weight_print)
    QMUIRoundButton btWeightPrint;
    @BindView(R.id.bt_outbound_print)
    QMUIRoundButton btOutboundPrint;
    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;

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
        topbar.setTitle("单据打印");
        btWeightPrint.setChangeAlphaWhenPress(true);
    }


    @OnClick({R.id.bt_weight_print, R.id.bt_outbound_print})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_weight_print:
                startFragment(new PoundAddFragment());
                break;

            case R.id.bt_outbound_print:
                showToast("该功能正在开发中");
//                startFragment(new OutboundAddFragment());
                break;
        }
    }
}
