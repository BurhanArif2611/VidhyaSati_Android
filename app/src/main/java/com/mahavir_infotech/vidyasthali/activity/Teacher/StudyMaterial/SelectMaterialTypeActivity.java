package com.mahavir_infotech.vidyasthali.activity.Teacher.StudyMaterial;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.mahavir_infotech.vidyasthali.activity.Student.SubjectListActivity;
import com.mahavir_infotech.vidyasthali.activity.Teacher.ListSyllabusActivity;
import com.mahavir_infotech.vidyasthali.activity.Teacher.Live_Class.ListLiveClassActivity;
import com.mahavir_infotech.vidyasthali.adapter.Material_Type_Adapter;
import com.mahavir_infotech.vidyasthali.models.Material_Type.Example;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectMaterialTypeActivity extends BaseActivity {

    Example example_type;
    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.syllabus_rcv)
    RecyclerView syllabusRcv;
    @BindView(R.id.no_data_tv)
    TextView noDataTv;
    @BindView(R.id.image1_img)
    ImageView image1Img;
    @BindView(R.id.live_session_layout)
    LinearLayout liveSessionLayout;
    @BindView(R.id.class_name)
    TextView className;
    private String Check = "";

    @Override
    protected int getContentResId() {
        return R.layout.activity_select_material_type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        titleTxt.setText("Material Type");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Check = bundle.getString("Check");
        }
        Get_Material_Type();
    }

    public void Get_Material_Type() {
        if (NetworkUtil.isNetworkAvailable(SelectMaterialTypeActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(SelectMaterialTypeActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().getlistStudyMaterialTypes();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            materialDialog.dismiss();
                            Gson gson = new Gson();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("Get_Material_Type" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                String responseData = jsonObject.toString();
                                example_type = gson.fromJson(responseData, Example.class);

                                if (example_type.getResult().getListTypes().size() > 0) {
                                    Material_Type_Adapter qtyListAdater = new Material_Type_Adapter(SelectMaterialTypeActivity.this, example_type.getResult().getListTypes(), "");
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SelectMaterialTypeActivity.this);
                                    syllabusRcv.setLayoutManager(mLayoutManager);
                                    syllabusRcv.setItemAnimator(new DefaultItemAnimator());
                                    syllabusRcv.setAdapter(qtyListAdater);
                                    qtyListAdater.notifyDataSetChanged();
                                }

                            } else {
                                ErrorMessage.T(SelectMaterialTypeActivity.this, jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("productdetail error" + e.toString());
                            //  ErrorMessage.T(SelectMaterialTypeActivity.this, "Server Error");
                            materialDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(SelectMaterialTypeActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(SelectMaterialTypeActivity.this, "Response Fail");
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(SelectMaterialTypeActivity.this, getResources().getString(R.string.no_internet));
        }
    }

    public void Select_MaterialType(String id, String materialType) {
        if (example_type.getResult().getListTypes().size() > 0) {
            Material_Type_Adapter qtyListAdater = new Material_Type_Adapter(SelectMaterialTypeActivity.this, example_type.getResult().getListTypes(), id);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SelectMaterialTypeActivity.this);
            syllabusRcv.setLayoutManager(mLayoutManager);
            syllabusRcv.setItemAnimator(new DefaultItemAnimator());
            syllabusRcv.setAdapter(qtyListAdater);
            qtyListAdater.notifyDataSetChanged();
        }
        if (Check.equals("")) {
            Bundle bundle = new Bundle();
            bundle.putString("Material_Type_id", id);
            bundle.putString("Material_name", materialType);
            ErrorMessage.I(SelectMaterialTypeActivity.this, ListSyllabusActivity.class, bundle);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("Material_Type_id", id);
            bundle.putString("Material_name", materialType);
            ErrorMessage.I(SelectMaterialTypeActivity.this, SubjectListActivity.class, bundle);
            //ErrorMessage.I(SelectMaterialTypeActivity.this, Student_ListSyllabusActivity.class, bundle);
        }
    }

    @OnClick({R.id.image1_img, R.id.live_session_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image1_img:
                liveSessionLayout.setBackground(getResources().getDrawable(R.drawable.background_selected_box));
                image1Img.setImageDrawable(getResources().getDrawable(R.drawable.ic_verified));
                className.setTextColor(getResources().getColor(R.color.colorWhite));
                Bundle bundle = new Bundle();
                bundle.putString("Check", Check);
                ErrorMessage.I(SelectMaterialTypeActivity.this, ListLiveClassActivity.class, bundle);
                break;
            case R.id.live_session_layout:
                liveSessionLayout.setBackground(getResources().getDrawable(R.drawable.background_selected_box));
                image1Img.setImageDrawable(getResources().getDrawable(R.drawable.ic_verified));
                className.setTextColor(getResources().getColor(R.color.colorWhite));
                Bundle bundle1 = new Bundle();
                bundle1.putString("Check", Check);
                ErrorMessage.I(SelectMaterialTypeActivity.this, ListLiveClassActivity.class, bundle1);

                break;
        }
    }
}
/*
/listStudyMaterialBySubjectId?subject_id=1&material_type_id=1*/
