package com.radmagnet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.radmagnet.models.News;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class BookmarkActivity extends BaseActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener, BookmarksAdapter.BookmarkListener {

    @InjectView(com.radmagnet.R.id.bookmarksList)
    RecyclerView mBookmarksRv;

    @InjectView(com.radmagnet.R.id.toolbar)
    Toolbar mToolbar;

    BookmarksAdapter mBookmarkAdapter;


    String mSearchQuery = "";

    SearchView mSearchView;
    RecyclerViewSwipeManager mRecyclerViewSwipeManager;
    private RecyclerViewTouchActionGuardManager mRecyclerViewTouchActionGuardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.radmagnet.R.layout.activity_bookmark);

        ButterKnife.inject(this);

        setupActionBar(true, (Toolbar) findViewById(com.radmagnet.R.id.toolbar));
        setupNewsList();

    }


    private void setupNewsList() {

        //touchguard manager
        mRecyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        mRecyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        mRecyclerViewTouchActionGuardManager.setEnabled(true);

        mBookmarkAdapter = new BookmarksAdapter(this, getRealmData(), true);

        mRecyclerViewSwipeManager = new RecyclerViewSwipeManager();


        mBookmarksRv.setLayoutManager(new LinearLayoutManager(this));
        mBookmarksRv.setAdapter(mRecyclerViewSwipeManager.createWrappedAdapter(mBookmarkAdapter));
        mBookmarksRv.setItemAnimator(new SwipeDismissItemAnimator());

        mRecyclerViewTouchActionGuardManager.attachRecyclerView(mBookmarksRv);
        mRecyclerViewSwipeManager.attachRecyclerView(mBookmarksRv);
        mBookmarkAdapter.setOnItemClickedListener(this);


    }

    private RealmResults<News> getRealmData() {

        RealmQuery<News> realmQuery = Realm.getInstance(this).where(News.class);

        if (!mSearchQuery.equals("")) {
            realmQuery = realmQuery.contains("headline", mSearchQuery, false);
        }

        realmQuery.equalTo("isBookmarked", true);

        return realmQuery.findAllSorted("key", false);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(com.radmagnet.R.menu.menu_main, menu);
        mSearchView = (SearchView) menu.findItem(com.radmagnet.R.id.action_search).getActionView();
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnCloseListener(this);

        return true;
    }

    @Override
    protected void onDestroy() {

        mRecyclerViewSwipeManager.release();
        mRecyclerViewTouchActionGuardManager.release();
        Realm.getInstance(this).close();

        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {

            if (!mSearchView.isIconified()) {
                mSearchView.onActionViewCollapsed();
                mSearchQuery = "";
                return true;
            }


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        mSearchView.onActionViewCollapsed();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {

        //if query is of length 1 no need to do any filtering
        if (query.length() <= 1) {
            mSearchQuery = "";
        } else {
            mSearchQuery = query;
        }

        mSearchQuery = query;
        mBookmarkAdapter.updateRealmResults(getRealmData());
        mBookmarkAdapter.setSearchTerm(query);


        return true;
    }

    @Override
    public boolean onClose() {

        mBookmarkAdapter.updateRealmResults(getRealmData());
        return true;
    }

    @Override
    public void onItemClick(News news, int positon) {

        ArrayList<String> realmIds = new ArrayList<>();
        for (News newsIterator : mBookmarkAdapter.getRealmResults())
            realmIds.add(newsIterator.getId());


        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("position", positon);
        intent.putExtra("realmIds", realmIds);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {


        if (!mSearchView.isIconified()) {
            mSearchView.onActionViewCollapsed();
            mSearchQuery = "";
            return;
        }

        super.onBackPressed();
    }


}
