
package com.app.smarthome.retrofit.model.main;

import java.util.List;

import com.app.smarthome.retrofit.model.sub.InvitedByOther;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelInvitedByOthersResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<InvitedByOther> data = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<InvitedByOther> getData() {
        return data;
    }

    public void setData(List<InvitedByOther> data) {
        this.data = data;
    }

}
