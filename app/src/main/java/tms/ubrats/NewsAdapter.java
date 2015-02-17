package tms.ubrats;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

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

            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.timeStampTv = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return null;
    }

    public RealmResults<News> getRealmResults() {
        return realmResults;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    private static class ViewHolder {

        TextView timeStampTv;
        TextView headlineTv;
        ImageButton bookmarkIbtn;
    }
}