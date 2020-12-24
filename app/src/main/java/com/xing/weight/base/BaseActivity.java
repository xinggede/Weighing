package com.xing.weight.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.xing.weight.base.mvp.BaseContract;
import com.xing.weight.base.mvp.BasePresenter;
import com.xing.weight.dialog.TipDialog;
import com.xing.weight.util.Tools;

import androidx.annotation.NonNull;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity<P extends BasePresenter> extends EasyPermissionActivity implements BaseContract.View, View.OnClickListener {

    protected P mPresenter;
    protected Unbinder unbinder;
    private TipDialog proDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getRootViewId() != 0){
            setContentView(getRootViewId());
        }
        unbinder = ButterKnife.bind(this);
        try {
            mPresenter = onLoadPresenter();
            getPresenter().attachView(this);
        } catch (Exception e) {

        }
        setStatusBar();
        initViews(savedInstanceState);
        initEventAndData(savedInstanceState);
    }

    protected void setStatusBar() {
//        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.colorPrimary), 0);
//        StatusBarUtil.setLightMode(this);
    }

    public P getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getPresenter() != null) {
            getPresenter().startView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (mPresenter != null) {
            mPresenter.stopView();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        hideLoading();
        unbinder.unbind();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        super.onDestroy();
    }

    protected abstract P onLoadPresenter();

    protected abstract void initViews(Bundle savedInstanceState);

    protected abstract void initEventAndData(Bundle savedInstanceState);

    protected int getRootViewId(){
        return 0;
    }

    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.ib_back) {
//            finish();
//        }
    }

    @Override
    public void showLoading() {
        showLoading("正在加载...");
    }

    @Override
    public void showLoading(String msg) {
        if (proDialog == null) {
            TipDialog.Builder builder = new TipDialog.Builder(this)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                    .setTipWord(msg);
            proDialog =  builder.create();
            proDialog.setBuilder(builder);
        } else {
            proDialog.getBuilder().setShowText(msg);
        }
        if(!proDialog.isShowing()){
            proDialog.show();
        }
    }

    public void setCancelable(boolean b) {
        if (proDialog != null) {
            proDialog.setCancelable(b);
        }
    }

    @Override
    public void hideLoading() {
        if (proDialog != null) {
            proDialog.dismiss();
        }
    }

    @Override
    public void showMessage(@NonNull String message) {

    }

    @Override
    public void showToast(@NonNull String message) {
        Tools.toastShow(this, message);
    }

    @Override
    public void showToast(int id) {
        Tools.toastShow(this, id);
    }

    @Override
    public void onTokenError() {

    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        startActivity(intent);
    }

    @Override
    public void closeActivity() {
        finish();
    }
}
