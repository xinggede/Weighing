package com.xing.weight.fragment.bill.pound;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.base.BaseRecyclerAdapter;
import com.xing.weight.bean.CompanyInfo;
import com.xing.weight.bean.CustomerInfo;
import com.xing.weight.bean.GoodsDetail;
import com.xing.weight.bean.PoundInfo;
import com.xing.weight.bean.PoundItemInfo;
import com.xing.weight.bean.TemplateInfo;
import com.xing.weight.fragment.bill.BillRecordFragment;
import com.xing.weight.fragment.bill.mode.BillContract;
import com.xing.weight.fragment.bill.mode.BillPresenter;
import com.xing.weight.fragment.bill.mode.PoundInputAdapter;
import com.xing.weight.util.Tools;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;


public class PoundInputFragment extends BaseFragment<BillPresenter> implements BillContract.View, BaseRecyclerAdapter.OnChildClickListener {

    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.tv_model)
    TextView tvModel;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private PoundInputAdapter inputAdapter;

    private QMUIPopup chooseGoodsPopup, chooseCustomPopup, chooseModel;
    private TemplateInfo templateInfo;
    private CompanyInfo companyInfo;

    @Override
    protected BillPresenter onLoadPresenter() {
        return new BillPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pound_input;
    }

    @Override
    protected void initView(View view) {
        topbar.setTitle("入场录单");
        topbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        topbar.addRightImageButton(R.mipmap.icon_menu, R.id.topbar_right_menu_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startFragment(new BillRecordFragment());
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        inputAdapter = new PoundInputAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(inputAdapter);
        inputAdapter.setOnChildClickListener(this);

        mPresenter.getTemplateChoose(1, false);

        companyInfo = mPresenter.getCompanyInfo();
    }

    private void setTemp(){
        tvModel.setText(templateInfo.name);
        List<PoundItemInfo> data = new ArrayList<>();
        data.addAll(templateInfo.contList);
        for (PoundItemInfo itemInfo : data) {
            if(itemInfo.type == PoundItemInfo.PoundType.CNAME){
                itemInfo.value = companyInfo.comname;
                continue;
            }
            if(itemInfo.type == PoundItemInfo.PoundType.CBOSS){
                itemInfo.value = companyInfo.boss;
                continue;
            }
        }
        PoundItemInfo itemInfo = new PoundItemInfo("添加");
        itemInfo.type = PoundItemInfo.PoundType.ADD;
        data.add(itemInfo);
        inputAdapter.setData(data);
    }

    private void showChooseModel(View v) {
        if (chooseModel == null) {
            ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.list_item_choose, mPresenter.getSaveTemps());
            AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (chooseModel != null) {
                        chooseModel.dismiss();
                    }
                    templateInfo = (TemplateInfo) adapterView.getItemAtPosition(i);
                    setTemp();
                }
            };
            chooseModel = QMUIPopups.listPopup(getContext(),
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
        chooseModel.show(v);
    }


    private void showChooseGoods(View v) {
        if (chooseGoodsPopup == null) {
            ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.list_item_choose, mPresenter.getSaveGoods());
            AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (chooseGoodsPopup != null) {
                        chooseGoodsPopup.dismiss();
                    }
                    GoodsDetail detail = (GoodsDetail) adapterView.getItemAtPosition(i);
                    inputAdapter.updateGoods(detail);
                }
            };
            chooseGoodsPopup = QMUIPopups.listPopup(getContext(),
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
        chooseGoodsPopup.show(v);
    }

    private void showChooseCustom(View v) {
        if (chooseCustomPopup == null) {
            ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.list_item_choose, mPresenter.getSaveCustomer());
            AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (chooseCustomPopup != null) {
                        chooseCustomPopup.dismiss();
                    }
                    CustomerInfo info = (CustomerInfo) adapterView.getItemAtPosition(i);
                    inputAdapter.updateCustom(info);

                }
            };
            chooseCustomPopup = QMUIPopups.listPopup(getContext(),
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
        chooseCustomPopup.show(v);
    }

    @Override
    public void onHttpResult(boolean success, int code, Object data) {
        if (success) {
            if (code == 0) {
                if (!mPresenter.getSaveTemps().isEmpty()) {
                    templateInfo = mPresenter.getSaveTemps().get(0);
                    setTemp();
                }
            } else if (code == 1) {
                if (!mPresenter.getSaveTemps().isEmpty()) {
                    showChooseModel(tvModel);
                } else {
                    showToast("请先添加模板");
                }
            } else if (code == 2) {
                if (!mPresenter.getSaveCustomer().isEmpty()) {
                    showChooseCustom(cView);
                } else {
                    showToast("请先添加客户");
                }
            } else if (code == 3) {
                if (!mPresenter.getSaveGoods().isEmpty()) {
                    showChooseGoods(gView);
                } else {
                    showToast("请先添加货品");
                }
            } else if (code == 4) {

            }
        }
    }

    @OnClick({R.id.tv_model})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_model:
                if (mPresenter.getSaveTemps().isEmpty()) {
                    mPresenter.getTemplateChoose(1, true);
                } else {
                    showChooseModel(view);
                }
                break;
        }
    }

    private View gView, cView;

    @Override
    public void onChildClick(View v, int pos) {
        PoundItemInfo poundItemInfo = inputAdapter.getItem(pos);
        if (poundItemInfo.type == PoundItemInfo.PoundType.RECEIVENAME) {
            cView = v;
            if (mPresenter.getSaveCustomer().isEmpty()) {
                mPresenter.getCustom();
            } else {
                showChooseCustom(v);
            }
        } else if (poundItemInfo.type == PoundItemInfo.PoundType.GTYPE) {
            gView = v;
            if (mPresenter.getSaveGoods().isEmpty()) {
                mPresenter.getGoods();
            } else {
                showChooseGoods(v);
            }
        } else if (poundItemInfo.type == PoundItemInfo.PoundType.ADD) {
            save();
        }
    }

    private void save() {
        PoundInfo info = checkPoundInfo(inputAdapter.getData());
        if(info!= null){
            mPresenter.addPound(info);
        }
    }

    private PoundInfo checkPoundInfo(List<PoundItemInfo> data) {
        PoundInfo poundInfo = new PoundInfo();
        for (PoundItemInfo info : data) {
            if(TextUtils.isEmpty(info.value) && info.type != PoundItemInfo.PoundType.REMARKS && info.type != PoundItemInfo.PoundType.ADD){
                showToast(String.format(getText(R.string.pls_input).toString(), info.name));
                return null;
            }
            switch (info.type) {
                case CODE:
                    poundInfo.code = info.value;
                    break;

                case CNAME:
                    poundInfo.title = info.value;
                    break;

                case CBOSS:
                    poundInfo.shipper = info.value;
                    break;

                case CPHONE:

                    break;

                case CADDRESS:

                    break;

                case GTYPE:
                    poundInfo.cargotype = info.value;
                    break;

                case CARWEIGHT:
                    poundInfo.truckweight = Tools.stringToDouble2(info.value);
                    break;

                case INTIME:
                    poundInfo.indate = info.value;
                    break;

                case OUTTIME:
                    poundInfo.outdate = info.value;
                    break;

                case TOTALWEIGHT:
                    poundInfo.allupweight = Tools.stringToDouble2(info.value);
                    break;

                case REALWEIGHT:
                    poundInfo.cargoweight = Tools.stringToDouble2(info.value);
                    break;

                case DISCOUNT:
                    poundInfo.percent = Tools.stringToDouble2(info.value);
                    break;

                case PRICE:
                    poundInfo.price = Tools.stringToDouble2(info.value);
                    break;

                case TOTALPRICE:
                    poundInfo.total = Tools.stringToDouble2(info.value);
                    break;

                case PERSON:
                    poundInfo.weighman = info.value;
                    break;

                case RECEIVENAME:
                    poundInfo.receiver = info.value;
                    break;

                case DRIVERCODE:
                    poundInfo.truckno = info.value;
                    break;

                case DRIVER:
                    poundInfo.driver = info.value;
                    break;

                case REMARKS:
                    poundInfo.remark = info.value;
                    break;

                default:
                    break;

            }
        }
        poundInfo.templetid = templateInfo.id;
        poundInfo.styleid = templateInfo.styleid;
        return poundInfo;
    }
}
