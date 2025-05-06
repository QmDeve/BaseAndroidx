package com.qingmu.baseandroidx;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.AnimRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.qingmu.baseandroidx.interfaces.FragmentInterceptor;
import com.qingmu.baseandroidx.interfaces.FragmentResultCallback;
import com.qingmu.baseandroidx.utils.NavigationBarUtilWrapper;
import com.qingmu.baseandroidx.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BaseFragment extends Fragment {

    private static final String BaseAndroidxTAG = "BaseAndroidx";
    // 请求码生成器
    private static int requestCodeCounter = 1000;
    private static final Map<Integer, ResultCallbackWrapper> callbackMap = new HashMap<>();
    private static final List<FragmentInterceptor> interceptors = new ArrayList<>();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    // 默认动画资源
    private static @AnimRes int enterAnim = -1;
    private static @AnimRes int exitAnim = -1;

    // 默认超时时间（毫秒）
    private static long defaultTimeout = TimeUnit.SECONDS.toMillis(30);

    public void toast(String text) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show();
    }

    public static void setDefaultAnimation(@AnimRes int enter, @AnimRes int exit) {
        enterAnim = enter;
        exitAnim = exit;
    }

    public static void setDefaultTimeout(long timeout, @NonNull TimeUnit unit) {
        defaultTimeout = unit.toMillis(timeout);
    }

    public static void addInterceptor(FragmentInterceptor interceptor) {
        interceptors.add(interceptor);
    }

    public static void removeInterceptor(FragmentInterceptor interceptor) {
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
                               FragmentResultCallback callback) {
        startForResult(activity, target, null, callback, defaultTimeout);
    }

    public void startForResult(Activity activity, Class<? extends Activity> target,
                               NavParams params, FragmentResultCallback callback) {
        startForResult(activity, target, params, callback, defaultTimeout);
    }

    public void startForResult(Activity activity, Class<? extends Activity> target,
                               NavParams params, FragmentResultCallback callback, long timeoutMs) {
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

        for (FragmentInterceptor interceptor : interceptors) {
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

        public NavParams put(String key, boolean value) {
            bundle.putBoolean(key, value);
            return this;
        }

        public NavParams put(String key, int value) {
            bundle.putInt(key, value);
            return this;
        }

        public NavParams put(String key, byte value) {
            bundle.putByte(key, value);
            return this;
        }

        public NavParams put(String key, long value) {
            bundle.putLong(key, value);
            return this;
        }

        public NavParams put(String key, IBinder value) {
            bundle.putBinder(key, value);
            return this;
        }

        public NavParams put(String key, Bundle value) {
            bundle.putBundle(key, value);
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
        private final FragmentResultCallback callback;
        private boolean handled = false;

        ResultCallbackWrapper(FragmentResultCallback callback) {
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

    /**
     * 设置沉浸式状态栏和导航栏
     *
     * @param isLight true：状态栏字体黑色；false：状态栏字体白色
     */
    @SuppressLint("SourceLockedOrientationActivity")
    public void Immersion(boolean isLight) {
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StatusBarUtil.getImmersion(requireContext(), requireActivity().getWindow(), android.R.color.transparent, isLight);
        NavigationBarUtilWrapper.getTransparentNavigationBarFromWrapper(requireActivity().getWindow());
    }
}
