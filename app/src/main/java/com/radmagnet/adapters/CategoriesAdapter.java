package com.radmagnet.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.radmagnet.R;
import com.radmagnet.activities.BaseActivity;
import com.radmagnet.models.Category;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by tmsbn on 2/18/15.
 */
public class CategoriesAdapter extends ArrayAdapter<Category> {

    int previousSelected = -1;

    public CategoriesAdapter(Context context, ArrayList<Category> categories) {
        super(context, 0, categories);
    }

    public void setSelectedCategory(int position) {


        if (previousSelected != -1)
            getItem(previousSelected).setSelected(false);

        previousSelected = position;
        getItem(position).setSelected(true);
        notifyDataSetChanged();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Category category = getItem(position);
        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(com.radmagnet.R.layout.row_category, parent, false);
        }

        ImageView thumbnailTv = (ImageView) convertView.findViewById(com.radmagnet.R.id.thumbnail);
        TextView categoryTv = (TextView) convertView.findViewById(com.radmagnet.R.id.category);
        ImageView categoryIconIv = (ImageView) convertView.findViewById(R.id.categoryIcon);

        try {

            int resID = getContext().getResources().getIdentifier(category.getIcon(), "drawable", getContext().getPackageName());
            categoryIconIv.setImageDrawable(getContext().getResources().getDrawable(resID));


        } catch (Exception e) {
            e.printStackTrace();
        }


        if (getItem(position).isSelected()) {
            convertView.setActivated(true);
            previousSelected = position;
        } else
            convertView.setActivated(false);


        categoryTv.setText(category.getName().toUpperCase(Locale.ENGLISH));

        Log.v("sidebar", category.getColor());

        try {
            thumbnailTv.setBackgroundColor(Color.parseColor(category.getColor()));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
