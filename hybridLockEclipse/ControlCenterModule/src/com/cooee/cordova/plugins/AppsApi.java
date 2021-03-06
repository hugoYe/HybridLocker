package com.cooee.cordova.plugins;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import com.coco.lock2.local.app.base.AppInfo;
import com.coco.lock2.local.app.base.Tools;
import com.cooee.pinyin4j.hp.hpl.sparta.xpath.ThisNodeTest;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AppsApi extends CordovaPlugin {

	private static final String TAG = "AppsApiPlugin";

	public final String ACTION_CHECK_AVAILABILITY = "checkAvailability";
	public final String ACTION_START_ACTIVITY = "startActivity";
	public final String ACTION_START_SHORTCUT = "startShortcut";
	public final String ACTION_START_BROWSER_URL = "startUrl";
	public final String ACTION_BIND_FAVORITE_APP = "bindFavoriteApp";

	private CallbackContext mCallbackContext;

	public interface UnlockListener {
		public void onUnlock();
	}

	private static UnlockListener mUnlockListener;

	public static void setOnUnlockListener(UnlockListener unlockListener) {
		mUnlockListener = unlockListener;
	}

	@Override
	public boolean execute(String action, final JSONArray args,
			CallbackContext callbackContext) throws JSONException {

		mCallbackContext = callbackContext;

		if (action.equals(ACTION_CHECK_AVAILABILITY)) {
			String uri = args.getString(0);
			this.checkAvailability(uri, callbackContext);
			return true;
		} else if (action.equals(ACTION_START_ACTIVITY)) {
			startApp(args);
			return true;
		} else if (action.equals(ACTION_START_SHORTCUT)) {
			startShortcut(args);
			return true;
		} else if (action.equals(ACTION_START_BROWSER_URL)) {
			startUrl(args);
			return true;
		} else if (action.equals(ACTION_BIND_FAVORITE_APP)) {
			bindWebFavoriteApp();
			return true;
		}
		return false;
	}

	// Thanks to
	// http://floresosvaldo.com/android-cordova-plugin-checking-if-an-app-exists
	public boolean appInstalled(String uri) {
		Context ctx = null;
		if (this.cordova.getActivity() != null) {
			ctx = this.cordova.getActivity().getApplicationContext();
		} else if (this.cordova.getContext() != null) {
			ctx = this.cordova.getContext();
		}
		final PackageManager pm = ctx.getPackageManager();
		boolean app_installed = false;
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
		}
		return app_installed;
	}

	private void startApp(final JSONArray args) {
		Log.e(TAG, "######## startActivity 111");
		if (cordova.getActivity() != null) {
			Log.e(TAG, "######## startActivity 222");
			cordova.getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Log.e(TAG, "######## startActivity 333");
					try {

						String method = args.getString(0);
						Log.d("", "intent=" + method);
						Intent intent;
						try {
							intent = Intent.parseUri(method, 0);
						} catch (Exception e) {
							intent = new Intent(Intent.ACTION_VIEW);
							Uri uri = Uri.parse(method);
							intent.setData(uri);
						}
						if (mUnlockListener != null) {
							mUnlockListener.onUnlock();
						}
						cordova.getActivity().startActivity(intent);
					} catch (JSONException ex) {
						mCallbackContext.sendPluginResult(new PluginResult(
								PluginResult.Status.JSON_EXCEPTION));
					}
				}

			});
		} else if (cordova.getContext() != null) {
			Log.e(TAG, "######## startActivity 444, cordova.getContext() = "
					+ cordova.getContext());
			cordova.getCordovaWrap().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Log.e(TAG, "######## startActivity 555");
					try {

						String method = args.getString(0);
						Log.i(TAG, "######## startActivity--- method = "
								+ method);
						Intent intent;
						try {
							intent = Intent.parseUri(method, 0);
							Log.i(TAG, "######## startActivity--- intent = "
									+ intent.toString());

						} catch (Exception e) {
							intent = new Intent(Intent.ACTION_VIEW);
							Uri uri = Uri.parse(method);
							intent.setData(uri);
						}
						if (mUnlockListener != null) {
							mUnlockListener.onUnlock();
						}
						cordova.getContext().startActivity(intent);
					} catch (JSONException ex) {
						mCallbackContext.sendPluginResult(new PluginResult(
								PluginResult.Status.JSON_EXCEPTION));
					}
				}

			});
		}
	}

	private void startShortcut(final JSONArray args) {
		if (cordova.getActivity() != null) {
			cordova.getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {

					Intent intent = null;
					try {
						String url = args.getString(0);
						String title = args.getString(1);
						String base64String = args.getString(2);
						Log.d(TAG, "####### url=" + url);
						Log.d(TAG, "####### title=" + title);
						Log.d(TAG, "####### base64String=" + base64String);

						try {
							intent = new Intent(Intent.ACTION_VIEW);
							intent.addCategory(Intent.CATEGORY_BROWSABLE);
							intent.setFlags((intent.getFlags() & ~Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
									| Intent.FLAG_ACTIVITY_NEW_TASK);
							Uri uri = Uri.parse(url);
							intent.setData(uri);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (mUnlockListener != null) {
							mUnlockListener.onUnlock();
						}
						cordova.getActivity().startActivity(intent);
					} catch (JSONException ex) {
						mCallbackContext.sendPluginResult(new PluginResult(
								PluginResult.Status.JSON_EXCEPTION));
					}
				}

			});
		} else if (cordova.getContext() != null) {

			cordova.getCordovaWrap().runOnUiThread(new Runnable() {

				@Override
				public void run() {

					Log.e(TAG, "######## startUrl 111, cordova.getContext() = "
							+ cordova.getContext());
					Intent intent = null;
					try {
						String url = args.getString(0);
						String title = args.getString(1);
						String base64String = args.getString(2);
						Log.e(TAG, "######## startUrl 222, url = " + url);
						Log.d(TAG, "####### url=" + url);
						Log.d(TAG, "####### title=" + title);
						Log.d(TAG, "####### base64String=" + base64String);

						try {
							intent = new Intent(Intent.ACTION_VIEW);
							intent.addCategory(Intent.CATEGORY_BROWSABLE);
							intent.setFlags((intent.getFlags() & ~Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
									| Intent.FLAG_ACTIVITY_NEW_TASK);
							Uri uri = Uri.parse(url);
							intent.setData(uri);
						} catch (Exception e) {
							e.printStackTrace();
						}
						Log.e(TAG, "######## startUrl 333, intent = " + intent);
						if (mUnlockListener != null) {
							mUnlockListener.onUnlock();
						}
						cordova.getContext().startActivity(intent);
					} catch (JSONException ex) {
						mCallbackContext.sendPluginResult(new PluginResult(
								PluginResult.Status.JSON_EXCEPTION));
					}
				}

			});
		}
	}

	private void startUrl(final JSONArray args) {
		Log.e(TAG,
				"######## startUrl, cordova.getContext() = "
						+ cordova.getContext());
		if (cordova.getActivity() != null) {

			cordova.getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {

					Intent intent = null;
					try {
						String url = args.getString(0);
						Log.d("", "url=" + url);

						try {
							intent = new Intent(Intent.ACTION_VIEW);
							intent.addCategory(Intent.CATEGORY_BROWSABLE);
							intent.setFlags((intent.getFlags() & ~Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
									| Intent.FLAG_ACTIVITY_NEW_TASK);
							Uri uri = Uri.parse(url);
							intent.setData(uri);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (mUnlockListener != null) {
							mUnlockListener.onUnlock();
						}
						cordova.getActivity().startActivity(intent);
					} catch (JSONException ex) {
						mCallbackContext.sendPluginResult(new PluginResult(
								PluginResult.Status.JSON_EXCEPTION));
					}
				}

			});
		} else if (cordova.getContext() != null) {

			cordova.getCordovaWrap().runOnUiThread(new Runnable() {

				@Override
				public void run() {

					Log.e(TAG, "######## startUrl 111, cordova.getContext() = "
							+ cordova.getContext());
					Intent intent = null;
					try {
						String url = args.getString(0);
						Log.e(TAG, "######## startUrl 222, url = " + url);

						try {
							intent = new Intent(Intent.ACTION_VIEW);
							intent.addCategory(Intent.CATEGORY_BROWSABLE);
							intent.setFlags((intent.getFlags() & ~Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
									| Intent.FLAG_ACTIVITY_NEW_TASK);
							Uri uri = Uri.parse(url);
							intent.setData(uri);
						} catch (Exception e) {
							e.printStackTrace();
						}
						Log.e(TAG, "######## startUrl 333, intent = " + intent);
						if (mUnlockListener != null) {
							mUnlockListener.onUnlock();
						}
						cordova.getContext().startActivity(intent);
					} catch (JSONException ex) {
						mCallbackContext.sendPluginResult(new PluginResult(
								PluginResult.Status.JSON_EXCEPTION));
					}
				}

			});
		}
	}

	private void checkAvailability(String uri, CallbackContext callbackContext) {
		if (appInstalled(uri)) {
			callbackContext.success();
		} else {
			callbackContext.error("");
		}
	}

	public void bindWebFavoriteApp() {
		Context ctx = null;
		if (this.cordova.getActivity() != null) {
			ctx = this.cordova.getActivity().getApplicationContext();
		} else if (this.cordova.getContext() != null) {
			ctx = this.cordova.getContext();
		}

		final JSONObject object = new JSONObject();// 创建一个总的对象，这个对象对整个json串
		JSONArray jsonarray = new JSONArray();// json数组，里面包含的内容为pet的所有对象
		Log.i(TAG, "######## bindWebFavoriteApp---context = " + ctx);
		ArrayList<AppInfo> list = Tools.recentTasks(ctx);
		Log.i(TAG, "######## bindWebFavoriteApp,list.size = " + list.size());
		for (int i = 0; i < list.size(); i++) {
			AppInfo app = list.get(i);
			try {
				JSONObject jsonObj = new JSONObject();// pet对象，json形式
				Log.i(TAG,
						"######## bindWebFavoriteApp--- app.appName = "
								+ app.appName + ", app.appIntent = "
								+ app.appIntent.toString()
								+ ", app.appIntent.toUri(0) = "
								+ app.appIntent.toUri(0));
				jsonObj.put("intent", app.appIntent.toUri(0));
				String base64 = Tools.bitmapToBase64(Tools
						.createIconBitmap(app.appIcon));
				jsonObj.put("bitmap", base64);
				// 把每个数据当作一对象添加到数组里
				jsonarray.put(jsonObj);// 向json数组里面添加对象

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.i(TAG,
						"######## bindWebFavoriteApp--- exception111 = "
								+ e.toString());
				e.printStackTrace();
			}
		}
		try {
			object.put("app", jsonarray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.i(TAG,
					"######## bindWebFavoriteApp--- exception222 = "
							+ e.toString());
			e.printStackTrace();
		}

		sendJS("javascript:bindWebFavoriteApp" + "(" + object.toString() + ");");

		Log.i(TAG, "######## bindWebFavoriteApp done !!!!");
	}

	private void sendJS(final String js) {
		if (cordova.getActivity() != null) {
			cordova.getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					webView.loadUrl(js);
				}
			});
		} else if (cordova.getCordovaWrap() != null) {
			cordova.getCordovaWrap().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					webView.loadUrl(js);
				}
			});
		}
	}
}
