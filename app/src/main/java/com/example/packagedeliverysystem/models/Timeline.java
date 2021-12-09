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
import java.util.Date;

public class Timeline extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "packagedeliverysystemdb";
    private static final  String TABLE_NAME = "timeline";
    private static final String KEY_ID = "id";
    private static final String KEY_ARRIVAL_DATE = "arrival_date";
    private static final String KEY_PACKAGE_ID = "package_id";
    private static final String KEY_DELIVERY_STATUS_ID = "delivery_status_id";
    private static final String KEY_TRANSIT_ID = "transit_id";


    ContentValues values;

    Integer id, package_id, transit_id, delivery_status_id;
    String arrival_date;

    public Timeline(Integer id, Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from "+TABLE_NAME+" where id = ?";
        System.out.println(query);
        Cursor res =  db.rawQuery( query, new String[] {id.toString()} );
        if(res.getCount() > 0) {
            res.moveToFirst();
            this.id = res.getInt(res.getColumnIndexOrThrow(KEY_ID));
            this.package_id = res.getInt(res.getColumnIndexOrThrow(KEY_PACKAGE_ID));
            this.transit_id = res.getInt(res.getColumnIndexOrThrow(KEY_TRANSIT_ID));
            this.delivery_status_id = res.getInt(res.getColumnIndexOrThrow(KEY_DELIVERY_STATUS_ID));
            this.arrival_date = res.getString(res.getColumnIndexOrThrow(KEY_ARRIVAL_DATE));
        }
    }
    public Timeline(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_ARRIVAL_DATE + " DATE,"
                + KEY_PACKAGE_ID + " INTEGER, "
                + KEY_TRANSIT_ID + " INTEGER, "
                + KEY_DELIVERY_STATUS_ID + " INTEGER, "
                + "FOREIGN KEY("+KEY_PACKAGE_ID+") REFERENCES package(id),"
                + "FOREIGN KEY("+KEY_DELIVERY_STATUS_ID+") REFERENCES deliverystatus(id),"
                + "FOREIGN KEY("+KEY_TRANSIT_ID+") REFERENCES package(id)"+");";
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
    public boolean insert (Date arrival_date, Integer package_id, Integer delivery_status_id, Integer transit_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ARRIVAL_DATE, arrival_date.toString());
        contentValues.put(KEY_PACKAGE_ID, package_id);
        contentValues.put(KEY_DELIVERY_STATUS_ID, delivery_status_id);
        contentValues.put(KEY_TRANSIT_ID, transit_id);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_NAME+" where id="+id+"", null );
        return res;
    }

    public Integer getTransitIdByPackage(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_NAME+" where "+KEY_PACKAGE_ID+"="+id+" order by "+KEY_ARRIVAL_DATE+" desc limit 1", null );
        res.moveToFirst();
        return res.getInt(res.getColumnIndexOrThrow(KEY_TRANSIT_ID));
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean update (Integer id, Date arrival_date, Integer package_id, Integer delivery_status_id, Integer transit_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ARRIVAL_DATE, arrival_date.toString());
        contentValues.put(KEY_PACKAGE_ID, package_id);
        contentValues.put(KEY_DELIVERY_STATUS_ID, delivery_status_id);
        contentValues.put(KEY_TRANSIT_ID, transit_id);

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
            array_list.add(res.getString(res.getColumnIndexOrThrow(KEY_ID)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<JSONObject> getPackageTimelineAsJSON(int package_id) {
        ArrayList<JSONObject> obj = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_NAME+" where "+KEY_PACKAGE_ID+" = "+package_id, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(KEY_ID,res.getInt(res.getColumnIndexOrThrow(KEY_ID)));
                jsonObject.put(KEY_DELIVERY_STATUS_ID,res.getInt(res.getColumnIndexOrThrow(KEY_DELIVERY_STATUS_ID)));
                jsonObject.put(KEY_TRANSIT_ID,res.getInt(res.getColumnIndexOrThrow(KEY_TRANSIT_ID)));
                jsonObject.put(KEY_ARRIVAL_DATE,res.getString(res.getColumnIndexOrThrow(KEY_ARRIVAL_DATE)));
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

    public Integer getPackage_id() {
        return package_id;
    }

    public void setPackage_id(Integer package_id) {
        this.package_id = package_id;
    }

    public Integer getTransit_id() {
        return transit_id;
    }

    public void setTransit_id(Integer transit_id) {
        this.transit_id = transit_id;
    }

    public Integer getDelivery_status_id() {
        return delivery_status_id;
    }

    public void setDelivery_status_id(Integer delivery_status_id) {
        this.delivery_status_id = delivery_status_id;
    }

    public String getArrival_date() {
        return arrival_date;
    }

    public void setArrival_date(String arrival_date) {
        this.arrival_date = arrival_date;
    }
}
