package com.mahavir_infotech.vidyasthali;

import android.content.Context;

import androidx.annotation.IntDef;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.mahavir_infotech.vidyasthali.database.UserProfileHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class MyApplication extends MultiDexApplication {
    public static final String TAG = MyApplication.class.getSimpleName();

    private static MyApplication mInstance;

   // private AppLocationListener mListener;

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
   /* @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
*/
    @Retention(RetentionPolicy.SOURCE)
    @IntDef(
            {GRANTED, DENIED, BLOCKED})
    public @interface PermissionStatus {
    }

    public static final int GRANTED = 0;
    public static final int DENIED = 1;
    public static final int BLOCKED = 2;

    /*@AppController.PermissionStatus
    public static int getPermissionStatus(Activity activity, String androidPermissionName) {
        if (ContextCompat.checkSelfPermission(activity, androidPermissionName) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName)) {
                return BLOCKED;
            }
            return DENIED;
        }
        return GRANTED;
    }*/





    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        // initialize the AdMob app
       new UserProfileHelper(this);
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
}

