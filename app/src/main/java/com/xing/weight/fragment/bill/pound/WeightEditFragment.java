package com.xing.weight.fragment.bill.pound;

import android.view.View;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.fragment.main.MainContract;
import com.xing.weight.fragment.main.MainPresenter;

import butterknife.BindView;

public class WeightEditFragment extends BaseFragment<MainPresenter> implements MainContract.View {


    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;

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


}
