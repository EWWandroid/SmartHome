package com.app.smarthome.retrofit;

public interface ApiConstants {

    String HEADER_KEY = "Authorization";
    String HEADER_VALUE = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOjIsImlhdCI6MTU4NTY0MDkwNH0.tZs0UEyLX5G4aVNDFir29bHabCdqFprRajcfhrBE4Nc";
    String HEADER = HEADER_KEY + ":" + HEADER_VALUE;

    String BASE_URL = "https://asmartparking.com:6004/api/v1/";

    String ENDPOINT_LOGIN = "login";
    String PARAMS_LOGIN_EMAIL = "email";
    String PARAMS_LOGIN_PASSWORD = "password";

    String ENDPOINT_REGISTER = "register";
    String PARAMS_REGISTER_EMAIL = "email";
    String PARAMS_REGISTER_PASSWORD = "password";
    String PARAMS_REGISTER_NAME = "name";

    String ENDPOINT_USERS = "users";

}
