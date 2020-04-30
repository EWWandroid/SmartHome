
package com.app.smarthome.retrofit.model.main;

import com.app.smarthome.retrofit.model.sub.CreateSwitchData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelCreateSwitchResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private CreateSwitchData data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public CreateSwitchData getData() {
        return data;
    }

    public void setData(CreateSwitchData data) {
        this.data = data;
    }

}
