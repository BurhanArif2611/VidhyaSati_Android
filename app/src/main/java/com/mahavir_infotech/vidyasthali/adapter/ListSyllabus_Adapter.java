package com.mahavir_infotech.vidyasthali.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.AppConfig;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.Utility.NetworkUtil;
import com.mahavir_infotech.vidyasthali.activity.Teacher.StudyMaterial.ADDSyllabusActivity;
import com.mahavir_infotech.vidyasthali.models.GetSyllabus.ListHomework;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListSyllabus_Adapter extends RecyclerView.Adapter<ListSyllabus_Adapter.ViewHolder> implements Filterable {

    Context activity;
    List<ListHomework> response_list;
    String Material_name;
    CustomFilter filter;
    public ListSyllabus_Adapter(Context activity, List<ListHomework> response,String material_name) {
        this.activity = activity;
        this.response_list = response;
        this.Material_name = material_name;
    }

    @NonNull
    @Override
    public ListSyllabus_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_homework_adapter, parent, false);
        ListSyllabus_Adapter.ViewHolder viewHolder = new ListSyllabus_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListSyllabus_Adapter.ViewHolder holder, final int position) {
        ListHomework product = response_list.get(position);
        holder.class_name_tv.setText(product.getClassName());
        holder.subject_name_tv.setText(product.getSubjectName());
        holder.date_of_homework_layout.setVisibility(View.GONE);
        holder.submittion_date_layout.setVisibility(View.GONE);
      if (Material_name.toLowerCase().contains("syllabus")) {
          holder.assignment_tv.setText(product.getTitle());
          holder.assignment_title_tv.setText("Title");
      }else if (Material_name.toLowerCase().contains("study material")) {
          holder.assignment_tv.setText(product.getDescription());
          holder.assignment_title_tv.setText("Description");
      }
        holder.delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout_PopUP(product.getId(),position);
            }
        });
        holder.edit_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("ALL_Data",product);
                bundle.putString("Material_name",Material_name);
                bundle.putString("Check","Update");
                ErrorMessage.I(activity, ADDSyllabusActivity.class,bundle);

            }
        });
    }

    @Override
    public int getItemCount() {
        return response_list.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilter();
        }

        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView class_name_tv,subject_name_tv,date_of_home_tv,submission_date_tv,assignment_tv,assignment_title_tv;
        ImageButton delete_item,edit_item;
        LinearLayout date_of_homework_layout,submittion_date_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            class_name_tv = (TextView) itemView.findViewById(R.id.class_name_tv);
            subject_name_tv = (TextView) itemView.findViewById(R.id.subject_name_tv);
            date_of_home_tv = (TextView) itemView.findViewById(R.id.date_of_home_tv);
            submission_date_tv = (TextView) itemView.findViewById(R.id.submission_date_tv);
            assignment_tv = (TextView) itemView.findViewById(R.id.assignment_tv);
            assignment_title_tv = (TextView) itemView.findViewById(R.id.assignment_title_tv);
            delete_item = (ImageButton) itemView.findViewById(R.id.delete_item);
            edit_item = (ImageButton) itemView.findViewById(R.id.edit_item);
            submittion_date_layout = (LinearLayout) itemView.findViewById(R.id.submittion_date_layout);
            date_of_homework_layout = (LinearLayout) itemView.findViewById(R.id.date_of_homework_layout);
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
            ErrorMessage.E("homework_id"+homework_id);
            Call<ResponseBody> call = AppConfig.getLoadInterface().deleteStudyMaterial(homework_id);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("REmove"+jsonObject.toString());
                            materialDialog.dismiss();
                            if (jsonObject.getString("status").equals("1")) {
                                ErrorMessage.E("REmove"+jsonObject.toString());
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
    class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // TODO Auto-generated method stub

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                //CONSTARINT TO UPPER
                constraint = constraint.toString().toLowerCase();

                ArrayList<ListHomework> filters = new ArrayList<ListHomework>();

                //get specific items
                for (int i = 0; i < response_list.size(); i++) {
                    ErrorMessage.E("filter"+response_list.get(i).getSubjectName()+">>"+constraint);
                    if (Material_name.toLowerCase().contains("syllabus")) {
                        if (response_list.get(i).getSubjectName().toLowerCase().contains(constraint) || response_list.get(i).getClassName().toLowerCase().contains(constraint)|| response_list.get(i).getTitle().toLowerCase().contains(constraint)) {
                            ListHomework recentModel = new ListHomework();
                            recentModel.setAttach_notes(response_list.get(i).getAttach_notes());
                            recentModel.setAttach_paper(response_list.get(i).getAttach_paper());
                            recentModel.setAttachLink(response_list.get(i).getAttachLink());
                            recentModel.setClass_id(response_list.get(i).getClass_id());
                            recentModel.setClassName(response_list.get(i).getClassName());
                            recentModel.setCreated_at(response_list.get(i).getCreated_at());
                            recentModel.setDescription(response_list.get(i).getDescription());
                            recentModel.setId(response_list.get(i).getId());
                            recentModel.setLinkType(response_list.get(i).getLinkType());
                            recentModel.setMaterialId(response_list.get(i).getMaterialId());
                            recentModel.setTitle(response_list.get(i).getTitle());
                            recentModel.setSubject_id(response_list.get(i).getSubject_id());
                            recentModel.setSubjectName(response_list.get(i).getSubjectName());
                            filters.add(recentModel);
                        }
                    }else if (Material_name.toLowerCase().contains("study material")) {
                        if (response_list.get(i).getSubjectName().toLowerCase().contains(constraint) || response_list.get(i).getClassName().toLowerCase().contains(constraint)|| response_list.get(i).getDescription().toLowerCase().contains(constraint)) {
                            ListHomework recentModel = new ListHomework();
                            recentModel.setAttach_notes(response_list.get(i).getAttach_notes());
                            recentModel.setAttach_paper(response_list.get(i).getAttach_paper());
                            recentModel.setAttachLink(response_list.get(i).getAttachLink());
                            recentModel.setClass_id(response_list.get(i).getClass_id());
                            recentModel.setClassName(response_list.get(i).getClassName());
                            recentModel.setCreated_at(response_list.get(i).getCreated_at());
                            recentModel.setDescription(response_list.get(i).getDescription());
                            recentModel.setId(response_list.get(i).getId());
                            recentModel.setLinkType(response_list.get(i).getLinkType());
                            recentModel.setMaterialId(response_list.get(i).getMaterialId());
                            recentModel.setTitle(response_list.get(i).getTitle());
                            recentModel.setSubject_id(response_list.get(i).getSubject_id());
                            recentModel.setSubjectName(response_list.get(i).getSubjectName());
                            filters.add(recentModel);
                        }
                    }

                }
                results.count = filters.size();
                results.values = filters;

            } else {
                results.count = response_list.size();
                results.values = response_list;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // TODO Auto-generated method stub
            response_list = (ArrayList<ListHomework>) results.values;
            notifyDataSetChanged();
        }
    }
}
