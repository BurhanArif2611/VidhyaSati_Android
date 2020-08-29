package com.mahavir_infotech.vidyasthali.activity.Teacher.Live_Class;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.AppConfig;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.Utility.NetworkUtil;
import com.mahavir_infotech.vidyasthali.Utility.UserAccount;
import com.mahavir_infotech.vidyasthali.activity.BaseActivity;
import com.mahavir_infotech.vidyasthali.adapter.GetClass_Adapter;
import com.mahavir_infotech.vidyasthali.adapter.GetSubject_Adapter;
import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;
import com.mahavir_infotech.vidyasthali.models.GetSubject.Example;
import com.mahavir_infotech.vidyasthali.models.ListLiveClass_Models.ListSession;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ADD_LiveClassActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.select_class_tv)
    EditText selectClassTv;
    @BindView(R.id.select_subject_tv)
    EditText selectSubjectTv;
    @BindView(R.id.title_tv)
    EditText titleTv;
    @BindView(R.id.url_tv)
    EditText urlTv;
    @BindView(R.id.date_tv)
    EditText dateTv;
    @BindView(R.id.time_tv)
    EditText timeTv;
    @BindView(R.id.type_assignment_tv)
    EditText typeAssignmentTv;
    @BindView(R.id.submit_btn)
    Button submitBtn;
    com.mahavir_infotech.vidyasthali.models.GetClass.Example example1;
    @BindView(R.id.end_time_tv)
    EditText endTimeTv;
    private Dialog dialog;
    private String Class_Id = "";
    Example example;
    private String Subject_id = "", Subject_name = "";
    private int Hour = 0, Minute = 0;
    private int mHour, mMin;
    private String Select_Time = "";
    private String Check = "", Session_id = "";
    ListSession listSession;
    private String EndTime = "";
    String select_start_time="",select_end_time="";

    @Override
    protected int getContentResId() {
        return R.layout.activity_add__live_class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Check = bundle.getString("Check");
            listSession = (ListSession) bundle.getSerializable("ALL_Data");
            selectClassTv.setText(listSession.getClassName());
            selectSubjectTv.setText(listSession.getSubjectName());
            urlTv.setText(listSession.getLiveUrl());
            titleTv.setText(listSession.getTitle());
            dateTv.setText(listSession.getSessionDate());
            timeTv.setText(listSession.getSessionTime());
            endTimeTv.setText(listSession.getSession_end_time());
            typeAssignmentTv.setText(listSession.getDescription());
            Session_id = listSession.getSessionId();
            titleTxt.setText("Update Live Session");
            submitBtn.setText("Update");
        } else {
            titleTxt.setText("New Live Session");
            Get_Class();
        }
    }

    @OnClick({R.id.select_class_tv, R.id.select_subject_tv, R.id.date_tv, R.id.time_tv, R.id.submit_btn, R.id.end_time_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_class_tv:
                if (Check.equals("")) {
                    GetClass_PopUP();
                }
                break;
            case R.id.select_subject_tv:
                if (Check.equals("")) {
                    GetSubject_PopUP();
                }
                break;
            case R.id.date_tv:
                datepicker();
                break;
            case R.id.time_tv:
                EndTime = "Start";
                show_Timepicker();
                break;
            case R.id.end_time_tv:
                EndTime = "End";
                show_Timepicker();
                break;
            case R.id.submit_btn:
                if (UserAccount.isEmpty(selectClassTv, selectSubjectTv, urlTv, titleTv, dateTv, timeTv, typeAssignmentTv)) {
                    if (Check.equals("")) {
                        Submit_LiveClass();
                    } else {
                        Update_LiveClass();
                    }
                } else {
                    UserAccount.EditTextPointer.requestFocus();
                    UserAccount.EditTextPointer.setError("This field is not Empty !");
                }


                break;
        }
    }

    public void Get_Class() {
        if (NetworkUtil.isNetworkAvailable(ADD_LiveClassActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ADD_LiveClassActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().getClasses(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            materialDialog.dismiss();
                            Gson gson = new Gson();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("Get_Class" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                String responseData = jsonObject.toString();
                                example1 = gson.fromJson(responseData, com.mahavir_infotech.vidyasthali.models.GetClass.Example.class);
                            } else {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("productdetail error" + e.toString());
                            //  ErrorMessage.T(ADD_LiveClassActivity.this, "Server Error");
                            materialDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(ADD_LiveClassActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ADD_LiveClassActivity.this, "Response Fail");
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(ADD_LiveClassActivity.this, getResources().getString(R.string.no_internet));
        }
    }

    public void Get_Subject(String class_id) {
        if (NetworkUtil.isNetworkAvailable(ADD_LiveClassActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ADD_LiveClassActivity.this);
            ErrorMessage.E("Get_Subject" + class_id);
            Call<ResponseBody> call = AppConfig.getLoadInterface().getSubject(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id(), class_id);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            materialDialog.dismiss();
                            Gson gson = new Gson();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("Get_Class" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                String responseData = jsonObject.toString();
                                example = gson.fromJson(responseData, Example.class);
                            } else {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("productdetail error" + e.toString());
                            //  ErrorMessage.T(ADD_LiveClassActivity.this, "Server Error");
                            materialDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(ADD_LiveClassActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ADD_LiveClassActivity.this, "Response Fail");
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(ADD_LiveClassActivity.this, getResources().getString(R.string.no_internet));
        }
    }

    public void GetClass_PopUP() {
        dialog = new Dialog(ADD_LiveClassActivity.this);
        dialog.setContentView(R.layout.get_class_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final Button submit_btn = (Button) dialog.findViewById(R.id.submit_btn);
        final Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        final RecyclerView class_rcv = (RecyclerView) dialog.findViewById(R.id.class_rcv);
        try {
            if (example1.getResult().getListClasses().size() > 0) {
                GetClass_Adapter qtyListAdater = new GetClass_Adapter(ADD_LiveClassActivity.this, example1.getResult().getListClasses(), "LiveClass");
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ADD_LiveClassActivity.this);
                class_rcv.setLayoutManager(mLayoutManager);
                class_rcv.setItemAnimator(new DefaultItemAnimator());
                class_rcv.setAdapter(qtyListAdater);
            }
        } catch (Exception e) {
        }
        submit_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    public void getSubject_id(String subjectName, String subjectId) {
        Subject_id = subjectId;
        Subject_name = subjectName;
        selectSubjectTv.setText(subjectName);
        dialog.dismiss();
    }

    private void datepicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(ADD_LiveClassActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        datePickerDialog.setMinDate(calendar);
       /* try {
            String dateStr = homeDateTv.getText().toString();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DAY_OF_MONTH, 1);
            datePickerDialog.setMinDate(cal);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        datePickerDialog.show(getFragmentManager(), "Date Picker");
    }


    @Override
    public void onDateSet(DatePickerDialog view, int eventYear, int monthOfYear, int dayOfMonth) {
        if (monthOfYear + 1 < 10 && dayOfMonth > 10)
            dateTv.setText((dayOfMonth + "-0" + (monthOfYear + 1) + "-" + eventYear));
        else if (dayOfMonth < 10 && monthOfYear + 1 >= 10)
            dateTv.setText((dayOfMonth + "-" + (monthOfYear + 1) + "-" + eventYear));
        else if (monthOfYear + 1 < 10 && dayOfMonth < 10)
            dateTv.setText((dayOfMonth + "-0" + (monthOfYear + 1) + "-" + eventYear));
        else dateTv.setText((dayOfMonth + "-" + (monthOfYear + 1) + "-" + eventYear));

    }

    public void Select_Class(String classId, String className) {
        dialog.dismiss();
        Class_Id = classId;
        selectClassTv.setText(className);
        Get_Subject(classId);
    }

    public void GetSubject_PopUP() {
        dialog = new Dialog(ADD_LiveClassActivity.this);
        dialog.setContentView(R.layout.get_class_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final TextView title_tv = (TextView) dialog.findViewById(R.id.title_tv);
        final Button submit_btn = (Button) dialog.findViewById(R.id.submit_btn);
        final Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        final RecyclerView class_rcv = (RecyclerView) dialog.findViewById(R.id.class_rcv);
        title_tv.setText("Select Subject");
        try {
            if (example.getResult().getListSubjects().size() > 0) {
                GetSubject_Adapter qtyListAdater = new GetSubject_Adapter(ADD_LiveClassActivity.this, example.getResult().getListSubjects(), "LiveClass");
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ADD_LiveClassActivity.this);
                class_rcv.setLayoutManager(mLayoutManager);
                class_rcv.setItemAnimator(new DefaultItemAnimator());
                class_rcv.setAdapter(qtyListAdater);
            }
        } catch (Exception e) {
        }

        submit_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    public void show_Timepicker() {
        Calendar c = Calendar.getInstance();
        Hour = c.get(Calendar.HOUR_OF_DAY);
        Minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(ADD_LiveClassActivity.this, Hour, Minute, false);
        timePickerDialog.setThemeDark(false);
        timePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimary));
        timePickerDialog.setOnTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                mHour = hourOfDay;
                mMin = minute;
                ErrorMessage.E("show_Timepicker"+hourOfDay+">>"+minute);
                String AM_PM;
                if (hourOfDay < 12) {
                    AM_PM = "AM";
                } else {
                    AM_PM = "PM";
                    mHour = mHour - 12;
                }
                if (mHour < 10 && mMin > 10) {
                    Select_Time = "0" + mHour + ":" + mMin;
                } else if (mHour < 10 && mMin < 10) {
                    Select_Time = "0" + mHour + ":0" + mMin;
                } else {
                    Select_Time = mHour + ":" + mMin;
                }

                if (EndTime.equals("Start")) {
                    select_start_time=hourOfDay+":"+minute;
                    timeTv.setText(Select_Time + " " + AM_PM);
                }else if (EndTime.equals("End")) {
                    select_end_time=hourOfDay+":"+minute;
                    endTimeTv.setText(Select_Time + " " + AM_PM);
                }

            }
        });
        timePickerDialog.show(getFragmentManager(), "TimePickerDialog");
    }

    private void Submit_LiveClass() {
        if (NetworkUtil.isNetworkAvailable(ADD_LiveClassActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ADD_LiveClassActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().addLiveSession(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id(), Class_Id, titleTv.getText().toString(), Subject_id, urlTv.getText().toString(), dateTv.getText().toString(), select_start_time, typeAssignmentTv.getText().toString(),select_end_time);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonobject = null;
                        Gson gson = new Gson();
                        try {
                            materialDialog.dismiss();
                            jsonobject = new JSONObject(response.body().string());
                            ErrorMessage.E("response" + jsonobject.toString());
                            if (jsonobject.getString("status").equals("1")) {
                                ErrorMessage.T(ADD_LiveClassActivity.this, jsonobject.getString("message"));
                                finish();
                            } else {
                                ErrorMessage.T(ADD_LiveClassActivity.this, jsonobject.getString("message"));
                                materialDialog.dismiss();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            //ErrorMessage.T(ADD_LiveClassActivity.this, "Server Error");
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(ADD_LiveClassActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ADD_LiveClassActivity.this, "Response Fail");
                    System.out.println("============update profile fail  :" + t.toString());
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(ADD_LiveClassActivity.this, this.getString(R.string.no_internet));
        }
    }

    private void Update_LiveClass() {
        if (NetworkUtil.isNetworkAvailable(ADD_LiveClassActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ADD_LiveClassActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().updateLiveSession(Session_id, titleTv.getText().toString(), urlTv.getText().toString(), dateTv.getText().toString(), timeTv.getText().toString(), typeAssignmentTv.getText().toString(),endTimeTv.getText().toString());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonobject = null;
                        Gson gson = new Gson();
                        try {
                            materialDialog.dismiss();
                            jsonobject = new JSONObject(response.body().string());
                            ErrorMessage.E("response" + jsonobject.toString());
                            if (jsonobject.getString("status").equals("1")) {
                                ErrorMessage.T(ADD_LiveClassActivity.this, jsonobject.getString("message"));
                                finish();
                            } else {
                                ErrorMessage.T(ADD_LiveClassActivity.this, jsonobject.getString("message"));
                                materialDialog.dismiss();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            //ErrorMessage.T(ADD_LiveClassActivity.this, "Server Error");
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(ADD_LiveClassActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ADD_LiveClassActivity.this, "Response Fail");
                    System.out.println("============update profile fail  :" + t.toString());
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(ADD_LiveClassActivity.this, this.getString(R.string.no_internet));
        }
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
    }
}
