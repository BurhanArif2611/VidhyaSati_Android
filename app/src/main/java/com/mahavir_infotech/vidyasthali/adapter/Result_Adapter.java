package com.mahavir_infotech.vidyasthali.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.activity.Student.PDFViewerActivity;

import com.mahavir_infotech.vidyasthali.models.Result_Models.ListResult;

import java.util.List;

public class Result_Adapter extends RecyclerView.Adapter<Result_Adapter.ViewHolder> {

    Context activity;
    //List<Shop> response;
    List<ListResult> response;



    public Result_Adapter(Context activity, List<ListResult> response) {
        this.activity = activity;
        this.response = response;
    }

    @NonNull
    @Override
    public Result_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_adapter, parent, false);
        Result_Adapter.ViewHolder viewHolder = new Result_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Result_Adapter.ViewHolder holder, final int position) {
        ListResult product = response.get(position);

        holder.class_name_tv.setText(product.getClassName());
        holder.title_tv.setText(product.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("image", product.getAttachNotes());
                bundle1.putString("title", "Result");
                ErrorMessage.I(activity, PDFViewerActivity.class, bundle1);
            }
        });  holder.view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("image", product.getAttachNotes());
                bundle1.putString("title", "Result");
                ErrorMessage.I(activity, PDFViewerActivity.class, bundle1);
            }
        });



    }

    @Override
    public int getItemCount() {
        return response.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView  title_tv, class_name_tv;
        Button view_btn;

        public ViewHolder(View itemView) {
            super(itemView);
            class_name_tv = (TextView) itemView.findViewById(R.id.subject_tv);
            title_tv = (TextView) itemView.findViewById(R.id.created_date_tv);
            view_btn = (Button) itemView.findViewById(R.id.view_btn);
        }
    }
}
