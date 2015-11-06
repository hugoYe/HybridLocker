package com.coco.lock2.local.app.art;

import android.app.Application;
import android.util.Log;

public class LockApplication extends Application {

	private static LockApplication sLockApplication;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.e("webview", "######## LockApplication  = " + this);
		sLockApplication = this;
	}

	public static LockApplication getInstance() {
		Log.e("webview", "######## LockApplication getInstance  = "
				+ sLockApplication);
		return sLockApplication;
	}
}
