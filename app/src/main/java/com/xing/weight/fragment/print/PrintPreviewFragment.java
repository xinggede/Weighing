package com.xing.weight.fragment.print;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.qmuiteam.qmui.arch.effect.MapEffect;
import com.qmuiteam.qmui.arch.effect.QMUIFragmentMapEffectHandler;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.base.Constants;
import com.xing.weight.bean.PoundItemInfo;
import com.xing.weight.bean.StyleInfo;
import com.xing.weight.bean.TemplateInfo;
import com.xing.weight.fragment.bill.mode.BillContract;
import com.xing.weight.fragment.bill.mode.BillPresenter;
import com.xing.weight.fragment.main.manage.StyleChooseFragment;
import com.xing.weight.view.CusTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.OnClick;

public class PrintPreviewFragment extends BaseFragment<BillPresenter> implements BillContract.View {

    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;

    public PrintPreviewFragment(TemplateInfo templateInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.DATA, templateInfo);
        setArguments(bundle);
    }

    @Override
    protected BillPresenter onLoadPresenter() {
        return new BillPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_print_preview;
    }

    @Override
    protected void initView(View view) {
        topbar.setTitle("打印预览");
        topbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

    }

    @Override
    public void onHttpResult(boolean success, int code, Object data) {
        if(success){
            Map<String,Object> map = new HashMap<>();
            map.put(getClass().getName(), true);
            notifyEffect(new MapEffect(map));
            popBackStack();
        }
    }
}
