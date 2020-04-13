
package com.app.smarthome.retrofit.model.main;

import java.util.List;

import com.app.smarthome.retrofit.model.sub.InviteData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelInvitedListResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<InviteData> data = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<InviteData> getData() {
        return data;
    }

    public void setData(List<InviteData> data) {
        this.data = data;
    }

}
