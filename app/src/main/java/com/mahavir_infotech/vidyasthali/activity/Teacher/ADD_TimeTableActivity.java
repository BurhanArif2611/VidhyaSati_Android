package com.mahavir_infotech.vidyasthali.activity.Teacher;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
import com.mahavir_infotech.vidyasthali.adapter.GetClass_Adapter;
import com.mahavir_infotech.vidyasthali.adapter.GetSubject_Adapter;
import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;
import com.mahavir_infotech.vidyasthali.models.GetSubject.Example;
import com.mahavir_infotech.vidyasthali.models.GetTimeTable.ListTimetable;

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

public class ADD_TimeTableActivity extends BaseActivity {

    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.select_class_tv)
    EditText selectClassTv;
    @BindView(R.id.select_subject_tv)
    EditText selectSubjectTv;
    @BindView(R.id.type_assignment_tv)
    EditText typeAssignmentTv;
    @BindView(R.id.select_file)
    Button selectFile;
    @BindView(R.id.select_tv)
    TextView selectTv;
    @BindView(R.id.submit_btn)
    Button submitBtn;
    Dialog dialog;
    Example example;
    com.mahavir_infotech.vidyasthali.models.GetClass.Example example1;
    @BindView(R.id.sample_paper_img)
    ImageView samplePaperImg;
    @BindView(R.id.sample_paper_img_btn)
    ImageButton samplePaperImgBtn;
    @BindView(R.id.attach_layout)
    RelativeLayout attachLayout;

    private String Subject_name = "", Subject_id = "", SelectType = "";
    private String Class_Id = "";
    private String selectedImagePath = "", Check = "", Material_Id = "";

    public static final int PERMISSION_REQUEST_CODE = 1111;
    private static final int REQUEST = 1337;
    public static int SELECT_FROM_GALLERY = 2;
    public static int CAMERA_PIC_REQUEST = 0;
    Uri mImageCaptureUri;
    ListTimetable listHomework;
    private static final int BUFFER_SIZE = 1024 * 2;
    private static final String IMAGE_DIRECTORY = "/demonuts_upload_gallery";
    @Override
    protected int getContentResId() {
        return R.layout.activity_add__time_table;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        titleTxt.setText("TIme Table");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Check = bundle.getString("Check");
            listHomework = (ListTimetable) bundle.getSerializable("ALL_Data");
            selectClassTv.setText(listHomework.getClassName());
            selectSubjectTv.setText(listHomework.getSubjectName());

            typeAssignmentTv.setText(listHomework.getTitle());
            typeAssignmentTv.setEnabled(false);
            selectTv.setVisibility(View.GONE);
            Material_Id = listHomework.getTimetableId();
            submitBtn.setText("Update TimeTable");
            titleTxt.setText("Update TimeTable");
            if (Check.equals("Student")) {
                submitBtn.setVisibility(View.GONE);
                selectFile.setVisibility(View.GONE);
            }
            if (!listHomework.getAttachFile().equals("")) {
                attachLayout.setVisibility(View.VISIBLE);
                if (listHomework.getAttachFile().contains("png") || listHomework.getAttachFile().contains("jpg")) {
                    samplePaperImg.setVisibility(View.VISIBLE);
                    Glide.with(ADD_TimeTableActivity.this).load(listHomework.getAttachFile()).into(samplePaperImg);
                    samplePaperImgBtn.setVisibility(View.GONE);
                } else {
                    samplePaperImg.setVisibility(View.GONE);
                    samplePaperImgBtn.setVisibility(View.VISIBLE);
                }
            }

        } else {
            Get_Class();
        }
        samplePaperImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!listHomework.getAttachFile().equals("")) {
                    if (listHomework.getAttachFile().contains("png") || listHomework.getAttachFile().contains("jpg")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("Image", listHomework.getAttachFile());
                        ErrorMessage.I(ADD_TimeTableActivity.this, FullImageActivity.class, bundle);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("image", listHomework.getAttachFile());
                        ErrorMessage.I(ADD_TimeTableActivity.this, PDFViewerActivity.class, bundle);
                    }
                }
            }
        });
        samplePaperImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!listHomework.getAttachFile().equals("")) {
                    if (listHomework.getAttachFile().contains("png") || listHomework.getAttachFile().contains("jpg")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("Image", listHomework.getAttachFile());
                        ErrorMessage.I(ADD_TimeTableActivity.this, FullImageActivity.class, bundle);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("image", listHomework.getAttachFile());
                        ErrorMessage.I(ADD_TimeTableActivity.this, PDFViewerActivity.class, bundle);
                    }
                }
            }
        });
    }

    @OnClick({R.id.select_class_tv, R.id.select_subject_tv, R.id.select_file, R.id.submit_btn})
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
            case R.id.select_file:
                selectImage();
                break;
            case R.id.submit_btn:
                if (selectedImagePath.equals("")) {
                    if (UserAccount.isEmpty(typeAssignmentTv)) {
                        if (Check.equals("")) {
                            Submit_Syllabus_withoutfile();
                        } else {
                            Submit_UpdateSyllabus_withoutfile();
                        }
                    } else {
                        UserAccount.EditTextPointer.requestFocus();
                        UserAccount.EditTextPointer.setError("This field is not Empty !");
                    }
                } else {
                    if (Check.equals("")) {
                        Submit_Syllabus();
                    } else {
                        Submit_UpdateSyllabus();
                    }
                }
                break;
        }
    }

    public void GetSubject_PopUP() {
        dialog = new Dialog(ADD_TimeTableActivity.this);
        dialog.setContentView(R.layout.get_class_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final Button submit_btn = (Button) dialog.findViewById(R.id.submit_btn);
        final Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        final RecyclerView class_rcv = (RecyclerView) dialog.findViewById(R.id.class_rcv);
        try {
            if (example.getResult().getListSubjects().size() > 0) {
                GetSubject_Adapter qtyListAdater = new GetSubject_Adapter(ADD_TimeTableActivity.this, example.getResult().getListSubjects(), "TimeTable");
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ADD_TimeTableActivity.this);
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
        if (NetworkUtil.isNetworkAvailable(ADD_TimeTableActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ADD_TimeTableActivity.this);
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
                            //  ErrorMessage.T(ADD_TimeTableActivity.this, "Server Error");
                            materialDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(ADD_TimeTableActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ADD_TimeTableActivity.this, "Response Fail");
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(ADD_TimeTableActivity.this, getResources().getString(R.string.no_internet));
        }
    }

    public void getSubject_id(String subjectName, String subjectId) {
        Subject_id = subjectId;
        Subject_name = subjectName;
        selectSubjectTv.setText(subjectName);
        dialog.dismiss();
    }

    public void Get_Class() {
        if (NetworkUtil.isNetworkAvailable(ADD_TimeTableActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ADD_TimeTableActivity.this);
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
                            //  ErrorMessage.T(ADD_TimeTableActivity.this, "Server Error");
                            materialDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(ADD_TimeTableActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ADD_TimeTableActivity.this, "Response Fail");
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(ADD_TimeTableActivity.this, getResources().getString(R.string.no_internet));
        }
    }

    public void Select_Class(String classId, String className) {
        dialog.dismiss();
        Class_Id = classId;
        selectClassTv.setText(className);
        Get_Subject(classId);
    }

    public void GetClass_PopUP() {
        dialog = new Dialog(ADD_TimeTableActivity.this);
        dialog.setContentView(R.layout.get_class_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final Button submit_btn = (Button) dialog.findViewById(R.id.submit_btn);
        final Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        final RecyclerView class_rcv = (RecyclerView) dialog.findViewById(R.id.class_rcv);
        try {
            if (example1.getResult().getListClasses().size() > 0) {
                GetClass_Adapter qtyListAdater = new GetClass_Adapter(ADD_TimeTableActivity.this, example1.getResult().getListClasses(), "TimeTable");
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ADD_TimeTableActivity.this);
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

    private void selectImage() {
        final CharSequence[] options = {"From Camera", "From Gallery", "PDF File", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ADD_TimeTableActivity.this);
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
                }else if (options[item].equals("PDF File")) {
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
                }else if (options[item].equals("Cancel")) {
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
        ActivityCompat.requestPermissions(ADD_TimeTableActivity.this, new String[]{CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void requestGalleryPermission() {
        ActivityCompat.requestPermissions(ADD_TimeTableActivity.this, new String[]{READ_EXTERNAL_STORAGE}, REQUEST);
    }

    private boolean checkCameraPermission() {
        int result1 = ContextCompat.checkSelfPermission(ADD_TimeTableActivity.this, CAMERA);
        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkGalleryPermission() {
        int result2 = ContextCompat.checkSelfPermission(ADD_TimeTableActivity.this, READ_EXTERNAL_STORAGE);
        return result2 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {
            selectTv.setText(selectedImagePath);
        } else if (requestCode == SELECT_FROM_GALLERY && resultCode == Activity.RESULT_OK && null != data) {
            selectedImagePath = getRealPathFromURIPath(data.getData(), ADD_TimeTableActivity.this);
            try {
                File file = Util.getCompressed_Gellery(this, selectedImagePath, data.getData());
                selectedImagePath = file.getAbsolutePath();
                ErrorMessage.E("selectedImagePath" + selectedImagePath);
            } catch (Exception e) {
                e.printStackTrace();
                ErrorMessage.E("Exception" + e.toString());

            }
            selectTv.setText(selectedImagePath);

        } else if (requestCode == 1 && resultCode == Activity.RESULT_OK && null != data) {
            selectedImagePath = getRealPathFromURIPath(data.getData(), ADD_TimeTableActivity.this);
            try {
                File file = Util.getCompressed_Gellery(this, selectedImagePath, data.getData());
                selectedImagePath = file.getAbsolutePath();
                ErrorMessage.E("selectedImagePath" + selectedImagePath);
            } catch (Exception e) {
                e.printStackTrace();
                ErrorMessage.E("Exception" + e.toString());

            }
            selectTv.setText(selectedImagePath);

        }
        else if (requestCode == 1212 && resultCode == Activity.RESULT_OK && null != data) {
            Uri uri = data.getData();
           /* String Actual_Uri = uri.toString();
           File myFile = new File(Actual_Uri);*/
            try {
                selectedImagePath = getFilePathFromURI(ADD_TimeTableActivity.this, uri);
                selectTv.setText(selectedImagePath);
            } catch (Exception e) {
            }
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
        if (pictureIntent.resolveActivity(ADD_TimeTableActivity.this.getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(ADD_TimeTableActivity.this, "com.mahavir_infotech.vidyasthali.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, CAMERA_PIC_REQUEST);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = ADD_TimeTableActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */);

        selectedImagePath = image.getAbsolutePath();
        return image;
    }

    private void Submit_Syllabus() {
        File file;
        file = new File(selectedImagePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("attach_notes", file.getName(), requestFile);

        if (NetworkUtil.isNetworkAvailable(ADD_TimeTableActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ADD_TimeTableActivity.this);

            RequestBody teacher_id = RequestBody.create(MediaType.parse("text/plain"), UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
            RequestBody class_id = RequestBody.create(MediaType.parse("text/plain"), Class_Id);
            RequestBody subject_id = RequestBody.create(MediaType.parse("text/plain"), Subject_id);
            RequestBody description = RequestBody.create(MediaType.parse("text/plain"), typeAssignmentTv.getText().toString());

            Call<ResponseBody> call = AppConfig.getLoadInterface().addTimeTable(teacher_id, class_id, description, subject_id, body);
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
                                ErrorMessage.T(ADD_TimeTableActivity.this, jsonobject.getString("message"));
                                finish();
                            } else {
                                ErrorMessage.T(ADD_TimeTableActivity.this, jsonobject.getString("message"));
                                materialDialog.dismiss();

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            //ErrorMessage.T(ADD_TimeTableActivity.this, "Server Error");
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(ADD_TimeTableActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ADD_TimeTableActivity.this, "Response Fail");
                    System.out.println("============update profile fail  :" + t.toString());
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(ADD_TimeTableActivity.this, this.getString(R.string.no_internet));
        }


    }

    private void Submit_Syllabus_withoutfile() {
        if (NetworkUtil.isNetworkAvailable(ADD_TimeTableActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ADD_TimeTableActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().addTimeTable_without_file(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id(), Class_Id, typeAssignmentTv.getText().toString(), Subject_id);
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
                                ErrorMessage.T(ADD_TimeTableActivity.this, jsonobject.getString("message"));
                                finish();
                            } else {
                                ErrorMessage.T(ADD_TimeTableActivity.this, jsonobject.getString("message"));
                                materialDialog.dismiss();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            //ErrorMessage.T(ADD_TimeTableActivity.this, "Server Error");
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(ADD_TimeTableActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ADD_TimeTableActivity.this, "Response Fail");
                    System.out.println("============update profile fail  :" + t.toString());
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(ADD_TimeTableActivity.this, this.getString(R.string.no_internet));
        }
    }

    private void Submit_UpdateSyllabus() {
        File file;
        file = new File(selectedImagePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("attach_notes", file.getName(), requestFile);

        if (NetworkUtil.isNetworkAvailable(ADD_TimeTableActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ADD_TimeTableActivity.this);
            RequestBody teacher_id = RequestBody.create(MediaType.parse("text/plain"), UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
            RequestBody material_Id = RequestBody.create(MediaType.parse("text/plain"), Material_Id);
            RequestBody description = RequestBody.create(MediaType.parse("text/plain"), typeAssignmentTv.getText().toString());

            Call<ResponseBody> call = AppConfig.getLoadInterface().updateTimeTable(teacher_id, material_Id, description, body);
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
                                ErrorMessage.T(ADD_TimeTableActivity.this, jsonobject.getString("message"));
                                finish();
                            } else {
                                ErrorMessage.T(ADD_TimeTableActivity.this, jsonobject.getString("message"));
                                materialDialog.dismiss();

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            //ErrorMessage.T(ADD_TimeTableActivity.this, "Server Error");
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(ADD_TimeTableActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ADD_TimeTableActivity.this, "Response Fail");
                    System.out.println("============update profile fail  :" + t.toString());
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(ADD_TimeTableActivity.this, this.getString(R.string.no_internet));
        }


    }

    private void Submit_UpdateSyllabus_withoutfile() {
        if (NetworkUtil.isNetworkAvailable(ADD_TimeTableActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ADD_TimeTableActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().updateTimeTable_without_file(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id(), Material_Id, typeAssignmentTv.getText().toString());
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
                                ErrorMessage.T(ADD_TimeTableActivity.this, jsonobject.getString("message"));
                                finish();
                            } else {
                                ErrorMessage.T(ADD_TimeTableActivity.this, jsonobject.getString("message"));
                                materialDialog.dismiss();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            //ErrorMessage.T(ADD_TimeTableActivity.this, "Server Error");
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(ADD_TimeTableActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ADD_TimeTableActivity.this, "Response Fail");
                    System.out.println("============update profile fail  :" + t.toString());
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(ADD_TimeTableActivity.this, this.getString(R.string.no_internet));
        }


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
