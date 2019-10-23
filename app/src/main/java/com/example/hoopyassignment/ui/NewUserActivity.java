package com.example.hoopyassignment.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.hoopyassignment.R;
import com.example.hoopyassignment.models.CreateUserResponse;
import com.example.hoopyassignment.models.UserModel;
import com.example.hoopyassignment.rest.Api;
import com.example.hoopyassignment.rest.RetrofitClientInstance;
import com.example.hoopyassignment.viewmodels.NewUserViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewUserActivity extends AppCompatActivity {

    private String TAG;
    private MaterialButton btnUploadPhoto;
    private TextInputEditText etName;
    private TextInputEditText etEmail;
    private TextInputEditText etUserName;
    private TextInputEditText etPhoneNumber;
    private MaterialButton btnCreateUser;
    private int SELECT_PICTURE;
    private String selectedImagePath;
    private String image_url;
    private String name;
    private String email;
    private String userName;
    private String phoneNumber;
    private Api api;
    private SharedPreferences pref;
    private RelativeLayout rlProgressbar;
    private NewUserViewModel newUserViewModel;
    private boolean isClickEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        btnUploadPhoto = findViewById(R.id.btnUploadPhoto);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etUserName = findViewById(R.id.etUserName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnCreateUser = findViewById(R.id.btnCreateUser);

        rlProgressbar = findViewById(R.id.rlProgressBar);

        SELECT_PICTURE = 0;

        init();
    }

    private void init() {
        isClickEnabled = false;
        newUserViewModel = ViewModelProviders.of(this).get(NewUserViewModel.class);
        newUserViewModel.init();
        rlProgressbar.setVisibility(View.GONE);
        TAG = "NewUserActivity";
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        api = RetrofitClientInstance.getRetrofitInstance().create(Api.class);
        btnCreateUser.setAlpha(0.5f);
        setUploadImageButtonOnClickListener();
        setCreateUserButtonOnClickListener();
        addTextChangeListeners();
        newUserViewModel.enableClick().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    btnCreateUser.setAlpha(1f);
                    isClickEnabled = true;
                } else {
                    btnCreateUser.setAlpha(0.5f);
                    isClickEnabled = false;
                }
            }
        });
    }

    private void addTextChangeListeners() {
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                newUserViewModel.checkNameValidity(s.toString());
            }
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                newUserViewModel.checkEmailValidity(s.toString());
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
                newUserViewModel.checkPhoneNumberValidity(s.toString());
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
                newUserViewModel.checkUserNameValidity(s.toString());
            }
        });
    }

    private void setCreateUserButtonOnClickListener() {
        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isClickEnabled) {
                    String message = newUserViewModel.getErrorMessage();
                    Toast.makeText(NewUserActivity.this, message, Toast.LENGTH_SHORT).show();
                    return;
                }
                name = Objects.requireNonNull(etName.getText()).toString();
                email = Objects.requireNonNull(etEmail.getText()).toString();
                userName = Objects.requireNonNull(etUserName.getText()).toString();
                phoneNumber = Objects.requireNonNull(etPhoneNumber.getText()).toString();
                UserModel userModel = new UserModel(0, name, userName, email, phoneNumber
                , image_url);
                createNewUser(userModel);
            }
        });
    }

    private void createNewUser(UserModel userModel) {
        Call<CreateUserResponse> call = api.createNewUser(userModel);
        call.enqueue(new Callback<CreateUserResponse>() {
            @Override
            public void onResponse(Call<CreateUserResponse> call, Response<CreateUserResponse> response) {
                SharedPreferences.Editor editor  = pref.edit();
                UserModel model = response.body().getUserModel();
                editor.putString("name", model.getName());
                editor.putString("email", model.getEmail());
                editor.putString("userName", model.getUserName());
                editor.putString("phoneNumber", model.getPhoneNumber());
                editor.putString("image_url", model.getImage_url());
                editor.putInt("id", model.getUser_id());
                editor.apply();
                closeActivity();
            }

            @Override
            public void onFailure(Call<CreateUserResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setUploadImageButtonOnClickListener() {
        btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissionToReadExternalStorage()) {
                    startIntent();
                } else {
                    requestPermissionToReadExternalStorage();
                }
            }
        });
    }

    private void startIntent() {
        try {
            Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, SELECT_PICTURE);
        } catch(Exception e) {
            Log.i("NewUserActivity", "Error = " + e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && null != data) {
            rlProgressbar.setVisibility(View.VISIBLE);
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            selectedImagePath = cursor.getString(columnIndex);
            cursor.close();

            getImageUrl();
        }

    }

    private void getImageUrl() {
        File file = new File(selectedImagePath);
        final RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        Call<JsonObject> call = api.getImageUrl(body);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                rlProgressbar.setVisibility(View.GONE);
                try {
                    JsonArray array = response.body().getAsJsonArray("urls");
                    image_url = array.get(0).toString();
                    newUserViewModel.checkImageUrlValidity(image_url);
                } catch (JsonIOException e) {
                    Log.v(TAG, "Exception = " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                rlProgressbar.setVisibility(View.GONE);
                Log.v(TAG, "OnFailure = " + t.getMessage());
            }
        });
    }

    public boolean checkPermissionToReadExternalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = getApplicationContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionToReadExternalStorage() {
        try {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 3) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startIntent();
            }
        }
    }

    private void closeActivity() {
        Intent intent = new Intent(NewUserActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
