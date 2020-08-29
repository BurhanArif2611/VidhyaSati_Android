package com.mahavir_infotech.vidyasthali.activity.Student;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.AppConfig;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.Utility.NetworkUtil;
import com.mahavir_infotech.vidyasthali.activity.BaseActivity;
import com.mahavir_infotech.vidyasthali.activity.Teacher.LoginActivity;
import com.mahavir_infotech.vidyasthali.adapter.SearchChatList_Adapter;
import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;
import com.mahavir_infotech.vidyasthali.models.RecentModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchChatListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.recycler_view_contest)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.search_img_btn)
    ImageButton searchImgBtn;
    @BindView(R.id.no_data_found_tv)
    TextView noDataFoundTv;
    private String Check = "";
    ArrayList<RecentModel> getResultlist = new ArrayList<>();
    SearchChatList_Adapter contestLatestAdapter;

    @Override
    protected int getContentResId() {
        return R.layout.activity_search_chat_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            Check = bundle.getString("Check");
            ErrorMessage.E("Check" + Check);
            if (Check.equals("student") || Check.contains("student")) {
                //RecentChat_UserList();
                titleTxt.setText("All Teacher List");
                RecentTeacherList();
            } else if (Check.equals("teacher") || Check.contains("teacher")) {
                titleTxt.setText("All Student List");
                RecentStudentList();
            }else if (Check.equals("parent") || Check.contains("parent")) {
                titleTxt.setText("Adminstrater");
                AdministratorList();
            }

        }

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, android.R.color.holo_green_dark, android.R.color.holo_orange_dark, android.R.color.holo_blue_dark);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ErrorMessage.E("onQueryTextSubmit");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    ErrorMessage.E("onQueryTextChange"+newText.toString());
                    if (newText.length() != 0) {
                        if (getResultlist.size() > 0) {
                            contestLatestAdapter.getFilter().filter(newText);
                            recyclerView.setVisibility(View.VISIBLE);
                            noDataFoundTv.setVisibility(View.GONE);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            noDataFoundTv.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (getResultlist.size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            noDataFoundTv.setVisibility(View.GONE);
                            contestLatestAdapter = new SearchChatList_Adapter(SearchChatListActivity.this, getResultlist);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SearchChatListActivity.this);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(contestLatestAdapter);
                            contestLatestAdapter.notifyDataSetChanged();
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            noDataFoundTv.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {
                    ErrorMessage.E("Exception"+e.toString());
                } return false;
            }
        });


        searchImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setVisibility(View.VISIBLE);
                searchImgBtn.setVisibility(View.GONE);
                titleTxt.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onRefresh() {

    }

    private void RecentTeacherList() {
        //  showProgressDialog();
        try {
            if (NetworkUtil.isNetworkAvailable(SearchChatListActivity.this)) {
                final Dialog materialDialog = ErrorMessage.initProgressDialog(SearchChatListActivity.this);
                ErrorMessage.E("RecentTeacherList" + UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id() + ">>");
                Call<ResponseBody> call = AppConfig.getLoadInterface().ChatSearchTeacher(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            materialDialog.dismiss();
                            if (response.isSuccessful()) {
                                try {
                                    getResultlist.clear();
                                    String str = response.body().string();
                                    JSONObject objectb = new JSONObject(str);
                                    ErrorMessage.E("RecentTeacherList" + objectb.toString());

                                    if (objectb.getString("error_code").equals("200")) {
                                        JSONObject jsonObject12 = objectb.getJSONObject("result");
                                        /*otherID = jsonObject12.getString("user_id");
                                        Username = jsonObject12.getString("user_name");
                                        img = jsonObject12.getString("profile_img");*/
                                        JSONArray jsonArray = jsonObject12.getJSONArray("list_types");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            RecentModel recentModel = new RecentModel();
                                            recentModel.setUser_id(jsonObject1.getString("teacher_id"));
                                            recentModel.setUsername(jsonObject1.getString("name"));
                                            recentModel.setUser_profile_pic(jsonObject1.getString("file_name"));
                                            recentModel.setRole(jsonObject1.getString("role"));
                                           /* recentModel.setEmail(jsonObject1.getString("email"));
                                            recentModel.setLast_message(jsonObject1.getString("message"));
                                            recentModel.setCreated_at(jsonObject1.getString("created_at"));
                                            recentModel.setUnread_msg(jsonObject1.getString("unread_msg"));*/
                                            getResultlist.add(recentModel);
                                        }
                                        ErrorMessage.E("RecentTeacherList" + getResultlist.size());
                                        if (getResultlist.size() > 0) {
                                            recyclerView.setVisibility(View.VISIBLE);
                                            contestLatestAdapter = new SearchChatList_Adapter(SearchChatListActivity.this, getResultlist);
                                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SearchChatListActivity.this);
                                            recyclerView.setLayoutManager(mLayoutManager);
                                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                                            recyclerView.setAdapter(contestLatestAdapter);
                                            contestLatestAdapter.notifyDataSetChanged();
                                            mSwipeRefreshLayout.setRefreshing(false);
                                        }
                                        //  hideProgressDialog();
                                    } else if (objectb.getString("status").equalsIgnoreCase("410")) {
                                        ErrorMessage.T(SearchChatListActivity.this, "Your Account is Inactive by Admin.");
                                        ErrorMessage.I_clear(SearchChatListActivity.this, LoginActivity.class, null);

                                    } else {
                                        mSwipeRefreshLayout.setRefreshing(false);
                                        materialDialog.dismiss();
                                        ErrorMessage.T(SearchChatListActivity.this, objectb.getString("message"));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    //  hideProgressDialog();
                                    materialDialog.dismiss();
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    ErrorMessage.E("Exception" + e.toString());
                                }


                            } else {
                                materialDialog.dismiss();
                                ErrorMessage.T(SearchChatListActivity.this, "response error");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            //  hideProgressDialog();
                            materialDialog.dismiss();
                            mSwipeRefreshLayout.setRefreshing(false);
                        }


                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                        materialDialog.dismiss();
                        mSwipeRefreshLayout.setRefreshing(false);
                        System.out.println("=======recent chat  failure:" + t.toString());
                        // hideProgressDialog();
                    }
                });
            }
        } catch (Exception e) {
        }

    }

    private void RecentStudentList() {
        //  showProgressDialog();
        try {
            if (NetworkUtil.isNetworkAvailable(SearchChatListActivity.this)) {
                final Dialog materialDialog = ErrorMessage.initProgressDialog(SearchChatListActivity.this);
                ErrorMessage.E("RecentStudentList" + UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id() + ">>");
                Call<ResponseBody> call = AppConfig.getLoadInterface().ChatSearchStudents(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            materialDialog.dismiss();
                            mSwipeRefreshLayout.setRefreshing(false);
                            if (response.isSuccessful()) {
                                try {
                                    getResultlist.clear();
                                    String str = response.body().string();
                                    JSONObject objectb = new JSONObject(str);
                                    ErrorMessage.E("RecentStudentList" + objectb.toString());

                                    if (objectb.getString("error_code").equals("200")) {
                                        JSONObject jsonObject12 = objectb.getJSONObject("result");
                                        /*otherID = jsonObject12.getString("user_id");
                                        Username = jsonObject12.getString("user_name");
                                        img = jsonObject12.getString("profile_img");*/
                                        JSONArray jsonArray = jsonObject12.getJSONArray("list_types");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            RecentModel recentModel = new RecentModel();
                                            recentModel.setUser_id(jsonObject1.getString("student_id"));
                                            recentModel.setUsername(jsonObject1.getString("name"));
                                            recentModel.setUser_profile_pic(jsonObject1.getString("file_name"));
                                            recentModel.setRole(jsonObject1.getString("role"));
                                            recentModel.setClass_name(jsonObject1.getString("class"));
                                           /* recentModel.setEmail(jsonObject1.getString("email"));
                                            recentModel.setLast_message(jsonObject1.getString("message"));
                                            recentModel.setCreated_at(jsonObject1.getString("created_at"));
                                            recentModel.setUnread_msg(jsonObject1.getString("unread_msg"));*/
                                            getResultlist.add(recentModel);
                                        }
                                        ErrorMessage.E("RecentTeacherList" + getResultlist.size());
                                        if (getResultlist.size() > 0) {
                                            recyclerView.setVisibility(View.VISIBLE);
                                            contestLatestAdapter = new SearchChatList_Adapter(SearchChatListActivity.this, getResultlist);
                                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SearchChatListActivity.this);
                                            recyclerView.setLayoutManager(mLayoutManager);
                                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                                            recyclerView.setAdapter(contestLatestAdapter);
                                            contestLatestAdapter.notifyDataSetChanged();
                                            mSwipeRefreshLayout.setRefreshing(false);
                                        }
                                        //  hideProgressDialog();
                                    } else if (objectb.getString("status").equalsIgnoreCase("410")) {
                                        ErrorMessage.T(SearchChatListActivity.this, "Your Account is Inactive by Admin.");
                                        ErrorMessage.I_clear(SearchChatListActivity.this, LoginActivity.class, null);

                                    } else {
                                        mSwipeRefreshLayout.setRefreshing(false);
                                        materialDialog.dismiss();
                                        ErrorMessage.T(SearchChatListActivity.this, objectb.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //  hideProgressDialog();
                                    materialDialog.dismiss();
                                    mSwipeRefreshLayout.setRefreshing(false);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    //  hideProgressDialog();
                                    materialDialog.dismiss();
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }


                            } else {
                                materialDialog.dismiss();
                                ErrorMessage.T(SearchChatListActivity.this, "response error");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            //  hideProgressDialog();
                            materialDialog.dismiss();
                            mSwipeRefreshLayout.setRefreshing(false);
                        }


                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                        materialDialog.dismiss();
                        mSwipeRefreshLayout.setRefreshing(false);
                        System.out.println("=======recent chat  failure:" + t.toString());
                        // hideProgressDialog();
                    }
                });
            }
        } catch (Exception e) {
        }

    }
    private void AdministratorList() {
        //  showProgressDialog();
        try {
            if (NetworkUtil.isNetworkAvailable(SearchChatListActivity.this)) {
                final Dialog materialDialog = ErrorMessage.initProgressDialog(SearchChatListActivity.this);
                ErrorMessage.E("AdministratorList" + UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id() + ">>");
                Call<ResponseBody> call = AppConfig.getLoadInterface().ChatSearchPrincipal(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            materialDialog.dismiss();
                            mSwipeRefreshLayout.setRefreshing(false);
                            if (response.isSuccessful()) {
                                try {
                                    getResultlist.clear();
                                    String str = response.body().string();
                                    JSONObject objectb = new JSONObject(str);
                                    ErrorMessage.E("RecentStudentList" + objectb.toString());

                                    if (objectb.getString("error_code").equals("200")) {
                                        JSONObject jsonObject12 = objectb.getJSONObject("result");
                                        /*otherID = jsonObject12.getString("user_id");
                                        Username = jsonObject12.getString("user_name");
                                        img = jsonObject12.getString("profile_img");*/
                                        JSONArray jsonArray = jsonObject12.getJSONArray("list_types");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            RecentModel recentModel = new RecentModel();
                                            recentModel.setUser_id(jsonObject1.getString("teacher_id"));
                                            recentModel.setUsername(jsonObject1.getString("name"));
                                            recentModel.setUser_profile_pic(jsonObject1.getString("file_name"));
                                            recentModel.setRole(jsonObject1.getString("role"));
                                           /* recentModel.setEmail(jsonObject1.getString("email"));
                                            recentModel.setLast_message(jsonObject1.getString("message"));
                                            recentModel.setCreated_at(jsonObject1.getString("created_at"));
                                            recentModel.setUnread_msg(jsonObject1.getString("unread_msg"));*/
                                            getResultlist.add(recentModel);
                                        }
                                        ErrorMessage.E("RecentTeacherList" + getResultlist.size());
                                        if (getResultlist.size() > 0) {
                                            recyclerView.setVisibility(View.VISIBLE);
                                            contestLatestAdapter = new SearchChatList_Adapter(SearchChatListActivity.this, getResultlist);
                                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SearchChatListActivity.this);
                                            recyclerView.setLayoutManager(mLayoutManager);
                                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                                            recyclerView.setAdapter(contestLatestAdapter);
                                            contestLatestAdapter.notifyDataSetChanged();
                                            mSwipeRefreshLayout.setRefreshing(false);
                                        }
                                        //  hideProgressDialog();
                                    } else if (objectb.getString("status").equalsIgnoreCase("410")) {
                                        ErrorMessage.T(SearchChatListActivity.this, "Your Account is Inactive by Admin.");
                                        ErrorMessage.I_clear(SearchChatListActivity.this, LoginActivity.class, null);

                                    } else {
                                        mSwipeRefreshLayout.setRefreshing(false);
                                        materialDialog.dismiss();
                                        ErrorMessage.T(SearchChatListActivity.this, objectb.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //  hideProgressDialog();
                                    materialDialog.dismiss();
                                    mSwipeRefreshLayout.setRefreshing(false);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    //  hideProgressDialog();
                                    materialDialog.dismiss();
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }


                            } else {
                                materialDialog.dismiss();
                                ErrorMessage.T(SearchChatListActivity.this, "response error");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            //  hideProgressDialog();
                            materialDialog.dismiss();
                            mSwipeRefreshLayout.setRefreshing(false);
                        }


                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                        materialDialog.dismiss();
                        mSwipeRefreshLayout.setRefreshing(false);
                        System.out.println("=======recent chat  failure:" + t.toString());
                        // hideProgressDialog();
                    }
                });
            }
        } catch (Exception e) {
        }

    }
}
