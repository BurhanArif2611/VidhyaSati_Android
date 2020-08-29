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
import com.mahavir_infotech.vidyasthali.adapter.ListStudent_Material_Adapter;
import com.mahavir_infotech.vidyasthali.models.GetSyllabus.Example;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Student_ListSyllabusActivity extends BaseActivity {


    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.syllabus_rcv)
    RecyclerView syllabusRcv;
    @BindView(R.id.no_data_tv)
    TextView noDataTv;
    private String Material_Type_id="",subjectId="",subjectName="";

    @Override
    protected int getContentResId() {
        return R.layout.activity_student__list_syllabus;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        titleTxt.setText("Syllabus");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            titleTxt.setText(bundle.getString("Material_name"));
            Material_Type_id=bundle.getString("Material_Type_id");
            subjectId=bundle.getString("subjectId");
            subjectName= bundle.getString("subjectName");
        }
        Get_Syllabus();
    }
    public void Get_Syllabus() {
        if (NetworkUtil.isNetworkAvailable(Student_ListSyllabusActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(Student_ListSyllabusActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().listStudyMaterialBySubjectId(subjectId,Material_Type_id);
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
                                    Collections.reverse(example.getResult().getListHomework());
                                    noDataTv.setVisibility(View.GONE);
                                    syllabusRcv.setVisibility(View.VISIBLE);
                                    ListStudent_Material_Adapter qtyListAdater = new ListStudent_Material_Adapter(Student_ListSyllabusActivity.this, example.getResult().getListHomework());
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Student_ListSyllabusActivity.this);
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
                            //  ErrorMessage.T(Student_ListSyllabusActivity.this, "Server Error");
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
                        ErrorMessage.T(Student_ListSyllabusActivity.this, "Response not successful");
                        materialDialog.dismiss();
                        noDataTv.setVisibility(View.VISIBLE);
                        syllabusRcv.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(Student_ListSyllabusActivity.this, "Response Fail");
                    materialDialog.dismiss();
                    noDataTv.setVisibility(View.VISIBLE);
                    syllabusRcv.setVisibility(View.GONE);
                }
            });

        } else {
            ErrorMessage.T(Student_ListSyllabusActivity.this, getResources().getString(R.string.no_internet));
            noDataTv.setVisibility(View.VISIBLE);
            syllabusRcv.setVisibility(View.GONE);
        }
    }
}
