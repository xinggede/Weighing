package com.xing.weight.fragment.main.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.qmuiteam.qmui.arch.effect.MapEffect;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.xing.weight.R;
import com.xing.weight.activity.ScanCodeActivity;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.base.Constants;
import com.xing.weight.bean.PrinterInfo;
import com.xing.weight.fragment.main.my.mode.MyContract;
import com.xing.weight.fragment.main.my.mode.MyPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;

public class PrintAddFragment extends BaseFragment<MyPresenter> implements MyContract.View {


    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.et_print_check_code)
    EditText etPrintCheckCode;
    @BindView(R.id.et_print_code)
    EditText etPrintCode;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_paper_type)
    TextView tvPaperType;
    @BindView(R.id.et_remarks)
    EditText etRemarks;

    private PrinterInfo printerInfo;

    public PrintAddFragment(PrinterInfo info) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.DATA, info);
        setArguments(bundle);
    }

    @Override
    protected MyPresenter onLoadPresenter() {
        return new MyPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_print_add;
    }

    @Override
    protected void initView(View view) {
        printerInfo = getArguments().getParcelable(Constants.DATA);
        topbar.addLeftBackImageButton().setOnClickListener((v)->
            popBackStack()
        );
        if(printerInfo == null){
            topbar.setTitle("添加打印机");
        } else {
            topbar.setTitle("编辑打印机");
            etPrintCode.setText(printerInfo.devcode);
            etPrintCheckCode.setText(printerInfo.verfycode);
            etName.setText(printerInfo.name);
            tvPaperType.setText(printerInfo.norms);
            etRemarks.setText(printerInfo.remark);
        }
    }

    private QMUIPopup choosePopup;

    private void showChoosePaper(View v) {
        List<String> data = new ArrayList<>();
        data.add("A4");
        data.add("A5");
        data.add("B5");

        ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.list_item_choose, data);

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tvPaperType.setText(adapterView.getItemAtPosition(i).toString());
                if (choosePopup != null) {
                    choosePopup.dismiss();
                }
            }
        };

        if (choosePopup == null) {
            choosePopup = QMUIPopups.listPopup(getContext(),
                    QMUIDisplayHelper.dp2px(getContext(), 200),
                    QMUIDisplayHelper.dp2px(getContext(), 300),
                    adapter,
                    onItemClickListener)
                    .bgColor(ContextCompat.getColor(getContext(), R.color.tab_bj))
                    .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                    .preferredDirection(QMUIPopup.DIRECTION_TOP)
                    .shadow(true)
                    .offsetYIfTop(QMUIDisplayHelper.dp2px(getContext(), 5));
        }
        choosePopup.show(v);
    }

    @OnClick({R.id.iv_scan, R.id.tv_paper_type, R.id.bt_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_scan:
                startScan();
                break;

            case R.id.tv_paper_type:
                showChoosePaper(view);
                break;

            case R.id.bt_save:
                save();
                break;
        }
    }

    private void save(){
        String code = etPrintCode.getText().toString();
        if(TextUtils.isEmpty(code)){
            showToast(R.string.pls_input_print_code);
            etPrintCode.requestFocus();
            return;
        }
        String checkCode = etPrintCheckCode.getText().toString();
        if(TextUtils.isEmpty(checkCode)){
            showToast(R.string.pls_input_print_check_code);
            etPrintCheckCode.requestFocus();
            return;
        }
        String name = etName.getText().toString();
        if(TextUtils.isEmpty(name)){
            showToast(R.string.pls_input_print_name);
            etName.requestFocus();
            return;
        }
        String paper = tvPaperType.getText().toString();
        if(TextUtils.isEmpty(paper)){
            showToast(R.string.pls_input_print_paper);
            return;
        }
        String remarks = etRemarks.getText().toString();

        if(printerInfo == null){
            PrinterInfo info = new PrinterInfo();
            info.devcode = code;
            info.verfycode = checkCode;
            info.name = name;
            info.norms = paper;
            info.remark = remarks;
            mPresenter.addPrinter(info);
        } else {
            printerInfo.devcode = code;
            printerInfo.verfycode = checkCode;
            printerInfo.name = name;
            printerInfo.norms = paper;
            printerInfo.remark = remarks;
            mPresenter.updatePrinter(printerInfo);
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
        String[] str = result.split("-");
        if(str.length == 2){
            etPrintCode.setText(str[0]);
            etPrintCheckCode.setText(str[1]);
            etName.requestFocus();
        }
    }

    @Override
    public void onHttpResult(boolean success, int code, Object data) {
        if(success){
            if(code == 3){
                Map<String,Object> map = new HashMap<>();
                map.put(getClass().getName(), true);
                notifyEffect(new MapEffect(map));
                popBackStack();
            }
        }
    }
}
