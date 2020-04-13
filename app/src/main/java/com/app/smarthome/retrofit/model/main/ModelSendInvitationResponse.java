
package com.app.smarthome.retrofit.model.main;

import com.app.smarthome.retrofit.model.sub.SendInvitationData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelSendInvitationResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private SendInvitationData data;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public SendInvitationData getData() {
        return data;
    }

    public void setData(SendInvitationData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
