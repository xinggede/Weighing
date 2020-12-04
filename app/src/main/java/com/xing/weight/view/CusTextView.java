package com.xing.weight.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;

import com.xing.weight.R;
import com.xing.weight.util.Tools;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class CusTextView extends AppCompatTextView {
    public CusTextView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CusTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CusTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private Bitmap bitmap;

    private void init(Context context){
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_xh);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if((getGravity() & Gravity.END) == Gravity.END ){
            TextPaint textPaint = getPaint();
            float x = textPaint.measureText(getText().toString());
            canvas.drawBitmap(bitmap,getWidth() - x - bitmap.getWidth() - 10,bitmap.getHeight(),null);
        }
    }
}
