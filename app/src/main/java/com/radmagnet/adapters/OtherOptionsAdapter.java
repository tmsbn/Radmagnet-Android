package com.radmagnet.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

public class OtherOptionsAdapter extends ArrayAdapter<Category> {

    public OtherOptionsAdapter(Context context, ArrayList<Category> categories) {
        super(context, 0, categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Category options = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(com.radmagnet.R.layout.row_category, parent, false);
        }

        TextView category = (TextView) convertView.findViewById(com.radmagnet.R.id.category);
        ImageView color = (ImageView) convertView.findViewById(com.radmagnet.R.id.thumbnail);
        ImageView categoryIconIv = (ImageView) convertView.findViewById(R.id.categoryIcon);

        try {

            int resID = getContext().getResources().getIdentifier(options.getIcon(), "drawable", getContext().getPackageName());
            categoryIconIv.setImageDrawable(getContext().getResources().getDrawable(resID));

        } catch (Exception e) {
            e.printStackTrace();
        }

        color.setBackgroundColor(Color.parseColor(options.getColor()));
        category.setText(options.getName());


        return convertView;
    }
}