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

public class CusEditText extends AppCompatEditText {

    private Drawable mRightDrawable;
    private boolean isHasFocus;
    private boolean isPwd = false;
    private boolean isVisible = false;
    private OnTextChanged onTextChanged;

    public CusEditText(Context context) {
        super(context);
        init();
    }

    public CusEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CusEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        Drawable[] drawables = this.getCompoundDrawables();

        // 取得right位置的Drawable
        // 即我们在布局文件中设置的android:drawableRight
        mRightDrawable = drawables[2];

        setClearDrawableVisible(false);
        this.setOnFocusChangeListener(new FocusChangeListenerImpl());
        // 设置EditText文字变化的监听
        this.addTextChangedListener(new TextWatcherImpl());
        // 初始化时让右边clean图标不可见
        setClearDrawableVisible(false);
        if (getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                || getInputType() == (InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD)) {
            isPwd = true;
            isVisible = false;
        } else {
            isPwd = false;
        }

        if (mRightDrawable == null) {
            mRightDrawable = ContextCompat.getDrawable(getContext(), isPwd ? R.mipmap.icon_hide : R.drawable.icon_popup_close_with_bg_dark);
        }
        mRightDrawable.setBounds(0, 0, mRightDrawable.getIntrinsicWidth(),
                mRightDrawable.getIntrinsicHeight());
    }

    /**
     * 当手指抬起的位置在clean的图标的区域 我们将此视为进行清除操作 getWidth():得到控件的宽度
     * event.getX():抬起时的坐标(改坐标是相对于控件本身而言的)
     * getTotalPaddingRight():clean的图标左边缘至控件右边缘的距离
     * getPaddingRight():clean的图标右边缘至控件右边缘的距离 于是: getWidth() -
     * getTotalPaddingRight()表示: 控件左边到clean的图标左边缘的区域 getWidth() -
     * getPaddingRight()表示: 控件左边到clean的图标右边缘的区域 所以这两者之间的区域刚好是clean的图标的区域
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                boolean touchable = (event.getX() > (getWidth() - getTotalPaddingRight()))
                        && (event.getX() < (getWidth() - getPaddingRight()));
                if (touchable) {
                    if (isPwd) {
                        if (!isVisible) { // 设置为可见
                            setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());
                            mRightDrawable = ContextCompat.getDrawable(getContext(), R.mipmap.icon_show);
                            isVisible = true;
                        } else { // 设置为不可见
                            setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());
                            mRightDrawable = ContextCompat.getDrawable(getContext(), R.mipmap.icon_hide);
                            isVisible = false;
                        }
                        mRightDrawable.setBounds(0, 0,
                                mRightDrawable.getIntrinsicWidth(),
                                mRightDrawable.getIntrinsicHeight());
                        setCompoundDrawables(getCompoundDrawables()[0],
                                getCompoundDrawables()[1], mRightDrawable,
                                getCompoundDrawables()[3]);
                    } else {
                        setText("");
                    }
                }
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    // 隐藏或者显示右边clean的图标
    protected void setClearDrawableVisible(boolean isVisible) {
        Drawable rightDrawable;
        if (isVisible) {
            rightDrawable = mRightDrawable;
        } else {
            rightDrawable = null;
        }
        // 使用代码设置该控件left, top, right, and bottom处的图标
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], rightDrawable,
                getCompoundDrawables()[3]);
    }

    // 显示一个动画,以提示用户输入
    public void setShakeAnimation() {
        this.setAnimation(shakeAnimation(5));
    }

    // CycleTimes动画重复的次数
    public Animation shakeAnimation(int cycleTimes) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 10);
        translateAnimation.setInterpolator(new CycleInterpolator(cycleTimes));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }

    public OnTextChanged getOnTextChanged() {
        return onTextChanged;
    }

    public void setOnTextChanged(OnTextChanged onTextChanged) {
        this.onTextChanged = onTextChanged;
    }

    public interface OnTextChanged {
        void onTextChanged(CharSequence s, int start, int before,
                           int count);
    }

    private class FocusChangeListenerImpl implements OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            isHasFocus = hasFocus;
            if (isHasFocus) {
                boolean isVisible = getText().toString().length() >= 1;
                setClearDrawableVisible(isVisible);
            } else {
                setClearDrawableVisible(false);
            }
        }
    }

    // 当输入结束后判断是否显示右边clean的图标
    private class TextWatcherImpl implements TextWatcher {
        @Override
        public void afterTextChanged(Editable s) {
            // boolean isVisible = getText().toString().length() >= 1;
            // setClearDrawableVisible(isVisible);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (s.length() == 0) {
                if (isPwd) {
                    setTransformationMethod(PasswordTransformationMethod
                            .getInstance());
                    mRightDrawable = ContextCompat.getDrawable(getContext(), R.mipmap.icon_hide);
                    isVisible = false;
                    mRightDrawable.setBounds(0, 0,
                            mRightDrawable.getIntrinsicWidth(),
                            mRightDrawable.getIntrinsicHeight());
                }
            }
            if (isHasFocus) {
                setClearDrawableVisible(s.length() > 0);
            }
            if (onTextChanged != null) {
                onTextChanged.onTextChanged(s, start, before, count);
            }
        }
    }
}
