package com.mahavir_infotech.vidyasthali.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.AppConfig;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.Utility.NetworkUtil;
import com.mahavir_infotech.vidyasthali.activity.Teacher.ADD_HomeWorkActivity;
import com.mahavir_infotech.vidyasthali.models.List_home_Work.ListHomework;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListHomeWork_Adapter extends RecyclerView.Adapter<ListHomeWork_Adapter.ViewHolder> {

    Context activity;
    //List<Shop> response;
    List<ListHomework> response_list;
    String Check;


    public ListHomeWork_Adapter(Context activity, List<ListHomework> response, String check) {
        this.activity = activity;
        this.response_list = response;
        this.Check = check;
    }

    @NonNull
    @Override
    public ListHomeWork_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
     if (Check.equals("")) {
          v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_homework_adapter, parent, false);
     }else {
          v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_student_syllabus_adapter, parent, false);

     }
        ListHomeWork_Adapter.ViewHolder viewHolder = new ListHomeWork_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListHomeWork_Adapter.ViewHolder holder, final int position) {
        ListHomework product = response_list.get(position);
       if (Check.equals("")) {
           if (!Check.equals("")) {
               holder.edit_item.setVisibility(View.GONE);
               holder.delete_item.setVisibility(View.GONE);
           }
           holder.class_name_tv.setText(product.getClassName());
           holder.subject_name_tv.setText(product.getSubjectName());
           holder.date_of_home_tv.setText(product.getHomeworkDate());
           holder.submission_date_tv.setText(product.getSubmissionDate());
           holder.assignment_tv.setText(product.getDescription());
           holder.delete_item.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Logout_PopUP(product.getHomeworkId(), position);
               }
           });
           holder.edit_item.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Bundle bundle = new Bundle();
                   bundle.putSerializable("ALL_Data", product);
                   bundle.putString("Check", "Update");
                   ErrorMessage.I(activity, ADD_HomeWorkActivity.class, bundle);

               }
           });
       }else {
           holder.title_tv.setText(product.getSubjectName());
           holder.created_date_tv.setText(product.getCreated_at());

           holder.view_btn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Bundle bundle = new Bundle();
                   bundle.putSerializable("ALL_Data", product);
                   bundle.putString("Check", "Student");
                   ErrorMessage.I(activity, ADD_HomeWorkActivity.class, bundle);

               }
           });
       }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Check.equals("")) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ALL_Data", product);
                    bundle.putString("Check", "Student");
                    ErrorMessage.I(activity, ADD_HomeWorkActivity.class, bundle);
                }
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

        TextView title_tv,created_date_tv;
        Button view_btn;
        public ViewHolder(View itemView) {
            super(itemView);
            class_name_tv = (TextView) itemView.findViewById(R.id.class_name_tv);
            subject_name_tv = (TextView) itemView.findViewById(R.id.subject_name_tv);
            date_of_home_tv = (TextView) itemView.findViewById(R.id.date_of_home_tv);
            submission_date_tv = (TextView) itemView.findViewById(R.id.submission_date_tv);
            assignment_tv = (TextView) itemView.findViewById(R.id.assignment_tv);
            delete_item = (ImageButton) itemView.findViewById(R.id.delete_item);
            edit_item = (ImageButton) itemView.findViewById(R.id.edit_item);

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
    private void Remove_list(String homework_id, final int Position) {
        if (NetworkUtil.isNetworkAvailable(activity)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(activity);
            ErrorMessage.E("homework_id" + homework_id);
            Call<ResponseBody> call = AppConfig.getLoadInterface().deleteHomework(homework_id);
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
