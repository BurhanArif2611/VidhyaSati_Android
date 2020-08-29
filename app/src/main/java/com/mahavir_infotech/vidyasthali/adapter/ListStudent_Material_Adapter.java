package com.mahavir_infotech.vidyasthali.adapter;

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
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.activity.Student.ViewStudyMaterialActivity;
import com.mahavir_infotech.vidyasthali.models.GetSyllabus.ListHomework;

import java.util.List;

public class ListStudent_Material_Adapter extends RecyclerView.Adapter<ListStudent_Material_Adapter.ViewHolder> {

    Context activity;
    List<ListHomework> response_list;

    public ListStudent_Material_Adapter(Context activity, List<ListHomework> response) {
        this.activity = activity;
        this.response_list = response;
    }

    @NonNull
    @Override
    public ListStudent_Material_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_student_syllabus_adapter, parent, false);
        ListStudent_Material_Adapter.ViewHolder viewHolder = new ListStudent_Material_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListStudent_Material_Adapter.ViewHolder holder, final int position) {
        ListHomework product = response_list.get(position);
        holder.title_tv.setText(product.getTitle());
        holder.created_date_tv.setText(product.getCreated_at());

        holder.view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("ALL_Data",product);
                bundle.putString("Check","Student");
                ErrorMessage.I(activity, ViewStudyMaterialActivity.class,bundle);

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putSerializable("ALL_Data",product);
                bundle.putString("Check","Student");
                ErrorMessage.I(activity, ViewStudyMaterialActivity.class,bundle);

            }
        });


    }

    @Override
    public int getItemCount() {
        return response_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title_tv,created_date_tv;
         Button view_btn;


        public ViewHolder(View itemView) {
            super(itemView);
            view_btn = (Button) itemView.findViewById(R.id.view_btn);
            title_tv = (TextView) itemView.findViewById(R.id.title_tv);
            created_date_tv = (TextView) itemView.findViewById(R.id.created_date_tv);

        }
    }
}
