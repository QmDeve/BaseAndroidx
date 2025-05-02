package com.qingmu.baseandroidx.interfaces;

import android.app.Activity;
import android.content.Context;

import com.qingmu.baseandroidx.BaseActivity;

public interface Interceptor {
    /**
     * @return true表示继续跳转，false表示拦截
     */
    boolean intercept(Context context, Class<? extends Activity> target, BaseActivity.NavParams params);
}