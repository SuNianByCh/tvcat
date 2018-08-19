package com.sunian.baselib.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sunian.baselib.model.http.SeesinPrenster;

public class HomeKeyBroadcastReceiver extends BroadcastReceiver {
    final String SYSTEM_DIALOG_REASON_KEY = "reason";

    final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";

    final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
            String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            if (reason != null) {
                if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                   // Toast.makeText(context.getApplicationContext(), "Home键被监听", Toast.LENGTH_SHORT).show();

                    new SeesinPrenster().startSensein();




                }
            }
        }
    }
}
