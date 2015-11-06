package com.coco.lock2.local.app.art;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class LockService extends Service {

	private WebView webview;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.e("webview", "######## LockService onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.e("webview", "######## LockService onStartCommand");

		LockView.handler.sendEmptyMessage(0);

		webview = new WebView(this);
		webview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 600));
		webview.setBackgroundColor(Color.RED);

		Log.e("webview", "########  LockView.mainView = " + LockView.mainView);
		Log.e("webview", "########  webview = " + webview);

		LockView.mainView.addView(webview);

		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}
		});
		webview.loadUrl("file:///android_asset/h5Test/index.html");
		Log.e("webview", "######## loadWebView webview.loadUrl done !!!!");
		// webview only end
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.e("webview", "######## LockService onDestroy");
		super.onDestroy();
	}

}
