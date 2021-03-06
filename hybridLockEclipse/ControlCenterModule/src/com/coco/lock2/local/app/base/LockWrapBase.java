package com.coco.lock2.local.app.base;

import com.cooee.cordova.plugins.AppsApi;
import com.cooee.cordova.plugins.AppsApi.UnlockListener;

import android.content.Context;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;

abstract public class LockWrapBase implements IWrap, UnlockListener {

	private static final String LOG_TAG = "LockWrap";

	private static Callback kernelCallback;

	private LockViewContainer mLockView;
	private View mCustomLockView;

	protected Context context;
	protected Context remoteContext;
	protected String simInf = "";

	private Callback appService = new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case APP_LOGINFO:
				if (msg.obj != null) {
					Log.d(LOG_TAG, "APP_LOGINFO=" + msg.obj.toString());
				} else {
					Log.d(LOG_TAG, "APP_LOGINFO=(null)");
				}
				return true;
			case APP_REMOTE_CONTEXT:
				remoteContext = (Context) msg.obj;
				Log.d(LOG_TAG, "APP_REMOTE_CONTEXT=" + remoteContext);
				Log.d(LOG_TAG, "CONTEXT=" + context);
				Log.d(LOG_TAG, "APP_CONTEXT=" + context.getApplicationContext());
				Log.d(LOG_TAG,
						"APP_CONTEXT=" + remoteContext.getApplicationContext());
				return true;
			case NOTIFY_APP_SIMCARD_NAME:
				Log.d(LOG_TAG, "notify,(String) msg.obj = " + (String) msg.obj);
				simInf = (String) msg.obj;

				break;
			}
			return false;
		}

	};

	abstract public View createLockView();
	
	public LockWrapBase(Context context){
		this.context = context;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		AppsApi.setOnUnlockListener(this);
		mCustomLockView = createLockView();
		mLockView = new LockViewContainer(context, remoteContext);
		mLockView.setupViews(mCustomLockView);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mLockView.onViewDestroy();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		mLockView.onViewResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		mLockView.onViewPause();
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return mLockView;
	}

	@Override
	public void setKernelCallback(Callback callback) {
		// TODO Auto-generated method stub
		kernelCallback = callback;
	}

	@Override
	public Callback getAppService() {
		// TODO Auto-generated method stub
		return appService;
	}

	public static void unLock() {
		Log.d(LOG_TAG, "finish");
		Message msg = Message.obtain();
		msg.what = KERNEL_EXIT;
		kernelCallback.handleMessage(msg);
		msg.recycle();
	}

	public static void resetLight() {
		Log.d(LOG_TAG, "resetLight");
		Message msg = Message.obtain();
		msg.what = KERNEL_RESET_LIGHT;
		kernelCallback.handleMessage(msg);
		msg.recycle();
	}

	@Override
	public void onUnlock() {
		// TODO Auto-generated method stub
		unLock();
	}

}
