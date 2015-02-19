package tms.ubrats;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;

/**
 * Created by tmsbn on 2/18/15.
 */
public abstract class BaseActivity extends ActionBarActivity {

    protected static final ConfigVars sConfig = null;

    SharedPreferences mPrefs;

    protected String LAST_UPDATED="last_updated";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mPrefs=getSharedPreferences("news_data",MODE_PRIVATE);


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
}
