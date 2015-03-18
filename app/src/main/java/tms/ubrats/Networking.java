package tms.ubrats;


import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

import io.realm.RealmObject;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public class Networking {

    private static final String URL = "http://radmagnet.com";

    private static RestInterface sRestService;

    public static RestInterface getRestClient() {

        if (sRestService == null) {

            Gson gson = new GsonBuilder()
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
                    .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                            return new Date(json.getAsJsonPrimitive().getAsLong()*1000);
                        }
                    })

                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
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

        @GET("/GET/config.json")
        void getConfig(Callback<ConfigVars> callback);


        @GET("/GET/highlights/{date}")
        void getAnnouncements(@Path("date") String date, Callback<NewsResponse> callback);

        @GET("POST/beta/feedback")
        void getFeedback(@Body Feedback feedback, Callback<Feedback.Output> callback);


    }
}
