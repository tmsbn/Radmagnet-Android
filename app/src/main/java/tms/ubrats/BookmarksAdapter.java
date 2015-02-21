package tms.ubrats;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;

import java.text.SimpleDateFormat;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.BookmarksViewHolder> implements SwipeableItemAdapter<BookmarksAdapter.BookmarksViewHolder> {


    private RealmResults<News> mRealmResults;
    Context mContext;

    public BookmarksAdapter(Context context, RealmResults<News> realmResults, boolean automaticUpdate) {

        mRealmResults = realmResults;
        mContext=context;

        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        if (realmResults == null) {
            throw new IllegalArgumentException("RealmResults cannot be null");
        }

        if (automaticUpdate) {
            Realm.getInstance(context).addChangeListener(new RealmChangeListener() {
                @Override
                public void onChange() {
                    notifyDataSetChanged();
                }
            });
        }

        setHasStableIds(true);
    }

    @Override
    public BookmarksViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lv_rw_bookmark, viewGroup, false);

        return new BookmarksViewHolder(itemView);
    }

    @Override
    public long getItemId(int position) {
        return mRealmResults.get(position).getPostId().hashCode();
    }



    @Override
    public void onBindViewHolder(BookmarksViewHolder holder, int position) {


        holder.headlineTv.setText(mRealmResults.get(position).getHeadline());
        holder.dateTv.setText(new SimpleDateFormat(BaseApplication.DATE_FORMAT, Locale.US).format(mRealmResults.get(position).getCreatedDate()));
    }


    @Override
    public int getItemCount() {
        return mRealmResults.size();
    }


    public RealmResults<News> getRealmResults() {
        return mRealmResults;
    }

    @Override
    public int onGetSwipeReactionType(BookmarksViewHolder bookmarksViewHolder, int i, int i2) {
        return RecyclerViewSwipeManager.REACTION_CAN_SWIPE_BOTH;
    }

    @Override
    public void onSetSwipeBackground(BookmarksViewHolder bookmarksViewHolder, int reaction) {
       bookmarksViewHolder.itemView.setBackgroundResource(R.color.dark_gray);

    }

    @Override
    public int onSwipeItem(BookmarksViewHolder bookmarksViewHolder, int result) {
        if (result == RecyclerViewSwipeManager.RESULT_SWIPED_LEFT || result == RecyclerViewSwipeManager.RESULT_SWIPED_RIGHT)
            return RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_REMOVE_ITEM;
        else
            return RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_DEFAULT;

    }

    @Override
    public void onPerformAfterSwipeReaction(final BookmarksViewHolder bookmarksViewHolder, int result, int reaction) {

        Realm.getInstance(mContext).executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mRealmResults.get(bookmarksViewHolder.getPosition()).setBookmarked(false);
            }
        });

    }


    public static class BookmarksViewHolder extends AbstractSwipeableItemViewHolder {

        protected TextView headlineTv;
        protected TextView dateTv;
        protected ViewGroup mContainer;


        public BookmarksViewHolder(View v) {
            super(v);
            mContainer = (ViewGroup) v.findViewById(R.id.container);
            headlineTv = (TextView) v.findViewById(R.id.headline);
            dateTv = (TextView) v.findViewById(R.id.date);

        }


        @Override
        public View getSwipeableContainerView() {
            return mContainer;
        }


    }

    public void updateRealmResults(RealmResults<News> realmResults) {
        this.mRealmResults = realmResults;
        notifyDataSetChanged();
    }
}