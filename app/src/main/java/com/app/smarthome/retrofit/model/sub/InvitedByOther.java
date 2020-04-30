
package com.app.smarthome.retrofit.model.sub;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvitedByOther {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("accept")
    @Expose
    private Integer accept;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("user")
    @Expose
    private InvitationDataUser user;

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

    public InvitationDataUser getUser() {
        return user;
    }

    public void setUser(InvitationDataUser user) {
        this.user = user;
    }

}
