package com.myeverydaybaby.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.myeverydaybaby.exceptions.EntityNotFoundException;
import com.myeverydaybaby.models.Baby;

import org.w3c.dom.Comment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static com.myeverydaybaby.persistence.EveryDayBabyContract.Babies;

public class BabyDAO {

    private SQLiteDatabase database;
    private EveryDayBabyDbHelper dbHelper;

    private String[] allColumns = {
        Babies._ID,
        Babies.COLUMN_NAME_NAME,
        Babies.COLUMN_NAME_BIRTHDAY,
        Babies.COLUMN_NAME_PICTURE
    };

    public BabyDAO(Context context){
        dbHelper = new EveryDayBabyDbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Baby createBaby(Baby baby){
        ContentValues values = new ContentValues();
        values.put(Babies.COLUMN_NAME_NAME, baby.getName());
        values.put(Babies.COLUMN_NAME_BIRTHDAY, baby.getBirthday());
        values.put(Babies.COLUMN_NAME_PICTURE, baby.getPicture());
        long babyId = database.insert(Babies.TABLE_NAME, null, values);
        baby.setId(babyId);
        return baby;
    }

    public Baby getBaby(long id) throws EntityNotFoundException{
        Cursor cursor = database.query(Babies.TABLE_NAME, allColumns, Babies._ID + " = " + id, null, null, null, null);
        try {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                Baby baby = getBabyFromCursor(cursor);
                return baby;
            }
        } finally {
            cursor.close();
        }
        throw new EntityNotFoundException();
    }

    public List<Baby> getBabies(){
        List<Baby> babyList = new ArrayList<Baby>();
        // public Cursor query (String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
        Cursor c = database.query(  Babies.TABLE_NAME, allColumns,  null,  null, null,  null, Babies.COLUMN_NAME_NAME,  null);
        try{
            c.moveToFirst();
            while (!c.isAfterLast()){
                Baby baby = getBabyFromCursor(c);
                babyList.add(baby);
            }
        } finally {
            c.close();
        }
        return babyList;
    }

    private Baby getBabyFromCursor(Cursor cursor){
        int idIndex = cursor.getColumnIndexOrThrow(Babies._ID);
        int nameIndex = cursor.getColumnIndexOrThrow(Babies.COLUMN_NAME_NAME);
        int bDayIndex = cursor.getColumnIndexOrThrow(Babies.COLUMN_NAME_BIRTHDAY);
        int picIndex = cursor.getColumnIndexOrThrow(Babies.COLUMN_NAME_PICTURE);

        Baby baby = new Baby();
        baby.setId( cursor.getLong( idIndex) );
        baby.setName(cursor.getString(nameIndex));
        baby.setBirthday(cursor.getLong(bDayIndex));
        baby.setPicture(cursor.getString(picIndex));

        return baby;
    }
}
