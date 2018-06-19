package com.tvcat;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tvcat.beans.UpdateBean;
import com.tvcat.util.IClickBack;

import java.util.List;

public class DialogUpdate extends Dialog {

    public IClickBack<String> iClickBack;


    public DialogUpdate(@NonNull Context context, UpdateBean updateBean) {
        super(context, R.style.dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_update, null, false);
        setContentView(view);

        TextView tvContent = findViewById(R.id.tv_content);
        List<String> changelog = updateBean.getChangelog();
        if (changelog != null && !changelog.isEmpty()) {
            tvContent.setText("");
            int i = 1;
            for (String content : changelog) {
                tvContent.append(i + ",");
                tvContent.append(content);
                tvContent.append("\n");
                i++;

            }
        }

        if (updateBean.isMust_upgrade()) {
            findViewById(R.id.bt_cancle).setVisibility(View.GONE);
            setCanceledOnTouchOutside(false);
        } else {
            findViewById(R.id.bt_cancle).setOnClickListener(v -> dismiss());
            setCanceledOnTouchOutside(true);
        }


        findViewById(R.id.bt_sure).setOnClickListener(v -> {
            if (iClickBack != null)
                iClickBack.callBack(0, updateBean.getApp_url());
            dismiss();
        });


    }


    @Override
    public void onBackPressed() {

    }

    public DialogUpdate setiClickBack(IClickBack<String> iClickBack) {
        this.iClickBack = iClickBack;
        return this;
    }
}
