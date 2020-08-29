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
import com.mahavir_infotech.vidyasthali.adapter.GetSubject_Adapter;
import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;
import com.mahavir_infotech.vidyasthali.models.GetSubject.Example;
import com.mahavir_infotech.vidyasthali.models.List_home_Work.ListHomework;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

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
import java.util.Calendar;
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

public class ADD_HomeWorkActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {
    String Class_id = "", Selected_Date = "", Class_Name = "";
    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.home_date_tv)
    EditText homeDateTv;
    @BindView(R.id.select_class_tv)
    EditText selectClassTv;
    @BindView(R.id.select_subject_tv)
    EditText selectSubjectTv;
    @BindView(R.id.submission_tv)
    EditText submissionTv;
    @BindView(R.id.type_assignment_tv)
    EditText typeAssignmentTv;
    @BindView(R.id.select_file)
    Button selectFile;
    @BindView(R.id.select_tv)
    TextView selectTv;
    @BindView(R.id.submit_btn)
    Button submitBtn;
    Example example;
    @BindView(R.id.sample_paper_img)
    ImageView samplePaperImg;
    @BindView(R.id.sample_paper_img_btn)
    ImageButton samplePaperImgBtn;
    @BindView(R.id.sample_paper_layout)
    RelativeLayout samplePaperLayout;
    @BindView(R.id.view_notes_file_tv)
    TextView viewNotesFileTv;
    private String Subject_name = "", Subject_id = "";
    Dialog dialog;
    public static final int PERMISSION_REQUEST_CODE = 1111;
    private static final int REQUEST = 1337;
    public static int SELECT_FROM_GALLERY = 2;
    public static int CAMERA_PIC_REQUEST = 0;
    Uri mImageCaptureUri;
    private String selectedImagePath = "";
    ListHomework listHomework;
    private String Check = "", Home_work_id = "";
    private static final int BUFFER_SIZE = 1024 * 2;
    private static final String IMAGE_DIRECTORY = "/demonuts_upload_gallery";

    @Override
    protected int getContentResId() {
        return R.layout.activity_add__home_work;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        titleTxt.setText("Home Work");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Check = bundle.getString("Check");
            if (bundle.getString("Check").equals("Update")) {
                listHomework = (ListHomework) bundle.getSerializable("ALL_Data");
                homeDateTv.setText(listHomework.getHomeworkDate());
                selectClassTv.setText(listHomework.getClassName());
                selectSubjectTv.setText(listHomework.getSubjectName());
                submissionTv.setText(listHomework.getSubmissionDate());
                typeAssignmentTv.setText(listHomework.getDescription());
                selectTv.setText(listHomework.getPickImage());
                Home_work_id = listHomework.getHomeworkId();
                submitBtn.setText("Update HomeWork");
                if (!listHomework.getPickImage().equals("")){
                    viewNotesFileTv.setVisibility(View.VISIBLE);
                }
            } else if (bundle.getString("Check").equals("Student")) {
                listHomework = (ListHomework) bundle.getSerializable("ALL_Data");
                homeDateTv.setText(listHomework.getHomeworkDate());
                selectClassTv.setText(listHomework.getClassName());
                selectSubjectTv.setText(listHomework.getSubjectName());
                submissionTv.setText(listHomework.getSubmissionDate());
                typeAssignmentTv.setText(listHomework.getDescription());
                selectTv.setText(listHomework.getPickImage());
                Home_work_id = listHomework.getHomeworkId();
                submissionTv.setEnabled(false);
                typeAssignmentTv.setEnabled(false);
                submitBtn.setVisibility(View.GONE);
                selectFile.setVisibility(View.GONE);
                selectTv.setVisibility(View.GONE);
                samplePaperLayout.setVisibility(View.VISIBLE);
                if (!listHomework.getPickImage().equals("")) {
                    if (listHomework.getPickImage().contains("png") || listHomework.getPickImage().contains("jpg")) {
                        Glide.with(ADD_HomeWorkActivity.this).load(listHomework.getPickImage()).into(samplePaperImg);
                        samplePaperImgBtn.setVisibility(View.GONE);
                    } else {
                        Glide.with(ADD_HomeWorkActivity.this).load(listHomework.getPickImage()).into(samplePaperImg);
                        samplePaperImgBtn.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                Class_id = bundle.getString("Class_id");
                Selected_Date = bundle.getString("Selected_Date");
                Class_Name = bundle.getString("Class_Name");
                homeDateTv.setText(Selected_Date);
                selectClassTv.setText(Class_Name);

                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date oneWayTripDate = input.parse(Selected_Date);  // parse input
                    homeDateTv.setText(output.format(oneWayTripDate));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Get_Subject();
            }
        }
        samplePaperImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("image", listHomework.getPickImage());
                bundle1.putString("title", "File");
                ErrorMessage.I(ADD_HomeWorkActivity.this, PDFViewerActivity.class, bundle1);
            }
        }); samplePaperImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listHomework.getPickImage().contains("png") || listHomework.getPickImage().contains("jpg")) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("Image", listHomework.getPickImage());
                bundle1.putString("title", "File");
                ErrorMessage.I(ADD_HomeWorkActivity.this, FullImageActivity.class, bundle1);
                }
            }
        });
        viewNotesFileTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listHomework.getPickImage().contains("png") || listHomework.getPickImage().contains("jpg")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("Image", listHomework.getPickImage());
                    ErrorMessage.I(ADD_HomeWorkActivity.this, FullImageActivity.class, bundle);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("image", listHomework.getPickImage());
                    ErrorMessage.I(ADD_HomeWorkActivity.this, PDFViewerActivity.class, bundle);
                }
            }
        });
    }

    @OnClick({R.id.select_file, R.id.submit_btn, R.id.select_subject_tv, R.id.submission_tv, R.id.home_date_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_file:
                selectImage();
                break;
            case R.id.submit_btn:
                if (Check.equals("")) {
                    if (selectedImagePath.equals("")) {
                        if (UserAccount.isEmpty(typeAssignmentTv)) {
                            Submit_HomeWork_withoutfile();
                        } else {
                            UserAccount.EditTextPointer.requestFocus();
                            UserAccount.EditTextPointer.setError("This field is not Empty !");
                        }
                    } else {
                        Submit_HomeWork();
                    }
                } else {
                    if (selectedImagePath.equals("")) {
                        if (UserAccount.isEmpty(typeAssignmentTv)) {
                            Submit_UpdateHomeWork_withoutfile();
                        } else {
                            UserAccount.EditTextPointer.requestFocus();
                            UserAccount.EditTextPointer.setError("This field is not Empty !");
                        }
                    } else {
                        Submit_UpdateHomeWork();
                    }
                }
                break;
            case R.id.select_subject_tv:
                if (Check.equals("")) {
                    GetSubject_PopUP();
                }
                break;
            case R.id.submission_tv:
                datepicker();
                break;
            case R.id.home_date_tv:
                if (Check.equals("")) {
                    finish();
                }
                break;
        }
    }

    public void GetSubject_PopUP() {
        dialog = new Dialog(ADD_HomeWorkActivity.this);
        dialog.setContentView(R.layout.get_class_popup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final Button submit_btn = (Button) dialog.findViewById(R.id.submit_btn);
        final Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
        final RecyclerView class_rcv = (RecyclerView) dialog.findViewById(R.id.class_rcv);
        try {
            if (example.getResult().getListSubjects().size() > 0) {
                GetSubject_Adapter qtyListAdater = new GetSubject_Adapter(ADD_HomeWorkActivity.this, example.getResult().getListSubjects(), "");
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ADD_HomeWorkActivity.this);
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

    public void Get_Subject() {
        if (NetworkUtil.isNetworkAvailable(ADD_HomeWorkActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ADD_HomeWorkActivity.this);
            ErrorMessage.E("Get_Subject" + Class_id);
            Call<ResponseBody> call = AppConfig.getLoadInterface().getSubject(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id(), Class_id);
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
                            //  ErrorMessage.T(ADD_HomeWorkActivity.this, "Server Error");
                            materialDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(ADD_HomeWorkActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ADD_HomeWorkActivity.this, "Response Fail");
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(ADD_HomeWorkActivity.this, getResources().getString(R.string.no_internet));
        }
    }

    public void getSubject_id(String subjectName, String subjectId) {
        Subject_id = subjectId;
        Subject_name = subjectName;
        selectSubjectTv.setText(subjectName);
        dialog.dismiss();
    }

    private void datepicker() {
        Calendar calendar = Calendar.getInstance();
        //calendar.add(Calendar.DAY_OF_MONTH, 1);
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(ADD_HomeWorkActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        datePickerDialog.setMinDate(calendar);

        try {
            String dateStr = homeDateTv.getText().toString();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DAY_OF_MONTH, 1);
            datePickerDialog.setMinDate(cal);
        } catch (Exception e) {
            e.printStackTrace();
        }

        datePickerDialog.show(getFragmentManager(), "Date Picker");

    }


    @Override
    public void onDateSet(DatePickerDialog view, int eventYear, int monthOfYear, int dayOfMonth) {
        if (monthOfYear + 1 < 10 && dayOfMonth > 10)
            submissionTv.setText((dayOfMonth + "-0" + (monthOfYear + 1) + "-" + eventYear));
        else if (dayOfMonth < 10 && monthOfYear + 1 >= 10)
            submissionTv.setText((dayOfMonth + "-" + (monthOfYear + 1) + "-" + eventYear));
        else if (monthOfYear + 1 < 10 && dayOfMonth < 10)
            submissionTv.setText((dayOfMonth + "-0" + (monthOfYear + 1) + "-" + eventYear));
        else submissionTv.setText((dayOfMonth + "-" + (monthOfYear + 1) + "-" + eventYear));
    }

    private void selectImage() {
        final CharSequence[] options = {"From Camera", "From Gallery", "PDF File", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ADD_HomeWorkActivity.this);
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
                } else if (options[item].equals("PDF File")) {
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
        ActivityCompat.requestPermissions(ADD_HomeWorkActivity.this, new String[]{CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void requestGalleryPermission() {
        ActivityCompat.requestPermissions(ADD_HomeWorkActivity.this, new String[]{READ_EXTERNAL_STORAGE}, REQUEST);
    }

    private boolean checkCameraPermission() {
        int result1 = ContextCompat.checkSelfPermission(ADD_HomeWorkActivity.this, CAMERA);
        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkGalleryPermission() {
        int result2 = ContextCompat.checkSelfPermission(ADD_HomeWorkActivity.this, READ_EXTERNAL_STORAGE);
        return result2 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {
            selectTv.setText(selectedImagePath);
        } else if (requestCode == SELECT_FROM_GALLERY && resultCode == Activity.RESULT_OK && null != data) {
            selectedImagePath = getRealPathFromURIPath(data.getData(), ADD_HomeWorkActivity.this);
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
            selectedImagePath = getRealPathFromURIPath(data.getData(), ADD_HomeWorkActivity.this);
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
                selectedImagePath = getFilePathFromURI(ADD_HomeWorkActivity.this, uri);
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
        if (pictureIntent.resolveActivity(ADD_HomeWorkActivity.this.getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(ADD_HomeWorkActivity.this, "com.mahavir_infotech.vidyasthali.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, CAMERA_PIC_REQUEST);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = ADD_HomeWorkActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */);

        selectedImagePath = image.getAbsolutePath();

        return image;
    }

    private void Submit_HomeWork() {
        File file;
        file = new File(selectedImagePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("homework_file", file.getName(), requestFile);

        if (NetworkUtil.isNetworkAvailable(ADD_HomeWorkActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ADD_HomeWorkActivity.this);
            RequestBody teacher_id = RequestBody.create(MediaType.parse("text/plain"), UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
            RequestBody class_id = RequestBody.create(MediaType.parse("text/plain"), Class_id);
            RequestBody homework_date = RequestBody.create(MediaType.parse("text/plain"), Selected_Date);
            RequestBody subject_id = RequestBody.create(MediaType.parse("text/plain"), Subject_id);
            RequestBody submission_date = RequestBody.create(MediaType.parse("text/plain"), submissionTv.getText().toString());
            RequestBody description = RequestBody.create(MediaType.parse("text/plain"), typeAssignmentTv.getText().toString());
            Call<ResponseBody> call = AppConfig.getLoadInterface().addHomework(teacher_id, class_id, homework_date, subject_id, submission_date, description, body);
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
                                ErrorMessage.T(ADD_HomeWorkActivity.this, jsonobject.getString("message"));
                                finish();
                            } else {
                                ErrorMessage.T(ADD_HomeWorkActivity.this, jsonobject.getString("message"));
                                materialDialog.dismiss();

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            //ErrorMessage.T(ADD_HomeWorkActivity.this, "Server Error");
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(ADD_HomeWorkActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ADD_HomeWorkActivity.this, "Response Fail");
                    System.out.println("============update profile fail  :" + t.toString());
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(ADD_HomeWorkActivity.this, this.getString(R.string.no_internet));
        }


    }

    private void Submit_HomeWork_withoutfile() {
        if (NetworkUtil.isNetworkAvailable(ADD_HomeWorkActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ADD_HomeWorkActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().addHomework_without_file(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id(), Class_id, Selected_Date, Subject_id, submissionTv.getText().toString(), typeAssignmentTv.getText().toString());
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
                                ErrorMessage.T(ADD_HomeWorkActivity.this, jsonobject.getString("message"));
                            } else {
                                ErrorMessage.T(ADD_HomeWorkActivity.this, jsonobject.getString("message"));
                                materialDialog.dismiss();

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            //ErrorMessage.T(ADD_HomeWorkActivity.this, "Server Error");
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(ADD_HomeWorkActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ADD_HomeWorkActivity.this, "Response Fail");
                    System.out.println("============update profile fail  :" + t.toString());
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(ADD_HomeWorkActivity.this, this.getString(R.string.no_internet));
        }


    }

    private void Submit_UpdateHomeWork() {
        File file;
        file = new File(selectedImagePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("homework_file", file.getName(), requestFile);

        if (NetworkUtil.isNetworkAvailable(ADD_HomeWorkActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ADD_HomeWorkActivity.this);
            RequestBody home_work_id = RequestBody.create(MediaType.parse("text/plain"), Home_work_id);
            RequestBody submission_date = RequestBody.create(MediaType.parse("text/plain"), submissionTv.getText().toString());
            RequestBody description = RequestBody.create(MediaType.parse("text/plain"), typeAssignmentTv.getText().toString());
            Call<ResponseBody> call = AppConfig.getLoadInterface().updateHomework(home_work_id, submission_date, description, body);
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
                                ErrorMessage.T(ADD_HomeWorkActivity.this, jsonobject.getString("message"));
                            } else {
                                ErrorMessage.T(ADD_HomeWorkActivity.this, jsonobject.getString("message"));
                                materialDialog.dismiss();

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            //ErrorMessage.T(ADD_HomeWorkActivity.this, "Server Error");
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(ADD_HomeWorkActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ADD_HomeWorkActivity.this, "Response Fail");
                    System.out.println("============update profile fail  :" + t.toString());
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(ADD_HomeWorkActivity.this, this.getString(R.string.no_internet));
        }


    }

    private void Submit_UpdateHomeWork_withoutfile() {
        if (NetworkUtil.isNetworkAvailable(ADD_HomeWorkActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(ADD_HomeWorkActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().updateHomework_without_file(Home_work_id, submissionTv.getText().toString(), typeAssignmentTv.getText().toString());
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
                                ErrorMessage.T(ADD_HomeWorkActivity.this, jsonobject.getString("message"));
                            } else {
                                ErrorMessage.T(ADD_HomeWorkActivity.this, jsonobject.getString("message"));
                                materialDialog.dismiss();

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            //ErrorMessage.T(ADD_HomeWorkActivity.this, "Server Error");
                            materialDialog.dismiss();
                        }


                    } else {
                        ErrorMessage.T(ADD_HomeWorkActivity.this, "Response not successful");
                        materialDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(ADD_HomeWorkActivity.this, "Response Fail");
                    System.out.println("============update profile fail  :" + t.toString());
                    materialDialog.dismiss();
                }
            });

        } else {
            ErrorMessage.T(ADD_HomeWorkActivity.this, this.getString(R.string.no_internet));
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
