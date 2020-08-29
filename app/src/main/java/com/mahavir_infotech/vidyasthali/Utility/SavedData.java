package com.mahavir_infotech.vidyasthali.Utility;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mahavir_infotech.vidyasthali.MyApplication;


public class SavedData {
    private static final String FCM_ID = "fcm_id";
    private static final String LAT = "lat";
    private static final String LONGITUTDE = "long";
    private static final String AddToCart_Count = "addtocart";
    private static final String Notification_Count = "Notification";
    private static final String TOKAN = "tokan";

    static SharedPreferences prefs;
    public static SharedPreferences getInstance() {
        if (prefs == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance());
        }

        return prefs;
    }

    public static String getFCM_ID() {
        return getInstance().getString(FCM_ID, "");
    }

    public static void saveFCM_ID(String fcm) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(FCM_ID, fcm);
        editor.apply();
    }
    public static String getLAT() {
        return getInstance().getString(LAT, "");
    }

    public static void saveLAT(String fcm) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(LAT, fcm);
        editor.apply();
    } public static String getLONGITUTDE() {
        return getInstance().getString(LONGITUTDE, "");
    }

    public static void saveLONGITUTDE(String fcm) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(LONGITUTDE, fcm);
        editor.apply();
    }

    public static void saveAddToCart_Count(String fg) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(AddToCart_Count, fg);
        editor.apply();
    }

    public static String getAddToCart_Count() {
        return getInstance().getString(AddToCart_Count, "0");
    }

    public static void saveNotification_Count(String fg) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(Notification_Count, fg);
        editor.apply();
    }

    public static String getNotification_Count() {
        return getInstance().getString(Notification_Count, "0");
    }
    public static String getTokan() {
        return getInstance().getString(TOKAN, "");
    }

    public static void saveTokan(String tokan) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(TOKAN, tokan);
        editor.apply();
    }
}
