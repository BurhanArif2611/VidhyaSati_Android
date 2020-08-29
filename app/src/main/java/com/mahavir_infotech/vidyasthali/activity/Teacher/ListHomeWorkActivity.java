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
import com.mahavir_infotech.vidyasthali.adapter.ListHomeWork_Adapter;
import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;
import com.mahavir_infotech.vidyasthali.models.List_home_Work.Example;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListHomeWorkActivity extends BaseActivity {

    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.list_home_work_rcv)
    RecyclerView listHomeWorkRcv;
    @BindView(R.id.add_btn)
    FloatingActionButton addBtn;
    @BindView(R.id.no_data_tv)
    TextView noDataTv;
    String Check = "";

    @Override
    protected int getContentResId() {
        return R.layout.activity_list_home_work;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        titleTxt.setText("Home Work");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Check = bundle.getString("Check");
            addBtn.setVisibility(View.GONE);
            Get_StudentHomeWork();
        } else {
            Get_HomeWork();
        }
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ErrorMessage.I(ListHomeWorkActivity.this, CalenderActivity.class, null);
            }
        });
    }

    public void Get_HomeWork() {
        if (NetworkUtil.isNetworkAvailable(ListHomeWorkActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ListHomeWorkActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().listHomework(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
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
                                Example example = gson.fromJson(responseData, Example.class);
                                if (example.getResult().getListHomework().size() > 0) {
                                    noDataTv.setVisibility(View.GONE);
                                    listHomeWorkRcv.setVisibility(View.VISIBLE);
                                    ListHomeWork_Adapter qtyListAdater = new ListHomeWork_Adapter(ListHomeWorkActivity.this, example.getResult().getListHomework(),"");
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ListHomeWorkActivity.this);
                                    listHomeWorkRcv.setLayoutManager(mLayoutManager);
                                    listHomeWorkRcv.setItemAnimator(new DefaultItemAnimator());
                                    listHomeWorkRcv.setAdapter(qtyListAdater);
                                } else {
                                    noDataTv.setVisibility(View.VISIBLE);
                                    listHomeWorkRcv.setVisibility(View.GONE);
                                }
                            } else {
                                noDataTv.setVisibility(View.VISIBLE);
                                listHomeWorkRcv.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("productdetail error" + e.toString());
                            //  ErrorMessage.T(ListHomeWorkActivity.this, "Server Error");
                            materialDialog.dismiss();
                            noDataTv.setVisibility(View.VISIBLE);
                            listHomeWorkRcv.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                            noDataTv.setVisibility(View.VISIBLE);
                            listHomeWorkRcv.setVisibility(View.GONE);
                        }


                    } else {
                        ErrorMessage.T(ListHomeWorkActivity.this, "Response not successful");
                        try {
                            materialDialog.dismiss();
                            noDataTv.setVisibility(View.VISIBLE);
                            listHomeWorkRcv.setVisibility(View.GONE);
                        }catch (Exception e){};
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ListHomeWorkActivity.this, "Response Fail");
                    materialDialog.dismiss();
                    noDataTv.setVisibility(View.VISIBLE);
                    listHomeWorkRcv.setVisibility(View.GONE);
                }
            });

        } else {
            ErrorMessage.T(ListHomeWorkActivity.this, getResources().getString(R.string.no_internet));
            noDataTv.setVisibility(View.VISIBLE);
            listHomeWorkRcv.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        if (Check.equals("")){
        Get_HomeWork();}
        super.onResume();
    }
    public void Get_StudentHomeWork() {
        if (NetworkUtil.isNetworkAvailable(ListHomeWorkActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ListHomeWorkActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().listHomeWorkByClassid(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUserPhone());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            materialDialog.dismiss();
                            Gson gson = new Gson();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("Get_StudentHomeWork" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                String responseData = jsonObject.toString();
                                Example example = gson.fromJson(responseData, Example.class);
                                if (example.getResult().getListHomework().size() > 0) {
                                    noDataTv.setVisibility(View.GONE);
                                    listHomeWorkRcv.setVisibility(View.VISIBLE);
                                    ListHomeWork_Adapter qtyListAdater = new ListHomeWork_Adapter(ListHomeWorkActivity.this, example.getResult().getListHomework(),"Student");
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ListHomeWorkActivity.this);
                                    listHomeWorkRcv.setLayoutManager(mLayoutManager);
                                    listHomeWorkRcv.setItemAnimator(new DefaultItemAnimator());
                                    listHomeWorkRcv.setAdapter(qtyListAdater);
                                } else {
                                    noDataTv.setVisibility(View.VISIBLE);
                                    listHomeWorkRcv.setVisibility(View.GONE);
                                }
                            } else {
                                noDataTv.setVisibility(View.VISIBLE);
                                listHomeWorkRcv.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("productdetail error" + e.toString());
                            //  ErrorMessage.T(ListHomeWorkActivity.this, "Server Error");
                            materialDialog.dismiss();
                            noDataTv.setVisibility(View.VISIBLE);
                            listHomeWorkRcv.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                            noDataTv.setVisibility(View.VISIBLE);
                            listHomeWorkRcv.setVisibility(View.GONE);
                        }


                    } else {
                        ErrorMessage.T(ListHomeWorkActivity.this, "Response not successful");
                        materialDialog.dismiss();
                        noDataTv.setVisibility(View.VISIBLE);
                        listHomeWorkRcv.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ListHomeWorkActivity.this, "Response Fail");
                    materialDialog.dismiss();
                    noDataTv.setVisibility(View.VISIBLE);
                    listHomeWorkRcv.setVisibility(View.GONE);
                }
            });

        } else {
            ErrorMessage.T(ListHomeWorkActivity.this, getResources().getString(R.string.no_internet));
            noDataTv.setVisibility(View.VISIBLE);
            listHomeWorkRcv.setVisibility(View.GONE);
        }
    }
}
