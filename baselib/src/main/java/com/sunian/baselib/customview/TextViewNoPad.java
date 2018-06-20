package com.sunian.baselib.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;


public class TextViewNoPad extends android.support.v7.widget.AppCompatTextView {
   Paint.FontMetricsInt fontMetricsInt;
    public TextViewNoPad(Context context) {
        super(context);
    }

    public TextViewNoPad(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewNoPad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (fontMetricsInt == null){
            fontMetricsInt = new Paint.FontMetricsInt();
            getPaint().getFontMetricsInt(fontMetricsInt);
        }
        canvas.translate(0, fontMetricsInt.top - fontMetricsInt.ascent);
        super.onDraw(canvas);



    }
}