package com.myeverydaybaby.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.myeverydaybaby.models.Baby;

import java.util.List;

/**
 * Created by David on 7/19/13.
 */
public class EveryDayBabyDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String  DATABASE_NAME = "EveryDayBaby.db";

    public EveryDayBabyDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL( EveryDayBabyContract.SQL_CREATE_BABIES );
       db.execSQL( EveryDayBabyContract.SQL_CREATE_FEEDINGS );
       db.execSQL( EveryDayBabyContract.SQL_CREATE_DIAPERS );
       db.execSQL( EveryDayBabyContract.SQL_CREATE_SLEEP );
       db.execSQL( EveryDayBabyContract.SQL_CREATE_STATISTICS );

       // Insert default baby
        ContentValues values = new ContentValues();
        values.put( EveryDayBabyContract.Babies.COLUMN_NAME_NAME, "My Baby");
        db.insert(EveryDayBabyContract.Babies.TABLE_NAME, EveryDayBabyContract.Babies.COLUMN_NAME_PICTURE, values );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
