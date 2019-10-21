package com.example.hoopyassignment.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FetchDataResponse {

    @SerializedName("metadata")
    private MetaData metaData;

    @SerializedName("data")
    private ArrayList<UserModel> data;

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public ArrayList<UserModel> getData() {
        return data;
    }

    public void setData(ArrayList<UserModel> data) {
        this.data = data;
    }
}

