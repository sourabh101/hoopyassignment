package com.example.hoopyassignment.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.hoopyassignment.R;
import com.example.hoopyassignment.models.MetaData;
import com.example.hoopyassignment.rest.Api;
import com.example.hoopyassignment.rest.RetrofitClientInstance;
import com.example.hoopyassignment.viewmodels.UpdateUserViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserActivity extends AppCompatActivity {

    private TextInputEditText etName;
    private TextInputEditText etEmail;
    private TextInputEditText etUserName;
    private TextInputEditText etPhoneNumber;
    private MaterialButton btnUpdateUser;
    private String name;
    private String email;
    private String userName;
    private String phoneNumber;
    private int id;
    private Api api;
    private SharedPreferences preferences;
    private boolean isClickEnabled;
    private UpdateUserViewModel updateUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        etName = findViewById(R.id.etName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etUserName = findViewById(R.id.etUserName);
        etEmail = findViewById(R.id.etEmail);

        btnUpdateUser = findViewById(R.id.btnUpdateUser);

        init();
        setUpdateUserButtonOnClickListener();
    }

    private void setUpdateUserButtonOnClickListener() {
        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isClickEnabled) {
                    String message = updateUserViewModel.getErrorMessage();
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    return;
                }
                name = etName.getText().toString();
                email = etEmail.getText().toString();
                userName = etUserName.getText().toString();
                phoneNumber = etPhoneNumber.getText().toString();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("user_id", id);
                jsonObject.addProperty("name", name);
                jsonObject.addProperty("username", userName);
                jsonObject.addProperty("email", email);
                jsonObject.addProperty("contact", phoneNumber);

                updateUser(jsonObject);
            }
        });
    }

    private void updateUser(JsonObject jsonObject) {
        Call<MetaData> call = api.updateUser(jsonObject);
        call.enqueue(new Callback<MetaData>() {
            @Override
            public void onResponse(Call<MetaData> call, Response<MetaData> response) {
                Toast.makeText(getApplicationContext(),"User was updated successfully.",Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor  = preferences.edit();
                editor.putString("name", name);
                editor.putString("email", email);
                editor.putString("userName", userName);
                editor.putString("phoneNumber", phoneNumber);
                editor.apply();
                closeActivity();
            }

            @Override
            public void onFailure(Call<MetaData> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"User couldn't be updated.",Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void init() {
        isClickEnabled = true;
        initalizeViewModel();
        setOnTextChangeListeners();
        preferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        name = preferences.getString("name", null);
        etName.setText(name);
        email = preferences.getString("email", null);
        etEmail.setText(email);
        userName = preferences.getString("userName", null);
        etUserName.setText(userName);
        phoneNumber = preferences.getString("phoneNumber", null);
        etPhoneNumber.setText(phoneNumber);
        id = preferences.getInt("id", 0);
        api = RetrofitClientInstance.getRetrofitInstance().create(Api.class);
    }

    private void initalizeViewModel() {
        updateUserViewModel = ViewModelProviders.of(this).get(UpdateUserViewModel.class);
        updateUserViewModel.init();
        updateUserViewModel.enableClick().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    btnUpdateUser.setAlpha(1f);
                    isClickEnabled = true;
                } else {
                    btnUpdateUser.setAlpha(0.5f);
                    isClickEnabled = false;
                }
            }
        });
    }

    private void setOnTextChangeListeners() {
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateUserViewModel.checkNameValidity(s.toString());
            }
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateUserViewModel.checkEmailValidity(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateUserViewModel.checkPhoneNumberValidity(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateUserViewModel.checkUserNameValidity(s.toString());
            }
        });
    }

    private void closeActivity() {
        Intent intent = new Intent(UpdateUserActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
