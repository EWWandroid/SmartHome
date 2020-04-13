
package com.app.smarthome.retrofit.model.main;

import java.util.List;

import com.app.smarthome.retrofit.model.sub.DevicesListData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelDeviceListResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<DevicesListData> data = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<DevicesListData> getData() {
        return data;
    }

    public void setData(List<DevicesListData> data) {
        this.data = data;
    }

}
