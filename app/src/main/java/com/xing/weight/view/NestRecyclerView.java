package com.xing.weight.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class NestRecyclerView extends RecyclerView {

    public NestRecyclerView(Context context) {
        super(context);
        init();
    }

    public NestRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NestRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setNestedScrollingEnabled(false);
        setFocusableInTouchMode(false);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int newHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        super.onMeasure(widthSpec, newHeightSpec);
    }
}
