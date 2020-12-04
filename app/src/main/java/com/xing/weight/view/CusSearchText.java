package com.xing.weight.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import com.xing.weight.R;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

public class CusSearchText extends AppCompatEditText {

    private OnIconClick onIconClick;
    private Drawable[] drawables;

    public CusSearchText(Context context) {
        super(context);
        init();
    }

    public CusSearchText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CusSearchText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        drawables = this.getCompoundDrawables();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(onIconClick == null || drawables == null){
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                float x = event.getRawX();
                float y = event.getRawY();
                for (int i = 0; i < drawables.length; i++) {
                    if(callTouch(x,y,drawables[i],i)){
                        onIconClick.onIconClick(i);
                        return true;
                    }
                }
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean callTouch(float x, float y, Drawable drawable, int index){
        if(drawable == null){
            return false;
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        if(index == 0){ //左
            if(x >= getPaddingStart()  && x <= (getPaddingStart() + w)){
                if(y >= getPaddingTop()  && x <= (getPaddingTop() + h)){
                    return true;
                }
            }
        } else if(index == 1){ //上
            int left = (getWidth() - w)>>1;  //假设居中
            if(x >= left && x <= (left + w)){
                if(y >= getPaddingTop()  && x <= (getPaddingTop() + h)){
                    return true;
                }
            }
        } else if(index == 2){ //右
            int left = getWidth() - getPaddingEnd() - w;
            if(x >= left  && x <= (left + w)){
                if(y >= getPaddingTop()  && x <= (getPaddingTop() + h)){
                    return true;
                }
            }
        } else { //下
            int left = (getWidth() - w)>>1;
            int top = (getHeight() - h - getPaddingBottom());
            if(x >= left  && x <= (left + w)){
                if(y >= top  && x <= (top + h)){
                    return true;
                }
            }
        }
        return false;
    }



    public OnIconClick getOnIconClick() {
        return onIconClick;
    }

    public void setOnIconClick(OnIconClick onIconClick) {
        this.onIconClick = onIconClick;
    }

    public interface OnIconClick {
        void onIconClick(int type);
    }

}
