
package com.app.smarthome.retrofit.model.main;

import java.util.List;

import com.app.smarthome.retrofit.model.sub.SearchUserData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelSearchUserListResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<SearchUserData> data = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<SearchUserData> getData() {
        return data;
    }

    public void setData(List<SearchUserData> data) {
        this.data = data;
    }

}
