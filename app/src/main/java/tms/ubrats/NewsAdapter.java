package tms.ubrats;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {


    private RealmResults<News> mRealmResults;
    Context mContext;

    NewsItemClickListener mListener;

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


        // setHasStableIds(true);
    }

    public void setOnItemClickedListener(NewsItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lv_rw_news, viewGroup, false);

        return new NewsHolder(itemView);
    }



    @Override
    public void onBindViewHolder(NewsHolder holder, final int position) {


        holder.headlineTv.setText(mRealmResults.get(position).getHeadline());
        holder.dateTv.setText(new SimpleDateFormat(BaseApplication.DATE_FORMAT, Locale.US).format(mRealmResults.get(position).getCreatedDate()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mListener != null)
                    mListener.onItemClick(mRealmResults.get(position), position);
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
        ImageButton bookmarkIbtn;


        public NewsHolder(View v) {
            super(v);
            dateTv = (TextView) v.findViewById(R.id.date);
            bookmarkIbtn = (ImageButton) v.findViewById(R.id.bookmark);
            headlineTv = (TextView) v.findViewById(R.id.headline);
        }


    }

    public void updateRealmResults(RealmResults<News> realmResults) {
        this.mRealmResults = realmResults;
        notifyDataSetChanged();
    }

    public static interface NewsItemClickListener {

        public void onItemClick(News news, int position);
    }
}