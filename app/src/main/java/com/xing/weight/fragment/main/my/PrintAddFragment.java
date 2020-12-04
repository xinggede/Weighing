package com.xing.weight.fragment.main.my;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qmuiteam.qmui.layout.QMUILinearLayout;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.xing.weight.R;
import com.xing.weight.activity.ScanCodeActivity;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.fragment.main.MainContract;
import com.xing.weight.fragment.main.MainPresenter;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

public class PrintAddFragment extends BaseFragment<MainPresenter> implements MainContract.View {


    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.et_print_number)
    EditText etPrintNumber;
    @BindView(R.id.et_print_code)
    EditText etPrintCode;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.tv_paper_type)
    TextView tvPaperType;
    @BindView(R.id.et_remarks)
    EditText etRemarks;
    @BindView(R.id.bt_ok)
    QMUIRoundButton btOk;
    @BindView(R.id.lin_top)
    QMUILinearLayout linTop;

    @Override
    protected MainPresenter onLoadPresenter() {
        return new MainPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_print_add;
    }

    @Override
    protected void initView(View view) {
        topbar.setTitle("添加打印机");
        topbar.addLeftBackImageButton().setOnClickListener((v)->
            popBackStack()
        );
    }

    @OnClick({R.id.iv_scan, R.id.tv_paper_type, R.id.bt_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_scan:
                startScan();
                break;

            case R.id.tv_paper_type:

                break;

            case R.id.bt_ok:

                break;
        }
    }

    private void startScan() {
        registerForActivityResult(new ActivityResultContract<Class<?>, String>() {
            @NonNull
            @Override
            public Intent createIntent(@NonNull Context context, Class<?> input) {
                return new Intent(context, input);
            }

            @Override
            public String parseResult(int resultCode, @Nullable Intent intent) {
                return null;
            }
        }, this::parseResult
        ).launch(ScanCodeActivity.class);
    }

    private void parseResult(String result){

    }
}
