package com.mahavir_infotech.vidyasthali.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.activity.Student.PlayLiveClassActivity;
import com.mahavir_infotech.vidyasthali.activity.Student.PlayVideoActivity;
import com.mahavir_infotech.vidyasthali.models.ListLiveClass_Models.ListSession;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Student_LiveClass_Adapter extends RecyclerView.Adapter<Student_LiveClass_Adapter.ViewHolder> {

    Context activity;
    List<ListSession> response_list;
    String Short_by;

    public Student_LiveClass_Adapter(Context activity, List<ListSession> response, String short_by) {
        this.activity = activity;
        this.response_list = response;
        this.Short_by = short_by;
    }

    @NonNull
    @Override
    public Student_LiveClass_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_student_live_class_adapter, parent, false);
        Student_LiveClass_Adapter.ViewHolder viewHolder = new Student_LiveClass_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Student_LiveClass_Adapter.ViewHolder holder, final int position) {
        ListSession product = response_list.get(position);
        holder.title_tv.setText(product.getTitle());
        holder.subject_tv.setText(product.getSubjectName());
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
                if (Short_by.equals("previous")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("URL", product.getLiveUrl());
                    ErrorMessage.I(activity, PlayVideoActivity.class, bundle);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("URL", product.getLiveUrl());
                    ErrorMessage.I(activity, PlayLiveClassActivity.class, bundle);
                }


            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Short_by.equals("previous")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("URL", product.getLiveUrl());
                    ErrorMessage.I(activity, PlayVideoActivity.class, bundle);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("URL", product.getLiveUrl());
                    ErrorMessage.I(activity, PlayLiveClassActivity.class, bundle);
                }
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
        TextView title_tv, created_date_tv, subject_tv;
        ImageButton view_btn;


        public ViewHolder(View itemView) {
            super(itemView);
            view_btn = (ImageButton) itemView.findViewById(R.id.view_btn);
            title_tv = (TextView) itemView.findViewById(R.id.title_tv);
            created_date_tv = (TextView) itemView.findViewById(R.id.created_date_tv);
            subject_tv = (TextView) itemView.findViewById(R.id.subject_tv);

        }
    }

}
