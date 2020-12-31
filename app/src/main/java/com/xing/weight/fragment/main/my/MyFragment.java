package com.xing.weight.fragment.main.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.xing.weight.LoginActivity;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.fragment.main.my.mode.MyContract;
import com.xing.weight.fragment.main.my.mode.MyPresenter;
import com.xing.weight.util.MyImageLoader;
import com.xing.weight.util.Tools;
import com.xing.weight.view.CircleImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

public class MyFragment extends BaseFragment<MyPresenter> implements MyContract.View {

    @BindView(R.id.iv_head)
    CircleImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_days)
    TextView tvDays;
    @BindView(R.id.bt_exit)
    QMUIRoundButton btExit;

    @Override
    protected MyPresenter onLoadPresenter() {
        return new MyPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView(View view) {
        tvName.setText(mPresenter.getUserName());
        MyImageLoader.loadHead(getContext(), mPresenter.getUserHead(), ivHead);
        String dueDate = mPresenter.getDueDate();
        if(!TextUtils.isEmpty(dueDate)){
            tvDays.setText(dueDate.split(" ")[0]);
        }

    }

    @OnClick({R.id.re_company_info,R.id.re_print_manage, R.id.re_term_of_validity, R.id.re_about, R.id.bt_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.re_company_info:
                startFragment(new CompanyInfoFragment());
                break;
            case R.id.re_print_manage:
                startFragment(new PrintManageFragment());
                break;
            case R.id.re_term_of_validity:
                break;
            case R.id.re_about:
                startFragment(new AboutFragment());
                break;
            case R.id.bt_exit:
                exit();
                break;
        }
    }

    public void exit() {
        new QMUIDialog.MessageDialogBuilder(getActivity())
                .setTitle("提示")
                .setMessage("确定要退出登录吗？")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "确定", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        mPresenter.logout();
                    }
                }).create(R.style.DialogTheme2).show();
    }

    @Override
    public void onHttpResult(boolean success, int code, Object data) {
        if(success){
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        }
    }
}
