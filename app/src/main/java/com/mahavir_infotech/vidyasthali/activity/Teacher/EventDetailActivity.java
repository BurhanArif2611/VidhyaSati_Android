package com.mahavir_infotech.vidyasthali.activity.Teacher;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.activity.BaseActivity;
import com.mahavir_infotech.vidyasthali.activity.Student.FullImageActivity;
import com.mahavir_infotech.vidyasthali.models.GetEvent_Model.Result;
import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.activity.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventDetailActivity extends BaseActivity {

    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.event_img)
    ImageView eventImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.date_tv)
    TextView dateTv;
    @BindView(R.id.time_tv)
    TextView timeTv;
    @BindView(R.id.address_tv)
    TextView addressTv;
    @BindView(R.id.describtion_tv)
    TextView describtionTv;
    Result result;
    @Override
    protected int getContentResId() {
        return R.layout.activity_event_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        titleTxt.setText("Detail");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
             result = (Result) bundle.getSerializable("AllData");
            titleTv.setText(result.getTitle());
            describtionTv.setText(result.getDescription());
            addressTv.setText(result.getAddress());

            Glide.with(EventDetailActivity.this).load(result.getPhoto().getUrl()).placeholder(R.drawable.ic_logo).into(eventImg);

            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
            SimpleDateFormat time = new SimpleDateFormat("hh:mm a");
            Date d = null;
            //  ErrorMessage.E("Date"+jsonObject.getString("lastModified"));
            try {
                // output.setTimeZone(TimeZone.getTimeZone("IMP"));
               // input.setTimeZone(TimeZone.getTimeZone("IMP"));
                d = input.parse(result.getDate());
            } catch (Exception e) {
                e.printStackTrace();
            }
            String formatted = output.format(d);
            String formatted_time = time.format(d);
            dateTv.setText(formatted);
            timeTv.setText(formatted_time);
        }
        eventImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("Image", result.getPhoto().getUrl());
                ErrorMessage.I(EventDetailActivity.this, FullImageActivity.class, bundle);
            }
        });
    }
}
