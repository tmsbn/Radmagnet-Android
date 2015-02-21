package tms.ubrats;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener, SearchView.OnCloseListener {


    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @InjectView(R.id.newsList)
    ListView mNewsLv;

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @InjectView(R.id.categories_list)
    ListView mCategoriesLv;

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @InjectView(R.id.left_drawer)
    LinearLayout mLeftDrawer;

    @InjectView(R.id.branding)
    TextView mBranding;

    @InjectView(R.id.swipeNewsLayout)
    SwipeRefreshLayout mSwipeLayout;

    ActionBarDrawerToggle mDrawerToggle;

    SearchView mSearchView;

    NewsAdapter mNewsAdapter;
    CategoriesAdapter mCategoriesAdapter;

    String mSearchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
        setupToolBar();
        setupCategoriesList();
        setupNewsList();
        fetchLatestNews();

    }

    private void setupNewsList() {

        mNewsAdapter = new NewsAdapter(this, getRealmData(), true);
        mNewsLv.setAdapter(mNewsAdapter);
        mNewsLv.setOnItemClickListener(this);

        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mSwipeLayout.setOnRefreshListener(this);
    }

    @OnClick(R.id.showBookMarks)
    public void showBookmarks(){

        Intent intent=new Intent(this,BookmarkActivity.class);
        startActivity(intent);
    }

    private void setupCategoriesList() {

        mCategoriesAdapter = new CategoriesAdapter(this, getConfig().categories);
        mCategoriesLv.setAdapter(mCategoriesAdapter);
        mCategoriesLv.setOnItemClickListener(this);

    }

    private void setupToolBar() {

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        mBranding.setText("UBrats");


    }


    private void fetchLatestNews() {

        long lastUpdatedDate = mPrefs.getLong(LAST_UPDATED_KEY, -1);
        String date = (lastUpdatedDate != -1) ? String.valueOf(lastUpdatedDate) : "";
        Networking.getRestClient().getAnnouncements(date, new GetNewsCallback());

        mSwipeLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void onRefresh() {
        fetchLatestNews();
    }

    @Override
    public boolean onClose() {

        mNewsAdapter.updateRealmResults(getRealmData());

        return true;
    }

    private class GetNewsCallback implements Callback<NewsResponse> {


        @Override
        public void success(final NewsResponse newsResponse, Response response) {

            final ArrayList<News> newsList = newsResponse.data;
            if (newsList == null || newsList.size() == 0) {
                Toast.makeText(MainActivity.this, getString(R.string.noUpdatesAvaliable_msg), Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(MainActivity.this, newsList.size() + "", Toast.LENGTH_SHORT).show();
                Realm realm = Realm.getInstance(MainActivity.this);
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(newsList);
                        mPrefs.edit().putLong(LAST_UPDATED_KEY, newsResponse.date.getTime() / 1000).apply();

                    }
                });

            }

            mSwipeLayout.setRefreshing(false);


        }


        @Override
        public void failure(RetrofitError error) {

            Toast.makeText(MainActivity.this, getString(R.string.cannotReachServer_msg), Toast.LENGTH_SHORT).show();
            mSwipeLayout.setRefreshing(false);

        }

    }

    @Override
    protected void onDestroy() {
        Realm.getInstance(this).close();
        super.onDestroy();
    }

    private RealmResults<News> getRealmData() {

        RealmQuery<News> realmQuery = Realm.getInstance(this).where(News.class);

        if (!mSearchQuery.equals("")) {
            realmQuery = realmQuery.contains("headline", mSearchQuery, false);

        }
        return realmQuery.findAllSorted("createdDate", false);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
            return;
        }

        if (!mSearchView.isIconified()) {
            mSearchView.onActionViewCollapsed();
            mSearchQuery = "";
            return;
        }

        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnCloseListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            default:

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {

            case R.id.newsList:


                ArrayList<String> realmIds = new ArrayList<>();
                RealmResults<News> results = mNewsAdapter.getRealmResults();
                for (News news : results) {
                    realmIds.add(news.getPostId());
                }

                Intent intent = new Intent(this, DetailsActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("realmIds", realmIds);
                startActivity(intent);

                break;

            case R.id.categories_list:

                break;
        }

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        mSearchView.onActionViewCollapsed();

        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {

        mSearchQuery = query;
        mNewsAdapter.updateRealmResults(getRealmData());

        return true;

    }


}
