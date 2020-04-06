
package com.app.smarthome.retrofit.model.main;

import com.app.smarthome.retrofit.model.sub.RegisterData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelRegisterResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private RegisterData data;
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

    public RegisterData getData() {
        return data;
    }

    public void setData(RegisterData data) {
        this.data = data;
    }

}
