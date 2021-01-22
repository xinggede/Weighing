package com.xing.weight.fragment.bill.bound;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.qmuiteam.qmui.arch.effect.MapEffect;
import com.qmuiteam.qmui.arch.effect.QMUIFragmentEffectHandler;
import com.qmuiteam.qmui.arch.effect.QMUIFragmentMapEffectHandler;
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
import com.xing.weight.bean.PoundItemInfo;
import com.xing.weight.bean.TemplateInfo;
import com.xing.weight.fragment.bill.mode.BillContract;
import com.xing.weight.fragment.bill.mode.BillPresenter;
import com.xing.weight.fragment.bill.mode.BoundInputAdapter;
import com.xing.weight.fragment.bill.mode.GoodsAdapter;
import com.xing.weight.fragment.bill.mode.PoundInputAdapter;
import com.xing.weight.fragment.main.manage.MyGoodsListFragment;
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

public class OutboundAddFragment extends BaseFragment<BillPresenter> implements BillContract.View, BaseRecyclerAdapter.OnChildClickListener {

    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.tv_model)
    TextView tvModel;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private BoundInputAdapter inputAdapter;
    private CompanyInfo companyInfo;
    private TemplateInfo templateInfo;

    private QMUIPopup chooseModel,chooseCustomPopup;
    private GoodsAdapter adapter;

    @Override
    protected BillPresenter onLoadPresenter() {
        return new BillPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_outbound_add;
    }

    @Override
    protected void initView(View view) {
        topbar.setTitle("出库录单");
        topbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        topbar.addRightImageButton(R.mipmap.icon_menu, R.id.topbar_right_menu_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        inputAdapter = new BoundInputAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(inputAdapter);
        inputAdapter.setOnChildClickListener(this);

        mPresenter.getTemplateChoose(2, false);
        companyInfo = mPresenter.getCompanyInfo();

        callbackTemplate();
        callbackAddGoods();
    }

    private void setTemp() {
        tvModel.setText(templateInfo.name);
        List<PoundItemInfo> data = new ArrayList<>();
        data.addAll(templateInfo.contList);
        for (PoundItemInfo itemInfo : data) {
            if(!TextUtils.isEmpty(itemInfo.value)){
                continue;
            }
            if (itemInfo.type == PoundItemInfo.PoundType.ORDERNUMBER || itemInfo.type == PoundItemInfo.PoundType.SERIALNUMBER) {
                itemInfo.value = String.valueOf(System.currentTimeMillis());
                continue;
            }
            if (itemInfo.type == PoundItemInfo.PoundType.CNAME) {
                itemInfo.value = companyInfo.comname;
                continue;
            }
            if (itemInfo.type == PoundItemInfo.PoundType.CBOSS) {
                itemInfo.value = companyInfo.boss;
                continue;
            }
            if (itemInfo.type == PoundItemInfo.PoundType.OUTTIME) {
                itemInfo.value = DateFormatUtils.getCurrentDate();
            }
        }
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
                    TemplateInfo temp = (TemplateInfo) adapterView.getItemAtPosition(i);
                    if(templateInfo == null || templateInfo.id != temp.id){
                        templateInfo = temp;
                        setTemp();
                    }
                }
            };
            chooseModel = QMUIPopups.listPopup(getContext(),
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
        chooseModel.show(v);
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

    private CustomDatePicker mDatePicker;

    private void showDatePicker(String time, PoundItemInfo.PoundType type){
        long beginTime = DateFormatUtils.getBeforeYear();
        long endTime = DateFormatUtils.getLastWeek();
        if(mDatePicker == null){
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
            }
        }
    }

    private View cView;

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
        }  else if (poundItemInfo.type == PoundItemInfo.PoundType.OUTTIME) {
            showDatePicker(poundItemInfo.value, poundItemInfo.type);
        }
    }

    @OnClick({R.id.ib_add, R.id.lin_add_goods, R.id.bt_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_add:
                startFragment(new OutboundTemplateEditFragment(null));
                break;

            case R.id.lin_add_goods:
                startFragment(new MyGoodsListFragment(true));
                break;

            case R.id.bt_ok:

                break;
        }
    }

    private void callbackTemplate() {
        registerEffect(this, new QMUIFragmentMapEffectHandler() {
            @Override
            public boolean shouldHandleEffect(@NonNull MapEffect effect) {
                return effect.getValue(OutboundTemplateEditFragment.class.getName()) != null;
            }

            @Override
            public void handleEffect(@NonNull MapEffect effect) {
                boolean value = (boolean) effect.getValue(OutboundTemplateEditFragment.class.getName());
                if (value) {
                    mPresenter.getTemplateChoose(2, false);
                }
            }
        });
    }

    private void callbackAddGoods() {
        registerEffect(this, new QMUIFragmentEffectHandler<GoodsDetail>() {
            @Override
            public boolean shouldHandleEffect(@NonNull GoodsDetail effect) {
                return false;
            }

            @Override
            public void handleEffect(@NonNull GoodsDetail effect) {

            }
        });
    }
}
