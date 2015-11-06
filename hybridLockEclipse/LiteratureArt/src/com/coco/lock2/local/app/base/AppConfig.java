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
	
	private boolean isFullScreen = true;
	private int chargeY = 0;
	private boolean isMTK = false;
	private String weatherPackage = "";
	private String weatherClass = "";
	private boolean clickToWeather = false;
	private boolean IsYiyunMusic = false;
	private boolean useAnim = true;
	private float unlockV = 0;
	private float bounceV = 0;
	private boolean hasWeather = true;
	private String backgroundPath1 = "";
	private String backgroundPath2 = "";
	private float unlockLen = 0;
	
	public float getUnlockLen()
	{
		return unlockLen;
	}
	
	public boolean isHasWeather()
	{
		return hasWeather;
	}
	
	public float getBounceV()
	{
		return bounceV;
	}
	
	public float getUnlockV()
	{
		return unlockV;
	}
	
	public boolean isUseAnim()
	{
		return useAnim;
	}
	
	public boolean IsYiyunMusic()
	{
		return IsYiyunMusic;
	}
	
	public boolean clickToWeather()
	{
		return clickToWeather;
	}
	
	public String getWeatherPackage()
	{
		return weatherPackage;
	}
	
	public String getWeatherClass()
	{
		return weatherClass;
	}
	
	public String getBackgroundPath1()
	{
		return backgroundPath1;
	}
	
	public String getBackgroundPath2()
	{
		return backgroundPath2;
	}
	
	public boolean isMTK()
	{
		return isMTK;
	}
	
	public int getChargeY()
	{
		return chargeY;
	}
	
	public boolean isFullScreen()
	{
		return isFullScreen;
	}
	
	private AppConfig()
	{
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
		if( itemName.equals( "backgroundPath1" ) )
		{
			backgroundPath1 = itemValue;
		}
		else if( itemName.equals( "backgroundPath2" ) )
		{
			backgroundPath2 = itemValue;
		}
		else if( itemName.equals( "isMTK" ) )
		{
			isMTK = itemValue.equals( "true" );
		}
		else if( itemName.equals( "isFullScreen" ) )
		{
			isFullScreen = itemValue.equals( "true" );
		}
		else if( itemName.equals( "chargeY" ) )
		{
			chargeY = Integer.parseInt( itemValue );
		}
		else if( itemName.equals( "weatherPackage" ) )
		{
			weatherPackage = itemValue;
		}
		else if( itemName.equals( "weatherClass" ) )
		{
			weatherClass = itemValue;
		}
		else if( itemName.equals( "clickToWeather" ) )
		{
			clickToWeather = itemValue.equals( "true" );
		}
		else if( itemName.equals( "IsYiyunMusic" ) )
		{
			IsYiyunMusic = Boolean.parseBoolean( itemValue );
		}
		else if( itemName.equals( "useAnim" ) )
		{
			useAnim = Boolean.parseBoolean( itemValue );
		}
		else if( itemName.equals( "unlockV" ) )
		{
			unlockV = Float.parseFloat( itemValue );
		}
		else if( itemName.equals( "bounceV" ) )
		{
			bounceV = Float.parseFloat( itemValue );
		}
		else if( itemName.equals( "hasWeather" ) )
		{
			hasWeather = Boolean.parseBoolean( itemValue );
		}
		else if( itemName.equals( "unlockLen" ) )
		{
			unlockLen = Float.parseFloat( itemValue );
		}
		else
		{
			Log.e( "AppConfig" , "ERROR item:" + itemName + "=" + itemValue );
		}
	}
}
