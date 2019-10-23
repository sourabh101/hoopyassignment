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
import com.example.hoopyassignment.models.FetchDataResponse;
import com.example.hoopyassignment.models.UserModel;
import com.example.hoopyassignment.rest.Api;
import com.example.hoopyassignment.rest.RetrofitClientInstance;
import com.example.hoopyassignment.viewmodels.FetchDataViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchDataActivity extends AppCompatActivity {

    private TextInputEditText etEmail;
    private TextInputEditText etUserName;
    private TextInputEditText etPhoneNumber;
    private MaterialButton btnFetchUserData;
    private Api api;
    private SharedPreferences preferences;
    private boolean isClickEnabled;
    private FetchDataViewModel fetchDataViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_data);

        etEmail = findViewById(R.id.etEmail);
        etUserName = findViewById(R.id.etUserName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);

        btnFetchUserData = findViewById(R.id.btnFetchData);

        init();
    }

    private void init() {
        isClickEnabled = false;
        btnFetchUserData.setAlpha(0.5f);
        initializeViewModel();
        addTextChangeListeners();
        preferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        api = RetrofitClientInstance.getRetrofitInstance().create(Api.class);
        setFetchDataButtonOnClickListener();
    }

    private void initializeViewModel() {
        fetchDataViewModel = ViewModelProviders.of(this).get(FetchDataViewModel.class);
        fetchDataViewModel.init();
        fetchDataViewModel.enableClick().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    btnFetchUserData.setAlpha(1f);
                    isClickEnabled = true;
                } else {
                    btnFetchUserData.setAlpha(0.5f);
                    isClickEnabled = false;
                }
            }
        });
    }

    private void addTextChangeListeners() {
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                fetchDataViewModel.checkEmailValidity(s.toString());
            }
        });

        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                fetchDataViewModel.checkPhoneNumberValidity(s.toString());
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
                fetchDataViewModel.checkUserNameValidity(s.toString());
            }
        });
    }

    private void setFetchDataButtonOnClickListener() {
        btnFetchUserData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isClickEnabled) {
                    String message = fetchDataViewModel.getErrorMessage();
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    return;
                }
                String email = Objects.requireNonNull(etEmail.getText()).toString();
                String phone = Objects.requireNonNull(etPhoneNumber.getText()).toString();
                String userName = Objects.requireNonNull(etUserName.getText()).toString();
                UserModel userModel = new UserModel(0, "", userName, email, phone, "");
                fetchData(userModel);
            }
        });
    }

    private void fetchData(UserModel userModel) {
        Call<FetchDataResponse> call = api.fetchUserData(userModel);
        call.enqueue(new Callback<FetchDataResponse>() {
            @Override
            public void onResponse(Call<FetchDataResponse> call, Response<FetchDataResponse> response) {
                if (response.body().getData() != null) {
                    Toast.makeText(getApplicationContext(),"Data was fetched from server",Toast.LENGTH_SHORT).show();
                    UserModel model = response.body().getData().get(0);
                    SharedPreferences.Editor editor  = preferences.edit();
                    editor.putString("name", model.getName());
                    editor.putString("email", model.getEmail());
                    editor.putString("userName", model.getUserName());
                    editor.putString("phoneNumber", model.getPhoneNumber());
                    editor.putString("image_url", model.getImage_url());
                    editor.putInt("id", model.getUser_id());
                    editor.apply();
                    closeActivity();
                } else if (response.body().getMetaData() != null) {
                    String message = response.body().getMetaData().getResponseText();
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Data couldn't be fetched",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchDataResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void closeActivity() {
        Intent intent = new Intent(FetchDataActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
