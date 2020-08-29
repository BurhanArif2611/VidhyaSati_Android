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
import com.mahavir_infotech.vidyasthali.activity.Teacher.ADD_HomeWorkActivity;
import com.mahavir_infotech.vidyasthali.activity.Teacher.ADD_TimeTableActivity;
import com.mahavir_infotech.vidyasthali.activity.Teacher.Live_Class.ADD_LiveClassActivity;
import com.mahavir_infotech.vidyasthali.activity.Teacher.StudyMaterial.ADDSyllabusActivity;
import com.mahavir_infotech.vidyasthali.models.GetSubject.ListSubject;

import java.util.List;

public class GetSubject_Adapter extends RecyclerView.Adapter<GetSubject_Adapter.ViewHolder> {

    Context activity;
    //List<Shop> response;
    List<ListSubject> response;
    String Check;


    public GetSubject_Adapter(Context activity, List<ListSubject> response, String check) {
        this.activity = activity;
        this.response = response;
        this.Check = check;
    }

    @NonNull
    @Override
    public GetSubject_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.classes_list_adapter, parent, false);
        GetSubject_Adapter.ViewHolder viewHolder = new GetSubject_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GetSubject_Adapter.ViewHolder holder, final int position) {
        ListSubject product = response.get(position);
      /*  if (position == 0) {
            holder.unit_tv.setTextColor(activity.getResources().getColor(R.color.background));
        } else {
            holder.unit_tv.setTextColor(activity.getResources().getColor(R.color.primary_text));
        }*/
        holder.class_name.setText(product.getSubjectName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.select_btn.setChecked(true);
                if (Check.equals("Syllabus")) {
                    ((ADDSyllabusActivity) activity).getSubject_id(product.getSubjectName(), product.getSubjectId());
                }else if (Check.equals("TimeTable")) {
                    ((ADD_TimeTableActivity) activity).getSubject_id(product.getSubjectName(), product.getSubjectId());
                }else if (Check.equals("LiveClass")) {
                    ((ADD_LiveClassActivity) activity).getSubject_id(product.getSubjectName(), product.getSubjectId());
                } else {
                    ((ADD_HomeWorkActivity) activity).getSubject_id(product.getSubjectName(), product.getSubjectId());
                }
            }
        });
        holder.select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.select_btn.setChecked(true);
                if (Check.equals("Syllabus")) {
                    ((ADDSyllabusActivity) activity).getSubject_id(product.getSubjectName(), product.getSubjectId());
                }else if (Check.equals("TimeTable")) {
                    ((ADD_TimeTableActivity) activity).getSubject_id(product.getSubjectName(), product.getSubjectId());
                }else if (Check.equals("LiveClass")) {
                    ((ADD_LiveClassActivity) activity).getSubject_id(product.getSubjectName(), product.getSubjectId());
                }
                else {
                    ((ADD_HomeWorkActivity) activity).getSubject_id(product.getSubjectName(), product.getSubjectId());
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
