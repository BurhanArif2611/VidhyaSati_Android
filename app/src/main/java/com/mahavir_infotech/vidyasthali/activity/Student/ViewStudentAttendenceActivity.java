package com.mahavir_infotech.vidyasthali.activity.Student;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.google.gson.Gson;
import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.AppConfig;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.Utility.NetworkUtil;
import com.mahavir_infotech.vidyasthali.activity.BaseActivity;
import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;
import com.mahavir_infotech.vidyasthali.models.Attendence_Models.Example;
import com.mahavir_infotech.vidyasthali.models.DrawableUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewStudentAttendenceActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener{

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.tool_barLayout)
    RelativeLayout toolBarLayout;
    @BindView(R.id.calendarView)
    CalendarView calendarView;
    SimpleDateFormat mdformat, mdformat1;
    Calendar calander;
    Example example;
    @BindView(R.id.no_data_found_tv)
    TextView noDataFoundTv;
    List<EventDay> events_present = new ArrayList<>();
    List<EventDay> events_absent = new ArrayList<>();
    @BindView(R.id.attendence_img_btn)
    ImageButton attendenceImgBtn;

    @Override
    protected int getContentResId() {
        return R.layout.activity_view_student_attendence;
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
        StudentAttendence_List(mdformat.format(calander.getTime()));
        attendenceImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker();
            }
        });
    }

    private void StudentAttendence_List(String date) {
        if (NetworkUtil.isNetworkAvailable(ViewStudentAttendenceActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ViewStudentAttendenceActivity.this);
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


                                    for (int i = 0; i < example.getResult().getListStudent().size(); i++) {
                                        calander = Calendar.getInstance();
                                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                                        Date dt1 = format1.parse(example.getResult().getListStudent().get(i).getDate());
                                        DateFormat format_yaer = new SimpleDateFormat("yyyy");
                                        int year = Integer.parseInt(format_yaer.format(dt1));
                                        DateFormat format_month = new SimpleDateFormat("M");
                                        int month = Integer.parseInt(format_month.format(dt1));
                                        DateFormat format_day = new SimpleDateFormat("d");
                                        int day = Integer.parseInt(format_day.format(dt1));
                                        ErrorMessage.E("Date" + year + ">>" + month + ">>" + day);
                                        //calendar.set(2019, 0, 5);
                                        calander.set(year, month - 1, day);
                                        calendarView.setDate(calander);

                                        if (example.getResult().getListStudent().get(i).getIs_present().equals("1")) {
                                            events_present.add(new EventDay(calander, DrawableUtils.getCircleDrawableWithText(ViewStudentAttendenceActivity.this, "P")));
                                        } else if (example.getResult().getListStudent().get(i).getIs_present().equals("0")) {
                                            events_absent.add(new EventDay(calander, DrawableUtils.getCircleDrawableWithText_red(ViewStudentAttendenceActivity.this, "A")));
                                        }


                                    }
                                    ErrorMessage.E("events" + events_present.size() + ">>" + events_absent.size());
                                    calendarView.setEvents(events_present);
                                    calendarView.setEvents(events_absent);
                                    //  ErrorMessage.T(ViewStudentAttendenceActivity.this, jsonObject.getString("message"));

                                }
                            } else {
                                ErrorMessage.T(ViewStudentAttendenceActivity.this, jsonObject.getString("message"));
                                calendarView.setVisibility(View.GONE);
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
                        ErrorMessage.T(ViewStudentAttendenceActivity.this, "Response not successful");
                        materialDialog.dismiss();
                        noDataFoundTv.setVisibility(View.VISIBLE);
                        noDataFoundTv.setText("No Any Attendence List !");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ViewStudentAttendenceActivity.this, "Response Fail");
                    materialDialog.dismiss();
                    noDataFoundTv.setVisibility(View.VISIBLE);
                    noDataFoundTv.setText("No Any Attendence List !");
                }
            });

        } else {
            ErrorMessage.T(ViewStudentAttendenceActivity.this, this.getString(R.string.no_internet));
        }
    }
    private void datepicker() {
        Calendar calendar = Calendar.getInstance();
        //calendar.add(Calendar.DAY_OF_MONTH, 1);
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(ViewStudentAttendenceActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
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
        StudentAttendence_List(date);

    }
}
