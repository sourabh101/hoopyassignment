package com.example.hoopyassignment.rest;

import com.example.hoopyassignment.models.CreateUserResponse;
import com.example.hoopyassignment.models.FetchDataResponse;
import com.example.hoopyassignment.models.MetaData;
import com.example.hoopyassignment.models.UserModel;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {

    @Multipart
    @POST("/api/1.0/testApis/upload_test")
    Call<JsonObject> getImageUrl(@Part MultipartBody.Part image);

    @POST("/api/1.0/testApis/insert_test")
    Call<CreateUserResponse> createNewUser(@Body UserModel userModel);

    @POST("/api/1.0/testApis/update_data_test")
    Call<MetaData> updateUser(@Body JsonObject jsonObject);

    @POST("/api/1.0/testApis/fetch_data_test")
    Call<FetchDataResponse> fetchUserData(@Body UserModel userModel);
}
