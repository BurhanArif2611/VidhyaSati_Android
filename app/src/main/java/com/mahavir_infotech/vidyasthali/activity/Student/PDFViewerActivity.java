package com.mahavir_infotech.vidyasthali.activity.Student;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.ErrorMessage;
import com.mahavir_infotech.vidyasthali.activity.BaseActivity;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PDFViewerActivity extends BaseActivity implements OnPageChangeListener, OnLoadCompleteListener, OnPageErrorListener {

    @BindView(R.id.title_txt)
    TextView titleTextTv;
    @BindView(R.id.pdfView)
    PDFView pdfView;
    Integer pageNumber = 0;

    private static final String TAG = PDFViewerActivity.class.getSimpleName();
    private final static int REQUEST_CODE = 42;
    public static final int PERMISSION_CODE = 42042;
    public String SAMPLE_FILE = "";
    public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    String pdfFileName;
    @BindView(R.id.no_data_found_tv)
    TextView noDataFoundTv;
    @BindView(R.id.download_file_btn)
    ImageButton downloadFileBtn;
    static AlertDialog alertDialog;
    Uri path;

    @Override
    protected int getContentResId() {
        return R.layout.activity_pdfviewer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("");
        ButterKnife.bind(this);
        int permissionCheck = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
            return;
        }
        //launchPicker();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            titleTextTv.setText(bundle.getString("title"));
            //displayFromUri(Uri.parse(bundle.getString("image")));
            try {
                SAMPLE_FILE = bundle.getString("image");
                if (!SAMPLE_FILE.equals("")) {

                    String last = SAMPLE_FILE.substring(SAMPLE_FILE.length() - 1);
                    ErrorMessage.E("SAMPLE_FILE" + last+">>"+SAMPLE_FILE.length());
                    if (last.equals(".")) {
                        SAMPLE_FILE = SAMPLE_FILE.substring(0, SAMPLE_FILE.lastIndexOf(".")) + "";
                        ErrorMessage.E("SAMPLE_FILE if>>" + SAMPLE_FILE);
                        new DownloadTask(PDFViewerActivity.this, SAMPLE_FILE);
                    } else {
                        ErrorMessage.E("SAMPLE_FILE" + SAMPLE_FILE);
                        new DownloadTask(PDFViewerActivity.this, SAMPLE_FILE);
                    }
                } else {
                    ErrorMessage.T(PDFViewerActivity.this, "File Not Found !");
                }
            } catch (Exception e) {
                ErrorMessage.E("SAMPLE_FILE Exception>>" + e.toString());
            }

        }
    }

    private void displayFromUri(Uri uri) {
        ErrorMessage.E("displayFromUri" + uri);
        pdfFileName = getFileName(uri);

        ErrorMessage.E("displayFromUri" + pdfFileName);
       try {
           if (pdfFileName.contains(".docx") || pdfFileName.contains(".doc")) {
               openDownloadedFolder(uri);
           } else {
               pdfView.fromUri(uri).defaultPage(pageNumber).onPageChange(PDFViewerActivity.this).enableAnnotationRendering(true).onLoad(this).scrollHandle(new DefaultScrollHandle(this)).spacing(10) // in dp
                       .onPageError(this).load();
           }
       }catch (Exception e){}
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

   /* @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }*/

    @Override
    public void onPageError(int page, Throwable t) {

    }

    void launchPicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            //alert user that file manager not working
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayFromAsset(String assetFileName) {
        pdfFileName = assetFileName;

        pdfView.fromAsset(SAMPLE_FILE).defaultPage(pageNumber).enableSwipe(true).swipeHorizontal(false).onPageChange(this).enableAnnotationRendering(true).onLoad(this).scrollHandle(new DefaultScrollHandle(this)).load();
    }


    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }


    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    @OnClick(R.id.download_file_btn)
    public void onClick() {
        if (path != null && !path.equals(Uri.EMPTY)) {
            //doTheThing()
            alertDialog = new AlertDialog.Builder(PDFViewerActivity.this).create();
            alertDialog.setMessage("Downloading Complete...");
            alertDialog.setTitle(path.toString());
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Open", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    openDownloadedFolder(path);
                }
            });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        } else {
            //followUri is null or empty
            new DownloadTask(PDFViewerActivity.this, SAMPLE_FILE);
        }
    }

    public class DownloadTask {
        private static final String TAG = "Download Task";
        private Context context;

        private String downloadUrl = "", downloadFileName = "";
        private ProgressDialog progressDialog;

        public DownloadTask(Context context, String downloadUrl) {
            this.context = context;

            this.downloadUrl = downloadUrl;


            downloadFileName = downloadUrl.substring(downloadUrl.lastIndexOf('/'), downloadUrl.length());//Create file name by picking download file name from URL
            if (downloadFileName.contains(".doc") || downloadFileName.contains(".docx")|| downloadFileName.contains(".pdf")|| downloadFileName.contains(".PDF"))
            {}else {
                downloadFileName=downloadFileName+".pdf";
            }
            Log.e(TAG, downloadFileName);

            //Start Downloading Task
            new DownloadingTask().execute();
        }

        private class DownloadingTask extends AsyncTask<Void, Void, Void> {

            File apkStorage = null;
            File outputFile = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Processing...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(Void result) {
                try {
                    if (outputFile != null) {
                        progressDialog.dismiss();
                        //ContextThemeWrapper ctw = new ContextThemeWrapper( context, R.style.Theme_AlertDialog);
                        File pdfFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/CodePlayon/" + downloadFileName);  // -> filename = maven.pdf
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            path = FileProvider.getUriForFile(PDFViewerActivity.this, "com.mahavir_infotech.vidyasthali.provider", pdfFile);
                        } else {
                            path = Uri.fromFile(pdfFile);
                        }

                        displayFromUri(path);

                       /* final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        alertDialogBuilder.setTitle("PDF Document  ");
                        alertDialogBuilder.setMessage("Wait for process ");
                        alertDialogBuilder.setCancelable(false);

                        alertDialogBuilder.setNegativeButton("Open report",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                File pdfFile = new File(Environment.getExternalStorageDirectory() + "/CodePlayon/" + downloadFileName);  // -> filename = maven.pdf
                                Uri path = Uri.fromFile(pdfFile);
                              *//*  Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                                pdfIntent.setDataAndType(path, "application/pdf");
                                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                try{
                                    context.startActivity(pdfIntent);
                                }catch(ActivityNotFoundException e){
                                    Toast.makeText(context, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
                                }*//*

                                displayFromUri(path);
                            }
                        });
                        alertDialogBuilder.show();*/
//                    Toast.makeText(context, "Document Downloaded Successfully", Toast.LENGTH_SHORT).show();
                    } else {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                            }
                        }, 3000);

                        Log.e(TAG, "Download Failed");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //Change button text if exception occurs
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 3000);

                    Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());
                    noDataFoundTv.setVisibility(View.VISIBLE);
                    pdfView.setVisibility(View.GONE);
                }


                super.onPostExecute(result);
            }

            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... arg0) {
                try {
                    URL url = new URL(downloadUrl);//Create Download URl
                    HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                    c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                    c.connect();//connect the URL Connection

                    //If Connection response is not OK then show Logs
                    if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        Log.e(TAG, "Server returned HTTP " + c.getResponseCode() + " " + c.getResponseMessage());

                    }


                    //Get File if SD card is present
                    if (new CheckForSDCard().isSDCardPresent()) {

                        apkStorage = new File(Environment.getExternalStorageDirectory() + "/" + "CodePlayon");
                    } else
                        Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

                    //If File is not present create directory
                    if (!apkStorage.exists()) {
                        apkStorage.mkdir();
                        Log.e(TAG, "Directory Created.");
                    }

                    outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File

                    //Create New File if not present
                    if (!outputFile.exists()) {
                        outputFile.createNewFile();
                        Log.e(TAG, "File Created");
                    }

                    FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                    InputStream is = c.getInputStream();//Get InputStream for connection

                    byte[] buffer = new byte[1024];//Set buffer type
                    int len1 = 0;//init length
                    while ((len1 = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len1);//Write new file
                    }

                    //Close all connection after doing task
                    fos.close();
                    is.close();

                } catch (Exception e) {

                    //Read exception if something went wrong
                    e.printStackTrace();
                    outputFile = null;
                    Log.e(TAG, "Download Error Exception " + e.getMessage());
                    progressDialog.dismiss();
                    Calling();
                }

                return null;
            }
        }
    }

    public class CheckForSDCard {
        //Check If SD Card is present or not method
        public boolean isSDCardPresent() {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                return true;
            }
            return false;
        }
    }

    public void Calling() {
        PDFViewerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
              /*  noDataFoundTv.setVisibility(View.VISIBLE);
                pdfView.setVisibility(View.GONE);*/
                try {
                    new DownloadTask(PDFViewerActivity.this, SAMPLE_FILE+".");
                }catch (Exception e){}

            }
        });
    }

    private void openDownloadedFolder(Uri path) {
        //First check if SD Card is present or not
        try {
            ErrorMessage.E("Path??" + path);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if (path.toString().contains(".docx") || path.toString().contains(".doc")) {
                intent.setDataAndType(path, "application/msword");
            } else {
                intent.setDataAndType(path, "application/msword");
            }
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);

        } catch (Exception e) {
            ErrorMessage.E("Exception" + e.toString());
            Toast.makeText(this, "Application not found", Toast.LENGTH_SHORT).show();
        }


    }
}
