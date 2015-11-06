package com.cooee.cordova.plugins;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import com.coco.lock2.local.app.base.AppInfo;
import com.coco.lock2.local.app.base.Tools;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AppsApi extends CordovaPlugin {

    public final String ACTION_CHECK_AVAILABILITY = "checkAvailability";
    public final String ACTION_START_ACTIVITY = "startActivity";
    public final String ACTION_BIND_FAVORITE_APP = "bindFavoriteApp";

    private CallbackContext mCallbackContext;

    @Override
    public boolean execute(String action, final JSONArray args,
                           CallbackContext callbackContext) throws JSONException {

        mCallbackContext = callbackContext;

        if (action.equals(ACTION_CHECK_AVAILABILITY)) {
            String uri = args.getString(0);
            this.checkAvailability(uri, callbackContext);
            return true;
        } else if (action.equals(ACTION_START_ACTIVITY)) {
            if (cordova.getActivity() != null) {
                cordova.getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {

                            String method = args.getString(0);
                            Log.d("", "intent=" + method);
                            Intent intent;
                            try {
                                intent = Intent.parseUri(method, 0);

                            } catch (Exception e) {
                                intent = new Intent(Intent.ACTION_VIEW);
                                Uri uri = Uri.parse(method);
                                intent.setData(uri);
                            }
                            cordova.getActivity().startActivity(intent);

                        } catch (JSONException ex) {
                            mCallbackContext.sendPluginResult(new PluginResult(
                                PluginResult.Status.JSON_EXCEPTION));
                        }
                    }

                });
            } else if (cordova.getContext() != null) {
                cordova.getCordovaWrap().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {

                            String method = args.getString(0);
                            Log.d("", "intent=" + method);
                            Intent intent;
                            try {
                                intent = Intent.parseUri(method, 0);

                            } catch (Exception e) {
                                intent = new Intent(Intent.ACTION_VIEW);
                                Uri uri = Uri.parse(method);
                                intent.setData(uri);
                            }
                            cordova.getActivity().startActivity(intent);

                        } catch (JSONException ex) {
                            mCallbackContext.sendPluginResult(new PluginResult(
                                PluginResult.Status.JSON_EXCEPTION));
                        }
                    }

                });
            }

            // cordova.getThreadPool().execute(new Runnable() {
            // @Override
            // public void run() {
            // try {
            //
            // String method = args.getString(0);
            // Log.d("", "intent=" + method);
            // Intent intent;
            // try {
            // intent = Intent.parseUri(method, 0);
            //
            // } catch (Exception e) {
            // intent = new Intent(Intent.ACTION_VIEW);
            // Uri uri = Uri.parse(method);
            // intent.setData(uri);
            // }
            // cordova.getActivity().startActivity(intent);
            //
            // } catch (JSONException ex) {
            // mCallbackContext.sendPluginResult(new PluginResult(
            // PluginResult.Status.JSON_EXCEPTION));
            // }
            // }
            // });
            return true;
        } else if (action.equals(ACTION_BIND_FAVORITE_APP)) {
            bindWebFavoriteApp();
            return true;
        }
        return false;
    }

    // Thanks to
    // http://floresosvaldo.com/android-cordova-plugin-checking-if-an-app-exists
    public boolean appInstalled(String uri) {
        Context ctx = null;
        if (this.cordova.getActivity() != null) {
            ctx = this.cordova.getActivity().getApplicationContext();
        } else if (this.cordova.getContext() != null) {
            ctx = this.cordova.getContext();
        }
        final PackageManager pm = ctx.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    private void checkAvailability(String uri, CallbackContext callbackContext) {
        if (appInstalled(uri)) {
            callbackContext.success();
        } else {
            callbackContext.error("");
        }
    }

    public void bindWebFavoriteApp() {
        Log.i("", "######## bindWebFavoriteApp 111");
        Context ctx = null;
        if (this.cordova.getActivity() != null) {
            ctx = this.cordova.getActivity().getApplicationContext();
        } else if (this.cordova.getContext() != null) {
            ctx = this.cordova.getContext();
        }

        final JSONObject object = new JSONObject();//创建一个总的对象，这个对象对整个json串
        JSONArray jsonarray = new JSONArray();//json数组，里面包含的内容为pet的所有对象
        ArrayList<AppInfo> list = Tools.recentTasks(ctx);
        Log.i("", "######## bindWebFavoriteApp 222");
        for (int i = 0; i < list.size(); i++) {
            AppInfo app = list.get(i);
            try {
                JSONObject jsonObj = new JSONObject();//pet对象，json形式
                jsonObj.put("intent", app.appIntent.toUri(0));
                String base64 = Tools.bitmaptoString(Tools.createIconBitmap(app.appIcon));
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

        sendJS("javascript:bindWebFavoriteApp" + "(" + object.toString() + ");");

        Log.i("", "######## bindWebFavoriteApp 333");
    }

    private void sendJS(final String js) {
        if (cordova.getActivity() != null) {
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl(js);
                }
            });
        } else if (cordova.getContext() != null) {
            cordova.getCordovaWrap().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl(js);
                }
            });
        }
    }
}
