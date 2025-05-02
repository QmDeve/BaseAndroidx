package com.qingmu.baseandroidx.interfaces;

import com.qingmu.baseandroidx.BaseActivity;

public interface ResultCallback {
    void onResult(BaseActivity.NavParams params);

    default void onTimeout() {
        // 默认超时处理
    }
}