
package com.app.smarthome.retrofit.model.main;

import java.util.List;

import com.app.smarthome.retrofit.model.sub.InvitationList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelInvitationListResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<InvitationList> data = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<InvitationList> getData() {
        return data;
    }

    public void setData(List<InvitationList> data) {
        this.data = data;
    }

}
