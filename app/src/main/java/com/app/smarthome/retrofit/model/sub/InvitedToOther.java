
package com.app.smarthome.retrofit.model.sub;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvitedToOther {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("accept")
    @Expose
    private Integer accept;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("invite")
    @Expose
    private InviteUserData invite;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccept() {
        return accept;
    }

    public void setAccept(Integer accept) {
        this.accept = accept;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public InviteUserData getInvite() {
        return invite;
    }

    public void setInvite(InviteUserData invite) {
        this.invite = invite;
    }
}
