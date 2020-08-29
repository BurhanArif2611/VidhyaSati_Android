package com.mahavir_infotech.vidyasthali.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataManager extends SQLiteOpenHelper {
  public static final int DATABASE_VERSION = 10;
  public static final String DATABASE_NAME = "VIDYASTHALI";

  public DataManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
    super(context, name, factory, version);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    UserProfileModel.creteTable(db);


  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int paramInt1, int paramInt2) {
    UserProfileModel.dropTable(db);

    onCreate(db);
  }


}
