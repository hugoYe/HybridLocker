package com.cooee.cordova.plugins;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;

import android.content.Intent;
import android.util.Log;


public class EventStatistics extends CordovaPlugin
{
	public final String ACTION_CLICK_APP = "clickApp";
    public final String ACTION_OPEN_CONTROL_CENTER = "openCentrolCenter";
    
    @Override
    public boolean execute(
    		String action ,
    		CordovaArgs args ,
    		CallbackContext callbackContext ) throws JSONException
    {
    	if( action.equals( ACTION_CLICK_APP ) )
		{
    		clickApp();
    		return true;
		}else if ( action.equals( ACTION_OPEN_CONTROL_CENTER ) ){
			openCentrolCenter();
			return true;
		}
    	return false;
    }
    
    private void clickApp(){
		Log.e("LOG_TAG", "clickApp-java222");
    	Intent it = new Intent();
		it.setClassName(cordova.getActivity(),
				"com.cooee.lock.statistics.StaticClass");
		it.putExtra( "ClickEvent" , "HeartApp" );
//		it.putExtra("SN", sn);
//		it.putExtra("APPID", appid);
//		it.putExtra("Version", version);
		cordova.getActivity().startService( it );
    }
    
    private void openCentrolCenter(){
		Log.e("LOG_TAG", "openCentrolCenter-java222");
    	Intent it = new Intent();
		it.setClassName(cordova.getActivity(),
				"com.cooee.lock.statistics.StaticClass");
		it.putExtra( "ClickEvent" , "ControlCenter" );
//		it.putExtra("SN", sn);
//		it.putExtra("APPID", appid);
//		it.putExtra("Version", version);
		cordova.getActivity().startService( it );
    }
}
