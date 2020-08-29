package com.mahavir_infotech.vidyasthali.activity.Teacher;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.AppConfig;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.Utility.LoadInterface;
import com.mahavir_infotech.vidyasthali.Utility.NetworkUtil;
import com.mahavir_infotech.vidyasthali.Utility.SavedData;
import com.mahavir_infotech.vidyasthali.Utility.UserAccount;
import com.mahavir_infotech.vidyasthali.activity.BaseActivity;
import com.mahavir_infotech.vidyasthali.activity.Student.StudentHomeActivity;
import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;
import com.mahavir_infotech.vidyasthali.database.UserProfileModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends BaseActivity implements TextWatcher {

    private static final String TODO = "";
    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.continue_tv)
    TextView continueTv;
    @BindView(R.id.editTextone)
    EditText editTextone;
    @BindView(R.id.editTexttwo)
    EditText editTexttwo;
    @BindView(R.id.editTextthree)
    EditText editTextthree;
    @BindView(R.id.editTextfour)
    EditText editTextfour;
    @BindView(R.id.editTextfifth)
    EditText editTextfifth;
    @BindView(R.id.editTextsixth)
    EditText editTextsixth;
    @BindView(R.id.enter_title_tv)
    TextView enter_title_tv;
    @BindView(R.id.dont_recive_otp_tv)
    TextView dontReciveOtpTv;
    @BindView(R.id.timer_tv)
    TextView timerTv;
    private String mVerificationId = "";
    /*private FirebaseAuth mAuth;*/
    String Check = "";
    Bundle bundle;
    private String Contact = "";
    private CountDownTimer yourCountDownTimer;
    private String IMEI_Number = "";

    @Override
    protected int getContentResId() {
        return R.layout.activity_otp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolbarWithBackButton("");
        titleTxt.setText("Verification code");
        IMEI_Number = getIMEI(OtpActivity.this);
        bundle = getIntent().getExtras();
        if (bundle != null) {
            Check = bundle.getString("Check");
            Contact = bundle.getString("Contact");
            enter_title_tv.setText("Enter the OTP sent to " + Contact);
            if (bundle.getString("Check").equals("Login")) {
                // sendVerificationCode(bundle.getString("Contact"));
                timer();

               /* try {
                    JSONObject object1 = new JSONObject(AllResponse);
                    JSONObject jsonObject1 = object1.getJSONObject("result");
                    User_id = (jsonObject1.getString("user_id"));

                } catch (Exception e) {
                }*/
            } else {
                //sendVerificationCode(bundle.getString("Contact"));
            }
        }
        editTextone.addTextChangedListener(this);
        editTexttwo.addTextChangedListener(this);
        editTextthree.addTextChangedListener(this);
        editTextfour.addTextChangedListener(this);
        editTextfifth.addTextChangedListener(this);
        editTextsixth.addTextChangedListener(this);

        dontReciveOtpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginOnServer(Contact);
            }
        });
    }

    @OnClick(R.id.continue_tv)
    public void onClick() {
        if (UserAccount.isEmpty(editTextone, editTexttwo, editTextthree, editTextfour)) {
            //verifyVerificationCode(editTextone.getText().toString() + editTexttwo.getText().toString() + editTextthree.getText().toString() + editTextfour.getText().toString() + editTextfifth.getText().toString() + editTextsixth.getText().toString());
            try {
                if (!IMEI_Number.equals("")) {
                    MATCHoTPOnServer(editTextone.getText().toString() + editTexttwo.getText().toString() + editTextthree.getText().toString() + editTextfour.getText().toString());
                } else {
                    IMEI_Number = getIMEI(OtpActivity.this);
                }
            } catch (Exception e) {

            }
        } else {
            UserAccount.EditTextPointer.setError("This Field Can't be Empty !");
            UserAccount.EditTextPointer.requestFocus();
        }
    }


   /* private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + mobile, 60, TimeUnit.SECONDS, OtpActivity.this, mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            try {
                if (code != null) {
                    String[] part = code.split("");
                    editTextone.setText(part[1]);
                    editTexttwo.setText(part[2]);
                    editTextthree.setText(part[3]);
                    editTextfour.setText(part[4]);
                    editTextfifth.setText(part[5]);
                    editTextsixth.setText(part[6]);
                    //verifying the code
                    verifyVerificationCode(code);
                }
            } catch (Exception e) {
                try {
                    String[] part = code.split("");
                    editTextone.setText(part[0]);
                    editTexttwo.setText(part[1]);
                    editTextthree.setText(part[2]);
                    editTextfour.setText(part[3]);
                    editTextfifth.setText(part[4]);
                    editTextsixth.setText(part[5]);
                    //verifying the code
                    verifyVerificationCode(code);
                }catch (Exception e1){}
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OtpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("FirebaseException", "" + e.getMessage());

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(OtpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //verification successful we will start the profile activity
                    if (Check.equals("Login")) {
                        try {
                            JSONObject object = new JSONObject(AllResponse);
                            JSONObject jsonObject1 = object.getJSONObject("user_details");
                            UserProfileModel userProfileModel = new UserProfileModel();
                            userProfileModel.setId(jsonObject1.getString("user_id"));
                            userProfileModel.setDisplayName(jsonObject1.getString("first_name"));
                            userProfileModel.setLastname(jsonObject1.getString("last_name"));
                            userProfileModel.setEmaiiId(jsonObject1.getString("email"));
                            userProfileModel.setProfile_pic(jsonObject1.getString("profile_image"));
                            userProfileModel.setUserPhone(jsonObject1.getString("phone"));
                            UserProfileHelper.getInstance().insertUserProfileModel(userProfileModel);
                            ErrorMessage.I(OtpActivity.this, ThankyouActivity.class, null);

                            //ErrorMessage.I(OtpActivity.this, DashboardActivity.class, null);
                        } catch (Exception e) {
                        }
                    }else if (Check.equals("Account")){
                        Intent returnIntent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("Contact",Contact);
                        returnIntent.putExtras(bundle);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                } else {
                    //verification unsuccessful.. display an error message
                    String message = "Somthing is wrong, we will fix it soon...";
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        message = "Invalid code entered...";
                    }
                    //Toast.makeText(OtpActivity.this, message, Toast.LENGTH_LONG).show();
                    Log.e("task", "" + message);
                           *//*
    }*/

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.length() == 1) {
            if (editTextone.length() == 1) {
                editTexttwo.requestFocus();
            }
            if (editTexttwo.length() == 1) {
                editTextthree.requestFocus();
            }
            if (editTextthree.length() == 1) {
                editTextfour.requestFocus();
            }
            if (editTextfour.length() == 1) {
                editTextfifth.requestFocus();
            }
            if (editTextfifth.length() == 1) {
                editTextsixth.requestFocus();
            }
        } else if (editable.length() == 0) {
            if (editTextsixth.length() == 0) {
                editTextfifth.requestFocus();
            }
            if (editTextfifth.length() == 0) {
                editTextfour.requestFocus();
            }
            if (editTextfour.length() == 0) {
                editTextthree.requestFocus();
            }
            if (editTextthree.length() == 0) {
                editTexttwo.requestFocus();
            }
            if (editTexttwo.length() == 0) {
                editTextone.requestFocus();
            }
        }

    }

    private void MATCHoTPOnServer(String Otp) {
        try {
            if (NetworkUtil.isNetworkAvailable(OtpActivity.this)) {
                final Dialog materialDialog = ErrorMessage.initProgressDialog(OtpActivity.this);
                LoadInterface apiService = AppConfig.getClient().create(LoadInterface.class);/*SavedData.getFCM_ID()*/
                Call<ResponseBody> call = apiService.verifyOtp(Contact, Otp.trim(), IMEI_Number, "Android", SavedData.getTokan());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        ErrorMessage.E("Response" + response.code());
                        if (response.isSuccessful()) {
                            JSONObject object = null;
                            try {
                                materialDialog.dismiss();
                                object = new JSONObject(response.body().string());
                                ErrorMessage.E("comes in cond" + object.toString());
                                if (object.getString("status").equals("1")) {
                                    ErrorMessage.T(OtpActivity.this, object.getString("message"));
                                    try {
                                        JSONObject jsonObject1 = object.getJSONObject("result");
                                        UserProfileModel userProfileModel = new UserProfileModel();
                                        userProfileModel.setDisplayName(jsonObject1.getString("name"));
                                        userProfileModel.setUser_id(jsonObject1.getString("user_id"));
                                        userProfileModel.setAuthToken(jsonObject1.getString("authToken"));
                                        userProfileModel.setEmaiiId(jsonObject1.getString("email"));
                                        userProfileModel.setRole(jsonObject1.getString("role"));
                                        userProfileModel.setUserPhone(jsonObject1.getString("class_id"));
                                        userProfileModel.setProfile_pic("123");
                                        ErrorMessage.E("Exception" + userProfileModel.getDisplayName());
                                        UserProfileHelper.getInstance().delete();
                                        UserProfileHelper.getInstance().insertUserProfileModel(userProfileModel);
                                        ErrorMessage.I(OtpActivity.this, StudentHomeActivity.class, null);
                                        try {
                                            if ((yourCountDownTimer == null)) {
                                            } else {
                                                yourCountDownTimer.cancel();
                                            }
                                        } catch (Exception e) {
                                        }

                                    } catch (Exception e) {
                                    }

                                } else {
                                    ErrorMessage.E("comes in else");
                                    ErrorMessage.T(OtpActivity.this, object.getString("message"));
                                    materialDialog.dismiss();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                materialDialog.dismiss();
                                ErrorMessage.E("Exceptions" + e.toString());
                            }
                        } else {
                            materialDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                        ErrorMessage.E("Falure login" + t);
                        materialDialog.dismiss();
                    }
                });
            } else {
                ErrorMessage.T(OtpActivity.this, "No Internet");
            }
        } catch (Exception e) {
        }


    }

    private void LoginOnServer(String Phone) {
        if (NetworkUtil.isNetworkAvailable(OtpActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(OtpActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().parentsLogin(Contact, IMEI_Number, "Android", SavedData.getTokan());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    ErrorMessage.E("Response" + response.code());
                    if (response.isSuccessful()) {
                        JSONObject object = null;
                        try {
                            materialDialog.dismiss();
                            object = new JSONObject(response.body().string());
                            ErrorMessage.E("comes in cond" + object.toString());
                            if (object.getString("status").equals("200")) {

                            } else {
                                ErrorMessage.E("comes in else");
                                ErrorMessage.T(OtpActivity.this, object.getString("message"));
                                materialDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                            ErrorMessage.E("JsonException" + e.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                            ErrorMessage.E("Exceptions" + e.toString());
                        }
                    } else {
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    ErrorMessage.E("Falure login" + t);
                    materialDialog.dismiss();
                }
            });
        } else {
            ErrorMessage.T(OtpActivity.this, "No Internet");
        }
    }

    private void timer() {
        yourCountDownTimer = new CountDownTimer(60000 * 2, 1000) { // adjust the milli seconds here
            public void onTick(long millisUntilFinished) {
                timerTv.setVisibility(View.VISIBLE);
                dontReciveOtpTv.setVisibility(View.GONE);
                timerTv.setText("" + String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                timerTv.setText("00:00");
                dontReciveOtpTv.setVisibility(View.VISIBLE);
                timerTv.setVisibility(View.GONE);
                //   dialog.show();
               /* onBackPressed();
                finish();
        */
            }
        }.start();
    }

    public String getIMEI(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return TODO;
            }
        }
        if (Build.VERSION.SDK_INT >= 26) {
            String android_id = Settings.Secure.getString(OtpActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
            return android_id;
        } else {
            return telephonyManager.getDeviceId();
        }

    }
}
