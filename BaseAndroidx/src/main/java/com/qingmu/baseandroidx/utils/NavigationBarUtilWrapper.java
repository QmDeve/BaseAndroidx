package com.qingmu.baseandroidx.utils;

import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.RequiresApi;

public class NavigationBarUtilWrapper {
    static class NavigationBarUtil {

        @RequiresApi(api = Build.VERSION_CODES.Q)
        static void setNavigationBarContrastEnforced(Window window, boolean enforce) {
            window.setNavigationBarContrastEnforced(enforce);
        }

        static void getTransparentNavigationBar(Window window) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                setNavigationBarContrastEnforced(window, false);
            }
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int systemUiVisibility = window.getDecorView().getSystemUiVisibility();
            systemUiVisibility = systemUiVisibility | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            window.getDecorView().setSystemUiVisibility(systemUiVisibility);
            window.setNavigationBarColor(android.graphics.Color.TRANSPARENT);
        }

        public static int getNavigationBarHeight(View view) {
            NavigationBarHeightUtil navigationBarUtils = new NavigationBarHeightUtil();
            return navigationBarUtils.getRealNavigationBarHeight(view);
        }
    }
    public static int getNavigationBarHeightFromWrapper(View view) {
        return NavigationBarUtil.getNavigationBarHeight(view);
    }

    public static void getTransparentNavigationBarFromWrapper(Window window) {
        NavigationBarUtil.getTransparentNavigationBar(window);
    }
}
