package com.mahavir_infotech.vidyasthali.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.activity.Student.ChatActivity;
import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;
import com.mahavir_infotech.vidyasthali.models.ChatList_Model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatingAdapter extends RecyclerView.Adapter<ChatingAdapter.MyViewHolder> {
    Context context;
    List<ChatList_Model> resultsList;
    View itemView;
    String LastDate = "";


    public ChatingAdapter(Context context, List<ChatList_Model> result) {
        this.context = context;
        this.resultsList = result;


    }


    @Override
    public ChatingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chatlist_adapter, parent, false);
        return new ChatingAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ChatingAdapter.MyViewHolder holder, final int position) {
        ChatList_Model contestsResult = resultsList.get(position);
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat mdformat1 = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = mdformat.parse(contestsResult.getDate());
            System.out.println(mdformat1.format(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (resultsList.size() > 0) {
            if (position==0){
                holder.date_linear_layout.setVisibility(View.VISIBLE);
                holder.date.setText("---------------"+mdformat1.format(date)+"---------------");
            }
            else {
                int listposition = position - 1;

                LastDate = resultsList.get(listposition).getDate();
                if (LastDate.equals(contestsResult.getDate())) {
                    holder.date_linear_layout.setVisibility(View.GONE);
                } else {
                    holder.date_linear_layout.setVisibility(View.VISIBLE);
                    holder.date.setText("---------------"+mdformat1.format(date)+"---------------");
                }
            }
        } else {
            holder.date_linear_layout.setVisibility(View.VISIBLE);
            holder.date.setText("---------------"+mdformat1.format(date)+"---------------");
        }

        if (contestsResult.getReq_from().equals(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id())) {
            holder.to_layout.setVisibility(View.GONE);
            holder.from_layout.setVisibility(View.VISIBLE);
            holder.from_tv.setText(contestsResult.getMessage());
            /*byte[] data = Base64.decode(contestsResult.getMessage(), Base64.DEFAULT);
            String newStringWithEmojis = null;*/
            try {
               /* newStringWithEmojis = new String(data, "UTF-8");
                ErrorMessage.E("All data"+newStringWithEmojis);
                holder.from_tv.setText(newStringWithEmojis);*/
                holder.from_time_tv.setText(contestsResult.getTime());
                if (contestsResult.getIs_read().equals("read")){
                    holder.message_seen_img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_double_tick_indicator));
                }
                else if (contestsResult.getIs_read().equals("not_read")){
                    holder.message_seen_img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_check_symbol));
                }
             } catch (Exception e) {
                e.printStackTrace();
                ErrorMessage.E("Exception Chatlist>>"+e.toString());
             }
        } else {
            holder.from_layout.setVisibility(View.GONE);
            holder.to_layout.setVisibility(View.VISIBLE);
            holder.to_tv.setText(contestsResult.getMessage());
            holder.to_time_tv.setText(contestsResult.getTime());
            /*byte[] data = Base64.decode(contestsResult.getMessage(), Base64.DEFAULT);
            String newStringWithEmojis = null;
            try {
                newStringWithEmojis = new String(data, "UTF-8");
                holder.to_tv.setText(newStringWithEmojis);
                holder.to_time_tv.setText(contestsResult.getTime());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/
        }


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.rv_item.setBackgroundColor(Color.GRAY);
                ((ChatActivity)context).selectedItem_onDelete(contestsResult.getId());
                return false;
            }
        });


    }


    @Override
    public int getItemCount() {
        return resultsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView activity_img, imgThreedot,message_seen_img;
        LinearLayout from_layout, to_layout, date_linear_layout;
        TextView from_tv, to_tv, date, from_time_tv, to_time_tv;
        RelativeLayout rv_item;

        public MyViewHolder(View itemView) {
            super(itemView);
            //content = (CustomTextView) itemView.findViewById(R.id.content);
            from_layout = (LinearLayout) itemView.findViewById(R.id.from_layout);
            to_layout = (LinearLayout) itemView.findViewById(R.id.to_layout);
            date_linear_layout = (LinearLayout) itemView.findViewById(R.id.date_linear_layout);
            message_seen_img=(ImageView)itemView.findViewById(R.id.message_seen_img);
            from_tv = (TextView) itemView.findViewById(R.id.from_tv);
            to_tv = (TextView) itemView.findViewById(R.id.to_tv);
            date = (TextView) itemView.findViewById(R.id.date);
            from_time_tv = (TextView) itemView.findViewById(R.id.from_time_tv);
            to_time_tv = (TextView) itemView.findViewById(R.id.to_time_tv);
            rv_item = (RelativeLayout) itemView.findViewById(R.id.rv_item);

        }
    }


}

