package com.mahavir_infotech.vidyasthali.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.activity.Student.ChatActivity;
import com.mahavir_infotech.vidyasthali.models.RecentModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchChatList_Adapter extends RecyclerView.Adapter<SearchChatList_Adapter.MyViewHolder> implements Filterable {
    Context context;
    List<RecentModel> resultsList;
    View itemView;
    CustomFilter filter;


    public SearchChatList_Adapter(Context activity, List<RecentModel> result) {
        this.context = activity;
        this.resultsList = result;
    }


    @Override
    public SearchChatList_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recentchat_adapter, parent, false);
        return new SearchChatList_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SearchChatList_Adapter.MyViewHolder holder, final int position) {
        holder.unread_relativelayout.setVisibility(View.GONE);
        RecentModel contestsResult = resultsList.get(position);
        holder.user_name.setText(contestsResult.getUsername());
        holder.message_tv.setVisibility(View.GONE);
      try{
        if (contestsResult.getClass_name()!=null ){
            holder.message_tv.setVisibility(View.VISIBLE);
            holder.message_tv.setText(contestsResult.getClass_name());
        }}
      catch (Exception e){}
      /*  byte[] data = Base64.decode(contestsResult.getLast_message(), Base64.DEFAULT);
        String newStringWithEmojis = null;
        try {
            newStringWithEmojis = new String(data, "UTF-8");
            holder.message_tv.setText(newStringWithEmojis);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try{
            if (!contestsResult.getUnread_msg().equals("0")){
                holder.unread_relativelayout.setVisibility(View.VISIBLE);
                holder.unread_count.setText(contestsResult.getUnread_msg());
            }
            else {
                holder.unread_relativelayout.setVisibility(View.GONE);
            }
        }catch (NullPointerException e){}*/
        /*   holder.message_tv.setText(contestsResult.getLast_message());*/


        //  holder.time.setText(resultsList.get(position).getCreated_at());
        Glide.with(context).load(contestsResult.getUser_profile_pic()).into(holder.profile_img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("otherID", contestsResult.getUser_id());
                intent.putExtra("img", contestsResult.getUser_profile_pic());
                intent.putExtra("Username", contestsResult.getUsername());
                intent.putExtra("role", contestsResult.getRole());
                intent.putExtra("check", "");
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return resultsList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilter();
        }

        return filter;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profile_img;
        TextView user_name,message_tv,time,unread_count;
        RelativeLayout unread_relativelayout;
        // public CustomTextView content;
        public MyViewHolder(View itemView) {
            super(itemView);
            profile_img = (CircleImageView) itemView.findViewById(R.id.profile_img);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            message_tv = (TextView) itemView.findViewById(R.id.message_tv);
            time = (TextView) itemView.findViewById(R.id.time);
            unread_count = (TextView) itemView.findViewById(R.id.unread_count);
            unread_relativelayout = (RelativeLayout) itemView.findViewById(R.id.unread_relativelayout);
        }
    }
    class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // TODO Auto-generated method stub

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                //CONSTARINT TO UPPER
                constraint = constraint.toString().toLowerCase();

                ArrayList<RecentModel> filters = new ArrayList<RecentModel>();

                //get specific items
                ErrorMessage.E("filter"+">>"+constraint);
                for (int i = 0; i < resultsList.size(); i++) {
                    ErrorMessage.E("filter"+resultsList.get(i).getUsername()+">>"+constraint);
                    if (resultsList.get(i).getUsername().toLowerCase().contains(constraint)) {
                        RecentModel recentModel = new RecentModel();
                        recentModel.setUser_id(resultsList.get(i).getUser_id());
                        recentModel.setUsername(resultsList.get(i).getUsername());
                        recentModel.setUser_profile_pic(resultsList.get(i).getUser_profile_pic());
                        recentModel.setRole(resultsList.get(i).getRole());
                        filters.add(recentModel);
                    }
                }
                results.count = filters.size();
                results.values = filters;

            } else {
                results.count = resultsList.size();
                results.values = resultsList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // TODO Auto-generated method stub
            resultsList = (ArrayList<RecentModel>) results.values;
            notifyDataSetChanged();
        }
    }
}
