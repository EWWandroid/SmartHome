
package com.app.smarthome.retrofit.model.main;

import com.app.smarthome.retrofit.model.sub.ShareSwitchData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelShareSwitchResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private ShareSwitchData data;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public ShareSwitchData getData() {
        return data;
    }

    public void setData(ShareSwitchData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
