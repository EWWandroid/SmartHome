
package com.app.smarthome.retrofit.model.sub;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pivot {

    @SerializedName("switch_id")
    @Expose
    private Integer switchId;
    @SerializedName("group_id")
    @Expose
    private Integer groupId;

    public Integer getSwitchId() {
        return switchId;
    }

    public void setSwitchId(Integer switchId) {
        this.switchId = switchId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

}
