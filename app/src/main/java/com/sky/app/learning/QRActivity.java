package com.sky.app.learning;

import android.content.Intent;
import android.hardware.Camera;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;

import com.sky.app.learning.R;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;


public class QRActivity extends AppCompatActivity implements QRCodeView.Delegate {

    private static final String TAG = QRActivity.class.getSimpleName();
    private ZXingView mZXingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        setTitle("二维码扫描");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        mZXingView = findViewById(R.id.zxingview);
        mZXingView.setDelegate(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i("QRCode Result:", result);
        if (Patterns.WEB_URL.matcher(result).matches() || URLUtil.isValidUrl(result)) {
            if (URLUtil.isNetworkUrl(result)) {
                String ext = MimeTypeMap.getFileExtensionFromUrl(result);
                Log.d(TAG, "result url ext:" + ext);
                if ("js".equals(ext)) {
                    Intent intent = new Intent(this, WXActivity.class);
                    intent.putExtra("name", "weex-load-url");
                    intent.putExtra("url", result);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, WebActivity.class);
                    intent.putExtra("name", "url");
                    intent.putExtra("url", result);
                    startActivity(intent);
                }
                mZXingView.stopCamera();
            }
        } else {
            mZXingView.startSpot();
        }
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        String tipText = mZXingView.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                mZXingView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                mZXingView.getScanBoxView().setTipText(tipText);
            }
        }
    }


    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "open camera error");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }

    @Override
    protected void onResume() {
        super.onResume();
        mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
    }

    @Override
    protected void onStop() {
        mZXingView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZXingView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }
}
