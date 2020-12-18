package com.xing.weight.util;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.xing.weight.R;

import java.lang.reflect.Method;

public class KeyBoardUtil implements KeyboardView.OnKeyboardActionListener {
    private final String reg = "[\\u4e00-\\u9fa5]";
    private Activity mActivity;
    private EditText mEdit;
    private KeyboardView keyboardView;

    private Keyboard provinceKey;

    private Keyboard numberKey;

    private View viewContainer;

    public KeyBoardUtil(Activity mActivity) {
        this.mActivity = mActivity;
        this.mEdit = mEdit;
        provinceKey = new Keyboard(mActivity, R.xml.province_key);
        numberKey = new Keyboard(mActivity,R.xml.number_or_letters_key);
    }

    public void attachTo(EditText editText, boolean isAuto) {
        this.mEdit = editText;
        hideSystemSoftKeyboard(this.mEdit);
        setAutoShowOnFocus(isAuto);
    }

    public void setAutoShowOnFocus(boolean enable) {
        if (mEdit == null){
            return;
        }
        if (enable){
            mEdit.setOnFocusChangeListener(onFocusChangeListener1);
        } else {
            mEdit.setOnFocusChangeListener(null);
        }

    }

    View.OnFocusChangeListener onFocusChangeListener1 = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus){
                showKeyboard();
            } else {
                hideKeyboard();
            }
        }
    };

    /**
     * 指定切换软键盘 isNumber false表示要切换为省份简称软键盘 true表示要切换为数字软键盘
     */
    private void changeKeyboard(boolean isNumber) {
        if (isNumber) {
            keyboardView.setKeyboard(numberKey);
        } else {
            keyboardView.setKeyboard(provinceKey);
        }
    }

    /**
     * 软键盘展示状态
     */
    public boolean isShow() {
        if (viewContainer == null){
            return false;
        }
        return viewContainer.getVisibility() == View.VISIBLE;
    }

    /**
     * 软键盘展示
     */
    public void showKeyboard() {
        if (viewContainer == null) {
            viewContainer = mActivity.getLayoutInflater().inflate(R.layout.keyboard_layout, null);
            keyboardView = viewContainer.findViewById(R.id.keyboard_view);
            keyboardView = mActivity.findViewById(R.id.keyboard_view);
            keyboardView.setKeyboard(provinceKey);
            keyboardView.setEnabled(true);
            keyboardView.setPreviewEnabled(false);
            keyboardView.setOnKeyboardActionListener(this);
        }

        FrameLayout frameLayout = (FrameLayout) mActivity.getWindow().getDecorView();
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM;
        frameLayout.addView(viewContainer, lp);
        viewContainer.setAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.down_to_up));


        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 软键盘隐藏
     */
    public void hideKeyboard() {
        if (viewContainer != null && viewContainer.getParent() != null) {
            ((ViewGroup) viewContainer.getParent()).removeView(viewContainer);
        }
    }

    /**
     * 隐藏系统键盘
     *
     * @param editText
     */
    public void hideSystemSoftKeyboard(EditText editText) {
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= 11) {
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editText, false);
            }  catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            editText.setInputType(InputType.TYPE_NULL);
        }
    }


    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        Editable editable = mEdit.getText();
        int start = mEdit.getSelectionStart();
        if(primaryCode == -1){ // 省份简称与数字键盘切换
            if(editable.toString().matches(reg)){
                changeKeyboard(true);
            }
        } else if (primaryCode == -3) {
            if (editable != null && editable.length() > 0) {
                //没有输入内容时软键盘重置为省份简称软键盘
                if (editable.length() == 1) {
                    changeKeyboard(false);
                }
                if (start > 0) {
                    editable.delete(start - 1, start);
                }
            }
        } else {
            editable.insert(start, Character.toString((char) primaryCode));
            // 判断第一个字符是否是中文,是，则自动切换到数字软键盘
            if (mEdit.getText().toString().matches(reg)) {
                changeKeyboard(true);
            }
        }
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}
