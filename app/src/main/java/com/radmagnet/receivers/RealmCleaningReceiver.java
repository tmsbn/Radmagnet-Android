package com.radmagnet.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.radmagnet.models.News;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmCleaningReceiver extends BroadcastReceiver {

    int MAX_DB_COUNT = 50;

    @Override
    public void onReceive(Context context, Intent intent) {


        /**
         * Database manintainance
         */
        Realm realm = Realm.getInstance(context);
        final RealmResults<News> results = realm.where(News.class).findAllSorted("key", false);
        final int size = results.size();
        realm.beginTransaction();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                if (size >= MAX_DB_COUNT) {

                    for (int i = size - 1; i > MAX_DB_COUNT; i--) {
                        if (!results.get(i).isBookmarked())
                            results.get(i).removeFromRealm();
                    }

                }


            }
        });

        realm.close();


    }
}
