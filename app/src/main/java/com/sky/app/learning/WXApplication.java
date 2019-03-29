package com.sky.app.learning;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.alibaba.weex.commons.adapter.ImageAdapter;
import com.alibaba.weex.commons.adapter.JSExceptionAdapter;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKEngine;


public class WXApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        WXEnvironment.setOpenDebugLog(true);
        WXEnvironment.setApkDebugable(true);
        WXSDKEngine.initialize(this,
            new InitConfig.Builder()
                    .setImgAdapter(new ImageAdapter())
                    .setJSExceptionAdapter(new JSExceptionAdapter())
                    .build()
        );


        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }
}
