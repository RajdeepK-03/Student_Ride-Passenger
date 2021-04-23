package com.test.student_ride;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Studentride.db";




    // PROFILE table name
    private static final String TABLE_PROFILE = "profile";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_FIRSTNAME = "fname";
    private static final String COLUMN_USER_LASTNAME = "lname";
    private static final String COLUMN_STUDENT_ID = "student_id";
    private static final String COLUMN_USER_UNIVERSITY = "university";
    private static final String KEY_IMAGE = "image";


    // PROFILE table name
    private static final String TABLE_CORDINATES = "cordinates";

    private static final String COLUMN_CORDINATE_ID = "id";
    private static final String COLUMN_CORDINATE_LATITUDE = "latitude";
    private static final String COLUMN_CORDINATE_LONGITUDE = "longitude";


    // PROFILE table name
    private static final String TABLE_CARD = "card_info";
    private static final String COLUMN_ID="id";
    private static final String COLUMN_CREDITCARD = "creditcard";
    private static final String COLUMN_EXPIRY = "expiry";

    private String CREATE_PROFILE_TABLE = "CREATE TABLE " + TABLE_PROFILE + "(" + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_FIRSTNAME + " TEXT,"+ COLUMN_USER_LASTNAME +" TEXT,"+COLUMN_STUDENT_ID +" TEXT,"+COLUMN_USER_UNIVERSITY +" TEXT,"+ KEY_IMAGE + " BLOB NOT NULL" + ")";

    private String CREATE_CORDINATES_TABLE = "CREATE TABLE " + TABLE_CORDINATES + "(" + COLUMN_CORDINATE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CORDINATE_LATITUDE + " TEXT,"+ COLUMN_CORDINATE_LONGITUDE + " TEXT" + ")";


    private String CREATE_CREDITCARD_TABLE = "CREATE TABLE " + TABLE_CARD + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ COLUMN_CREDITCARD + " TEXT," + COLUMN_EXPIRY + " TEXT" + ")";

    private String DROP_PROFILE_TABLE = "DROP TABLE IF EXISTS " + TABLE_PROFILE;

    private String DROP_CORDINATE_TABLE = "DROP TABLE IF EXISTS " + TABLE_CORDINATES;

    private String DROP_CARD_TABLE = "DROP TABLE IF EXISTS " + TABLE_CARD;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_PROFILE_TABLE);
        db.execSQL(CREATE_CORDINATES_TABLE);
        db.execSQL(CREATE_CREDITCARD_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        db.execSQL(DROP_PROFILE_TABLE);
        db.execSQL(DROP_CORDINATE_TABLE);
        db.execSQL(DROP_CARD_TABLE);
        onCreate(db);

    }


    public Boolean addUser(String fname,String lname,String id,String university ,byte[] imageBytes) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_FIRSTNAME, fname);
        values.put(COLUMN_USER_LASTNAME, lname);
        values.put(COLUMN_STUDENT_ID, id);
        values.put(COLUMN_USER_UNIVERSITY, university);

        values.put(KEY_IMAGE, imageBytes);
        // Inserting Row
         long result=  db.insert(TABLE_PROFILE, null, values);

        if (result == -1)

            return false;
        else
            return true;

    }

    public Boolean addcoordinate(String latitude,String longitude ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CORDINATE_LATITUDE, latitude);
        values.put(COLUMN_CORDINATE_LONGITUDE, longitude);
        // Inserting Row
        long result=  db.insert(TABLE_CORDINATES, null, values);

        if (result == -1)

            return false;
        else
            return true;

    }


    public Boolean add_card(String credit_card,String expiry ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CREDITCARD, credit_card);
        values.put(COLUMN_EXPIRY, expiry);
        // Inserting Row
        long result=  db.insert(TABLE_CARD, null, values);

        if (result == -1)

            return false;
        else
            return true;

    }


    public byte[] retreiveImageFromDB( String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + "profile" + " where id = '1'" , null);
        if (cur.moveToFirst()) {
            byte[] blob = cur.getBlob(cur.getColumnIndex(KEY_IMAGE));
            cur.close();
            return blob;
        }
        cur.close();
        return null;
    }

    public List<Profile> get_user_info() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Profile> _profile = new ArrayList<Profile>();
        Cursor c = db.rawQuery("SELECT * FROM " + "profile" , null);
        String temp = null;
        if (c.moveToFirst()) {
            do {
                Profile profile=new Profile();
                profile.setFName(c.getString(c.getColumnIndex(COLUMN_USER_FIRSTNAME)));
                profile.setLname(c.getString(c.getColumnIndex(COLUMN_USER_LASTNAME)));
                profile.setId(c.getString(c.getColumnIndex(COLUMN_STUDENT_ID)));
                profile.setuniversity(c.getString(c.getColumnIndex(COLUMN_USER_UNIVERSITY)));
                _profile.add(profile);
            } while (c.moveToNext());
        }
        c.close();

        return _profile;
    }

    public int count(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + table , null);
        return c.getCount();
    }







    public boolean update_info(String  fname,String lname ,String id,String uni,byte[] array) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_FIRSTNAME,fname);
        values.put(COLUMN_USER_LASTNAME,lname);
        values.put(COLUMN_STUDENT_ID,id);
        values.put(COLUMN_USER_UNIVERSITY,uni);
        values.put(KEY_IMAGE,array);
        int cursor= db.update(TABLE_PROFILE, values, "id=?", new String[] {String.valueOf("1")});
        db.close();
        if (cursor > 0)
            return true;
        else
            return false;
    }





}