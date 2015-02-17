package tms.ubrats;


import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filterable;

public class NewsAdapter extends ArrayAdapter<News> implements Filterable {

    public NewsAdapter(Context context, int resource) {

        super(context, resource);
    }
}