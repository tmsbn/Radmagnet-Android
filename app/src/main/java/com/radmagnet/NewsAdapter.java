package com.radmagnet;


import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {


    private RealmResults<News> mRealmResults;
    Context mContext;

    NewsItemClickListener mListener;

    private int lastPosition = -1;

    int height;
    String mSearchTerm = "";

    public NewsAdapter(Context context, RealmResults<News> realmResults, boolean automaticUpdate) {

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

    public void setOnItemClickedListener(NewsItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(com.radmagnet.R.layout.lv_rw_news, viewGroup, false);

        return new NewsHolder(itemView);
    }

    @Override
    public long getItemId(int position) {

        return mRealmResults.get(position).getPostId().hashCode();
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, final int position) {

        final News news = mRealmResults.get(position);

        holder.creatorTv.setText(news.getCreator());




        try {

            //news image
            Picasso.with(mContext).load(news.getImageUrl()).into(holder.newsImageIv);

            //creator display picture
            Picasso.with(mContext).load(news.getCreatorDp()).transform(new CircleTransform()).into(holder.creatorDp);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //date
        holder.dateTv.setText(new SimpleDateFormat(BaseApplication.DATE_FORMAT, Locale.US).format(news.getCreatedDate()));


        if (mContext instanceof BaseActivity) {

            //headline
            BaseActivity activity = (BaseActivity) mContext;
            holder.headlineTv.setText(activity.highlight(mSearchTerm, news.getHeadline()));

            //category line
            int color = activity.getColorFromCategory(news.getCategory());
            holder.categoryLine.setBackgroundColor(color);
            Drawable drawable = holder.linearLayout.getBackground();

            //category
            holder.categoryTv.setText(activity.getTitleFromConfig(news.getCategory()).toUpperCase());
            drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));

            //events
            if (news.getStartDate() != null && news.getStartDate().getTime() != 0) {
                EventsView eventsView = new EventsView(mContext);
                eventsView.setDate(news.getStartDate());
                if (holder.linearLayout.getChildCount() < 2)
                    holder.linearLayout.addView(eventsView);

            } else {
                if (holder.linearLayout.getChildCount() > 1)
                    holder.linearLayout.removeViewAt(1);
            }

            //location
            if (!news.getLocation().equals("")) {
                holder.location.setText(news.getLocation());
                holder.location.setVisibility(View.VISIBLE);
            } else {
                holder.location.setVisibility(View.GONE);
            }


        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mListener != null)
                    mListener.onItemClick(news, position);
            }
        });




    }


    @Override
    public int getItemCount() {

        return null != mRealmResults ? mRealmResults.size() : 0;
    }


    public RealmResults<News> getRealmResults() {
        return mRealmResults;
    }


    public static class NewsHolder extends RecyclerView.ViewHolder {

        TextView dateTv;
        TextView headlineTv;
        TextView creatorTv;
        ImageView creatorDp;
        ImageButton bookmarkIbtn;
        View categoryLine;
        TextView categoryTv;
        ImageView newsImageIv;
        LinearLayout linearLayout;
        TextView location;


        public NewsHolder(View v) {

            super(v);
            categoryTv = (TextView) v.findViewById(com.radmagnet.R.id.category);
            creatorTv = (TextView) v.findViewById(com.radmagnet.R.id.creator);
            categoryLine = v.findViewById(com.radmagnet.R.id.categoryColor);
            dateTv = (TextView) v.findViewById(com.radmagnet.R.id.date);
            bookmarkIbtn = (ImageButton) v.findViewById(com.radmagnet.R.id.bookmark);
            headlineTv = (TextView) v.findViewById(com.radmagnet.R.id.headline);
            newsImageIv = (ImageView) v.findViewById(com.radmagnet.R.id.newsImage);
            creatorDp = (ImageView) v.findViewById(com.radmagnet.R.id.creatorDp);
            linearLayout = (LinearLayout) v.findViewById(com.radmagnet.R.id.specific_details);
            location = (TextView) v.findViewById(R.id.location);


        }


    }

    public void setSearchTerm(String searchTerm) {
        if (searchTerm != null)
            mSearchTerm = searchTerm;
    }

    public void updateRealmResults(RealmResults<News> realmResults) {
        this.mRealmResults = realmResults;
        notifyDataSetChanged();


    }


    public static interface NewsItemClickListener {

        public void onItemClick(News news, int position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? com.radmagnet.R.anim.up_from_bottom : com.radmagnet.R.anim.down_from_top);
        viewToAnimate.startAnimation(animation);
        lastPosition = position;


    }


}