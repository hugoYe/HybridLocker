package com.coco.lock2.local.app.blinkCircle;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.coco.lock2.local.app.base.AppInfo;
import com.coco.lock2.local.app.base.IBaseView;
import com.coco.lock2.local.app.base.Tools;
import com.cooee.cordova.plugins.TouchEventPrevent;
import com.cooee.lock.statistics.Assets;
import com.cooee.statistics.StatisticsBaseNew;
import com.cooee.statistics.StatisticsExpandNew;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by user on 2015/11/2.
 */
public class FrameContainer extends FrameLayout implements IBaseView {

    private static final String TAG = "FrameContainer";

    private Context mContext;

    private Runnable mExitFunc = null;

    private JsInteration jsInteration = new JsInteration();

    private TouchEventPrevent touchEventPrevent = new TouchEventPrevent();
    private WebView webView;
    private boolean mUseStatistics = false;
    private BCView mLockScreen; 
    private BCWrap mLockWrap;

    public FrameContainer(Context context) {
        super(context);
        mContext = context;
        setBackgroundColor(Color.RED);
        startStatisticsService();
    }

    private void startStatisticsService()
	{
		if (mUseStatistics) {
			String appid = null;
			String sn = null;
			int launcherVersion = 0;
			StatisticsBaseNew.setApplicationContext(mContext);
			StatisticsExpandNew.setStatiisticsLogEnable(true);
			Assets.initAssets(mContext);
			JSONObject tmp = Assets.config;
			PackageManager mPackageManager = mContext.getPackageManager();
			try {
				JSONObject config = tmp.getJSONObject("config");
				appid = config.getString("app_id");
				sn = config.getString("serialno");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				launcherVersion = mPackageManager.getPackageInfo(
						mContext.getPackageName(), 0).versionCode;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			// xiatian add end

			Log.d("clear", "before first run");
			Intent it = new Intent();
			it.setClassName(mContext.getPackageName(),
					"com.cooee.lock.statistics.StaticClass");
			it.putExtra( "ClickEvent" , "" );
			it.putExtra("SN", sn);
			it.putExtra("APPID", appid);
			it.putExtra("Version", Integer.toString( launcherVersion ));
			it.putExtra("package", mContext.getPackageName());
			mContext.startService(it);
		}
	}
    
    

    public void setupViews(WebView webView) {
    	mLockScreen = new BCView(mContext, "");
    	mLockScreen.setExitFunction(mExitFunc);
    	mLockScreen.setWrap(mLockWrap);
        addView(mLockScreen);
        
//        String launchUrl = "";
//        File file = new File( mContext.getPackageResourcePath()+"/index.html" );
//		if( file.exists() )
//		{
//			launchUrl = "file:///data/data/com.coco.lock.app.locktestdemo/index.html";
//			createSystemSwitcherShortCut(mContext,"");
//		}
//		else
//		{
//			launchUrl = "file:///android_asset/www/index.html";
//			createSystemSwitcherShortCut(mContext);
//		}
		
        addView(webView);
        
        if( IsHaveInternet( mContext ) )
		{
			Intent intent = new Intent();
			intent.setClassName( mContext.getPackageName(), "com.cooee.control.center.module.update.UpdateService" );
			mContext.startService( intent );
		}
    }

    /**
	 * 判断是否联网
	 */
	public boolean IsHaveInternet(
			final Context context )
	{
		try
		{
			ConnectivityManager manger = (ConnectivityManager)context.getSystemService( Context.CONNECTIVITY_SERVICE );
			NetworkInfo info = manger.getActiveNetworkInfo();
			return( info != null && info.isConnected() );
		}
		catch( Exception e )
		{
			return false;
		}
	}
	
	public void createSystemSwitcherShortCut(
			Context context )
	{
		Bitmap bitmap = BitmapFactory.decodeResource( getResources() , R.drawable.time_0 );
		ParceBean icon = new ParceBean();
		icon.setDw( BitmapFactory.decodeResource( getResources() , R.drawable.time_0 ) );
        String str = getResources().getResourceName( R.drawable.time_0 );
        Log.v( "createSystemSwitcherShortCut" , str );
		final Intent addIntent = new Intent( "com.android.launcher.action.INSTALL_SHORTCUT" );
//		final Parcelable icon = Intent.ShortcutIconResource.fromContext( context , R.drawable.icon ); // 获取快捷键的图标  
		addIntent.putExtra( "duplicate" , false );
		Intent myIntent = new Intent( );
		myIntent.setClassName( "com.coco.lock.app.locktestdemo" , "com.iLoong.launcher.MList.MainActivity" );
		myIntent.putExtra( "APP_ID" , 10009 );
		myIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
		addIntent.putExtra( Intent.EXTRA_SHORTCUT_NAME , "心动应用" );// 快捷方式的标题  
//		addIntent.putExtra( Intent.EXTRA_SHORTCUT_ICON_RESOURCE , icon );// 快捷方式的图标  
		addIntent.putExtra( Intent.EXTRA_SHORTCUT_ICON , bitmap );// 快捷方式的图标  
		addIntent.putExtra( Intent.EXTRA_SHORTCUT_INTENT , myIntent );// 快捷方式的动作  
		context.sendBroadcast( addIntent );
	}
	
	public void createSystemSwitcherShortCut(
			Context context,String uristring )
	{
		final Intent addIntent = new Intent( "com.android.launcher.action.INSTALL_SHORTCUT" );
		final Parcelable icon = Intent.ShortcutIconResource.fromContext( context , R.drawable.icon ); // 获取快捷键的图标  
		addIntent.putExtra( "duplicate" , false );
		Uri uri = Uri.parse( "http://open.egret.com/gamecenter/?channelId=18464" );
		Intent myIntent = new Intent( Intent.ACTION_VIEW , uri );
		myIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
		addIntent.putExtra( Intent.EXTRA_SHORTCUT_NAME , "心动应用" );// 快捷方式的标题  
		addIntent.putExtra( Intent.EXTRA_SHORTCUT_ICON_RESOURCE , icon );// 快捷方式的图标  
		addIntent.putExtra( Intent.EXTRA_SHORTCUT_INTENT , myIntent );// 快捷方式的动作  
		context.sendBroadcast( addIntent );
	}
	
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
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:bindWebFavoriteApp" + "(" + object.toString() + ");");
            }
        });

        Log.i("", "######## bindWebFavoriteApp 333");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG, "###### FrameContainer dispatchTouchEvent action = " + ev.getAction());

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//        	mLockScreen.hybridTouchDown(ev);
        }

        if (TouchEventPrevent.preventWebTouchEvent) {
            Log.i(TAG, "###### FrameContainer dispatchTouchEvent action = " + ev.getAction());
            mLockScreen.onTouchEvent(ev);

            if (ev.getAction() == MotionEvent.ACTION_UP) {
                TouchEventPrevent.preventWebTouchEvent = false;
            }
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG, "###### FrameContainer onInterceptTouchEvent action = " + ev.getAction());
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "###### FrameContainer onTouchEvent action = " + event.getAction());
        return super.onTouchEvent(event);
    }

    public void setExitFunction(
        Runnable runnable) {
        mExitFunc = runnable;
    }

    public void setWrap(
    		BCWrap lockWrap) {
        mLockWrap = lockWrap;
    }

    @Override
    public void onViewResume() {
    	mLockScreen.onViewResume();
    }

    @Override
    public void onViewPause() {
    	mLockScreen.onViewPause();
    }

    @Override
    public void onViewDestroy() {
    	mLockScreen.onViewDestroy();
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
