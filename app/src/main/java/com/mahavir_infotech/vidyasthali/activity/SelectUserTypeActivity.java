package com.mahavir_infotech.vidyasthali.activity;

import android.os.Bundle;
import android.view.View;

import androidx.cardview.widget.CardView;

import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.Utility.SavedData;
import com.mahavir_infotech.vidyasthali.activity.Teacher.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectUserTypeActivity extends BaseActivity {

    @BindView(R.id.teacher_cardview)
    CardView teacherCardview;
    @BindView(R.id.parent_cardview)
    CardView parentCardview;
    @BindView(R.id.student_cardview)
    CardView studentCardview;

    @Override
    protected int getContentResId() {
        return R.layout.activity_select_user_type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        ErrorMessage.E("tokan"+ SavedData.getTokan());
    }

    @OnClick({R.id.teacher_cardview, R.id.parent_cardview, R.id.student_cardview})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.teacher_cardview:
                Bundle bundle=new Bundle();
                bundle.putString("Role","teacher");
                ErrorMessage.I(SelectUserTypeActivity.this, LoginActivity.class,bundle);
                break;
            case R.id.parent_cardview:
                Bundle bundle2=new Bundle();
                bundle2.putString("Role","Parent");
                ErrorMessage.I(SelectUserTypeActivity.this, ParentLoginActivity.class,bundle2);
                break;
            case R.id.student_cardview:
                Bundle bundle1=new Bundle();
                bundle1.putString("Role","student");
                ErrorMessage.I(SelectUserTypeActivity.this, LoginActivity.class,bundle1);
                break;
        }
    }
}
