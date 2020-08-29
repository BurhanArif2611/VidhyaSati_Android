package com.mahavir_infotech.vidyasthali.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.activity.Teacher.ADD_TimeTableActivity;
import com.mahavir_infotech.vidyasthali.models.GetTimeTable.ListTimetable;

import java.util.List;

public class StudentList_TimeTable_Adapter extends RecyclerView.Adapter<StudentList_TimeTable_Adapter.ViewHolder> {

    Context activity;
    List<ListTimetable> response_list;

    public StudentList_TimeTable_Adapter(Context activity, List<ListTimetable> response) {
        this.activity = activity;
        this.response_list = response;
    }

    @NonNull
    @Override
    public StudentList_TimeTable_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_homework_adapter, parent, false);
        StudentList_TimeTable_Adapter.ViewHolder viewHolder = new StudentList_TimeTable_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentList_TimeTable_Adapter.ViewHolder holder, final int position) {
        ListTimetable product = response_list.get(position);
        holder.class_name_tv.setText(product.getClassName());
        holder.subject_name_tv.setText(product.getSubjectName());
        holder.date_of_homework_layout.setVisibility(View.GONE);
        holder.submittion_date_layout.setVisibility(View.GONE);
        holder.assignment_tv.setText(product.getTitle());
        holder.delete_item.setVisibility(View.GONE);
        holder.edit_item.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("ALL_Data",product);
                bundle.putString("Check","Student");
                ErrorMessage.I(activity, ADD_TimeTableActivity.class,bundle);

            }
        });


    }

    @Override
    public int getItemCount() {
        return response_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView class_name_tv,subject_name_tv,date_of_home_tv,submission_date_tv,assignment_tv;
        ImageButton delete_item,edit_item;
        LinearLayout date_of_homework_layout,submittion_date_layout;

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

}
