package com.example.hoopyassignment.utils;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyUtilities {

    public static boolean checkEmailValidity(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"; ;
        Pattern pat = Pattern.compile(regex);
        if (email == null || email.isEmpty())
            return false;
        return pat.matcher(email).matches();
    }

    public static boolean checkNameValidity(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        } else {
            if (name.length() >= 3) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkUserNameValidity(String userName) {
        if (userName == null || userName.isEmpty()) {
            return false;
        } else {
            if (userName.length() >= 3) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkPhoneNumberValidity(String number) {
        number = number.replace("+", "");
        String regex = "(0|91)?[1-9][0-9]{9}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

}
