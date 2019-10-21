package com.example.hoopyassignment.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hoopyassignment.utils.MyUtilities;

public class NewUserViewModel extends ViewModel {
    private MutableLiveData<Boolean> mEnableClick = new MutableLiveData<>();
    private boolean isNameValid = false;
    private boolean isEmailValid = false;
    private boolean isUsernameValid = false;
    private boolean isPhoneNumberValid = false;
    private boolean isImageUrlValid = false;

    public LiveData<Boolean> enableClick() {
        return mEnableClick;
    }

    public void init() {
        mEnableClick.setValue(false);
    }

    public void checkNameValidity(String name) {
        isNameValid = MyUtilities.checkNameValidity(name);
        updateEmableClick();
    }

    public void checkEmailValidity(String email) {
        isEmailValid = MyUtilities.checkEmailValidity(email);
        updateEmableClick();
    }

    public void checkUserNameValidity(String userName) {
        isUsernameValid = MyUtilities.checkUserNameValidity(userName);
        updateEmableClick();
    }

    public void checkPhoneNumberValidity(String phoneNumber) {
        isPhoneNumberValid = MyUtilities.checkPhoneNumberValidity(phoneNumber);
        updateEmableClick();
    }

    public void checkImageUrlValidity(String url) {
        if (url != null && !url.isEmpty()) {
            isImageUrlValid = true;
        } else {
            isImageUrlValid = false;
        }
        updateEmableClick();
    }

    private void updateEmableClick() {
        boolean boolOne = mEnableClick.getValue();
        boolean boolTwo = isNameValid && isPhoneNumberValid && isImageUrlValid && isUsernameValid && isEmailValid;
        if (boolOne != boolTwo) {
            mEnableClick.setValue(boolTwo);
        }
    }

    public String getErrorMessage() {
        String message = "";
        if (!isNameValid) {
            message = "Name should be at least 3 characters";
        } else if (!isPhoneNumberValid) {
            message = "Phone Number is invalid";
        } else if (!isImageUrlValid) {
            message = "Image is not uploaded";
        } else if (!isUsernameValid) {
            message = "UserName should be at least 3 characters";
        } else if (!isEmailValid) {
            message = "Email is invalid";
        }

        return message;
    }

}
