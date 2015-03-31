package com.radmagnet.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.radmagnet.fragment.NewsFragment;
import com.radmagnet.models.News;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;


public class DetailsActivity extends BaseActivity implements ViewPager.OnPageChangeListener, NewsFragment.OnFragmentInteractionListener {

    public ArrayList<String> mRealmIds;

    @InjectView(com.radmagnet.R.id.newsPager)
    public ViewPager mViewPager;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.radmagnet.R.layout.activity_details);
        ButterKnife.inject(this);

        ArrayList<String> realmIds = getIntent().getStringArrayListExtra("realmIds");
        position = getIntent().getIntExtra("position", 0);

        if (realmIds == null || realmIds.isEmpty()) {

            //check if the data is coming from an intent
            String data = getIntent().getDataString();
            if (data == null || data.isEmpty() )

                finish();

            else {

                data = data.replace("http://", "");
                String[] parts = data.split("/");
                if (parts.length == 5) {

                   //make new arrayList for storing realm ids to be passed to the details controller
                    realmIds = new ArrayList<>();
                    String id = parts[3] + parts[4];

                    Realm realm = Realm.getInstance(this);
                    News news = realm.where(News.class).contains("id", parts[3] + parts[4]).findFirst();

                    if (news!=null && news.isValid())
                        realmIds.add(id);
                    else {

                        //if the data doesn't exist in the db yet, go up to the parent
                        Intent intent=new Intent(this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                }

            }

        }

        mRealmIds = realmIds;
        setupData();

    }


    public void setupData() {

        NewsDetailPagerAdapter newsDetailPagerAdapter = new NewsDetailPagerAdapter(mRealmIds);
        mViewPager.setAdapter(newsDetailPagerAdapter);
        mViewPager.setOnPageChangeListener(this);

        if (position != -1) {
            mViewPager.setCurrentItem(position);
        }


    }

    @Override
    protected void onDestroy() {
        Realm.getInstance(this).close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(com.radmagnet.R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case com.radmagnet.R.id.share:


                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class NewsDetailPagerAdapter extends FragmentStatePagerAdapter {

        ArrayList<String> mRealmIds;

        public NewsDetailPagerAdapter(ArrayList<String> realmIds) {

            super(getSupportFragmentManager());
            mRealmIds = realmIds;
        }


        @Override
        public Fragment getItem(int position) {

            return NewsFragment.newInstance(mRealmIds.get(position));
        }

        @Override
        public int getCount() {
            if (mRealmIds != null)
                return mRealmIds.size();
            else
                return 0;
        }
    }


}
