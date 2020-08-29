package com.mahavir_infotech.vidyasthali.Utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;


public class Util {

    //SDF to generate a unique name for our compress file.
    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault());
    private static int THUMBNAIL_SIZE=100;


    /*
        compress the file/photo from @param <b>path</b> to a private location on the current device and return the compressed file.
        @param path = The original image path
        @param context = Current android Context
     */
    public static File getCompressed(Context context, String path) throws IOException {

       /* if(context == null)
            throw new NullPointerException("Context must not be null.");
        //getting device external cache directory, might not be available on some devices,
        // so our code fall back to internal storage cache directory, which is always available but in smaller quantity
        File myDir=new File("/storage/saved_images");
        if (!myDir.exists()) {
            myDir.mkdir();
        }
        //myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "photo_"+ n +".jpg";*/
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "JPEG_"+ n +".jpg";
        File compressed = new File(path);

        Bitmap bitmap = decodeImageFromFiles(path, /* your desired width*/300, /*your desired height*/ 300);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);

        FileOutputStream fileOutputStream = new FileOutputStream(compressed);
        fileOutputStream.write(byteArrayOutputStream.toByteArray());
        fileOutputStream.flush();
        fileOutputStream.close();

        //File written, return to the caller. Done!
        return compressed;
    }
    public static File getCompressed_Gellery(Context context, String path,Uri uri) throws IOException {

        if(context == null)
            throw new NullPointerException("Context must not be null.");

        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/VIDYASTHALI");
            if (!f.isFile()) {
                if (!(f.isDirectory())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        try {
                            Files.createDirectory(Paths.get(f.getAbsolutePath()));
                        } catch (IOException e) {
                            e.printStackTrace();

                        }
                    } else {
                        f.mkdir();
                    }
                }
            }

            Random generator = new Random();
            int n = 10000;
            n = generator.nextInt(n);
            String fname = "photo_" + n + ".jpg";

            File compressed = new File(f, fname);

            Bitmap bitmap = decodeImageFromFiles(path, /* your desired width*/300, /*your desired height*/ 300);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);

            FileOutputStream fileOutputStream = new FileOutputStream(compressed);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
            fileOutputStream.flush();
            fileOutputStream.close();
            //File written, return to the caller. Done!
            return compressed;
    }

    public static Bitmap decodeImageFromFiles(String path, int width, int height) {
        BitmapFactory.Options scaleOptions = new BitmapFactory.Options();
        scaleOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, scaleOptions);
        int scale = 1;
        while (scaleOptions.outWidth / scale / 2 >= width
                && scaleOptions.outHeight / scale / 2 >= height) {
            scale *= 2;
        }
        // decode with the sample size
        BitmapFactory.Options outOptions = new BitmapFactory.Options();
        outOptions.inSampleSize = scale;
        return BitmapFactory.decodeFile(path, outOptions);
    }


}