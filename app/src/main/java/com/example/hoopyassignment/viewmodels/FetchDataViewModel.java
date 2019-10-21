package com.example.hoopyassignment.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hoopyassignment.utils.MyUtilities;

public class FetchDataViewModel extends ViewModel {
    private MutableLiveData<Boolean> mEnableClick = new MutableLiveData<>();
    private boolean isEmailValid = false;
    private boolean isUsernameValid = false;
    private boolean isPhoneNumberValid = false;

    public LiveData<Boolean> enableClick() {
        return mEnableClick;
    }

    public void init() {
        mEnableClick.setValue(false);
    }

    public void checkEmailValidity(String email) {
        isEmailValid = MyUtilities.checkEmailValidity(email);
        updateEnableClick();
    }

    public void checkUserNameValidity(String userName) {
        isUsernameValid = MyUtilities.checkUserNameValidity(userName);
        updateEnableClick();
    }

    public void checkPhoneNumberValidity(String phoneNumber) {
        isPhoneNumberValid = MyUtilities.checkPhoneNumberValidity(phoneNumber);
        updateEnableClick();
    }

    private void updateEnableClick() {
        boolean boolOne = mEnableClick.getValue();
        boolean boolTwo = isPhoneNumberValid || isUsernameValid || isEmailValid;
        if (boolOne != boolTwo) {
            mEnableClick.setValue(boolTwo);
        }
    }

    public String getErrorMessage() {
        String message = "Entered value is not valid.";
        return message;
    }
}
