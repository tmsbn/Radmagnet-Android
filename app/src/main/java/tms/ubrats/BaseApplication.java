package tms.ubrats;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by tmsbn on 2/18/15.
 */
public class BaseApplication extends Application {


   public static String DATE_FORMAT="dd MMMM yyyy @ hh:mm aa";

    public static int NOTIF_REQUEST_CODE=1506;

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/AlegreyaSans-Regular.otf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

    }


    public static enum Categories {

        EVENTS("events"), NEWS("news"), LIFE_HACKS("hacks");

        private String text;

        private Categories(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }


}
