package com.mahavir_infotech.vidyasthali.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.activity.Teacher.NoticeBoardDetailActivity;
import com.mahavir_infotech.vidyasthali.models.Noticeboard_Models.ListLeaf;

import java.util.List;
import java.util.Random;

public class NoticeBoard_Adapter extends RecyclerView.Adapter<NoticeBoard_Adapter.ViewHolder> {

    Context activity;
    List<ListLeaf> response;

    public NoticeBoard_Adapter(Context activity, List<ListLeaf> response) {
        this.activity = activity;
        this.response = response;

    }

    @NonNull
    @Override
    public NoticeBoard_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_board_adapter, parent, false);
        NoticeBoard_Adapter.ViewHolder viewHolder = new NoticeBoard_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeBoard_Adapter.ViewHolder holder, final int position) {
        ListLeaf product = response.get(position);
        holder.title_tv.setText(product.getTitle());
        holder.date_tv.setText(product.getCreatedAt());
        holder.discribtion_tv.setText(product.getDescription());
        try{
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        holder.date_tv.setBackgroundColor(Color.parseColor("#"+number));}
        catch (Exception e){}
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("ALLData", product);
                ErrorMessage.I(activity, NoticeBoardDetailActivity.class, bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return response.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date_tv, title_tv, discribtion_tv;
        ImageView event_img;

        public ViewHolder(View itemView) {
            super(itemView);
            date_tv = (TextView) itemView.findViewById(R.id.date_tv);
            title_tv = (TextView) itemView.findViewById(R.id.title_tv);
            discribtion_tv = (TextView) itemView.findViewById(R.id.discribtion_tv);


        }
    }
}
