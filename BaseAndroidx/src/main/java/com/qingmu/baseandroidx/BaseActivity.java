package com.qingmu.baseandroidx;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.AnimRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.qingmu.baseandroidx.interfaces.Interceptor;
import com.qingmu.baseandroidx.interfaces.OnClick;
import com.qingmu.baseandroidx.interfaces.OnNavigationBarHeight;
import com.qingmu.baseandroidx.interfaces.mClick;
import com.qingmu.baseandroidx.utils.NavigationBarUtilWrapper;
import com.qingmu.baseandroidx.utils.StatusBarUtil;
import com.qingmu.baseandroidx.interfaces.ResultCallback;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class BaseActivity extends AppCompatActivity {

    private static final String BaseAndroidxTAG = "BaseAndroidx";
    // 请求码生成器
    private static int requestCodeCounter = 1000;
    private static final Map<Integer, ResultCallbackWrapper> callbackMap = new HashMap<>();
    private static final List<Interceptor> interceptors = new ArrayList<>();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    // 默认动画资源
    private static @AnimRes int enterAnim = -1;
    private static @AnimRes int exitAnim = -1;

    // 默认超时时间（毫秒）
    private static long defaultTimeout = TimeUnit.SECONDS.toMillis(30);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!onBacks()) {
                    finish();
                }
            }
        });

    }

    /**
     * 不推荐使用此方法，此方法可能在API 36中不生效，推荐重写 onBacks()方法
     */
    @Override
    @Deprecated
    public void onBackPressed() {
        if (!onBacks()) {
            super.onBackPressed();
        }
    }

    /**
     * 根据 return 值确定是否允许执行返回指令。
     *
     * @return true：拦截返回指令；false：执行返回指令。
     */
    public boolean onBacks() {
        return false;
    }

    /**
     * Toast提示
     */
    public void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置沉浸式状态栏和导航栏
     *
     * @param isLight true：状态栏字体黑色；false：状态栏字体白色
     */
    @SuppressLint("SourceLockedOrientationActivity")
    public void Immersion(boolean isLight) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StatusBarUtil.getImmersion(this, getWindow(), android.R.color.transparent, isLight);
        NavigationBarUtilWrapper.getTransparentNavigationBarFromWrapper(getWindow());
    }

    /**
     * 获取手机底部导航栏高度
     *
     * @param callback 导航栏高度的回调方法。在获取到导航栏高度后，会调用此方法
     */
    public void getNavigationBarHeight(OnNavigationBarHeight callback) {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(@NonNull View v) {
                int height = NavigationBarUtilWrapper.getNavigationBarHeightFromWrapper(v);

                callback.onNavigationBarHeight(height);
            }

            @Override
            public void onViewDetachedFromWindow(@NonNull View v) {

            }
        });
    }

    /**
     * 获取手机状态栏高度
     */
    public int getStatusBarHeight() {
        try {
            @SuppressLint("PrivateApi")
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(Objects.requireNonNull(field.get(obj)).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            Log.e(BaseAndroidxTAG, "获取状态栏高度失败; Failed to get status bar height");
        }
        return 0;
    }

    /**
     * dp转px方法
     *
     * @param context 上下文
     * @return int
     */
    public int dptopx(@NonNull Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp方法
     *
     * @param context 上下文
     * @return int
     */
    public int pxtodp(@NonNull Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * dp转sp方法
     *
     * @param context 上下文
     * @return int
     */
    public int dptosp(@NonNull Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (dpValue * scale / fontScale + 0.5f);
    }

    /**
     * sp转dp方法
     *
     * @param context 上下文
     * @return int
     */
    public int sptodp(@NonNull Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale / scale + 0.5f);
    }

    /**
     * px转sp方法
     *
     * @param context 上下文
     * @return int
     */
    public int pxtosp(@NonNull Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * sp转px方法
     *
     * @param context 上下文
     * @return int
     */
    public int sptopx(@NonNull Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 延迟执行任务
     *
     * @param runnable 实现 Runnable 的对象
     * @param time     延迟时间
     */
    public void runDelayed(Runnable runnable, long time) {
        Handler handler = new Handler();
        handler.postDelayed(runnable, time);
    }

    public void Click(@NonNull View v, OnClick onClick) {
        v.setOnClickListener(onClick);
    }

    public void mClick(mClick onClick, @NonNull int... viewIds) {
        for (int id : viewIds) {
            View view = findViewById(id);
            view.setOnClickListener(v -> onClick.onClick(id));
        }
    }

    public static void setDefaultAnimation(@AnimRes int enter, @AnimRes int exit) {
        enterAnim = enter;
        exitAnim = exit;
    }

    public static void setDefaultTimeout(long timeout, @NonNull TimeUnit unit) {
        defaultTimeout = unit.toMillis(timeout);
    }

    public static void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }

    public static void removeInterceptor(Interceptor interceptor) {
        interceptors.remove(interceptor);
    }

    public void ToActivity(Context context, Class<? extends Activity> target) {
        ToActivity(context, target, null, null, enterAnim, exitAnim);
    }

    public void ToActivity(Context context, Class<? extends Activity> target,
                             NavParams params) {
        ToActivity(context, target, params, null, enterAnim, exitAnim);
    }

    public void ToActivity(Context context, Class<? extends Activity> target,
                             NavParams params, @AnimRes int enterAnim, @AnimRes int exitAnim) {
        ToActivity(context, target, params, null, enterAnim, exitAnim);
    }

    public void startForResult(Activity activity, Class<? extends Activity> target,
                                      ResultCallback callback) {
        startForResult(activity, target, null, callback, defaultTimeout);
    }

    public void startForResult(Activity activity, Class<? extends Activity> target,
                                      NavParams params, ResultCallback callback) {
        startForResult(activity, target, params, callback, defaultTimeout);
    }

    public void startForResult(Activity activity, Class<? extends Activity> target,
                                      NavParams params, ResultCallback callback, long timeoutMs) {
        int requestCode = generateRequestCode();
        if (callback != null) {
            ResultCallbackWrapper wrapper = new ResultCallbackWrapper(callback);
            callbackMap.put(requestCode, wrapper);

            if (timeoutMs > 0) {
                mainHandler.postDelayed(() -> {
                    ResultCallbackWrapper removed = callbackMap.remove(requestCode);
                    if (removed != null) {
                        removed.onTimeout();
                    }
                }, timeoutMs);
            }
        }
        ToActivity(activity, target, params, requestCode, enterAnim, exitAnim);
    }

    private void ToActivity(Context context, Class<? extends Activity> target,
                              NavParams params, Integer requestCode,
                              @AnimRes int enterAnim, @AnimRes int exitAnim) {

        for (Interceptor interceptor : interceptors) {
            if (!interceptor.intercept(context, target, params)) {
                return;
            }
        }

        Intent intent = new Intent(context, target);
        if (params != null) {
            intent.putExtras(params.toBundle());
        }

        if (requestCode != null && context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, requestCode);
            if (enterAnim != -1 || exitAnim != -1) {
                ((Activity) context).overridePendingTransition(enterAnim, exitAnim);
            }
        } else {
            context.startActivity(intent);
            if (context instanceof Activity && (enterAnim != -1 || exitAnim != -1)) {
                ((Activity) context).overridePendingTransition(enterAnim, exitAnim);
            }
        }
    }

    public void handleResult(int requestCode, int resultCode, Intent data) {
        ResultCallbackWrapper wrapper = callbackMap.remove(requestCode);
        if (wrapper != null) {
            wrapper.handleResult(resultCode, data);
        }
    }

    private synchronized int generateRequestCode() {
        return requestCodeCounter++;
    }

    public static class NavParams {
        private final Bundle bundle;

        public NavParams() {
            this.bundle = new Bundle();
        }

        public NavParams(Bundle bundle) {
            this.bundle = bundle != null ? bundle : new Bundle();
        }

        public NavParams put(String key, String value) {
            bundle.putString(key, value);
            return this;
        }

        public String getString(String key) {
            return bundle.getString(key);
        }

        public Bundle toBundle() {
            return new Bundle(bundle);
        }
    }

    private static class ResultCallbackWrapper {
        private final ResultCallback callback;
        private boolean handled = false;

        ResultCallbackWrapper(ResultCallback callback) {
            this.callback = callback;
        }

        void handleResult(int resultCode, Intent data) {
            if (!handled && resultCode == Activity.RESULT_OK) {
                handled = true;
                callback.onResult(new NavParams(data != null ? data.getExtras() : null));
            }
        }

        void onTimeout() {
            if (!handled) {
                handled = true;
                callback.onTimeout();
            }
        }
    }
}