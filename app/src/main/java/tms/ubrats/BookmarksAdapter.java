package tms.ubrats;


import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.BookmarksViewHolder> implements SwipeableItemAdapter<BookmarksAdapter.BookmarksViewHolder> {


    private RealmResults<News> mRealmResults;
    Context mContext;

    BookmarkListener bookmarkListener;

    String mSearchTerm="";

    public BookmarksAdapter(Context context, RealmResults<News> realmResults, boolean automaticUpdate) {

        mRealmResults = realmResults;
        mContext = context;

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

    public void setOnItemClickedListener(BookmarkListener bookmarkListener) {
        this.bookmarkListener = bookmarkListener;
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
    public void onBindViewHolder(BookmarksViewHolder holder, final int position) {

        News news=mRealmResults.get(position);
        holder.headlineTv.setText(news.getHeadline());
        holder.dateTv.setText(new SimpleDateFormat(BaseApplication.DATE_FORMAT, Locale.US).format(news.getCreatedDate()));
        Picasso.with(mContext).load(news.getImageUrl()).into(holder.icon);
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bookmarkListener != null)
                    bookmarkListener.onItemClick(mRealmResults.get(position), position);
            }
        });

        if (mContext instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) mContext;
            holder.headlineTv.setText(activity.highlight(mSearchTerm,news.getHeadline()));
            int color = activity.getColorFromCategory(news.getCategory());
            holder.categoryTv.setText(activity.getTitleFromConfig(news.getCategory()).toUpperCase());
            holder.categoryTv.setBackgroundColor(color);
            Drawable drawable = holder.categoryTv.getBackground();
            drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
        }

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
       // bookmarksViewHolder.itemView.setBackgroundResource(R.color.dark_gray);

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

        if (reaction == RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_REMOVE_ITEM) {
            Realm.getInstance(mContext).executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    mRealmResults.get(bookmarksViewHolder.getPosition()).setBookmarked(false);
                }
            });
        }

    }


    public static class BookmarksViewHolder extends AbstractSwipeableItemViewHolder {

        protected TextView headlineTv;
        protected TextView dateTv;
        protected ViewGroup container;
        protected ImageView icon;
        protected TextView categoryTv;


        public BookmarksViewHolder(View v) {
            super(v);
            container = (ViewGroup) v.findViewById(R.id.container);
            headlineTv = (TextView) v.findViewById(R.id.headline);
            dateTv = (TextView) v.findViewById(R.id.date);
            icon = (ImageView) v.findViewById(R.id.newsImage);
            categoryTv = (TextView) v.findViewById(R.id.category);



        }


        @Override
        public View getSwipeableContainerView() {
            return container;
        }


    }

    public void updateRealmResults(RealmResults<News> realmResults) {
        this.mRealmResults = realmResults;
        notifyDataSetChanged();
    }

    public void setSearchTerm(String searchTerm) {
        if (searchTerm != null)
            mSearchTerm = searchTerm;
    }

    public interface BookmarkListener {

        public void onItemClick(News news,int position);
    }
}