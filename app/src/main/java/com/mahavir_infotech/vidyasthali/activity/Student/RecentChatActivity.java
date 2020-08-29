package com.mahavir_infotech.vidyasthali.activity.Student;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mahavir_infotech.vidyasthali.Firebase.Config;
import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.AppConfig;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.Utility.NetworkUtil;
import com.mahavir_infotech.vidyasthali.activity.Teacher.LoginActivity;
import com.mahavir_infotech.vidyasthali.adapter.RecentChatAdapter;
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

public class RecentChatActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<RecentModel> getResultlist = new ArrayList<>();

    FloatingActionButton fab;
    @BindView(R.id.no_data_found_tv)
    TextView noDataFoundTv;
    @BindView(R.id.search_btn)
    ImageButton searchBtn;
    @BindView(R.id.search_etv)
    EditText searchEtv;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.cancel_back_img)
    ImageView cancelBackImg;
    @BindView(R.id.search_layout)
    LinearLayout searchLayout;


    private String otherID = "", img = "", Username = "";
    LinearLayout toolbar;
    private String Check = "";
    RecentChatAdapter contestLatestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_chat);
        ButterKnife.bind(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_contest);
        toolbar = (LinearLayout) findViewById(R.id.toolbar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Check = bundle.getString("Check");
            ErrorMessage.E("RecentChatActivity" + Check);
            RecentChat_UserList();
        }
// SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, android.R.color.holo_green_dark, android.R.color.holo_orange_dark, android.R.color.holo_blue_dark);

        ImageView cancel_img = (ImageView) findViewById(R.id.cancel_img);
        cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  ErrorMessage.I_clear(RecentChatActivity.this, StudentHomeActivity.class, null);
                finish();
            }
        });
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(Config.Chating));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecentChatActivity.this, SearchChatListActivity.class);
                intent.putExtra("Check", Check);
                startActivity(intent);

            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchLayout.setVisibility(View.VISIBLE);

            }
        });
        cancelBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchLayout.setVisibility(View.GONE);
                toolbar.setVisibility(View.VISIBLE);
                if (getResultlist.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    noDataFoundTv.setVisibility(View.GONE);
                    contestLatestAdapter = new RecentChatAdapter(RecentChatActivity.this, getResultlist);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(RecentChatActivity.this);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(contestLatestAdapter);
                    contestLatestAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    noDataFoundTv.setVisibility(View.VISIBLE);
                }

            }
        });
        searchEtv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence newText, int start, int before, int count) {
                try {
                    if (newText.length() != 0) {
                        if (getResultlist.size() > 0) {
                            contestLatestAdapter.getFilter().filter(searchEtv.getText().toString());
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
                            contestLatestAdapter = new RecentChatAdapter(RecentChatActivity.this, getResultlist);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(RecentChatActivity.this);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(contestLatestAdapter);
                            contestLatestAdapter.notifyDataSetChanged();
                            mSwipeRefreshLayout.setRefreshing(false);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            noDataFoundTv.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                RecentChat_UserList();
            } catch (NullPointerException e) {
            }
        }
    };


    private void RecentChat_UserList() {
        try {
            if (NetworkUtil.isNetworkAvailable(RecentChatActivity.this)) {
                fab.setVisibility(View.VISIBLE);
                final Dialog materialDialog = ErrorMessage.initProgressDialog(RecentChatActivity.this);
                ErrorMessage.E("RecentChat_UserList" + UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id() + ">>" + Check);
                Call<ResponseBody> call = AppConfig.getLoadInterface().RecentChatList(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id(), Check);
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
                                    ErrorMessage.E("recent chat  before" + objectb.toString());

                                    if (objectb.getString("error_code").equals("200")) {
                                        JSONObject jsonObject12 = objectb.getJSONObject("result");
                                       /* otherID = jsonObject12.getString("user_id");
                                        Username = jsonObject12.getString("user_name");
                                        img = jsonObject12.getString("profile_img");*/
                                        JSONArray jsonArray = jsonObject12.getJSONArray("list_types");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            RecentModel recentModel = new RecentModel();
                                            recentModel.setUser_id(jsonObject1.getString("user_id"));
                                            recentModel.setUsername(jsonObject1.getString("username"));
                                            recentModel.setUser_profile_pic(jsonObject1.getString("profile_image"));
                                            recentModel.setRole(Check);
                                            /*if (Check.equals("teacher")) {
                                                recentModel.setRole(jsonObject1.getString("receiver_role"));
                                            } else {
                                                recentModel.setRole(jsonObject1.getString("sender_role"));
                                            }*/
                                            recentModel.setUnread_msg(jsonObject1.getString("unread_count"));
                                            recentModel.setLast_message(jsonObject1.getString("message"));
                                            recentModel.setCreated_at(jsonObject1.getString("created_at"));
                                           /* recentModel.setEmail(jsonObject1.getString("email"));
                                            recentModel.setLast_message(jsonObject1.getString("message"));
                                            recentModel.setCreated_at(jsonObject1.getString("created_at"));
                                            recentModel.setUnread_msg(jsonObject1.getString("unread_msg"));*/
                                            getResultlist.add(recentModel);
                                        }
                                        if (getResultlist.size() > 0) {
                                            noDataFoundTv.setVisibility(View.GONE);
                                            recyclerView.setVisibility(View.VISIBLE);
                                            contestLatestAdapter = new RecentChatAdapter(RecentChatActivity.this, getResultlist);
                                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(RecentChatActivity.this);
                                            recyclerView.setLayoutManager(mLayoutManager);
                                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                                            recyclerView.setAdapter(contestLatestAdapter);
                                            contestLatestAdapter.notifyDataSetChanged();
                                            mSwipeRefreshLayout.setRefreshing(false);
                                        } else {
                                            noDataFoundTv.setVisibility(View.VISIBLE);
                                            recyclerView.setVisibility(View.GONE);
                                        }
                                        //  hideProgressDialog();
                                    } else if (objectb.getString("status").equalsIgnoreCase("410")) {
                                        ErrorMessage.T(RecentChatActivity.this, "Your Account is Inactive by Admin.");
                                        ErrorMessage.I_clear(RecentChatActivity.this, LoginActivity.class, null);

                                    } else {
                                        mSwipeRefreshLayout.setRefreshing(false);
                                        materialDialog.dismiss();
                                        ErrorMessage.T(RecentChatActivity.this, objectb.getString("message"));
                                        noDataFoundTv.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //  hideProgressDialog();
                                    materialDialog.dismiss();
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    noDataFoundTv.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    //  hideProgressDialog();
                                    materialDialog.dismiss();
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    noDataFoundTv.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }


                            } else {
                                materialDialog.dismiss();
                                ErrorMessage.T(RecentChatActivity.this, "response error");
                                noDataFoundTv.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            //  hideProgressDialog();
                            materialDialog.dismiss();
                            mSwipeRefreshLayout.setRefreshing(false);
                            noDataFoundTv.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                        materialDialog.dismiss();
                        mSwipeRefreshLayout.setRefreshing(false);
                        noDataFoundTv.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                });
            } else {
                fab.setVisibility(View.GONE);
            }
        } catch (Exception e) {
        }

    }


    @Override
    public void onRefresh() {
        RecentChat_UserList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        RecentChat_UserList();
    }

    /*@OnClick(R.id.retry_btn)
    public void onClick() {
         if (Check.equals("student") || Check.contains("student")){
                // RecentChat_UserList();
                RecentTeacherList();
            }else  if (Check.equals("teacher") || Check.contains("teacher")){
                // RecentChat_UserList();
                RecentStudentList();
            }

    }*/
}

