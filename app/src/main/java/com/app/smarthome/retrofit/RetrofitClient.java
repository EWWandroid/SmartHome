package com.app.smarthome.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient implements ApiConstants {

    private static ServiceAPI serviceAPI = null;

    public static ServiceAPI getServiceApi() {
        //If condition to ensure we don't create multiple retrofit instances in a single application
        if (serviceAPI == null) {
            //using instance of retrofit we will process our request
            OkHttpClient okHttpClient = new OkHttpClient()
                    .newBuilder()
                    .connectTimeout(10, TimeUnit.MINUTES)
                    .readTimeout(30,TimeUnit.MINUTES)
                    .build();


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();

            //retrofit automatically creates implementation of getPost() method
            serviceAPI = retrofit.create(ServiceAPI.class);
        }

        return serviceAPI;
    }

}
