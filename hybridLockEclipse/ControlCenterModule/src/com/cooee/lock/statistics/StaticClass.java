package com.cooee.lock.statistics;


import com.cooee.shell.sdk.CooeeSdk;
import com.cooee.statistics.StatisticsExpandNew;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;


public class StaticClass extends Service
{
	
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
		
		if( intent != null )
		{
			String sn = intent.getStringExtra( "SN" );
			String appid = intent.getStringExtra( "APPID" );
			String packagename = intent.getStringExtra( "package" );
			String launcherVersion = intent.getStringExtra( "Version" );
			{
				// @gaominghui2015/08/19 ADD START 添加push
				Log.i( "StaticClass" , "before init CooeeSdk!!" );
				CooeeSdk.initCooeeSdk( this );
				// @gaominghui2015/08/19 ADD END
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
					StatisticsExpandNew.register( getApplicationContext() , sn , appid , "" , 1 , packagename , launcherVersion );
				}
				else
				{
					Log.d( "clear" , "is not first run" );
					StatisticsExpandNew.use( getApplicationContext() , sn , appid , "" , 1 , packagename , launcherVersion );
				}
				sqliteDatabase.close();
			}
		}
		stopSelf();
		return START_NOT_STICKY;
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
}
