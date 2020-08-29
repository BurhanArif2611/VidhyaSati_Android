package com.mahavir_infotech.vidyasthali.activity.Student;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
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
import com.mahavir_infotech.vidyasthali.activity.Teacher.ListTimeTableActivity;
import com.mahavir_infotech.vidyasthali.adapter.Result_Adapter;
import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;
import com.mahavir_infotech.vidyasthali.models.Result_Models.Example;


import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultActivity extends BaseActivity {

    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.time_table_rcv)
    RecyclerView timeTableRcv;
    @BindView(R.id.no_data_tv)
    TextView noDataTv;

    @Override
    protected int getContentResId() {
        return R.layout.activity_result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        titleTxt.setText("Result");
        Get_Result();
    }

    public void Get_Result() {
        if (NetworkUtil.isNetworkAvailable(ResultActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ResultActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().listResult(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            materialDialog.dismiss();
                            Gson gson = new Gson();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("Get_Result" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                String responseData = jsonObject.toString();
                                Example example = gson.fromJson(responseData, Example.class);
                                if (example.getResult().getListResult().size() > 0) {

                                    noDataTv.setVisibility(View.GONE);
                                    timeTableRcv.setVisibility(View.VISIBLE);
                                    Result_Adapter qtyListAdater = new Result_Adapter(ResultActivity.this, example.getResult().getListResult());
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ResultActivity.this);
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
                        ErrorMessage.T(ResultActivity.this, "Response not successful");
                        materialDialog.dismiss();
                        noDataTv.setVisibility(View.VISIBLE);
                        timeTableRcv.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ResultActivity.this, "Response Fail");
                    materialDialog.dismiss();
                    noDataTv.setVisibility(View.VISIBLE);
                    timeTableRcv.setVisibility(View.GONE);
                }
            });

        } else {
            ErrorMessage.T(ResultActivity.this, getResources().getString(R.string.no_internet));
            noDataTv.setVisibility(View.VISIBLE);
            timeTableRcv.setVisibility(View.GONE);
        }
    }

}
