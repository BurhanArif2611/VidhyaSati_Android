package com.mahavir_infotech.vidyasthali.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.models.Gallery_models.Result;

import java.util.List;

public class Gallery_Adapter extends RecyclerView.Adapter<Gallery_Adapter.ViewHolder> {

    Context activity;
    List<Result> response;


    public Gallery_Adapter(Context activity, List<Result> response) {
        this.activity = activity;
        this.response = response;

    }

    @NonNull
    @Override
    public Gallery_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_adapter, parent, false);
        Gallery_Adapter.ViewHolder viewHolder = new Gallery_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Gallery_Adapter.ViewHolder holder, final int position) {
        Result product = response.get(position);
        holder.title_tv.setText(product.getName());
        try {
            if (product.getPhotos().size() > 0) {
                Photo_Adapter qtyListAdater = new Photo_Adapter(activity, product.getPhotos());
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(activity, 2);
                holder.photo_rcv.setLayoutManager(mLayoutManager);
                holder.photo_rcv.setItemAnimator(new DefaultItemAnimator());
                holder.photo_rcv.setAdapter(qtyListAdater);
            }
            if (product.getMedia().size() > 0) {
                Photo_Adapter qtyListAdater = new Photo_Adapter(activity, product.getMedia());
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(activity, 2);
                holder.media_rcv.setLayoutManager(mLayoutManager);
                holder.media_rcv.setItemAnimator(new DefaultItemAnimator());
                holder.media_rcv.setAdapter(qtyListAdater);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return response.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title_tv;
        RecyclerView photo_rcv, media_rcv;

        public ViewHolder(View itemView) {
            super(itemView);
            title_tv = (TextView) itemView.findViewById(R.id.title_tv);
            media_rcv = (RecyclerView) itemView.findViewById(R.id.media_rcv);
            photo_rcv = (RecyclerView) itemView.findViewById(R.id.photo_rcv);
        }
    }
}
