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
import com.mahavir_infotech.vidyasthali.adapter.SubjectList_Adapter;
import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;
import com.mahavir_infotech.vidyasthali.models.GetSubject.Example;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubjectListActivity extends BaseActivity {

    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.subject_rcv)
    RecyclerView subjectRcv;
    @BindView(R.id.no_data_tv)
    TextView noDataTv;
    Example example;
    String Material_name="",Material_Type_id="";
    @Override
    protected int getContentResId() {
        return R.layout.activity_subject_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        titleTxt.setText("Select Subject");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Material_name=(bundle.getString("Material_name"));
            Material_Type_id=bundle.getString("Material_Type_id");
        }
        Get_SubjectList();
    }
    public void Get_SubjectList() {
        if (NetworkUtil.isNetworkAvailable(SubjectListActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(SubjectListActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().getSubjectByClassId(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUserPhone());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            materialDialog.dismiss();
                            Gson gson = new Gson();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("Get_SubjectList" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                String responseData = jsonObject.toString();
                                example = gson.fromJson(responseData, Example.class);
                                if (example.getResult().getListSubjects().size() > 0) {
                                    noDataTv.setVisibility(View.GONE);
                                    subjectRcv.setVisibility(View.VISIBLE);
                                    SubjectList_Adapter qtyListAdater = new SubjectList_Adapter(SubjectListActivity.this, example.getResult().getListSubjects(), "");
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SubjectListActivity.this);
                                    subjectRcv.setLayoutManager(mLayoutManager);
                                    subjectRcv.setItemAnimator(new DefaultItemAnimator());
                                    subjectRcv.setAdapter(qtyListAdater);
                                    qtyListAdater.notifyDataSetChanged();
                                }
                            } else {
                                noDataTv.setVisibility(View.VISIBLE);
                                subjectRcv.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("productdetail error" + e.toString());
                            //  ErrorMessage.T(SubjectListActivity.this, "Server Error");
                            materialDialog.dismiss();
                            noDataTv.setVisibility(View.VISIBLE);
                            subjectRcv.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                            noDataTv.setVisibility(View.VISIBLE);
                            subjectRcv.setVisibility(View.GONE);
                        }


                    } else {
                        ErrorMessage.T(SubjectListActivity.this, "Response not successful");
                        materialDialog.dismiss();
                        noDataTv.setVisibility(View.VISIBLE);
                        subjectRcv.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(SubjectListActivity.this, "Response Fail");
                    materialDialog.dismiss();
                    noDataTv.setVisibility(View.VISIBLE);
                    subjectRcv.setVisibility(View.GONE);
                }
            });

        } else {
            ErrorMessage.T(SubjectListActivity.this, getResources().getString(R.string.no_internet));
        }
    }

    public void Select_MaterialType(String subjectId, String subjectName) {
        if (example.getResult().getListSubjects().size() > 0) {
            SubjectList_Adapter qtyListAdater = new SubjectList_Adapter(SubjectListActivity.this, example.getResult().getListSubjects(), subjectId);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SubjectListActivity.this);
            subjectRcv.setLayoutManager(mLayoutManager);
            subjectRcv.setItemAnimator(new DefaultItemAnimator());
            subjectRcv.setAdapter(qtyListAdater);
            qtyListAdater.notifyDataSetChanged();
        }

            Bundle bundle = new Bundle();
            bundle.putString("Material_Type_id", Material_Type_id);
            bundle.putString("Material_name", Material_name);
            bundle.putString("subjectId", subjectId);
            bundle.putString("subjectName", subjectName);
            ErrorMessage.I(SubjectListActivity.this, Student_ListSyllabusActivity.class, bundle);


    }
}
