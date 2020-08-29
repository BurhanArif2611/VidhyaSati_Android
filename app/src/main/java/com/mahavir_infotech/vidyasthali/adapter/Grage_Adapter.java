package com.mahavir_infotech.vidyasthali.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.activity.Teacher.MonthlyPerformanceSummeryActivity;
import com.mahavir_infotech.vidyasthali.models.Monthly_Performance.GradeType;

import java.util.List;

public class Grage_Adapter extends RecyclerView.Adapter<Grage_Adapter.ViewHolder> {

    Context activity;
    List<GradeType> response;
    String Check = "";
    String Position;


    public Grage_Adapter(Context activity, List<GradeType> response, String check, String position) {
        this.activity = activity;
        this.response = response;
        this.Check = check;
        this.Position = position;


    }

    @NonNull
    @Override
    public Grage_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grade_adapter_layout, parent, false);
        Grage_Adapter.ViewHolder viewHolder = new Grage_Adapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Grage_Adapter.ViewHolder holder, final int position) {
        GradeType product = response.get(position);
        holder.grade_name_tv.setText("* " + product.getName());
        holder.grade_spiner_tv.setText(product.getRemark());
       if (!product.getRemark().equals("")){
           ((MonthlyPerformanceSummeryActivity)activity).getData((Position),product.getRemark(),product.getGradeId());
       }
        holder.grade_spiner_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Check.equals("")) {
                    showPopup(v, holder,product.getGradeId());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return response.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView grade_name_tv, grade_spiner_tv;


        public ViewHolder(View itemView) {
            super(itemView);
            grade_name_tv = (TextView) itemView.findViewById(R.id.grade_name_tv);
            grade_spiner_tv = (TextView) itemView.findViewById(R.id.grade_spiner_tv);

        }
    }

    private void showPopup(View v, ViewHolder holder, String gradeId) {
        PopupMenu popup = new PopupMenu(activity, v);
// popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.grade_menu, popup.getMenu());
        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.a) {
                    holder.grade_spiner_tv.setText("A");
                    ((MonthlyPerformanceSummeryActivity)activity).getData((Position),"A",gradeId);

                } else if (item.getItemId() == R.id.b) {
                    holder.grade_spiner_tv.setText("B");
                    ((MonthlyPerformanceSummeryActivity)activity).getData((Position),"B",gradeId);

                } else if (item.getItemId() == R.id.c) {
                    holder.grade_spiner_tv.setText("C");
                    ((MonthlyPerformanceSummeryActivity)activity).getData((Position),"C",gradeId);

                } else if (item.getItemId() == R.id.d) {
                    holder.grade_spiner_tv.setText("D");
                    ((MonthlyPerformanceSummeryActivity)activity).getData((Position),"D",gradeId);

                }
                return false;
            }
        });
    }

}
