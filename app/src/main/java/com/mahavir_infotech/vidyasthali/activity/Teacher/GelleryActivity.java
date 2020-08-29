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
import com.mahavir_infotech.vidyasthali.adapter.Gallery_Adapter;
import com.mahavir_infotech.vidyasthali.adapter.ListHomeWork_Adapter;
import com.mahavir_infotech.vidyasthali.models.Gallery_models.Example;


import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GelleryActivity extends BaseActivity {

    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.gallery_rcv)
    RecyclerView galleryRcv;
    @BindView(R.id.no_data_tv)
    TextView noDataTv;

    @Override
    protected int getContentResId() {
        return R.layout.activity_gellery;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        titleTxt.setText("Gallery");
        Get_Gellery();
    }

    public void Get_Gellery() {
        if (NetworkUtil.isNetworkAvailable(GelleryActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(GelleryActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().listGallery();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            materialDialog.dismiss();
                            Gson gson = new Gson();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("Get_Gellery" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                String responseData = jsonObject.toString();
                                Example example = gson.fromJson(responseData, Example.class);
                                if (example.getResult().size() > 0) {
                                    noDataTv.setVisibility(View.GONE);
                                    galleryRcv.setVisibility(View.VISIBLE);
                                    Gallery_Adapter qtyListAdater = new Gallery_Adapter(GelleryActivity.this, example.getResult());
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(GelleryActivity.this);
                                    galleryRcv.setLayoutManager(mLayoutManager);
                                    galleryRcv.setItemAnimator(new DefaultItemAnimator());
                                    galleryRcv.setAdapter(qtyListAdater);
                                } else {
                                    noDataTv.setVisibility(View.VISIBLE);
                                    galleryRcv.setVisibility(View.GONE);
                                }
                            } else {
                                noDataTv.setVisibility(View.VISIBLE);
                                galleryRcv.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("productdetail error" + e.toString());
                            //  ErrorMessage.T(GelleryActivity.this, "Server Error");
                            materialDialog.dismiss();
                            noDataTv.setVisibility(View.VISIBLE);
                            galleryRcv.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                            noDataTv.setVisibility(View.VISIBLE);
                            galleryRcv.setVisibility(View.GONE);
                        }


                    } else {
                        ErrorMessage.T(GelleryActivity.this, "Response not successful");
                        materialDialog.dismiss();
                        noDataTv.setVisibility(View.VISIBLE);
                        galleryRcv.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(GelleryActivity.this, "Response Fail");
                    materialDialog.dismiss();
                    noDataTv.setVisibility(View.VISIBLE);
                    galleryRcv.setVisibility(View.GONE);
                }
            });

        } else {
            ErrorMessage.T(GelleryActivity.this, getResources().getString(R.string.no_internet));
            noDataTv.setVisibility(View.VISIBLE);
            galleryRcv.setVisibility(View.GONE);
        }
    }
}
