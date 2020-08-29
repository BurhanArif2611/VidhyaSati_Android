package com.mahavir_infotech.vidyasthali.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.AppConfig;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.Utility.NetworkUtil;
import com.mahavir_infotech.vidyasthali.activity.Teacher.ADD_TimeTableActivity;
import com.mahavir_infotech.vidyasthali.models.GetTimeTable.ListTimetable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetListTable_Adapter extends RecyclerView.Adapter<GetListTable_Adapter.ViewHolder> {

    Context activity;
    List<ListTimetable> response_list;

    public GetListTable_Adapter(Context activity, List<ListTimetable> response) {
        this.activity = activity;
        this.response_list = response;
    }

    @NonNull
    @Override
    public GetListTable_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_homework_adapter, parent, false);
        GetListTable_Adapter.ViewHolder viewHolder = new GetListTable_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GetListTable_Adapter.ViewHolder holder, final int position) {
        ListTimetable product = response_list.get(position);
        holder.class_name_tv.setText(product.getClassName());
        holder.subject_name_tv.setText(product.getSubjectName());
        holder.date_of_homework_layout.setVisibility(View.GONE);
        holder.submittion_date_layout.setVisibility(View.GONE);
        holder.assignment_tv.setText(product.getTitle());
        holder.delete_item.setVisibility(View.VISIBLE);
        holder.delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout_PopUP(product.getTimetableId(), position);
            }
        });
        holder.edit_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("ALL_Data", product);
                bundle.putString("Check", "Update");
                ErrorMessage.I(activity, ADD_TimeTableActivity.class, bundle);

            }
        });


    }

    @Override
    public int getItemCount() {
        return response_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView class_name_tv, subject_name_tv, date_of_home_tv, submission_date_tv, assignment_tv;
        ImageButton delete_item, edit_item;
        LinearLayout date_of_homework_layout, submittion_date_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            class_name_tv = (TextView) itemView.findViewById(R.id.class_name_tv);
            subject_name_tv = (TextView) itemView.findViewById(R.id.subject_name_tv);
            date_of_home_tv = (TextView) itemView.findViewById(R.id.date_of_home_tv);
            submission_date_tv = (TextView) itemView.findViewById(R.id.submission_date_tv);
            assignment_tv = (TextView) itemView.findViewById(R.id.assignment_tv);
            delete_item = (ImageButton) itemView.findViewById(R.id.delete_item);
            edit_item = (ImageButton) itemView.findViewById(R.id.edit_item);
            submittion_date_layout = (LinearLayout) itemView.findViewById(R.id.submittion_date_layout);
            date_of_homework_layout = (LinearLayout) itemView.findViewById(R.id.date_of_homework_layout);
        }
    }

    public void Logout_PopUP(String session_id, int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.alert_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final Button submit_btn = (Button) dialog.findViewById(R.id.submit_btn);
        final Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                Remove_list(session_id, position);
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    private void Remove_list(String homework_id, final int Position) {
        if (NetworkUtil.isNetworkAvailable(activity)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(activity);
            ErrorMessage.E("homework_id" + homework_id);
            Call<ResponseBody> call = AppConfig.getLoadInterface().deleteTimetable(homework_id);
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
