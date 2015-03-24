package com.radmagnet;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.radmagnet.models.Category;

import java.util.ArrayList;

public class OtherOptionsAdapter extends ArrayAdapter<Category> {

    public OtherOptionsAdapter(Context context, ArrayList<Category> categories) {
        super(context, 0, categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Category options = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(com.radmagnet.R.layout.lv_rw_category, parent, false);
        }

        TextView category = (TextView) convertView.findViewById(com.radmagnet.R.id.category);
        ImageView color = (ImageView) convertView.findViewById(com.radmagnet.R.id.thumbnail);

        color.setBackgroundColor(Color.parseColor(options.getColor()));
        category.setText(options.getName());


        return convertView;
    }
}