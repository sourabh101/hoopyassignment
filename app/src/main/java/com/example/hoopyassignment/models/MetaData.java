package com.example.hoopyassignment.models;

import com.google.gson.annotations.SerializedName;

public class MetaData {

    @SerializedName("response_code")
    private int responseCode;

    @SerializedName("response_text")
    private String responseText;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String resnponseText) {
        this.responseText = resnponseText;
    }
}
