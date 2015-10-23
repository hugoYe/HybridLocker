/***/
package com.coco.lock2.local.app.base;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;


/**
 * @author gaominghui   2015年6月20日
 */
public class Tools
{
	
	public static HashMap<String , String> setTime(
			Context context ,
			Date date )
	{
		date.setTime( System.currentTimeMillis() );
		HashMap<String , String> time = new HashMap<String , String>();
		String hour = null;
		String minute = null;
		String week = null;
		String strDate = null;
		String ampm = null;
		if( DateFormat.is24HourFormat( context ) )
		{
			hour = String.format( "%tH" , date );
			ampm = " " + " ";
		}
		else
		{
			hour = String.format( "%tI" , date );
			ampm = String.format( Locale.US , "%tp" , date );
		}
		minute = String.format( "%tM" , date );
		Locale locale = context.getResources().getConfiguration().locale;
		String language = locale.getLanguage();
		SimpleDateFormat format = null;
		if (language.endsWith("zh")){
			format = new SimpleDateFormat("MM月dd日");
		}else {
			format = new SimpleDateFormat("MM - dd");
		}
		strDate = format.format( date );
		
		week = String.format( "%tA" , date );
		time.put( "hour" , hour );
		time.put( "minute" , minute );
		time.put( "ampm" , ampm );
		time.put( "date" , strDate );
		time.put( "week" , week );
		return time;
	}
}
