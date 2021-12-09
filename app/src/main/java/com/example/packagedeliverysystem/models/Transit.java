package com.example.packagedeliverysystem.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Transit extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "packagedeliverysystemdb";
    private static final  String TABLE_NAME = "transit";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_FULL_ADDRESS = "full_address";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_MAP_COORDINATES = "map_coordinates";
    private static final String KEY_COMPANY_ID = "company_id";


    ContentValues values;

    Integer id, company_id;
    String name, full_address, phone, map_coordinates;

    public Transit(Integer id, Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from "+TABLE_NAME+" where id = ?";
        System.out.println(query);
        Cursor res =  db.rawQuery( query, new String[] {id.toString()} );
        if(res.getCount() > 0) {
            res.moveToFirst();
            this.id = res.getInt(res.getColumnIndexOrThrow(KEY_ID));
            this.name = res.getString(res.getColumnIndexOrThrow(KEY_NAME));
            this.full_address = res.getString(res.getColumnIndexOrThrow(KEY_FULL_ADDRESS));
            this.phone = res.getString(res.getColumnIndexOrThrow(KEY_PHONE));
            this.map_coordinates = res.getString(res.getColumnIndexOrThrow(KEY_MAP_COORDINATES));
            this.company_id = res.getInt(res.getColumnIndexOrThrow(KEY_COMPANY_ID));
        }

    }

    public Transit(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_FULL_ADDRESS + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_MAP_COORDINATES + " TEXT,"
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
    public boolean insert (String name, String full_address, String phone, String map_coordinates, Integer company_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_FULL_ADDRESS, full_address);
        contentValues.put(KEY_PHONE, phone);
        contentValues.put(KEY_MAP_COORDINATES, map_coordinates);
        contentValues.put(KEY_COMPANY_ID, company_id);
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

    public boolean update (Integer id, String name, String full_address, String phone, String map_coordinates, Integer company_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_FULL_ADDRESS, full_address);
        contentValues.put(KEY_PHONE, phone);
        contentValues.put(KEY_MAP_COORDINATES, map_coordinates);
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

        //hp = new HashMap();
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

    public Integer getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Integer company_id) {
        this.company_id = company_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFull_address() {
        return full_address;
    }

    public void setFull_address(String full_address) {
        this.full_address = full_address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMap_coordinates() {
        return map_coordinates;
    }

    public void setMap_coordinates(String map_coordinates) {
        this.map_coordinates = map_coordinates;
    }
}
