package com.coco.lock2.local.app.blinkCircle;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.cooee.statistics.StatisticsBaseNew;
import com.cooee.statistics.StatisticsExpandNew;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.PowerManager;
import android.text.format.Time;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class BCView extends View {
    private static String LOG_TAG = "BCView";
    private Bitmap imgPoint;
    private Bitmap imgCircle;
    private Bitmap imgLock;
    private Bitmap imgPressLock;
    private Bitmap imgCover;
    private Bitmap imgUnlock;
    private Bitmap imgCover1;
    private static Bitmap imgPointCircle;

    private Paint paintP;
    private Paint paintPmove;
    private Paint paintTime;
    private Paint paintPanim;
    private Paint paintdown;
    private Paint paintCir;

    private Timer timer;
    private TimerTask BCtask;
    private TimerTask Longtask;
    private TimerTask Uptask;

    private int brightlevel;

    private float circleR;
    private float circleO_x;
    private float circleO_y;
    private int as;

    private float cur_x;
    private float cur_y;
    private float init_x;
    private float init_y;
    private float unlock_x;
    private float unlock_y;

    private float scaleWidth;
    private float scaleHeight;
    private float scaleWidthp;
    private float scaleHeightp;
    private float scaleWidthc;
    public int ScreenWidth;
    public int ScreenHeight;

    public int power;
    public boolean ischarge;
    public int chargePosition = 0;

    private int animMode = 0;
    private int count_max = 10;
    private int scaleNum = 30;
    private float scaleUpDown = 0.85f;
    private boolean blinking = false;

    public String language;
    private String charge_res;
    private Context mContext = null;
    private boolean flag;
    private boolean longandmove = false;
    private boolean canDrawPress = true;
    private boolean Increaseweek = false;
    private AppConfig mAppConfig = null;
    private Drawable bg;
    
	private MessageNotify msgNotify;
	private CallNotify callNotify;
	private int mUnreadSmsNum = 0;
	private int mMissedCallNum = 0;
	private Paint callAndSmsPaint;
	
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        ScreenHeight = h;
        ScreenWidth = w;

        scaleWidthp = 0.1f;
        scaleHeightp = 0.1f;

        scaleWidthc = scaleUpDown;

        timer = new Timer();
        brightlevel = -80;

        scaleWidth = ((float) ScreenWidth) / 480f;
        scaleHeight = ((float) ScreenHeight) / 800f;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleWidth);

        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(
                R.drawable.ic_lockscreen_handle_normal)).getBitmap();
        imgPressLock = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);

        bitmap = ((BitmapDrawable) getResources().getDrawable(
                R.drawable.ic_lockscreen_glowdot)).getBitmap();
        imgPoint = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        bitmap = ((BitmapDrawable) getResources().getDrawable(
                R.drawable.unlock_ring)).getBitmap();
        imgCircle = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        bitmap = ((BitmapDrawable) getResources()
                .getDrawable(R.drawable.cover4)).getBitmap();

        imgCover = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        bitmap = ((BitmapDrawable) getResources().getDrawable(
                R.drawable.unlock_default)).getBitmap();
        imgLock = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        bitmap = ((BitmapDrawable) getResources().getDrawable(
                R.drawable.choose_effect)).getBitmap();
        imgUnlock = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        bitmap = ((BitmapDrawable) getResources()
                .getDrawable(R.drawable.cover9)).getBitmap();
        imgCover1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);

        bg = getResources().getDrawable(R.drawable.bg2);
        bg.setBounds(0, 0, getWidth(), getHeight());
        
        init_x = 240f * scaleWidth;
        init_y = 461f * scaleHeight;
        circleR = imgCircle.getWidth() / 2;
        as = 4;
        circleO_x = init_x;
        circleO_y = init_y + imgPressLock.getHeight() / 2;
        imgPointCircle = CreatBitmap().copy(Bitmap.Config.ARGB_8888, true);
        PowerManager pm = (PowerManager) mContext
                .getSystemService(Context.POWER_SERVICE);
        Log.v(LOG_TAG, "pm.isScreenOn()=" + pm.isScreenOn());
        if (pm.isScreenOn()) {
            mHandlerMain.removeCallbacks(mRunFlush);
            mHandlerMain.postDelayed(mRunFlush, 0);
        }

        callAndSmsPaint.setTextSize((int)(mAppConfig.fontSize()*scaleWidth));
    }

    private boolean hasInCircle = false;
    public String mSImStr = "";
    private BroadcastReceiver mIntentReceiver;
    private IntentFilter mIntentFilter;

    public BCView(Context context, String sImStr) {
        super(context);
        mContext = context;
        mSImStr = sImStr;
        mAppConfig = AppConfig.getInstance(mContext);
        Increaseweek = mAppConfig.Increaseweek();
        paintP = new Paint();
        paintPmove = new Paint();
        paintPanim = new Paint();
        paintdown = new Paint();
        paintdown.setFilterBitmap(true);
        paintdown.setAntiAlias(true);
        paintTime = new Paint();
        paintTime.setColor(Color.WHITE);
        paintTime.setFilterBitmap(true);
        paintTime.setAntiAlias(true);
        paintTime.setShadowLayer(1, 2, 2, 0xff000000);
        paintCir = new Paint();
        paintCir.setFilterBitmap(true);
        paintCir.setAntiAlias(true);

        callAndSmsPaint = new Paint();
        callAndSmsPaint.setColor(Color.WHITE);
        callAndSmsPaint.setFilterBitmap(true);
        callAndSmsPaint.setAntiAlias(true);
        callAndSmsPaint.setShadowLayer(1, 2, 2, 0xff000000);
        
        timeStyle = android.text.format.DateFormat.is24HourFormat(context);
        Locale locale = getResources().getConfiguration().locale;
        language = locale.getLanguage();
        charge_res = context.getString(R.string.charging);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        mIntentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                    power = intent.getIntExtra("level", 0);
                    int statePower = intent.getIntExtra("status",
                            BatteryManager.BATTERY_STATUS_UNKNOWN);
                    if (statePower == BatteryManager.BATTERY_STATUS_CHARGING)
                    {
                        ischarge = true;
                    }
                    else
                    {
                        ischarge = false;
                    }
                }
            }
        };
        mContext.registerReceiver(mIntentReceiver, mIntentFilter);
		
		msgNotify = new MessageNotify();
		msgNotify.registerObserver(context, msgObserver);
		callNotify = new CallNotify();
		callNotify.registerObserver(context, callObserver);
		new CallChangeThread().start();
		new SmsChangeThread().start();
    }

    private SimpleDateFormat mWeek = new SimpleDateFormat("EEEE");
    private SimpleDateFormat mAMPM = new SimpleDateFormat("aa");
    private SimpleDateFormat time12Format = new SimpleDateFormat("hh:mm");
    private SimpleDateFormat minute24Format = new SimpleDateFormat("mm");
    private SimpleDateFormat hour24Format = new SimpleDateFormat("kk");
    private boolean timeStyle;

    @Override
    protected void onDraw(Canvas canvas) {       
    	bg.draw(canvas);
        
        if (canDrawPress) {
            canvas.drawBitmap(imgPressLock, init_x - imgPressLock.getWidth()
                    / 2, init_y, null);
        }
        // long draw
        if (longandmove) {
            Log.v("xxxxxx", "long draw");
            Bitmap cir = changeCircle();
            Bitmap lock = changeLock();
            canvas.drawBitmap(cir, circleO_x - cir.getWidth() / 2, circleO_y
                    - cir.getHeight() / 2, paintdown);
            canvas.drawBitmap(lock,
                    circleO_x + cir.getWidth() / 2 - lock.getWidth() / 2,
                    init_y, paintdown);
        }
        // down draw

        // move draw
        if(!canDrawPress){
        if (animMode == 2) {
            if (!flag) {
                drawCircle_t(canvas);

            } else if(hasInCircle){
                canvas.drawBitmap(imgUnlock, unlock_x - imgUnlock.getWidth()
                        / 2, unlock_y - imgUnlock.getHeight() / 2, null);
            }
        }
        }
        // up draw
        if (animMode == 1) {
            draw11circle(canvas);
        }
        Date curDate = new Date(System.currentTimeMillis());
        java.text.DateFormat dateFormat = android.text.format.DateFormat
                .getDateFormat(getContext());
        String str5 = "";
        if (Increaseweek) {
            str5 = mWeek.format(curDate);
        }
        String str3 = dateFormat.format(curDate);
        str3 += (" " + str5);

        paintTime.setTextSize(mAppConfig.dataFontSize() * scaleWidth);
        int w1 = (int) paintTime.measureText(str3);
        canvas.drawText(str3, (ScreenWidth - w1) / 2+mAppConfig.dataX()*scaleWidth,
        		mAppConfig.dataY() * scaleHeight,paintTime);

        Time t = new Time();
        t.setToNow();

        String s;
        String ampm = "";
        if (!timeStyle) {
            s = time12Format.format(curDate);
            ampm = mAMPM.format(curDate);
        } else {
            if (hour24Format.format(curDate).equals("24")) {
                s = "00:" + minute24Format.format(curDate);
            } else {
                s = hour24Format.format(curDate) + ":"
                        + minute24Format.format(curDate);
            }
        }
        paintTime.setTextSize(mAppConfig.timeFontSize() * scaleWidth);
        int w = (int) paintTime.measureText(s);

        canvas.drawText(s, (ScreenWidth - w) / 2+mAppConfig.timeX()*scaleWidth,
        		mAppConfig.timeY() * scaleHeight, paintTime);
        paintTime.setTextSize(mAppConfig.ampmFontSize() * scaleWidth);
        canvas.drawText(ampm, (ScreenWidth - w) / 2 + w+mAppConfig.ampmX()*scaleWidth, 
        		mAppConfig.ampmY() * scaleHeight, paintTime);
        if (ischarge) {
            String charge_s;
            charge_s = charge_res + "(" + Integer.toString(power) + "%)";

            paintTime.setTextSize(mAppConfig.chargeFontSize() * scaleWidth);
            int c1 = (int) paintTime.measureText(charge_s);
            canvas.drawText(charge_s, (ScreenWidth - c1) / 2+mAppConfig.chargeX()*scaleWidth,
            		mAppConfig.chargeY() * scaleHeight, paintTime);
        }
        
        if (mUnreadSmsNum > 0) {
        	String smsText = getContext().getString(R.string.unread,mUnreadSmsNum);
        	int smsTextLen = (int)callAndSmsPaint.measureText(smsText);
        	canvas.drawText(smsText, (ScreenWidth - smsTextLen) / 2+mAppConfig.smsX()*scaleWidth,
            		mAppConfig.smsY() * scaleHeight, callAndSmsPaint);
		}
        
        if (mMissedCallNum > 0) {
        	String callText = getContext().getString(R.string.misscall,mMissedCallNum);
        	int callTextLen = (int)callAndSmsPaint.measureText(callText);
        	canvas.drawText(callText, (ScreenWidth - callTextLen) / 2+mAppConfig.callX()*scaleWidth,
            		mAppConfig.callY() * scaleHeight, callAndSmsPaint);
		}
        
        super.onDraw(canvas);
    }

    protected void exitLock() {
        Context context = getContext();
        Log.d(LOG_TAG, "BaseView.exitLock");
        Intent intent = new Intent();
        intent.setAction("com.cooee.action.DISABLE_SYSLOCK");
        context.sendBroadcast(intent);
        if (mExitFunc != null) {
            mExitFunc.run();
        } else {

            if (context instanceof Activity) {
                ((Activity) mContext).finish();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (blinking != false) {
            return true;
        }
        if (wrap != null) {
            wrap.resetLight();
        }
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:

                if (Longtask != null) {
                    Longtask.cancel();
                    Longtask = null;
                }
                if (flag && hasInCircle && animMode == 2 && longandmove) {
                    exitLock();
                } else {
                    if (animMode == 2) {
                        animMode = 1;
                        Log.v("xxxxxxx", "MotionEvent.ACTION_UP");

                        blinking = true;
                        pointBlink_t();
                    } else {
                        animMode = 0;
                    }
                    if (longandmove == true) {
                        upTask();
                    }
                }
                cur_x = 0;
                cur_y = 0;
                hasInCircle = false;
                break;
            case MotionEvent.ACTION_DOWN:
                float down_x = event.getX();
                float down_y = event.getY();

                if (canDrawPresscle(down_x, down_y)) {
                    longandmove = true;
                    if (Longtask == null) {
                        longPressTask();
                    }
                    canDrawPress = false;

                } else {
                    canDrawPress = true;
                    if (noCircleTouch(down_x, down_y)){
                       // noCirlcledraw = true;
                        animMode = 2;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float move_x = event.getX();
                float move_y = event.getY();

                if (move_x > circleO_x - imgCircle.getWidth() / 2 + 5
                        && move_x < circleO_x + imgCircle.getWidth() / 2 - 5
                        && move_y > circleO_y - imgCircle.getHeight() / 2 + 5
                        && move_y < circleO_y + imgCircle.getHeight() / 2 - 5) {
                    hasInCircle = true;
                }
                if (canDrawPresscle(move_x, move_y)) {
                    canDrawPress = false;
                    animMode = 2;
                    if (longandmove == false) {
                        longandmove = true;
                        if (Longtask == null) {
                            longPressTask();
                        }
                    }
                }
                if (canUnlock(move_x, move_y)) {
                    flag = true;
                } else {
                    flag = false;
                }
                cur_x = move_x;
                cur_y = move_y;
                break;
        }
        return true;
    }

    private boolean noCircleTouch(float x, float y) {
        boolean ret = false;
        if (y > 240f * scaleHeight) {
            ret = true;
        }
        return ret;
    }

    private boolean canDrawPresscle(float x, float y) {
        boolean res = false;
        float len;

        len = (x - circleO_x) * (x - circleO_x) + (y - circleO_y)
                * (y - circleO_y);

        if (len <= imgPressLock.getWidth() / 2 * imgPressLock.getWidth() / 2) {
            res = true;
        }
        return res;
    }

    private boolean canUnlock(float x, float y) {
        boolean res = false;
        float curLen = 0;
        double temp_x;
        double temp_y;

        curLen = (x - circleO_x) * (x - circleO_x) + (y - circleO_y)
                * (y - circleO_y);
        if (curLen >= (circleR - 10f) * (circleR - 10f)) {
            res = true;
        }
        if (res) {
            temp_x = Math
                    .sqrt((double) (circleR * circleR * (x - circleO_x)
                            * (x - circleO_x) / ((x - circleO_x)
                            * (x - circleO_x) + (y - circleO_y)
                            * (y - circleO_y))));
            temp_y = Math
                    .sqrt((double) (circleR * circleR * (y - circleO_y)
                            * (y - circleO_y) / ((x - circleO_x)
                            * (x - circleO_x) + (y - circleO_y)
                            * (y - circleO_y))));
            if (x >= circleO_x) {
                unlock_x = (float) (temp_x + circleO_x);
            } else {
                unlock_x = (float) (circleO_x - temp_x);
            }
            if (y >= circleO_y) {
                unlock_y = (float) (temp_y + circleO_y);
            } else {
                unlock_y = (float) (circleO_y - temp_y);
            }
        }
        return res;
    }

    private Bitmap changeCover() {
        Bitmap temp;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidthp, scaleHeightp);
        temp = Bitmap.createBitmap(imgCover1, 0, 0, imgCover1.getWidth(),
                imgCover1.getHeight(), matrix, true);
        return temp;
    }

    private void draw11circle(Canvas canvas) {
        int x = 0;
        int y = 0;
        Bitmap cover = changeCover();
        Log.v("cccccccccc", "cover start");
        paintPanim.setFilterBitmap(true);
        paintPanim.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.saveLayer(x, y, ScreenWidth, ScreenHeight, null, Canvas.MATRIX_SAVE_FLAG
                | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
                | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
                | Canvas.CLIP_TO_LAYER_SAVE_FLAG);
        canvas.drawBitmap(cover, circleO_x - cover.getWidth() / 2, circleO_y
                - cover.getHeight() / 2, null);

        canvas.drawBitmap(imgPointCircle, 0, 0, paintPanim);

        canvas.restore();
    }

    private Bitmap CreatBitmap() {
        double circleA_init = 0;
        Bitmap img;
        int i = 0;
        Bitmap temp = Bitmap.createBitmap(ScreenWidth, ScreenHeight,
                Bitmap.Config.ARGB_8888);
        // imgCircle.copy(Bitmap.Config.ARGB_8888, true);

        Canvas c1 = new Canvas(temp);
        while (i < count_max) {
            if (i >= count_max) {
                img = resizePoint((1 - ((float) count_max - 1) / scaleNum),
                        (1 - ((float) count_max - 1) / scaleNum));
            } else {
                img = resizePoint((1 - (float) i / scaleNum), (1 - (float) i
                        / scaleNum));
            }
            while (circleA_init <= Math.PI * 2) {
                c1.drawBitmap(img,
                        (float) (circleO_x + circleR * Math.cos(circleA_init)
                                * ((1 + i) * 0.1) - img.getWidth() / 2),
                        (float) (circleO_y + circleR * Math.sin(circleA_init)
                                * ((1 + i) * 0.1) - img.getHeight() / 2),
                        paintP);
                circleA_init += Math.PI / (as * (i + 1));
            }
            circleA_init = 0;
            i++;
        }
        // c1.save();
        return temp;
    }

    private void drawCircle_t(Canvas canvas) {
        int x = 0;
        int y = 0;
        Log.v("cccccccccc", "cover start");
        paintPmove.setFilterBitmap(true);
        paintP.setFilterBitmap(true);
        paintPmove.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.saveLayer(x, y, ScreenWidth, ScreenHeight, null, Canvas.MATRIX_SAVE_FLAG
                | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
                | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
                | Canvas.CLIP_TO_LAYER_SAVE_FLAG);
        canvas.drawBitmap(imgCover, cur_x - imgCover.getWidth() / 2, cur_y
                - imgCover.getHeight() / 2, null);

        canvas.drawBitmap(imgPointCircle, 0, 0, paintPmove);

        canvas.restore();
    }

    private void changeBL(int brightlevel) {
        ColorMatrix mCm = new ColorMatrix();
        mCm.set(new float[] {
                1, 0, 0, 0, brightlevel, 0, 1, 0, 0, brightlevel,
                0, 0, 1, 0, brightlevel, 0, 0, 0, 1, 0
        });
        paintdown.setColorFilter(new ColorMatrixColorFilter(mCm));
    }

    private Bitmap resizePoint(float scaleWidthp, float scaleHeightp) {
        Bitmap temp;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidthp, scaleWidthp);
        temp = Bitmap.createBitmap(imgPoint, 0, 0, imgPoint.getWidth(),
                imgPoint.getHeight(), matrix, true);
        return temp;
    }

    private Bitmap changeCircle() {
        Bitmap temp;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidthc, scaleWidthc);
        temp = Bitmap.createBitmap(imgCircle, 0, 0, imgCircle.getWidth(),
                imgCircle.getHeight(), matrix, true);
        return temp;
    }

    private Bitmap changeLock() {
        Bitmap temp;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidthc, scaleWidthc);
        temp = Bitmap.createBitmap(imgLock, 0, 0, imgLock.getWidth(),
                imgLock.getHeight(), matrix, true);
        return temp;
    }

    private void pointBlink_t() {
        if (BCtask != null) {
            BCtask.cancel();
        }
        BCtask = new TimerTask() {

            @Override
            public void run() {

                if (imgCover.getWidth() * scaleWidthp < 2 * circleR) {
                    scaleWidthp += 0.3f;
                    scaleHeightp += 0.3f;
                } else {
                    // longandmove = false;
                    BCtask.cancel();
                    animMode = 0;
                    blinking = false;
                    canDrawPress = true;
                    scaleWidthp = 0.1f;
                    scaleHeightp = 0.1f;
                }
            }
        };
        timer.schedule(BCtask, 0, 100);
    }

    private void longPressTask() {
        if (Longtask != null) {
            Longtask.cancel();
        }
        Longtask = new TimerTask() {

            @Override
            public void run() {
                if (scaleWidthc <= 1.0f) {
                    scaleWidthc += 0.05f;
                    brightlevel += 90;
                    changeBL(brightlevel);
                } else {
                    Longtask.cancel();
                    Longtask = null;
                }
                // Log.v("xxxxxxxxxx", "run!!!!!1");
                // longandmove = true;
                // Longtask.cancel();
                // Longtask = null;
            }
        };
        timer.schedule(Longtask, 0, 10);
    }

    private void upTask() {
        if (Uptask != null) {
            Uptask.cancel();
        }
        Uptask = new TimerTask() {

            @Override
            public void run() {
                if (scaleWidthc > scaleUpDown) {
                    scaleWidthc -= 0.05f;
                    brightlevel -= 90;
                    changeBL(brightlevel);
                } else {
                    Uptask.cancel();
                    Uptask = null;
                    longandmove = false;
                }
            }
        };
        timer.schedule(Uptask, 0, 10);
    }

    private Handler mHandlerMain = new Handler();
    private Runnable mRunFlush = new Runnable() {
        public void run() {
            if (ischarge) {
                chargePosition += 10;
                if (chargePosition > 320) {
                    chargePosition = 0;
                }

            }
            mHandlerMain.postDelayed(this, 30);
            postInvalidate();
        }
    };

    private Runnable mExitFunc = null;

    public void setExitFunction(Runnable run) {
        mExitFunc = run;
    }

    private BCWrap wrap = null;

    public void setWrap(BCWrap w) {
        this.wrap = w;
    }

    public void onViewDestroy() {
        mContext.unregisterReceiver(mIntentReceiver);
        msgNotify.unregisterObserver(mContext, msgObserver);
		callNotify.unregisterObserver(mContext, callObserver);
    }

    public void onViewResume() {
        PowerManager pm = (PowerManager) mContext
                .getSystemService(Context.POWER_SERVICE);
        Log.v(LOG_TAG, "pm.isScreenOn()=" + pm.isScreenOn());
        if (pm.isScreenOn()) {
            mHandlerMain.removeCallbacks(mRunFlush);
            mHandlerMain.postDelayed(mRunFlush, 0);
        }

    }

    public void onViewPause() {
        mHandlerMain.removeCallbacks(mRunFlush);
    }
    
    ContentObserver msgObserver = new ContentObserver(null) {
		@Override
		public void onChange(boolean selfChange) {
			new SmsChangeThread().start();
		}
	};
	ContentObserver callObserver = new ContentObserver(null) {
		@Override
		public void onChange(boolean selfChange) {
			new CallChangeThread().start();
		}
	};
	 
	public class CallChangeThread extends Thread {
		public CallChangeThread() {
			super();
		}

		@Override
		public void run() {
			mMissedCallNum = callNotify.getMissedCallCount(mContext);
//			mMissedCallNum = 1;
			if (mMissedCallNum > 0){
				postInvalidate();
			}
		}
	}
	
	public class SmsChangeThread extends Thread {
		public SmsChangeThread() {
			super();
		}

		@Override
		public void run() {
			mUnreadSmsNum = msgNotify.getUnreadCount(mContext);
//			mUnreadSmsNum = 1;
			if (mUnreadSmsNum > 0){
				postInvalidate();
			}
		}
	}
}
