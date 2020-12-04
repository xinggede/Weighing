package com.xing.weight.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import com.qmuiteam.qmui.arch.annotation.ActivityScheme;
import com.xing.weight.LoginActivity;
import com.xing.weight.MainActivity;
import com.xing.weight.R;
import com.xing.weight.base.BaseActivity;
import com.xing.weight.base.EmptyPresenter;
import com.xing.weight.base.mvp.BaseContract;

import static com.xing.weight.base.Constants.RC_WRITE_PERM;


@ActivityScheme(name = "launcher")
public class LauncherActivity extends BaseActivity<EmptyPresenter> implements BaseContract.View {

    @Override
    protected EmptyPresenter onLoadPresenter() {
        return new EmptyPresenter();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
        }
    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        writeTask();
    }

    private boolean hasWritePermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @AfterPermissionGranted(RC_WRITE_PERM)
    public void writeTask() {
        if (hasWritePermission()) {
            start();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.perm_write), RC_WRITE_PERM,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void start() {
        startActivity(new Intent(this, LoginActivity.class));
        if (mPresenter.isFirst()) {
//            startActivity(new Intent(this, RegisterActivity.class));
        } else if (mPresenter.isLogin()) {
//            startActivity(new Intent(this, MainActivity.class));
        } else {
//            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (hasWritePermission()) {
                start();
            } else {
                showToast("权限申请失败");
                finish();
            }
        }
    }

}
