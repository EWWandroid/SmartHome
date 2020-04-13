
package com.app.smarthome.retrofit.model.sub;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Switch {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("device_id")
    @Expose
    private Integer deviceId;
    @SerializedName("pin")
    @Expose
    private String pin;
    @SerializedName("state")
    @Expose
    private Integer state;
    @SerializedName("used")
    @Expose
    private Integer used;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("pivot")
    @Expose
    private Pivot pivot;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Pivot getPivot() {
        return pivot;
    }

    public void setPivot(Pivot pivot) {
        this.pivot = pivot;
    }

}
