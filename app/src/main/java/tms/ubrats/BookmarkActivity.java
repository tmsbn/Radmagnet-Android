package tms.ubrats;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class BookmarkActivity extends ActionBarActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    @InjectView(R.id.bookmarksList)
    RecyclerView mBookmarksRv;

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    BookmarksAdapter mBookmarkAdapter;


    String mSearchQuery = "";

    SearchView mSearchView;
    RecyclerViewSwipeManager mRecyclerViewSwipeManager;
    private RecyclerViewTouchActionGuardManager mRecyclerViewTouchActionGuardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        ButterKnife.inject(this);

        setupToolBar();
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


    }

    private RealmResults<News> getRealmData() {

        RealmQuery<News> realmQuery = Realm.getInstance(this).where(News.class);

        if (!mSearchQuery.equals("")) {
            realmQuery = realmQuery.contains("headline", mSearchQuery, false);
        }

        realmQuery.equalTo("isBookmarked",true);

        return realmQuery.findAllSorted("createdDate", false);

    }

    private void setupToolBar() {

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
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
    protected void onDestroy() {

        mRecyclerViewSwipeManager.release();
        mRecyclerViewTouchActionGuardManager.release();

        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
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
        mSearchQuery = query;
        mBookmarkAdapter.updateRealmResults(getRealmData());

        return true;
    }

    @Override
    public boolean onClose() {

        mBookmarkAdapter.updateRealmResults(getRealmData());
        return true;
    }
}
