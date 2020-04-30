
package com.app.smarthome.retrofit.model.main;

import java.util.List;

import com.app.smarthome.retrofit.model.sub.InvitedToOther;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelInvitedToOtherResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<InvitedToOther> data = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<InvitedToOther> getData() {
        return data;
    }

    public void setData(List<InvitedToOther> data) {
        this.data = data;
    }

}
