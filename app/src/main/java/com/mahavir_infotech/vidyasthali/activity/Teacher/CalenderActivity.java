package com.mahavir_infotech.vidyasthali.activity.Teacher;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.gson.Gson;
import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.AppConfig;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.Utility.NetworkUtil;
import com.mahavir_infotech.vidyasthali.activity.BaseActivity;
import com.mahavir_infotech.vidyasthali.adapter.GetClass_Adapter;
import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;
import com.mahavir_infotech.vidyasthali.models.GetClass.Example;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalenderActivity extends BaseActivity  {

    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.calendarView)
    CalendarView calendarView;
    String formateDate="";
    Example example;
    Dialog dialog;
    @Override
    protected int getContentResId() {
        return R.layout.activity_calender;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        titleTxt.setText("Home Work");
        Get_Class();



        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                ErrorMessage.E("clickedDayCalendar 0"+clickedDayCalendar.getTime().toString());
               try {
                   SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
                   Date date = formatter.parse(clickedDayCalendar.getTime().toString());
                   formateDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                   ErrorMessage.T(CalenderActivity.this,formateDate);
                   GetClass_PopUP();
               }catch (Exception e){}

            }
        });
    }
    public void GetClass_PopUP() {
        dialog = new Dialog(CalenderActivity.this);
        dialog.setContentView(R.layout.get_class_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final Button submit_btn = (Button) dialog.findViewById(R.id.submit_btn);
        final Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        final RecyclerView class_rcv = (RecyclerView) dialog.findViewById(R.id.class_rcv);
        if (example.getResult().getListClasses().size() > 0) {
            GetClass_Adapter qtyListAdater = new GetClass_Adapter(CalenderActivity.this, example.getResult().getListClasses(),formateDate);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CalenderActivity.this);
            class_rcv.setLayoutManager(mLayoutManager);
            class_rcv.setItemAnimator(new DefaultItemAnimator());
            class_rcv.setAdapter(qtyListAdater);
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
    public void Get_Class() {
        if (NetworkUtil.isNetworkAvailable(CalenderActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(CalenderActivity.this);
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
                                 example = gson.fromJson(responseData, Example.class);
                            } else {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("productdetail error" + e.toString());
                            //  ErrorMessage.T(CalenderActivity.this, "Server Error");
                            materialDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(CalenderActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(CalenderActivity.this, "Response Fail");
                    materialDialog.dismiss();
                }
            });

        } else {
          ErrorMessage.T(CalenderActivity.this,getResources().getString(R.string.no_internet));
        }
    }

    public void Select_Class(String classId, String className) {
        dialog.dismiss();
        Bundle bundle=new Bundle();
        bundle.putString("Class_id",classId);
        bundle.putString("Class_Name",className);
        bundle.putString("Check","");
        bundle.putString("Selected_Date",formateDate);
        ErrorMessage.I(CalenderActivity.this, ADD_HomeWorkActivity.class,bundle);
    }
}
