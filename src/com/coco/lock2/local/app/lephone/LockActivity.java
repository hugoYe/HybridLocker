package com.coco.lock2.local.app.lephone;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.coco.lock2.local.app.base.IWrap;
import com.cooee.hybridlocker.TouchEventPrevent;

import java.lang.reflect.Method;


public class LockActivity extends Activity {

    private LockWrap wrap = null;

    private TouchEventPrevent prevent = new TouchEventPrevent();

    @SuppressLint("JavascriptInterface")
    @Override
    public void onCreate(
        Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wrap = new LockWrap(this);
        wrap.setKernelCallback(new Callback() {

            @Override
            public boolean handleMessage(
                Message msg) {
                switch (msg.what) {
                    case IWrap.KERNEL_EXIT:
                        finish();
                        return true;
                }
                return false;
            }
        });
        wrap.onCreate();
        setContentView(wrap.getView());

        //  cordova begin
//        View webview = loadWebViewUrl(launchUrl);
//        webview.setBackgroundColor(Color.TRANSPARENT);
//        addContentView(webview, (new FrameLayout.LayoutParams(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.MATCH_PARENT)));
        //  cordova end

        // touch demo
        final WebView webView = new MyWebView(this);
        webView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                                             FrameLayout.LayoutParams.MATCH_PARENT));
        addContentView(webView, (new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)));
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/testproject/test.html");
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.addJavascriptInterface(new JsInteration(), "control");
//        webView.setWebChromeClient(new WebChromeClient() {
//        });
//        webView.setWebViewClient(new WebViewClient() {
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                testMethod(webView);
//            }
//
//        });
    }

    private void testMethod(WebView webView) {
        Log.e("", "###### testMethod 111");
        String call = "javascript:sayHello()";
//        webView.loadUrl(call);
//        call = "javascript:alertMessage(\"" + "content" + "\")";
//        webView.loadUrl(call);
//        call = "javascript:toastMessage(\"" + "content" + "\")";
//        webView.loadUrl(call);
        call = "javascript:sumToJava(1,2)";
        webView.loadUrl(call);
        Log.e("", "###### testMethod 222");
    }


    private class MyWebView extends WebView {

        public MyWebView(Context context) {
            super(context, null);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return super.onTouchEvent(event);
//            return false;
        }
    }


    // 屏蔽下拉
    @Override
    public void onWindowFocusChanged(
        boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (Build.VERSION.SDK_INT < 14) {
            try {
                Object service = getSystemService("statusbar");
                Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
                Method test = statusbarManager.getMethod("collapse");
                test.invoke(service);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // 屏蔽掉Home
    public void onAttachedToWindow() {
        if (Build.VERSION.SDK_INT < 14) {
            this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        }
        super.onAttachedToWindow();
    }

    // 屏蔽掉Back
    public boolean onKeyDown(
        int keyCode,
        KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wrap != null) {
            wrap.onResume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wrap != null) {
            wrap.onDestroy();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wrap != null) {
            wrap.onPause();
        }
    }

    public class JsInteration {

        @JavascriptInterface
        public void toastMessage(String message) {
            Toast.makeText(LockActivity.this.getApplicationContext(), message, Toast.LENGTH_LONG)
                .show();
        }

        @JavascriptInterface
        public void onSumResult(int result) {
            Log.i("", "###### onSumResult result=" + result);
        }
    }
}
