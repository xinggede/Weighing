package com.xing.weight.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qmuiteam.qmui.arch.QMUIFragment;
import com.qmuiteam.qmui.arch.SwipeBackLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.xing.weight.base.mvp.BaseContract;
import com.xing.weight.base.mvp.BasePresenter;
import com.xing.weight.dialog.TipDialog;
import com.xing.weight.fragment.main.MainFragment;
import com.xing.weight.util.Tools;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFragment<P extends BasePresenter> extends QMUIFragment implements BaseContract.View, DialogInterface.OnKeyListener {

    protected P mPresenter;
    protected Unbinder unbinder;
    private TipDialog proDialog;

    //第一次走 1、2、3、4  返回只走 1,3

    @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);  //1
        }

        @Override
        protected View onCreateView() {
            return LayoutInflater.from(getActivity()).inflate(getLayoutId(), null); //2
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState); //3
        }

        @Override
        protected void onViewCreated(@NonNull View rootView) {
            super.onViewCreated(rootView); //4
            unbinder = ButterKnife.bind(this, rootView);
             mPresenter = onLoadPresenter();
        try {
            if (mPresenter != null) {
                mPresenter.attachView(this);
            }
        } catch (Exception e) {
        }
        initView(rootView);
    }

    @Override
    public void onDestroyView() {
        hideLoading();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
    }

    @Override
    protected int backViewInitOffset(Context context, int dragDirection, int moveEdge) {
        if (moveEdge == SwipeBackLayout.EDGE_TOP || moveEdge == SwipeBackLayout.EDGE_BOTTOM) {
            return 0;
        }
        return QMUIDisplayHelper.dp2px(context, 100);
    }

    protected abstract P onLoadPresenter();

    public P getPresenter() {
        return mPresenter;
    }

    protected abstract int getLayoutId();

    protected abstract void initView(View view);

    @Override
    public void showLoading() {
        showLoading("正在加载...");
    }

    @Override
    public void showLoading(String msg) {
        if (proDialog == null) {
            TipDialog.Builder builder = new TipDialog.Builder(getContext())
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                    .setTipWord(msg);
            proDialog =  builder.create();
            proDialog.setBuilder(builder);
            proDialog.setOnKeyListener(this);
        } else {
            proDialog.getBuilder().setShowText(msg);
        }
        if(!proDialog.isShowing()){
            proDialog.show();
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
        Tools.toastShow(getContext(), message);
    }

    @Override
    public void showToast(int id) {
        Tools.toastShow(getContext(), id);
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
        getActivity().finish();
    }


    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dialog.dismiss();
            if(mPresenter != null){
                mPresenter.unDispose();
            }
            return true;
        }
        return false;
    }
}
