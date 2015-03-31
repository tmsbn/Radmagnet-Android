package com.radmagnet.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.radmagnet.R;
import com.radmagnet.models.Category;
import com.radmagnet.models.ConfigVars;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.text.Normalizer;
import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * Created by tmsbn on 2/18/15.
 */
public abstract class BaseActivity extends ActionBarActivity {

    protected static final ConfigVars sConfig = null;

    SharedPreferences mPrefs;

    protected String LAST_UPDATED_KEY = "last_updated";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mPrefs = getSharedPreferences("news_data", MODE_PRIVATE);


    }


    public ConfigVars getConfig() {

        if (sConfig == null) {

            try {
                Gson gson = new Gson();
                InputStream input = getAssets().open("config.json");
                String text = IOUtils.toString(input);
                return gson.fromJson(text, ConfigVars.class);

            } catch (Exception e) {
                Log.d("sConfig test", e.getMessage());
                return null;

            }

        } else
            return sConfig;
    }

    public int getColorFromCategory(String postType) {

        ArrayList<Category> categories = getConfig().categories;
        for (Category category : categories) {
            if (category.getValue().equalsIgnoreCase(postType))
                return Color.parseColor(category.getColor());
        }

        return Color.WHITE;
    }

    public String getTitleFromConfig(String categoryName) {

        ArrayList<Category> categories = getConfig().categories;
        for (Category category : categories) {
            if (category.getValue().equalsIgnoreCase(categoryName))
                return category.getName();
        }

        return "";
    }


    public Drawable applyColorToDrawable(int drawableId, int color) {

        Drawable drawable = getResources().getDrawable(drawableId);
        drawable.mutate().setColorFilter(new
                PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));

        return drawable;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    public CharSequence highlight(String search, String originalText) {
        // ignore case and accents
        // the same thing should have been done for the search text
        if (search.equals(""))
            return originalText;

        String normalizedText = Normalizer.normalize(originalText, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();

        int start = normalizedText.indexOf(search);
        if (start < 0) {
            // not found, nothing to to
            return originalText;
        } else {
            // highlight each appearance in the original text
            // while searching in normalized text
            Spannable highlighted = new SpannableString(originalText);
            while (start >= 0) {
                int spanStart = Math.min(start, originalText.length());
                int spanEnd = Math.min(start + search.length(), originalText.length());

                highlighted.setSpan(new BackgroundColorSpan(Color.parseColor("#e4d90c")), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                start = normalizedText.indexOf(search, spanEnd);
            }

            return highlighted;
        }
    }

    protected void setupActionBar(boolean withTitle, Toolbar toolbar) {

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);




        if (withTitle) {

            actionBar.setTitle(getStyledActionTitle(getTitle()));
            actionBar.setDisplayShowTitleEnabled(true);
        } else {
            actionBar.setLogo(getResources().getDrawable(com.radmagnet.R.drawable.ic_branding));
            actionBar.setDisplayShowTitleEnabled(false);

            //Show beta tag if version code is less than 100
            try {
                TextView brandingTv = (TextView) toolbar.findViewById(R.id.beta_tag);
                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                int versionCode = pInfo.versionCode;
                if (versionCode < 100)
                    brandingTv.setVisibility(View.VISIBLE);

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }

    }

    public SpannableString getStyledActionTitle(CharSequence title) {

        SpannableString s = new SpannableString(getTitle().toString().toUpperCase());
        CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getAssets(), "fonts/AlegreyaSans-BlackItalic.otf"));
        s.setSpan(typefaceSpan, 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return s;

    }

    public int getValueInDp(int valueInDp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, getResources().getDisplayMetrics());

    }

    public String getDateSuffix(int day) {

        switch (day) {
            case 1:
            case 21:
            case 31:
                return ("st");

            case 2:
            case 22:
                return ("nd");

            case 3:
            case 23:
                return ("rd");

            default:
                return ("th");
        }
    }
}
