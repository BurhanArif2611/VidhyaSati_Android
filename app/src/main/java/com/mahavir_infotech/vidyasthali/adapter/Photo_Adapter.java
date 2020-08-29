package com.mahavir_infotech.vidyasthali.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.activity.Student.FullImageActivity;
import com.mahavir_infotech.vidyasthali.models.Gallery_models.Photo;

import java.util.List;

public class Photo_Adapter extends RecyclerView.Adapter<Photo_Adapter.ViewHolder> {

    Context activity;
    List<Photo> response;


    public Photo_Adapter(Context activity, List<Photo> response) {
        this.activity = activity;
        this.response = response;

    }

    @NonNull
    @Override
    public Photo_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_adapter, parent, false);
        Photo_Adapter.ViewHolder viewHolder = new Photo_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Photo_Adapter.ViewHolder holder, final int position) {
        Photo product = response.get(position);
        try {
            Glide.with(activity).load(product.getUrl()).placeholder(R.drawable.ic_logo).into(holder.gallery_img);
        } catch (Exception e) {
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                Bundle bundle = new Bundle();
                bundle.putString("Image", product.getUrl());
                ErrorMessage.I(activity, FullImageActivity.class, bundle);
                } catch (Exception e) {
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return response.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView gallery_img;


        public ViewHolder(View itemView) {
            super(itemView);
            gallery_img = (ImageView) itemView.findViewById(R.id.gallery_img);

        }
    }
}
