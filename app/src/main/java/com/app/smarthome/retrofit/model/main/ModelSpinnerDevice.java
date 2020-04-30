package com.app.smarthome.retrofit.model.main;

public class ModelSpinnerDevice {

    private String ssid;
    private String mac;

    public ModelSpinnerDevice(String ssid, String mac) {
        this.ssid = ssid;
        this.mac = mac;
    }

    public ModelSpinnerDevice(String ssid) {
        this.ssid = ssid;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
