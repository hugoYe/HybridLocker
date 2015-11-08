package com.cooee.hybridlocker;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.coco.lock2.local.app.base.AppInfo;
import com.coco.lock2.local.app.base.IBaseView;
import com.coco.lock2.local.app.base.Tools;
import com.coco.lock2.local.app.lephone.LockView;
import com.coco.lock2.local.app.lephone.LockWrap;
import com.cooee.cordova.plugins.TouchEventPrevent;

import org.apache.cordova.CordovaWrap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by user on 2015/11/2.
 */
public class FrameContainer extends FrameLayout implements IBaseView {

    private static final String TAG = "FrameContainer";

    private Context mContext;
    private Context mRemoteContext;

    private LockView lockView;


    private JsInteration jsInteration = new JsInteration();

    private TouchEventPrevent touchEventPrevent = new TouchEventPrevent();


    public FrameContainer(Context context, Context remoteContext) {
        super(context);
        mContext = context;
        mRemoteContext = remoteContext;
        setBackgroundColor(Color.RED);
    }


    private CordovaWrap mCordovaWrap;
    private WebView mWebView;


    WebView webView1;

    private void bindWebFavoriteApp11() {
        Log.i("", "######## bindWebFavoriteApp11 111");
        final JSONObject object = new JSONObject();//创建一个总的对象，这个对象对整个json串
        JSONArray jsonarray = new JSONArray();//json数组，里面包含的内容为pet的所有对象
        ArrayList<AppInfo> list = Tools.recentTasks(mContext);
        Log.i("", "######## bindWebFavoriteApp11 222");
        for (int i = 0; i < list.size(); i++) {
            AppInfo app = list.get(i);
            try {
                JSONObject jsonObj = new JSONObject();//pet对象，json形式
                jsonObj.put("intent", app.appIntent);
                String base64 = Tools.bitmapToBase64(Tools.createIconBitmap(app.appIcon));
                jsonObj.put("bitmap", base64);
                // 把每个数据当作一对象添加到数组里
                jsonarray.put(jsonObj);//向json数组里面添加对象

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            object.put("app", jsonarray);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        webView1.post(new Runnable() {
            @Override
            public void run() {
                webView1.loadUrl("javascript:bindWebFavoriteApp" + "(" + object.toString() + ");");
            }
        });

        Log.i("", "######## bindWebFavoriteApp11 333");
    }

    private void bindWebFavoriteApp() {
        Log.i("", "######## bindWebFavoriteApp 111");
        final JSONObject object = new JSONObject();//创建一个总的对象，这个对象对整个json串
        JSONArray jsonarray = new JSONArray();//json数组，里面包含的内容为pet的所有对象
        ArrayList<AppInfo> list = Tools.recentTasks(mContext);
        Log.i("", "######## bindWebFavoriteApp 222");
        for (int i = 0; i < list.size(); i++) {
            AppInfo app = list.get(i);
            try {
                JSONObject jsonObj = new JSONObject();//pet对象，json形式
                jsonObj.put("intent", app.appIntent.toUri(0));
                String base64 = Tools.bitmapToBase64(Tools.createIconBitmap(app.appIcon));
                jsonObj.put("bitmap", base64);
                // 把每个数据当作一对象添加到数组里
                jsonarray.put(jsonObj);//向json数组里面添加对象

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            object.put("app", jsonarray);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl("javascript:bindWebFavoriteApp" + "(" + object.toString() + ");");
            }
        });

        Log.i("", "######## bindWebFavoriteApp 333");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.i(TAG, "###### FrameContainer dispatchTouchEvent action = " + ev.getAction());

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            lockView.hybridTouchDown(ev);
        }

        if (TouchEventPrevent.preventWebTouchEvent) {
//            Log.i(TAG, "###### FrameContainer dispatchTouchEvent action = " + ev.getAction());
            lockView.onTouchEvent(ev);

            if (ev.getAction() == MotionEvent.ACTION_UP) {
                TouchEventPrevent.preventWebTouchEvent = false;
            }
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


    @Override
    public void onViewCreate() {
        lockView = new LockView(mContext, "");

        addView(lockView);
        mCordovaWrap = new CordovaWrap(mContext, mRemoteContext);
        if (mRemoteContext == null)
            mRemoteContext = mContext;
        mCordovaWrap.onCreate(null);
//        try {
//            FileUtils.copyAssetDirToFiles(mRemoteContext, mContext, "www");
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        mCordovaWrap.launchUrl = "file:///" + mRemoteContext.getFilesDir() + "/"
//                + "www" + "/index.html";
        mWebView = (WebView) mCordovaWrap.loadWebViewUrl(mCordovaWrap.launchUrl);
        mWebView.setBackgroundColor(0x00000000);
        mWebView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1100));
        addView(mWebView);

        lockView.onViewCreate();
    }

    @Override
    public void onViewResume() {
        mCordovaWrap.onResume();
        lockView.onViewResume();
    }

    @Override
    public void onViewPause() {
        mCordovaWrap.onPause();
        lockView.onViewPause();
    }

    @Override
    public void onViewDestroy() {
        mCordovaWrap.onDestroy();
        lockView.onViewDestroy();
    }


    private class MyWebView extends WebView {

        public MyWebView(Context context) {
            super(context, null);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            Log.i(TAG, "###### MyWebView dispatchTouchEvent action = " + ev.getAction());
            return super.onInterceptTouchEvent(ev);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            Log.i(TAG, "###### MyWebView dispatchTouchEvent action = " + ev.getAction());
            return super.dispatchTouchEvent(ev);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            Log.i(TAG, "###### MyWebView onTouchEvent action = " + event.getAction());
            return super.onTouchEvent(event);
        }
    }

    public class JsInteration {

        public boolean prevent = false;

        @JavascriptInterface
        public void bindFavoriteApp() {
//            bindWebFavoriteApp11();
            bindWebFavoriteApp();
        }

        @JavascriptInterface
        public void toastMessage(String message) {
            Toast.makeText(mContext, message, Toast.LENGTH_LONG)
                    .show();
        }

        @JavascriptInterface
        public void onSumResult(int result) {
            Log.i("", "###### onSumResult result=" + result);
        }

        @JavascriptInterface
        public void prevent() {
            Log.i("", "###### prevent");
            this.prevent = true;
        }
    }
}
