package com.radmagnet;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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

    int previousSelected = -1;

    public CategoriesAdapter(Context context, ArrayList<Category> categories) {
        super(context, 0, categories);
    }

    public void setSelectedCategory(int position) {


        if (previousSelected != -1)
            getItem(previousSelected).isSelected = false;

        previousSelected = position;
        getItem(position).isSelected = true;
        notifyDataSetChanged();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Category category = getItem(position);
        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(com.radmagnet.R.layout.lv_rw_category, parent, false);
        }

        ImageView thumbnailTv = (ImageView) convertView.findViewById(com.radmagnet.R.id.thumbnail);
        TextView categoryTv = (TextView) convertView.findViewById(com.radmagnet.R.id.category);


        if (getItem(position).isSelected) {
            convertView.setActivated(true);
            previousSelected=position;
        }
        else
            convertView.setActivated(false);


        categoryTv.setText(category.name.toUpperCase(Locale.ENGLISH));

        Log.v("sidebar", category.color);

        try {
            thumbnailTv.setBackgroundColor(Color.parseColor(category.color));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
