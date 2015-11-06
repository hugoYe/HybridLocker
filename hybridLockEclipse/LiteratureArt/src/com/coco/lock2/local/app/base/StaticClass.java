package com.coco.lock2.local.app.base;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;
import com.cooee.statistics.StatisticsExpandNew;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateStatus;


public class StaticClass extends Service
{
	private boolean startMobclickAgent = true;
	private boolean resumeMobclickAgent = false;
	
	@Override
	public IBinder onBind(
			Intent intent )
	{
		Log.v( "xxxx" , "xxxxx" );
		return null;
	}
	
	@Override
	public int onStartCommand(
			Intent intent ,
			int flags ,
			int startId )
	{
		super.onStartCommand( intent , flags , startId );
		if( startMobclickAgent )
		{
			startMobclickAgent = false;
			Log.v( "StaticClass" , "startMobclickAgent" );
			UmengUpdateAgent.setAppkey( "5604e1d6e0f55a3ef8001d36" );
			UmengUpdateAgent.setChannel( "test" );
			UmengUpdateAgent.setUpdateUIStyle(UpdateStatus.STYLE_NOTIFICATION);
			UmengUpdateAgent.silentUpdate(this);
			
			AnalyticsConfig.setAppkey( "5604e1d6e0f55a3ef8001d36" );
			AnalyticsConfig.setChannel( "test" );
			MobclickAgent.updateOnlineConfig( this );
		}
		if( !resumeMobclickAgent )
		{
			resumeMobclickAgent = true;
			Log.v( "StaticClass" , "status = resume" );
			MobclickAgent.onResume( this );
		}else {
			resumeMobclickAgent = false;
			Log.v( "StaticClass" , "status = pause" );
			MobclickAgent.onPause( this );
			startMobclickAgent = true;
			stopSelf();
		}
		
//		if( intent.getStringExtra( "status" ).equals( "resume" ) )
//		{
//			Log.v( "StaticClass" , "status = resume" );
//			MobclickAgent.onResume( this );
//		}else if( intent.getStringExtra( "status" ).equals( "pause" ) )
//		{
//			Log.v( "StaticClass" , "status = pause" );
//			MobclickAgent.onPause( this );
//			stopSelf();
//		}else 
		{
//			Log.v( "StaticClass" , "status = null" );
//			UmengUpdateAgent.setAppkey( "5604e1d6e0f55a3ef8001d36" );
//			UmengUpdateAgent.setChannel( "test" );
//			UmengUpdateAgent.setUpdateUIStyle(UpdateStatus.STYLE_NOTIFICATION);
//			UmengUpdateAgent.silentUpdate(this);
//			
//			AnalyticsConfig.setAppkey( "5604e1d6e0f55a3ef8001d36" );
//			AnalyticsConfig.setChannel( "test" );
//			MobclickAgent.updateOnlineConfig( this );
			
			IntentFilter filter = new IntentFilter();
			filter.addAction( ACTION_RESUME );
			filter.addAction( ACTION_PAUSE );
			registerReceiver( mobclickAgentReceiver , filter );
			
			String sn = intent.getStringExtra( "SN" );
			String appid = intent.getStringExtra( "APPID" );
			String packagename = intent.getStringExtra( "package" );
			int launcherVersion = intent.getIntExtra( "Version" , 1 );
			Dbhelp dbhelp = new Dbhelp( getApplicationContext() , "lockbase.db" );
			SQLiteDatabase sqliteDatabase = dbhelp.getWritableDatabase();
			Log.d( "clear" , "in first run sqliteDatabase = " + sqliteDatabase );
			if( !dbhelp.onSerch( sqliteDatabase , "locktable" ) )
			{
				// dbhelp.onCreateTable(sqliteDatabase, "locktable");
				ContentValues values = new ContentValues();
				values.put( "_id" , 1 );
				values.put( "num" , 1 );
				sqliteDatabase.insert( "locktable" , null , values );
				Log.d( "clear" , "is first run" );
				// xiatian add start //StatisticsNew
				StatisticsExpandNew.register( getApplicationContext() , sn , appid , "" , 1 , packagename , "" + launcherVersion );
			}
			else
			{
				Log.d( "clear" , "is not first run" );
				StatisticsExpandNew.use( getApplicationContext() , sn , appid , "" , 1 , packagename , "" + launcherVersion );
			}
			sqliteDatabase.close();
		}
//		stopSelf();
		return START_NOT_STICKY;
	}
	
	public static final String ACTION_RESUME = "coco.lock.mobclick.agent.resume";
	public static final String ACTION_PAUSE = "coco.lock.mobclick.agent.pause";
	private BroadcastReceiver mobclickAgentReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(
				Context context ,
				Intent intent )
		{
			String action = intent.getAction();
			if( action.equals( ACTION_RESUME ) )
			{
				Log.v( "StaticClass" , "ACTION_RESUME" );
				onResume();
			}else if( action.equals( ACTION_PAUSE ) ){
				Log.v( "StaticClass" , "ACTION_PAUSE" );
				onPause();
			}
		}
	};
	
	private void onResume()
	{
//		MobclickAgent.onResume( this );
		MobclickAgent.onEvent(this, "click_test","蓝牙");
	}
	
	private void onPause()
	{
		MobclickAgent.onPause( this );
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if( mobclickAgentReceiver != null )
		{
			unregisterReceiver( mobclickAgentReceiver );
			mobclickAgentReceiver = null;
		}
//		startMobclickAgent = true;
//		stopSelf();
	}
}
