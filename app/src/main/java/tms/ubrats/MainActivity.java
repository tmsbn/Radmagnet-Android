package tms.ubrats;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {


    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @InjectView(R.id.newsList)
    ListView mNewsLv;

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @InjectView(R.id.categories_list)
    ListView mCategoriesList;

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @InjectView(R.id.left_drawer)
    LinearLayout mLeftDrawer;

    @InjectView(R.id.branding)
    TextView mBranding;

    @InjectView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeLayout;


    ActionBarDrawerToggle mDrawerToggle;

    SearchView mSearchView;

    NewsAdapter mNewsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
        setupToolBar();
        setupSideBar();
        setupListView();
        fetchLatestNews();

    }

    private void setupSideBar() {

        mCategoriesList.setOnItemClickListener(this);
        mSwipeLayout.setRefreshing(true);

    }

    private void setupToolBar() {

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        mBranding.setText("UBrats");


    }

    private void setupListView() {

        mNewsAdapter = new NewsAdapter(this, null, true);
    }

    private void fetchLatestNews() {

        Networking.getRestClient().getAnnouncements("", new GetNewsCallback());

    }

    private class GetNewsCallback implements Callback<NewsResponse> {

        @Override
        public void success(NewsResponse newsResponse, Response response) {

            final ArrayList<News> newsList = newsResponse.newsList;
            Realm realm = Realm.getInstance(MainActivity.this);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(newsList);

                }
            });


        }

        @Override
        public void failure(RetrofitError error) {

            Crouton.makeText(MainActivity.this, getString(R.string.cannotReachServer_msg), Style.INFO);
            refreshList();

        }
    }

    private void refreshList() {

        Realm realm = Realm.getInstance(MainActivity.this);
        RealmQuery<News> query = realm.where(News.class);
        RealmResults<News> results = query.findAll();
        mNewsAdapter.updateRealmResults(results);
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
        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setOnQueryTextListener(this);

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

    }

    @Override
    public boolean onQueryTextSubmit(String s) {

        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        Log.v("searchTerm", s);
        return true;
    }
}
