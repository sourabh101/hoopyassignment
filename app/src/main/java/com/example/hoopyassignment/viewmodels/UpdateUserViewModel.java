package com.example.hoopyassignment.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hoopyassignment.utils.MyUtilities;

public class UpdateUserViewModel extends ViewModel {
    private MutableLiveData<Boolean> mEnableClick = new MutableLiveData<>();
    private boolean isNameValid = true;
    private boolean isEmailValid = true;
    private boolean isUsernameValid = true;
    private boolean isPhoneNumberValid = true;

    public LiveData<Boolean> enableClick() {
        return mEnableClick;
    }

    public void init() {
        mEnableClick.setValue(true);
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

    private void updateEmableClick() {
        boolean boolOne = mEnableClick.getValue();
        boolean boolTwo = isNameValid && isPhoneNumberValid && isUsernameValid && isEmailValid;
        if (boolOne != boolTwo) {
            mEnableClick.setValue(boolTwo);
        }
    }

    public String getErrorMessage() {
        String message = "";
        if (!isNameValid) {
            message = "Name should be at least 3 characters";
        } else if (!isPhoneNumberValid) {
            message = "Phone Number is missing or invalid";
        } else if (!isUsernameValid) {
            message = "UserName should be at least 3 characters";
        } else if (!isEmailValid) {
            message = "Email is missing or invalid";
        }

        return message;
    }
}
