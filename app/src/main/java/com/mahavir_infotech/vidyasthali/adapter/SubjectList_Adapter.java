package com.mahavir_infotech.vidyasthali.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.activity.Student.SubjectListActivity;
import com.mahavir_infotech.vidyasthali.models.GetSubject.ListSubject;

import java.util.List;

public class SubjectList_Adapter extends RecyclerView.Adapter<SubjectList_Adapter.ViewHolder> {

    Context activity;
    List<ListSubject> response;
    String Selected_id;


    public SubjectList_Adapter(Context activity, List<ListSubject> response, String SelectedDate) {
        this.activity = activity;
        this.response = response;
        this.Selected_id = SelectedDate;
    }

    @NonNull
    @Override
    public SubjectList_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_type_adapter, parent, false);
        SubjectList_Adapter.ViewHolder viewHolder = new SubjectList_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectList_Adapter.ViewHolder holder, final int position) {
        ListSubject product = response.get(position);
        if (!Selected_id.equals("")) {
            if (Selected_id.equals(product.getSubjectId())) {
                holder.top_layout.setBackground(activity.getResources().getDrawable(R.drawable.background_selected_box));
                holder.image1_img.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_verified));
                holder.class_name.setTextColor(activity.getResources().getColor(R.color.colorWhite));
            } else {
                holder.top_layout.setBackground(activity.getResources().getDrawable(R.drawable.un_select_box));
                holder.image1_img.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_right_arrow));
                holder.class_name.setTextColor(activity.getResources().getColor(R.color.primary_text));
            }
        }
        holder.class_name.setText(product.getSubjectName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    holder.top_layout.setBackground(activity.getResources().getDrawable(R.drawable.background_selected_box));
                    holder.image1_img.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_verified));
                    holder.class_name.setTextColor(activity.getResources().getColor(R.color.colorWhite));
                    ((SubjectListActivity) activity).Select_MaterialType(product.getSubjectId(), product.getSubjectName());
                }catch (Exception e){}
            }
        });



    }

    @Override
    public int getItemCount() {
        return response.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView class_name;
        LinearLayout top_layout;
        ImageView image1_img;
        public ViewHolder(View itemView) {
            super(itemView);
            class_name = (TextView) itemView.findViewById(R.id.class_name);
            top_layout = (LinearLayout) itemView.findViewById(R.id.top_layout);
            image1_img = (ImageView) itemView.findViewById(R.id.image1_img);

        }
    }
}
