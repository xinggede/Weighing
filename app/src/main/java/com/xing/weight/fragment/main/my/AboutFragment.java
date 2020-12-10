package com.xing.weight.fragment.main.my;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.fragment.main.MainContract;
import com.xing.weight.fragment.main.MainPresenter;
import com.xing.weight.util.Tools;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutFragment extends BaseFragment<MainPresenter> implements MainContract.View {


    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.tv_ver)
    TextView tvVer;
    @BindView(R.id.tv_phone)
    TextView tvPhone;

    @Override
    protected MainPresenter onLoadPresenter() {
        return new MainPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about;
    }

    @Override
    protected void initView(View view) {
        topbar.setTitle("关于");
        topbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        tvVer.setText(Tools.getVerCode(getContext()) + " - V" + Tools.getVersionName(getContext()));
    }

    @OnClick({R.id.re_ver, R.id.re_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.re_ver:
                break;

            case R.id.re_phone:
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + tvPhone.getText().toString()));
                startActivity(intent);
                break;
        }
    }
}
