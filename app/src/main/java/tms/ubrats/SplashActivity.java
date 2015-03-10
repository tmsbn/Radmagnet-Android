package tms.ubrats;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

    @InjectView(R.id.introPager)
    AutoScrollViewPager mIntroPager;

    @InjectView(R.id.splashScreen)
    ImageView mSplashScreenIv;

    @InjectView(R.id.circlePagerIndicator)
    CirclePageIndicator mCirclePageIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);

        int SPLASH_DISPLAY_LENGTH = 2000;
        setupReminderNotification();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                mSplashScreenIv.setVisibility(View.GONE);
                setUpPager();

            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    public void setUpPager(){

        ArrayList<Integer> imageIdList = new ArrayList<>();
        imageIdList.add(R.drawable.bg_intro_1);
        imageIdList.add(R.drawable.bg_intro_2);


        mIntroPager.setAdapter(new ImagePagerAdapter(this,imageIdList));
        mIntroPager.setAutoScrollDurationFactor(8);
        mIntroPager.setInterval(5000);
        mIntroPager.setCycle(false);
        mIntroPager.startAutoScroll();

        mCirclePageIndicator.setViewPager(mIntroPager);

    }

    @OnClick(R.id.StartButton)
    public void skipButton(){

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

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}
