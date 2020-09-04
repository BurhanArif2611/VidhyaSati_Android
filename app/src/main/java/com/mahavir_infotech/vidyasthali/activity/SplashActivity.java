package com.mahavir_infotech.vidyasthali.activity;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.activity.Student.StudentHomeActivity;
import com.mahavir_infotech.vidyasthali.activity.Teacher.TeacherHomeActivity;
import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

public class SplashActivity extends AppCompatActivity {
    public final int PERMISSION_REQUEST_CODE = 1111;

    AppUpdateManager appUpdateManager;
    Task<AppUpdateInfo> appUpdateInfoTask;
    final int MY_REQUEST_CODE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appUpdateManager = AppUpdateManagerFactory.create(this);
        appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        // For a flexible update, use AppUpdateType.FLEXIBLE
                        && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {
                    // Request the update.
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                // Pass the intent that is returned by 'getAppUpdateInfo()'.
                                appUpdateInfo,
                                // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                                IMMEDIATE,
                                // The current activity making the update request.
                                SplashActivity.this,
                                // Include a request code to later monitor this update request.
                                MY_REQUEST_CODE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkPermission()) {
                            ErrorMessage.E("checkPermission is come");
                            checkTimerIntent();
                        } else {
                            ErrorMessage.E("requestPermission is come");
                            requestPermission();

                        }
                    } else {
                        checkTimerIntent();
                    }
                }
            }
        });
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                ErrorMessage.E("checkPermission is come");
                checkTimerIntent();
            } else {
                ErrorMessage.E("requestPermission is come");
                requestPermission();

            }
        } else {
            checkTimerIntent();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CAMERA, READ_CONTACTS, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, READ_PHONE_STATE, " "}, PERMISSION_REQUEST_CODE);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result4 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result5 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);


        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED && result5 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ContactAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean ExtStorageAccepted = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean WriteStorageAccepted = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadSMSAccepted = grantResults[5] == PackageManager.PERMISSION_GRANTED;


                    if (locationAccepted && cameraAccepted && ContactAccepted && ExtStorageAccepted && WriteStorageAccepted && ReadSMSAccepted) {
                        checkTimerIntent();
                        //   ErrorMessage.T(SplashActivity.this, "Permission Granted, Now you can access location data and camera.");
                    }    //Snackbar.make(view, "Permission Granted, Now you can access location data and camera.", Snackbar.LENGTH_LONG).show();

                    else {
                        checkTimerIntent();
                        //   ErrorMessage.T(SplashActivity.this, "Permission Denied, You cannot access location data and camera.");
                        //   Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, CAMERA, READ_CONTACTS, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
                                    checkTimerIntent();
                                }
                            }
                        }
                        return;
                    }
                } else {

                }
                break;
        }
    }

    private void checkTimerIntent() {
        ErrorMessage.E("checkTimerIntent is come");
        /*final ImageView imageViewlogo = (ImageView) findViewById(R.id.id_imagelogo);
        Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_animation);
        imageViewlogo.startAnimation(startAnimation);*/

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                try {
                    if (UserProfileHelper.getInstance().getUserProfileModel().size() > 0) {
                        if (UserProfileHelper.getInstance().getUserProfileModel().get(0).getRole().equals("teacher")) {
                            ErrorMessage.I_clear(SplashActivity.this, TeacherHomeActivity.class, null);
                        } else {
                            ErrorMessage.I_clear(SplashActivity.this, StudentHomeActivity.class, null);
                        }
                    } else {
                        ErrorMessage.I_clear(SplashActivity.this, WelcomeActivity.class, null);
                    }
                } catch (Exception e) {
                    ErrorMessage.I_clear(SplashActivity.this, WelcomeActivity.class, null);
                }

            }
        }, 3000);
    }
    @Override
    protected void onResume() {
        super.onResume();
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {

                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    // If an in-app update is already running, resume the update.

                    try {
                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo, IMMEDIATE, SplashActivity.this, MY_REQUEST_CODE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Log.e("Else is working","");

                }
            }
        });
    }
}
