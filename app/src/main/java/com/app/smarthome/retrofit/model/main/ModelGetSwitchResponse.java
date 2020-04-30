
package com.app.smarthome.retrofit.model.main;

import com.app.smarthome.retrofit.model.sub.GetSwitchData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelGetSwitchResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private GetSwitchData data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public GetSwitchData getData() {
        return data;
    }

    public void setData(GetSwitchData data) {
        this.data = data;
    }

}
