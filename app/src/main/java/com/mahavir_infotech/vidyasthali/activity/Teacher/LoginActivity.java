package com.mahavir_infotech.vidyasthali.activity.Teacher;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.AppConfig;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.Utility.NetworkUtil;
import com.mahavir_infotech.vidyasthali.Utility.SavedData;
import com.mahavir_infotech.vidyasthali.Utility.UserAccount;
import com.mahavir_infotech.vidyasthali.activity.Student.StudentHomeActivity;
import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;
import com.mahavir_infotech.vidyasthali.database.UserProfileModel;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TODO = "";
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.email_et)
    EditText emailEt;
    @BindView(R.id.password_et)
    EditText passwordEt;
    @BindView(R.id.forgot_password_tv)
    TextView forgotPasswordTv;
    @BindView(R.id.roll_number_et)
    EditText rollNumberEt;
    @BindView(R.id.roll_number_textinput)
    TextInputLayout rollNumberTextinput;
    @BindView(R.id.email_textinput)
    TextInputLayout emailTextinput;
    private String IMEI_Number = "";
    private String Role = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        IMEI_Number = getIMEI(LoginActivity.this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Role = bundle.getString("Role");
            if (Role.equals("teacher")) {
                emailTextinput.setVisibility(View.VISIBLE);
                rollNumberTextinput.setVisibility(View.GONE);
            } else {
                rollNumberTextinput.setVisibility(View.VISIBLE);
                emailTextinput.setVisibility(View.GONE);
            }
        }
        forgotPasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ErrorMessage.I(LoginActivity.this, ForgotPasswordActivity.class, null);
            }
        });
    }

    @OnClick(R.id.login_btn)
    public void onClick() {
        if (Role.equals("teacher")) {
            if (UserAccount.isEmpty(emailEt, passwordEt)) {
                if (UserAccount.isEmailValid(emailEt)) {
                    if (UserAccount.isPasswordLength(passwordEt)) {
                        loginCall();
                    } else {
                        UserAccount.EditTextPointer.requestFocus();
                        UserAccount.EditTextPointer.setError("Invalid Password  !");
                    }
                } else {
                    UserAccount.EditTextPointer.requestFocus();
                    UserAccount.EditTextPointer.setError("Please check Email ID  !");
                }
            } else {
                UserAccount.EditTextPointer.requestFocus();
                UserAccount.EditTextPointer.setError("This field is not Empty !");
            }
        }else {
            if (UserAccount.isEmpty(rollNumberEt, passwordEt)) {
                if (UserAccount.isRollNumberLength(rollNumberEt)) {
                    if (UserAccount.isPasswordLength(passwordEt)) {
                        if (!IMEI_Number.equals("")) {
                                Student_loginCall();
                        } else {
                                IMEI_Number = getIMEI(LoginActivity.this);
                        }

                    } else {
                        UserAccount.EditTextPointer.requestFocus();
                        UserAccount.EditTextPointer.setError("Invalid Password  !");
                    }
                } else {
                    UserAccount.EditTextPointer.requestFocus();
                    UserAccount.EditTextPointer.setError("Invalid Roll Number  !");
                }
            } else {
                UserAccount.EditTextPointer.requestFocus();
                UserAccount.EditTextPointer.setError("This field is not Empty !");
            }
        }
    }

    private void loginCall() {
        if (NetworkUtil.isNetworkAvailable(LoginActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(LoginActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().userLogin(emailEt.getText().toString().trim(), passwordEt.getText().toString(), "teacher", SavedData.getTokan(),"Android");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            materialDialog.dismiss();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("Login Response" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                                UserProfileModel userProfileModel = new UserProfileModel();
                                userProfileModel.setDisplayName(jsonObject1.getString("name"));
                                userProfileModel.setUser_id(jsonObject1.getString("user_id"));
                                userProfileModel.setAuthToken(jsonObject1.getString("authToken"));
                                userProfileModel.setEmaiiId(jsonObject1.getString("email"));
                                userProfileModel.setRole(jsonObject1.getString("role"));
                                userProfileModel.setUserPhone("123");
                                userProfileModel.setProfile_pic(jsonObject1.getString("profile_image"));
                                ErrorMessage.E("Exception" + userProfileModel.getDisplayName());
                                UserProfileHelper.getInstance().insertUserProfileModel(userProfileModel);
                                ErrorMessage.I(LoginActivity.this, TeacherHomeActivity.class, null);
                            } else {
                                ErrorMessage.T(LoginActivity.this, jsonObject.getString("message"));
                                materialDialog.dismiss();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            ErrorMessage.E("Exception" + e.toString());
                        }


                    } else {
                        ErrorMessage.T(LoginActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(LoginActivity.this, "Response Fail");
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(LoginActivity.this, this.getString(R.string.no_internet));
        }
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
            String android_id = Settings.Secure.getString(LoginActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
            return android_id;
        } else {
            return telephonyManager.getDeviceId();
        }

    }

    private void Student_loginCall() {
        if (NetworkUtil.isNetworkAvailable(LoginActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(LoginActivity.this);
            ErrorMessage.E("Student_loginCall"+rollNumberEt.getText().toString().trim()+">>"+ passwordEt.getText().toString()+">>"+ IMEI_Number);
            Call<ResponseBody> call = AppConfig.getLoadInterface().student_userLogin(rollNumberEt.getText().toString().trim(), passwordEt.getText().toString(), IMEI_Number, SavedData.getTokan(),"Android");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            materialDialog.dismiss();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("Login Response" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                                UserProfileModel userProfileModel = new UserProfileModel();
                                userProfileModel.setDisplayName(jsonObject1.getString("name"));
                                userProfileModel.setUser_id(jsonObject1.getString("user_id"));
                                userProfileModel.setAuthToken(jsonObject1.getString("authToken"));
                                userProfileModel.setEmaiiId(jsonObject1.getString("email"));
                                userProfileModel.setRole(jsonObject1.getString("role"));
                                userProfileModel.setUserPhone(jsonObject1.getString("class_id"));
                                userProfileModel.setProfile_pic(jsonObject1.getString("profile_image"));
                                userProfileModel.setClass_name(jsonObject1.getString("class_name"));
                                ErrorMessage.E("Exception" + userProfileModel.getDisplayName());
                                UserProfileHelper.getInstance().insertUserProfileModel(userProfileModel);
                                ErrorMessage.I(LoginActivity.this, StudentHomeActivity.class, null);
                            } else {
                                ErrorMessage.T(LoginActivity.this, jsonObject.getString("message"));
                                materialDialog.dismiss();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            ErrorMessage.E("Exception" + e.toString());
                        }


                    } else {
                        ErrorMessage.T(LoginActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(LoginActivity.this, "Response Fail");
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(LoginActivity.this, this.getString(R.string.no_internet));
        }
    }
}
