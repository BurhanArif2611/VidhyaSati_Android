package com.mahavir_infotech.vidyasthali.activity.Student;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.mahavir_infotech.vidyasthali.Firebase.Config;
import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.AppConfig;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.Utility.NetworkUtil;
import com.mahavir_infotech.vidyasthali.activity.SelectUserTypeActivity;
import com.mahavir_infotech.vidyasthali.adapter.ChatingAdapter;
import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;
import com.mahavir_infotech.vidyasthali.models.ChatList_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    EmojIconActions emojIcon;
    EmojiconEditText emojiconEditText;
    ImageView emojiImageView, camera;
    View rootView;
    FloatingActionButton send;
    RecyclerView chating_recycler_view;

    String otherID = "", role = "";
    ArrayList<ChatList_Model> chatList_models = new ArrayList<>();
    ImageView back_btn;
    CircleImageView profile_img;
    TextView user_name;
    private ChatingAdapter generateBillAdapter;
    private String s;
    ImageView back, delete_imag;

    Dialog materialDialog;
    LinearLayout sendcomment_layout;
    ArrayList<String> stringArrayList = new ArrayList<>();
    public String messageIds = "";
    String is_block = "1";
    Calendar calander;
    SimpleDateFormat simpleDateFormat, mdformat;
    private String passwordCurrent = "";

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public void BloackPopUP(String check) {
        final Dialog dialog = new Dialog(ChatActivity.this);
        dialog.setContentView(R.layout.confirmation_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // get the Refferences of views

        final Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        final Button continue_video = (Button) dialog.findViewById(R.id.continue_video);
        final TextView main_content_tv = (TextView) dialog.findViewById(R.id.main_content_tv);
        if (check.equals("1")) {
            main_content_tv.setText("Are you Sure ! you want to Block");
            continue_video.setText("Block");
        } else if (check.equals("2")) {
            main_content_tv.setText("Are you Sure ! you want to UnBlock");
            continue_video.setText("UnBlock");
        }
        continue_video.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                Bloack_User();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void confirmClearPopUP() {
        final Dialog dialog = new Dialog(ChatActivity.this);
        dialog.setContentView(R.layout.confirmation_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // get the Refferences of views

        final Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        final Button continue_video = (Button) dialog.findViewById(R.id.continue_video);
        final TextView main_content_tv = (TextView) dialog.findViewById(R.id.main_content_tv);
        main_content_tv.setText("Are you sure you want to clear messages in this chat?");
        continue_video.setText("Clear");
        continue_video.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                clearChat();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.show();

    }


    private void clearChat() {
        if (NetworkUtil.isNetworkAvailable(ChatActivity.this)) {
            materialDialog = ErrorMessage.initProgressDialog(ChatActivity.this);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("req_from", UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
            jsonObject.addProperty("req_to", otherID);

            ErrorMessage.E("clearChat request" + jsonObject);

           /* MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<ResponseBody> call = apiService.clearChat(jsonObject);*/
            Call<ResponseBody> call = AppConfig.getLoadInterface().clearChat(jsonObject);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        String str = null;
                        try {
                            str = response.body().string();
                            System.out.println("---------- clearChat :" + str.toString());
                            JSONObject objectb = new JSONObject(str);
                            if (objectb.getString("status").equalsIgnoreCase("200")) {
                                materialDialog.dismiss();
                                chatList_models.clear();
                                ErrorMessage.T(ChatActivity.this, objectb.getString("message"));
                            } else {
                                materialDialog.dismiss();
                                ErrorMessage.T(ChatActivity.this, objectb.getString("message"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                        } catch (Exception e) {
                            materialDialog.dismiss();
                        }


                        //
                    } else {
                        materialDialog.dismiss();
                        ErrorMessage.T(ChatActivity.this, "Server Response not Getting");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    materialDialog.dismiss();
                }
            });

        }

    }

    private void Bloack_User() {
        if (NetworkUtil.isNetworkAvailable(ChatActivity.this)) {
            materialDialog = ErrorMessage.initProgressDialog(ChatActivity.this);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("sender_id", UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
            jsonObject.addProperty("receiver_id", otherID);

            ErrorMessage.E("Bloack_User request" + jsonObject);
            Call<ResponseBody> call = AppConfig.getLoadInterface().blockUser(jsonObject);
            /*MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<ResponseBody> call = apiService.blockUser(jsonObject);*/
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        try {
                            JSONObject objectb = new JSONObject(response.body().string());
                            ErrorMessage.E("Bloack_User" + objectb.toString());
                            if (objectb.getString("error_code").equalsIgnoreCase("200")) {
                                materialDialog.dismiss();
                                is_block = objectb.getString("is_block");
                                ErrorMessage.T(ChatActivity.this, objectb.getString("message"));
                                if (is_block.equals("2")) {
                                    sendcomment_layout.setVisibility(View.GONE);
                                } else if (is_block.equals("1")) {
                                    sendcomment_layout.setVisibility(View.VISIBLE);
                                }
                            } else {
                                materialDialog.dismiss();
                                ErrorMessage.T(ChatActivity.this, objectb.getString("message"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                        } catch (Exception e) {
                            materialDialog.dismiss();
                        }


                        //
                    } else {
                        materialDialog.dismiss();
                        ErrorMessage.T(ChatActivity.this, "Server Response not Getting");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    materialDialog.dismiss();
                }
            });

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        materialDialog.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        calander = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm:a");
        mdformat = new SimpleDateFormat("yyyy-MM-dd");
        materialDialog = ErrorMessage.initProgressDialog(ChatActivity.this);
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // back_btn = (ImageView) findViewById(R.id.back_btn);
        profile_img = (CircleImageView) findViewById(R.id.profile_img);
        user_name = (TextView) findViewById(R.id.user_name);
        back = (ImageView) findViewById(R.id.back);
        delete_imag = (ImageView) findViewById(R.id.delete_imag);
        sendcomment_layout = (LinearLayout) findViewById(R.id.sendcomment_layout);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });




        /*   final Dialog dialog = new Dialog(NovelDetailsActivity.this);
        dialog.setContentView(R.layout.donate_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);*/


        emojiconEditText = (EmojiconEditText) findViewById(R.id.editEmojicon);
        emojiImageView = (ImageView) findViewById(R.id.emojiIcon);
        camera = (ImageView) findViewById(R.id.camera);
        send = (FloatingActionButton) findViewById(R.id.send);
        rootView = findViewById(R.id.root_view);
        emojIcon = new EmojIconActions(ChatActivity.this, rootView, emojiconEditText, emojiImageView);

        emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("", "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("", "Keyboard closed");
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emojiconEditText.getText().toString().length() == 0) {
                    ErrorMessage.T(ChatActivity.this, "Please Enter a value.");
                } else {
                    Send_Message();
                }
            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("check").equals("")) {
                otherID = bundle.getString("otherID");
                role = bundle.getString("role");
                Glide.with(getApplicationContext()).load(bundle.getString("img")).into(profile_img);
                user_name.setText(bundle.getString("Username"));
            } else if (bundle.getString("check").equals("Notification")) {
                otherID = bundle.getString("sender_id");
                role = bundle.getString("sender_role");
                Glide.with(getApplicationContext()).load(bundle.getString("reciver_pic")).into(profile_img);
                user_name.setText(bundle.getString("receiver_name"));
            }
        }
       /* mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ChatActivity.this, OtherProfileActivity.class);
                intent1.putExtra("Other_id",otherID);
                startActivity(intent1);
            }
        });*/
        chating_recycler_view = (RecyclerView) findViewById(R.id.chating_recycler_view);
        Get_Message();
      /*  back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(Config.Chating));
        delete_imag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete_Selected_Item();
            }
        });

    }

    private BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                ChatList_Model chatList_model = new ChatList_Model();
                chatList_model.setId("");
                chatList_model.setReq_from(intent.getStringExtra("sender_id"));
                chatList_model.setReq_to(intent.getStringExtra("receiver_id"));
                chatList_model.setMsg_type("");
                chatList_model.setMessage(intent.getStringExtra("message"));
                chatList_model.setCreated_at("just");
                chatList_model.setDate(mdformat.format(calander.getTime()));
                chatList_model.setTime(simpleDateFormat.format(calander.getTime()));
                chatList_model.setIs_read("read");
                chatList_models.add(chatList_model);
                Log.e("receiver", "Got message: " + intent.getStringExtra("message"));

                ChatingAdapter generateBillAdapter = new ChatingAdapter(ChatActivity.this, chatList_models);
                LinearLayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this, LinearLayoutManager.VERTICAL, false);
                chating_recycler_view.setLayoutManager(layoutManager);
                chating_recycler_view.setItemAnimator(new DefaultItemAnimator());
                chating_recycler_view.setAdapter(generateBillAdapter);
                chating_recycler_view.scrollToPosition(chatList_models.size() - 1);
                generateBillAdapter.notifyItemInserted(chatList_models.size() - 1);

            } catch (Exception e) {
            }


        }
    };


    private void Send_Message() {
        // showProgressDialog();

        if (NetworkUtil.isNetworkAvailable(ChatActivity.this)) {
            s = emojiconEditText.getText().toString();
            /*byte[] data = new byte[0];
            try {
                data = s.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String base64String = Base64.encodeToString(data, Base64.DEFAULT);*/

            ChatList_Model chatList_model = new ChatList_Model();
            chatList_model.setId("");
            chatList_model.setReq_from(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
            chatList_model.setReq_to(otherID);
            chatList_model.setMsg_type("send");
            chatList_model.setMessage(s);
            chatList_model.setCreated_at("just");
            chatList_model.setDate(mdformat.format(calander.getTime()));
            chatList_model.setIs_read("read");
            chatList_model.setTime(simpleDateFormat.format(calander.getTime()));
            chatList_models.add(chatList_model);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            generateBillAdapter = new ChatingAdapter(ChatActivity.this, chatList_models);
            chating_recycler_view.setLayoutManager(mLayoutManager);

            chating_recycler_view.setItemAnimator(new DefaultItemAnimator());
            chating_recycler_view.setAdapter(generateBillAdapter);
            chating_recycler_view.scrollToPosition(chatList_models.size() - 1);
            generateBillAdapter.notifyItemInserted(chatList_models.size() - 1);

            emojiconEditText.setText("");

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("sender_id", UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
            jsonObject.addProperty("receiver_id", otherID);
            jsonObject.addProperty("message", s);
            jsonObject.addProperty("sender_role", UserProfileHelper.getInstance().getUserProfileModel().get(0).getRole());
            if (UserProfileHelper.getInstance().getUserProfileModel().get(0).getRole().equals("teacher")) {
                jsonObject.addProperty("receiver_role", "student");//role
            }else {
                jsonObject.addProperty("receiver_role", "teacher");
            }
            /*jsonObject.addProperty("receiver_role", role);*/
            ErrorMessage.E("chat Request" + jsonObject.toString());

            // MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<ResponseBody> call = AppConfig.getLoadInterface().chatUsers(jsonObject);
            //  Call<ResponseBody> call = apiService.chatUsers(jsonObject);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        ErrorMessage.E("chat Request" + response.code());
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                ErrorMessage.E("chat Request" + jsonObject.toString());
                                if (jsonObject.getString("error_code").equals("200")) {
                                    emojiconEditText.setText("");

                                } else if (jsonObject.getString("status").equalsIgnoreCase("410")) {
                                    ErrorMessage.T(ChatActivity.this, "Your Account is Inactive by Admin.");
                                    ErrorMessage.I_clear(ChatActivity.this, SelectUserTypeActivity.class, null);
                                } else {
                                    ErrorMessage.T(ChatActivity.this, jsonObject.getString("message"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();

                                //   hideProgressDialog();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        // hideProgressDialog();
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    ErrorMessage.E("onFailure" + t.toString());
                }
            });


        } else {
            ErrorMessage.T(ChatActivity.this, ChatActivity.this.getString(R.string.internetnotfound));
        }
    }

    private void Get_Message() {
        // showProgressDialog();
        if (NetworkUtil.isNetworkAvailable(ChatActivity.this)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("sender_id", UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
            jsonObject.addProperty("receiver_id", otherID);
            jsonObject.addProperty("sender_role", UserProfileHelper.getInstance().getUserProfileModel().get(0).getRole());
            if (UserProfileHelper.getInstance().getUserProfileModel().get(0).getRole().equals("teacher")) {
                jsonObject.addProperty("receiver_role", "student");//role
            }else {
                jsonObject.addProperty("receiver_role", "teacher");
            }
            ErrorMessage.E("chat Request" + jsonObject);

           /* MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<ResponseBody> call = apiService.ChatList(jsonObject);*/
            Call<ResponseBody> call = AppConfig.getLoadInterface().ChatList(jsonObject);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        materialDialog.dismiss();
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                ErrorMessage.E("if value on response" + jsonObject.toString());
                                if (jsonObject.getString("error_code").equals("200")) {
                                   /* is_block=jsonObject.getString("is_block");
                                    if (jsonObject.getString("is_block").equals("2")) {
                                        sendcomment_layout.setVisibility(View.GONE);
                                    } else if(jsonObject.getString("is_block").equals("1")){
                                        sendcomment_layout.setVisibility(View.VISIBLE);
                                    }*/
                                    chatList_models.clear();
                                    JSONObject jsonObject12 = jsonObject.getJSONObject("result");
                                    JSONArray jsonArray = jsonObject12.getJSONArray("list_types");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        ChatList_Model chatList_model = new ChatList_Model();
                                        chatList_model.setId(jsonObject1.getString("id"));
                                        chatList_model.setReq_from(jsonObject1.getString("sender_id"));
                                        chatList_model.setReq_to(jsonObject1.getString("receiver_id"));
                                        //chatList_model.setMsg_type(jsonObject1.getString("msg_type"));
                                        chatList_model.setMessage(jsonObject1.getString("message"));
                                        chatList_model.setDate(jsonObject1.getString("date"));
                                        chatList_model.setTime(jsonObject1.getString("time"));
                                        chatList_model.setIs_read(jsonObject1.getString("is_read"));
                                        chatList_model.setCreated_at(jsonObject1.getString("created_at"));
                                        chatList_models.add(chatList_model);
                                    }
                                    ErrorMessage.E("chatList_models" + chatList_models.size());
                                    if (chatList_models.size() > 0) {
                                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                        generateBillAdapter = new ChatingAdapter(ChatActivity.this, chatList_models);
                                        chating_recycler_view.setLayoutManager(mLayoutManager);
                                        /*chating_recycler_view.setItemAnimator(new DefaultItemAnimator());*/
                                        chating_recycler_view.setAdapter(generateBillAdapter);
                                        chating_recycler_view.scrollToPosition(chating_recycler_view.getAdapter().getItemCount() - 1);
                                        chating_recycler_view.smoothScrollToPosition(generateBillAdapter.getItemCount() - 1);
                                    }
                                } else if (jsonObject.getString("status").equalsIgnoreCase("410")) {
                                    ErrorMessage.T(ChatActivity.this, "Your Account is Inactive by Admin.");
                                    ErrorMessage.I_clear(ChatActivity.this, SelectUserTypeActivity.class, null);

                                } else {
                                    //ErrorMessage.T(ChatActivity.this, jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                materialDialog.dismiss();
                                //   hideProgressDialog();
                            } catch (Exception e) {
                                materialDialog.dismiss();
                            }


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        materialDialog.dismiss();
                        // hideProgressDialog();
                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    materialDialog.dismiss();
                    ErrorMessage.E("onFailure" + t.toString());
                }
            });


        } else {
            ErrorMessage.T(ChatActivity.this, ChatActivity.this.getString(R.string.internetnotfound));
        }
    }

    public void selectedItem_onDelete(String message_id) {
        stringArrayList.add(message_id);
        if (stringArrayList.size() > 0) {
            delete_imag.setVisibility(View.VISIBLE);
        } else {
            delete_imag.setVisibility(View.GONE);
        }
    }

    private void Delete_Selected_Item() {

        if (NetworkUtil.isNetworkAvailable(ChatActivity.this)) {
            for (int i = 0; i < stringArrayList.size(); i++) {
                if (i == 0) {
                    messageIds = stringArrayList.get(i);
                } else {
                    messageIds = messageIds + "," + stringArrayList.get(i);
                }
            }
            materialDialog = ErrorMessage.initProgressDialog(ChatActivity.this);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("user_id", UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
            jsonObject.addProperty("messageIds", messageIds);

            ErrorMessage.E("Delete_Selected_Item request" + jsonObject);
            /* MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<ResponseBody> call = apiService.deleteMessagesByIds(jsonObject);*/
            Call<ResponseBody> call = AppConfig.getLoadInterface().deleteMessagesByIds(jsonObject);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject objectb = new JSONObject(response.body().string());
                            ErrorMessage.E("Delete_Selected_Item" + objectb.toString());
                            if (objectb.getString("error_code").equalsIgnoreCase("200")) {
                                materialDialog.dismiss();
                                stringArrayList.clear();
                                delete_imag.setVisibility(View.GONE);
                                ErrorMessage.T(ChatActivity.this, objectb.getString("message"));
                                Get_Message();
                            } else {
                                materialDialog.dismiss();
                                ErrorMessage.T(ChatActivity.this, objectb.getString("message"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                        } catch (Exception e) {
                            materialDialog.dismiss();
                        }


                        //
                    } else {
                        materialDialog.dismiss();
                        ErrorMessage.T(ChatActivity.this, "Server Response not Getting");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    materialDialog.dismiss();
                }
            });

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.your_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            /*case android.R.id.home:
                finish();
                return true;*/
            case R.id.delete_all:
                confirmClearPopUP();
                return true;

            case R.id.bloack_user:
                BloackPopUP("1");

                return true;
            case R.id.report:
                Report_popup();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (Build.VERSION.SDK_INT > 11) {
            invalidateOptionsMenu();
            if (is_block.equals("2")) {
                menu.findItem(R.id.bloack_user).setTitle("UnBlock User");
            } else if (is_block.equals("1")) {
                menu.findItem(R.id.bloack_user).setTitle("Block User");
            }

        }
        return super.onPrepareOptionsMenu(menu);
    }

    public void Report_popup() {
        final Dialog dialog = new Dialog(ChatActivity.this);
        dialog.setContentView(R.layout.report_to_adminpopup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // get the Refferences of views

        final Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        final Button continue_video = (Button) dialog.findViewById(R.id.continue_video);
        final TextView main_content_tv = (TextView) dialog.findViewById(R.id.main_content_tv);
        final EditText etpassword = (EditText) dialog.findViewById(R.id.etpassword);

        LinearLayout linearlayout = (LinearLayout) dialog.findViewById(R.id.linearlayout);

        linearlayout.setVisibility(View.VISIBLE);

        continue_video.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                passwordCurrent = etpassword.getText().toString().trim();

                if (passwordCurrent.equalsIgnoreCase("")) {
                    etpassword.setError("Please enter your current password");
                } else {
                    dialog.dismiss();
                    ReportToAdmin(passwordCurrent);
                }
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void ReportToAdmin(String passwordCurrent) {
        if (NetworkUtil.isNetworkAvailable(ChatActivity.this)) {
            materialDialog = ErrorMessage.initProgressDialog(ChatActivity.this);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("user_id", UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
            jsonObject.addProperty("to_user_id", otherID);
            jsonObject.addProperty("report_msg", passwordCurrent);

            ErrorMessage.E("Delete_Selected_Item request" + jsonObject);
           /* MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<ResponseBody> call = apiService.ReportUser(jsonObject);*/
            Call<ResponseBody> call = AppConfig.getLoadInterface().ReportUser(jsonObject);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        String str = null;
                        try {
                            materialDialog.dismiss();
                            str = response.body().string();
                            System.out.println("---------- clearChat :" + str.toString());
                            JSONObject objectb = new JSONObject(str);
                            if (objectb.getString("status").equalsIgnoreCase("200")) {
                                ErrorMessage.T(ChatActivity.this, objectb.getString("message"));
                            } else {
                                ErrorMessage.T(ChatActivity.this, objectb.getString("message"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                        }

                    } else {
                        materialDialog.dismiss();
                        ErrorMessage.T(ChatActivity.this, "Server Response not Getting");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    materialDialog.dismiss();
                }
            });

        }

    }
}
