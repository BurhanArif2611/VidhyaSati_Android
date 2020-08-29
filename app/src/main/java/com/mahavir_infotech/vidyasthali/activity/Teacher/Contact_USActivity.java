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

public class Contact_USActivity extends BaseActivity {

    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.user_name_etv)
    EditText userNameEtv;
    @BindView(R.id.email_id_etv)
    EditText emailIdEtv;
    @BindView(R.id.mobile_etv)
    EditText mobileEtv;
    @BindView(R.id.type_assignment_tv)
    EditText typeAssignmentTv;
    @BindView(R.id.contact_us_btn)
    Button contactUsBtn;

    @Override
    protected int getContentResId() {
        return R.layout.activity_contact__us;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        titleTxt.setText("Contact Us");
    }

    @OnClick(R.id.contact_us_btn)
    public void onClick() {
        if (UserAccount.isEmpty(userNameEtv, emailIdEtv,mobileEtv,typeAssignmentTv)) {
            if (UserAccount.isEmailValid(emailIdEtv)) {
                if (UserAccount.isPhoneNumberLength(mobileEtv)) {
                    Contact_US();
                }else {
                    UserAccount.EditTextPointer.setError("Mobile Number Invalid !");
                    UserAccount.EditTextPointer.requestFocus();
                }
            } else {
                UserAccount.EditTextPointer.setError("Email-ID Invalid !");
                UserAccount.EditTextPointer.requestFocus();
            }
        } else {
            UserAccount.EditTextPointer.setError("This Field Can't be Empty !");
            UserAccount.EditTextPointer.requestFocus();
        }
    }
    private void Contact_US() {
        if (NetworkUtil.isNetworkAvailable(Contact_USActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(Contact_USActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().contactus(userNameEtv.getText().toString(),emailIdEtv.getText().toString().trim(), mobileEtv.getText().toString(), typeAssignmentTv.getText().toString());
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
                                ErrorMessage.T(Contact_USActivity.this, jsonObject.getString("message"));
                                ErrorMessage.I_clear(Contact_USActivity.this, TeacherHomeActivity.class, null);
                            } else {
                                ErrorMessage.T(Contact_USActivity.this, jsonObject.getString("message"));
                                materialDialog.dismiss();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            ErrorMessage.E("Exception" + e.toString());
                        }


                    } else {
                        ErrorMessage.T(Contact_USActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(Contact_USActivity.this, "Response Fail");
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(Contact_USActivity.this, this.getString(R.string.no_internet));
        }
    }
}
