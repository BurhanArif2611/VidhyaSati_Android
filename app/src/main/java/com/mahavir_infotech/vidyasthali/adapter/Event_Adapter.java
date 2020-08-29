package com.mahavir_infotech.vidyasthali.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.activity.Student.FullImageActivity;
import com.mahavir_infotech.vidyasthali.activity.Teacher.EventDetailActivity;
import com.mahavir_infotech.vidyasthali.models.GetEvent_Model.Result;

import java.util.List;

public class Event_Adapter extends RecyclerView.Adapter<Event_Adapter.ViewHolder> {

    Context activity;
    List<Result> response;

    public Event_Adapter(Context activity, List<Result> response) {
        this.activity = activity;
        this.response = response;

    }

    @NonNull
    @Override
    public Event_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_adapter, parent, false);
        Event_Adapter.ViewHolder viewHolder = new Event_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Event_Adapter.ViewHolder holder, final int position) {
        Result product = response.get(position);
        holder.title_tv.setText(product.getTitle());
        holder.describtion_tv.setText(product.getDescription());
        holder.address_tv.setText(product.getAddress());
        try {
            Glide.with(activity).load(product.getPhoto().getUrl()).placeholder(R.drawable.ic_logo).into(holder.event_img);
        } catch (Exception e) {
        }
        holder.event_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                Bundle bundle = new Bundle();
                bundle.putString("Image", product.getPhoto().getUrl());
                ErrorMessage.I(activity, FullImageActivity.class, bundle);}catch (Exception w){}
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("AllData", product);
                ErrorMessage.I(activity, EventDetailActivity.class, bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return response.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title_tv, address_tv, describtion_tv;
        ImageView event_img;

        public ViewHolder(View itemView) {
            super(itemView);
            title_tv = (TextView) itemView.findViewById(R.id.title_tv);
            address_tv = (TextView) itemView.findViewById(R.id.address_tv);
            describtion_tv = (TextView) itemView.findViewById(R.id.describtion_tv);
            event_img = (ImageView) itemView.findViewById(R.id.event_img);

        }
    }
}
