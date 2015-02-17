package tms.ubrats;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;

public class Networking {

    private static final String URL = "http://temp/restpoint";
    private static RestInterface sRestService;

    public static RestInterface getRestClient() {

        if (sRestService == null) {

            String DATE_FORMAT = "dd/MM/yyyy";
            Gson gson = new GsonBuilder()
                    .setDateFormat(DATE_FORMAT)
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


    }
}
