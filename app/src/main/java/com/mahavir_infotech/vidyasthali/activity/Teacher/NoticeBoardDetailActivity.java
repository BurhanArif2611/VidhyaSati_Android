package com.mahavir_infotech.vidyasthali.activity.Teacher;

import android.os.Bundle;
import android.widget.TextView;

import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.activity.BaseActivity;
import com.mahavir_infotech.vidyasthali.models.Noticeboard_Models.ListLeaf;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoticeBoardDetailActivity extends BaseActivity {

    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.date_tv)
    TextView dateTv;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.describtion_tv)
    TextView describtionTv;

    @Override
    protected int getContentResId() {
        return R.layout.activity_notice_board_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        titleTxt.setText("Detail");
        Bundle bundle=getIntent().getExtras();
        if (bundle !=null){
            ListLeaf listLeaf=(ListLeaf)bundle.getSerializable("ALLData");
            dateTv.setText(listLeaf.getCreatedAt());
            titleTv.setText(listLeaf.getTitle());
            describtionTv.setText(listLeaf.getDescription());
        }
    }
}
