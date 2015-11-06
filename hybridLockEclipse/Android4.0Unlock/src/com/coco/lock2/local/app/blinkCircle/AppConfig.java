package com.coco.lock2.local.app.blinkCircle;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import android.content.Context;
import android.util.Log;

public class AppConfig {
	private static AppConfig mInstance = null;

	public static AppConfig getInstance(Context context) {
		if (mInstance != null) {
			return mInstance;
		}
		AppConfig ins = new AppConfig();
		ins.loadXml(context);
		mInstance = ins;
		return mInstance;
	}

	private String default_lockscreen_package = "";// 默认解锁的包�?
	private String default_lockscreen_class = ""; // 默认解锁的类�?
	private String default_lockscreen_wrap = "";// 默认解锁的反射名
	private boolean zheshanVisible = true; // 内置解锁是否显示折扇
	private boolean systemWallpaper = false; // S4解锁是否使用系统壁纸
	private boolean lightColor = false;// S4是否�?.3版本显示浅色图片

	private int volume = 100;//
	private boolean needIcon = false;
	private int unlockTime = 1000;
	private int xinYiX = 0;
	private int xinYiY = 0;
	private int xinYiProportion = 0;
	public String timeXy = "0";
	public int slidingScreenWidth = 0;
	public boolean useSystemIconStyle = false;
	private int IconX = 0;
	private int IconY = 0;
	private int SimX = 0;
	private int SimY = 0;
	private int PromptX = 0;
	private int PromptY = 0;
	private int IconProportion = 0;
	private boolean xinYiDisplay = true;
	private int pHoneIconIn = 0;
	private int sMsIconIn = 0;
	private int timefontsize = 100;
	private int datefontsize = 36;
	static String[] PackageName = new String[4];
	static String[] ClassName = new String[4];
	static String[] ImageNames = new String[4];
	private String bgPath = "";
	private boolean Increaseweek = false;
	private boolean systemWallpaper4_0 = false;
	private int missingInformationY = 0;
	private int mSmsbl = 100;
	private boolean Accordingtocarrier = true;
	private int Powerfontsize = 30;
	private int powerX = 0;
	private int powerY = 0;
	private boolean missingInformationTimeDis = true;
	private boolean AmFixed = true;
	private boolean shielding = false;
	private boolean LandscapeDraw = true;
	private boolean statusbarupdate = true;
	private boolean displayOwner= false;
	private int ownerY = 0;
	private int ownerfont = 30;
	private int slidingScreenUpWidth = 30;
	private int mSmsblY = 0;
	private boolean dateLimit = true;
	
	private int dataX = 0;
	private int dataY = 0;
	private int dataFontSize=0;
	private int timeX = 0;
	private int timeY = 0;
	private int timeFontSize=0;
	private int ampmX = 0;
	private int ampmY = 0;
	private int ampmFontSize=0;
	private int callX = 0;
	private int callY = 0;
	private int smsX = 0;
	private int smsY = 0;
	private int fontSize=0;
	private int chargeX = 0;
	private int chargeY = 0;
	private int chargeFontSize=0;
	
	public boolean dateLimit() {
		return dateLimit;
	}
	public int mSmsblY() {
		return mSmsblY;
	}
	public int slidingScreenUpWidth() {
		return slidingScreenUpWidth;
	}
	public int ownerY() {
		return ownerY;
	}
	public int ownerfont() {
		return ownerfont;
	}
	public boolean isDisplayOwner() {
		return displayOwner;
	}
	public boolean statusbarupdate() {
	return statusbarupdate;
}
public boolean LandscapeDraw() {
	return LandscapeDraw;
}

	public boolean shielding() {
		return shielding;
	}
	
	public boolean AmFixed() {
		return AmFixed;
	}

	public boolean missingInformationTimeDis() {
		return missingInformationTimeDis;
	}

	public int Powerfontsize() {
		return Powerfontsize;
	}

	public int powerX() {
		return powerX;
	}

	public int powerY() {
		return powerY;
	}

	public boolean Accordingtocarrier() {
		return Accordingtocarrier;
	}

	public int mSmsbl() {
		return mSmsbl;
	}

	public int missingInformationY() {
		return missingInformationY;
	}

	public boolean systemWallpaper4_0() {
		return systemWallpaper4_0;
	}

	public boolean Increaseweek() {
		return Increaseweek;
	}

	public int unlockTime() {
		return unlockTime;
	}

	public int datefontsize() {
		return datefontsize;
	}

	public int timefontsize() {
		return timefontsize;
	}

	public String bgPath() {
		return bgPath;
	}

	public int pHoneIconIn() {
		return pHoneIconIn;
	}

	public int sMsIconIn() {
		return sMsIconIn;
	}

	public String[] PackageName() {
		return PackageName;
	}

	public String[] ClassName() {
		return ClassName;
	}

	public String[] ImageNames() {
		return ImageNames;
	}

	public boolean xinYiDisplay() {
		return xinYiDisplay;
	}

	public int dataX() {
		return dataX;
	}

	public int dataY() {
		return dataY;
	}

	public int dataFontSize(){
		return dataFontSize;
	}
	
	public int timeX() {
		return timeX;
	}

	public int timeY() {
		return timeY;
	}
	
	public int timeFontSize(){
		return timeFontSize;
	}
	
	public int ampmX(){
		return ampmX;
	}
	
	public int ampmY(){
		return ampmY;
	}

	public int ampmFontSize(){
		return ampmFontSize;
	}
	
	public int callX(){
		return callX;
	}
	
	public int callY(){
		return callY;
	}
	
	public int smsX(){
		return smsX;
	}
	
	public int smsY(){
		return smsY;
	}
	
	public int fontSize(){
		return fontSize;
	}
	
	public int chargeX(){
		return chargeX;
	}
	
	public int chargeY(){
		return chargeY;
	}
	
	public int chargeFontSize(){
		return chargeFontSize;
	}
	
	public boolean useSystemIconStyle() {
		return useSystemIconStyle;
	}

	public int slidingScreenWidth() {
		return slidingScreenWidth;
	}

	public int IconProportion() {
		return IconProportion;
	}

	public int IconX() {
		return IconX;
	}

	public int IconY() {
		return IconY;
	}

	public int SimX() {
		return SimX;
	}

	public int SimY() {
		return SimY;
	}

	public int PromptX() {
		return PromptX;
	}

	public int PromptY() {
		return PromptY;
	}

	public int xinYiProportion() {
		return xinYiProportion;
	}

	public int xinYiX() {
		return xinYiX;
	}

	public int xinYiY() {
		return xinYiY;
	}

	public String timeXy() {
		return timeXy;
	}

	public boolean needIcon() {
		return needIcon;
	}

	public int volume() {
		return volume;
	}

	public boolean lightColor() {
		return lightColor;
	}

	public boolean getSystemWallpaper() {
		return systemWallpaper;
	}

	public boolean getZheshanVisible() {
		return zheshanVisible;
	}

	public String getDefaultLockscreenPackage() {
		return default_lockscreen_package;
	}

	public String getDefaultLockscreenClass() {
		return default_lockscreen_class;
	}

	public String getDefaultLockscreenWrap() {
		return default_lockscreen_wrap;
	}
	
	private AppConfig() {
	}

	private void loadXml(Context context) {
		InputStream xmlStream = null;
		XmlPullParser xmlPull = null;
		try {
			File file = new File("system/appconfigs4.xml");		
			if(!file.exists())
			{
				xmlStream = context.getAssets().open("appconfig.xml");
			}
			else
			{
				xmlStream = new FileInputStream(file);
			}
			xmlPull = XmlPullParserFactory.newInstance().newPullParser();
			xmlPull.setInput(xmlStream, "UTF-8");

			int eventType = xmlPull.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_TAG: {//
					if ("item".equals(xmlPull.getName())) {
						String itemName = getAttributeValue(xmlPull, "name", "");
						String itemValue = getAttributeValue(xmlPull, "value",
								"");
						readItem(itemName, itemValue);
					}
				}
					break;
				default:
					break;
				}
				eventType = xmlPull.next();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} finally {
			if (xmlStream != null) {
				try {
					xmlStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String getAttributeValue(XmlPullParser pull, String attName,
			String defaultValue) {
		for (int i = 0; i < pull.getAttributeCount(); i++) {
			if (pull.getAttributeName(i).equals(attName)) {
				return pull.getAttributeValue(i);
			}
		}
		return defaultValue;
	}

	private void readItem(String itemName, String itemValue) {
		Log.v("AppConfig", itemName + "=" + itemValue);
		if (itemName.equals("zheshanVisible")) {
			zheshanVisible = itemValue.equals("true");
		} else if (itemName.equals("default_lockscreen_package")) {
			default_lockscreen_package = itemValue;
		} else if (itemName.equals("default_lockscreen_class")) {
			default_lockscreen_class = itemValue;
		} else if (itemName.equals("default_lockscreen_wrap")) {
			default_lockscreen_wrap = itemValue;
		} else if (itemName.equals("systemWallpaper")) {
			systemWallpaper = itemValue.equals("true");
		} else if (itemName.equals("lightColor")) {
			lightColor = itemValue.equals("true");
		} else if (itemName.equals("volume")) {
			volume = Integer.parseInt(itemValue);
		} else if (itemName.equals("timeXy")) {
			timeXy = itemValue;
		} else if (itemName.equals("xinYiX")) {
			xinYiX = Integer.parseInt(itemValue);
		} else if (itemName.equals("xinYiY")) {
			xinYiY = Integer.parseInt(itemValue);
		} else if (itemName.equals("xinYiProportion")) {
			xinYiProportion = Integer.parseInt(itemValue);
		} else if (itemName.equals("needIcon")) {
			needIcon = itemValue.equals("true");
		} else if (itemName.equals("slidingScreenWidth")) {
			slidingScreenWidth = Integer.parseInt(itemValue);
		} else if (itemName.equals("useSystemIconStyle")) {
			useSystemIconStyle = itemValue.equals("true");
		} else if (itemName.equals("IconX")) {
			IconX = Integer.parseInt(itemValue);
		} else if (itemName.equals("SimX")) {
			SimX = Integer.parseInt(itemValue);
		} else if (itemName.equals("SimY")) {
			SimY = Integer.parseInt(itemValue);
		} else if (itemName.equals("IconY")) {
			IconY = Integer.parseInt(itemValue);
		} else if (itemName.equals("PromptX")) {
			PromptX = Integer.parseInt(itemValue);
		} else if (itemName.equals("PromptY")) {
			PromptY = Integer.parseInt(itemValue);
		} else if (itemName.equals("IconProportion")) {
			IconProportion = Integer.parseInt(itemValue);
		} else if (itemName.equals("dataX")) {
			dataX = Integer.parseInt(itemValue);
		} else if (itemName.equals("dataY")) {
			dataY = Integer.parseInt(itemValue);
		} else if (itemName.equals("dataFontSize")) {
			dataFontSize = Integer.parseInt(itemValue);
		} else if (itemName.equals("timeX")) {
			timeX = Integer.parseInt(itemValue);
		} else if (itemName.equals("timeY")) {
			timeY = Integer.parseInt(itemValue);
		} else if (itemName.equals("timeFontSize")) {
			timeFontSize = Integer.parseInt(itemValue);
		} else if (itemName.equals("ampmX")) {
			ampmX = Integer.parseInt(itemValue);
		} else if (itemName.equals("ampmY")) {
			ampmY = Integer.parseInt(itemValue);
		} else if (itemName.equals("ampmFontSize")) {
			ampmFontSize = Integer.parseInt(itemValue);
		} else if (itemName.equals("callX")) {
			callX = Integer.parseInt(itemValue);
		} else if (itemName.equals("callY")) {
			callY = Integer.parseInt(itemValue);
		} else if (itemName.equals("smsX")) {
			smsX = Integer.parseInt(itemValue);
		} else if (itemName.equals("smsY")) {
			smsY = Integer.parseInt(itemValue);
		} else if (itemName.equals("fontSize")) {
			fontSize = Integer.parseInt(itemValue);
		} else if (itemName.equals("chargeX")) {
			chargeX = Integer.parseInt(itemValue);
		} else if (itemName.equals("chargeY")) {
			chargeY = Integer.parseInt(itemValue);
		} else if (itemName.equals("chargeFontSize")) {
			chargeFontSize = Integer.parseInt(itemValue);
		} else if (itemName.equals("xinYiDisplay")) {
			xinYiDisplay = itemValue.equals("true");
		} else if (itemName.equals("pHoneIconIn")) {
			pHoneIconIn = Integer.parseInt(itemValue);
		} else if (itemName.equals("SmsIconIn")) {
			sMsIconIn = Integer.parseInt(itemValue);
		}
		// //////////////////包名///////////////////
		else if (itemName.equals("Icon1PackageName")) {
			PackageName[0] = itemValue;
		} else if (itemName.equals("Icon2PackageName")) {
			PackageName[1] = itemValue;
		} else if (itemName.equals("Icon3PackageName")) {
			PackageName[2] = itemValue;
		} else if (itemName.equals("Icon4PackageName")) {
			PackageName[3] = itemValue;
		}
		// //////////////////类名///////////////////
		else if (itemName.equals("Icon1ClassName")) {
			ClassName[0] = itemValue;
		} else if (itemName.equals("Icon2ClassName")) {
			ClassName[1] = itemValue;
		} else if (itemName.equals("Icon3ClassName")) {
			ClassName[2] = itemValue;
		} else if (itemName.equals("Icon4ClassName")) {
			ClassName[3] = itemValue;
		}
		// //////////////////图标名字///////////////////
		else if (itemName.equals("Icon1ImageNames")) {
			ImageNames[0] = itemValue;
		} else if (itemName.equals("Icon2ImageNames")) {
			ImageNames[1] = itemValue;
		} else if (itemName.equals("Icon3ImageNames")) {
			ImageNames[2] = itemValue;
		} else if (itemName.equals("Icon4ImageNames")) {
			ImageNames[3] = itemValue;
		}
		// //////////////////////////////////////////
		else if (itemName.equals("bgPath")) {
			bgPath = itemValue;
		} else if (itemName.equals("timefontsize")) {
			timefontsize = Integer.parseInt(itemValue);
		} else if (itemName.equals("datefontsize")) {
			datefontsize = Integer.parseInt(itemValue);
		} else if (itemName.equals("unlockTime")) {
			unlockTime = Integer.parseInt(itemValue);
		} else if (itemName.equals("Increaseweek")) {
			Increaseweek = itemValue.equals("true");
		} else if (itemName.equals("systemWallpaper4_0")) {
			systemWallpaper4_0 = itemValue.equals("true");
		} else if (itemName.equals("missingInformationY")) {
			missingInformationY = Integer.parseInt(itemValue);
		} else if (itemName.equals("mSmsbl")) {
			mSmsbl = Integer.parseInt(itemValue);
		} else if (itemName.equals("Accordingtocarrier")) {
			Accordingtocarrier = itemValue.equals("true");
		} else if (itemName.equals("Powerfontsize")) {
			Powerfontsize = Integer.parseInt(itemValue);
		} else if (itemName.equals("powerX")) {
			powerX = Integer.parseInt(itemValue);
		} else if (itemName.equals("powerY")) {
			powerY = Integer.parseInt(itemValue);
		} else if (itemName.equals("missingInformationTimeDis")) {
			missingInformationTimeDis = itemValue.equals("true");
		} else if (itemName.equals("AmFixed")) {
			AmFixed = itemValue.equals("true");
		} else if (itemName.equals("shielding")) {
			shielding = itemValue.equals("true");
		} else if (itemName.equals("LandscapeDraw")) {
			LandscapeDraw = itemValue.equals("true");
		}  else if (itemName.equals("statusbarupdate")) {
			statusbarupdate = itemValue.equals("true");
		} else if(itemName.equals("DisplayOwner")){
			displayOwner=itemValue.equals("true");
		}else if (itemName.equals("ownerY")) {
			ownerY = Integer.parseInt(itemValue);
		}else if (itemName.equals("ownerfont")) {
			ownerfont = Integer.parseInt(itemValue);
		}  else if (itemName.equals("slidingScreenUpWidth")) {
			slidingScreenUpWidth = Integer.parseInt(itemValue);
		}  else if (itemName.equals("mSmsblY")) {
			mSmsblY = Integer.parseInt(itemValue);
		} else if (itemName.equals("dateLimit")) {
			dateLimit = itemValue.equals("true");
		}else {
			Log.e("AppConfig", "ERROR item:" + itemName + "=" + itemValue);
		}
	}
}
