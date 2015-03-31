package com.radmagnet.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.radmagnet.BaseApplication;
import com.radmagnet.R;
import com.radmagnet.activities.SplashActivity;
import com.radmagnet.models.News;

import io.realm.Realm;
import io.realm.RealmResults;

public class CheckAppReceiver extends BroadcastReceiver {

    int MAX_DB_COUNT = 50;

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent notifIntent = new Intent(context, SplashActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, BaseApplication.NOTIF_REQUEST_CODE, notifIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Resources res = context.getResources();
        builder.setContentIntent(contentIntent)
                .setSmallIcon(com.radmagnet.R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_launcher))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setLocalOnly(true)
                .setContentTitle(context.getString(com.radmagnet.R.string.notif_title))
                .setContentText(context.getString(com.radmagnet.R.string.notif_content));

        Notification notification = builder.build();
        notificationManager.notify(BaseApplication.NOTIF_REQUEST_CODE, notification);

        /**
         * Database manintainance
         */
        Realm realm = Realm.getInstance(context);
        final RealmResults<News> results = realm.where(News.class).findAllSorted("createdDate", false);
        final int size = results.size();
        realm.beginTransaction();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                if (size >= MAX_DB_COUNT) {

                    for (int i = size-1; i > MAX_DB_COUNT; i--) {
                        if (!results.get(i).isBookmarked())
                            results.get(i).removeFromRealm();
                    }

                }


            }
        });

        realm.close();


    }
}
