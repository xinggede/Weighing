package com.xing.weight.fragment.bill.pound;

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
import com.xing.weight.bean.PoundModel;
import com.xing.weight.fragment.bill.BillRecordFragment;
import com.xing.weight.fragment.bill.mode.BillContract;
import com.xing.weight.fragment.bill.mode.BillPresenter;
import com.xing.weight.fragment.bill.mode.WeightInputAdapter;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class WeightInputFragment1 extends BaseFragment<BillPresenter> implements BillContract.View, BaseRecyclerAdapter.OnChildClickListener {

    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.tv_model)
    TextView tvModel;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private WeightInputAdapter inputAdapter;

    private QMUIPopup chooseGoodsPopup, chooseCustomPopup, chooseModel;

    @Override
    protected BillPresenter onLoadPresenter() {
        return new BillPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_weight_input1;
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
        inputAdapter = new WeightInputAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(inputAdapter);
        inputAdapter.setOnChildClickListener(this);
    }

    private void showChooseModel(View v) {
        if (chooseModel == null) {
            ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.list_item_choose, mPresenter.getSaveGoods());
            AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (chooseModel != null) {
                        chooseModel.dismiss();
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
                showChooseGoods(gView);
            } else if (code == 1) {
                showChooseCustom(cView);
            }
        }
    }

    @OnClick({R.id.tv_model})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_model:
                showChooseModel(view);
                break;
        }
    }

    private View gView,cView;

    @Override
    public void onChildClick(View v, int pos) {
        PoundModel poundModel = inputAdapter.getItem(pos);
        if(poundModel.type == PoundModel.PoundType.CNAME){
            cView = v;
            showChooseCustom(v);
        } else if(poundModel.type == PoundModel.PoundType.GTYPE){
            gView = v;
            if(mPresenter.getSaveGoods().isEmpty()){
                mPresenter.getGoods();
            } else {
                showChooseGoods(v);
            }
        } else if(poundModel.type == PoundModel.PoundType.ADD){

        }
    }
}
