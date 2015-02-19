package tms.ubrats;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

public class NewsAdapter extends RealmBaseAdapter<News> implements Filterable {



    public NewsAdapter(Context context, RealmResults<News> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.lv_rw_news, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.dateTv = (TextView) convertView.findViewById(R.id.date);
            viewHolder.bookmarkIbtn = (ImageButton) convertView.findViewById(R.id.bookmark);
            viewHolder.headlineTv = (TextView) convertView.findViewById(R.id.headline);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.headlineTv.setText(getItem(position).getHeadline());
        viewHolder.dateTv.setText(new SimpleDateFormat(BaseApplication.DATE_FORMAT).format(getItem(position).getCreatedDate()));

        return convertView;
    }

    public RealmResults<News> getRealmResults() {
        return realmResults;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    private static class ViewHolder {

        TextView dateTv;
        TextView headlineTv;
        ImageButton bookmarkIbtn;
    }
}