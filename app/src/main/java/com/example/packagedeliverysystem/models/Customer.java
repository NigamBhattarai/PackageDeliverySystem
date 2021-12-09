package com.example.packagedeliverysystem.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Customer extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "packagedeliverysystemdb";
    private static final  String TABLE_NAME = "customer";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_VERIFIED = "verified";
    private static final String KEY_COMPANY_ID = "company_id";
    ContentValues values;
    Context context;

    Integer id;
    String name, address, phone, verified, companyId;


    public Customer(Integer id, Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from "+TABLE_NAME+" where id = ?";
        System.out.println(query);
        Cursor res =  db.rawQuery( query, new String[] {id.toString()} );
        if(res.getCount() > 0) {
            res.moveToFirst();
            this.id = res.getInt(res.getColumnIndexOrThrow(KEY_ID));
            this.name = res.getString(res.getColumnIndexOrThrow(KEY_NAME));
            this.address = res.getString(res.getColumnIndexOrThrow(KEY_ADDRESS));
            this.phone = res.getString(res.getColumnIndexOrThrow(KEY_PHONE));
            this.verified = res.getString(res.getColumnIndexOrThrow(KEY_VERIFIED));
            this.companyId = res.getString(res.getColumnIndexOrThrow(KEY_COMPANY_ID));
        }
    }


    public Customer(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_VERIFIED + " TEXT,"
                + KEY_COMPANY_ID + " INTEGER,"
                + "FOREIGN KEY("+KEY_COMPANY_ID+") REFERENCES company(id)"+")";
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
    public boolean insert (String name, String address, String phone, Boolean verified, Integer company_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_ADDRESS, address);
        contentValues.put(KEY_PHONE, phone);
        contentValues.put(KEY_VERIFIED, verified);
        contentValues.put(KEY_COMPANY_ID, company_id);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_NAME+" where id="+id+"", null );
        return res;
    }

    public ArrayList<Integer> getDataByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> data = new ArrayList<>();
        Cursor res =  db.rawQuery( "select * from "+TABLE_NAME+" where UPPER("+KEY_NAME+") like '%"+name+"%'", null );
        res.moveToFirst();
        while (!res.isAfterLast()) {
            data.add(res.getInt(res.getColumnIndexOrThrow(KEY_ID)));
        }
        return data;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean update (Integer id, String name, String address, String phone, Boolean verified, Integer company_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_ADDRESS, address);
        contentValues.put(KEY_PHONE, phone);
        contentValues.put(KEY_VERIFIED, verified);
        contentValues.put(KEY_COMPANY_ID, company_id);
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
            array_list.add(res.getString(res.getColumnIndexOrThrow(KEY_NAME)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<JSONObject> getAllAsJSON() {
        ArrayList<JSONObject> obj = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(KEY_NAME,res.getString(res.getColumnIndexOrThrow(KEY_NAME)));
                jsonObject.put(KEY_ID,res.getInt(res.getColumnIndexOrThrow(KEY_ID)));
                obj.add(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            res.moveToNext();
        }
        return obj;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
