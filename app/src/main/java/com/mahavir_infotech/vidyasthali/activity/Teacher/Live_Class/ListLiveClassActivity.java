package com.mahavir_infotech.vidyasthali.activity.Teacher.Live_Class;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.AppConfig;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.Utility.NetworkUtil;
import com.mahavir_infotech.vidyasthali.activity.BaseActivity;
import com.mahavir_infotech.vidyasthali.adapter.ListLiveClass_Adapter;
import com.mahavir_infotech.vidyasthali.adapter.Student_LiveClass_Adapter;
import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;
import com.mahavir_infotech.vidyasthali.fragment.LiveFragment;
import com.mahavir_infotech.vidyasthali.models.ListLiveClass_Models.Example;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListLiveClassActivity extends BaseActivity {

    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.live_class_rcv)
    RecyclerView liveClassRcv;
    @BindView(R.id.no_data_tv)
    TextView noDataTv;
    @BindView(R.id.add_btn)
    FloatingActionButton addBtn;
    String Check = "";
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected int getContentResId() {
        return R.layout.activity_list_live_class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        titleTxt.setText("Upcoming Session");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Check = bundle.getString("Check");
            addBtn.setVisibility(View.GONE);
            /* Get_StudentLiveSession();*/
        } else {
            /* Get_LiveSession();*/
        }
        tabs.addTab(tabs.newTab().setText("UpComing Live Class's"));
        tabs.addTab(tabs.newTab().setText("Previous Live Class's"));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        MyTabAdapter adapter = new MyTabAdapter(ListLiveClassActivity.this, getSupportFragmentManager(), tabs.getTabCount());
        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @OnClick(R.id.add_btn)
    public void onClick() {
        ErrorMessage.I(ListLiveClassActivity.this, ADD_LiveClassActivity.class, null);

    }

   /* public void Get_LiveSession() {
        if (NetworkUtil.isNetworkAvailable(ListLiveClassActivity.this)) {
            ErrorMessage.E("Get_LiveSession" + UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ListLiveClassActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().listSessionByTeacherId(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
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
                                    noDataTv.setVisibility(View.GONE);
                                    liveClassRcv.setVisibility(View.VISIBLE);
                                    ListLiveClass_Adapter qtyListAdater = new ListLiveClass_Adapter(ListLiveClassActivity.this, example.getResult().getListSession());
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ListLiveClassActivity.this);
                                    liveClassRcv.setLayoutManager(mLayoutManager);
                                    liveClassRcv.setItemAnimator(new DefaultItemAnimator());
                                    liveClassRcv.setAdapter(qtyListAdater);
                                } else {
                                    noDataTv.setVisibility(View.VISIBLE);
                                    liveClassRcv.setVisibility(View.GONE);
                                }
                            } else {
                                noDataTv.setVisibility(View.VISIBLE);
                                liveClassRcv.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("productdetail error" + e.toString());
                            //  ErrorMessage.T(ListLiveClassActivity.this, "Server Error");
                            materialDialog.dismiss();
                            noDataTv.setVisibility(View.VISIBLE);
                            liveClassRcv.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                            noDataTv.setVisibility(View.VISIBLE);
                            liveClassRcv.setVisibility(View.GONE);
                        }
                    } else {
                        ErrorMessage.T(ListLiveClassActivity.this, "Response not successful");
                        materialDialog.dismiss();
                        noDataTv.setVisibility(View.VISIBLE);
                        liveClassRcv.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ListLiveClassActivity.this, "Response Fail");
                    materialDialog.dismiss();
                    noDataTv.setVisibility(View.VISIBLE);
                    liveClassRcv.setVisibility(View.GONE);
                }
            });

        } else {
            ErrorMessage.T(ListLiveClassActivity.this, getResources().getString(R.string.no_internet));
            noDataTv.setVisibility(View.VISIBLE);
            liveClassRcv.setVisibility(View.GONE);
        }
    }*/

   /* @Override
    protected void onResume() {
        ErrorMessage.E("Working" + "onResume");
        if (Check.equals("")) {
            Get_LiveSession();
        } else {
            Get_StudentLiveSession();
        }
        super.onResume();
    }*/

    /*  public void Get_StudentLiveSession() {
          if (NetworkUtil.isNetworkAvailable(ListLiveClassActivity.this)) {
              final Dialog materialDialog = ErrorMessage.initProgressDialog(ListLiveClassActivity.this);
              Call<ResponseBody> call = AppConfig.getLoadInterface().listSessionByClassid(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUserPhone());
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
                                      noDataTv.setVisibility(View.GONE);
                                      liveClassRcv.setVisibility(View.VISIBLE);
                                      Student_LiveClass_Adapter qtyListAdater = new Student_LiveClass_Adapter(ListLiveClassActivity.this, example.getResult().getListSession());
                                      RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ListLiveClassActivity.this);
                                      liveClassRcv.setLayoutManager(mLayoutManager);
                                      liveClassRcv.setItemAnimator(new DefaultItemAnimator());
                                      liveClassRcv.setAdapter(qtyListAdater);
                                  } else {
                                      noDataTv.setVisibility(View.VISIBLE);
                                      liveClassRcv.setVisibility(View.GONE);
                                  }
                              } else {
                                  noDataTv.setVisibility(View.VISIBLE);
                                  liveClassRcv.setVisibility(View.GONE);
                              }
                          } catch (JSONException e) {
                              e.printStackTrace();
                              ErrorMessage.E("productdetail error" + e.toString());
                              //  ErrorMessage.T(ListLiveClassActivity.this, "Server Error");
                              materialDialog.dismiss();
                              noDataTv.setVisibility(View.VISIBLE);
                              liveClassRcv.setVisibility(View.GONE);
                          } catch (Exception e) {
                              e.printStackTrace();
                              materialDialog.dismiss();
                              noDataTv.setVisibility(View.VISIBLE);
                              liveClassRcv.setVisibility(View.GONE);
                          }
                      } else {
                          ErrorMessage.T(ListLiveClassActivity.this, "Response not successful");
                          materialDialog.dismiss();
                          noDataTv.setVisibility(View.VISIBLE);
                          liveClassRcv.setVisibility(View.GONE);
                      }
                  }
  
                  @Override
                  public void onFailure(Call<ResponseBody> call, Throwable t) {
                      ErrorMessage.T(ListLiveClassActivity.this, "Response Fail");
                      materialDialog.dismiss();
                      noDataTv.setVisibility(View.VISIBLE);
                      liveClassRcv.setVisibility(View.GONE);
                  }
              });
  
          } else {
              ErrorMessage.T(ListLiveClassActivity.this, getResources().getString(R.string.no_internet));
              noDataTv.setVisibility(View.VISIBLE);
              liveClassRcv.setVisibility(View.GONE);
          }
      }*/
    public class MyTabAdapter extends FragmentPagerAdapter {
        private Context myContext;
        int totalTabs;

        public MyTabAdapter(Context context, FragmentManager fm, int totalTabs) {
            super(fm);
            myContext = context;
            this.totalTabs = totalTabs;
        }


        // this is for fragment tabs
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    LiveFragment homeFragment = new LiveFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Check", Check);
                    bundle.putString("short_by", "upcoming");
                    homeFragment.setArguments(bundle);
                    return homeFragment;
                case 1:
                    LiveFragment sportFragment = new LiveFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("Check", Check);
                    bundle1.putString("short_by", "previous");
                    sportFragment.setArguments(bundle1);
                    return sportFragment;
                default:
                    return null;
            }
        }

        // this counts total number of tabs
        @Override
        public int getCount() {
            return totalTabs;
        }
    }

}
