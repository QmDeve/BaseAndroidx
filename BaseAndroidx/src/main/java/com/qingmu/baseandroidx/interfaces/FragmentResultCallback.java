package com.qingmu.baseandroidx.interfaces;

import com.qingmu.baseandroidx.BaseFragment;

public interface FragmentResultCallback {

    void onResult(BaseFragment.NavParams params);

    default void onTimeout() {

        // 默认超时处理

    }

}
