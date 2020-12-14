package com.xing.weight.fragment.main.manage;

import android.view.View;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.fragment.bill.pound.PoundTemplateEditFragment;
import com.xing.weight.fragment.bill.pound.PoundTemplateListFragment;
import com.xing.weight.fragment.main.MainContract;
import com.xing.weight.fragment.main.MainPresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class ManageFragment extends BaseFragment<MainPresenter> implements MainContract.View {

    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;

    @Override
    protected MainPresenter onLoadPresenter() {
        return new MainPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_manage;
    }

    @Override
    protected void initView(View view) {
        topbar.setTitle("单据管理");
    }

    @OnClick({R.id.bt_bill_custom, R.id.bt_outbound_custom, R.id.bt_my_order, R.id.bt_my_goods, R.id.bt_my_user, R.id.bt_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_bill_custom:
                startFragment(new PoundTemplateListFragment());
                break;
            case R.id.bt_outbound_custom:
                break;
            case R.id.bt_my_order:
                startFragment(new MyOrderFragment());
                break;
            case R.id.bt_my_goods:
                startFragment(new MyGoodsListFragment());
                break;
            case R.id.bt_my_user:
                startFragment(new MyCustomListFragment());
                break;
            case R.id.bt_more:
                startFragment(new StyleChooseFragment(1));
                break;
        }
    }
}
