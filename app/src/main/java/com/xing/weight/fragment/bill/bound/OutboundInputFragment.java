package com.xing.weight.fragment.bill.bound;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.qmuiteam.qmui.recyclerView.QMUIRVItemSwipeAction;
import com.qmuiteam.qmui.recyclerView.QMUISwipeAction;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.fragment.bill.PoundRecordFragment;
import com.xing.weight.fragment.bill.mode.GoodsAdapter;
import com.xing.weight.fragment.bill.mode.InputChangeListener;
import com.xing.weight.fragment.main.MainContract;
import com.xing.weight.fragment.main.MainPresenter;
import com.xing.weight.fragment.main.manage.MyCustomAddFragment;
import com.xing.weight.fragment.main.manage.MyGoodsListFragment;
import com.xing.weight.view.NestRecyclerView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class OutboundInputFragment extends BaseFragment<MainPresenter> implements MainContract.View, InputChangeListener {


    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_receive_name)
    TextView tvReceiveName;
    @BindView(R.id.tv_receive_address)
    TextView tvReceiveAddress;
    @BindView(R.id.et_create_name)
    EditText etCreateName;
    @BindView(R.id.et_handler_name)
    EditText etHandlerName;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.recyclerView)
    NestRecyclerView recyclerView;
    @BindView(R.id.tv_total_info)
    TextView tvTotalInfo;

    private QMUIPopup choosePopup;
    private GoodsAdapter adapter;

    @Override
    protected MainPresenter onLoadPresenter() {
        return new MainPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_outbound_input;
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
                startFragment(new PoundRecordFragment());
            }
        });

        QMUIRVItemSwipeAction swipeAction = new QMUIRVItemSwipeAction(true, new QMUIRVItemSwipeAction.Callback() {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.remove(viewHolder.getAdapterPosition());
            }

            @Override
            public int getSwipeDirection(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return QMUIRVItemSwipeAction.SWIPE_LEFT;
            }

            @Override
            public void onClickAction(QMUIRVItemSwipeAction swipeAction, RecyclerView.ViewHolder selected, QMUISwipeAction action) {
                super.onClickAction(swipeAction, selected, action);
                adapter.remove(selected.getAdapterPosition());
                swipeAction.clear();
            }
        });
        swipeAction.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        adapter = new GoodsAdapter(getContext(),new ArrayList<>());
        adapter.setInputChangeListener(this);
        recyclerView.setAdapter(adapter);
    }


    private void showChooseName(View v) {
        List<String> data = new ArrayList<>();
        data.add("矿砂");
        data.add("山石");
        data.add("土方");

        ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.list_item_choose, data);

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tvReceiveName.setText(adapterView.getItemAtPosition(i).toString());
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

    @OnClick({R.id.tv_receive_name, R.id.ib_add, R.id.bt_print, R.id.lin_add_goods})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_receive_name:
                showChooseName(view);
                break;

            case R.id.ib_add:
                startFragment(new MyCustomAddFragment(null));
                break;

            case R.id.bt_print:
                break;

            case R.id.lin_add_goods:
                startFragment(new MyGoodsListFragment());
                break;
        }
    }

    @Override
    public void onChange(String value) {

    }
}
