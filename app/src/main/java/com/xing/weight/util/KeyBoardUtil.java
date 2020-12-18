package com.xing.weight.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.os.IBinder;
import android.text.Editable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
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
    private ViewGroup rootView;
    private View scrollView;

    public KeyBoardUtil(Activity mActivity) {
        this.mActivity = mActivity;
        provinceKey = new Keyboard(mActivity, R.xml.province_key);
        numberKey = new Keyboard(mActivity,R.xml.number_or_letters_key);
//        rootView = (ViewGroup) mActivity.getWindow().getDecorView();
        rootView = mActivity.findViewById(R.id.root);
        scrollView =  mActivity.findViewById(R.id.scrollView);
    }

    public void attachTo(EditText editText, boolean isAuto) {
        this.mEdit = editText;
        if(editText.getTag(R.string.attach) != null){
            hideSystemSoftKeyboard(editText);
            setAutoShowOnFocus(isAuto);
        } else {
            showSystemSoftKeyboard(editText);
            setAutoShowOnFocus(false);
        }
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

       /* mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isShow()){
                    closeInput(mActivity);
                    rootView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showKeyboard();
                        }
                    },200);
                }
            }
        });*/

    }

    View.OnFocusChangeListener onFocusChangeListener1 = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(v.getTag(R.string.attach) == null){
                return;
            }
            if (hasFocus){
//                closeInput(mActivity);
                showKeyboard();
//                rootView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        showKeyboard();
//                    }
//                },200);
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
     * 计算屏幕向上移动距离
     * @param view 响应输入焦点的控件
     * @parm showUnderView  如果有基线View. 则计算基线View到屏幕的距离
     * @parm keyboardHeight 键盘高
     *
     * @return 移动偏移量
     */
    private int getMoveHeight(View view, int keyboardHeight, View showUnderView) {
        Rect rect = new Rect();
        rootView.getWindowVisibleDisplayFrame(rect); //获取当前显示区域的宽高

        int[] vLocation = new int[2];
        view.getLocationOnScreen(vLocation); //计算输入框在屏幕中的位置
        int keyboardTop = vLocation[1] + view.getHeight() + view.getPaddingBottom() + view.getPaddingTop();
        if (keyboardTop - keyboardHeight < 0) { //如果输入框到屏幕顶部已经不能放下键盘的高度, 则不需要移动了.
            return 0;
        }
        if (null != showUnderView) { //
            int[] underVLocation = new int[2];
            showUnderView.getLocationOnScreen(underVLocation);
            keyboardTop = underVLocation[1] + showUnderView.getHeight() + showUnderView.getPaddingBottom() + showUnderView.getPaddingTop();
        }
        //输入框或基线View的到屏幕的距离 + 键盘高度 如果 超出了屏幕的承载范围, 就需要移动.
        int moveHeight = keyboardTop + keyboardHeight - rect.bottom;
        return moveHeight > 0 ? moveHeight : 0;
    }

    /**
     * 软键盘展示状态
     */
    public boolean isShow() {
        if (viewContainer == null){
            return false;
        }
        return viewContainer.isAttachedToWindow();
    }

    /**
     * 软键盘展示
     */
    public void showKeyboard() {
        if (viewContainer == null) {
            viewContainer = mActivity.getLayoutInflater().inflate(R.layout.keyboard_layout, null);
            keyboardView = viewContainer.findViewById(R.id.keyboard_view);
            keyboardView.setKeyboard(provinceKey);
            keyboardView.setEnabled(true);
            keyboardView.setPreviewEnabled(false);
            keyboardView.setOnKeyboardActionListener(this);
        }

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM;
//        lp.bottomMargin = NavigationBarInfo.getNavigationBarHeight(mActivity);
        rootView.addView(viewContainer, lp);
        viewContainer.setAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.down_to_up));

        int moveHeight = getMoveHeight(mEdit, Tools.dip2px(mActivity,200+15+20),null);

        if (moveHeight > 0) {
            scrollView.scrollBy(0, moveHeight); //移动屏幕
        } else {
            moveHeight = 0;
        }
        mEdit.setTag(R.id.keyboard_view_move_height, moveHeight);
    }

    /**
     * 软键盘隐藏
     */
    public void hideKeyboard() {
        int moveHeight = 0;
        Object tag = mEdit.getTag(R.id.keyboard_view_move_height);
        if (null != tag) moveHeight = (int) tag;
        if (moveHeight > 0) { //复原屏幕
            scrollView.scrollBy(0, -1 * moveHeight);
            mEdit.setTag(R.id.keyboard_view_move_height, 0);
        }
        rootView.removeView(viewContainer); //将键盘从根布局中移除.
        viewContainer.setAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.up_to_hide));

    }

    /**
     * 显示系统键盘
     *
     * @param editText
     */
    public void showSystemSoftKeyboard(EditText editText) {
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= 11) {
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editText, true);
            }  catch (Exception e) {
                e.printStackTrace();
            }
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

    /**
     * 关闭输入法
     *
     * @param context
     */
    public static void closeInput(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            if (context != null) {
                try {
                    IBinder iBinder = context.getCurrentFocus().getWindowToken();
                    if (iBinder != null)
                        imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {
                    // Log.e("xing", "close input exception");
                }
            }
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
