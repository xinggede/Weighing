package com.xing.weight.fragment.bill.pound;

import android.view.View;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout2;
import com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.fragment.bill.mode.BillContract;
import com.xing.weight.fragment.bill.mode.BillPresenter;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class WeightListFragment extends BaseFragment<BillPresenter> implements BillContract.View {

    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.pull_layout)
    QMUIPullLayout pullLayout;

    @Override
    protected BillPresenter onLoadPresenter() {
        return new BillPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_weight;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    public void onHttpResult(boolean success, int code, Object data) {

    }
}
