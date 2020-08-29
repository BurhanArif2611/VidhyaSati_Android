package com.mahavir_infotech.vidyasthali.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.AppConfig;
import com.mahavir_infotech.vidyasthali.Utility.Constant;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.Utility.LoadInterface;
import com.mahavir_infotech.vidyasthali.Utility.NetworkUtil;
import com.mahavir_infotech.vidyasthali.Utility.UserAccount;
import com.mahavir_infotech.vidyasthali.Utility.Util;
import com.mahavir_infotech.vidyasthali.activity.Student.StudentHomeActivity;
import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;
import com.mahavir_infotech.vidyasthali.database.UserProfileModel;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class UpdateProfileActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.title_txt)
    TextView titleTextTv;
    @BindView(R.id.proffileImage)
    CircleImageView proffileImage;
    @BindView(R.id.takeimage)
    ImageView takeimage;
    @BindView(R.id.user_name_etv)
    EditText userNameEtv;
    @BindView(R.id.email_id_etv)
    EditText emailIdEtv;
    @BindView(R.id.mobile_etv)
    EditText mobileEtv;
    @BindView(R.id.signup_btn)
    Button signupBtn;
    @BindView(R.id.user_name_tv)
    TextView userNameTv;
    @BindView(R.id.change_password_btn)
    Button changePasswordBtn;
    @BindView(R.id.father_name_etv)
    EditText fatherNameEtv;
    @BindView(R.id.mother_name_etv)
    EditText motherNameEtv;
    @BindView(R.id.dob_etv)
    EditText dobEtv;
    @BindView(R.id.student_layout)
    LinearLayout studentLayout;
    @BindView(R.id.edit_profile_img)
    ImageButton editProfileImg;


    private Dialog dialog;
    private EditText otp_et, verify_phone_et;
    private String mVerificationId;
    Unbinder unbinder;
    public static final int PERMISSION_REQUEST_CODE = 1111;
    private static final int REQUEST = 1337;
    public static int SELECT_FROM_GALLERY = 2;
    public static int CAMERA_PIC_REQUEST = 0;
    Uri mImageCaptureUri;
    private File finalFile;

    private String Camera_bitmap = "";
    String Check_status = "";
    private Menu menu;
    private File file;
    String Role = "";

    @Override
    protected int getContentResId() {
        return R.layout.activity_update_profile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        titleTextTv.setText("Update Profile");

        if (UserProfileHelper.getInstance().getUserProfileModel().size() > 0) {
            userNameTv.setText(UserProfileHelper.getInstance().getUserProfileModel().get(0).getDisplayName());
            userNameEtv.setText(UserProfileHelper.getInstance().getUserProfileModel().get(0).getDisplayName());
            emailIdEtv.setText(UserProfileHelper.getInstance().getUserProfileModel().get(0).getEmaiiId());
            mobileEtv.setText(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUserPhone());
            try {
                Role = UserProfileHelper.getInstance().getUserProfileModel().get(0).getRole();
                if (Role.contains("teacher")) {
                    studentLayout.setVisibility(View.GONE);
                    mobileEtv.setVisibility(View.GONE);
                    //Get_TeacherProfile();
                } else if (Role.contains("parent")){
                    studentLayout.setVisibility(View.VISIBLE);
                    Get_StudentProfile();

                }else {
                    studentLayout.setVisibility(View.VISIBLE);
                    Get_StudentProfile();

                }
            } catch (Exception e) {
            }
            if (UserProfileHelper.getInstance().getUserProfileModel().get(0).getProfile_pic() != null && !UserProfileHelper.getInstance().getUserProfileModel().get(0).getProfile_pic().equals("")) {
                Picasso.with(UpdateProfileActivity.this).load(UserProfileHelper.getInstance().getUserProfileModel().get(0).getProfile_pic()).placeholder(R.drawable.ic_defult_user).into(proffileImage);
            }
        }
    }

    @OnClick({R.id.takeimage, R.id.signup_btn, R.id.change_password_btn, R.id.dob_etv, R.id.edit_profile_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.takeimage:
                selectImage();
                break;
            case R.id.change_password_btn:
                ErrorMessage.I(UpdateProfileActivity.this, ChangePasswordActivity.class, null);
                break;
            case R.id.dob_etv:
                datepicker();
                break;
            case R.id.edit_profile_img:
                if (Check_status.equals("")) {
                    userNameEtv.setEnabled(true);
                    emailIdEtv.setEnabled(true);
                    mobileEtv.setEnabled(true);
                    fatherNameEtv.setEnabled(true);
                    motherNameEtv.setEnabled(true);
                    dobEtv.setEnabled(true);
                    changePasswordBtn.setVisibility(View.GONE);
                    signupBtn.setVisibility(View.VISIBLE);
                    takeimage.setVisibility(View.VISIBLE);

                    editProfileImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_password_colour));
                    Check_status = "pass";
                } else if (Check_status.equals("pass")) {
                    userNameEtv.setEnabled(false);
                    emailIdEtv.setEnabled(false);
                    mobileEtv.setEnabled(false);
                    fatherNameEtv.setEnabled(false);
                    motherNameEtv.setEnabled(false);
                    dobEtv.setEnabled(true);
                    changePasswordBtn.setVisibility(View.VISIBLE);
                    signupBtn.setVisibility(View.GONE);
                    takeimage.setVisibility(View.GONE);

                    editProfileImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit_profile));
                    Check_status = "";
                }
                break;
            case R.id.signup_btn:
                if (UserAccount.isEmpty(userNameEtv, emailIdEtv)) {
                    if (UserAccount.isEmailValid(emailIdEtv)) {
                        if (Role.contains("teacher")) {
                            updateProfileApi();
                        } else {
                            updateStudentProfileApi();
                        }
                    } else {
                        UserAccount.EditTextPointer.setError("Email-ID Invalid !");
                        UserAccount.EditTextPointer.requestFocus();
                    }
                } else {
                    UserAccount.EditTextPointer.setError("This Field Can't be Empty !");
                    UserAccount.EditTextPointer.requestFocus();
                }
                break;

        }
    }

    private void selectImage() {
        final CharSequence[] options = {"From Camera", "From Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.create().show();
    }

    /* private void galleryIntent() {
         Intent intent = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
         startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FROM_GALLERY);
     }*/
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
        ActivityCompat.requestPermissions(UpdateProfileActivity.this, new String[]{CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void requestGalleryPermission() {
        ActivityCompat.requestPermissions(UpdateProfileActivity.this, new String[]{READ_EXTERNAL_STORAGE}, REQUEST);
    }

    private boolean checkCameraPermission() {
        int result1 = ContextCompat.checkSelfPermission(UpdateProfileActivity.this, CAMERA);
        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkGalleryPermission() {
        int result2 = ContextCompat.checkSelfPermission(UpdateProfileActivity.this, READ_EXTERNAL_STORAGE);
        return result2 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {
            Glide.with(this).load(Camera_bitmap).into(proffileImage);
            try {
                file = Util.getCompressed(this, Camera_bitmap);
                Camera_bitmap = file.getPath();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == SELECT_FROM_GALLERY && resultCode == Activity.RESULT_OK && null != data) {
            Uri galleryURI = data.getData();
            proffileImage.setImageURI(galleryURI);
            Camera_bitmap = getRealPathFromURIPath(galleryURI, UpdateProfileActivity.this);
            try {
                file = Util.getCompressed_Gellery(this, Camera_bitmap, galleryURI);
                Camera_bitmap = file.getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
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


    private File storeCameraPhotoInSDCard(Bitmap bitmap, String currentDate) {
        File outputFile1 = new File(Environment.getExternalStorageDirectory(), "Hind MPPSC");
        if (!outputFile1.exists()) {
            outputFile1.mkdir();
        }
        File outputFile = new File(outputFile1, "photo_" + currentDate + ".jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputFile;
    }

    private Bitmap getImageFileFromSDCard(String filename) {
        Bitmap bitmap = null;
        File imageFile = new File(Environment.getExternalStorageDirectory() + filename);
        try {
            FileInputStream fis = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(UpdateProfileActivity.this, "com.mahavir_infotech.vidyasthali.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, CAMERA_PIC_REQUEST);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */);
        Camera_bitmap = image.getAbsolutePath();
        return image;
    }


    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }


    private void updateProfileApi() {
        ErrorMessage.E("selectImagePath in Update Profile" + Camera_bitmap);
        if (NetworkUtil.isNetworkAvailable(UpdateProfileActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(UpdateProfileActivity.this);
            Call<ResponseBody> call = null;
            if (!Camera_bitmap.equals("")) {

                final RequestBody requestfile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("profile_image", Camera_bitmap, requestfile);
                Log.e("rrrr", "" + body.toString());
                RequestBody fullname = RequestBody.create(MediaType.parse("text/plain"), userNameEtv.getText().toString());

                RequestBody auth_token = RequestBody.create(MediaType.parse("text/plain"), UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
                RequestBody email = RequestBody.create(MediaType.parse("text/plain"), emailIdEtv.getText().toString());
                LoadInterface apiService = AppConfig.getClient().create(LoadInterface.class);
                call = apiService.updateProfile(auth_token, fullname, email, body);
            } else {
                LoadInterface apiService = AppConfig.getClient().create(LoadInterface.class);
                call = apiService.updateProfile_withoutimage(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id(), userNameEtv.getText().toString(), emailIdEtv.getText().toString());
            }

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        materialDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("==========profile update  :" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                ErrorMessage.E("comes in if cond" + jsonObject.toString());
                                JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                                ErrorMessage.T(UpdateProfileActivity.this, jsonObject.getString("message"));
                                UserProfileModel userProfileModel = new UserProfileModel();
                                userProfileModel.setDisplayName(jsonObject1.getString("name"));
                                userProfileModel.setUser_id(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
                                userProfileModel.setEmaiiId(jsonObject1.getString("email"));
                                userProfileModel.setRole(Role);
                                userProfileModel.setUserPhone(mobileEtv.getText().toString());
                                if (jsonObject1.getJSONObject("photo").equals(JSONObject.NULL)) {
                                    ErrorMessage.E("is is runing");
                                    userProfileModel.setProfile_pic("");
                                } else {
                                    ErrorMessage.E("is is not runing");
                                    JSONObject jsonObject2 = jsonObject1.getJSONObject("photo");
                                    if (jsonObject2.getString("url").contains("http")) {
                                        userProfileModel.setProfile_pic(jsonObject2.getString("url"));
                                    }else {
                                        userProfileModel.setProfile_pic(Constant.Image_BASE_URL + jsonObject2.getString("url"));

                                    }
                                }
                                UserProfileHelper.getInstance().delete();
                                UserProfileHelper.getInstance().insertUserProfileModel(userProfileModel);
                                finish();
                            } else if (jsonObject.getString("status").equals("403")) {
                                ErrorMessage.T(UpdateProfileActivity.this, jsonObject.getString("message"));
                                UserProfileHelper.getInstance().delete();

                            } else {
                                ErrorMessage.T(UpdateProfileActivity.this, jsonObject.getString("message"));
                                materialDialog.dismiss();
                                System.out.println("==========response not geting 400 :");

                            }
                        } catch (JSONException e) {
                            materialDialog.dismiss();
                            ErrorMessage.E("response not:" + e.toString());

                        } catch (Exception e) {
                            e.printStackTrace();
                            ErrorMessage.E("response not:" + e.toString());
                        }

                    } else {
                        materialDialog.dismiss();
                        System.out.println("==========response not success :");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    materialDialog.dismiss();
                    System.out.println("==========response faiil :");
                }
            });


        } else {
            ErrorMessage.T(UpdateProfileActivity.this, this.getString(R.string.no_internet));
        }
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_password:
                if (Check_status.equals("")) {
                    userNameEtv.setEnabled(true);
                    emailIdEtv.setEnabled(true);
                    mobileEtv.setEnabled(true);
                    changePasswordBtn.setVisibility(View.GONE);
                    signupBtn.setVisibility(View.VISIBLE);

                    menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_password_colour));
                    Check_status = "pass";
                } else if (Check_status.equals("pass")) {
                    userNameEtv.setEnabled(false);
                    emailIdEtv.setEnabled(false);
                    mobileEtv.setEnabled(false);
                    changePasswordBtn.setVisibility(View.VISIBLE);
                    signupBtn.setVisibility(View.GONE);

                    menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_edit_profile));

                    Check_status = "";
                }
                break;
        }
        return true;
    }*/

    private void updateStudentProfileApi() {
        ErrorMessage.E("selectImagePath in Update Profile" + Camera_bitmap + ">>" + emailIdEtv.getText().toString());
        if (NetworkUtil.isNetworkAvailable(UpdateProfileActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(UpdateProfileActivity.this);
            Call<ResponseBody> call = null;
            if (!Camera_bitmap.equals("")) {
                final RequestBody requestfile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("profile_image", Camera_bitmap, requestfile);
                Log.d("rrrr", body.toString());
                RequestBody fullname = RequestBody.create(MediaType.parse("text/plain"), userNameEtv.getText().toString());

                RequestBody auth_token = RequestBody.create(MediaType.parse("text/plain"), UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
                RequestBody email = RequestBody.create(MediaType.parse("text/plain"), emailIdEtv.getText().toString());
                RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), mobileEtv.getText().toString());
                RequestBody father_name = RequestBody.create(MediaType.parse("text/plain"), fatherNameEtv.getText().toString());
                RequestBody mother_name = RequestBody.create(MediaType.parse("text/plain"), motherNameEtv.getText().toString());
                RequestBody DOB = RequestBody.create(MediaType.parse("text/plain"), dobEtv.getText().toString());
                LoadInterface apiService = AppConfig.getClient().create(LoadInterface.class);
                call = apiService.updateStudentProfile(auth_token, fullname, email, father_name, mother_name, DOB, body);
            } else {
                LoadInterface apiService = AppConfig.getClient().create(LoadInterface.class);
                call = apiService.updateStudentProfile_withoutimage(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id(), userNameEtv.getText().toString(), emailIdEtv.getText().toString(), fatherNameEtv.getText().toString(), motherNameEtv.getText().toString(), dobEtv.getText().toString());
            }

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        materialDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("==========profile update  :" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                ErrorMessage.E("comes in if cond" + jsonObject.toString());
                                JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                                ErrorMessage.T(UpdateProfileActivity.this, jsonObject.getString("message"));
                                UserProfileModel userProfileModel = new UserProfileModel();
                                userProfileModel.setDisplayName(jsonObject1.getString("name"));
                                userProfileModel.setUser_id(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
                                userProfileModel.setEmaiiId(jsonObject1.getString("email"));
                                userProfileModel.setRole(Role);
                                userProfileModel.setUserPhone(mobileEtv.getText().toString());
                                userProfileModel.setProfile_pic(jsonObject1.getString("profile_image"));
                                UserProfileHelper.getInstance().delete();
                                UserProfileHelper.getInstance().insertUserProfileModel(userProfileModel);
                                ErrorMessage.I_clear(UpdateProfileActivity.this, StudentHomeActivity.class, null);
                            } else if (jsonObject.getString("status").equals("403")) {
                                ErrorMessage.T(UpdateProfileActivity.this, jsonObject.getString("message"));
                                UserProfileHelper.getInstance().delete();

                            } else {
                                ErrorMessage.T(UpdateProfileActivity.this, jsonObject.getString("message"));
                                materialDialog.dismiss();
                                System.out.println("==========response not geting 400 :");

                            }
                        } catch (JSONException e) {
                            materialDialog.dismiss();
                            System.out.println("==========response not  :" + e.toString());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        materialDialog.dismiss();
                        System.out.println("==========response not success :");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    materialDialog.dismiss();
                    System.out.println("==========response faiil :");
                }
            });


        } else {
            ErrorMessage.T(UpdateProfileActivity.this, this.getString(R.string.no_internet));
        }
    }

    private void datepicker() {
        Calendar calendar = Calendar.getInstance();
        //calendar.add(Calendar.DAY_OF_MONTH, 1);
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(UpdateProfileActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        datePickerDialog.setMaxDate(calendar);
        datePickerDialog.show(getFragmentManager(), "Date Picker");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int eventYear, int monthOfYear, int dayOfMonth) {
        if (monthOfYear + 1 < 10 && dayOfMonth > 10)
            dobEtv.setText((dayOfMonth + "-0" + (monthOfYear + 1) + "-" + eventYear));
        else if (dayOfMonth < 10 && monthOfYear + 1 >= 10)
            dobEtv.setText((dayOfMonth + "-" + (monthOfYear + 1) + "-" + eventYear));
        else if (monthOfYear + 1 < 10 && dayOfMonth < 10)
            dobEtv.setText((dayOfMonth + "-0" + (monthOfYear + 1) + "-" + eventYear));
        else dobEtv.setText((dayOfMonth + "-" + (monthOfYear + 1) + "-" + eventYear));


    }

    public void Get_TeacherProfile() {
        if (NetworkUtil.isNetworkAvailable(UpdateProfileActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(UpdateProfileActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().getTeacherProfile(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            materialDialog.dismiss();
                            Gson gson = new Gson();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("Get_TeacherProfile" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                                //  mobileEtv.setText(jsonObject1.getString("student_contact"));
                                emailIdEtv.setText(jsonObject1.getString("email"));
                                //dobEtv.setText(jsonObject1.getString("dob"));

                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("productdetail error" + e.toString());
                            //  ErrorMessage.T(UpdateProfileActivity.this, "Server Error");
                            materialDialog.dismiss();

                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();

                        }


                    } else {
                        ErrorMessage.T(UpdateProfileActivity.this, "Response not successful");
                        materialDialog.dismiss();

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(UpdateProfileActivity.this, "Response Fail");
                    materialDialog.dismiss();

                }
            });

        } else {
            ErrorMessage.T(UpdateProfileActivity.this, getResources().getString(R.string.no_internet));

        }
    }

    public void Get_StudentProfile() {
        if (NetworkUtil.isNetworkAvailable(UpdateProfileActivity.this)) {
            final Dialog materialDialog = ErrorMessage.initProgressDialog(UpdateProfileActivity.this);
            Call<ResponseBody> call = AppConfig.getLoadInterface().getStudentProfile(UserProfileHelper.getInstance().getUserProfileModel().get(0).getUser_id());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
                        try {
                            materialDialog.dismiss();
                            jsonObject = new JSONObject(response.body().string());
                            ErrorMessage.E("getStudentProfile" + jsonObject.toString());
                            if (jsonObject.getString("status").equals("1")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                                fatherNameEtv.setText(jsonObject1.getString("father_name"));
                                motherNameEtv.setText(jsonObject1.getString("mother_name"));
                                mobileEtv.setText(jsonObject1.getString("student_contact"));
                                emailIdEtv.setText(jsonObject1.getString("email"));
                                dobEtv.setText(jsonObject1.getString("dob"));

                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ErrorMessage.E("productdetail error" + e.toString());
                            //  ErrorMessage.T(UpdateProfileActivity.this, "Server Error");
                            materialDialog.dismiss();

                        } catch (Exception e) {
                            e.printStackTrace();
                            materialDialog.dismiss();

                        }


                    } else {
                        ErrorMessage.T(UpdateProfileActivity.this, "Response not successful");
                        materialDialog.dismiss();

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ErrorMessage.T(UpdateProfileActivity.this, "Response Fail");
                    materialDialog.dismiss();

                }
            });

        } else {
            ErrorMessage.T(UpdateProfileActivity.this, getResources().getString(R.string.no_internet));

        }
    }
}
