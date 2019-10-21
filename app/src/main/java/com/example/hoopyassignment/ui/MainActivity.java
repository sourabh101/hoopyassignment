package com.example.hoopyassignment.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hoopyassignment.R;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private MaterialButton btnNewUser;
    private MaterialButton btnUpdateUser;
    private MaterialButton btnFetchData;
    private SharedPreferences preferences;
    private LinearLayout llUserProfile;
    private TextView tvUserName;
    private TextView tvEmail;
    private TextView tvPhoneNumber;
    private ImageView ivProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNewUser = findViewById(R.id.btnNewUser);
        btnUpdateUser = findViewById(R.id.btnUpdateUser);
        btnFetchData = findViewById(R.id.btnFetchData);

        llUserProfile = findViewById(R.id.llUserProfile);
        tvUserName = findViewById(R.id.tvUserName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhoneNumber = findViewById(R.id.tvPhone);
        ivProfilePic = findViewById(R.id.ivProfilePicture);

        init();
    }

    private void init() {
        updateUi();
        setNewUserButtonOnClickListener();
        setUpdateUserButtonOnClickListener();
        setFetchDataOnClickListener();
    }

    private void setFetchDataOnClickListener() {
        btnFetchData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FetchDataActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpdateUserButtonOnClickListener() {
        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UpdateUserActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setNewUserButtonOnClickListener() {
        btnNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewUserActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateUi() {
        preferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        String name = preferences.getString("name", null);
        if (name == null) {
            btnUpdateUser.setVisibility(View.GONE);
            llUserProfile.setVisibility(View.GONE);
        } else {
            btnUpdateUser.setVisibility(View.VISIBLE);
            llUserProfile.setVisibility(View.VISIBLE);
            String email = preferences.getString("email", null);
            String userName = preferences.getString("userName", null);
            String phoneNumber = preferences.getString("phoneNumber", null);
            String imageUrl = preferences.getString("image_url", null);
            tvUserName.setText(userName);
            tvPhoneNumber.setText(phoneNumber);
            tvEmail.setText(email);
            imageUrl = imageUrl.replaceAll("\"", "");
            Glide.with(this).load(imageUrl).into(ivProfilePic);
        }
    }


}
