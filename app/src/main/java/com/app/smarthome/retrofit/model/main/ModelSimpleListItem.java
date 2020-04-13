package com.app.smarthome.retrofit.model.main;

import java.time.temporal.Temporal;

public class ModelSimpleListItem {
    private String email;

    public ModelSimpleListItem(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
