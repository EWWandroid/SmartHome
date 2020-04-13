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

    String ENDPOINT_GROUP = "groups";
    String CREATE_GROUP_PARAM_NAME = "name";

    String ENDPOINT_SWITCH = "switches";

    String ENDPOINT_DEVICES = "devices";
    String PARAMS_CREATE_DEVICES_NAME = "name";
    String PARAMS_CREATE_DEVICES_MAC = "mac";
    String PARAMS_CREATE_DEVICES_UUID = "uuid";

    String ENDPOINT_SEARCH = "search/users";
    String QUERY_SEARCH_EMAIL = "email";

    String ENDPOINT_INVITATION = "users/invitations";

    String ENDPOINT_INVITED = "users/invited";


}
