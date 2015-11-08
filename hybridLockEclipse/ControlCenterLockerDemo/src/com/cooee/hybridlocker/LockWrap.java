package com.cooee.hybridlocker;

import android.content.Context;
import android.os.Handler.Callback;
import android.view.View;

import com.coco.lock2.local.app.base.LockWrapBase;

public class LockWrap extends LockWrapBase {

	public LockWrap(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
	}

	@Override
	public View createLockView() {
		// TODO Auto-generated method stub
		return new LockView(context, "");
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return super.getView();
	}

	@Override
	public void setKernelCallback(Callback callback) {
		// TODO Auto-generated method stub
		super.setKernelCallback(callback);
	}
	
	@Override
	public Callback getAppService() {
		// TODO Auto-generated method stub
		return super.getAppService();
	}

}
