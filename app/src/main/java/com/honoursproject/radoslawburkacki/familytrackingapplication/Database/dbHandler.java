package com.honoursproject.radoslawburkacki.familytrackingapplication.Database;

/**
 * Created by radek on 10/02/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.honoursproject.radoslawburkacki.familytrackingapplication.Model.Message;

public class dbHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FamilyCentreApp";
    private static final String TABLE_MESSAGESS = "messagess";
    public static final String ID = "message_id";
    public static final String FROMID = "from_id";
    public static final String TOID = "to_id";
    public static final String MESSAGE = "message";
    public static final String DATE = "date";
    private static final String DB_FULL_PATH = "/data/data/com.honoursproject.radoslawburkacki.familytrackingapplication/databases/messages.db";


    public dbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MESSAGES_TABLE = "CREATE TABLE " + TABLE_MESSAGESS + "("
                + ID + " INTEGER PRIMARY KEY,"
                + FROMID + " TEXT,"
                + TOID + " TEXT,"
                + MESSAGE + " TEXT,"
                + DATE + " TEXT" + ")";
        db.execSQL(CREATE_MESSAGES_TABLE);
        Log.d("", "Database created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGESS);

        // Create tables again
        onCreate(db);
    }


    public void addMessage(Message m) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FROMID, m.getFromId());
        values.put(TOID, m.getToId());
        values.put(MESSAGE, m.getMessage());
        values.put(DATE, m.getDate());
        // Inserting Row
        db.insert(TABLE_MESSAGESS, null, values);
        db.close(); // Closing database connection
    }


    public Message getMessage(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MESSAGESS, new String[]{ID,
                        FROMID, TOID, MESSAGE, DATE}, ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Message message = new Message(Long.parseLong(cursor.getString(0)), Long.parseLong(cursor.getString(1)), Long.parseLong(cursor.getString(2)),
                cursor.getString(3), cursor.getString(4));
        return message;
    }


    public List<Message> getAllMessagess() {
        List<Message> messageList = new ArrayList<Message>();

        String selectQuery = "SELECT  * FROM " + TABLE_MESSAGESS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {

                Message message = new Message(Long.parseLong(cursor.getString(0)), Long.parseLong(cursor.getString(1)), Long.parseLong(cursor.getString(2)),
                        cursor.getString(3), cursor.getString(4));

                messageList.add(message);
            } while (cursor.moveToNext());
        }

        return messageList;
    }

    public List<Message> get20lastMessages(long fromid, long toid) { // getting last 20 messages
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ( SELECT * FROM " + TABLE_MESSAGESS + " WHERE from_id=" + fromid + " AND to_id=" + toid + " OR from_id = " + toid + " AND to_id =" + fromid + " ORDER BY MESSAGE_ID DESC LIMIT 20) sub ORDER BY " + ID + " ASC", null);

        List<Message> messagesList = new ArrayList<Message>();

        if (cursor.moveToFirst()) {
            do {

                Message message = new Message(Long.parseLong(cursor.getString(0)), Long.parseLong(cursor.getString(1)), Long.parseLong(cursor.getString(2)),
                        cursor.getString(3), cursor.getString(4));

                messagesList.add(message);
            } while (cursor.moveToNext());
        }

        return messagesList;
    }

}