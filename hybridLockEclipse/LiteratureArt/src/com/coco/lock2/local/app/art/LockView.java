package com.coco.lock2.local.app.art;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LockView extends FrameLayout {
	public static LinearLayout mainView = null;
	private Button unlockButton = null;
	private Button lightButton = null;
	private TextView textView = null;
	private String str = "123456789";
	private WebView webview;

	public static final Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Log.e("", "######## handleMessage");
		}

	};

	public LockView(Context remoteContext, Context context, String simInfo) {
		super(context);

		// setBackgroundColor(Color.WHITE);
		// setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT));
		// setPadding(0, 300, 0, 0);
		// setOrientation(LinearLayout.VERTICAL);

		mainView = (LinearLayout) ((LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.dialog_layout, null);
		Log.e("webview", "######## 111 mainView = " + mainView);
		unlockButton = (Button) mainView.findViewById(R.id.unlock);
		unlockButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mRunnable != null) {
					mRunnable.run();
				}
			}
		});
		lightButton = (Button) mainView.findViewById(R.id.light);
		lightButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mLockWrap != null) {
					mLockWrap.resetLight();
				}
			}
		});
		textView = (TextView) mainView.findViewById(R.id.mListView);
		textView.setText(str);
		Log.e("webview", "######## 222 mainView = " + mainView);
		addView(mainView);
		Log.e("webview", "######## 333 mainView = " + mainView);

		loadWebView(remoteContext, context);
	}

	private void loadWebView(Context remoteContext, Context context) {

		Log.e("webview", "######## loadWebView context = " + context);
		Log.e("webview", "######## loadWebView remoteContext = "
				+ remoteContext);
		File file = context.getDir("www", Context.MODE_PRIVATE);
		Log.e("webview",
				"######## file.getAbsolutePath() = " + file.getAbsolutePath());
		try {
			FileUtils.copyAssetDirToFiles(remoteContext, context, "h5Test");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// String remotePackageName = remoteContext.getPackageName();
		// Log.e("webview", "######## loadWebView remotePackageName = "
		// + remotePackageName);
		// File file = remoteContext.getDir("www", Context.MODE_PRIVATE);
		// Log.e("webview", "######## loadWebView file.exists = " +
		// file.exists());
		// String path = file.getAbsolutePath();
		// Log.e("webview", "######## loadWebView file.path = " + path);

		// service begin
		// Intent service = new Intent();
		// service.setClassName(context.getPackageName(),
		// "com.coco.lock2.local.app.art.LockService");
		// Log.e("webview", "######## loadWebView context.getPackageName() = "
		// + context.getPackageName());
		// context.startService(service);
		// Log.e("webview", "######## loadWebView context.startService done!!");
		// service end

		// webview only begin
		webview = new WebView(remoteContext);
		Log.e("webview", "######## loadWebView webview = " + webview);
		webview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 600));
		webview.setBackgroundColor(Color.TRANSPARENT);
		mainView.addView(webview);
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
		String urlString = "file:///" + remoteContext.getFilesDir() + "/"
				+ "h5Test" + "/index.html";
		webview.loadUrl(urlString);
		Log.e("webview", "######## loadWebView webview.loadUrl done !!!!");
		// webview only end
	}

	private Runnable mRunnable = null;

	public void setExitFunction(Runnable runnable) {
		mRunnable = runnable;
	}

	private LockWrap mLockWrap = null;

	public void setWrap(LockWrap wrap) {
		mLockWrap = wrap;
	}

}
