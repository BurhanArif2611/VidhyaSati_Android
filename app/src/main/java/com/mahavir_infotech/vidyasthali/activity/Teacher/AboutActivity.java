package com.mahavir_infotech.vidyasthali.activity.Teacher;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.AppConfig;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.Utility.NetworkUtil;
import com.mahavir_infotech.vidyasthali.activity.BaseActivity;
import com.mahavir_infotech.vidyasthali.activity.Student.FullImageActivity;
import com.mahavir_infotech.vidyasthali.models.AboutUS_Models.Example;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.event_img)
    ImageView eventImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.discribtion_tv)
    TextView discribtionTv;
    Example example;
    @Override
    protected int getContentResId() {
        return R.layout.activity_about;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("Check").equals("About")) {
                titleTxt.setText("About Us");
                Get_AboutUs("about_us");
            } else {
                titleTxt.setText("Terms & Condition");
                Get_AboutUs("terms");
            }
        }
        eventImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                Bundle bundle = new Bundle();
                bundle.putString("Image", example.getResult().getPhoto().getUrl());
                ErrorMessage.I(AboutActivity.this, FullImageActivity.class, bundle);}
                catch (Exception e){}
            }
        });
    }

    public void Get_AboutUs(String type) {
        if (NetworkUtil.isNetworkAvailable(AboutActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(AboutActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().getStaticPages(type);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            materialDialog.dismiss();
                            Gson gson = new Gson();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("Get_AboutUs" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                String responseData = jsonObject.toString();
                                 example = gson.fromJson(responseData, Example.class);
                                titleTv.setText(example.getResult().getName());
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                    discribtionTv.setText(Html.fromHtml(example.getResult().getDescription(), Html.FROM_HTML_MODE_COMPACT));
                                } else {
                                    discribtionTv.setText(Html.fromHtml(example.getResult().getDescription()));
                                }
                                Glide.with(AboutActivity.this).load(example.getResult().getPhoto().getUrl()).placeholder(R.drawable.ic_logo).into(eventImg);

                              /*  if (example.getResult().getListLeaves().size() > 0) {
                                    noDataFoundTv.setVisibility(View.GONE);
                                    noticeBoardRcv.setVisibility(View.VISIBLE);
                                    Notification_Adapter qtyListAdater = new Notification_Adapter(AboutActivity.this, example.getResult().getListLeaves());
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AboutActivity.this);
                                    noticeBoardRcv.setLayoutManager(mLayoutManager);
                                    noticeBoardRcv.setItemAnimator(new DefaultItemAnimator());
                                    noticeBoardRcv.setAdapter(qtyListAdater);
                                } else {
                                    noDataFoundTv.setVisibility(View.VISIBLE);
                                    noticeBoardRcv.setVisibility(View.GONE);
                                }*/
                            } else {
                               /* noDataFoundTv.setVisibility(View.VISIBLE);
                                noticeBoardRcv.setVisibility(View.GONE);*/
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("productdetail error" + e.toString());
                            //  ErrorMessage.T(AboutActivity.this, "Server Error");
                            materialDialog.dismiss();

                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();

                        }
                    } else {
                        ErrorMessage.T(AboutActivity.this, "Response not successful");
                        materialDialog.dismiss();

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(AboutActivity.this, "Response Fail");
                    materialDialog.dismiss();

                }
            });

        } else {
            ErrorMessage.T(AboutActivity.this, getResources().getString(R.string.no_internet));

        }
    }
}
