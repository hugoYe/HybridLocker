package com.coco.lock2.local.app.blinkCircle;


import android.content.Context;
import android.content.Intent;
import android.os.Handler.Callback;
import android.widget.FrameLayout;


public class LockView extends FrameLayout
{
	
	private BCView mLock4Screen;
	private Context mContext;
	
	public LockView(
			Context context,String simInfo )
	{
		super( context );
		mContext = context;
		
		mLock4Screen = new BCView( context,simInfo );
		addView( mLock4Screen );
		Intent intent = new Intent();
		intent.setClassName( mContext , "com.coco.lock.operations.service.OperationsService" );
		context.startService( intent );
	}
	
	
	
	public void setSimInfo(String sim){
		mLock4Screen.mSImStr = sim;
	}
	
	public void setWrap(
			BCWrap w )
	{
		mLock4Screen.setWrap( w );
	}
	
	public void setKernelCallback(
			Callback callback )
	{
		
	}
	
	public void setExitFunction(
			Runnable run )
	{
		mLock4Screen.setExitFunction( run );
		
	}
	
	public void onViewDestroy()
	{
		mLock4Screen.onViewDestroy();
		
	}
	
	public void onViewResume()
	{
		mLock4Screen.onViewResume();
		
	}
	
	public void onViewPause()
	{
		mLock4Screen.onViewPause();
		
	}
}
