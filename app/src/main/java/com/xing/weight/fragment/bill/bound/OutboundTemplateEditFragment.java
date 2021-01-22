package com.xing.weight.fragment.bill.bound;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.qmuiteam.qmui.arch.effect.MapEffect;
import com.qmuiteam.qmui.arch.effect.QMUIFragmentMapEffectHandler;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.base.BaseRecyclerAdapter;
import com.xing.weight.base.Constants;
import com.xing.weight.bean.PoundItemInfo;
import com.xing.weight.bean.StyleInfo;
import com.xing.weight.bean.TemplateInfo;
import com.xing.weight.fragment.bill.mode.BillContract;
import com.xing.weight.fragment.bill.mode.BillPresenter;
import com.xing.weight.fragment.bill.mode.BoundTemplateAdapter;
import com.xing.weight.fragment.main.manage.StyleChooseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class OutboundTemplateEditFragment extends BaseFragment<BillPresenter> implements BillContract.View, BaseRecyclerAdapter.OnChildClickListener {


    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    TemplateInfo templateInfo;
    private BoundTemplateAdapter boundTemplateAdapter;
    private StyleInfo styleInfo;

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
        boundTemplateAdapter.setOnChildClickListener(this);
    }

    private List<PoundItemInfo> initData(){
        String modelName=null,styleName = null;
        HashMap<String, String> map = new HashMap<>();
        if(templateInfo != null){
            modelName = templateInfo.name;
            styleName = templateInfo.stylename;
            styleInfo = new StyleInfo();
            styleInfo.id = templateInfo.styleid;
            styleInfo.name = templateInfo.stylename;

            for (PoundItemInfo poundItemInfo : templateInfo.contList) {
                map.put(poundItemInfo.type.name(), poundItemInfo.value);
            }
        }

        List<PoundItemInfo> data = new ArrayList<>();
        PoundItemInfo itemInfo = new PoundItemInfo("模板名称：");
        itemInfo.hint = "请输入模板名称";
        itemInfo.type = PoundItemInfo.PoundType.MODELNAME;
        itemInfo.value = modelName;
        itemInfo.isChecked = true;
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("模板样式：");
        itemInfo.hint = "请选择模板样式";
        itemInfo.type = PoundItemInfo.PoundType.MODELSTYLE;
        itemInfo.value = styleName;
        itemInfo.isChecked = true;
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("公司名称：");
        itemInfo.hint = "自动加载";
        itemInfo.type = PoundItemInfo.PoundType.CNAME;
        itemInfo.value = map.get(itemInfo.type.name());
        itemInfo.isChecked = true;
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("负责人：");
        itemInfo.hint = "自动加载";
        itemInfo.type = PoundItemInfo.PoundType.CBOSS;
        itemInfo.value = map.get(itemInfo.type.name());
        itemInfo.isChecked = true;
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("经手人：");
        itemInfo.hint = "手动输入";
        itemInfo.type = PoundItemInfo.PoundType.JSR;
        itemInfo.value = map.get(itemInfo.type.name());
        itemInfo.isChecked = true;
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("联系方式：");
        itemInfo.hint = "自动加载";
        itemInfo.inputType = 2;
        itemInfo.type = PoundItemInfo.PoundType.CPHONE;
        itemInfo.value = map.get(itemInfo.type.name());
        itemInfo.isChecked = true;
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("联系地址：");
        itemInfo.hint = "自动加载";
        itemInfo.lenght = 50;
        itemInfo.type = PoundItemInfo.PoundType.CADDRESS;
        itemInfo.value = map.get(itemInfo.type.name());
        itemInfo.isChecked = map.containsKey(itemInfo.type.name());
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("订单号：");
        itemInfo.hint = "自动生成";
        itemInfo.type = PoundItemInfo.PoundType.ORDERNUMBER;
        itemInfo.value = map.get(itemInfo.type.name());
        itemInfo.isChecked = true;
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("序号：");
        itemInfo.hint = "自动生成";
        itemInfo.type = PoundItemInfo.PoundType.SERIALNUMBER;
        itemInfo.value = map.get(itemInfo.type.name());
        itemInfo.isChecked = map.containsKey(itemInfo.type.name());
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("录单日期：");
        itemInfo.hint = "手动选择";
        itemInfo.type = PoundItemInfo.PoundType.OUTTIME;
        itemInfo.value = map.get(itemInfo.type.name());
        itemInfo.isChecked = true;
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("收货单位：");
        itemInfo.hint = "手动选择";
        itemInfo.type = PoundItemInfo.PoundType.RECEIVENAME;
        itemInfo.value = map.get(itemInfo.type.name());
        itemInfo.isChecked = map.containsKey(itemInfo.type.name());
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("联系人：");
        itemInfo.hint = "自动加载";
        itemInfo.type = PoundItemInfo.PoundType.RECEIVECONTACTS;
        itemInfo.value = map.get(itemInfo.type.name());
        itemInfo.isChecked = map.containsKey(itemInfo.type.name());
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("联系方式：");
        itemInfo.hint = "自动加载";
        itemInfo.inputType = 2;
        itemInfo.type = PoundItemInfo.PoundType.RECEIVEPHONE;
        itemInfo.value = map.get(itemInfo.type.name());
        itemInfo.isChecked = map.containsKey(itemInfo.type.name());
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("联系地址：");
        itemInfo.hint = "自动加载";
        itemInfo.lenght = 50;
        itemInfo.type = PoundItemInfo.PoundType.RECEIVEADDRESS;
        itemInfo.value = map.get(itemInfo.type.name());
        itemInfo.isChecked = map.containsKey(itemInfo.type.name());
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("客户签收：");
        itemInfo.hint = "手动输入";
        itemInfo.type = PoundItemInfo.PoundType.QSR;
        itemInfo.value = map.get(itemInfo.type.name());
        itemInfo.isChecked = true;
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("送货方式：");
        itemInfo.hint = "手动输入";
        itemInfo.lenght = 50;
        itemInfo.type = PoundItemInfo.PoundType.SHFS;
        itemInfo.value = map.get(itemInfo.type.name());
        itemInfo.isChecked = map.containsKey(itemInfo.type.name());
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("备注：");
        itemInfo.hint = "手动输入";
        itemInfo.lenght = 200;
        itemInfo.inputType = 3;
        itemInfo.type = PoundItemInfo.PoundType.REMARKS;
        itemInfo.value = map.get(itemInfo.type.name());
        itemInfo.isChecked = map.containsKey(itemInfo.type.name());
        data.add(itemInfo);

        itemInfo = new PoundItemInfo("保存");
        itemInfo.type = PoundItemInfo.PoundType.ADD;
        data.add(itemInfo);

        return data;
    }

    private void chooseStyle(){
        callback();
        startFragment(new StyleChooseFragment(2));
    }

    private void callback() {
        registerEffect(this, new QMUIFragmentMapEffectHandler() {
            @Override
            public boolean shouldHandleEffect(@NonNull MapEffect effect) {
                return effect.getValue(StyleChooseFragment.class.getName()) != null;
            }

            @Override
            public void handleEffect(@NonNull MapEffect effect) {  //该方法只会在界面显示的时候才调用（主线程）
                styleInfo = (StyleInfo) effect.getValue(StyleChooseFragment.class.getName());
                if (styleInfo !=  null) {
                    boundTemplateAdapter.setModelStyle(styleInfo.name);
                }
            }
        });
    }


    @Override
    public void onChildClick(View v, int pos) {
        PoundItemInfo poundItemInfo = boundTemplateAdapter.getItem(pos);
        if (poundItemInfo.type == PoundItemInfo.PoundType.MODELSTYLE) {
            chooseStyle();
        } else if (poundItemInfo.type == PoundItemInfo.PoundType.MODELNAME) {
            if(TextUtils.isEmpty(poundItemInfo.value)){
                showToast(String.format(getText(R.string.pls_input).toString(), poundItemInfo.name));
                return;
            }
        } else if (poundItemInfo.type == PoundItemInfo.PoundType.ADD) {
            save();
        }
    }

    private void save() {
        PoundItemInfo poundItemInfo = boundTemplateAdapter.getItem(0);
        String modelName = poundItemInfo.value;
        if(TextUtils.isEmpty(modelName)){
            showToast(String.format(getText(R.string.pls_input).toString(), poundItemInfo.name));
            return;
        }

        if(styleInfo == null){
            showToast(R.string.pls_choose_model_style);
            return;
        }

        List<PoundItemInfo> list = boundTemplateAdapter.getChooseItem();

        if(templateInfo == null){
            TemplateInfo info = new TemplateInfo();
            info.name = modelName;
            info.contList = list;
            info.styleid = styleInfo.id;
            info.stylename = styleInfo.name;
            info.type = 2;
            mPresenter.addTemplate(info,true);
        } else {
            templateInfo.name = modelName;
            templateInfo.contList = list;
            templateInfo.type = 2;
            templateInfo.stylename = styleInfo.name;
            templateInfo.styleid = styleInfo.id;
            mPresenter.addTemplate(templateInfo,false);
        }
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
