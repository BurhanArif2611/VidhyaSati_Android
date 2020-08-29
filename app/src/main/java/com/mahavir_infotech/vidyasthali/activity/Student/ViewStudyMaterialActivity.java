package com.mahavir_infotech.vidyasthali.activity.Student;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.activity.BaseActivity;
import com.mahavir_infotech.vidyasthali.models.GetSyllabus.ListHomework;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewStudyMaterialActivity extends BaseActivity {

    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.select_class_tv)
    EditText selectClassTv;
    @BindView(R.id.select_subject_tv)
    EditText selectSubjectTv;
    @BindView(R.id.title_tv)
    EditText titleTv;
    @BindView(R.id.type_assignment_tv)
    EditText typeAssignmentTv;
    @BindView(R.id.video_thumnail_img)
    ImageView videoThumnailImg;
    @BindView(R.id.video_layout)
    RelativeLayout videoLayout;
    @BindView(R.id.sample_paper_img)
    ImageView samplePaperImg;
    @BindView(R.id.sample_paper_layout)
    RelativeLayout samplePaperLayout;
    @BindView(R.id.notes_img)
    ImageView notesImg;
    @BindView(R.id.notes_layout)
    RelativeLayout notesLayout;
    @BindView(R.id.sample_paper_img_btn)
    ImageButton samplePaperImgBtn;
    @BindView(R.id.notes_img_btn)
    ImageButton notesImgBtn;
    @BindView(R.id.video_icon_img)
    ImageButton video_icon_img;
    @BindView(R.id.video_view)
    View videoView;
    @BindView(R.id.sample_paper_view)
    View samplePaperView;
    private String Check = "";
    ListHomework listHomework;

    @Override
    protected int getContentResId() {
        return R.layout.activity_view_study_material;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Check = bundle.getString("Check");
            if (Check.equals("Student")) {
                listHomework = (ListHomework) bundle.getSerializable("ALL_Data");
                titleTxt.setText(listHomework.getClassName());
                selectSubjectTv.setText(listHomework.getSubjectName());
                typeAssignmentTv.setText(listHomework.getDescription());
                titleTv.setText(listHomework.getTitle());
                try {
                    if (listHomework.getLinkType().equals("youtube")) {
                        String videoId = "";
                        if (listHomework.getAttachLink().contains("v=")) {
                            String[] separated = listHomework.getAttachLink().split("v=");
                            videoId = separated[1];
                        } else if (listHomework.getAttachLink().contains("/")) {
                            videoId = listHomework.getAttachLink().substring(listHomework.getAttachLink().lastIndexOf("/")).replaceAll("/", "");
                            ErrorMessage.E("videoId" + videoId);
                        }
                        Glide.with(ViewStudyMaterialActivity.this).load("https://img.youtube.com/vi/" + videoId + "/0.jpg").into(videoThumnailImg);
                    } else {
                        videoLayout.setVisibility(View.GONE);
                        videoView.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                }
                if (!listHomework.getAttach_notes().equals("")) {
                    if (listHomework.getAttach_notes().contains("png") || listHomework.getAttach_notes().contains("jpg")) {
                        Glide.with(ViewStudyMaterialActivity.this).load(listHomework.getAttach_notes()).into(notesImg);
                        notesImgBtn.setVisibility(View.GONE);
                    } else {
                        Glide.with(ViewStudyMaterialActivity.this).load(listHomework.getAttach_notes()).into(notesImg);
                        notesImgBtn.setVisibility(View.VISIBLE);
                    }
                } else {
                    notesLayout.setVisibility(View.GONE);
                }
                if (!listHomework.getAttach_paper().equals("")) {
                    if (listHomework.getAttach_paper().contains("png") || listHomework.getAttach_paper().contains("jpg")) {
                        Glide.with(ViewStudyMaterialActivity.this).load(listHomework.getAttach_paper()).into(samplePaperImg);
                        samplePaperImgBtn.setVisibility(View.GONE);
                    } else {
                        Glide.with(ViewStudyMaterialActivity.this).load(listHomework.getAttach_paper()).into(samplePaperImg);
                        samplePaperImgBtn.setVisibility(View.VISIBLE);
                    }
                } else {
                    samplePaperLayout.setVisibility(View.GONE);
                    samplePaperView.setVisibility(View.GONE);
                }
            }

        }
        samplePaperImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listHomework.getAttach_paper().contains("png") || listHomework.getAttach_paper().contains("jpg")) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("Image", listHomework.getAttach_paper());
                    bundle1.putString("title", "Sample Paper");
                    ErrorMessage.I(ViewStudyMaterialActivity.this, FullImageActivity.class, bundle1);
                } else {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("image", listHomework.getAttach_paper());
                    bundle1.putString("title", "Sample Paper");
                    ErrorMessage.I(ViewStudyMaterialActivity.this, PDFViewerActivity.class, bundle1);
                }
            }
        });
        samplePaperImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listHomework.getAttach_paper().contains("png") || listHomework.getAttach_paper().contains("jpg")) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("Image", listHomework.getAttach_paper());
                    bundle1.putString("title", "Sample Paper");
                    ErrorMessage.I(ViewStudyMaterialActivity.this, FullImageActivity.class, bundle1);
                } else {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("image", listHomework.getAttach_paper());
                    bundle1.putString("title", "Notes");
                    ErrorMessage.I(ViewStudyMaterialActivity.this, PDFViewerActivity.class, bundle1);
                }
            }
        });
        notesImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listHomework.getAttach_notes().contains("png") || listHomework.getAttach_notes().contains("jpg")) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("Image", listHomework.getAttach_notes());
                    bundle1.putString("title", "Notes");
                    ErrorMessage.I(ViewStudyMaterialActivity.this, FullImageActivity.class, bundle1);
                } else {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("image", listHomework.getAttach_notes());
                    bundle1.putString("title", "Notes");
                    ErrorMessage.I(ViewStudyMaterialActivity.this, PDFViewerActivity.class, bundle1);
                }
            }
        });
        notesImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listHomework.getAttach_notes().contains("png") || listHomework.getAttach_notes().contains("jpg")) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("Image", listHomework.getAttach_notes());
                    bundle1.putString("title", "Notes");
                    ErrorMessage.I(ViewStudyMaterialActivity.this, FullImageActivity.class, bundle1);
                } else {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("image", listHomework.getAttach_notes());
                    bundle1.putString("title", "Notes");
                    ErrorMessage.I(ViewStudyMaterialActivity.this, PDFViewerActivity.class, bundle1);
                }
            }
        });
        videoThumnailImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("URL", listHomework.getAttachLink());
                bundle1.putString("title", "Notes");
                ErrorMessage.I(ViewStudyMaterialActivity.this, PlayVideoActivity.class, bundle1);
            }
        }); video_icon_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("URL", listHomework.getAttachLink());
                bundle1.putString("title", "Notes");
                ErrorMessage.I(ViewStudyMaterialActivity.this, PlayVideoActivity.class, bundle1);
            }
        });

    }
}
