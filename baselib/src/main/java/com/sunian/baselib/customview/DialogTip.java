package com.sunian.baselib.customview;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.sunian.baselib.R;


public class DialogTip extends Dialog {
    public DialogTip(@NonNull Context context) {
        super(context, R.style.dialog);

        ProgressBar progressBar = new ProgressBar(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(100, 100);
        addContentView(progressBar, layoutParams);
        setCanceledOnTouchOutside(true);

    }
}
