package com.mahavir_infotech.vidyasthali.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.AppConfig;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.Utility.NetworkUtil;
import com.mahavir_infotech.vidyasthali.activity.Student.ADD_LeaveActivity;
import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;
import com.mahavir_infotech.vidyasthali.models.LeaveList_Models.ListLeaf;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveList_Adapter extends RecyclerView.Adapter<LeaveList_Adapter.ViewHolder> {

    Context activity;
    List<ListLeaf> response;
    String Check = "";


    public LeaveList_Adapter(Context activity, List<ListLeaf> response, String check) {
        this.activity = activity;
        this.response = response;
        this.Check = check;

    }

    @NonNull
    @Override
    public LeaveList_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_leave_adapter, parent, false);
        LeaveList_Adapter.ViewHolder viewHolder = new LeaveList_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveList_Adapter.ViewHolder holder, final int position) {
        ListLeaf product = response.get(position);
        holder.student_name_tv.setText(product.getStudentName());
        holder.leave_reason_tv.setText(product.getReason());

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(product.getFromDate());
            Date date1 = formatter.parse(product.getToDate());
            String from = new SimpleDateFormat("dd MMM yyyy").format(date);
            String to = new SimpleDateFormat("dd MMM yyyy").format(date1);
            holder.leave_date_tv.setText(from + " To " + to);
        } catch (Exception e) {
        }
        if (product.getStatus().equals("pending")) {
            holder.second_layout.setVisibility(View.VISIBLE);
            holder.other_layout.setVisibility(View.GONE);
        } else {
            holder.second_layout.setVisibility(View.GONE);
            holder.other_layout.setVisibility(View.VISIBLE);
            if (product.getStatus().equals("approve")) {
                holder.status_tv.setText(product.getStatus());
                holder.status_img.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_acknowledged_leaves));
            } else if (product.getStatus().equals("cancel")) {
                holder.status_tv.setText(product.getStatus());
                holder.status_img.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_cancelled_leaves));
            } else if (product.getStatus().equals("reject")) {
                holder.status_tv.setText(product.getStatus());
                holder.status_img.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_cancelled_leaves));
            }
        }
        holder.acknowlege_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Confirmation_PopUP(product.getLeaveId(), "approve", holder);
            }
        });
        holder.cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Confirmation_PopUP(product.getLeaveId(), "reject", holder);
            }
        });
        if (Check.equals("")) {
            holder.modify_tv.setVisibility(View.GONE);
        } else {
            holder.modify_tv.setVisibility(View.VISIBLE);
            holder.acknowlege_tv.setVisibility(View.GONE);
        }
        holder.modify_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Check.equals("Student")) {
                    Bundle bundle=new Bundle();
                    bundle.putString("Check","Update");
                    bundle.putSerializable("AllData",product);
                    ErrorMessage.I(activity, ADD_LeaveActivity.class,bundle);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return response.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView student_name_tv, leave_date_tv, leave_reason_tv;
        TextView acknowlege_tv, modify_tv, cancel_tv, status_tv;
        LinearLayout second_layout, other_layout;
        ImageView status_img;


        public ViewHolder(View itemView) {
            super(itemView);
            leave_reason_tv = (TextView) itemView.findViewById(R.id.leave_reason_tv);
            leave_date_tv = (TextView) itemView.findViewById(R.id.leave_date_tv);
            student_name_tv = (TextView) itemView.findViewById(R.id.student_name_tv);

            cancel_tv = (TextView) itemView.findViewById(R.id.cancel_tv);
            modify_tv = (TextView) itemView.findViewById(R.id.modify_tv);
            acknowlege_tv = (TextView) itemView.findViewById(R.id.acknowlege_tv);
            status_tv = (TextView) itemView.findViewById(R.id.status_tv);
            other_layout = (LinearLayout) itemView.findViewById(R.id.other_layout);
            second_layout = (LinearLayout) itemView.findViewById(R.id.second_layout);
            status_img = (ImageView) itemView.findViewById(R.id.status_img);

        }
    }

    public void Confirmation_PopUP(String leaveId, String status, ViewHolder holder) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.leave_confirmation_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final ImageView status_img = (ImageView) dialog.findViewById(R.id.status_img);
        final TextView content_tv = (TextView) dialog.findViewById(R.id.content_tv);
        final Button confirm_btn = (Button) dialog.findViewById(R.id.confirm_btn);
        final EditText reason_tv = (EditText) dialog.findViewById(R.id.reason_tv);
        final ImageButton cancel_btn = (ImageButton) dialog.findViewById(R.id.cancel_btn);
        if (status.equals("approve")) {
            content_tv.setText("Are you sure you want to confirm this Leave.");
            status_img.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_acknowledged_leaves));
        } else if (status.equals("reject")) {
            content_tv.setText("Are you sure you want to cancel this Leave.");
            status_img.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_cancelled_leaves));
        }

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Leave_Status(leaveId, status, reason_tv.getText().toString(), holder);
            }
        });

        dialog.show();
    }

    private void Leave_Status(String leave_id, String Status, String Reason, ViewHolder holder) {
        if (NetworkUtil.isNetworkAvailable(activity)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(activity);

            Call<ResponseBody> call = AppConfig.getLoadInterface().responseLeaveRequest(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id(), leave_id, Status, Reason);
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
                                holder.second_layout.setVisibility(View.GONE);
                                holder.other_layout.setVisibility(View.VISIBLE);
                                if (Status.equals("approve")) {
                                    holder.status_tv.setText(Status);
                                    holder.status_img.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_acknowledged_leaves));
                                } else if (Status.equals("cancel")) {
                                    holder.status_tv.setText(Status);
                                    holder.status_img.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_cancelled_leaves));
                                } else if (Status.equals("reject")) {
                                    holder.status_tv.setText(Status);
                                    holder.status_img.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_cancelled_leaves));
                                }
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
