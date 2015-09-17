package com.nna88.voz.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter1 {
    private static final String DATABASE_CREATE = "create table VOZ(_id integer primary key autoincrement, thread text not null, urlthread text not null);";
    private static final String DATABASE_NAME = "MyDB1";
    private static final String DATABASE_TABLE = "VOZ";
    private static final int DATABESE_VERSION = 1;
    public static final String KEY_ROWID = "_id";
    public static final String KEY_THREAD = "thread";
    public static final String KEY_URLTHREAD = "urlthread";
    private DatabaseHelper DBHelper;
    private final Context context;
    private SQLiteDatabase db;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DBAdapter1.DATABASE_NAME, null, DBAdapter1.DATABESE_VERSION);
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            try {
                sQLiteDatabase.execSQL(DBAdapter1.DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            sQLiteDatabase.execSQL("DROP TABLE IF EXITS VOZ");
            onCreate(sQLiteDatabase);
        }
    }

    public DBAdapter1(Context context) {
        this.context = context;
        this.DBHelper = new DatabaseHelper(this.context);
    }

    public void close() {
        this.DBHelper.close();
    }

    public boolean deleteThread(String str) {
        return this.db.delete(DATABASE_TABLE, new StringBuilder().append("thread='").append(str).append("'").toString(), null) > 0;
    }

    public Cursor getAllContacts() {
        return this.db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_THREAD, KEY_URLTHREAD}, null, null, null, null, null);
    }

    public Cursor getThread(String str, String[] strArr, String str2, String str3, String str4) {
        return this.db.query(true, DATABASE_TABLE, new String[]{KEY_ROWID, KEY_THREAD, KEY_URLTHREAD}, str, strArr, str2, str3, str4, null);
    }

    public long insertContact(String str, String str2) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_THREAD, str);
        contentValues.put(KEY_URLTHREAD, str2);
        return this.db.insert(DATABASE_TABLE, null, contentValues);
    }

    public DBAdapter1 open() {
        this.db = this.DBHelper.getWritableDatabase();
        return this;
    }
}
