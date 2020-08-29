package com.mahavir_infotech.vidyasthali.activity.Teacher;

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
import com.mahavir_infotech.vidyasthali.adapter.Notification_Adapter;
import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;
import com.mahavir_infotech.vidyasthali.models.Notification_Models.Example;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationListActivity extends BaseActivity {


    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.notice_board_rcv)
    RecyclerView noticeBoardRcv;
    @BindView(R.id.no_data_found_tv)
    TextView noDataFoundTv;

    @Override
    protected int getContentResId() {
        return R.layout.activity_notice_board;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        titleTxt.setText("Notification");
        Get_Notification();
    }

    public void Get_Notification() {
        if (NetworkUtil.isNetworkAvailable(NotificationListActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(NotificationListActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().listNotification(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id(), UserProfileHelper.getInstance().getUserProfileModel().get(0).getRole());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            materialDialog.dismiss();
                            Gson gson = new Gson();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("Get_Notification" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                String responseData = jsonObject.toString();
                                Example example = gson.fromJson(responseData, Example.class);
                                if (example.getResult().getListLeaves().size() > 0) {
                                    noDataFoundTv.setVisibility(View.GONE);
                                    noticeBoardRcv.setVisibility(View.VISIBLE);
                                    Notification_Adapter qtyListAdater = new Notification_Adapter(NotificationListActivity.this, example.getResult().getListLeaves());
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(NotificationListActivity.this);
                                    noticeBoardRcv.setLayoutManager(mLayoutManager);
                                    noticeBoardRcv.setItemAnimator(new DefaultItemAnimator());
                                    noticeBoardRcv.setAdapter(qtyListAdater);
                                } else {
                                    noDataFoundTv.setVisibility(View.VISIBLE);
                                    noticeBoardRcv.setVisibility(View.GONE);
                                }
                            } else {
                                noDataFoundTv.setVisibility(View.VISIBLE);
                                noticeBoardRcv.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("productdetail error" + e.toString());
                            //  ErrorMessage.T(NotificationListActivity.this, "Server Error");
                            materialDialog.dismiss();
                            noDataFoundTv.setVisibility(View.VISIBLE);
                            noticeBoardRcv.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                            noDataFoundTv.setVisibility(View.VISIBLE);
                            noticeBoardRcv.setVisibility(View.GONE);
                        }
                    } else {
                        ErrorMessage.T(NotificationListActivity.this, "Response not successful");
                        materialDialog.dismiss();
                        noDataFoundTv.setVisibility(View.VISIBLE);
                        noticeBoardRcv.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(NotificationListActivity.this, "Response Fail");
                    materialDialog.dismiss();
                    noDataFoundTv.setVisibility(View.VISIBLE);
                    noticeBoardRcv.setVisibility(View.GONE);
                }
            });

        } else {
            ErrorMessage.T(NotificationListActivity.this, getResources().getString(R.string.no_internet));
            noDataFoundTv.setVisibility(View.VISIBLE);
            noticeBoardRcv.setVisibility(View.GONE);
        }
    }
}
