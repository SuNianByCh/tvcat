package com.tvcat.util;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tvcat.R;

public class DialogTip extends Dialog {
    public DialogTip(@NonNull Context context) {
        super(context, R.style.dialog);

        ProgressBar progressBar = new ProgressBar(context);
        int color = context.getResources().getColor(R.color.main_color);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(100, 100);
        addContentView(progressBar, layoutParams);
        setCanceledOnTouchOutside(true);

    }
}
