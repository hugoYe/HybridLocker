package com.coco.lock2.local.app.lephone;


import com.coco.lock2.local.app.base.IWrap;

import android.content.Context;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;



public class LockWrap implements IWrap
{
	
	private static final String LOG_TAG = "LockWrap";
	private LockView lockView = null;
	private Context context = null;
	private Callback kernelCallback = null;
	private String simInf = "";
	private Callback appService = new Callback() {
		
		@Override
		public boolean handleMessage(
				Message msg )
		{
			switch( msg.what )
			{
				case APP_LOGINFO:
					if( msg.obj != null )
					{
						Log.d( LOG_TAG , "APP_LOGINFO=" + msg.obj.toString() );
					}
					else
					{
						Log.d( LOG_TAG , "APP_LOGINFO=(null)" );
					}
					return true;
				case APP_REMOTE_CONTEXT:
					Context remoteContext = (Context)msg.obj;
					Log.d( LOG_TAG , "APP_REMOTE_CONTEXT=" + remoteContext );
					Log.d( LOG_TAG , "CONTEXT=" + context );
					Log.d( LOG_TAG , "APP_CONTEXT=" + context.getApplicationContext() );
					Log.d( LOG_TAG , "APP_CONTEXT=" + remoteContext.getApplicationContext() );
					return true;
				case NOTIFY_APP_SIMCARD_NAME:
					Log.d( LOG_TAG , "notify,int" );
					Log.d( LOG_TAG , "notify,(String) msg.obj = " + (String)msg.obj );
					if( lockView != null )
					{
						Log.d( LOG_TAG , "notify,out" );
						simInf = (String)msg.obj;
						//lockView.setSimInfo(simInf);
					}
					else
					{
						simInf = (String)msg.obj;
					}
					break;
			}
			return false;
		}
	};
	
	public LockWrap(
			Context context )
	{
		this.context = context;
	}
	
	@Override
	public void onCreate()
	{
		lockView = new LockView( context , simInf );
		lockView.setExitFunction( new Runnable() {
			
			@Override
			public void run()
			{
				finish();
			}
		} );
		lockView.setWrap( this );
	}
	
	@Override
	public void onDestroy()
	{
		Log.d( LOG_TAG , "onDestroy" );
		lockView.onViewDestroy();
	}
	
	@Override
	public void onResume()
	{
		Log.d( LOG_TAG , "onResume" );
		lockView.onViewResume();
	}
	
	@Override
	public void onPause()
	{
		Log.d( LOG_TAG , "onPause" );
		lockView.onViewPause();
	}
	
	@Override
	public View getView()
	{
		return lockView;
	}
	
	@Override
	public void setKernelCallback(
			Callback callback )
	{
		kernelCallback = callback;
	}
	
	@Override
	public Callback getAppService()
	{
		return appService;
	}
	
	private void finish()
	{
		Log.d( LOG_TAG , "finish" );
		Message msg = Message.obtain();
		msg.what = KERNEL_EXIT;
		kernelCallback.handleMessage( msg );
		msg.recycle();
	}
	
	public void resetLight()
	{
		Log.d( LOG_TAG , "resetLight" );
		Message msg = Message.obtain();
		msg.what = KERNEL_RESET_LIGHT;
		kernelCallback.handleMessage( msg );
		msg.recycle();
	}
}
