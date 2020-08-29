package com.mahavir_infotech.vidyasthali.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.AppConfig;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.Utility.NetworkUtil;
import com.mahavir_infotech.vidyasthali.models.Support.Example;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupportActivity extends BaseActivity {

    @BindView(R.id.contact_first_tv)
    TextView contactFirstTv;
    @BindView(R.id.firstContactBtn)
    TextView firstContactBtn;
    @BindView(R.id.contact_second_tv)
    TextView contactSecondTv;
    @BindView(R.id.secondContactBtn)
    TextView secondContactBtn;
    @BindView(R.id.contact_whats_up_tv)
    TextView contactWhatsUpTv;
    @BindView(R.id.whatsup_Btn)
    TextView whatsupBtn;
    @BindView(R.id.address_tv)
    TextView addressTv;

    @BindView(R.id.title_txt)
    TextView titleTxt;

    @Override
    protected int getContentResId() {
        return R.layout.activity_support;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        titleTxt.setText("Support");
        Get_Support();
    }

    @OnClick({R.id.firstContactBtn, R.id.secondContactBtn, R.id.whatsup_Btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.firstContactBtn:
                try {
                    Intent callIntent = new Intent(Intent.ACTION_VIEW);
                    callIntent.setData(Uri.parse("tel:" + contactFirstTv.getText().toString()));
                    startActivity(callIntent);
                } catch (Exception e) {
                }
                break;
            case R.id.secondContactBtn:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    // Uri data = Uri.parse("mailto:" + "mppschindclass@gmail.com" + "?subject=Ask From HIND MPPSC Support Team");
                    Uri data = Uri.parse("mailto:" + contactSecondTv.getText().toString() + "?subject=Ask From VIDYASTHALI Support Team");
                    intent.setData(data);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e("Exaception e", "" + e);
                }
                break;
            case R.id.whatsup_Btn:
                try {
                    String toNumber = contactFirstTv.getText().toString(); // Replace with mobile phone number without +Sign or leading zeros.
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + toNumber));
                    startActivity(intent);
                } catch (Exception e) {
                    ErrorMessage.T(SupportActivity.this, " Please Install a What's Up ");
                    Log.e("Exaception e", "" + e);
                }
                break;
        }
    }


    public void Get_Support() {
        if (NetworkUtil.isNetworkAvailable(SupportActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(SupportActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().getSupport();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            materialDialog.dismiss();
                            Gson gson = new Gson();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("Get_TimeTable" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                String responseData = jsonObject.toString();
                                Example example=gson.fromJson(responseData, Example.class);
                                contactFirstTv.setText(example.getResult().getContactPhone());
                                addressTv.setText(example.getResult().getFooterAddress());
                                String[] separated1 = example.getResult().getContactEmail().split(",");
                                contactSecondTv.setText(separated1[0]);
                                String[] separated = example.getResult().getFooterMobile().split(",");
                                contactWhatsUpTv.setText( separated[0]);
                               /* if (example.getResult().getListTimetable().size() > 0) {
                                    noDataTv.setVisibility(View.GONE);
                                    timeTableRcv.setVisibility(View.VISIBLE);
                                    StudentList_TimeTable_Adapter qtyListAdater = new StudentList_TimeTable_Adapter(ListTimeTableActivity.this, example.getResult().getListTimetable());
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ListTimeTableActivity.this);
                                    timeTableRcv.setLayoutManager(mLayoutManager);
                                    timeTableRcv.setItemAnimator(new DefaultItemAnimator());
                                    timeTableRcv.setAdapter(qtyListAdater);
                                }else {
                                    noDataTv.setVisibility(View.VISIBLE);
                                    timeTableRcv.setVisibility(View.GONE);
                                }*/
                            } else {
                               /* noDataTv.setVisibility(View.VISIBLE);
                                timeTableRcv.setVisibility(View.GONE);*/
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();

                        }


                    } else {
                        ErrorMessage.T(SupportActivity.this, "Response not successful");
                        materialDialog.dismiss();

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(SupportActivity.this, "Response Fail");
                    materialDialog.dismiss();

                }
            });

        } else {
            ErrorMessage.T(SupportActivity.this, getResources().getString(R.string.no_internet));
        }
    }
}
