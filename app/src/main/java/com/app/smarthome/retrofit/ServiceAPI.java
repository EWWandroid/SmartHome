package com.app.smarthome.retrofit;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceAPI extends ApiConstants {

    @FormUrlEncoded
    @POST(ENDPOINT_LOGIN)
    Call<ResponseBody> login(
            @Field(PARAMS_LOGIN_EMAIL) String email,
            @Field(PARAMS_LOGIN_PASSWORD) String password
    );

    @FormUrlEncoded
    @POST(ENDPOINT_REGISTER)
    Call<ResponseBody> register(
            @Field(PARAMS_REGISTER_EMAIL) String email,
            @Field(PARAMS_REGISTER_PASSWORD) String password,
            @Field(PARAMS_REGISTER_NAME) String name
    );

    @GET(ENDPOINT_GROUP)
    Call<ResponseBody> groupList(@Header(HEADER_KEY) String headerValue);//@Header(HEADER_KEY) String headerValue

    @POST(ENDPOINT_GROUP)
    Call<ResponseBody> createGroup(@Header(HEADER_KEY) String headerValue,
                                   @Field(CREATE_GROUP_PARAM_NAME) String name);

    @POST(ENDPOINT_SWITCH)
    Call<ResponseBody> createSwitches(@Header(HEADER_KEY) String headerValue);

    @GET(ENDPOINT_DEVICES)
    Call<ResponseBody> deviceList(@Header(HEADER_KEY) String headerValue);

    @POST(ENDPOINT_DEVICES)
    Call<ResponseBody> createDevices(@Header(HEADER_KEY) String headerValue,
                                     @Field(PARAMS_CREATE_DEVICES_NAME) String name,
                                     @Field(PARAMS_CREATE_DEVICES_MAC) String mac,
                                     @Field(PARAMS_CREATE_DEVICES_UUID) String uuid
    );

    @GET(ENDPOINT_SEARCH)
    Call<ResponseBody> searchUsers(@Header(HEADER_KEY) String headerValue,
                                   @Query(QUERY_SEARCH_EMAIL) String email);

    @POST(ENDPOINT_INVITATION + "/{id}")
    Call<ResponseBody> sendInvitation(@Header(HEADER_KEY) String headerValue,
                                      @Path("id") String id);


    @GET(ENDPOINT_INVITATION)
    Call<ResponseBody> getInvitedByList(@Header(HEADER_KEY) String headerValue);


    @GET(ENDPOINT_INVITED)
    Call<ResponseBody> getInvitedToList(@Header(HEADER_KEY) String headerValue);

    @PUT(ENDPOINT_INVITATION + "/{id}")
    Call<ResponseBody> acceptInvitation(@Header(HEADER_KEY) String headerValue,
                                        @Path("id") int id) ;

    @DELETE(ENDPOINT_INVITATION + "/{id}")
    Call<ResponseBody> rejectInvitation(@Header(HEADER_KEY) String headerValue,
                                        @Path("id") int id) ;

}