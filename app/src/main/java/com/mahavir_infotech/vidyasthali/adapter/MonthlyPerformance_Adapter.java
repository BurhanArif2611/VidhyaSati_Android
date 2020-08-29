package com.mahavir_infotech.vidyasthali.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.activity.Teacher.MonthlyPerformanceSummeryActivity;
import com.mahavir_infotech.vidyasthali.models.Monthly_Performance.ListStudent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

public class MonthlyPerformance_Adapter extends PagerAdapter {//
    Activity context;
    private List<ListStudent> numeronList;
    private LayoutInflater inflater;
    String Check;

    public MonthlyPerformance_Adapter(Activity context, List<ListStudent> riskQuestionModelList, String check) {
        this.context = context;
        this.numeronList = riskQuestionModelList;
        this.Check = check;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return numeronList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        inflater = context.getLayoutInflater();
        View views = inflater.inflate(R.layout.monthly_performane_adapter, view, false);
        ListStudent servicessBean = numeronList.get(position);
        TextView student_name_tv = (TextView) views.findViewById(R.id.student_name_tv);
        TextView student_roll_tv = (TextView) views.findViewById(R.id.student_roll_tv);
        TextView title_tv = (TextView) views.findViewById(R.id.title_tv);
        try {

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat simpleMonth=new SimpleDateFormat("MMMM");
            cal.add(Calendar.MONTH, -1);
            //title_tv.setText("Month :"+simpleMonth.format(cal.getTime()));

            SpannableStringBuilder builder = new SpannableStringBuilder();
            SpannableString str1 = new SpannableString("Month :");
            str1.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.secondary_text)), 0, str1.length(), 0);
            str1.setSpan(new AbsoluteSizeSpan(context.getResources().getDimensionPixelSize(R.dimen.text_size_15)), 0, str1.length(), SPAN_INCLUSIVE_INCLUSIVE); // set size
            builder.append(str1);

            SpannableString str2 = new SpannableString(simpleMonth.format(cal.getTime()));
            str2.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.primary_text)), 0, str2.length(), 0);
            str2.setSpan(new AbsoluteSizeSpan(context.getResources().getDimensionPixelSize(R.dimen.text_size_18)), 0, str2.length(), SPAN_INCLUSIVE_INCLUSIVE); // set size
            str2.setSpan(new StyleSpan(Typeface.BOLD), 0, str2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            builder.append(str2);

            title_tv.setText(builder, TextView.BufferType.SPANNABLE);
        } catch (Exception e) {
        }
        EditText type_assignment_tv = (EditText) views.findViewById(R.id.type_assignment_tv);

        RecyclerView grade_rcv = (RecyclerView) views.findViewById(R.id.grade_rcv);
        Button submit_btn = (Button) views.findViewById(R.id.monthly_submit_btn);

        view.addView(views);
        student_name_tv.setText(servicessBean.getFullname());
        type_assignment_tv.setText(servicessBean.getDescription());
        student_roll_tv.setText("Roll No.: " + servicessBean.getRollNo());

        if (!Check.equals("")) {
            type_assignment_tv.setEnabled(false);
            submit_btn.setVisibility(View.GONE);
        } else {
            type_assignment_tv.setEnabled(true);
            submit_btn.setVisibility(View.VISIBLE);
        }
        Grage_Adapter qtyListAdater = new Grage_Adapter(context, servicessBean.getGradeTypes(), Check, servicessBean.getStudentId());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        grade_rcv.setLayoutManager(mLayoutManager);
        grade_rcv.setItemAnimator(new DefaultItemAnimator());
        grade_rcv.setAdapter(qtyListAdater);
        qtyListAdater.notifyDataSetChanged();

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MonthlyPerformanceSummeryActivity) context).SubmitDataData(servicessBean.getStudentId(), type_assignment_tv.getText().toString());
            }
        });
        return views;

    }

}
