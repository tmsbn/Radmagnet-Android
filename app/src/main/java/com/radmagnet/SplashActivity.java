package com.radmagnet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class SplashActivity extends BaseActivity {


    AlarmManager mAlarmManager;

    private static final int REPEAT_NOTIF_AFTER_DAYS = 3;

    @InjectView(com.radmagnet.R.id.introPager)
    AutoScrollViewPager mIntroPager;

    @InjectView(com.radmagnet.R.id.splashScreen)
    ImageView mSplashScreenIv;

    @InjectView(com.radmagnet.R.id.circlePagerIndicator)
    CirclePageIndicator mCirclePageIndicator;

    @InjectView(com.radmagnet.R.id.startButton)
    Button mStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.radmagnet.R.layout.activity_splash);
        ButterKnife.inject(this);

        int SPLASH_DISPLAY_LENGTH = 2000;
        setupReminderNotification();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                mSplashScreenIv.setVisibility(View.GONE);

                if (getPreferences(MODE_PRIVATE).getBoolean("showSplash", true)) {
                    setUpPager();
                    getPreferences(MODE_PRIVATE).edit().putBoolean("showSplash", false).apply();
                } else {
                    startFeed();
                }


            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    public void setUpPager() {

        ArrayList<Integer> imageIdList = new ArrayList<>();
        imageIdList.add(com.radmagnet.R.layout.layout_splash_1);
        imageIdList.add(com.radmagnet.R.layout.layout_splash_2);

        mIntroPager.setAdapter(new ImagePagerAdapter(this, imageIdList));
        mIntroPager.setAutoScrollDurationFactor(8);
        mIntroPager.setInterval(5000);
        mIntroPager.setCycle(false);
        mIntroPager.startAutoScroll();

        mCirclePageIndicator.setViewPager(mIntroPager);
        mCirclePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1 && mStartButton.getAlpha() == 0) {
                    mStartButton.animate().setDuration(1200).alpha(1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @OnClick(com.radmagnet.R.id.startButton)
    public void startFeed() {

        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(mainIntent);
        SplashActivity.this.finish();

    }


    public void setupReminderNotification() {

        ComponentName receiver = new ComponentName(this, CheckAppReceiver.class);
        PackageManager pm = getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent intent = new Intent(this, CheckAppReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, BaseApplication.NOTIF_REQUEST_CODE,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

//        calendar.add(Calendar.MINUTE, 1);
//
//        mAlarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
//               20*1000, pendingIntent);


        calendar.add(Calendar.DATE, REPEAT_NOTIF_AFTER_DAYS);

        mAlarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY * REPEAT_NOTIF_AFTER_DAYS, pendingIntent);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(com.radmagnet.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}
