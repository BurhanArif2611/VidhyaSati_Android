package com.mahavir_infotech.vidyasthali.Utility;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;

public class UserAccount {
    //for EditText Refrance
    public static EditText EditTextPointer;
    public static String errorMessage;
    private EditText userName, password;
    private Context mCont;

    public UserAccount(Context mCont, EditText un, EditText pw) {
        this.userName = un;
        this.password = pw;
        this.mCont = mCont;
        isLoginInit(userName, password);
    }

    private static void isLoginInit(EditText userName, EditText password) {
        int maxLength = 10;
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxLength);
        //this is for userName
        userName.setHint("Enter Email / Contact No");
        userName.setSingleLine(true);
        userName.setMaxLines(1);

        password.setHint("Enter Passwrod");
        password.setSingleLine(true);
        password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        password.setMaxLines(1);
        password.setFilters(fArray);

    }

    public static boolean isEmailValid(EditText tv) {
        if (TextUtils.isEmpty(tv.getText())) {
            EditTextPointer = tv;
            errorMessage = "This field can't be empty.!";
            return false;
        } else {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(tv.getText()).matches()) {
                return true;
            } else {
                EditTextPointer = tv;
                errorMessage = "Invalid Email Id";
                return false;
            }
        }
    }

    public static boolean isPasswordValid(EditText tv) {

        if (tv.getText().toString().length() >= 6) {
            return true;
        } else {
            EditTextPointer = tv;
            errorMessage = "Greater than 6 char";
            return false;
        }
    }
    public static boolean isPhoneNumberLength(EditText tv) {

        if (tv.getText().toString().length() == 10) {
            return true;
        } else {
            EditTextPointer = tv;
            errorMessage = "Enter 10 digits number";
            return false;
        }
    }
    public static boolean isPasswordLength(EditText tv) {
        //add your own logic
        if (tv.getText().toString().length() >= 6) {
            return true;
        } else {
            EditTextPointer = tv;
            errorMessage = "Enter 10 digits number";
            return false;
        }
    }
    public static boolean isPIN(EditText tv) {
        //add your own logic
        if (tv.getText().toString().length() == 16) {
            return true;
        } else {
            EditTextPointer = tv;
            errorMessage = "Enter 16 digits number";
            return false;
        }
    }

    public static final boolean isValidPhoneNumber(EditText tv) {
        if (tv.getText() == null || TextUtils.isEmpty(tv.getText())) {
            return false;
        } else {
            if (android.util.Patterns.PHONE.matcher(tv.getText()).matches()) {
                return true;
            } else {
                EditTextPointer = tv;
                errorMessage = "Invalid Mobile No.";
                return false;
            }
        }
    }

    public static boolean isEmpty(EditText... arg) {
        for (int i = 0; i < arg.length; i++) {
            if (arg[i].getText().length() <= 0) {
                EditTextPointer = arg[i];
                EditTextPointer.requestFocus();
                    return false;
            }

        }
        return true;
    }
    public static boolean isRollNumberLength(EditText tv) {
        //add your own logic
        if (tv.getText().toString().length() <= 6) {
            return true;
        } else {
            EditTextPointer = tv;
            errorMessage = "Enter 10 digits number";
            return false;
        }
    }

}