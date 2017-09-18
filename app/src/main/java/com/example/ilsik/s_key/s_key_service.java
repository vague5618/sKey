package com.example.ilsik.s_key;

import android.animation.Animator;
import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class s_key_service extends Service {

    Toast toast;
    int cnt = 0;
    TextView tv;
    IntentFilter intentFilter = new IntentFilter();

    public static final String ScreenOff = "android.intent.action.SCREEN_OFF";
    public static final String ScreenOn = "android.intent.action.SCREEN_ON";
    WindowManager.LayoutParams params;
    WindowManager wm;

    Handler mHandler=new Handler();

    public void runa() throws Exception{
        mHandler.post(new Runnable(){
            public void run(){
                toast.setText("Thread working..");
                toast.show();
            }
        });
    }

    BroadcastReceiver screenOnOff = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(ScreenOff))
            {
                Log.e("s_key_service", "Screen Off");
                tv.setAlpha(0.8f);

                if(cnt%2==0){
                    tv.setText("Baby Mode");
                }else{
                    tv.setText("Work Mode");
                }

            }
            else if (intent.getAction().equals(ScreenOn))
            {
                Log.v("s_key_service", "Screen On");

                tv.animate().alpha(1f).setDuration(1500);

                tv.animate().alpha(0f).setDuration(3000);

                if(cnt%2==0){
                    ComponentName compoentName = new ComponentName("com.sec.android.app.launcher","com.android.launcher3.Launcher");
                    Intent it = new Intent(Intent.ACTION_MAIN);
                    it.setComponent(compoentName);
                    startActivity(it);
                    cnt++;

                }else
                {
                    ComponentName compoentName = new ComponentName("com.galaxys.launcher","com.galaxys.launcher.Launcher");
                    Intent it = new Intent(Intent.ACTION_MAIN);
                    it.setComponent(compoentName);
                    startActivity(it);
                    cnt++;
                }
                Log.e("s_key_service", "Screen On");
            }
        }

    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("s_key_service", "onCreate");

        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);

        tv = new TextView(this);

        tv.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
        tv.setHeight(ActionBar.LayoutParams.MATCH_PARENT);
        String color = "#2F4F4F";
        tv.setBackgroundColor(Color.parseColor(color));

        tv.getBackground().setAlpha(240);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL); //텍스트 뷰 자체의 Gravity를 가로,세로 중앙으로 설정한다
        tv.setAlpha(0f);
        tv.setText("Baby Mode");

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,//터치 인식, 나중에 기능 추가를 위해 일단 넣어둠
                PixelFormat.TRANSLUCENT
        );

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(tv, params);

        registerReceiver(screenOnOff, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("s_key_service", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(tv != null) {
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(tv);
            tv = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v("s_key_service", "onBind");
        return null;
    }

}