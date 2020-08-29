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
import com.mahavir_infotech.vidyasthali.activity.Teacher.StudyMaterial.ADDSyllabusActivity;
import com.mahavir_infotech.vidyasthali.adapter.ListSyllabus_Adapter;
import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;
import com.mahavir_infotech.vidyasthali.models.GetSyllabus.Example;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListSyllabusActivity extends BaseActivity {

    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.syllabus_rcv)
    RecyclerView syllabusRcv;
    @BindView(R.id.add_btn)
    FloatingActionButton addBtn;
    @BindView(R.id.no_data_tv)
    TextView noDataTv;
    String Material_Type_id = "";
    String Material_name = "";

    @Override
    protected int getContentResId() {
        return R.layout.activity_add_syllabus;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            titleTxt.setText(bundle.getString("Material_name"));
            Material_Type_id=bundle.getString("Material_Type_id");
            Material_name=bundle.getString("Material_name");
        }
        Get_Syllabus();
    }

    @OnClick(R.id.add_btn)
    public void onClick() {
        Bundle bundle=new Bundle();
        bundle.putString("Material_Type_id",Material_Type_id);
        bundle.putString("Material_name",titleTxt.getText().toString());
        bundle.putString("Check","");
        ErrorMessage.I(ListSyllabusActivity.this, ADDSyllabusActivity.class, bundle);
    }

    public void Get_Syllabus() {
        if (NetworkUtil.isNetworkAvailable(ListSyllabusActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ListSyllabusActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().listStudyMaterial(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id(),Material_Type_id);
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
                                if (example.getResult().getListHomework().size() > 0) {
                                    noDataTv.setVisibility(View.GONE);
                                    syllabusRcv.setVisibility(View.VISIBLE);
                                    ListSyllabus_Adapter qtyListAdater = new ListSyllabus_Adapter(ListSyllabusActivity.this, example.getResult().getListHomework(),Material_name);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ListSyllabusActivity.this);
                                    syllabusRcv.setLayoutManager(mLayoutManager);
                                    syllabusRcv.setItemAnimator(new DefaultItemAnimator());
                                    syllabusRcv.setAdapter(qtyListAdater);
                                } else {
                                    noDataTv.setVisibility(View.VISIBLE);
                                    syllabusRcv.setVisibility(View.GONE);
                                }
                            } else {
                                noDataTv.setVisibility(View.VISIBLE);
                                syllabusRcv.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("productdetail error" + e.toString());
                            //  ErrorMessage.T(ListSyllabusActivity.this, "Server Error");
                            materialDialog.dismiss();
                            noDataTv.setVisibility(View.VISIBLE);
                            syllabusRcv.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                            noDataTv.setVisibility(View.VISIBLE);
                            syllabusRcv.setVisibility(View.GONE);
                        }
                    } else {
                        ErrorMessage.T(ListSyllabusActivity.this, "Response not successful");
                        materialDialog.dismiss();
                        noDataTv.setVisibility(View.VISIBLE);
                        syllabusRcv.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ListSyllabusActivity.this, "Response Fail");
                    materialDialog.dismiss();
                    noDataTv.setVisibility(View.VISIBLE);
                    syllabusRcv.setVisibility(View.GONE);
                }
            });

        } else {
            ErrorMessage.T(ListSyllabusActivity.this, getResources().getString(R.string.no_internet));
            noDataTv.setVisibility(View.VISIBLE);
            syllabusRcv.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        ErrorMessage.E("Working"+"onResume");
        Get_Syllabus();
        super.onResume();
    }


}
