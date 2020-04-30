
package com.app.smarthome.retrofit.model.main;

import com.app.smarthome.retrofit.model.sub.UpdateSwitchData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelUpdateSwitchResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private UpdateSwitchData data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public UpdateSwitchData getData() {
        return data;
    }

    public void setData(UpdateSwitchData data) {
        this.data = data;
    }

}
