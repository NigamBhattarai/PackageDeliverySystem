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

public class Package extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "packagedeliverysystemdb";
    private static final  String TABLE_NAME = "package";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SEVERITY = "severity";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_EXPECTED_DELIVERY_DAYS = "expected_delivery_days";
    private static final String KEY_SENDER_ID = "sender_id";
    private static final String KEY_RECEIVER_ID = "receiver_id";
    private static final String KEY_DELIVERY_FROM_ID = "delivery_from_id";
    private static final String KEY_DELIVERY_TO_ID = "delivery_to_id";
    private static final String KEY_COMPANY_ID = "company_id";
    private static final String KEY_DELIVERY_STATUS_ID = "delivery_status_id";


    ContentValues values;

    Integer id, sender_id, receiver_id, delivery_from_id, delivery_to_id, company_id, delivery_status_id;
    String name, severity, weight, expected_delivery_days;
    Context context;

    public Package(Integer id, Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        //3rd argument to be passed is CursorFactory instance
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from "+TABLE_NAME+" where id = ?";
        System.out.println(query);
        Cursor res =  db.rawQuery( query, new String[] {id.toString()} );
        if(res.getCount() > 0) {
            res.moveToFirst();
            this.id = res.getInt(res.getColumnIndexOrThrow(KEY_ID));
            this.name = res.getString(res.getColumnIndexOrThrow(KEY_NAME));
            this.severity = res.getString(res.getColumnIndexOrThrow(KEY_SEVERITY));
            this.weight = res.getString(res.getColumnIndexOrThrow(KEY_WEIGHT));
            this.expected_delivery_days = res.getString(res.getColumnIndexOrThrow(KEY_EXPECTED_DELIVERY_DAYS));
            this.sender_id = res.getInt(res.getColumnIndexOrThrow(KEY_SENDER_ID));
            this.receiver_id = res.getInt(res.getColumnIndexOrThrow(KEY_RECEIVER_ID));
            this.delivery_from_id = res.getInt(res.getColumnIndexOrThrow(KEY_DELIVERY_FROM_ID));
            this.delivery_to_id = res.getInt(res.getColumnIndexOrThrow(KEY_DELIVERY_TO_ID));
            this.company_id = res.getInt(res.getColumnIndexOrThrow(KEY_COMPANY_ID));
            this.delivery_status_id = res.getInt(res.getColumnIndexOrThrow(KEY_DELIVERY_STATUS_ID));
        }
    }

    public Package(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_SEVERITY + " TEXT,"
                + KEY_WEIGHT + " TEXT,"
                + KEY_EXPECTED_DELIVERY_DAYS + " TEXT,"
                + KEY_SENDER_ID + " INTEGER,"
                + KEY_RECEIVER_ID + " INTEGER,"
                + KEY_DELIVERY_FROM_ID + " INTEGER,"
                + KEY_DELIVERY_TO_ID + " INTEGER,"
                + KEY_COMPANY_ID + " INTEGER,"
                + KEY_DELIVERY_STATUS_ID + " INTEGER,"
                + "FOREIGN KEY("+KEY_SENDER_ID+") REFERENCES customer(id),"
                + "FOREIGN KEY("+KEY_RECEIVER_ID+") REFERENCES customer(id),"
                + "FOREIGN KEY("+KEY_DELIVERY_FROM_ID+") REFERENCES transit(id),"
                + "FOREIGN KEY("+KEY_DELIVERY_TO_ID+") REFERENCES transit(id),"
                + "FOREIGN KEY("+KEY_COMPANY_ID+") REFERENCES company(id),"
                + "FOREIGN KEY("+KEY_DELIVERY_STATUS_ID+") REFERENCES deliverystatus(id)"+");";

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
    public Long insert (String name, String severity, String weight, String expected_delivery_days, Integer sender_id, Integer receiver_id, Integer delivery_from_id, Integer delivery_to_id, Integer company_id, Integer delivery_status_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_SEVERITY, severity);
        contentValues.put(KEY_WEIGHT, weight);
        contentValues.put(KEY_EXPECTED_DELIVERY_DAYS, expected_delivery_days);
        contentValues.put(KEY_SENDER_ID, sender_id);
        contentValues.put(KEY_RECEIVER_ID, receiver_id);
        contentValues.put(KEY_DELIVERY_FROM_ID, delivery_from_id);
        contentValues.put(KEY_DELIVERY_TO_ID, delivery_to_id);
        contentValues.put(KEY_COMPANY_ID, company_id);
        contentValues.put(KEY_DELIVERY_STATUS_ID, delivery_status_id);
        return db.insert(TABLE_NAME, null, contentValues);
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

    public boolean update (Integer id, String name, String severity, String weight, String expected_delivery_days, Integer sender_id, Integer receiver_id, Integer delivery_from_id, Integer delivery_to_id, Integer company_id, Integer delivery_status_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_SEVERITY, severity);
        contentValues.put(KEY_WEIGHT, weight);
        contentValues.put(KEY_EXPECTED_DELIVERY_DAYS, expected_delivery_days);
        contentValues.put(KEY_SENDER_ID, sender_id);
        contentValues.put(KEY_RECEIVER_ID, receiver_id);
        contentValues.put(KEY_DELIVERY_FROM_ID, delivery_from_id);
        contentValues.put(KEY_DELIVERY_TO_ID, delivery_to_id);
        contentValues.put(KEY_COMPANY_ID, company_id);
        contentValues.put(KEY_DELIVERY_STATUS_ID, delivery_status_id);
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

    public ArrayList<JSONObject> getAllActiveAsJSON() {
        ArrayList<JSONObject> obj = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_NAME+" where "+KEY_DELIVERY_STATUS_ID+"!="+new DeliveryStatus(context).getIdByName("Cancelled"), null );
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

    public Integer getSender_id() {
        return sender_id;
    }

    public void setSender_id(Integer sender_id) {
        this.sender_id = sender_id;
    }

    public Integer getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(Integer receiver_id) {
        this.receiver_id = receiver_id;
    }

    public Integer getDelivery_from_id() {
        return delivery_from_id;
    }

    public void setDelivery_from_id(Integer delivery_from_id) {
        this.delivery_from_id = delivery_from_id;
    }


    public Integer getDelivery_to_id() {
        return delivery_to_id;
    }

    public void setDelivery_to_id(Integer delivery_to_id) {
        this.delivery_to_id = delivery_to_id;
    }

    public Integer getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Integer company_id) {
        this.company_id = company_id;
    }

    public Integer getDelivery_status_id() {
        return delivery_status_id;
    }

    public void setDelivery_status_id(Integer delivery_status_id) {
        this.delivery_status_id = delivery_status_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getExpected_delivery_days() {
        return expected_delivery_days;
    }

    public void setExpected_delivery_days(String expected_delivery_days) {
        this.expected_delivery_days = expected_delivery_days;
    }
}
