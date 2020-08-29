package com.mahavir_infotech.vidyasthali.activity.Teacher;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.AppConfig;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.Utility.NetworkUtil;
import com.mahavir_infotech.vidyasthali.activity.BaseActivity;
import com.mahavir_infotech.vidyasthali.adapter.Attendence_list_Adapter;
import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;
import com.mahavir_infotech.vidyasthali.models.Attendence_Models.Example;
import com.mahavir_infotech.vidyasthali.models.Attendence_Models.Selection_module;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendenceListActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.attendence_list)
    RecyclerView attendenceList;
    @BindView(R.id.total_tv)
    TextView totalTv;
    ArrayList<Selection_module> selection_modules = new ArrayList<>();
    ArrayList<String> parenset_list = new ArrayList<>();
    ArrayList<String> absent_list = new ArrayList<>();
    ArrayList<String> leave_list = new ArrayList<>();
    @BindView(R.id.all_present_checkbox)
    CheckBox allPresentCheckbox;
    @BindView(R.id.all_absent_checkbox)
    CheckBox allAbsentCheckbox;
    @BindView(R.id.status_tv)
    TextView statusTv;
    @BindView(R.id.total_student_tv)
    TextView totalStudentTv;
    @BindView(R.id.total_present_tv)
    TextView totalPresentTv;
    @BindView(R.id.total_absent_tv)
    TextView totalAbsentTv;
    @BindView(R.id.total_leave_tv)
    TextView totalLeaveTv;
    @BindView(R.id.submit_btn)
    Button submitBtn;
    @BindView(R.id.top_layout)
    LinearLayout topLayout;
    Example example;
    String Student_id = "", Present_status = "", Check = "";
    @BindView(R.id.bottom_layout)
    LinearLayout bottomLayout;
    @BindView(R.id.no_data_found_tv)
    TextView noDataFoundTv;
    SimpleDateFormat mdformat, mdformat1;
    Calendar calander;
    @BindView(R.id.attendence_img_btn)
    ImageButton attendenceImgBtn;
    private Parcelable recyclerViewState;
    String Already_submit = "";

    @Override
    protected int getContentResId() {
        return R.layout.activity_attendence_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        titleTxt.setText("Attendence");
        calander = Calendar.getInstance();
        mdformat = new SimpleDateFormat("yyyy-MM-dd");
        mdformat1 = new SimpleDateFormat("dd-MM-yyyy");
        selection_modules.clear();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Check = bundle.getString("Check");
            submitBtn.setVisibility(View.GONE);
            attendenceImgBtn.setVisibility(View.VISIBLE);
            StudentAttendence_List(mdformat.format(calander.getTime()));
        } else {
            titleTxt.setText("Attendence \n" + mdformat1.format(calander.getTime()));
            attendenceImgBtn.setVisibility(View.VISIBLE);
            Attendence_List();
        }
        attendenceImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker();
            }
        });

    }

    private void Attendence_List() {
        if (NetworkUtil.isNetworkAvailable(AttendenceListActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(AttendenceListActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().listStudentAttendance_get(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            Gson gson = new Gson();
                            materialDialog.dismiss();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("Attendence_List" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                if (jsonObject.getString("todays_present") != null && jsonObject.getString("todays_present").equals("1")) {
                                    titleTxt.setText("Attendence \n" + jsonObject.getString("today_date"));
                                    Already_submit = "yes";
                                    PreviousAttendence_List(jsonObject.getString("today_date"));
                                } else {
                                    example = gson.fromJson(jsonObject.toString(), Example.class);
                                    if (example.getResult().getListStudent().size() > 0) {
                                        totalTv.setText("Total :" + example.getResult().getListStudent().size());
                                        totalStudentTv.setText(example.getResult().getListStudent().size() + "\n" + "Total");
                                        Attendence_list_Adapter qtyListAdater = new Attendence_list_Adapter(AttendenceListActivity.this, example.getResult().getListStudent(), "", "");
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AttendenceListActivity.this);
                                        attendenceList.setLayoutManager(mLayoutManager);
                                        attendenceList.setItemAnimator(new DefaultItemAnimator());
                                        attendenceList.setAdapter(qtyListAdater);
                                        qtyListAdater.notifyDataSetChanged();
                                        attendenceList.setItemViewCacheSize(example.getResult().getListStudent().size());


                                        for (int i = 0; i < example.getResult().getListStudent().size(); i++) {
                                            Selected_rool(1, i);
                                        }
                                    } else {
                                        attendenceList.setVisibility(View.GONE);
                                        bottomLayout.setVisibility(View.GONE);
                                        noDataFoundTv.setVisibility(View.VISIBLE);
                                        if (UserProfileHelper.getInstance().getUserProfileModel().get(0).getRole().equals("teacher")) {
                                            noDataFoundTv.setText("Not Assaigned Any Class !");
                                        } else {
                                            noDataFoundTv.setText("No Any Attendence List !");
                                        }

                                    }
                                }

                            } else {
                                ErrorMessage.T(AttendenceListActivity.this, jsonObject.getString("message"));
                                materialDialog.dismiss();
                                attendenceList.setVisibility(View.GONE);
                                bottomLayout.setVisibility(View.GONE);
                                noDataFoundTv.setVisibility(View.VISIBLE);
                                if (UserProfileHelper.getInstance().getUserProfileModel().get(0).getRole().equals("teacher")) {
                                    noDataFoundTv.setText("Not Assaigned Any Class !");
                                } else {
                                    noDataFoundTv.setText("No Any Attendence List !");
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            ErrorMessage.E("Exception" + e.toString());
                            attendenceList.setVisibility(View.GONE);
                            bottomLayout.setVisibility(View.GONE);
                            noDataFoundTv.setVisibility(View.VISIBLE);
                            if (UserProfileHelper.getInstance().getUserProfileModel().get(0).getRole().equals("teacher")) {
                                noDataFoundTv.setText("Not Assaigned Any Class !");
                            } else {
                                noDataFoundTv.setText("No Any Attendence List !");
                            }
                        }


                    } else {
                        ErrorMessage.T(AttendenceListActivity.this, "Response not successful");
                        materialDialog.dismiss();
                        attendenceList.setVisibility(View.GONE);
                        bottomLayout.setVisibility(View.GONE);
                        noDataFoundTv.setVisibility(View.VISIBLE);
                        if (UserProfileHelper.getInstance().getUserProfileModel().get(0).getRole().equals("teacher")) {
                            noDataFoundTv.setText("Not Assaigned Any Class !");
                        } else {
                            noDataFoundTv.setText("No Any Attendence List !");
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(AttendenceListActivity.this, "Response Fail");
                    materialDialog.dismiss();
                    attendenceList.setVisibility(View.GONE);
                    bottomLayout.setVisibility(View.GONE);
                    noDataFoundTv.setVisibility(View.VISIBLE);
                    if (UserProfileHelper.getInstance().getUserProfileModel().get(0).getRole().equals("teacher")) {
                        noDataFoundTv.setText("Not Assaigned Any Class !");
                    } else {
                        noDataFoundTv.setText("No Any Attendence List !");
                    }
                }
            });

        } else {
            ErrorMessage.T(AttendenceListActivity.this, this.getString(R.string.no_internet));
        }
    }

    private void PreviousAttendence_List(String date) {
        if (NetworkUtil.isNetworkAvailable(AttendenceListActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(AttendenceListActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().viewStudentAttendance(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id(), date);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            Gson gson = new Gson();
                            materialDialog.dismiss();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("PreviousAttendence_List" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                example = gson.fromJson(jsonObject.toString(), Example.class);
                                if (example.getResult().getListStudent().size() > 0) {
                                    attendenceList.setVisibility(View.VISIBLE);
                                    bottomLayout.setVisibility(View.VISIBLE);
                                    if (!Already_submit.equals("")) {
                                        submitBtn.setVisibility(View.VISIBLE);
                                        submitBtn.setText("Update");
                                    } else {
                                        submitBtn.setVisibility(View.GONE);
                                    }
                                    noDataFoundTv.setVisibility(View.GONE);

                                    totalTv.setText("Total :" + example.getResult().getListStudent().size());
                                    totalStudentTv.setText(example.getResult().getListStudent().size() + "\n" + "Total");
                                    Attendence_list_Adapter qtyListAdater = new Attendence_list_Adapter(AttendenceListActivity.this, example.getResult().getListStudent(), "Teacher", Already_submit);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AttendenceListActivity.this);
                                    attendenceList.setLayoutManager(mLayoutManager);
                                    attendenceList.setItemAnimator(new DefaultItemAnimator());
                                    attendenceList.setAdapter(qtyListAdater);
                                    qtyListAdater.notifyDataSetChanged();
                                    attendenceList.setItemViewCacheSize(example.getResult().getListStudent().size());
                                    try {
                                        for (int i = 0; i < example.getResult().getListStudent().size(); i++) {
                                            Selected_rool(Integer.parseInt(example.getResult().getListStudent().get(i).getIs_present()), i);
                                        }
                                    } catch (Exception e) {
                                    }

                                }
                            } else {
                                ErrorMessage.T(AttendenceListActivity.this, jsonObject.getString("message"));
                                bottomLayout.setVisibility(View.GONE);
                                attendenceList.setVisibility(View.GONE);
                                noDataFoundTv.setVisibility(View.VISIBLE);
                                noDataFoundTv.setText("No Any Attendence List !");
                                materialDialog.dismiss();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            ErrorMessage.E("Exception" + e.toString());
                            attendenceList.setVisibility(View.GONE);
                            bottomLayout.setVisibility(View.GONE);
                            noDataFoundTv.setVisibility(View.VISIBLE);
                            if (UserProfileHelper.getInstance().getUserProfileModel().get(0).getRole().equals("teacher")) {
                                noDataFoundTv.setText("No Any Attendence List !");
                            } else {
                                noDataFoundTv.setText("No Any Attendence List !");
                            }
                        }


                    } else {
                        ErrorMessage.T(AttendenceListActivity.this, "Response not successful");
                        materialDialog.dismiss();
                        attendenceList.setVisibility(View.GONE);
                        bottomLayout.setVisibility(View.GONE);
                        noDataFoundTv.setVisibility(View.VISIBLE);
                        if (UserProfileHelper.getInstance().getUserProfileModel().get(0).getRole().equals("teacher")) {
                            noDataFoundTv.setText("No Any Attendence List !");
                        } else {
                            noDataFoundTv.setText("No Any Attendence List !");
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(AttendenceListActivity.this, "Response Fail");
                    materialDialog.dismiss();
                    attendenceList.setVisibility(View.GONE);
                    bottomLayout.setVisibility(View.GONE);
                    noDataFoundTv.setVisibility(View.VISIBLE);
                    if (UserProfileHelper.getInstance().getUserProfileModel().get(0).getRole().equals("teacher")) {
                        noDataFoundTv.setText("No Any Attendence List !");
                    } else {
                        noDataFoundTv.setText("No Any Attendence List !");
                    }
                }
            });

        } else {
            ErrorMessage.T(AttendenceListActivity.this, this.getString(R.string.no_internet));
        }
    }

    public void Selected_rool(int Select_item, int Position) {
        absent_list.clear();
        parenset_list.clear();
        leave_list.clear();
        if (selection_modules.size() > 0) {
            int I = -1, rool = -1;

            for (int i = 0; i < selection_modules.size(); i++) {
                //  ErrorMessage.E("in loop" + selection_modules.get(i).getPosition() + ">>" + Position);
                if (selection_modules.get(i).getPosition() == Position) {
                    I = i;
                    rool = selection_modules.get(i).getSelection_rool();
                }
            }
            if (I == -1) {
                Selection_module selection_module = new Selection_module();
                selection_module.setSelection_rool(Select_item);
                selection_module.setPosition(Position);
                selection_modules.add(selection_module);
            } else {
                Selection_module selection_module = new Selection_module();
                selection_module.setSelection_rool(Select_item);
                selection_module.setPosition(Position);
                selection_modules.set(I, selection_module);
                ErrorMessage.E("rool>>" + rool + "index>>" + I);
            }
            for (int i = 0; i < selection_modules.size(); i++) {
                if (selection_modules.get(i).getSelection_rool() == 0) {
                    absent_list.add("");
                } else if (selection_modules.get(i).getSelection_rool() == 1) {
                    parenset_list.add("");
                } else if (selection_modules.get(i).getSelection_rool() == 2) {
                    leave_list.add("");
                }
            }
            totalAbsentTv.setText(absent_list.size() + "\n" + "Absent");
            totalPresentTv.setText(parenset_list.size() + "\n" + "Present");
            totalLeaveTv.setText(leave_list.size() + "\n" + "Leave");
        } else {
            if (Select_item == 0) {
                absent_list.add("");
            } else if (Select_item == 1) {
                parenset_list.add("");
            } else if (Select_item == 2) {
                leave_list.add("");
            }
            Selection_module selection_module = new Selection_module();
            selection_module.setSelection_rool(Select_item);
            selection_module.setPosition(Position);
            selection_modules.add(selection_module);

            totalAbsentTv.setText(absent_list.size() + "\n" + "Absent");
            totalPresentTv.setText(parenset_list.size() + "\n" + "Present");
            totalLeaveTv.setText(leave_list.size() + "\n" + "Leave");
        }
    }


    @OnClick({R.id.all_present_checkbox, R.id.all_absent_checkbox, R.id.submit_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.all_present_checkbox:
                break;
            case R.id.all_absent_checkbox:
                break;
            case R.id.submit_btn:
                try {
                    if (example.getResult().getListStudent().size() == selection_modules.size()) {
                        if (example.getResult().getListStudent().size() > 0) {
                            for (int i = 0; i < example.getResult().getListStudent().size(); i++) {
                                if (i == 0) {
                                    Student_id = example.getResult().getListStudent().get(i).getStudentId();
                                    Present_status = String.valueOf(selection_modules.get(i).getSelection_rool());
                                } else {
                                    Student_id = Student_id + "," + example.getResult().getListStudent().get(i).getStudentId();
                                    Present_status = Present_status + "," + (String.valueOf(selection_modules.get(i).getSelection_rool()));
                                }
                            }
                            ErrorMessage.E("Student_id" + Student_id);
                            ErrorMessage.E("Present_status" + Present_status);
                            Submit_Attendence();
                        }
                    } else {
                        ErrorMessage.T(AttendenceListActivity.this, "Please Take All Student Attendence !");
                    }
                } catch (Exception e) {
                }
                break;
        }
    }

    private void Submit_Attendence() {
        if (NetworkUtil.isNetworkAvailable(AttendenceListActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(AttendenceListActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().addAttaindance(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id(), Student_id, Present_status, "");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            Gson gson = new Gson();
                            materialDialog.dismiss();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("Attendence_List" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                ErrorMessage.T(AttendenceListActivity.this, jsonObject.getString("message"));
                                finish();
                            } else {
                                ErrorMessage.T(AttendenceListActivity.this, jsonObject.getString("message"));
                                materialDialog.dismiss();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            ErrorMessage.E("Exception" + e.toString());
                        }


                    } else {
                        ErrorMessage.T(AttendenceListActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(AttendenceListActivity.this, "Response Fail");
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(AttendenceListActivity.this, this.getString(R.string.no_internet));
        }
    }

    private void StudentAttendence_List(String date) {
        if (NetworkUtil.isNetworkAvailable(AttendenceListActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(AttendenceListActivity.this);
            ErrorMessage.E("StudentAttendence_List" + date);
            Call<ResponseBody> call = AppConfig.getLoadInterface().viewStudentAttendanceByMonth(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id(), date);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            Gson gson = new Gson();
                            materialDialog.dismiss();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("StudentAttendence_List" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                example = gson.fromJson(jsonObject.toString(), Example.class);
                                if (example.getResult().getListStudent().size() > 0) {
                                    totalTv.setText("Total :" + example.getResult().getListStudent().size());
                                    totalStudentTv.setText(example.getResult().getListStudent().size() + "\n" + "Total");
                                    Attendence_list_Adapter qtyListAdater = new Attendence_list_Adapter(AttendenceListActivity.this, example.getResult().getListStudent(), "Student", "");
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AttendenceListActivity.this);
                                    attendenceList.setLayoutManager(mLayoutManager);
                                    attendenceList.setItemAnimator(new DefaultItemAnimator());
                                    attendenceList.setAdapter(qtyListAdater);
                                    qtyListAdater.notifyDataSetChanged();
                                    try {
                                        for (int i = 0; i < example.getResult().getListStudent().size(); i++) {
                                            Selected_rool(Integer.parseInt(example.getResult().getListStudent().get(i).getIs_present()), i);
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            } else {
                                ErrorMessage.T(AttendenceListActivity.this, jsonObject.getString("message"));
                                bottomLayout.setVisibility(View.GONE);
                                attendenceList.setVisibility(View.GONE);
                                noDataFoundTv.setVisibility(View.VISIBLE);
                                noDataFoundTv.setText("No Any Attendence List !");
                                materialDialog.dismiss();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            ErrorMessage.E("Exception" + e.toString());
                            noDataFoundTv.setVisibility(View.VISIBLE);
                            noDataFoundTv.setText("No Any Attendence List !");

                        }


                    } else {
                        ErrorMessage.T(AttendenceListActivity.this, "Response not successful");
                        materialDialog.dismiss();
                        noDataFoundTv.setVisibility(View.VISIBLE);
                        noDataFoundTv.setText("No Any Attendence List !");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(AttendenceListActivity.this, "Response Fail");
                    materialDialog.dismiss();
                    noDataFoundTv.setVisibility(View.VISIBLE);
                    noDataFoundTv.setText("No Any Attendence List !");
                }
            });

        } else {
            ErrorMessage.T(AttendenceListActivity.this, this.getString(R.string.no_internet));
        }
    }

    private void datepicker() {
        Calendar calendar = Calendar.getInstance();
        //calendar.add(Calendar.DAY_OF_MONTH, 1);
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(AttendenceListActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        datePickerDialog.setMaxDate(calendar);

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
        String date = "";
        if (monthOfYear + 1 < 10 && dayOfMonth > 10)
            //submissionTv.setText((dayOfMonth + "-0" + (monthOfYear + 1) + "-" + eventYear));
            date = (eventYear + "-0" + (monthOfYear + 1) + "-" + dayOfMonth);
        else if (dayOfMonth < 10 && monthOfYear + 1 >= 10)
            //submissionTv.setText((dayOfMonth + "-" + (monthOfYear + 1) + "-" + eventYear));
            date = (eventYear + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
        else if (monthOfYear + 1 < 10 && dayOfMonth < 10)
            // submissionTv.setText((dayOfMonth + "-0" + (monthOfYear + 1) + "-" + eventYear));
            date = (eventYear + "-0" + (monthOfYear + 1) + "-" + dayOfMonth);
        else
            // submissionTv.setText((dayOfMonth + "-" + (monthOfYear + 1) + "-" + eventYear));
            date = (eventYear + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
        try {
            Date d = mdformat.parse(date);
            titleTxt.setText("Attendence \n" + mdformat1.format(d));
        } catch (Exception ex) {

        }

        Already_submit = "";
        if (!Check.equals("")) {
            StudentAttendence_List(date);
        } else {
            PreviousAttendence_List(date);
        }
    }
}
