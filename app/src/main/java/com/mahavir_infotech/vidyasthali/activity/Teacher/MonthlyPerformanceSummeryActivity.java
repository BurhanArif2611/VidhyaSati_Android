package com.mahavir_infotech.vidyasthali.activity.Teacher;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.eftimoff.viewpagertransformers.ZoomOutSlideTransformer;
import com.google.gson.Gson;
import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.AppConfig;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.Utility.NetworkUtil;
import com.mahavir_infotech.vidyasthali.activity.BaseActivity;
import com.mahavir_infotech.vidyasthali.adapter.MonthlyPerformance_Adapter;
import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;
import com.mahavir_infotech.vidyasthali.models.Grade_Submit;
import com.mahavir_infotech.vidyasthali.models.Monthly_Performance.Example;
import com.mahavir_infotech.vidyasthali.models.Monthly_Performance.Example1;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonthlyPerformanceSummeryActivity extends BaseActivity {

    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.monthly_performance_pager)
    ViewPager monthlyPerformancePager;
    @BindView(R.id.no_data_tv)
    TextView noDataTv;
    @BindView(R.id.previous_tv)
    TextView previousTv;
    @BindView(R.id.next_tv)
    TextView nextTv;
    @BindView(R.id.total_count_tv)
    TextView totalCountTv;
    @BindView(R.id.bottom_layout)
    LinearLayout bottomLayout;
    private int position = 0;
    Example example;
    ArrayList<Grade_Submit> grade_submits = new ArrayList<>();

    @Override
    protected int getContentResId() {
        return R.layout.activity_monthly_performance_summery;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        grade_submits.clear();
        titleTxt.setText("Monthly Performance");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Get_SingleStudentData();
        } else {
            Get_StudentList();
        }

        monthlyPerformancePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                position = i + 1;
                totalCountTv.setText("" + position + " to " + example.getResult().getListStudent().size());
                ErrorMessage.E("selected onPageSelected" + position);
                if (i == 0) {
                    previousTv.setVisibility(View.GONE);
                } else {
                    previousTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        previousTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    position = position - 1;
                  //  monthlyPerformancePager.setCurrentItem(position,true);
                    monthlyPerformancePager.arrowScroll(View.FOCUS_LEFT);
                } catch (Exception e) {
                }
            }
        });
        nextTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (example.getResult().getListStudent().size() >= position) {
                        monthlyPerformancePager.arrowScroll(View.FOCUS_RIGHT);
                        position = position + 1;
                       // monthlyPerformancePager.setCurrentItem(position,true);
                    }
                } catch (Exception e) {
                }
            }
        });

    }

    public void Get_StudentList() {
        if (NetworkUtil.isNetworkAvailable(MonthlyPerformanceSummeryActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(MonthlyPerformanceSummeryActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().listStudentPerformance(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            materialDialog.dismiss();
                            Gson gson = new Gson();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("Get_StudentList" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                String responseData = jsonObject.toString();
                                example = gson.fromJson(responseData, Example.class);
                                if (example.getResult().getListStudent().size() > 0) {
                                    //  Collections.reverse(example.getResult().getListTimetable());
                                    noDataTv.setVisibility(View.GONE);
                                    monthlyPerformancePager.setVisibility(View.VISIBLE);
                                    bottomLayout.setVisibility(View.VISIBLE);
                                  /*  GetListTable_Adapter qtyListAdater = new GetListTable_Adapter(MonthlyPerformanceSummeryActivity.this, example.getResult().getListTimetable());
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MonthlyPerformanceSummeryActivity.this);
                                    timeTableRcv.setLayoutManager(mLayoutManager);
                                    timeTableRcv.setItemAnimator(new DefaultItemAnimator());
                                    timeTableRcv.setAdapter(qtyListAdater);*/
                                    MonthlyPerformance_Adapter riskAnalysisAdapter = new MonthlyPerformance_Adapter(MonthlyPerformanceSummeryActivity.this, example.getResult().getListStudent(), "");
                                    monthlyPerformancePager.setAdapter(riskAnalysisAdapter);
                                    monthlyPerformancePager.setOffscreenPageLimit(example.getResult().getListStudent().size());
                                    //pager.setPageTransformer(false, new FadePageTransformer());ZoomOutSlideTransformer
                                   // monthlyPerformancePager.setPageTransformer(true, new ZoomOutSlideTransformer());
                                } else {
                                    noDataTv.setVisibility(View.VISIBLE);
                                    monthlyPerformancePager.setVisibility(View.GONE);
                                    bottomLayout.setVisibility(View.GONE);
                                }
                            } else {
                                noDataTv.setVisibility(View.VISIBLE);
                                monthlyPerformancePager.setVisibility(View.GONE);
                                bottomLayout.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                            noDataTv.setVisibility(View.VISIBLE);
                            monthlyPerformancePager.setVisibility(View.GONE);
                            bottomLayout.setVisibility(View.GONE);
                        }


                    } else {
                        ErrorMessage.T(MonthlyPerformanceSummeryActivity.this, "Response not successful");
                        materialDialog.dismiss();
                        noDataTv.setVisibility(View.VISIBLE);
                        monthlyPerformancePager.setVisibility(View.GONE);
                        bottomLayout.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(MonthlyPerformanceSummeryActivity.this, "Response Fail");
                    materialDialog.dismiss();
                    noDataTv.setVisibility(View.VISIBLE);
                    monthlyPerformancePager.setVisibility(View.GONE);
                    bottomLayout.setVisibility(View.GONE);
                }
            });

        } else {
            ErrorMessage.T(MonthlyPerformanceSummeryActivity.this, getResources().getString(R.string.no_internet));
            noDataTv.setVisibility(View.VISIBLE);
            monthlyPerformancePager.setVisibility(View.GONE);
            bottomLayout.setVisibility(View.GONE);
        }
    }

    public void Get_SingleStudentData() {
        if (NetworkUtil.isNetworkAvailable(MonthlyPerformanceSummeryActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(MonthlyPerformanceSummeryActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().getStudentPerformance(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            materialDialog.dismiss();
                            Gson gson = new Gson();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("Get_SingleStudentData" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                String responseData = jsonObject.toString();
                                Example1 example = gson.fromJson(responseData, Example1.class);
                                ErrorMessage.E("Get_SingleStudentData" + example.getListStudent().size());
                                if (example.getListStudent().size() > 0) {
                                    //  Collections.reverse(example.getResult().getListTimetable());
                                    noDataTv.setVisibility(View.GONE);
                                    monthlyPerformancePager.setVisibility(View.VISIBLE);
                                    bottomLayout.setVisibility(View.GONE);
                                  /*  GetListTable_Adapter qtyListAdater = new GetListTable_Adapter(MonthlyPerformanceSummeryActivity.this, example.getResult().getListTimetable());
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MonthlyPerformanceSummeryActivity.this);
                                    timeTableRcv.setLayoutManager(mLayoutManager);
                                    timeTableRcv.setItemAnimator(new DefaultItemAnimator());
                                    timeTableRcv.setAdapter(qtyListAdater);*/
                                    MonthlyPerformance_Adapter riskAnalysisAdapter = new MonthlyPerformance_Adapter(MonthlyPerformanceSummeryActivity.this, example.getListStudent(), "Student");
                                    monthlyPerformancePager.setAdapter(riskAnalysisAdapter);
                                    monthlyPerformancePager.setOffscreenPageLimit(example.getListStudent().size());
                                    //pager.setPageTransformer(false, new FadePageTransformer());ZoomOutSlideTransformer
                                  //  monthlyPerformancePager.setPageTransformer(true, new ZoomOutSlideTransformer());
                                } else {
                                    noDataTv.setVisibility(View.VISIBLE);
                                    monthlyPerformancePager.setVisibility(View.GONE);
                                    bottomLayout.setVisibility(View.GONE);
                                }
                            } else {
                                noDataTv.setVisibility(View.VISIBLE);
                                monthlyPerformancePager.setVisibility(View.GONE);
                                bottomLayout.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                            noDataTv.setVisibility(View.VISIBLE);
                            monthlyPerformancePager.setVisibility(View.GONE);
                            bottomLayout.setVisibility(View.GONE);
                        }


                    } else {
                        ErrorMessage.T(MonthlyPerformanceSummeryActivity.this, "Response not successful");
                        materialDialog.dismiss();
                        noDataTv.setVisibility(View.VISIBLE);
                        monthlyPerformancePager.setVisibility(View.GONE);
                        bottomLayout.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(MonthlyPerformanceSummeryActivity.this, "Response Fail");
                    materialDialog.dismiss();
                    noDataTv.setVisibility(View.VISIBLE);
                    monthlyPerformancePager.setVisibility(View.GONE);
                    bottomLayout.setVisibility(View.GONE);
                }
            });

        } else {
            ErrorMessage.T(MonthlyPerformanceSummeryActivity.this, getResources().getString(R.string.no_internet));
            noDataTv.setVisibility(View.VISIBLE);
            monthlyPerformancePager.setVisibility(View.GONE);
            bottomLayout.setVisibility(View.GONE);
        }
    }

    public void getData(String Position, String remark, String grade_id) {
        if (grade_submits.size() > 0) {
            int I = -1, rool = -1;

            for (int i = 0; i < grade_submits.size(); i++) {
                if (grade_submits.get(i).getRoll_number() == Position) {
                    if (grade_submits.get(i).getGrade_id() == grade_id) {
                        I = i;

                    }
                }
            }
            if (I == -1) {
                Grade_Submit grade_submit = new Grade_Submit();
                grade_submit.setRoll_number(Position);
                grade_submit.setRemarks(remark);
                grade_submit.setGrade_id(grade_id);
                grade_submits.add(grade_submit);
            } else {
                Grade_Submit grade_submit = new Grade_Submit();
                grade_submit.setRoll_number(Position);
                grade_submit.setRemarks(remark);
                grade_submit.setGrade_id(grade_id);
                grade_submits.set(I, grade_submit);
                ErrorMessage.E("rool>>" + rool + "index>>" + I);
            }
        } else {
            Grade_Submit grade_submit = new Grade_Submit();
            grade_submit.setRoll_number(Position);
            grade_submit.setRemarks(remark);
            grade_submit.setGrade_id(grade_id);
            grade_submits.add(grade_submit);
        }
    }

    public void SubmitDataData(String student_id, String describtion) {
        String grade_ids = "", remarks = "";
        if (grade_submits.size() > 0) {
            for (int i = 0; i < grade_submits.size(); i++) {
                if (student_id.equals(grade_submits.get(i).getRoll_number())) {
                    if (grade_ids.equals("")) {
                        grade_ids = grade_submits.get(i).getGrade_id();
                        remarks = grade_submits.get(i).getRemarks();
                    } else {
                        grade_ids = grade_ids + "," + grade_submits.get(i).getGrade_id();
                        remarks = remarks + "," + grade_submits.get(i).getRemarks();
                    }
                }
            }
            ErrorMessage.E("grade_ids" + grade_ids);
            ErrorMessage.E("remarks" + remarks);
            ErrorMessage.E("SubmitDataData" + student_id+">>"+grade_ids+">>"+remarks+">>"+describtion);

            Submit_StudentList(student_id,grade_ids,remarks,describtion);
          try{
            position = position + 1;
            //monthlyPerformancePager.setCurrentItem(position);
              monthlyPerformancePager.arrowScroll(View.FOCUS_RIGHT);
          }catch (Exception e){}
        }
    }

    public void Submit_StudentList(String student_id, String grades, String remarks, String describtion) {
        if (NetworkUtil.isNetworkAvailable(MonthlyPerformanceSummeryActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(MonthlyPerformanceSummeryActivity.this);
            ErrorMessage.E("Submit_StudentList"+UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id()+">>"+ student_id+">>"+  grades+">>"+  remarks+">>"+  describtion);
            Call<ResponseBody> call = AppConfig.getLoadInterface().addStudentPerformance(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id(), student_id, grades, remarks, describtion);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            materialDialog.dismiss();
                            Gson gson = new Gson();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("Submit_StudentList" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                String responseData = jsonObject.toString();

                            } else {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();

                        }


                    } else {
                        ErrorMessage.T(MonthlyPerformanceSummeryActivity.this, "Response not successful");
                        materialDialog.dismiss();

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(MonthlyPerformanceSummeryActivity.this, "Response Fail");
                    materialDialog.dismiss();

                }
            });

        } else {
            ErrorMessage.T(MonthlyPerformanceSummeryActivity.this, getResources().getString(R.string.no_internet));


        }
    }

}
