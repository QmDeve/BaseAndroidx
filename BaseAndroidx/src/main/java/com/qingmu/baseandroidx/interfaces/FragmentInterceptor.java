package com.qingmu.baseandroidx.interfaces;

import android.app.Activity;
import android.content.Context;
import com.qingmu.baseandroidx.BaseFragment;

public interface FragmentInterceptor {

    /**

     * @return true表示继续跳转，false表示拦截

     */

    boolean intercept(Context context, Class<? extends Activity> target, BaseFragment.NavParams params);

}
