package com.coco.lock2.local.app.blinkCircle;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;


public class BCWrap implements IWrap
{
	
	private static final String LOG_TAG = "BCWrap";
	private FrameContainer mLockView = null;
	private LockView lockView = null;
	private Context context = null;
	private Callback kernelCallback = null;
	private String simInf = null;
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
					return true;
				case NOTIFY_APP_SIMCARD_NAME:
					Log.d( LOG_TAG , "notify,int" );
					Log.d( LOG_TAG , "notify,(String) msg.obj = " + (String)msg.obj );
					if( lockView != null )
					{
						Log.d( LOG_TAG , "notify,out" );
						lockView.setSimInfo( (String)msg.obj );
						lockView.postInvalidate();
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
	
	private String getSimInfo()
	{
		Log.d( LOG_TAG , "kernelCallback,in" );
		if( kernelCallback == null )
		{
			return "";
		}
		Log.d( LOG_TAG , "kernelCallback,out" );
		Message msg = Message.obtain();
		msg.what = REQUEST_KERNEL_SEND_SIMCARD_NAME;
		kernelCallback.handleMessage( msg );
		Log.d( LOG_TAG , "kernelCallback,msg.obj = " + (String)msg.obj );
		String strSim = (String)msg.obj;
		msg.recycle();
		if( strSim == null )
		{
			strSim = "";
		}
		return strSim;
	}
	private WebView webView;
	public BCWrap(
			Context context,WebView webview )
	{
		this.context = context;
		this.webView = webview;
	}
	
	@Override
	public void onCreate()
	{
		Log.d( LOG_TAG , "onCreate,t=1.0.2" );
		mLockView = new FrameContainer( context );
		//		lockView = new LockView(context,simInf);
		mLockView.setExitFunction( new Runnable() {
			
			@Override
			public void run()
			{
				
				finish();
			}
		} );
		mLockView.setupViews( webView );
		mLockView.setWrap( this );
	}
	
	@Override
	public void onDestroy()
	{
		Log.d( LOG_TAG , "onDestroy" );
		mLockView.onViewDestroy();
	}
	
	@Override
	public void onResume()
	{
		Log.d( LOG_TAG , "onResume" );
		mLockView.onViewResume();
	}
	
	@Override
	public void onPause()
	{
		Log.d( LOG_TAG , "onPause" );
		mLockView.onViewPause();
	}
	
	@Override
	public View getView()
	{
		return mLockView;
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
		if( kernelCallback != null )
		{
			Message msg = Message.obtain();
			msg.what = KERNEL_EXIT;
			kernelCallback.handleMessage( msg );
			msg.recycle();
		}
	}
	
	public void resetLight()
	{
		Log.d( LOG_TAG , "resetLight" );
		if( kernelCallback != null )
		{
			Message msg = Message.obtain();
			msg.what = KERNEL_RESET_LIGHT;
			kernelCallback.handleMessage( msg );
			msg.recycle();
		}
	}
}
