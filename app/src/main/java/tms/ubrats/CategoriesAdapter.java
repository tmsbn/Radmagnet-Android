package tms.ubrats;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by tmsbn on 2/18/15.
 */
public class CategoriesAdapter extends ArrayAdapter<Category> {


    public CategoriesAdapter(Context context, ArrayList<Category> categories) {
        super(context, 0, categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Category category = getItem(position);
        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lv_rw_category, parent, false);
        }

        ImageView thumbnailTv = (ImageView) convertView.findViewById(R.id.thumbnail);
        TextView categoryTv = (TextView) convertView.findViewById(R.id.category);

        categoryTv.setText(category.name.toUpperCase(Locale.ENGLISH));

        try {
            thumbnailTv.setBackgroundColor(Color.parseColor(category.color));
        }catch(IllegalArgumentException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}