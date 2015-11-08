package com.coco.lock2.local.app.base;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.util.Log;


public class AppConfig
{
	
	private static AppConfig mInstance = null;
	
	public static AppConfig getInstance(
			Context context )
	{
		if( mInstance != null )
		{
			return mInstance;
		}
		AppConfig ins = new AppConfig();
		ins.loadXml( context );
		mInstance = ins;
		return mInstance;
	}
	
	private int unlockDis = 360;
	private int unlockY = 70;
	private int lephoneY = 255;
	private int timeLephone = 1000;
	private String backgroundPath1 = "";
	private String backgroundPath2 = "";
	public int unlockY(){
		return unlockY;
	}
	public int lephoneY(){
		return lephoneY;
	}
	public String backgroundPath1()
	{
		return backgroundPath1;
	}
	
	public String backgroundPath2()
	{
		return backgroundPath2;
	}
	public int timeLephone(){
		return timeLephone;
	}
	public int unlockDis(){
		return unlockDis;
	}
	
	private void loadXml(
			Context context )
	{
		InputStream xmlStream = null;
		XmlPullParser xmlPull = null;
		try
		{
			String filename = "/mnt/sdcard/cocolock/" + context.getPackageName() + "/data/appconfig.xml";
			File file = new File( filename );
			if( file.exists() )
			{
				xmlStream = new FileInputStream( filename );
			}
			else
			{
				xmlStream = context.getAssets().open( "appconfig.xml" );
			}
			xmlPull = XmlPullParserFactory.newInstance().newPullParser();
			xmlPull.setInput( xmlStream , "UTF-8" );
			int eventType = xmlPull.getEventType();
			while( eventType != XmlPullParser.END_DOCUMENT )
			{
				switch( eventType )
				{
					case XmlPullParser.START_TAG:
					{//
						if( "item".equals( xmlPull.getName() ) )
						{
							String itemName = getAttributeValue( xmlPull , "name" , "" );
							String itemValue = getAttributeValue( xmlPull , "value" , "" );
							readItem( itemName , itemValue );
						}
					}
						break;
					default:
						break;
				}
				eventType = xmlPull.next();
			}
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
		catch( XmlPullParserException e )
		{
			e.printStackTrace();
		}
		finally
		{
			if( xmlStream != null )
			{
				try
				{
					xmlStream.close();
				}
				catch( IOException e )
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	private String getAttributeValue(
			XmlPullParser pull ,
			String attName ,
			String defaultValue )
	{
		for( int i = 0 ; i < pull.getAttributeCount() ; i++ )
		{
			if( pull.getAttributeName( i ).equals( attName ) )
			{
				return pull.getAttributeValue( i );
			}
		}
		return defaultValue;
	}
	
	private void readItem(
			String itemName ,
			String itemValue )
	{
		Log.d( "AppConfig" , itemName + "=" + itemValue );
		if (itemName.equals( "unlockDis" )){
			unlockDis = Integer.parseInt( itemValue );
		}else if (itemName.equals( "timeLephone" )){
			timeLephone = Integer.parseInt( itemValue );
		}else if( itemName.equals( "backgroundPath1" ) )
		{
			backgroundPath1 = itemValue;
		}
		else if( itemName.equals( "backgroundPath2" ) )
		{
			backgroundPath2 = itemValue;
		}else if(itemName.equals( "lephoneY" )){
			lephoneY = Integer.parseInt( itemValue );
		}else if (itemName.equals( "unlockY" )){
			unlockY = Integer.parseInt( itemValue );
		}
		else
		{
			Log.e( "AppConfig" , "ERROR item:" + itemName + "=" + itemValue );
		}
	}
}
