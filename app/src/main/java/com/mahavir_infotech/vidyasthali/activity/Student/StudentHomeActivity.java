package com.mahavir_infotech.vidyasthali.activity.Student;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.gson.Gson;
import com.mahavir_infotech.vidyasthali.BuildConfig;
import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.AppConfig;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.Utility.NetworkUtil;
import com.mahavir_infotech.vidyasthali.activity.SelectUserTypeActivity;
import com.mahavir_infotech.vidyasthali.activity.SupportActivity;
import com.mahavir_infotech.vidyasthali.activity.Teacher.AboutActivity;
import com.mahavir_infotech.vidyasthali.activity.Teacher.AttendenceListActivity;
import com.mahavir_infotech.vidyasthali.activity.Teacher.Contact_USActivity;
import com.mahavir_infotech.vidyasthali.activity.Teacher.EventActivity;
import com.mahavir_infotech.vidyasthali.activity.Teacher.GelleryActivity;
import com.mahavir_infotech.vidyasthali.activity.Teacher.LeaveListActivity;
import com.mahavir_infotech.vidyasthali.activity.Teacher.ListHomeWorkActivity;
import com.mahavir_infotech.vidyasthali.activity.Teacher.Live_Class.ListLiveClassActivity;
import com.mahavir_infotech.vidyasthali.activity.Teacher.MonthlyPerformanceSummeryActivity;
import com.mahavir_infotech.vidyasthali.activity.Teacher.NoticeBoardActivity;
import com.mahavir_infotech.vidyasthali.activity.Teacher.NotificationListActivity;
import com.mahavir_infotech.vidyasthali.activity.Teacher.StudyMaterial.SelectMaterialTypeActivity;
import com.mahavir_infotech.vidyasthali.activity.UpdateProfileActivity;
import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentHomeActivity extends AppCompatActivity {
    @BindView(R.id.drawer_img_btn)
    ImageButton drawerImgBtn;
    @BindView(R.id.notificans_btn)
    ImageButton notificansBtn;
    @BindView(R.id.relative_layout)
    RelativeLayout relativeLayout;
    @BindView(R.id.badge_notification_1)
    TextView badgeNotification1;
    @BindView(R.id.badge_layout1)
    RelativeLayout badgeLayout1;

    @BindView(R.id.imageView)
    CircleImageView imageView;
    @BindView(R.id.user_name_tv)
    TextView userNameTv;
    @BindView(R.id.user_email_tv)
    TextView userEmailTv;
    @BindView(R.id.profile_tv)
    TextView profileTv;
    @BindView(R.id.about_tv)
    TextView aboutTv;
    @BindView(R.id.terms_condition_tv)
    TextView termsConditionTv;
    @BindView(R.id.share_tv)
    TextView shareTv;
    @BindView(R.id.logoutItemNav)
    TextView logoutItemNav;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.top_layout)
    LinearLayout topLayout;
    @BindView(R.id.notice_board_cardview)
    CardView noticeBoardCardview;
    @BindView(R.id.attendence_cardview)
    CardView attendenceCardview;
    @BindView(R.id.leave_cardview)
    CardView leaveCardview;
    @BindView(R.id.dairy_cardview)
    CardView dairyCardview;
    @BindView(R.id.add_home_work_cardview)
    CardView addHomeWorkCardview;
    @BindView(R.id.syllabus_cardview)
    CardView syllabusCardview;
    @BindView(R.id.time_table_cardview)
    CardView timeTableCardview;
    @BindView(R.id.event_cardview)
    CardView eventCardview;
    @BindView(R.id.gallery_cardview)
    CardView galleryCardview;
    @BindView(R.id.contact_us_tv)
    TextView contactUsTv;
    @BindView(R.id.live_class_cardview)
    CardView liveClassCardview;
    @BindView(R.id.chat_layout)
    LinearLayout chatLayout;
    @BindView(R.id.live_session_count_tv)
    TextView liveSessionCountTv;
    @BindView(R.id.home_work_count)
    TextView homeWorkCount;
    @BindView(R.id.recent_chat_count_tv)
    TextView recentChatCountTv;
    @BindView(R.id.monthly_performance_cardview)
    CardView monthlyPerformanceCardview;
    @BindView(R.id.support_tv)
    TextView supportTv;
    @BindView(R.id.result_cardview)
    CardView resultCardview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
        ButterKnife.bind(this);
        if (UserProfileHelper.getInstance().getUserProfileModel().size() > 0) {

            userNameTv.setText(UserProfileHelper.getInstance().getUserProfileModel().get(0).getDisplayName());
            userEmailTv.setText(UserProfileHelper.getInstance().getUserProfileModel().get(0).getEmaiiId());
            if (UserProfileHelper.getInstance().getUserProfileModel().get(0).getProfile_pic() != null && !UserProfileHelper.getInstance().getUserProfileModel().get(0).getProfile_pic().equals("")) {
                Picasso.with(StudentHomeActivity.this).load(UserProfileHelper.getInstance().getUserProfileModel().get(0).getProfile_pic()).placeholder(R.drawable.ic_defult_user).into(imageView);
            }
            if (UserProfileHelper.getInstance().getUserProfileModel().get(0).getRole().equals("parent")) {
                monthlyPerformanceCardview.setVisibility(View.VISIBLE);
            }
        }
        Get_Count();
    }

    @OnClick({R.id.drawer_img_btn, R.id.profile_tv, R.id.about_tv, R.id.terms_condition_tv, R.id.share_tv, R.id.logoutItemNav, R.id.chat_layout, R.id.attendence_cardview, R.id.leave_cardview, R.id.dairy_cardview, R.id.add_home_work_cardview, R.id.syllabus_cardview, R.id.time_table_cardview, R.id.event_cardview, R.id.gallery_cardview, R.id.contact_us_tv, R.id.live_class_cardview, R.id.notice_board_cardview, R.id.notificans_btn, R.id.monthly_performance_cardview, R.id.support_tv, R.id.result_cardview})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.drawer_img_btn:
                drawerLayout.isDrawerOpen(GravityCompat.START);
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.profile_tv:
                drawerLayout.closeDrawers();
                ErrorMessage.I(StudentHomeActivity.this, UpdateProfileActivity.class, null);
                break;
            case R.id.support_tv:
                drawerLayout.closeDrawers();
                ErrorMessage.I(StudentHomeActivity.this, SupportActivity.class, null);
                break;
            case R.id.about_tv:
                Bundle bundle = new Bundle();
                bundle.putString("Check", "About");
                ErrorMessage.I(StudentHomeActivity.this, AboutActivity.class, bundle);
                break;
            case R.id.terms_condition_tv:
                Bundle bundle1 = new Bundle();
                bundle1.putString("Check", "Terms");
                ErrorMessage.I(StudentHomeActivity.this, AboutActivity.class, bundle1);
                break;
            case R.id.monthly_performance_cardview:
                Bundle bundle12 = new Bundle();
                bundle12.putString("Check", "Student");
                ErrorMessage.I(StudentHomeActivity.this, MonthlyPerformanceSummeryActivity.class, bundle12);
                break;
            case R.id.time_table_cardview:
                ErrorMessage.I(StudentHomeActivity.this, ListTimeTableActivity.class, null);
                break;
            case R.id.notificans_btn:
                ErrorMessage.I(StudentHomeActivity.this, NotificationListActivity.class, null);
                break;
            case R.id.notice_board_cardview:
                ErrorMessage.I(StudentHomeActivity.this, NoticeBoardActivity.class, null);
                break;
            case R.id.result_cardview:
                ErrorMessage.I(StudentHomeActivity.this, ResultActivity.class, null);
                break;
            case R.id.share_tv:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "HIND MPPSC");
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
                break;
            case R.id.logoutItemNav:

                Logout_PopUP();
                break;
            case R.id.chat_layout:
                Bundle bundle7 = new Bundle();
                ErrorMessage.E("" + UserProfileHelper.getInstance().getUserProfileModel().get(0).getRole());
                bundle7.putString("Check", UserProfileHelper.getInstance().getUserProfileModel().get(0).getRole());
                ErrorMessage.I(StudentHomeActivity.this, RecentChatActivity.class, bundle7);
                break;
            case R.id.add_home_work_cardview:
                Bundle bundle3 = new Bundle();
                bundle3.putString("Check", "Student");
                ErrorMessage.I(StudentHomeActivity.this, ListHomeWorkActivity.class, bundle3);
                break;
            case R.id.syllabus_cardview:
                Bundle bundle2 = new Bundle();
                bundle2.putString("Check", "Student");
                ErrorMessage.I(StudentHomeActivity.this, SelectMaterialTypeActivity.class, bundle2);
                break;
            case R.id.attendence_cardview:
                Bundle bundle4 = new Bundle();
                bundle4.putString("Check", "Student");
                ErrorMessage.I(StudentHomeActivity.this, AttendenceListActivity.class, bundle4);
                break;
            case R.id.leave_cardview:
                Bundle bundle5 = new Bundle();
                bundle5.putString("Check", "Student");
                ErrorMessage.I(StudentHomeActivity.this, LeaveListActivity.class, bundle5);
                break;
            case R.id.dairy_cardview:
                break;
            case R.id.event_cardview:
                ErrorMessage.I(StudentHomeActivity.this, EventActivity.class, null);
                break;
            case R.id.gallery_cardview:
                ErrorMessage.I(StudentHomeActivity.this, GelleryActivity.class, null);
                break;
            case R.id.contact_us_tv:
                ErrorMessage.I(StudentHomeActivity.this, Contact_USActivity.class, null);
                break;
            case R.id.live_class_cardview:
                Bundle bundle6 = new Bundle();
                bundle6.putString("Check", "Student");
                ErrorMessage.I(StudentHomeActivity.this, ListLiveClassActivity.class, bundle6);
                break;
        }
    }

    public void Logout_PopUP() {
        final Dialog dialog = new Dialog(StudentHomeActivity.this);
        dialog.setContentView(R.layout.logout_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final Button submit_btn = (Button) dialog.findViewById(R.id.submit_btn);
        final Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                UserProfileHelper.getInstance().delete();
                ErrorMessage.I_clear(StudentHomeActivity.this, SelectUserTypeActivity.class, null);
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        exitApplication();
        /* super.onBackPressed();*/
    }

    private void exitApplication() {
        new AlertDialog.Builder(this).setIcon(R.drawable.ic_launcher).setTitle("VIDYASTHALI").setMessage("Do you want to exit?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }

        }).setNegativeButton("No", null).show();

    }

    /*"{
        ""status"": 1,
        ""error_code"": 200,
        ""error"": 0,
        ""error_line"": 0,
        ""message"": ""count success"",
        ""result"": {
            ""chat_count"": ""0"",
            ""live_session"": ""0"",
            ""homework"": ""0""
        }
    }"*/
    public void Get_Count() {
        if (NetworkUtil.isNetworkAvailable(StudentHomeActivity.this)) {
            // final Dialog materialDialog = ErrorMessage.initProgressDialog(StudentHomeActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().dashboard_counts(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id(), UserProfileHelper.getInstance().getUserProfileModel().get(0).getRole());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            //     materialDialog.dismiss();
                            Gson gson = new Gson();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("Get_Syllabus" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                                liveSessionCountTv.setText(jsonObject1.getString("live_session") + "\n" + "Live Session's");
                                homeWorkCount.setText(jsonObject1.getString("homework") + "\n" + "Today's Home Work");
                                recentChatCountTv.setText(jsonObject1.getString("chat_count") + "\n" + "Chat");
                            } else {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("productdetail error" + e.toString());
                            //  ErrorMessage.T(StudentHomeActivity.this, "Server Error");
                            // materialDialog.dismiss();

                        } catch (Exception e) {
                            e.printStackTrace();
                            // materialDialog.dismiss();

                        }


                    } else {
                        // ErrorMessage.T(StudentHomeActivity.this, "Response not successful");
                        //materialDialog.dismiss();

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // ErrorMessage.T(StudentHomeActivity.this, "Response Fail");
                    // materialDialog.dismiss();

                }
            });

        } else {
            ErrorMessage.T(StudentHomeActivity.this, getResources().getString(R.string.no_internet));

        }
    }

    @Override
    protected void onResume() {
        userNameTv.setText(UserProfileHelper.getInstance().getUserProfileModel().get(0).getDisplayName());
        userEmailTv.setText(UserProfileHelper.getInstance().getUserProfileModel().get(0).getEmaiiId());
        ErrorMessage.E("profile" + UserProfileHelper.getInstance().getUserProfileModel().get(0).getProfile_pic());
        if (UserProfileHelper.getInstance().getUserProfileModel().get(0).getProfile_pic() != null && !UserProfileHelper.getInstance().getUserProfileModel().get(0).getProfile_pic().equals("")) {
            Picasso.with(StudentHomeActivity.this).load(UserProfileHelper.getInstance().getUserProfileModel().get(0).getProfile_pic()).placeholder(R.drawable.ic_defult_user).into(imageView);
        }
        Get_Count();
        super.onResume();
    }

}
