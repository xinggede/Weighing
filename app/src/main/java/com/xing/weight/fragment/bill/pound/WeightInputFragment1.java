package com.xing.weight.fragment.bill.pound;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.fragment.bill.BillRecordFragment;
import com.xing.weight.fragment.bill.mode.BillContract;
import com.xing.weight.fragment.bill.mode.BillPresenter;

import androidx.core.content.ContextCompat;
import butterknife.BindView;

public class WeightInputFragment1 extends BaseFragment<BillPresenter> implements BillContract.View {


    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;

    private QMUIPopup chooseGoodsPopup, chooseCustomPopup;

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
                showChooseGoods(null);
            } else if (code == 1) {
                showChooseCustom(null);
            }
        }
    }
}
