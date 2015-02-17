package tms.ubrats;


import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.RealmObject;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Path;

public class Networking {

    private static final String URL = "http://temp/restpoint";

    private static RestInterface sRestService;

    public static RestInterface getRestClient() {

        if (sRestService == null) {

            String DATE_FORMAT = "dd/MM/yyyy";
            Gson gson = new GsonBuilder()
                    .setDateFormat(DATE_FORMAT)
                    .setExclusionStrategies(

                            new ExclusionStrategy() {
                                @Override
                                public boolean shouldSkipField(FieldAttributes f) {
                                    return f.getDeclaringClass().equals(RealmObject.class);
                                }

                                @Override
                                public boolean shouldSkipClass(Class<?> clazz) {
                                    return false;
                                }
                            }
                    )
                    .create();

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(URL)
                    .setConverter(new GsonConverter(gson))
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setLog(new AndroidLog("retrofit logs"))
                    .build();


            sRestService = restAdapter.create(RestInterface.class);


        }
        return sRestService;
    }

    public interface RestInterface {

        @GET("/config.json")
        void getConfig(Callback<ConfigVars> callback);

        @GET("/announcements/{date}")
        void getAnnouncements(@Path("date") String date, Callback<NewsResponse> callback);


    }
}
