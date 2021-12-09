package com.example.packagedeliverysystem.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Company extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "packagedeliverysystemdb";
    private static final  String TABLE_NAME = "company";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_BASE_COLOR = "base_color";
    private static final String KEY_STATUS = "status";
    private static final String KEY_OPEN_HOUR = "open_hour";
    private static final String KEY_CLOSE_HOUR = "close_hour";
    private static final String KEY_VERIFIED = "verified";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    Integer id;
    String name, base_color, status, open_hour, close_hour, verified, email, phone, username, password;
    public Company (Integer id, Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from "+TABLE_NAME+" where id = ?";
        System.out.println(query);
        Cursor res =  db.rawQuery( query, new String[] {id.toString()} );
        if(res.getCount() > 0) {
            res.moveToFirst();
            this.id = res.getInt(res.getColumnIndexOrThrow(KEY_ID));
            this.name = res.getString(res.getColumnIndexOrThrow(KEY_NAME));
            this.base_color = res.getString(res.getColumnIndexOrThrow(KEY_BASE_COLOR));
            this.status = res.getString(res.getColumnIndexOrThrow(KEY_STATUS));
            this.open_hour = res.getString(res.getColumnIndexOrThrow(KEY_OPEN_HOUR));
            this.close_hour = res.getString(res.getColumnIndexOrThrow(KEY_CLOSE_HOUR));
            this.verified = res.getString(res.getColumnIndexOrThrow(KEY_VERIFIED));
            this.email = res.getString(res.getColumnIndexOrThrow(KEY_EMAIL));
            this.phone = res.getString(res.getColumnIndexOrThrow(KEY_PHONE));
            this.username = res.getString(res.getColumnIndexOrThrow(KEY_USERNAME));
            this.password= res.getString(res.getColumnIndexOrThrow(KEY_PASSWORD));
        }
    }


    ContentValues values;

    public Company(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_BASE_COLOR + " TEXT,"
                + KEY_STATUS + " TEXT,"
                + KEY_OPEN_HOUR + " TEXT,"
                + KEY_CLOSE_HOUR + " TEXT,"
                + KEY_VERIFIED + " BOOLEAN,"
                + KEY_EMAIL + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_USERNAME + " TEXT,"
                + KEY_PASSWORD + " TEXT"+")";
        db.execSQL(CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }
    // code to add the new entry
    public boolean insert (String name, String base_color, String status, String open_hour,String close_hour, Boolean verified, String email, String phone, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_BASE_COLOR, base_color);
        contentValues.put(KEY_STATUS, status);
        contentValues.put(KEY_OPEN_HOUR, open_hour);
        contentValues.put(KEY_CLOSE_HOUR, close_hour);
        contentValues.put(KEY_VERIFIED, verified);
        contentValues.put(KEY_EMAIL, email);
        contentValues.put(KEY_PHONE, phone);
        contentValues.put(KEY_USERNAME, username);
        contentValues.put(KEY_PASSWORD, password);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_NAME+" where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean update (Integer id, String name, String base_color, String status, String open_hour,String close_hour, Boolean verified, String email, String phone, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_BASE_COLOR, base_color);
        contentValues.put(KEY_STATUS, status);
        contentValues.put(KEY_OPEN_HOUR, open_hour);
        contentValues.put(KEY_CLOSE_HOUR, close_hour);
        contentValues.put(KEY_VERIFIED, verified);
        contentValues.put(KEY_EMAIL, email);
        contentValues.put(KEY_PHONE, phone);
        contentValues.put(KEY_PASSWORD, password);
        db.update(TABLE_NAME, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer delete (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAll() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndexOrThrow(KEY_USERNAME)));
            res.moveToNext();
        }
        return array_list;
    }

    public Integer validateUser(String username, String password) {
//        String[] params = new String[]{username, password};
        Integer userId;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from "+TABLE_NAME+" where username = ? and password = ?";
        System.out.println(query);
        Cursor res =  db.rawQuery( query, new String[] {username.trim(), password.trim()} );
        if(res.getCount() > 0) {
            res.moveToFirst();
            userId = res.getInt(res.getColumnIndexOrThrow(KEY_ID));
        }
        else
            userId = -1;
        return userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBase_color() {
        return base_color;
    }

    public void setBase_color(String base_color) {
        this.base_color = base_color;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOpen_hour() {
        return open_hour;
    }

    public void setOpen_hour(String open_hour) {
        this.open_hour = open_hour;
    }

    public String getClose_hour() {
        return close_hour;
    }

    public void setClose_hour(String close_hour) {
        this.close_hour = close_hour;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }
}
