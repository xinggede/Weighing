package com.xing.weight.base;

import android.os.Bundle;

import com.qmuiteam.qmui.arch.QMUIFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * 必须是BaseActivity下的fragment，为了以后管理方便
 *
 * @author 星哥的
 */
public abstract class LazyFragment extends QMUIFragment implements EasyPermissions.PermissionCallbacks {
    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;
    /**
     * 标志位，标志已经初始化完成
     */
    protected boolean isPrepared = false;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    protected boolean mHasLoadedOnce = false;
    Bundle savedState;

    public LazyFragment() {
        super();
        if (getArguments() == null) {
            setArguments(new Bundle());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!restoreStateFromArguments()) {
            onFirstTimeLaunched();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
    }

    protected Bundle getSavedState() {
        Bundle b = getArguments();
        if (b != null) {
            savedState = b.getBundle("invoice");
        }
        return savedState;
    }

    private void onFirstTimeLaunched() { // 第一次加载

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad(false);
    }

    /**
     * 不可见
     */
    protected void onInvisible() {

    }

    /**
     * isRefresh 是否主动刷新 延迟加载 子类必须重写此方法
     */
    public abstract void lazyLoad(boolean isRefresh);

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save State Here
        saveStateToArguments();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveStateToArguments();
    }

    private void saveStateToArguments() { // 保存状态
        if (getView() != null) {
            savedState = saveState();
        }
        if (savedState != null) {
            Bundle b = getArguments();
            if (b != null) {
                b.putBundle("invoice", savedState);
            }
        }
    }

    private Bundle saveState() {
        Bundle state = new Bundle();
        onSaveState(state);
        return state;
    }

    protected void onSaveState(Bundle outState) {

    }

    private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
        if (b != null) {
            savedState = b.getBundle("invoice");
            if (savedState != null) {
                restoreState();
                return true;
            }
        }
        return false;
    }

    private void restoreState() { // 还原
        if (savedState != null) {
            // For Example
            // tv1.setText(savedState.getString("text"));
            onRestoreState(savedState);
        }
    }

    protected void onRestoreState(Bundle savedInstanceState) {

    }
}
