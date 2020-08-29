package com.mahavir_infotech.vidyasthali.activity.Student;

import android.app.Dialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.AppConfig;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.Utility.NetworkUtil;
import com.mahavir_infotech.vidyasthali.Utility.UserAccount;
import com.mahavir_infotech.vidyasthali.activity.BaseActivity;
import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;
import com.mahavir_infotech.vidyasthali.models.LeaveList_Models.ListLeaf;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ADD_LeaveActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.from_date_etv)
    EditText fromDateEtv;
    @BindView(R.id.to_date_etv)
    EditText toDateEtv;
    @BindView(R.id.type_assignment_tv)
    EditText typeAssignmentTv;
    @BindView(R.id.apply_btn)
    Button applyBtn;
    String Check = "",Come_from="";
    ListLeaf listLeaf;
    @Override
    protected int getContentResId() {
        return R.layout.activity_add__leave;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);

        Bundle bundle=getIntent().getExtras();
        if (bundle !=null){
            Come_from=bundle.getString("Check");
            listLeaf=(ListLeaf)bundle.getSerializable("AllData");
            typeAssignmentTv.setText(listLeaf.getReason());
            toDateEtv.setText(listLeaf.getToDate());
            fromDateEtv.setText(listLeaf.getFromDate());
            titleTxt.setText("Update Leave");
            applyBtn.setText("Update");
        }else {
            titleTxt.setText("Apply Leave");
        }
    }


    @OnClick({R.id.from_date_etv, R.id.to_date_etv, R.id.apply_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.from_date_etv:
                Check = "from";
                datepicker();
                break;
            case R.id.to_date_etv:
                Check = "to";
                datepicker();
                break;
            case R.id.apply_btn:
                if (UserAccount.isEmpty(fromDateEtv,toDateEtv,typeAssignmentTv)) {
                    if (Come_from.equals("")){
                    Submit_ADD_Leave();
                    }
                    else {
                        Submit_Update_Leave();
                    }
                } else {
                    UserAccount.EditTextPointer.requestFocus();
                    UserAccount.EditTextPointer.setError("This field is not Empty !");
                }
                break;
        }
    }

    private void datepicker() {
        Calendar calendar = Calendar.getInstance();
        //calendar.add(Calendar.DAY_OF_MONTH, 1);
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(ADD_LeaveActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);

        if (Check.equals("to")) {
            try {
                String dateStr = fromDateEtv.getText().toString();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = format.parse(dateStr);

                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                datePickerDialog.setMinDate(calendar);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Check.equals("from")) {
            try {
                Date d = new Date();
                CharSequence s = DateFormat.format("yyyy-MM-dd", d.getTime());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = format.parse(s.toString());
                calendar.setTime(date);
                //calendar.add(Calendar.DAY_OF_MONTH, 1);
                datePickerDialog.setMinDate(calendar);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        datePickerDialog.show(getFragmentManager(), "Date Picker");

    }


    @Override
    public void onDateSet(DatePickerDialog view, int eventYear, int monthOfYear, int dayOfMonth) {
        if (Check.equals("from")) {
            if (monthOfYear + 1 < 10 && dayOfMonth > 10)
                fromDateEtv.setText((eventYear + "-0" + (monthOfYear + 1) + "-" + dayOfMonth));
            else if (dayOfMonth < 10 && monthOfYear + 1 >= 10)
                fromDateEtv.setText((eventYear + "-" + (monthOfYear + 1) + "-" + dayOfMonth));
            else if (monthOfYear + 1 < 10 && dayOfMonth < 10)
                fromDateEtv.setText((eventYear + "-0" + (monthOfYear + 1) + "-" + dayOfMonth));
            else fromDateEtv.setText((eventYear + "-" + (monthOfYear + 1) + "-" + dayOfMonth));
        }else if (Check.equals("to")) {
            if (monthOfYear + 1 < 10 && dayOfMonth > 10)
                toDateEtv.setText((eventYear + "-0" + (monthOfYear + 1) + "-" + dayOfMonth));
            else if (dayOfMonth < 10 && monthOfYear + 1 >= 10)
                toDateEtv.setText((eventYear + "-" + (monthOfYear + 1) + "-" + dayOfMonth));
            else if (monthOfYear + 1 < 10 && dayOfMonth < 10)
                toDateEtv.setText((eventYear + "-0" + (monthOfYear + 1) + "-" + dayOfMonth));
            else toDateEtv.setText((eventYear + "-" + (monthOfYear + 1) + "-" + dayOfMonth));
        }
    }
    private void Submit_ADD_Leave() {
        if (NetworkUtil.isNetworkAvailable(ADD_LeaveActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ADD_LeaveActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().studentAddLeave(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id(),fromDateEtv.getText().toString(),toDateEtv.getText().toString(),typeAssignmentTv.getText().toString());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonobject = null;
                        Gson gson = new Gson();
                        try {
                            materialDialog.dismiss();
                            jsonobject = new JSONObject(response.body().string());
                            ErrorMessage.E("Submit_ADD_Leave" + jsonobject.toString());
                            if (jsonobject.getString("status").equals("1")) {
                                ErrorMessage.T(ADD_LeaveActivity.this, jsonobject.getString("message"));
                                finish();
                            } else {
                                ErrorMessage.T(ADD_LeaveActivity.this, jsonobject.getString("message"));
                                materialDialog.dismiss();

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            //ErrorMessage.T(ADD_LeaveActivity.this, "Server Error");
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(ADD_LeaveActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ADD_LeaveActivity.this, "Response Fail");
                    System.out.println("============update profile fail  :" + t.toString());
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(ADD_LeaveActivity.this, this.getString(R.string.no_internet));
        }


    }
    private void Submit_Update_Leave() {
        if (NetworkUtil.isNetworkAvailable(ADD_LeaveActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ADD_LeaveActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().studentUpdateLeave(listLeaf.getLeaveId(),fromDateEtv.getText().toString(),toDateEtv.getText().toString(),typeAssignmentTv.getText().toString());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonobject = null;
                        Gson gson = new Gson();
                        try {
                            materialDialog.dismiss();
                            jsonobject = new JSONObject(response.body().string());
                            ErrorMessage.E("Submit_ADD_Leave" + jsonobject.toString());
                            if (jsonobject.getString("status").equals("1")) {
                                ErrorMessage.T(ADD_LeaveActivity.this, jsonobject.getString("message"));
                                finish();
                            } else {
                                ErrorMessage.T(ADD_LeaveActivity.this, jsonobject.getString("message"));
                                materialDialog.dismiss();

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            //ErrorMessage.T(ADD_LeaveActivity.this, "Server Error");
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(ADD_LeaveActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ADD_LeaveActivity.this, "Response Fail");
                    System.out.println("============update profile fail  :" + t.toString());
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(ADD_LeaveActivity.this, this.getString(R.string.no_internet));
        }


    }
}
