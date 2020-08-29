package com.mahavir_infotech.vidyasthali.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.activity.Teacher.AttendenceListActivity;
import com.mahavir_infotech.vidyasthali.models.Attendence_Models.ListStudent;

import java.util.List;

public class Attendence_list_Adapter extends RecyclerView.Adapter<Attendence_list_Adapter.ViewHolder> {

    Context activity;
    List<ListStudent> response;
    String Check = "", Already_submit = "";

    public Attendence_list_Adapter(Context activity, List<ListStudent> response, String check, String already_submit) {
        this.activity = activity;
        this.response = response;
        this.Check = check;
        this.Already_submit = already_submit;
    }

    @NonNull
    @Override
    public Attendence_list_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_attendence_adapter, parent, false);
        Attendence_list_Adapter.ViewHolder viewHolder = new Attendence_list_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Attendence_list_Adapter.ViewHolder holder, final int position) {
        ListStudent product = response.get(position);
        if (Check.equals("")) {
            ((AttendenceListActivity) activity).Selected_rool(1, position);
            holder.student_name_tv.setText(product.getFullname());
            holder.roll_num_tv.setText("Roll No: " + product.getRollNo());
            //   ((AttendenceListActivity) activity).Selected_rool(1, position);
            holder.first.setChecked(true);
            holder.radios.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                    int select_position = 0;
                    if (radioButton.getText().toString().equals("Present")) {
                        select_position = 1;
                    } else if (radioButton.getText().toString().equals("Absent")) {
                        select_position = 0;
                    } else if (radioButton.getText().toString().equals("Leave")) {
                        select_position = 2;
                    }
                    ((AttendenceListActivity) activity).Selected_rool(select_position, position);
                }
            });
        } else if (Check.equals("Teacher")) {
            holder.student_name_tv.setText(product.getFullname());
            holder.roll_num_tv.setVisibility(View.VISIBLE);
            holder.roll_num_tv.setText("Roll No: " + product.getRollNo());
            if (Already_submit.equals("")) {
                holder.radios.setEnabled(false);
                holder.first.setEnabled(false);
                holder.second.setEnabled(false);
                holder.third.setEnabled(false);
            } else {
                holder.radios.setEnabled(true);
                holder.first.setEnabled(true);
                holder.second.setEnabled(true);
                holder.third.setEnabled(true);

            }
            if (product.getIs_present().equals("1")) {
                holder.first.setChecked(true);
                holder.second.setChecked(false);
                holder.third.setChecked(false);
            } else if (product.getIs_present().equals("0")) {
                holder.second.setChecked(true);
                holder.first.setChecked(false);
                holder.third.setChecked(false);
            } else if (product.getIs_present().equals("2")) {
                holder.third.setChecked(true);
                holder.first.setChecked(false);
                holder.second.setChecked(false);
            }
            holder.radios.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                    int select_position = 0;
                    if (radioButton.getText().toString().equals("Present")) {
                        select_position = 1;
                    } else if (radioButton.getText().toString().equals("Absent")) {
                        select_position = 0;
                    } else if (radioButton.getText().toString().equals("Leave")) {
                        select_position = 2;
                    }
                    ((AttendenceListActivity) activity).Selected_rool(select_position, position);
                }
            });
        } else {
            holder.student_name_tv.setText("Date: " + product.getDate());
            holder.roll_num_tv.setVisibility(View.GONE);
            holder.radios.setEnabled(false);
            holder.first.setEnabled(false);
            holder.second.setEnabled(false);
            holder.third.setEnabled(false);
            if (product.getIs_present().equals("1")) {
                holder.first.setChecked(true);
                holder.second.setChecked(false);
                holder.third.setChecked(false);
            } else if (product.getIs_present().equals("0")) {
                holder.second.setChecked(true);
                holder.first.setChecked(false);
                holder.third.setChecked(false);
            } else if (product.getIs_present().equals("2")) {
                holder.third.setChecked(true);
                holder.first.setChecked(false);
                holder.second.setChecked(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return response.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView student_name_tv, roll_num_tv;
        RadioGroup radios;
        RadioButton first, second, third;


        public ViewHolder(View itemView) {
            super(itemView);
            roll_num_tv = (TextView) itemView.findViewById(R.id.roll_num_tv);
            student_name_tv = (TextView) itemView.findViewById(R.id.student_name_tv);
            radios = (RadioGroup) itemView.findViewById(R.id.radios);
            first = (RadioButton) itemView.findViewById(R.id.first);
            second = (RadioButton) itemView.findViewById(R.id.second);
            third = (RadioButton) itemView.findViewById(R.id.third);
        }
    }


}
