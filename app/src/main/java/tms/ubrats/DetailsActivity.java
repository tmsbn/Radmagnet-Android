package tms.ubrats;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.realm.Realm;


public class DetailsActivity extends BaseActivity implements ViewPager.OnPageChangeListener,NewsFragment.OnFragmentInteractionListener {

    public ArrayList<String> mRealmIds;

    @InjectView(R.id.newsPager)
    public ViewPager mViewPager;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.inject(this);

        ArrayList<String> realmIds = getIntent().getStringArrayListExtra("realmIds");
        position= getIntent().getIntExtra("position",-1);
        if (realmIds == null || realmIds.isEmpty())
            finish();

        mRealmIds=realmIds;
        setupActionBar();
        setupData();

    }

    private void setupActionBar(){
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

    }

    public void setupData(){

        NewsDetailPagerAdapter newsDetailPagerAdapter=new NewsDetailPagerAdapter(mRealmIds);
        mViewPager.setAdapter(newsDetailPagerAdapter);
        mViewPager.setOnPageChangeListener(this);

        if(position!=-1){
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

        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch(id){

            case R.id.menu_share:


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

    private class NewsDetailPagerAdapter extends FragmentPagerAdapter{

        ArrayList<String> mRealmIds;

        public NewsDetailPagerAdapter(ArrayList<String> realmIds){

            super(getSupportFragmentManager());
            mRealmIds=realmIds;
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
