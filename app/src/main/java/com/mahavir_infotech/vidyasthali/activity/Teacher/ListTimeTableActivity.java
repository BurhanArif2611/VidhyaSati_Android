package com.mahavir_infotech.vidyasthali.activity.Teacher;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.AppConfig;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.Utility.NetworkUtil;
import com.mahavir_infotech.vidyasthali.activity.BaseActivity;
import com.mahavir_infotech.vidyasthali.adapter.GetListTable_Adapter;
import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;
import com.mahavir_infotech.vidyasthali.models.GetTimeTable.Example;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListTimeTableActivity extends BaseActivity {

    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.time_table_rcv)
    RecyclerView timeTableRcv;
    @BindView(R.id.add_btn)
    FloatingActionButton addBtn;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.tool_barLayout)
    RelativeLayout toolBarLayout;
    @BindView(R.id.no_data_tv)
    TextView noDataTv;

    @Override
    protected int getContentResId() {
        return R.layout.activity_list_time_table;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        titleTxt.setText("Time Table's");
        Get_TimeTable();
       /* if (UserProfileHelper.getInstance().getUserProfileModel().get(0).getRole().equals("teacher"))*/
    }

    @OnClick(R.id.add_btn)
    public void onClick() {
        ErrorMessage.I(ListTimeTableActivity.this, ADD_TimeTableActivity.class, null);
    }

    public void Get_TimeTable() {
        if (NetworkUtil.isNetworkAvailable(ListTimeTableActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ListTimeTableActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().listTimeTable(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            materialDialog.dismiss();
                            Gson gson = new Gson();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("Get_Syllabus" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                String responseData = jsonObject.toString();
                                Example example = gson.fromJson(responseData, Example.class);
                                if (example.getResult().getListTimetable().size() > 0) {
                                    Collections.reverse(example.getResult().getListTimetable());
                                    noDataTv.setVisibility(View.GONE);
                                    timeTableRcv.setVisibility(View.VISIBLE);
                                    GetListTable_Adapter qtyListAdater = new GetListTable_Adapter(ListTimeTableActivity.this, example.getResult().getListTimetable());
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ListTimeTableActivity.this);
                                    timeTableRcv.setLayoutManager(mLayoutManager);
                                    timeTableRcv.setItemAnimator(new DefaultItemAnimator());
                                    timeTableRcv.setAdapter(qtyListAdater);
                                }else {
                                    noDataTv.setVisibility(View.VISIBLE);
                                    timeTableRcv.setVisibility(View.GONE);
                                }
                            } else {
                                noDataTv.setVisibility(View.VISIBLE);
                                timeTableRcv.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("productdetail error" + e.toString());
                            //  ErrorMessage.T(ListTimeTableActivity.this, "Server Error");
                            materialDialog.dismiss();
                            noDataTv.setVisibility(View.VISIBLE);
                            timeTableRcv.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                            noDataTv.setVisibility(View.VISIBLE);
                            timeTableRcv.setVisibility(View.GONE);
                        }


                    } else {
                        ErrorMessage.T(ListTimeTableActivity.this, "Response not successful");
                        materialDialog.dismiss();
                        noDataTv.setVisibility(View.VISIBLE);
                        timeTableRcv.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ListTimeTableActivity.this, "Response Fail");
                    materialDialog.dismiss();
                    noDataTv.setVisibility(View.VISIBLE);
                    timeTableRcv.setVisibility(View.GONE);
                }
            });

        } else {
            ErrorMessage.T(ListTimeTableActivity.this, getResources().getString(R.string.no_internet));
            noDataTv.setVisibility(View.VISIBLE);
            timeTableRcv.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        Get_TimeTable();
        super.onResume();
    }
}