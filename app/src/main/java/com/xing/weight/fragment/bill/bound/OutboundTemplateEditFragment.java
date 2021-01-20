package com.xing.weight.fragment.bill.bound;

import android.os.Bundle;
import android.view.View;

import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.base.Constants;
import com.xing.weight.bean.PoundItemInfo;
import com.xing.weight.bean.TemplateInfo;
import com.xing.weight.fragment.bill.mode.BillContract;
import com.xing.weight.fragment.bill.mode.BillPresenter;
import com.xing.weight.fragment.bill.mode.BoundTemplateAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class OutboundTemplateEditFragment extends BaseFragment<BillPresenter> implements BillContract.View {


    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    TemplateInfo templateInfo;
    private BoundTemplateAdapter boundTemplateAdapter;

    public OutboundTemplateEditFragment(TemplateInfo templateInfo) {
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
        return R.layout.fragment_outbound_template_edit;
    }

    @Override
    protected void initView(View view) {
        templateInfo = getArguments().getParcelable(Constants.DATA);
        topbar.setTitle("出库单定制");
        topbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        boundTemplateAdapter = new BoundTemplateAdapter(getContext(), initData());
        recyclerView.setAdapter(boundTemplateAdapter);
    }

    private List<PoundItemInfo> initData(){
        if(templateInfo != null){
            return templateInfo.contList;
        }
        List<PoundItemInfo> data = new ArrayList<>();
        PoundItemInfo itemInfo = new PoundItemInfo("模板名称：");
        itemInfo.hint = "请输入模板名称";
        itemInfo.type = PoundItemInfo.PoundType.MODELNAME;
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("公司名称：");
        itemInfo.hint = "自动加载";
        itemInfo.type = PoundItemInfo.PoundType.CNAME;
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("负责人：");
        itemInfo.hint = "自动加载";
        itemInfo.type = PoundItemInfo.PoundType.CBOSS;
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("经手人：");
        itemInfo.hint = "手动输入";
        itemInfo.type = PoundItemInfo.PoundType.JSR;
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("联系方式：");
        itemInfo.hint = "自动加载";
        itemInfo.inputType = 2;
        itemInfo.type = PoundItemInfo.PoundType.CPHONE;
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("联系地址：");
        itemInfo.hint = "自动加载";
        itemInfo.lenght = 50;
        itemInfo.type = PoundItemInfo.PoundType.CADDRESS;
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("订单号：");
        itemInfo.hint = "自动生成";
        itemInfo.type = PoundItemInfo.PoundType.ORDERNUMBER;
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("序号：");
        itemInfo.hint = "自动生成";
        itemInfo.type = PoundItemInfo.PoundType.SERIALNUMBER;
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("录单日期：");
        itemInfo.hint = "手动选择";
        itemInfo.type = PoundItemInfo.PoundType.OUTTIME;
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("收货单位：");
        itemInfo.hint = "手动选择";
        itemInfo.type = PoundItemInfo.PoundType.RECEIVENAME;
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("联系人：");
        itemInfo.hint = "自动加载";
        itemInfo.type = PoundItemInfo.PoundType.RECEIVECONTACTS;
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("联系方式：");
        itemInfo.hint = "自动加载";
        itemInfo.inputType = 2;
        itemInfo.type = PoundItemInfo.PoundType.RECEIVEPHONE;
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("联系地址：");
        itemInfo.hint = "自动加载";
        itemInfo.lenght = 50;
        itemInfo.type = PoundItemInfo.PoundType.RECEIVEADDRESS;
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("客户签收：");
        itemInfo.hint = "手动输入";
        itemInfo.type = PoundItemInfo.PoundType.QSR;
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("送货方式：");
        itemInfo.hint = "手动输入";
        itemInfo.lenght = 50;
        itemInfo.type = PoundItemInfo.PoundType.SHFS;
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("备注：");
        itemInfo.hint = "手动输入";
        itemInfo.lenght = 200;
        itemInfo.inputType = 3;
        itemInfo.type = PoundItemInfo.PoundType.REMARKS;
        data.add(itemInfo);

        return data;
    }

    @Override
    public void onHttpResult(boolean success, int code, Object data) {

    }
}
