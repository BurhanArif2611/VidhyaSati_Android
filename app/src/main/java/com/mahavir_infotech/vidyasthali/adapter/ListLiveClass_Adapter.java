package com.mahavir_infotech.vidyasthali.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.AppConfig;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.Utility.NetworkUtil;
import com.mahavir_infotech.vidyasthali.activity.Teacher.Live_Class.ADD_LiveClassActivity;
import com.mahavir_infotech.vidyasthali.models.ListLiveClass_Models.ListSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListLiveClass_Adapter extends RecyclerView.Adapter<ListLiveClass_Adapter.ViewHolder> {

    Context activity;
    List<ListSession> response_list;


    public ListLiveClass_Adapter(Context activity, List<ListSession> response) {
        this.activity = activity;
        this.response_list = response;
    }

    @NonNull
    @Override
    public ListLiveClass_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_student_syllabus_adapter, parent, false);
        ListLiveClass_Adapter.ViewHolder viewHolder = new ListLiveClass_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListLiveClass_Adapter.ViewHolder holder, final int position) {
        ListSession product = response_list.get(position);
        holder.title_tv.setText(product.getTitle());

        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
        Date d = null;
        //  ErrorMessage.E("Date"+jsonObject.getString("lastModified"));
        try {
            // output.setTimeZone(TimeZone.getTimeZone("IMP"));
            // input.setTimeZone(TimeZone.getTimeZone("IMP"));
            d = input.parse(product.getSessionDate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String formatted = output.format(d);
        holder.created_date_tv.setText(formatted + " " + product.getSessionTime());

        holder.view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.view_btn.getText().toString().equals("View")) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ALL_Data", product);
                    bundle.putString("Check", "Update");
                    ErrorMessage.I(activity, ADD_LiveClassActivity.class, bundle);
                }else {
                    Logout_PopUP(product.getSessionId(),position);
                }


            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.view_btn.getText().toString().equals("View")) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ALL_Data", product);
                    bundle.putString("Check", "Update");
                    ErrorMessage.I(activity, ADD_LiveClassActivity.class, bundle);
                }else {
                    Logout_PopUP(product.getSessionId(),position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.view_btn.setText("Delete");
                return false;
            }
        });

    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return response_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title_tv, created_date_tv;
        Button view_btn;


        public ViewHolder(View itemView) {
            super(itemView);
            view_btn = (Button) itemView.findViewById(R.id.view_btn);
            title_tv = (TextView) itemView.findViewById(R.id.title_tv);
            created_date_tv = (TextView) itemView.findViewById(R.id.created_date_tv);

        }
    }
    public void Logout_PopUP(String session_id,int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.alert_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final Button submit_btn = (Button) dialog.findViewById(R.id.submit_btn);
        final Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                Remove_list(session_id,position);
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();
    }
    private void Remove_list(String session_id, final int Position) {
        if (NetworkUtil.isNetworkAvailable(activity)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(activity);
            ErrorMessage.E("homework_id" + session_id);
            Call<ResponseBody> call = AppConfig.getLoadInterface().deleteSession(session_id);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("REmove" + jsonObject.toString());
                            materialDialog.dismiss();
                            if (jsonObject.getString("status").equals("1")) {
                                ErrorMessage.E("REmove" + jsonObject.toString());
                                response_list.remove(Position);
                                notifyDataSetChanged();
                                ErrorMessage.T(activity, jsonObject.getString("message"));

                            } else {
                                ErrorMessage.T(activity, jsonObject.getString("message"));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.T(activity, "Server Error");
                            materialDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(activity, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(activity, "Response Fail");
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(activity, activity.getString(R.string.no_internet));
        }
    }
}
