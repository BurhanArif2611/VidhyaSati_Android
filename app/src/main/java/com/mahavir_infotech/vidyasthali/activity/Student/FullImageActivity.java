package com.mahavir_infotech.vidyasthali.activity.Student;

import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullImageActivity extends BaseActivity {

    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.sliderImage)
    PhotoView sliderImage;

    @Override
    protected int getContentResId() {
        return R.layout.activity_full_image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        titleTxt.setText("Full Image");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ErrorMessage.E("Image" + bundle.getString("Image"));
           /* titleTxt.setText(bundle.getString("Title"));
            descriptionTv.setText(bundle.getString("Describtion"));*/
            Glide.with(FullImageActivity.this).load(bundle.getString("Image")).placeholder(R.drawable.logo).into(sliderImage);

        }
    }
}
