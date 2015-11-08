package com.coco.lock2.local.app.lephone;


import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.coco.lock2.local.app.base.AppConfig;
import com.coco.lock2.local.app.base.Assets;
import com.coco.lock2.local.app.base.Tools;
import com.cooee.hybridlocker.R;
import com.cooee.statistics.StatisticsBaseNew;
import com.cooee.statistics.StatisticsExpandNew;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;


public class LockView extends View {

    private static final String TAG = "LockView";
    private Context mContext;
    private AppConfig mAppConfig = null;
    private boolean mUseStatistics = true;
    private String appid = null;
    private String sn = null;
    private int launcherVersion = -1;
    private float mScreenWidth = 0;
    private float mScreenHeight = 0;
    private float mScaleX = 0;


    private float TIME_X = 0;
    private static float TIME_Y = 295;
    private static float TIME_GAP = 14;
    private static float AMPM_GAP = 12;
    //	private static float CHARGE_Y = 500;
    private static float UNLOCK_Y = 70;//相对于底边
    private static float LEPHONE_Y = 255;//相对于底边
    private float mTimeWidth = 0;
    private float mDateX = 0;
    private float mWeekX = 0;
    private Drawable mDrawableBg = null;
    private Drawable mDrawableUnlock = null;
    private Drawable mDrawableLephone = null;
    private Drawable mDrawableMask = null;
    private Drawable mDrawableTime[] = null;
    private Drawable mDrawableColon = null;
    private Drawable mDrawableAm = null;
    private Drawable mDrawablePm = null;
    private HashMap<String, String> mTime = new HashMap<String, String>();
    private Date mDate = null;
    private int mHour;
    private int mMinute;
    private String mStringAmPm;
    private String mStringWeek;
    private String mStringDate;
    private Paint mPaint = null;
    //	private boolean mDisplayUnlock = true;
    private float mTouchDownX = 0;
    private float mMoveX = 0;
    private PowerManager mPm = null;
    private boolean mIsCharge = false;
    private int mPower = 99;
    private boolean mShowUnlock = true;
    private ValueAnimator mHideAnimator = null;
    //	private ValueAnimator showAnimator = null;
    private String mBackgroundPath1 = "";
    private String mBackgroundPath2 = "";
    /**
     * 接收系统更新的广播，ACTION_TIME_TICK 每分钟发一次
     */
    private BroadcastReceiver mChangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(
            Context context,
            Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                int power = intent.getIntExtra("level", 0);
                if (mPower != power) {
                    mPower = power;
                }
                int
                    statePower =
                    intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);
                if (statePower == BatteryManager.BATTERY_STATUS_CHARGING
                    || statePower == BatteryManager.BATTERY_STATUS_FULL) {
                    mIsCharge = true;
                    if (mPm.isScreenOn()) {
                        postInvalidate();
                    }
                } else if (statePower == BatteryManager.BATTERY_STATUS_DISCHARGING) {
                    mIsCharge = false;
                    if (mPm.isScreenOn()) {
                        postInvalidate();
                    }
                }
            } else if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
                mIsCharge = false;
                if (mPm.isScreenOn()) {
                    postInvalidate();
                }
            } else if (action.equals(Intent.ACTION_POWER_CONNECTED)) {
                mIsCharge = true;
                if (mPm.isScreenOn()) {
                    postInvalidate();
                }
            } else if (action.equals(Intent.ACTION_TIME_TICK)) {
                if (mPm != null && mPm.isScreenOn()) {
                    updateTime();
                }
            }
        }
    };


    public LockView(
        Context context,
        String simInfo) {
        super(context);
        mContext = context;
        mPm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        initAppConfig();
        StartStatisticsService();
        calibration();
        initElement();
        initDrawable();
        initReceiver();
        initAnimator();
        updateTime();
    }

    private void initAppConfig() {
        mAppConfig = AppConfig.getInstance(mContext);
        if (mAppConfig != null) {
            mBackgroundPath1 = mAppConfig.backgroundPath1();
            mBackgroundPath2 = mAppConfig.backgroundPath2();
        }

        UNLOCK_Y += mAppConfig.unlockY();
        LEPHONE_Y += mAppConfig.lephoneY();
    }

    private void initAnimator() {
        mHideAnimator = ValueAnimator.ofInt(255, 0, 255).setDuration(500);
        mHideAnimator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(
                    ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                Log.i("test", "value: " + value);
                if (mDrawableUnlock != null) {
                    mDrawableUnlock.setAlpha(value);
                }
                postInvalidate();
                if (!mShowUnlock && value < 50) {
                    if (mDrawableUnlock != null) {
                        mDrawableUnlock.setAlpha(0);
                    }
                    if (Build.VERSION.SDK_INT >= 19) {
                        animation.pause();
                    }
                }
            }
        });
    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        mContext.registerReceiver(mChangeReceiver, filter);
    }

    private void initElement() {
        mDate = new Date();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(0xFF343101);
        mPaint.setTextSize(29 * mScaleX);
    }

    private void initDrawable() {

        mDrawableBg = getBitmapBySimpleSize(mBackgroundPath1, mScreenWidth, mScreenHeight);
        if (mDrawableBg == null) {
            mDrawableBg = getBitmapBySimpleSize(mBackgroundPath2, mScreenWidth, mScreenHeight);
        }
        if (mDrawableBg != null) {
            float
                sc =
                Math.max((float) mScreenWidth / (float) mDrawableBg.getIntrinsicWidth(),
                         (float) mScreenHeight / (float) mDrawableBg.getIntrinsicHeight());
            mDrawableBg.setBounds(
                (int) (mScreenWidth - mDrawableBg.getIntrinsicWidth() * sc) / 2,
                (int) (mScreenHeight - mDrawableBg.getIntrinsicHeight() * sc) / 2,
                (int) (mScreenWidth - mDrawableBg.getIntrinsicWidth() * sc) / 2 + (int) (
                    mDrawableBg.getIntrinsicWidth() * sc),
                (int) (mScreenHeight - mDrawableBg.getIntrinsicHeight() * sc) / 2 + (int) (
                    mDrawableBg.getIntrinsicHeight() * sc));
        }
        mDrawableUnlock = mContext.getResources().getDrawable(R.drawable.slide_unlock);
        mDrawableLephone = mContext.getResources().getDrawable(R.drawable.lephone);
        mDrawableMask = mContext.getResources().getDrawable(R.drawable.mask);
        mDrawableTime = new Drawable[10];
        for (int i = 0; i < 10; i++) {
            mDrawableTime[i] = mContext.getResources().getDrawable(R.drawable.time_0 + i);
        }
        mDrawableColon = mContext.getResources().getDrawable(R.drawable.colon);
        mDrawableAm = mContext.getResources().getDrawable(R.drawable.am);
        mDrawablePm = mContext.getResources().getDrawable(R.drawable.pm);
    }

    private void StartStatisticsService() {
        if (mUseStatistics) {
            StatisticsBaseNew.setApplicationContext(mContext);
            StatisticsExpandNew.setStatiisticsLogEnable(true);
            Assets.initAssets(mContext);
            JSONObject tmp = Assets.config;
            PackageManager mPackageManager = mContext.getPackageManager();
            try {
                JSONObject config = tmp.getJSONObject("config");
                appid = config.getString("app_id");
                sn = config.getString("serialno");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                launcherVersion =
                    mPackageManager.getPackageInfo(mContext.getPackageName(), 0).versionCode;
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            // xiatian add end
            Log.d("clear", "before first run");

        }
    }

    private void calibration() {
        Resources res = mContext.getResources();
        int widthPixels = res.getDisplayMetrics().widthPixels;
        int heightPixels = res.getDisplayMetrics().heightPixels;
        mScreenHeight = Math.max(heightPixels, widthPixels);
        mScreenWidth = Math.min(widthPixels, heightPixels);
        mScaleX = mScreenWidth / 720f;
    }

    @Override
    protected void onDraw(
        Canvas canvas) {
        if (mDrawableBg == null) {
            canvas.drawColor(0xFFFFD800);
        } else {
            mDrawableBg.draw(canvas);
        }
        drawTime(canvas);
        drawCharge(canvas);
        drawLephone(canvas);
        drawUnlock(canvas);
        super.onDraw(canvas);
    }

    private void drawCharge(
        Canvas canvas) {
        if (mIsCharge) {
            String string = null;
            if (mPower < 100) {
                string = mContext.getString(R.string.charging) + "   " + mPower + "%";
            } else {
                string = mContext.getString(R.string.charged);
            }
            canvas.drawText(string, (mScreenWidth - mPaint.measureText(string)) / 2,
                            (TIME_Y + mDrawableTime[0].getIntrinsicHeight() + 122) * mScaleX,
                            mPaint);
        }
    }

    private void drawLephone(
        Canvas canvas) {
        int sc = canvas.saveLayer(
            (int) ((mScreenWidth - mDrawableLephone.getIntrinsicWidth() * mScaleX) / 2),
            (int) (mScreenHeight - LEPHONE_Y * mScaleX),
            (int) ((mScreenWidth - mDrawableLephone.getIntrinsicWidth() * mScaleX) / 2
                   + mDrawableLephone.getIntrinsicWidth() * mScaleX),
            (int) (mScreenHeight - LEPHONE_Y * mScaleX
                   + mDrawableLephone.getIntrinsicHeight() * mScaleX),
            null,
            Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
            | Canvas.FULL_COLOR_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG);
        mDrawableMask.setBounds(
            (int) ((mScreenWidth - mDrawableLephone.getIntrinsicWidth() * mScaleX) / 2
                   + mDrawableLephone.getIntrinsicWidth() * mScaleX
                   - mDrawableMask.getIntrinsicWidth() * mScaleX + mMoveX),
            (int) (mScreenHeight - LEPHONE_Y * mScaleX),
            (int) ((mScreenWidth - mDrawableLephone.getIntrinsicWidth() * mScaleX) / 2
                   + mDrawableLephone.getIntrinsicWidth() * mScaleX + mMoveX),
            (int) (mScreenHeight - LEPHONE_Y * mScaleX
                   + mDrawableLephone.getIntrinsicHeight() * mScaleX));
        mDrawableMask.draw(canvas);
        ((BitmapDrawable) mDrawableLephone).getPaint()
            .setXfermode(new PorterDuffXfermode(Mode.MULTIPLY));
        mDrawableLephone.setBounds(
            (int) ((mScreenWidth - mDrawableLephone.getIntrinsicWidth() * mScaleX) / 2),
            (int) (mScreenHeight - LEPHONE_Y * mScaleX),
            (int) ((mScreenWidth - mDrawableLephone.getIntrinsicWidth() * mScaleX) / 2
                   + mDrawableLephone.getIntrinsicWidth() * mScaleX),
            (int) (mScreenHeight - LEPHONE_Y * mScaleX
                   + mDrawableLephone.getIntrinsicHeight() * mScaleX));
        mDrawableLephone.draw(canvas);
        canvas.restoreToCount(sc);
    }

    private void drawUnlock(
        Canvas canvas) {
        //		if( mShowUnlock )
        //		{
        mDrawableUnlock.setBounds(
            (int) ((mScreenWidth - mDrawableUnlock.getIntrinsicWidth() * mScaleX) / 2),
            (int) (mScreenHeight - UNLOCK_Y * mScaleX),
            (int) ((mScreenWidth - mDrawableUnlock.getIntrinsicWidth() * mScaleX) / 2
                   + mDrawableUnlock.getIntrinsicWidth() * mScaleX),
            (int) (mScreenHeight - UNLOCK_Y * mScaleX
                   + mDrawableUnlock.getIntrinsicHeight() * mScaleX));
        mDrawableUnlock.draw(canvas);
        //		}
    }

    private void drawTime(
        Canvas canvas) {
        if (mHour / 10 == 0) {
            mTimeWidth =
                (3 * (mDrawableTime[0].getIntrinsicWidth() + TIME_GAP) + mDrawableColon
                    .getIntrinsicWidth()) * mScaleX;
        } else {
            mTimeWidth =
                (4 * (mDrawableTime[0].getIntrinsicWidth() + TIME_GAP) + mDrawableColon
                    .getIntrinsicWidth()) * mScaleX;
        }
        TIME_X = (int) ((mScreenWidth - mTimeWidth) / 2);
        mDateX = TIME_X;
        if (mHour / 10 != 0) {
            mDrawableTime[mHour / 10].setBounds(
                (int) (TIME_X),
                (int) (TIME_Y * mScaleX),
                (int) (TIME_X + mDrawableTime[0].getIntrinsicWidth() * mScaleX),
                (int) (TIME_Y * mScaleX + mDrawableTime[0].getIntrinsicHeight() * mScaleX));
            mDrawableTime[mHour / 10].draw(canvas);
            TIME_X += (mDrawableTime[0].getIntrinsicWidth() + TIME_GAP) * mScaleX;
        }
        mDrawableTime[mHour % 10].setBounds(
            (int) (TIME_X),
            (int) (TIME_Y * mScaleX),
            (int) (TIME_X + mDrawableTime[0].getIntrinsicWidth() * mScaleX),
            (int) (TIME_Y * mScaleX + mDrawableTime[0].getIntrinsicHeight() * mScaleX));
        mDrawableTime[mHour % 10].draw(canvas);
        TIME_X += (mDrawableTime[0].getIntrinsicWidth() + TIME_GAP) * mScaleX;
        mDrawableColon.setBounds(
            (int) (TIME_X),
            (int) (TIME_Y * mScaleX),
            (int) (TIME_X + mDrawableColon.getIntrinsicWidth() * mScaleX),
            (int) (TIME_Y * mScaleX + mDrawableTime[0].getIntrinsicHeight() * mScaleX));
        mDrawableColon.draw(canvas);
        TIME_X += (mDrawableColon.getIntrinsicWidth() + TIME_GAP) * mScaleX;
        mDrawableTime[mMinute / 10].setBounds(
            (int) (TIME_X),
            (int) (TIME_Y * mScaleX),
            (int) (TIME_X + mDrawableTime[0].getIntrinsicWidth() * mScaleX),
            (int) (TIME_Y * mScaleX + mDrawableTime[0].getIntrinsicHeight() * mScaleX));
        mDrawableTime[mMinute / 10].draw(canvas);
        TIME_X += (mDrawableTime[0].getIntrinsicWidth() + TIME_GAP) * mScaleX;
        mDrawableTime[mMinute % 10].setBounds(
            (int) (TIME_X),
            (int) (TIME_Y * mScaleX),
            (int) (TIME_X + mDrawableTime[0].getIntrinsicWidth() * mScaleX),
            (int) (TIME_Y * mScaleX + mDrawableTime[0].getIntrinsicHeight() * mScaleX));
        mDrawableTime[mMinute % 10].draw(canvas);
        mWeekX =
            TIME_X + mDrawableTime[0].getIntrinsicWidth() * mScaleX - mPaint
                .measureText(mStringWeek);
        TIME_X += (mDrawableTime[0].getIntrinsicWidth() + AMPM_GAP) * mScaleX;
        if (mStringAmPm.equals("am")) {
            mDrawableAm.setBounds(
                (int) (TIME_X),
                (int) (TIME_Y * mScaleX),
                (int) (TIME_X + mDrawableAm.getIntrinsicWidth() * mScaleX),
                (int) (TIME_Y * mScaleX + mDrawableAm.getIntrinsicHeight() * mScaleX));
            mDrawableAm.draw(canvas);
        } else if (mStringAmPm.equals("pm")) {
            mDrawablePm.setBounds(
                (int) (TIME_X),
                (int) (TIME_Y * mScaleX),
                (int) (TIME_X + mDrawablePm.getIntrinsicWidth() * mScaleX),
                (int) (TIME_Y * mScaleX + mDrawablePm.getIntrinsicHeight() * mScaleX));
            mDrawablePm.draw(canvas);
        }
        //		mPaint.setShadowLayer( 1 , 1 , 1 , 0xFF343101 );
        canvas.drawText(mStringDate, mDateX,
                        (TIME_Y + mDrawableTime[0].getIntrinsicHeight() + 44) * mScaleX, mPaint);
        canvas.drawText(mStringWeek, mWeekX,
                        (TIME_Y + mDrawableTime[0].getIntrinsicHeight() + 44) * mScaleX, mPaint);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i(TAG, "###### LockView dispatchTouchEvent action = " + event.getAction());
        return super.dispatchTouchEvent(event);
    }


    public void hybridTouchDown(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            mShowUnlock = false;
            mTouchDownX = event.getRawX();
            mDrawableUnlock.setAlpha(100);
        }
    }

    @Override
    public boolean onTouchEvent(
        MotionEvent event) {
//        Log.i(TAG, "######### LockView onTouchEvent action = " + event.getAction());
        LockWrap.resetLight();
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            mShowUnlock = false;
            mTouchDownX = event.getRawX();
            mDrawableUnlock.setAlpha(100);
//            mHideAnimator.start();
        } else
        if (action == MotionEvent.ACTION_MOVE) {
            if (mHideAnimator != null && !mHideAnimator.isStarted()) {
                mHideAnimator.start();
            }
            mMoveX = event.getRawX() - mTouchDownX;
            if (mMoveX <= 0) {
                mMoveX = 0;
            }
            postInvalidate();
        } else if (action == MotionEvent.ACTION_UP) {
            if (mMoveX > mAppConfig.unlockDis() * mScaleX) {
                LockWrap.unLock();
            } else {
                mShowUnlock = true;
                mMoveX = 0;
                postInvalidate();
                if (Build.VERSION.SDK_INT >= 19) {
                    mHideAnimator.resume();
                }
            }
        }
        return true;
    }

    //	private Runnable lePhoneRunnable = new Runnable() {
    //
    //		@Override
    //		public void run()
    //		{
    //			mDisplayLephone = false;
    //			postInvalidate();
    //		}
    //	};
    public void updateTime() {
        mDate.setTime(System.currentTimeMillis());
        if (mTime != null) {
            mTime.clear();
        }
        mTime = Tools.setTime(mContext, mDate);
        mHour = Integer.parseInt(mTime.get("hour"));
        mMinute = Integer.parseInt(mTime.get("minute"));
        mStringAmPm = mTime.get("ampm");
        mStringDate = mTime.get("date");
        mStringWeek = mTime.get("week");
        //		postInvalidate();
    }

    /**
     * according to the width to get bitmap,if image's width more than width, than compress the
     * image's width and height
     *
     * @param object image url
     * @param width  the width which need to be
     */
    public static Drawable getBitmapBySimpleSize(
        Object object,
        float width,
        float height) {
        if (null == object) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 通过这个bitmap获取图片的宽和高
        BitmapFactory.decodeFile(object.toString(), options);
        if (options.outWidth > width) {
            int scale = (int) (options.outWidth / width);
            // 如果按照宽度缩放比例导致高度小于原定高度，则按照高度的缩放比例来缩放
            if (options.outHeight / scale < height) {
                scale = (int) (options.outHeight / height);
            }
            scale = scale <= 0 ? 1 : scale;
            options.inSampleSize = scale;
            options.outWidth = options.outWidth / options.inSampleSize;
            options.outHeight = options.outHeight / options.inSampleSize;
        }
        if (options.outWidth <= 0) {
            return null;
        }
        setBitmapNormalOptions(options);
        // 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来的。
        return new BitmapDrawable(BitmapFactory.decodeFile(object.toString(), options));
    }

    private static void setBitmapNormalOptions(
        Options options) {
        if (null == options) {
            return;
        }
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
    }


    public void onViewCreate() {
    }


    public void onViewResume() {
        updateTime();
    }


    public void onViewPause() {
    }


    public void onViewDestroy() {
        if (mHideAnimator != null) {
            mHideAnimator.end();
        }
        if (mChangeReceiver != null) {
            mContext.unregisterReceiver(mChangeReceiver);
        }
        if (mDrawableBg != null) {
            mDrawableBg.setCallback(null);
            mDrawableBg = null;
        }
        if (mDrawableAm != null) {
            mDrawableAm.setCallback(null);
            mDrawableAm = null;
        }
        if (mDrawablePm != null) {
            mDrawablePm.setCallback(null);
            mDrawablePm = null;
        }
        if (mDrawableColon != null) {
            mDrawableColon.setCallback(null);
            mDrawableColon = null;
        }
        if (mDrawableLephone != null) {
            mDrawableLephone.setCallback(null);
            mDrawableLephone = null;
        }
        if (mDrawableMask != null) {
            mDrawableMask.setCallback(null);
            mDrawableMask = null;
        }
        if (mDrawableTime != null) {
            for (int i = 0; i < mDrawableTime.length; i++) {
                if (mDrawableTime[i] != null) {
                    mDrawableTime[i].setCallback(null);
                    mDrawableTime[i] = null;
                }
            }
        }
        if (mDrawableUnlock != null) {
            mDrawableUnlock.setCallback(null);
            mDrawableUnlock = null;
        }
    }
}
