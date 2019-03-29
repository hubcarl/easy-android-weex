package com.sky.app.learning;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.Date;
import java.util.regex.Pattern;

import com.sky.app.learning.weex.R;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.utils.WXFileUtils;

public class WXActivity extends AppCompatActivity implements IWXRenderListener {

    private static String TAG = "--WXActivity--";
    private WXSDKInstance mWXSDKInstance;
    private FrameLayout weexContainer;
    private String name;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx);
        init();
        render();
    }

    private void init() {

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        name = getIntent().getStringExtra("name");
        url = getIntent().getStringExtra("url");

        setTitle(name);

        weexContainer = findViewById(R.id.weexContainer);
    }

    protected void render() {
        if (mWXSDKInstance != null) {
            mWXSDKInstance.destroy();
        }
        mWXSDKInstance = new WXSDKInstance(this);
        mWXSDKInstance.registerRenderListener(this);

        if ( url != null && Pattern.matches("^http?://.*", url)) {
            Log.d(TAG,  "http url load:" + url);
            mWXSDKInstance.renderByUrl(name, url, null, null, WXRenderStrategy.APPEND_ASYNC);
        } else {
            Log.d(TAG,  "http file load:" + url);
            String script = WXFileUtils.loadFileOrAsset(url,this);
            mWXSDKInstance.render(name, script, null, null, WXRenderStrategy.APPEND_ASYNC);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_refresh, menu);
        return super.onCreateOptionsMenu(menu);
    }

        @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                break;
            case R.id.action_refresh:
                Log.d(TAG, "reload page");
                render();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(WXSDKInstance instance, View view) {
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        Log.d(TAG, "onViewCreated");
        weexContainer.addView(view);
    }

    @Override
    public void onRenderSuccess(WXSDKInstance instance, int width, int height) {
        Log.d(TAG, "onRenderSuccess");
    }

    @Override
    public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {
    }

    @Override
    public void onException(WXSDKInstance instance, String errCode, String msg) {
        Log.e(TAG, errCode + msg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mWXSDKInstance!=null){
            mWXSDKInstance.onActivityResume();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityStart();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mWXSDKInstance!=null){
            mWXSDKInstance.onActivityPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mWXSDKInstance!=null){
            mWXSDKInstance.onActivityStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mWXSDKInstance!=null){
            mWXSDKInstance.onActivityDestroy();
        }
    }
}
