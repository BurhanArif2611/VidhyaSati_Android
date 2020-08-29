package com.mahavir_infotech.vidyasthali.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.AppConfig;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.Utility.NetworkUtil;
import com.mahavir_infotech.vidyasthali.adapter.ListLiveClass_Adapter;
import com.mahavir_infotech.vidyasthali.adapter.Student_LiveClass_Adapter;
import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;
import com.mahavir_infotech.vidyasthali.models.ListLiveClass_Models.Example;




import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LiveFragment extends Fragment {
    View view;
    @BindView(R.id.live_class_rcv)
    RecyclerView liveClassRcv;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;
    @BindView(R.id.no_data_found_tv)
    TextView noDataFoundTv;
    private Unbinder unbinder;
    private String Check="",short_by="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_live, container, false);
        unbinder = ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            Check= bundle.getString("Check");
            short_by= bundle.getString("short_by");
            ErrorMessage.E("short_by"+short_by);
            if (bundle.getString("Check").equals("")) {
                Get_LiveSession();
            } else {
                Get_StudentLiveSession();
            }
        }
        swiperefresh.setColorSchemeResources(R.color.colorPrimary, android.R.color.holo_green_dark, android.R.color.holo_orange_dark, android.R.color.holo_blue_dark);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Check.equals("")) {
                    Get_LiveSession();
                } else {
                    Get_StudentLiveSession();
                }

            }
        });
        return view;
    }

    public void Get_StudentLiveSession() {
        if (NetworkUtil.isNetworkAvailable(getActivity())) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(getActivity());
            Call<ResponseBody> call = AppConfig.getLoadInterface().listSessionByClassid(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUserPhone(),short_by);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            materialDialog.dismiss();
                            Gson gson = new Gson();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("Get_StudentLiveSession" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                String responseData = jsonObject.toString();
                                Example example = gson.fromJson(responseData, Example.class);
                                if (example.getResult().getListSession().size() > 0) {
                                    noDataFoundTv.setVisibility(View.GONE);
                                    liveClassRcv.setVisibility(View.VISIBLE);
                                    Student_LiveClass_Adapter qtyListAdater = new Student_LiveClass_Adapter(getActivity(), example.getResult().getListSession(),short_by);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                    liveClassRcv.setLayoutManager(mLayoutManager);
                                    liveClassRcv.setItemAnimator(new DefaultItemAnimator());
                                    liveClassRcv.setAdapter(qtyListAdater);
                                } else {
                                    noDataFoundTv.setVisibility(View.VISIBLE);
                                    liveClassRcv.setVisibility(View.GONE);
                                }
                            } else {
                                noDataFoundTv.setVisibility(View.VISIBLE);
                                liveClassRcv.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("productdetail error" + e.toString());
                            //  ErrorMessage.T(getActivity(), "Server Error");
                            materialDialog.dismiss();
                            noDataFoundTv.setVisibility(View.VISIBLE);
                            liveClassRcv.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                            noDataFoundTv.setVisibility(View.VISIBLE);
                            liveClassRcv.setVisibility(View.GONE);
                        }
                    } else {
                        ErrorMessage.T(getActivity(), "Response not successful");
                        materialDialog.dismiss();
                        noDataFoundTv.setVisibility(View.VISIBLE);
                        liveClassRcv.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(getActivity(), "Response Fail");
                    materialDialog.dismiss();
                    noDataFoundTv.setVisibility(View.VISIBLE);
                    liveClassRcv.setVisibility(View.GONE);
                }
            });

        } else {
            ErrorMessage.T(getActivity(), getResources().getString(R.string.no_internet));
            noDataFoundTv.setVisibility(View.VISIBLE);
            liveClassRcv.setVisibility(View.GONE);
        }
    }

    public void Get_LiveSession() {
        if (NetworkUtil.isNetworkAvailable(getActivity())) {
            ErrorMessage.E("Get_LiveSession" + UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
            final Dialog materialDialog = ErrorMessage.initProgressDialog(getActivity());
            Call<ResponseBody> call = AppConfig.getLoadInterface().listSessionByTeacherId(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id(),short_by);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            materialDialog.dismiss();
                            Gson gson = new Gson();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("Get_LiveSession" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                String responseData = jsonObject.toString();
                                Example example = gson.fromJson(responseData, Example.class);
                                if (example.getResult().getListSession().size() > 0) {
                                    noDataFoundTv.setVisibility(View.GONE);
                                    liveClassRcv.setVisibility(View.VISIBLE);
                                    ListLiveClass_Adapter qtyListAdater = new ListLiveClass_Adapter(getActivity(), example.getResult().getListSession());
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                    liveClassRcv.setLayoutManager(mLayoutManager);
                                    liveClassRcv.setItemAnimator(new DefaultItemAnimator());
                                    liveClassRcv.setAdapter(qtyListAdater);
                                } else {
                                    noDataFoundTv.setVisibility(View.VISIBLE);
                                    liveClassRcv.setVisibility(View.GONE);
                                }
                            } else {
                                noDataFoundTv.setVisibility(View.VISIBLE);
                                liveClassRcv.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("productdetail error" + e.toString());
                            //  ErrorMessage.T(getActivity(), "Server Error");
                            materialDialog.dismiss();
                            noDataFoundTv.setVisibility(View.VISIBLE);
                            liveClassRcv.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                            noDataFoundTv.setVisibility(View.VISIBLE);
                            liveClassRcv.setVisibility(View.GONE);
                        }
                    } else {
                        ErrorMessage.T(getActivity(), "Response not successful");
                        materialDialog.dismiss();
                        noDataFoundTv.setVisibility(View.VISIBLE);
                        liveClassRcv.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(getActivity(), "Response Fail");
                    materialDialog.dismiss();
                    noDataFoundTv.setVisibility(View.VISIBLE);
                    liveClassRcv.setVisibility(View.GONE);
                }
            });

        } else {
            ErrorMessage.T(getActivity(), getResources().getString(R.string.no_internet));
            noDataFoundTv.setVisibility(View.VISIBLE);
            liveClassRcv.setVisibility(View.GONE);
        }
    }
}
