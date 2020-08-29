package com.mahavir_infotech.vidyasthali.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.activity.Teacher.ADD_TimeTableActivity;
import com.mahavir_infotech.vidyasthali.activity.Teacher.CalenderActivity;
import com.mahavir_infotech.vidyasthali.activity.Teacher.Live_Class.ADD_LiveClassActivity;
import com.mahavir_infotech.vidyasthali.activity.Teacher.StudyMaterial.ADDSyllabusActivity;
import com.mahavir_infotech.vidyasthali.models.GetClass.ListClass;

import java.util.List;

public class GetClass_Adapter extends RecyclerView.Adapter<GetClass_Adapter.ViewHolder> {

    Context activity;

    List<ListClass> response;
    String Selected_Date;


    public GetClass_Adapter(Context activity, List<ListClass> response, String SelectedDate) {
        this.activity = activity;
        this.response = response;
        this.Selected_Date = SelectedDate;
    }

    @NonNull
    @Override
    public GetClass_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.classes_list_adapter, parent, false);
        GetClass_Adapter.ViewHolder viewHolder = new GetClass_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GetClass_Adapter.ViewHolder holder, final int position) {
        ListClass product = response.get(position);
      /*  if (position == 0) {
            holder.unit_tv.setTextColor(activity.getResources().getColor(R.color.background));
        } else {
            holder.unit_tv.setTextColor(activity.getResources().getColor(R.color.primary_text));
        }*/
        holder.class_name.setText(product.getClassName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.select_btn.setChecked(true);
                if (Selected_Date.equals("Syllabus")) {
                    ((ADDSyllabusActivity) activity).Select_Class(product.getClassId(), product.getClassName());
                }else if (Selected_Date.equals("TimeTable")) {
                    ((ADD_TimeTableActivity) activity).Select_Class(product.getClassId(), product.getClassName());
                }else if (Selected_Date.equals("LiveClass")) {
                    ((ADD_LiveClassActivity) activity).Select_Class(product.getClassId(), product.getClassName());
                } else {
                    ((CalenderActivity) activity).Select_Class(product.getClassId(), product.getClassName());
                }
            }
        });
        holder.select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.select_btn.setChecked(true);
                if (Selected_Date.equals("Syllabus")) {
                    ((ADDSyllabusActivity) activity).Select_Class(product.getClassId(), product.getClassName());
                }else if (Selected_Date.equals("TimeTable")) {
                    ((ADD_TimeTableActivity) activity).Select_Class(product.getClassId(), product.getClassName());
                } else if (Selected_Date.equals("LiveClass")) {
                    ((ADD_LiveClassActivity) activity).Select_Class(product.getClassId(), product.getClassName());
                }
                else {
                    ((CalenderActivity) activity).Select_Class(product.getClassId(), product.getClassName());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return response.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView class_name;
        RadioButton select_btn;

        public ViewHolder(View itemView) {
            super(itemView);
            class_name = (TextView) itemView.findViewById(R.id.class_name);
            select_btn = (RadioButton) itemView.findViewById(R.id.select_btn);
        }
    }
}
