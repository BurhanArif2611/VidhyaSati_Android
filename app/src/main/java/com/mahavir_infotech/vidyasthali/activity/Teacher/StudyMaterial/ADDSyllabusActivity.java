package com.mahavir_infotech.vidyasthali.activity.Teacher.StudyMaterial;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.AppConfig;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.Utility.NetworkUtil;
import com.mahavir_infotech.vidyasthali.Utility.UserAccount;
import com.mahavir_infotech.vidyasthali.Utility.Util;
import com.mahavir_infotech.vidyasthali.activity.BaseActivity;
import com.mahavir_infotech.vidyasthali.activity.Student.FullImageActivity;
import com.mahavir_infotech.vidyasthali.activity.Student.PDFViewerActivity;
import com.mahavir_infotech.vidyasthali.activity.Student.PlayVideoActivity;
import com.mahavir_infotech.vidyasthali.adapter.GetClass_Adapter;
import com.mahavir_infotech.vidyasthali.adapter.GetSubject_Adapter;
import com.mahavir_infotech.vidyasthali.adapter.Material_Type_Adapter;
import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;
import com.mahavir_infotech.vidyasthali.models.GetSubject.Example;
import com.mahavir_infotech.vidyasthali.models.GetSyllabus.ListHomework;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class ADDSyllabusActivity extends BaseActivity {

    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.select_class_tv)
    EditText selectClassTv;
    @BindView(R.id.select_subject_tv)
    EditText selectSubjectTv;
    @BindView(R.id.type_assignment_tv)
    EditText typeAssignmentTv;
    @BindView(R.id.select_type_tv)
    EditText selectTypeTv;
    @BindView(R.id.select_file)
    Button selectFile;
    @BindView(R.id.select_tv)
    TextView selectTv;
    @BindView(R.id.submit_btn)
    Button submitBtn;
    Dialog dialog;
    Example example;
    @BindView(R.id.select_layout)
    LinearLayout selectLayout;
    @BindView(R.id.video_link_tv)
    EditText videoLinkTv;
    @BindView(R.id.video_inputlayout)
    TextInputLayout videoInputlayout;
    @BindView(R.id.select_material_type_tv)
    EditText selectMaterialTypeTv;
    @BindView(R.id.title_tv)
    EditText titleTv;
    @BindView(R.id.watch_video_btn)
    Button watchVideoBtn;
    @BindView(R.id.view_image_btn)
    Button viewImageBtn;
    @BindView(R.id.select_notes_file)
    Button selectNotesFile;
    @BindView(R.id.select_notes_tv)
    TextView selectNotesTv;
    @BindView(R.id.select_sample_paper_file)
    Button selectSamplePaperFile;
    @BindView(R.id.sample_paper_tv)
    TextView samplePaperTv;
    @BindView(R.id.sample_paper_layout)
    LinearLayout samplePaperLayout;
    @BindView(R.id.video_link_layout)
    LinearLayout videoLinkLayout;
    @BindView(R.id.type_assignment_layout)
    LinearLayout typeAssignmentLayout;
    @BindView(R.id.view_notes_file_tv)
    TextView viewFileTv;
    @BindView(R.id.view_samplepaper_file_tv)
    TextView viewSamplepaperFileTv;
    @BindView(R.id.view_video_link_tv)
    TextView viewVideoLinkTv;
    private String Subject_name = "", Subject_id = "", SelectType = "";

    com.mahavir_infotech.vidyasthali.models.GetClass.Example example1;
    com.mahavir_infotech.vidyasthali.models.Material_Type.Example example_type;
    private String Class_Id = "";
    private String Material_Type_Id = "", Material_name = "";
    public static final int PERMISSION_REQUEST_CODE = 1111;
    private static final int REQUEST = 1337;
    public static int SELECT_FROM_GALLERY = 2;
    public static int CAMERA_PIC_REQUEST = 0;
    Uri mImageCaptureUri;
    private String selectedImagePath = "", Check = "", Material_Id = "";
    ListHomework listHomework;


    private static final int BUFFER_SIZE = 1024 * 2;
    private static final String IMAGE_DIRECTORY = "/demonuts_upload_gallery";

    @Override
    protected int getContentResId() {
        return R.layout.activity_addsyllabus;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Check = bundle.getString("Check");
            Material_name = bundle.getString("Material_name");
            if (Check.equals("Update")) {
                listHomework = (ListHomework) bundle.getSerializable("ALL_Data");
                selectClassTv.setText(listHomework.getClassName());
                selectSubjectTv.setText(listHomework.getSubjectName());

                typeAssignmentTv.setText(listHomework.getDescription());
                titleTv.setText(listHomework.getTitle());
                selectNotesTv.setText(listHomework.getAttach_notes());
                samplePaperTv.setText(listHomework.getAttach_paper());
                Material_Id = listHomework.getId();
                Class_Id = listHomework.getClass_id();
                Subject_id = listHomework.getSubject_id();

                submitBtn.setText("Update " + bundle.getString("Material_name"));
                titleTxt.setText("Update " + bundle.getString("Material_name"));
                videoLinkTv.setText(listHomework.getAttachLink());
                /*if (!listHomework.getLinkType().equals("")) {
                    videoInputlayout.setVisibility(View.VISIBLE);
                    selectTypeTv.setText(listHomework.getLinkType());
                    videoLinkTv.setText(listHomework.getAttachLink());
                } else if (listHomework.getLinkType().equals("image")) {
                    videoInputlayout.setVisibility(View.GONE);
                    selectLayout.setVisibility(View.VISIBLE);
                    videoLinkTv.setText(listHomework.getAttachLink());
                }*/
                if (!listHomework.getAttach_notes().equals("")) {
                    viewFileTv.setVisibility(View.VISIBLE);
                }
                if (!listHomework.getAttach_paper().equals("")) {
                    viewSamplepaperFileTv.setVisibility(View.VISIBLE);
                }
                if (!listHomework.getAttachLink().equals("")) {
                    viewVideoLinkTv.setVisibility(View.VISIBLE);
                }
                if (bundle.getString("Material_name").equals("Syllabus")) {
                    samplePaperLayout.setVisibility(View.GONE);
                    videoLinkLayout.setVisibility(View.GONE);
                    typeAssignmentLayout.setVisibility(View.GONE);
                    selectNotesFile.setText("Attachment");
                    if (!listHomework.getAttach_notes().equals("")) {
                    viewFileTv.setVisibility(View.VISIBLE);}
                    viewSamplepaperFileTv.setVisibility(View.GONE);
                    viewVideoLinkTv.setVisibility(View.GONE);
                }
            } else if (Check.equals("Student")) {
                listHomework = (ListHomework) bundle.getSerializable("ALL_Data");
                selectClassTv.setText(listHomework.getClassName());
                selectSubjectTv.setText(listHomework.getSubjectName());

                typeAssignmentTv.setText(listHomework.getDescription());
                titleTv.setText(listHomework.getTitle());
                selectNotesTv.setText(listHomework.getAttach_notes());
                samplePaperTv.setText(listHomework.getAttach_paper());
                Material_Id = listHomework.getMaterialId();
                submitBtn.setText("Update Syllabus");
                submitBtn.setVisibility(View.GONE);
                titleTxt.setText("Study &amp; Notes");
                if (listHomework.getLinkType().equals("youtube")) {
                    submitBtn.setVisibility(View.GONE);
                    videoInputlayout.setVisibility(View.VISIBLE);
                    selectTypeTv.setText(listHomework.getLinkType());
                    videoLinkTv.setText(listHomework.getAttachLink());
                    watchVideoBtn.setVisibility(View.VISIBLE);
                } else if (listHomework.getLinkType().equals("image")) {
                    submitBtn.setVisibility(View.GONE);
                    videoInputlayout.setVisibility(View.GONE);
                    selectLayout.setVisibility(View.VISIBLE);
                    videoLinkTv.setText(listHomework.getAttachLink());
                    watchVideoBtn.setVisibility(View.GONE);
                    viewImageBtn.setVisibility(View.VISIBLE);
                }
            } else if (Check.equals("")) {
                Material_Type_Id = bundle.getString("Material_Type_id");
                Material_name = bundle.getString("Material_name");
                titleTxt.setText("ADD " + bundle.getString("Material_name"));
                ErrorMessage.E("Material_name" + bundle.getString("Material_name"));
                if (bundle.getString("Material_name").equals("Syllabus")) {
                    samplePaperLayout.setVisibility(View.GONE);
                    videoLinkLayout.setVisibility(View.GONE);
                    typeAssignmentLayout.setVisibility(View.GONE);
                    selectNotesFile.setText("Attachment");
                }
                Get_Class();
            }
        } else {
            Get_Class();

        }
        //Get_Material_Type();
        watchVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("URL", listHomework.getAttachLink());
                ErrorMessage.I(ADDSyllabusActivity.this, PlayVideoActivity.class, bundle);
            }
        });
        viewVideoLinkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("URL", listHomework.getAttachLink());
                ErrorMessage.I(ADDSyllabusActivity.this, PlayVideoActivity.class, bundle);
            }
        });
        viewImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("Image", listHomework.getAttachLink());
                ErrorMessage.I(ADDSyllabusActivity.this, FullImageActivity.class, bundle);
            }
        });
        viewFileTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listHomework.getAttach_notes().contains("png") || listHomework.getAttach_notes().contains("jpg")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("Image", listHomework.getAttach_notes());
                    ErrorMessage.I(ADDSyllabusActivity.this, FullImageActivity.class, bundle);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("image", listHomework.getAttach_notes());
                    ErrorMessage.I(ADDSyllabusActivity.this, PDFViewerActivity.class, bundle);
                }
            }
        });
        viewSamplepaperFileTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listHomework.getAttach_paper().contains("png") || listHomework.getAttach_paper().contains("jpg")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("Image", listHomework.getAttach_paper());
                    ErrorMessage.I(ADDSyllabusActivity.this, FullImageActivity.class, bundle);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("image", listHomework.getAttach_paper());
                    ErrorMessage.I(ADDSyllabusActivity.this, PDFViewerActivity.class, bundle);
                }
            }
        });
    }

    @OnClick({R.id.select_class_tv, R.id.select_subject_tv, R.id.select_type_tv, R.id.select_file, R.id.submit_btn, R.id.select_material_type_tv, R.id.select_notes_file, R.id.select_sample_paper_file})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_class_tv:
                if (Check.equals("")) {
                    GetClass_PopUP();
                }
                break;
            case R.id.select_subject_tv:
                if (Check.equals("")) {
                    GetSubject_PopUP();
                }
                break;
            case R.id.select_type_tv:
                SelectType_PopUP();
                break;
            case R.id.select_file:
                if (SelectType.equals("pdf")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/pdf");
                    startActivityForResult(Intent.createChooser(intent, "Select Pdf"), 1212);
                } else {
                    selectImage();
                }
                break;
            case R.id.select_notes_file:
                SelectType = "notes";
                selectImage();
                break;
            case R.id.select_sample_paper_file:
                SelectType = "sample_paper";
                selectImage();
                break;
            case R.id.select_material_type_tv:
                GetMaterialType_PopUP();
                break;
            case R.id.submit_btn:
                if (Material_name.equals("Syllabus")) {
                    if (!selectedImagePath.equals("")) {
                        if (Check.equals("")) {
                            if (!videoLinkTv.getText().toString().equals("")) {
                                SelectType = "youtube";
                            } else {
                                SelectType = "";
                            }
                            Submit_Syllabus();
                        } else {
                            if (!videoLinkTv.getText().toString().equals("")) {
                                SelectType = "youtube";
                            } else {
                                SelectType = "";
                            }
                            Submit_UpdateSyllabus();
                        }
                    } else {
                        if (Check.equals("")) {
                            ErrorMessage.T(ADDSyllabusActivity.this, "Please Select Attachment !");
                        } else {
                            Submit_UpdateSyllabus_withoutfile();
                        }

                    }
                } else {
                    if (selectedImagePath.equals("")) {
                        if (UserAccount.isEmpty(titleTv)) {
                            if (Material_name.equals("Syllabus")) {
                                if (Check.equals("")) {
                                    Submit_Syllabus_withoutfile();
                                } else {
                                    Submit_UpdateSyllabus_withoutfile();
                                }

                            } else {
                                if (UserAccount.isEmpty(typeAssignmentTv)) {
                                    if (!videoLinkTv.getText().toString().equals("")) {
                                        SelectType = "youtube";
                                    } else {
                                        SelectType = "";
                                    }
                                    if (Check.equals("")) {
                                        Submit_Syllabus_withoutfile();
                                    } else {
                                        Submit_UpdateSyllabus_withoutfile();
                                    }

                                } else {
                                    if (UserAccount.isEmpty(videoLinkTv)) {
                                        if (Check.equals("")) {
                                            if (!videoLinkTv.getText().toString().equals("")) {
                                                SelectType = "youtube";
                                            } else {
                                                SelectType = "";
                                            }
                                            Submit_Syllabus_withoutfile();
                                        } else {
                                            Submit_UpdateSyllabus_withoutfile();
                                        }
                                    } else {
                                        UserAccount.EditTextPointer.requestFocus();
                                        UserAccount.EditTextPointer.setError("This field is not Empty !");
                                    }
                                }
                            }
                        } else {
                            UserAccount.EditTextPointer.requestFocus();
                            UserAccount.EditTextPointer.setError("This field is not Empty !");
                        }
                    } else {
                        if (Check.equals("")) {
                            if (!videoLinkTv.getText().toString().equals("")) {
                                SelectType = "youtube";
                            } else {
                                SelectType = "";
                            }
                            Submit_Syllabus();
                        } else {
                            if (!videoLinkTv.getText().toString().equals("")) {
                                SelectType = "youtube";
                            } else {
                                SelectType = "";
                            }
                            Submit_UpdateSyllabus();
                        }
                    }
                }
                break;
        }
    }

    public void GetSubject_PopUP() {
        dialog = new Dialog(ADDSyllabusActivity.this);
        dialog.setContentView(R.layout.get_class_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final TextView title_tv = (TextView) dialog.findViewById(R.id.title_tv);
        final Button submit_btn = (Button) dialog.findViewById(R.id.submit_btn);
        final Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        final RecyclerView class_rcv = (RecyclerView) dialog.findViewById(R.id.class_rcv);
        title_tv.setText("Select Subject");
        try {
            if (example.getResult().getListSubjects().size() > 0) {
                GetSubject_Adapter qtyListAdater = new GetSubject_Adapter(ADDSyllabusActivity.this, example.getResult().getListSubjects(), "Syllabus");
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ADDSyllabusActivity.this);
                class_rcv.setLayoutManager(mLayoutManager);
                class_rcv.setItemAnimator(new DefaultItemAnimator());
                class_rcv.setAdapter(qtyListAdater);
            }
        } catch (Exception e) {
        }

        submit_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    public void Get_Subject(String classId) {
        if (NetworkUtil.isNetworkAvailable(ADDSyllabusActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ADDSyllabusActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().getSubject(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id(), classId);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            materialDialog.dismiss();
                            Gson gson = new Gson();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("Get_Class" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                String responseData = jsonObject.toString();
                                example = gson.fromJson(responseData, Example.class);
                            } else {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("productdetail error" + e.toString());
                            //  ErrorMessage.T(ADDSyllabusActivity.this, "Server Error");
                            materialDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(ADDSyllabusActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ADDSyllabusActivity.this, "Response Fail");
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(ADDSyllabusActivity.this, getResources().getString(R.string.no_internet));
        }
    }

    public void getSubject_id(String subjectName, String subjectId) {
        Subject_id = subjectId;
        Subject_name = subjectName;
        selectSubjectTv.setText(subjectName);
        dialog.dismiss();
    }

    public void Get_Class() {
        if (NetworkUtil.isNetworkAvailable(ADDSyllabusActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ADDSyllabusActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().getClasses(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            materialDialog.dismiss();
                            Gson gson = new Gson();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("Get_Class" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                String responseData = jsonObject.toString();
                                example1 = gson.fromJson(responseData, com.mahavir_infotech.vidyasthali.models.GetClass.Example.class);
                            } else {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("productdetail error" + e.toString());
                            //  ErrorMessage.T(ADDSyllabusActivity.this, "Server Error");
                            materialDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(ADDSyllabusActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ADDSyllabusActivity.this, "Response Fail");
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(ADDSyllabusActivity.this, getResources().getString(R.string.no_internet));
        }
    }

    public void Select_Class(String classId, String className) {
        dialog.dismiss();
        Class_Id = classId;
        selectClassTv.setText(className);
        Get_Subject(classId);
    }

    public void GetClass_PopUP() {
        dialog = new Dialog(ADDSyllabusActivity.this);
        dialog.setContentView(R.layout.get_class_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final Button submit_btn = (Button) dialog.findViewById(R.id.submit_btn);
        final Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        final RecyclerView class_rcv = (RecyclerView) dialog.findViewById(R.id.class_rcv);
        try {
            if (example1.getResult().getListClasses().size() > 0) {
                GetClass_Adapter qtyListAdater = new GetClass_Adapter(ADDSyllabusActivity.this, example1.getResult().getListClasses(), "Syllabus");
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ADDSyllabusActivity.this);
                class_rcv.setLayoutManager(mLayoutManager);
                class_rcv.setItemAnimator(new DefaultItemAnimator());
                class_rcv.setAdapter(qtyListAdater);
            }
        } catch (Exception e) {
        }
        submit_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    public void SelectType_PopUP() {
        Dialog dialog = new Dialog(ADDSyllabusActivity.this);
        dialog.setContentView(R.layout.select_attachment_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final RadioButton video_link_btn = (RadioButton) dialog.findViewById(R.id.video_link_btn);
        final RadioButton image_btn = (RadioButton) dialog.findViewById(R.id.image_btn);
        final RadioButton pdf_btn = (RadioButton) dialog.findViewById(R.id.pdf_btn);

        video_link_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectType = "youtube";
                selectLayout.setVisibility(View.GONE);
                videoInputlayout.setVisibility(View.VISIBLE);
                selectTypeTv.setText(SelectType);
                dialog.dismiss();
            }
        });
        image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectType = "image";
                selectLayout.setVisibility(View.VISIBLE);
                videoInputlayout.setVisibility(View.GONE);
                selectTypeTv.setText(SelectType);
                dialog.dismiss();
            }
        });
        pdf_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectType = "pdf";
                selectTypeTv.setText(SelectType);
                dialog.dismiss();
                selectLayout.setVisibility(View.VISIBLE);
            }
        });
        dialog.show();
    }

    private void selectImage() {
        final CharSequence[] options = {"From Camera", "From Gallery", "PDF", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ADDSyllabusActivity.this);
        builder.setTitle("Please choose an Image");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("From Camera")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkCameraPermission()) openCameraIntent();
                        else requestPermission();
                    } else openCameraIntent();

                } else if (options[item].equals("From Gallery")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkGalleryPermission()) galleryIntent();
                        else requestGalleryPermission();
                    } else galleryIntent();
                } else if (options[item].equals("PDF")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkGalleryPermission()) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/pdf");
                            startActivityForResult(Intent.createChooser(intent, "Select Pdf"), 1212);
                        } else {
                            requestGalleryPermission();
                        }
                    } else {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("application/pdf");
                        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), 1212);
                    }
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.create().show();
    }


    private void galleryIntent() {
        Intent intent = new Intent().setType("image/*").setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FROM_GALLERY);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, CAMERA_PIC_REQUEST);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(ADDSyllabusActivity.this, new String[]{CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void requestGalleryPermission() {
        ActivityCompat.requestPermissions(ADDSyllabusActivity.this, new String[]{READ_EXTERNAL_STORAGE}, REQUEST);
    }

    private boolean checkCameraPermission() {
        int result1 = ContextCompat.checkSelfPermission(ADDSyllabusActivity.this, CAMERA);
        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkGalleryPermission() {
        int result2 = ContextCompat.checkSelfPermission(ADDSyllabusActivity.this, READ_EXTERNAL_STORAGE);
        return result2 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {
            if (SelectType.equals("notes")) {
                selectNotesTv.setText(selectedImagePath);
            } else if (SelectType.equals("sample_paper")) {
                samplePaperTv.setText(selectedImagePath);
            }


        } else if (requestCode == SELECT_FROM_GALLERY && resultCode == Activity.RESULT_OK && null != data) {
            selectedImagePath = getRealPathFromURIPath(data.getData(), ADDSyllabusActivity.this);
            try {
                File file = Util.getCompressed_Gellery(this, selectedImagePath, data.getData());
                selectedImagePath = file.getAbsolutePath();
                ErrorMessage.E("selectedImagePath" + selectedImagePath);
            } catch (Exception e) {
                e.printStackTrace();
                ErrorMessage.E("Exception" + e.toString());

            }
           selectTv.setText(selectedImagePath);
            if (SelectType.equals("notes")) {
                selectNotesTv.setText(selectedImagePath);
            } else if (SelectType.equals("sample_paper")) {
                samplePaperTv.setText(selectedImagePath);
            }

        } else if (requestCode == 1 && resultCode == Activity.RESULT_OK && null != data) {
            selectedImagePath = getRealPathFromURIPath(data.getData(), ADDSyllabusActivity.this);
            try {
                File file = Util.getCompressed_Gellery(this, selectedImagePath, data.getData());
                selectedImagePath = file.getAbsolutePath();
                ErrorMessage.E("selectedImagePath" + selectedImagePath);
            } catch (Exception e) {
                e.printStackTrace();
                ErrorMessage.E("Exception" + e.toString());

            }
            selectTv.setText(selectedImagePath);

        } else if (requestCode == 1212 && resultCode == Activity.RESULT_OK && null != data) {
            Uri uri = data.getData();
           /* String Actual_Uri = uri.toString();
           File myFile = new File(Actual_Uri);*/
            try {
                selectedImagePath = getFilePathFromURI(ADDSyllabusActivity.this, uri);
                selectTv.setText(selectedImagePath);
                if (SelectType.equals("notes")) {
                    selectNotesTv.setText(selectedImagePath);
                } else if (SelectType.equals("sample_paper")) {
                    samplePaperTv.setText(selectedImagePath);
                }
            } catch (Exception e) {
            }
           /* ErrorMessage.E("displayName path>>"+selectedImagePath);
            selectedImagePath = myFile.getAbsolutePath();
            ErrorMessage.E("displayName path><"+selectedImagePath);
            String displayName = null;
            selectTv.setText(selectedImagePath);
            if (Actual_Uri.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    cursor.close();
                }
            } else if (Actual_Uri.startsWith("file://")) {
                displayName = myFile.getName();
            }
              ErrorMessage.E("displayName"+displayName);
        */
        }

    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = null;
        int idx = 0;
        String s = "";
        try {
            cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
            if (cursor == null) {
                return contentURI.getPath();
            } else {
                cursor.moveToFirst();
                idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                s = cursor.getString(idx);
            }
        } catch (IllegalStateException e) {
            Log.e("Exception image", "selected " + e.toString());
        }
        return s;

    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(ADDSyllabusActivity.this.getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(ADDSyllabusActivity.this, "com.mahavir_infotech.vidyasthali.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, CAMERA_PIC_REQUEST);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = ADDSyllabusActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */);

        selectedImagePath = image.getAbsolutePath();
        return image;
    }

    /*"teacher_id:6
        class_id:1
        subject_id:2
        title:syllabus
        material_type_id: 1
        attach_link:http://youtube.com
        link_type:youtube
        attach_notes: SELECT FILE
        attach_paper: SELECT FILE
        description: Important question list Required"
          */
    private void Submit_Syllabus() {
        MultipartBody.Part body = null;
        MultipartBody.Part body1 = null;
        if (!selectNotesTv.getText().toString().equals("")) {
            File file = new File(selectNotesTv.getText().toString());
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("attach_notes", file.getPath(), requestFile);
        }
        if (!samplePaperTv.getText().toString().equals("")) {
            File file = new File(samplePaperTv.getText().toString());
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body1 = MultipartBody.Part.createFormData("attach_paper", file.getPath(), requestFile);
        }
        ErrorMessage.E("displayName" + body);
        ErrorMessage.E("displayName" + body1);
        if (NetworkUtil.isNetworkAvailable(ADDSyllabusActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ADDSyllabusActivity.this);
            RequestBody teacher_id = RequestBody.create(MediaType.parse("text/plain"), UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
            RequestBody class_id = RequestBody.create(MediaType.parse("text/plain"), Class_Id);
            RequestBody subject_id = RequestBody.create(MediaType.parse("text/plain"), Subject_id);
            RequestBody title = RequestBody.create(MediaType.parse("text/plain"), titleTv.getText().toString());
            RequestBody selectType = RequestBody.create(MediaType.parse("text/plain"), SelectType);
            RequestBody Video_Link = RequestBody.create(MediaType.parse("text/plain"), videoLinkTv.getText().toString());
            RequestBody material_Id = RequestBody.create(MediaType.parse("text/plain"), Material_Type_Id);
            RequestBody description = RequestBody.create(MediaType.parse("text/plain"), typeAssignmentTv.getText().toString());
            Call<ResponseBody> call = AppConfig.getLoadInterface().addStudyMaterial(teacher_id, class_id, title, subject_id, Video_Link, selectType, material_Id, description, body, body1);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonobject = null;
                        Gson gson = new Gson();
                        try {
                            materialDialog.dismiss();
                            jsonobject = new JSONObject(response.body().string());
                            ErrorMessage.E("response" + jsonobject.toString());
                            if (jsonobject.getString("status").equals("1")) {
                                ErrorMessage.T(ADDSyllabusActivity.this, jsonobject.getString("message"));
                                finish();
                            } else {
                                ErrorMessage.T(ADDSyllabusActivity.this, jsonobject.getString("message"));
                                materialDialog.dismiss();

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            //ErrorMessage.T(ADDSyllabusActivity.this, "Server Error");
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(ADDSyllabusActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ADDSyllabusActivity.this, "Response Fail");
                    System.out.println("============update profile fail  :" + t.toString());
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(ADDSyllabusActivity.this, this.getString(R.string.no_internet));
        }


    }

    private void Submit_Syllabus_withoutfile() {
        if (NetworkUtil.isNetworkAvailable(ADDSyllabusActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ADDSyllabusActivity.this);
            ErrorMessage.E("Submit_Syllabus_withoutfile" + UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id() + ">>" + Class_Id + ">>" + titleTv.getText().toString() + ">>" + Subject_id + ">>" + videoLinkTv.getText().toString() + ">>" + SelectType + ">>" + Material_Type_Id + ">>" + typeAssignmentTv.getText().toString());
            Call<ResponseBody> call = AppConfig.getLoadInterface().addStudyMaterial_without_file(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id(), Class_Id, titleTv.getText().toString(), Subject_id, videoLinkTv.getText().toString(), SelectType, Material_Type_Id, typeAssignmentTv.getText().toString());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonobject = null;
                        Gson gson = new Gson();
                        try {
                            materialDialog.dismiss();
                            jsonobject = new JSONObject(response.body().string());
                            ErrorMessage.E("response" + jsonobject.toString());
                            if (jsonobject.getString("status").equals("1")) {
                                ErrorMessage.T(ADDSyllabusActivity.this, jsonobject.getString("message"));
                                finish();
                            } else {
                                ErrorMessage.T(ADDSyllabusActivity.this, jsonobject.getString("message"));
                                materialDialog.dismiss();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            //ErrorMessage.T(ADDSyllabusActivity.this, "Server Error");
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(ADDSyllabusActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ADDSyllabusActivity.this, "Response Fail");
                    System.out.println("============update profile fail  :" + t.toString());
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(ADDSyllabusActivity.this, this.getString(R.string.no_internet));
        }
    }

    private void Submit_UpdateSyllabus() {
        MultipartBody.Part body = null;
        MultipartBody.Part body1 = null;
        if (!selectNotesTv.getText().toString().equals("")) {
            File file = new File(selectNotesTv.getText().toString());
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("attach_notes", file.getPath(), requestFile);
        }
        if (!samplePaperTv.getText().toString().equals("")) {
            File file = new File(samplePaperTv.getText().toString());
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body1 = MultipartBody.Part.createFormData("attach_paper", file.getPath(), requestFile);
        }


        if (NetworkUtil.isNetworkAvailable(ADDSyllabusActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ADDSyllabusActivity.this);
            RequestBody material_Id = RequestBody.create(MediaType.parse("text/plain"), Material_Id);
            RequestBody class_id = RequestBody.create(MediaType.parse("text/plain"), Class_Id);
            RequestBody subject_id = RequestBody.create(MediaType.parse("text/plain"), Subject_id);
            RequestBody description = RequestBody.create(MediaType.parse("text/plain"), typeAssignmentTv.getText().toString());
            RequestBody title = RequestBody.create(MediaType.parse("text/plain"), titleTv.getText().toString());
            RequestBody selectType = RequestBody.create(MediaType.parse("text/plain"), SelectType);
            RequestBody Video_Link = RequestBody.create(MediaType.parse("text/plain"), videoLinkTv.getText().toString());
            Call<ResponseBody> call = AppConfig.getLoadInterface().updateStudyMaterial(material_Id, title, Video_Link, selectType, description, body, body1);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonobject = null;
                        Gson gson = new Gson();
                        try {
                            materialDialog.dismiss();
                            jsonobject = new JSONObject(response.body().string());
                            ErrorMessage.E("response" + jsonobject.toString());
                            if (jsonobject.getString("status").equals("1")) {
                                ErrorMessage.T(ADDSyllabusActivity.this, jsonobject.getString("message"));
                                finish();
                            } else {
                                ErrorMessage.T(ADDSyllabusActivity.this, jsonobject.getString("message"));
                                materialDialog.dismiss();

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            //ErrorMessage.T(ADDSyllabusActivity.this, "Server Error");
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(ADDSyllabusActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ADDSyllabusActivity.this, "Response Fail");
                    System.out.println("============update profile fail  :" + t.toString());
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(ADDSyllabusActivity.this, this.getString(R.string.no_internet));
        }


    }

    private void Submit_UpdateSyllabus_withoutfile() {
        if (NetworkUtil.isNetworkAvailable(ADDSyllabusActivity.this)) {
            ErrorMessage.E("Submit_UpdateSyllabus_withoutfile" + videoLinkTv.getText().toString() + ">>" + SelectType + ">>" + Material_Id);
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ADDSyllabusActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().updateStudyMaterial_without_file(Material_Id, titleTv.getText().toString(), videoLinkTv.getText().toString(), SelectType, typeAssignmentTv.getText().toString());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonobject = null;
                        Gson gson = new Gson();
                        try {
                            materialDialog.dismiss();
                            jsonobject = new JSONObject(response.body().string());
                            ErrorMessage.E("response" + jsonobject.toString());
                            if (jsonobject.getString("status").equals("1")) {
                                ErrorMessage.T(ADDSyllabusActivity.this, jsonobject.getString("message"));
                                finish();
                            } else {
                                ErrorMessage.T(ADDSyllabusActivity.this, jsonobject.getString("message"));
                                materialDialog.dismiss();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            //ErrorMessage.T(ADDSyllabusActivity.this, "Server Error");
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(ADDSyllabusActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ADDSyllabusActivity.this, "Response Fail");
                    System.out.println("============update profile fail  :" + t.toString());
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(ADDSyllabusActivity.this, this.getString(R.string.no_internet));
        }


    }

    public void Get_Material_Type() {
        if (NetworkUtil.isNetworkAvailable(ADDSyllabusActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ADDSyllabusActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().getlistStudyMaterialTypes();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            materialDialog.dismiss();
                            Gson gson = new Gson();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("Get_Material_Type" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                String responseData = jsonObject.toString();
                                example_type = gson.fromJson(responseData, com.mahavir_infotech.vidyasthali.models.Material_Type.Example.class);
                            } else {
                                ErrorMessage.T(ADDSyllabusActivity.this, jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("productdetail error" + e.toString());
                            //  ErrorMessage.T(ADDSyllabusActivity.this, "Server Error");
                            materialDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(ADDSyllabusActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ADDSyllabusActivity.this, "Response Fail");
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(ADDSyllabusActivity.this, getResources().getString(R.string.no_internet));
        }
    }

    public void GetMaterialType_PopUP() {
        dialog = new Dialog(ADDSyllabusActivity.this);
        dialog.setContentView(R.layout.get_class_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final TextView title_tv = (TextView) dialog.findViewById(R.id.title_tv);
        final Button submit_btn = (Button) dialog.findViewById(R.id.submit_btn);
        final Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        final RecyclerView class_rcv = (RecyclerView) dialog.findViewById(R.id.class_rcv);
        title_tv.setText("Select Material Type");
        try {
            if (example_type.getResult().getListTypes().size() > 0) {
                Material_Type_Adapter qtyListAdater = new Material_Type_Adapter(ADDSyllabusActivity.this, example_type.getResult().getListTypes(), "");
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ADDSyllabusActivity.this);
                class_rcv.setLayoutManager(mLayoutManager);
                class_rcv.setItemAnimator(new DefaultItemAnimator());
                class_rcv.setAdapter(qtyListAdater);
            }
        } catch (Exception e) {
        }
        submit_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    public void Select_MaterialType(String classId, String className) {
        dialog.dismiss();
        Material_Type_Id = classId;
        selectMaterialTypeTv.setText(className);

    }

    public static String getFilePathFromURI(Context context, Uri contentUri) {
        //copy file and send new file path
        String fileName = getFileName(contentUri);
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(wallpaperDirectory + File.separator + fileName);
            // create folder if not exists

            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            copystream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int copystream(InputStream input, OutputStream output) throws Exception, IOException {
        byte[] buffer = new byte[BUFFER_SIZE];

        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), String.valueOf(e));
            }
            try {
                in.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), String.valueOf(e));
            }
        }
        return count;
    }


}
