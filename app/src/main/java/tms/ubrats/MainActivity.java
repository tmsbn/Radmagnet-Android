package tms.ubrats;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener, SearchView.OnCloseListener, NewsAdapter.NewsItemClickListener {


    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @InjectView(R.id.newsList)
    RecyclerView mNewsRv;

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @InjectView(R.id.categories_list)
    ListView mCategoriesLv;

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;


    @InjectView(R.id.branding)
    TextView mBranding;

    @InjectView(R.id.swipeNewsLayout)
    SwipeRefreshLayout mSwipeLayout;

    ActionBarDrawerToggle mDrawerToggle;

    SearchView mSearchView;

    NewsAdapter mNewsAdapter;
    CategoriesAdapter mCategoriesAdapter;

    String mSearchQuery = "";
    String mSelectedCategory = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
        setupActionBar(false);
        setupToolBar();
        setupCategoriesList();
        setupNewsList();
        fetchLatestNews();

    }

    private void setupNewsList() {

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mNewsRv.setLayoutManager(linearLayoutManager);
        SlideInUpAnimator slideInUpAnimator = new SlideInUpAnimator();
        slideInUpAnimator.setChangeDuration(1000);
        mNewsRv.setItemAnimator(slideInUpAnimator);
        mNewsAdapter = new NewsAdapter(this, getRealmData(), true);
        // SlideInBottomAnimationAdapter slideInBottomAnimationAdapter=new SlideInBottomAnimationAdapter(mNewsAdapter);
        //slideInBottomAnimationAdapter.setDuration(700);

        mNewsRv.setAdapter(mNewsAdapter);
        mNewsAdapter.setOnItemClickedListener(this);

        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mSwipeLayout.setOnRefreshListener(this);
    }

    @OnClick(R.id.showBookMarks)
    public void showBookmarks() {

        Intent intent = new Intent(this, BookmarkActivity.class);
        startActivity(intent);
    }

    private void setupCategoriesList() {

        ArrayList<Category> categories = getConfig().categories;
        categories.add(0, new Category(getString(R.string.allTheRad_txt), "#" + Integer.toHexString(getResources().getColor(android.R.color.white)), "", true));
        mCategoriesAdapter = new CategoriesAdapter(this, categories);
        mCategoriesLv.setAdapter(mCategoriesAdapter);
        mCategoriesLv.setOnItemClickListener(this);


    }

    private void setupToolBar() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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

    @Override
    public void onItemClick(News newsItem, int position) {

        ArrayList<String> realmIds = new ArrayList<>();
        RealmResults<News> results = mNewsAdapter.getRealmResults();
        for (News news : results) {
            realmIds.add(news.getPostId());
        }

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("realmIds", realmIds);
        startActivity(intent);
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

        if (!mSelectedCategory.equals("")) {
            realmQuery = realmQuery.contains("category", mSelectedCategory, false);
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
        MenuItem menuItem = menu.findItem(R.id.action_search);
        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                mSwipeLayout.setEnabled(false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mSwipeLayout.setEnabled(true);
                return true;
            }
        });

        mSearchView = (SearchView) menuItem.getActionView();
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


                break;

            case R.id.categories_list:

                mCategoriesAdapter.setSelectedCategory(position);
                mSelectedCategory = mCategoriesAdapter.getItem(position).value;
                mNewsAdapter.updateRealmResults(getRealmData());
                mDrawerLayout.closeDrawers();

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
        mNewsAdapter.setSearchTerm(query);

        return true;

    }


}
