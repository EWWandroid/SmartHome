package com.app.smarthome.retrofit.model.main;

import com.app.smarthome.retrofit.model.sub.InvitationDataUser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelFriendsData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("accept")
    @Expose
    private Integer accept;
    @SerializedName("user")
    @Expose
    private InvitationDataUser user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAccept() {
        return accept;
    }

    public void setAccept(Integer accept) {
        this.accept = accept;
    }

    public InvitationDataUser getUser() {
        return user;
    }

    public void setUser(InvitationDataUser user) {
        this.user = user;
    }


}
