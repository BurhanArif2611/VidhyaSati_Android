package com.mahavir_infotech.vidyasthali.activity.Teacher;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
import com.mahavir_infotech.vidyasthali.activity.Student.ADD_LeaveActivity;
import com.mahavir_infotech.vidyasthali.activity.Student.StudentHomeActivity;
import com.mahavir_infotech.vidyasthali.adapter.LeaveList_Adapter;
import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;
import com.mahavir_infotech.vidyasthali.models.LeaveList_Models.Example;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveListActivity extends BaseActivity {

    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.leave_rcv)
    RecyclerView leaveRcv;
    @BindView(R.id.total_tv)
    TextView totalTv;
    String Check = "";
    @BindView(R.id.add_btn)
    FloatingActionButton addBtn;

    @Override
    protected int getContentResId() {
        return R.layout.activity_leave_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        titleTxt.setText("Leave");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Check = bundle.getString("Check");
            addBtn.setVisibility(View.VISIBLE);
            Get_StudentLeaveList();
        } else {
            Get_LeaveList();
            addBtn.setVisibility(View.GONE);
        }
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ErrorMessage.I(LeaveListActivity.this, ADD_LeaveActivity.class, null);
            }
        });
    }

    public void Get_LeaveList() {
        if (NetworkUtil.isNetworkAvailable(LeaveListActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(LeaveListActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().leaveRequests(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            materialDialog.dismiss();
                            Gson gson = new Gson();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("Get_LeaveList" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                String responseData = jsonObject.toString();
                                Example example = gson.fromJson(responseData, Example.class);
                                if (example.getResult().getListLeaves().size() > 0) {
                                    totalTv.setText("Total :" + example.getResult().getListLeaves().size());
                                    LeaveList_Adapter qtyListAdater = new LeaveList_Adapter(LeaveListActivity.this, example.getResult().getListLeaves(),"");
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(LeaveListActivity.this);
                                    leaveRcv.setLayoutManager(mLayoutManager);
                                    leaveRcv.setItemAnimator(new DefaultItemAnimator());
                                    leaveRcv.setAdapter(qtyListAdater);
                                }
                            } else {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("productdetail error" + e.toString());
                            //  ErrorMessage.T(LeaveListActivity.this, "Server Error");
                            materialDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(LeaveListActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(LeaveListActivity.this, "Response Fail");
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(LeaveListActivity.this, getResources().getString(R.string.no_internet));
        }
    }

    public void Get_StudentLeaveList() {
        if (NetworkUtil.isNetworkAvailable(LeaveListActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(LeaveListActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().studentListLeave(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUserPhone());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            materialDialog.dismiss();
                            Gson gson = new Gson();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("Get_StudentLeaveList" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                String responseData = jsonObject.toString();
                                Example example = gson.fromJson(responseData, Example.class);
                                if (example.getResult().getListLeaves().size() > 0) {
                                    totalTv.setText("Total :" + example.getResult().getListLeaves().size());
                                    LeaveList_Adapter qtyListAdater = new LeaveList_Adapter(LeaveListActivity.this, example.getResult().getListLeaves(),"Student");
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(LeaveListActivity.this);
                                    leaveRcv.setLayoutManager(mLayoutManager);
                                    leaveRcv.setItemAnimator(new DefaultItemAnimator());
                                    leaveRcv.setAdapter(qtyListAdater);
                                }
                            } else {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("productdetail error" + e.toString());
                            //  ErrorMessage.T(LeaveListActivity.this, "Server Error");
                            materialDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(LeaveListActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(LeaveListActivity.this, "Response Fail");
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(LeaveListActivity.this, getResources().getString(R.string.no_internet));
        }
    }
    @Override
    protected void onResume() {
        if (!Check.equals("")){
            Get_StudentLeaveList();}
        else {
            Get_LeaveList();
        }
        super.onResume();
    }
}
