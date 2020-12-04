package com.xing.weight.base;

import android.content.Intent;

import com.qmuiteam.qmui.arch.QMUIFragmentActivity;
import com.xing.weight.R;

import java.util.List;

import androidx.annotation.NonNull;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 权限申请
 *
 * @author 星哥的
 */
public class EasyPermissionActivity extends QMUIFragmentActivity implements EasyPermissions.PermissionCallbacks {

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        //成功
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //失败
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
        new AppSettingsDialog.Builder(this).setTitle(R.string.perm_title).setRationale(R.string.perm_content).build().show();
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            //可以检查权限，提示什么的
        }
    }
}
