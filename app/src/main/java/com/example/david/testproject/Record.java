package com.example.david.testproject;

import android.content.ContentValues;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Record {

    private final String DATABASE_NAME = "game_record.db"; // db name

    private static SQLiteDatabase db;

    private String TABLE_RECORD = "record"; // table name
    private String[] columns = {  "name", "score", "date" }; // column name

    private static Record record = null;

    private Record(ContextWrapper contextWrapper){
        db = contextWrapper.openOrCreateDatabase(DATABASE_NAME, SQLiteDatabase.OPEN_READWRITE, null);
    }

    public static Record getInstant(ContextWrapper contextWrapper){
        if (record == null){
            record = new Record(contextWrapper);
            return record;
        }
        return record;
    }


    public void createTable(){
        try {
            // Table creation sql statement
            final String TABLE_CREATION = "create table if not exists "
                    + TABLE_RECORD + "( "
                    + columns[0] + " text not null, "
                    + columns[1] + " integer not null, "
                    + columns[2] + " DATETIME not null);";
            // Create table
            db.execSQL(TABLE_CREATION);
        }
        catch (Exception e) { }
    }

    public void addRecord(User user){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(new Date());
        ContentValues contentValues = new ContentValues();
        contentValues.put(columns[0], user.name);
        contentValues.put(columns[1], user.score);
        contentValues.put(columns[2], dateString);
        db.insert(TABLE_RECORD, null, contentValues);
    }

    public ArrayList<User> getRecords(){
        Cursor cursor = db.query(TABLE_RECORD, null, null, null, null, null,  columns[1]+ " desc, " + columns[2] + " desc");
        ArrayList<User> users = new ArrayList<>();
        while(cursor.moveToNext()){
            String name = cursor.getString(0);
            int score = cursor.getInt(1);
            users.add(new User(name, score));
        }
        return users;
    }
}
