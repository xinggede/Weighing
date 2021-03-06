package com.xing.weight.fragment.bill.pound;

import android.os.Bundle;
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
import com.xing.weight.base.Constants;
import com.xing.weight.bean.AddPoundResultInfo;
import com.xing.weight.bean.CustomerInfo;
import com.xing.weight.bean.GoodsDetail;
import com.xing.weight.bean.PoundInfo;
import com.xing.weight.bean.PoundItemInfo;
import com.xing.weight.bean.TemplateInfo;
import com.xing.weight.fragment.bill.mode.BillContract;
import com.xing.weight.fragment.bill.mode.BillPresenter;
import com.xing.weight.fragment.bill.mode.PoundInputAdapter;
import com.xing.weight.fragment.print.PrintPreviewFragment;
import com.xing.weight.util.Tools;
import com.xing.weight.view.datepicker.CustomDatePicker;
import com.xing.weight.view.datepicker.DateFormatUtils;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class PoundEditFragment extends BaseFragment<BillPresenter> implements BillContract.View, BaseRecyclerAdapter.OnChildClickListener {

    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.tv_model)
    TextView tvModel;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private PoundInputAdapter inputAdapter;

    private QMUIPopup chooseGoodsPopup, chooseCustomPopup;
    private TemplateInfo templateInfo;

    private CustomDatePicker mDatePicker;
    private PoundInfo poundInfo;

    public PoundEditFragment(PoundInfo poundInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.DATA, poundInfo);
        setArguments(bundle);
    }

    @Override
    protected BillPresenter onLoadPresenter() {
        return new BillPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pound_edit;
    }

    @Override
    protected void initView(View view) {
        poundInfo = getArguments().getParcelable(Constants.DATA);
        topbar.setTitle("磅单编辑");
        topbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        inputAdapter = new PoundInputAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(inputAdapter);
        inputAdapter.setOnChildClickListener(this);

        if (poundInfo != null) {
            mPresenter.getTemplateChoose(1, false);
        } else {
            showToast("模版异常,无法编辑");
            popBackStack();
        }
    }

    private void setTemp() {
        if (!mPresenter.getSaveTemps().isEmpty()) {
            for (TemplateInfo saveTemp : mPresenter.getSaveTemps()) {
                if (saveTemp.id == poundInfo.templetid) {
                    templateInfo = saveTemp;
                    break;
                }
            }
        }
        if (templateInfo == null) {
            showToast("模版异常,无法编辑");
            popBackStack();
            return;
        }
        tvModel.setText(templateInfo.name);
        List<PoundItemInfo> data = new ArrayList<>();
        data.addAll(templateInfo.contList);
        for (PoundItemInfo info : data) {
            switch (info.type) {
                case CODE:
                    info.value = poundInfo.code;
                    break;

                case CNAME:
                    info.value = poundInfo.title;
                    break;

                case CBOSS:
                    info.value = poundInfo.shipper;
                    break;

                case GTYPE:
                    info.value = poundInfo.cargotype;
                    break;

                case CARWEIGHT:
                    info.value = String.valueOf(poundInfo.truckweight);
                    break;

                case INTIME:
                    info.value = poundInfo.indate;
                    break;

                case OUTTIME:
                    info.value = poundInfo.outdate;
                    break;

                case TOTALWEIGHT:
                    info.value = String.valueOf(poundInfo.allupweight);
                    break;

                case REALWEIGHT:
                    info.value = String.valueOf(poundInfo.cargoweight);
                    break;

                case DISCOUNT:
                    info.value = String.valueOf(poundInfo.percent);
                    break;

                case PRICE:
                    info.value = String.valueOf(poundInfo.price);
                    break;

                case TOTALPRICE:
                    info.value = String.valueOf(poundInfo.total);
                    break;

                case PERSON:
                    info.value = poundInfo.weighman;
                    break;

                case RECEIVENAME:
                    info.value = poundInfo.receiver;
                    break;

                case DRIVERCODE:
                    info.value = poundInfo.truckno;
                    break;

                case DRIVER:
                    info.value = poundInfo.driver;
                    break;

                case REMARKS:
                    info.value = poundInfo.remark;
                    break;

                default:
                    break;
            }
        }
        PoundItemInfo itemInfo = new PoundItemInfo("添加");
        itemInfo.type = PoundItemInfo.PoundType.ADD;
        data.add(itemInfo);
        inputAdapter.setData(data);
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
                    .preferredDirection(QMUIPopup.DIRECTION_BOTTOM)
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
                    .preferredDirection(QMUIPopup.DIRECTION_BOTTOM)
                    .shadow(true)
                    .offsetYIfTop(QMUIDisplayHelper.dp2px(getContext(), 5));
        }
        chooseCustomPopup.show(v);
    }

    @Override
    public void onHttpResult(boolean success, int code, Object data) {
        if (success) {
            if (code == 0) {
                setTemp();
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
                AddPoundResultInfo info = (AddPoundResultInfo) data;
                startFragmentAndDestroyCurrent(new PrintPreviewFragment(info,1));
            }
        }
    }

    private void showDatePicker(String time, PoundItemInfo.PoundType type) {
        long beginTime = DateFormatUtils.getBeforeYear();
        long endTime = DateFormatUtils.getLastWeek();
        if (mDatePicker == null) {
            mDatePicker = new CustomDatePicker(getContext(), new CustomDatePicker.Callback() {
                @Override
                public void onTimeSelected(long timestamp) {
                    inputAdapter.updateTime((PoundItemInfo.PoundType) mDatePicker.getTag(), DateFormatUtils.long2Str(timestamp, true));
                }
            }, beginTime, endTime);
            mDatePicker.setCancelable(true);
            // 显示时和分
            mDatePicker.setCanShowPreciseTime(true);
            // 允许循环滚动
            mDatePicker.setScrollLoop(true);
            mDatePicker.setCanShowAnim(true);
        }
        mDatePicker.setTag(type);
        mDatePicker.show(time);
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
        } else if (poundItemInfo.type == PoundItemInfo.PoundType.INTIME || poundItemInfo.type == PoundItemInfo.PoundType.OUTTIME) {
            showDatePicker(poundItemInfo.value, poundItemInfo.type);
        } else if (poundItemInfo.type == PoundItemInfo.PoundType.ADD) {
            save();
        }
    }

    private void save() {
        PoundInfo info = checkPoundInfo(inputAdapter.getData());
        if (info != null) {
            mPresenter.editPound(info);
        }
    }

    private PoundInfo checkPoundInfo(List<PoundItemInfo> data) {
        PoundInfo poundInfo = new PoundInfo();
        for (PoundItemInfo info : data) {
            if (TextUtils.isEmpty(info.value) && info.type != PoundItemInfo.PoundType.REMARKS && info.type != PoundItemInfo.PoundType.ADD) {
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
        poundInfo.id = this.poundInfo.id;
        return poundInfo;
    }
}
