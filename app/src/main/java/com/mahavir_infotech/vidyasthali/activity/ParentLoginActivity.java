package com.mahavir_infotech.vidyasthali.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.AppConfig;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.Utility.NetworkUtil;
import com.mahavir_infotech.vidyasthali.Utility.SavedData;
import com.mahavir_infotech.vidyasthali.Utility.UserAccount;
import com.mahavir_infotech.vidyasthali.activity.Teacher.OtpActivity;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentLoginActivity extends AppCompatActivity {
    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.mobile_number_et)
    EditText mobileNumberEt;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.forgot_password_tv)
    TextView forgotPasswordTv;
    private String IMEI_Number = "";
    private String Role = "";
    private static final String TODO = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);
        ButterKnife.bind(this);
        IMEI_Number = getIMEI(ParentLoginActivity.this);
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
            String android_id = Settings.Secure.getString(ParentLoginActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
            return android_id;
        } else {
            return telephonyManager.getDeviceId();
        }

    }

    @OnClick(R.id.login_btn)
    public void onClick() {

        if (UserAccount.isEmpty(mobileNumberEt)) {
            if (UserAccount.isPhoneNumberLength(mobileNumberEt)) {
                loginCall();

            } else {
                UserAccount.EditTextPointer.requestFocus();
                UserAccount.EditTextPointer.setError("Invalid Mobile Number !");
            }
        } else {
            UserAccount.EditTextPointer.requestFocus();
            UserAccount.EditTextPointer.setError("This field is not Empty !");
        }


    }

    private void loginCall() {
        if (NetworkUtil.isNetworkAvailable(ParentLoginActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ParentLoginActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().parentsLogin(mobileNumberEt.getText().toString().trim(), IMEI_Number,  "Android",SavedData.getTokan());
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
                                Bundle bundle=new Bundle();
                                bundle.putString("Contact",mobileNumberEt.getText().toString().trim());
                                bundle.putString("Check","Login");
                                ErrorMessage.I(ParentLoginActivity.this, OtpActivity.class, bundle);

                            } else {
                                ErrorMessage.T(ParentLoginActivity.this, jsonObject.getString("message"));
                                materialDialog.dismiss();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            ErrorMessage.E("Exception" + e.toString());
                        }


                    } else {
                        ErrorMessage.T(ParentLoginActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ParentLoginActivity.this, "Response Fail");
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(ParentLoginActivity.this, this.getString(R.string.no_internet));
        }
    }

}
