# BaseAndroidx
[![](https://jitpack.io/v/QmDeve/BaseAndroidx.svg)](https://jitpack.io/#QmDeve/BaseAndroidx)
[![](https://camo.githubusercontent.com/c3b7a50769cb50bd6e9ac6ce04c47435001631dee3c8cd946fdceafe02d779d8/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f4c6963656e73652d417061636865253230322e302d7265642e737667)](http://www.apache.org/licenses/LICENSE-2.0)


## BaseAndroidx是什么
BaseAndroidx是一款Android适配框架
</br>
封装了Activity、Fragment、Adapter和Service
</br>
简化了很多代码，让开发更快速
</br>
以及提供了很多方法（沉浸式状态栏和导航栏、获取导航栏高度、获取状态栏高度、Activity跳转、Toast提示、Log打印、延迟执行任务、dp px互转等）
</br>
让您快速开发Android App

## 引入
1.添加jitpack仓库：
```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
2.引入BaseAndroidx
</br>
[![](https://jitpack.io/v/QmDeve/BaseAndroidx.svg)](https://jitpack.io/#QmDeve/BaseAndroidx)
```
dependencies {
   implementation 'com.github.QmDeve:BaseAndroidx:Tag'
}
```

## 用法
在你的activity中继承BaseActivity
``` java
import com.qingmu.baseandroidx.BaseActivity;

public class MainActivity extends BaseActivity {
```

在你的Fragment中继承BaseFragment
``` java
import com.qingmu.baseandroidx.BaseFragment;

public class MainFragment extends BaseFragment {
```


#### 目前的全部方法
``` java
/**
* 拦截返回事件，根据 return 值确定是否允许执行返回。
* 不推荐使用onBackPressed()方法 在API 36可能不生效
*
* @return true：拦截返回；false：不拦截
*/
@Override
public boolean onBacks() {
  return true;
}


/**
* Toast提示
* @String  需要提示的字符串
*/
toast(String);


/**
* 设置沉浸式状态栏和导航栏
*
* @isLight true：状态栏字体黑色；false：状态栏字体白色
*/
Immersion(boolean isLight);


/**
* 获取手机底部导航栏高度
*
* @param callback 导航栏高度的回调方法。在获取到导航栏高度后，会调用此方法
*/
getNavigationBarHeight(height -> {
    toast(height);
});


/**
* 延迟执行任务
*
* runnable 实现 Runnable 的对象
* time 延迟时间
*/
runDelayed(new Runnable() {
   @Override
   public void run() {
     System.out.println("延迟执行");
}, 1000);


/**
* 绑定单个View的点击事件
*
* View 
*/
Click(view, v -> {
  toast("View 被点击");
});

/**
* 绑定多个View的点击事件
*
* runnable 实现 Runnable 的对象
* time 延迟时间
*/
mClick(id -> {
   if (id == R.id.button_1) {
      
   } else if (id == R.id.button_1) {
       
   }, R.id.button_1, R.id.button_1
});


// Activity跳转
ToActivity(MainActivity.this, Main2Activity.class);

// Activity带参数跳转
NavParams params = new NavParams()
    .put("username", "张三")
    .put("age", 25)
    .put("ad", true);
ToActivity(MainActivity.this, Main2Activity.class, params);

// Activity带参数跳转并期待返回结果
startForResult(
    MainActivity.this, 
    Main2Activity.class, 
    new NavParams().put("title", "编辑资料"),
    new ResultCallback() {
        @Override
        public void onResult(NavParams params) {
            String result = params.getString("result");
            int status = params.getInt("status");
            // 处理返回结果
        }
    }
);

// 返回时设置返回参数
NavParams resultParams = new NavParams()
    .put("result", "操作成功")
    .put("status", 1);
Intent data = new Intent();
data.putExtras(resultParams.toBundle());
setResult(Activity.RESULT_OK, data);
finish();

// 带参数返回需在调用方Activity中重写onActivityResult()方法
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
   super.onActivityResult(requestCode, resultCode, data);
   handleResult(requestCode, resultCode, data);
}


/**
* 获取手机状态栏高度
*/
getStatusBarHeight();

// dp转px方法
dptopx(Context context, int dpValue);

// px转dp方法
pxtodp(Context context, int pxValue);

// dp转sp方法
dptosp(Context context, int dpValue);

// sp转dp方法
sptodp(Context context, int spValue);

// sp转px方法
sptopx(Context context, int spValue);

```

## 更新日志

#### 2025-05-07：v0.0.1-Beta-rc05
1.跳转Activity支持传递更多类型参数
2.封装了Fragment
3.修复部分bug
</br>
#### 2025-05-02：v0.0.1-Beta-rc03
BaseAndroidx 测试版发布。

## 联系方式
QQ：3100602519
</br>
QQ群：689861300
