package com.mahavir_infotech.vidyasthali.activity;

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
import com.mahavir_infotech.vidyasthali.activity.Student.StudentHomeActivity;
import com.mahavir_infotech.vidyasthali.activity.Teacher.TeacherHomeActivity;
import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends BaseActivity {

    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.current_etv)
    EditText currentEtv;
    @BindView(R.id.new_password_etv)
    EditText newPasswordEtv;
    @BindView(R.id.confirm_password_etv)
    EditText confirmPasswordEtv;
    @BindView(R.id.signup_btn)
    Button signupBtn;
    private String Role = "";

    @Override
    protected int getContentResId() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        titleTxt.setText("Change Password");
        try {
            Role = UserProfileHelper.getInstance().getUserProfileModel().get(0).getRole();
        } catch (Exception e) {
        }

    }

    @OnClick(R.id.signup_btn)
    public void onClick() {
        if (UserAccount.isEmpty(currentEtv, newPasswordEtv, confirmPasswordEtv)) {
            if (UserAccount.isPasswordValid(currentEtv)) {
                if (UserAccount.isPasswordValid(newPasswordEtv)) {
                    if (UserAccount.isPasswordValid(confirmPasswordEtv)) {
                        if (newPasswordEtv.getText().toString().trim().equals(confirmPasswordEtv.getText().toString().trim())) {
                            if (Role.contains("teacher")) {
                                ChangePassword();
                            } else {
                                Student_ChangePassword();
                            }
                        } else {
                            ErrorMessage.T(ChangePasswordActivity.this, "Password Mismatch !");

                        }
                    } else {
                        UserAccount.EditTextPointer.requestFocus();
                        UserAccount.EditTextPointer.setError("Greater then 6  !");
                    }
                } else {
                    UserAccount.EditTextPointer.requestFocus();
                    UserAccount.EditTextPointer.setError("Greater then 6  !");
                }
            } else {
                UserAccount.EditTextPointer.requestFocus();
                UserAccount.EditTextPointer.setError("Please check Password  !");
            }
        } else {
            UserAccount.EditTextPointer.requestFocus();
            UserAccount.EditTextPointer.setError("This field is not Empty !");
        }
    }

    private void ChangePassword() {
        if (NetworkUtil.isNetworkAvailable(ChangePasswordActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ChangePasswordActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().teacherChangePassword(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id(), newPasswordEtv.getText().toString(), currentEtv.getText().toString());
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
                                ErrorMessage.T(ChangePasswordActivity.this, jsonObject.getString("message"));
                                ErrorMessage.I(ChangePasswordActivity.this, TeacherHomeActivity.class, null);

                            } else {
                                ErrorMessage.T(ChangePasswordActivity.this, jsonObject.getString("message"));
                                materialDialog.dismiss();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            ErrorMessage.E("Exception" + e.toString());
                        }


                    } else {
                        ErrorMessage.T(ChangePasswordActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ChangePasswordActivity.this, "Response Fail");
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(ChangePasswordActivity.this, this.getString(R.string.no_internet));
        }
    }
    private void Student_ChangePassword() {
        if (NetworkUtil.isNetworkAvailable(ChangePasswordActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ChangePasswordActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().studentChangePassword(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id(), newPasswordEtv.getText().toString(), currentEtv.getText().toString());
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
                                ErrorMessage.T(ChangePasswordActivity.this, jsonObject.getString("message"));
                                ErrorMessage.I(ChangePasswordActivity.this, StudentHomeActivity.class, null);
                            } else {
                                ErrorMessage.T(ChangePasswordActivity.this, jsonObject.getString("message"));
                                materialDialog.dismiss();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            ErrorMessage.E("Exception" + e.toString());
                        }


                    } else {
                        ErrorMessage.T(ChangePasswordActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ChangePasswordActivity.this, "Response Fail");
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(ChangePasswordActivity.this, this.getString(R.string.no_internet));
        }
    }
}
