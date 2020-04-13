
package com.app.smarthome.retrofit.model.main;

import java.util.List;

import com.app.smarthome.retrofit.model.sub.GroupListData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelGroupsListResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<GroupListData> data = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<GroupListData> getData() {
        return data;
    }

    public void setData(List<GroupListData> data) {
        this.data = data;
    }

}
