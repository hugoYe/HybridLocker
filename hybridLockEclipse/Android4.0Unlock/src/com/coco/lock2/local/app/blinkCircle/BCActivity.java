package com.coco.lock2.local.app.blinkCircle;


import java.io.File;

import org.apache.cordova.CordovaActivity;

import android.os.Bundle;
import android.os.Message;
import android.os.Handler.Callback;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Window;
import android.webkit.WebView;


public class BCActivity extends CordovaActivity
{
	
	private BCWrap ps;
	
	@Override
	public void onCreate(
			Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		this.requestWindowFeature( Window.FEATURE_NO_TITLE );
		SharedPreferences sharedPrefer = getSharedPreferences( "Update" , Context.MODE_WORLD_WRITEABLE );
		if( sharedPrefer.getBoolean( "update_complete" , false ) )
		{
			String launchUrl = getDir("update", Context.MODE_PRIVATE ).getAbsolutePath()+"/www/index.html";
			File file = new File( launchUrl );
			if( file.exists() )
			{
				launchUrl = "file://"+launchUrl;
			}else {
				launchUrl = "file:///android_asset/testproject/test.html";
			}
		}else {
			launchUrl = "file:///android_asset/testproject/test.html";
		}
		// cordova begin
		WebView webview = (WebView)loadWebViewUrl( launchUrl );
		webview.setBackgroundColor( Color.TRANSPARENT );
		// cordova end
		ps = new BCWrap( this,webview );
		ps.setKernelCallback( new Callback() {
			
			@Override
			public boolean handleMessage(
					Message msg )
			{
				switch( msg.what )
				{
					case IWrap.KERNEL_EXIT:
						finish();
						return true;
				}
				return false;
			}
		} );
		ps.onCreate();
		setContentView( ps.getView() );
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		ps.onResume();
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		ps.onDestroy();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		ps.onPause();
	}
}
