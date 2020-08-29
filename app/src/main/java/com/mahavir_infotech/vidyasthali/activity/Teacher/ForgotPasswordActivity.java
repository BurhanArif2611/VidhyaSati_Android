package com.mahavir_infotech.vidyasthali.activity.Teacher;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.AppConfig;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.Utility.NetworkUtil;
import com.mahavir_infotech.vidyasthali.Utility.UserAccount;
import com.mahavir_infotech.vidyasthali.activity.BaseActivity;
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

public class ForgotPasswordActivity extends BaseActivity {

    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.email_et)
    EditText emailEt;
    @BindView(R.id.login_btn)
    Button loginBtn;

    @Override
    protected int getContentResId() {
        return R.layout.activity_forgot_password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
    }

    @OnClick(R.id.login_btn)
    public void onClick() {
        if (UserAccount.isEmpty(emailEt)) {
            ForgotCall();
        } else {
            UserAccount.EditTextPointer.requestFocus();
            UserAccount.EditTextPointer.setError("This field is not Empty !");
        }
    }
    private void ForgotCall() {
        if (NetworkUtil.isNetworkAvailable(ForgotPasswordActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ForgotPasswordActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().forgetPassword(emailEt.getText().toString().trim(),  "teacher");
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
                               finish();
                            } else {
                                ErrorMessage.T(ForgotPasswordActivity.this, jsonObject.getString("message"));
                                materialDialog.dismiss();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            ErrorMessage.E("Exception" + e.toString());
                        }


                    } else {
                        ErrorMessage.T(ForgotPasswordActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ForgotPasswordActivity.this, "Response Fail");
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(ForgotPasswordActivity.this, this.getString(R.string.no_internet));
        }
    }
}
