package com.coco.lock2.local.app.base;

import java.io.IOException;

import org.apache.cordova.CordovaWrap;

import com.cooee.cordova.plugins.TouchEventPrevent;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;

final public class LockViewContainer extends FrameLayout implements IBaseView {

	private static final int MSG_COPY_FILE_SUCCESS = 0;
	private static final String SP_KEY_COPY_FILE_SUCCESS = "copyFileSuccess";

	private Context mContext;
	private Context mRemoteContext;
	private CordovaWrap mCordovaWrap;
	private WebView mWebView;
	private View mLockView;

	public LockViewContainer(Context context, Context remoteContext) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		mRemoteContext = remoteContext;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			mLockView.onTouchEvent(ev);
		}

		if (TouchEventPrevent.preventWebTouchEvent) {
			mLockView.onTouchEvent(ev);

			if (ev.getAction() == MotionEvent.ACTION_UP) {
				TouchEventPrevent.preventWebTouchEvent = false;
			}
			return true;
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public void onViewResume() {
		// TODO Auto-generated method stub
		mCordovaWrap.onResume();
		if (mLockView instanceof IBaseView) {
			((IBaseView) mLockView).onViewResume();
		}
	}

	@Override
	public void onViewPause() {
		// TODO Auto-generated method stub
		mCordovaWrap.onPause();
		if (mLockView instanceof IBaseView) {
			((IBaseView) mLockView).onViewPause();
		}
	}

	@Override
	public void onViewDestroy() {
		// TODO Auto-generated method stub
		mCordovaWrap.onDestroy();
		if (mLockView instanceof IBaseView) {
			((IBaseView) mLockView).onViewDestroy();
		}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == MSG_COPY_FILE_SUCCESS) {
				initialWebview();
			}
		}
	};

	private void initialWebview() {
		if (mRemoteContext != null) {
			mCordovaWrap.launchUrl = "file:///" + mRemoteContext.getFilesDir()
					+ "/" + "www" + "/index.html";
		} else {
			mCordovaWrap.launchUrl = "file:///" + mContext.getFilesDir() + "/"
					+ "www" + "/index.html";
		}

		mWebView = (WebView) mCordovaWrap
				.loadWebViewUrl(mCordovaWrap.launchUrl);
		mWebView.setBackgroundColor(Color.TRANSPARENT);
		mWebView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				1100));
		addView(mWebView);
	}

	public void setupViews(View customView) {
		mLockView = customView;
		addView(customView);

		mCordovaWrap = new CordovaWrap(mContext, mRemoteContext);
		mCordovaWrap.onCreate(null);
		final SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(mContext);
		if (!sp.getBoolean(SP_KEY_COPY_FILE_SUCCESS, false)) {
			ThreadUtil.execute(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						FileUtils.copyAssetDirToFiles(mRemoteContext, mContext,
								"www");
						sp.edit().putBoolean(SP_KEY_COPY_FILE_SUCCESS, true)
								.commit();
						mHandler.sendEmptyMessage(MSG_COPY_FILE_SUCCESS);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		} else {
			initialWebview();
		}
	}

}
