package fr.iut2.saeprojet.exemple.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIUserClient {


    //https://square.github.io/retrofit/?utm_source=developer.android.com&utm_medium=referral
    //https://www.digitalocean.com/community/tutorials/retrofit-android-example-tutorial

    //
    private static final String BASE_URL = "https://reqres.in";

    //
    private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private static OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

    // Instance retrofit
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            //.client(client)
            .build();

    public static APIUserService getAPIService() {
        return retrofit.create(APIUserService.class);
    }

}
