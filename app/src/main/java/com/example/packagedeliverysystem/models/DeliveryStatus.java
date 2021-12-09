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

public class DeliveryStatus extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "packagedeliverysystemdb";
    private static final  String TABLE_NAME = "deliverystatus";
    private static final String KEY_ID = "id";
    private static final String KEY_STATUS = "status";
    private static final String KEY_SEVERITY = "severity";

    Integer id, severity;
    String status;


    ContentValues values;
    public DeliveryStatus(Integer id, Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from "+TABLE_NAME+" where id = ?";
            System.out.println(query);
        Cursor res =  db.rawQuery( query, new String[] {id.toString()} );
            if(res.getCount() > 0) {
            res.moveToFirst();
            this.id = res.getInt(res.getColumnIndexOrThrow(KEY_ID));
            this.status = res.getString(res.getColumnIndexOrThrow(KEY_STATUS));
            this.severity = res.getInt(res.getColumnIndexOrThrow(KEY_SEVERITY));
        }
    }
    public DeliveryStatus(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_STATUS + " TEXT,"
                + KEY_SEVERITY + " INTEGER"+")";
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
    public boolean insert (String status, Integer severity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_STATUS, status);
        contentValues.put(KEY_SEVERITY, severity);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_NAME+" where id="+id+"", null );
        return res;
    }

    public Integer getIdByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_NAME+" where "+KEY_STATUS+"='"+name+"'", null );
        res.moveToFirst();
        return res.getInt(res.getColumnIndexOrThrow(KEY_ID));
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean update (Integer id, String status, Integer severity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_STATUS, status);
        contentValues.put(KEY_SEVERITY, severity);
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
            array_list.add(res.getString(res.getColumnIndexOrThrow(KEY_STATUS)));
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
                jsonObject.put(KEY_STATUS,res.getString(res.getColumnIndexOrThrow(KEY_STATUS)));
                jsonObject.put(KEY_SEVERITY, res.getString(res.getColumnIndexOrThrow(KEY_SEVERITY)));
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

    public Integer getSeverity() {
        return severity;
    }

    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
