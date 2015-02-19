package tms.ubrats;

import android.app.Application;

/**
 * Created by tmsbn on 2/18/15.
 */
public class BaseApplication extends Application {


   public static String DATE_FORMAT="dd MMMM yyyy @ hh:mm aa";

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
