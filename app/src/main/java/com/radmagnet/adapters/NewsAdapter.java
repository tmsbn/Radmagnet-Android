package com.radmagnet.adapters;


import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.radmagnet.BaseApplication;
import com.radmagnet.utils.CircleTransform;
import com.radmagnet.customviews.EventsView;
import com.radmagnet.R;
import com.radmagnet.activities.BaseActivity;
import com.radmagnet.models.News;
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

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(com.radmagnet.R.layout.row_news, viewGroup, false);

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
            Picasso.with(mContext).load(news.getCreatorDp()).placeholder(R.drawable.ic_fb).transform(new CircleTransform()).into(holder.creatorDp);
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


            //category
           // holder.categoryIv.setText(activity.getTitleFromConfig(news.getCategory()).toUpperCase());
            holder.categoryIv.getBackground().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
            try{
                holder.categoryIv.setImageDrawable(activity.getIconFromCategory(news.getCategory()));
            }catch (Exception e){
                e.printStackTrace();
            }



            //is the news item read
            if(news.isRead())
                holder.isNewTv.setVisibility(View.GONE);
            else {
                holder.isNewTv.setVisibility(View.VISIBLE);
                holder.isNewTv.getBackground().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));

            }


            //events
            holder.frameLayout.getBackground().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
            if (news.getStartDate() != null && news.getStartDate().getTime() != 0) {

                EventsView eventsView = new EventsView(mContext);
                eventsView.setDate(news.getStartDate());
                if (holder.frameLayout.getChildCount() == 0)
                    holder.frameLayout.addView(eventsView);

            } else {
                if (holder.frameLayout.getChildCount() > 0)
                    holder.frameLayout.removeViewAt(0);
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
        ImageView categoryIv;
        ImageView newsImageIv;
        FrameLayout frameLayout;
        TextView location;
        TextView isNewTv;


        public NewsHolder(View v) {

            super(v);
            categoryIv = (ImageView) v.findViewById(com.radmagnet.R.id.category);
            creatorTv = (TextView) v.findViewById(com.radmagnet.R.id.creator);
            categoryLine = v.findViewById(com.radmagnet.R.id.categoryColor);
            dateTv = (TextView) v.findViewById(com.radmagnet.R.id.date);
            bookmarkIbtn = (ImageButton) v.findViewById(com.radmagnet.R.id.bookmark);
            headlineTv = (TextView) v.findViewById(com.radmagnet.R.id.headline);
            newsImageIv = (ImageView) v.findViewById(com.radmagnet.R.id.newsImage);
            creatorDp = (ImageView) v.findViewById(com.radmagnet.R.id.creatorDp);
            frameLayout = (FrameLayout) v.findViewById(com.radmagnet.R.id.specific_details);
            location = (TextView) v.findViewById(R.id.location);
            isNewTv= (TextView) v.findViewById(R.id.newItem);


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