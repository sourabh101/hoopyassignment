package com.example.hoopyassignment.models;

import com.google.gson.annotations.SerializedName;

public class CreateUserResponse {

    @SerializedName("data")
    private UserModel userModel;

    @SerializedName("metadata")
    private MetaData metaData;

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }
}
