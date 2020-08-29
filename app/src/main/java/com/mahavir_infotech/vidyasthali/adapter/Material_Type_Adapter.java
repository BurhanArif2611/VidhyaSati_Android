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
import com.mahavir_infotech.vidyasthali.activity.Teacher.StudyMaterial.SelectMaterialTypeActivity;
import com.mahavir_infotech.vidyasthali.models.Material_Type.ListType;

import java.util.List;

public class Material_Type_Adapter extends RecyclerView.Adapter<Material_Type_Adapter.ViewHolder> {

    Context activity;
    List<ListType> response;
    String Selected_id;


    public Material_Type_Adapter(Context activity, List<ListType> response, String SelectedDate) {
        this.activity = activity;
        this.response = response;
        this.Selected_id = SelectedDate;
    }

    @NonNull
    @Override
    public Material_Type_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_type_adapter, parent, false);
        Material_Type_Adapter.ViewHolder viewHolder = new Material_Type_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Material_Type_Adapter.ViewHolder holder, final int position) {
        ListType product = response.get(position);
       if (!Selected_id.equals("")) {
           if (Selected_id.equals(product.getId())) {
               holder.top_layout.setBackground(activity.getResources().getDrawable(R.drawable.background_selected_box));
               holder.image1_img.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_verified));
               holder.class_name.setTextColor(activity.getResources().getColor(R.color.colorWhite));
           } else {
               holder.top_layout.setBackground(activity.getResources().getDrawable(R.drawable.un_select_box));
               holder.image1_img.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_right_arrow));
               holder.class_name.setTextColor(activity.getResources().getColor(R.color.primary_text));
           }
       }
        holder.class_name.setText(product.getMaterialType());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    holder.top_layout.setBackground(activity.getResources().getDrawable(R.drawable.background_selected_box));
                    holder.image1_img.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_verified));
                    holder.class_name.setTextColor(activity.getResources().getColor(R.color.colorWhite));
                    ((SelectMaterialTypeActivity) activity).Select_MaterialType(product.getId(), product.getMaterialType());
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
