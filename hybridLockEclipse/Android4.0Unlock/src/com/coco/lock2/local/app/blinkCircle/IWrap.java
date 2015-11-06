package com.coco.lock2.local.app.blinkCircle;

import android.os.Handler.Callback;
import android.view.View;

public interface IWrap {
	void onCreate();

	void onDestroy();

	void onResume();

	void onPause();

	View getView();

	void setKernelCallback(Callback callback);

	Callback getAppService();

	int KERNEL_EXIT = 10000;
	int KERNEL_RESET_LIGHT = 10001;
	int REQUEST_KERNEL_SEND_SIMCARD_NAME = 10002;

	int APP_LOGINFO = 20000;
	int APP_REMOTE_CONTEXT = 20001;
	int NOTIFY_APP_SIMCARD_NAME = 20002;
}
