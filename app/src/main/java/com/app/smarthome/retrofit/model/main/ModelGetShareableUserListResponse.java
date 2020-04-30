
package com.app.smarthome.retrofit.model.main;

import java.util.List;

import com.app.smarthome.retrofit.model.sub.ShareableUserData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelGetShareableUserListResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<ShareableUserData> data = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<ShareableUserData> getData() {
        return data;
    }

    public void setData(List<ShareableUserData> data) {
        this.data = data;
    }

}
