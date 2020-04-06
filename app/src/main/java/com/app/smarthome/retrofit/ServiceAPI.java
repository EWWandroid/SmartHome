package com.app.smarthome.retrofit;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ServiceAPI extends ApiConstants {

    @FormUrlEncoded
    @POST(ENDPOINT_LOGIN)
    @Headers(HEADER)
    Call<ResponseBody>login(
            @Field(PARAMS_LOGIN_EMAIL) String email,
            @Field(PARAMS_LOGIN_PASSWORD) String password
    );

    @FormUrlEncoded
    @POST(ENDPOINT_REGISTER)
    @Headers(HEADER)
    Call<ResponseBody>register(
            @Field(PARAMS_REGISTER_EMAIL) String email,
            @Field(PARAMS_REGISTER_PASSWORD) String password,
            @Field(PARAMS_REGISTER_NAME) String name
    );

}
