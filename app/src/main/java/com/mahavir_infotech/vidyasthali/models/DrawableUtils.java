package com.mahavir_infotech.vidyasthali.models;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;

import androidx.core.content.ContextCompat;

import com.applandeo.materialcalendarview.CalendarUtils;
import com.mahavir_infotech.vidyasthali.R;


public class DrawableUtils {

    public static Drawable getCircleDrawableWithText(Context context,String string) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.demo);
        Drawable text = CalendarUtils.getDrawableText(context, string, null, android.R.color.white, 12);

        Drawable[] layers = {background, text};
        return new LayerDrawable(layers);
    }
    public static Drawable getCircleDrawableWithText_red(Context context,String string) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.demo_red);
        Drawable text = CalendarUtils.getDrawableText(context, string, null, android.R.color.white, 12);

        Drawable[] layers = {background, text};
        return new LayerDrawable(layers);
    }

    public static Drawable getThreeDots(Context context){
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.demo);

        //Add padding to too large icon
        return new InsetDrawable(drawable, 10, 50, 10, 50);
    }

    private DrawableUtils() {
    }
}
