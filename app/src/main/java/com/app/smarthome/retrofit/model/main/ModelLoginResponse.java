
package com.app.smarthome.retrofit.model.main;

import com.app.smarthome.retrofit.model.sub.Auth;
import com.app.smarthome.retrofit.model.sub.LoginData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelLoginResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private LoginData data;
    @SerializedName("auth")
    @Expose
    private Auth auth;
    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public LoginData getData() {
        return data;
    }

    public void setData(LoginData data) {
        this.data = data;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public static class ModelRegisterResponse {

        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("data")
        @Expose
        private LoginData data;

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public LoginData getData() {
            return data;
        }

        public void setData(LoginData data) {
            this.data = data;
        }

    }
}
