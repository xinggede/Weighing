package com.xing.weight.fragment.main.my;

import android.view.View;
import android.view.ViewGroup;

import com.qmuiteam.qmui.arch.effect.MapEffect;
import com.qmuiteam.qmui.arch.effect.QMUIFragmentMapEffectHandler;
import com.qmuiteam.qmui.recyclerView.QMUIRVItemSwipeAction;
import com.qmuiteam.qmui.recyclerView.QMUISwipeAction;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout;
import com.xing.weight.R;
import com.xing.weight.base.BaseFragment;
import com.xing.weight.base.BaseRecyclerAdapter;
import com.xing.weight.bean.PageList;
import com.xing.weight.bean.PrinterInfo;
import com.xing.weight.fragment.main.my.mode.MyContract;
import com.xing.weight.fragment.main.my.mode.MyPresenter;
import com.xing.weight.fragment.main.my.mode.PrintAdapter;
import com.xing.weight.view.SpaceItemDecoration;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout.PULL_EDGE_TOP;

public class PrintManageFragment extends BaseFragment<MyPresenter> implements MyContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.pull_layout)
    QMUIPullLayout pullLayout;
    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;

    private PrintAdapter mAdapter;
    private QMUIPullLayout.PullAction mPullAction;
    private int deleteIndex;

    @Override
    protected MyPresenter onLoadPresenter() {
        return new MyPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initView(View view) {
        topbar.setTitle(R.string.print_manage);
        topbar.addLeftBackImageButton().setOnClickListener((v)->{
            popBackStack();
        });
        topbar.addRightImageButton(R.mipmap.icon_add, R.id.topbar_right_add_button).setOnClickListener((v)->{
            startFragment(new PrintAddFragment(null));
        });

        pullLayout.setEnabledEdges(PULL_EDGE_TOP);

        pullLayout.setActionListener(new QMUIPullLayout.ActionListener() {
            @Override
            public void onActionTriggered(@NonNull QMUIPullLayout.PullAction pullAction) {
                mPullAction = pullAction;
                if (pullAction.getPullEdge() == QMUIPullLayout.PULL_EDGE_TOP) {
                    onRefreshData();
                }
            }
        });

        QMUIRVItemSwipeAction swipeAction = new QMUIRVItemSwipeAction(true, new QMUIRVItemSwipeAction.Callback() {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                delete(viewHolder.getAdapterPosition());
            }

            @Override
            public int getSwipeDirection(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return QMUIRVItemSwipeAction.SWIPE_LEFT;
            }

            @Override
            public void onClickAction(QMUIRVItemSwipeAction swipeAction, RecyclerView.ViewHolder selected, QMUISwipeAction action) {
                super.onClickAction(swipeAction, selected, action);
                if(action == mAdapter.mDeleteAction){
                    delete(selected.getAdapterPosition());
                } else{

                }
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

        recyclerView.addItemDecoration(new SpaceItemDecoration(QMUIDisplayHelper.dp2px(getContext(),10), 2));
        mAdapter = new PrintAdapter(getContext());
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                startFragment(new PrintAddFragment(mAdapter.getItem(pos)));
            }
        });
        callback();
        onDataLoaded();
    }

    private void callback() {
        registerEffect(this, new QMUIFragmentMapEffectHandler() {
            @Override
            public boolean shouldHandleEffect(@NonNull MapEffect effect) {
                return effect.getValue(PrintAddFragment.class.getName()) != null;
            }

            @Override
            public void handleEffect(@NonNull MapEffect effect) {  //该方法只会在界面显示的时候才调用（主线程）
                boolean value = (boolean) effect.getValue(PrintAddFragment.class.getName());
                if (value) {
                    onDataLoaded();
                }
            }
        });
    }

    private void onDataLoaded() {
        mPresenter.getPrinter(true);
    }

    private void onRefreshData() {
        mPresenter.getPrinter(false);
    }

    private void delete(int position) {
        deleteIndex = position;
        PrinterInfo printerInfo = mAdapter.getItem(position);
        new QMUIDialog.MessageDialogBuilder(getActivity())
                .setTitle(printerInfo.name)
                .setMessage("确定要删除该打印机吗？")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        mPresenter.deletePrinter(printerInfo.id);
                    }
                }).create(R.style.DialogTheme2).show();
    }


    @Override
    public void onHttpResult(boolean success, int code, Object data) {
        if (mPullAction != null) {
            pullLayout.finishActionRun(mPullAction);
        }
        if(success){
            if(code == 0){
                PageList<PrinterInfo> pageList = (PageList<PrinterInfo>) data;
                mAdapter.setData(pageList.records);
                if(mAdapter.getItemCount()== 0){
                    startFragment(new PrintAddFragment(null));
                }
            } else if(code == 1){
                mAdapter.remove(deleteIndex);
            }
        }
    }
}
