package com.xing.weight.fragment.main.manage.mode;

import android.content.Context;
import com.xing.weight.R;
import com.xing.weight.base.BaseRecyclerAdapter;
import com.xing.weight.base.RecyclerViewHolder;
import com.xing.weight.bean.StyleInfo;
import com.xing.weight.util.MyImageLoader;

import java.util.List;

import androidx.annotation.Nullable;

public class StyleAdapter extends BaseRecyclerAdapter<StyleInfo> {

    public StyleAdapter(Context ctx, @Nullable List<StyleInfo> list) {
        super(ctx, list);
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.list_item_style;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, StyleInfo item) {
        holder.setText(R.id.tv_name,item.name);
        holder.setText(R.id.tv_date,item.modifyDate);
        MyImageLoader.loadImage(mContext,item.url, holder.getImageView(R.id.iv_content), R.mipmap.img_default);
    }
}
