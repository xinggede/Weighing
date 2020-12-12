package com.xing.weight.fragment.main.manage.mode;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qmuiteam.qmui.recyclerView.QMUISwipeAction;
import com.qmuiteam.qmui.recyclerView.QMUISwipeViewHolder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.xing.weight.R;
import com.xing.weight.base.BaseRecyclerAdapter;
import com.xing.weight.bean.CustomerInfo;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class MyCustomAdapter extends RecyclerView.Adapter<QMUISwipeViewHolder> {

    private List<CustomerInfo> mData = new ArrayList<>();
    public final QMUISwipeAction mDeleteAction;
    private BaseRecyclerAdapter.OnItemClickListener mClickListener;

    public MyCustomAdapter(Context context) {
        QMUISwipeAction.ActionBuilder builder = new QMUISwipeAction.ActionBuilder()
                .textSize(QMUIDisplayHelper.sp2px(context, 14))
                .textColor(Color.WHITE)
                .paddingStartEnd(QMUIDisplayHelper.dp2px(context, 20));

        mDeleteAction = builder.text("删除").backgroundColor(Color.RED).build();
    }

    public void setData(@Nullable List<CustomerInfo> list) {
        mData.clear();
        if (list != null) {
            mData.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void remove(int pos) {
        mData.remove(pos);
        notifyItemRemoved(pos);
    }

    public void add(int pos, CustomerInfo item) {
        mData.add(pos, item);
        notifyItemInserted(pos);
    }

    public void prepend(@NonNull List<CustomerInfo> items) {
        mData.addAll(0, items);
        notifyDataSetChanged();
    }

    public void append(@NonNull List<CustomerInfo> items) {
        mData.addAll(items);
        notifyDataSetChanged();
    }

    public CustomerInfo getItem(int position){
        return mData.get(position);
    }

    @NonNull
    @Override
    public QMUISwipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_custom, parent, false);
        final QMUISwipeViewHolder vh = new QMUISwipeViewHolder(view);
        vh.addSwipeAction(mDeleteAction);
        if (mClickListener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(view, vh.getAdapterPosition());
                }
            });
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull QMUISwipeViewHolder holder, int position) {
        CustomerInfo info = getItem(position);
        TextView tvCompanyName = holder.itemView.findViewById(R.id.tv_company_name);
        tvCompanyName.setText(info.comname);

        TextView tvName = holder.itemView.findViewById(R.id.tv_name);
        tvName.setText(info.name+": " + info.phone);

        TextView tvAddress = holder.itemView.findViewById(R.id.tv_address);
        tvAddress.setText(info.address);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnItemClickListener(BaseRecyclerAdapter.OnItemClickListener listener) {
        mClickListener = listener;
    }
}
