package com.xing.weight.view;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * 小数输入
 * @author 星
 */
public class CusFloatEditText extends AppCompatEditText implements InputFilter {

    private int decimalDigits = 2;

    public CusFloatEditText(Context context) {
        super(context);
        init();
    }

    public CusFloatEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CusFloatEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public int getDigit() {
        return decimalDigits;
    }

    public void setDigit(int digit) {
        this.decimalDigits = digit;
        InputFilter[] filters = getFilters();
        filters[filters.length - 1] = this;
        setFilters(filters);
    }

    private void init() {
        InputFilter[] filters = getFilters();
        InputFilter[] inputFilters = new InputFilter[filters.length + 1];
        for (int i = 0; i < filters.length; i++) {
            inputFilters[i] = filters[i];
        }
        inputFilters[inputFilters.length - 1] = this;
        setFilters(inputFilters);
    }


    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int dotPos = -1;
        int len = dest.length();
        for (int i = 0; i < len; i++) {
            char c = dest.charAt(i);
            if (c == '.' || c == ',') {
                dotPos = i;
                break;
            }
        }
        if (dotPos >= 0) {
            if (source.equals(".") || source.equals(",")) {
                return "";
            }
            if (dend <= dotPos) {
                return null;
            }
            if (len - dotPos > decimalDigits) {
                return "";
            }
        }
        return null;
    }
}
