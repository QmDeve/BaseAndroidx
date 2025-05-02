package com.qingmu.baseandroidx.utils;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class StatusBarUtil {
    public static void getImmersion(Context context, Window window, int statusbarback, boolean isLight) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(context, statusbarback));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

            WindowInsetsControllerCompat wic = ViewCompat.getWindowInsetsController(window.getDecorView());
            if (wic != null) {
                wic.setAppearanceLightStatusBars(isLight);
            }
        }

    }
}
