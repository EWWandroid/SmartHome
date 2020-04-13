
package com.app.smarthome.retrofit.model.main;

import com.app.smarthome.retrofit.model.sub.AcceptInvitationData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelAcceptInvitationResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private AcceptInvitationData data;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public AcceptInvitationData getData() {
        return data;
    }

    public void setData(AcceptInvitationData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
