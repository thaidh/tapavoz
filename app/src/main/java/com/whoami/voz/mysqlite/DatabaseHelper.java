package com.whoami.voz.mysqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.whoami.voz.contain.VozThread;

import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DB_NAME = "tapavoz.db";
    private static final String TABLE_BOOKMARK = "bookmark_thread";
    private SQLiteDatabase db;
    private volatile static DatabaseHelper instance = null;

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    instance = new DatabaseHelper(context);
                }
            }

            instance.createEventTable();
        }
        return instance;
    }

    private void createEventTable() {
        if (!isExistTable(TABLE_BOOKMARK)) {
            db.execSQL("CREATE TABLE " + TABLE_BOOKMARK + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, idthread, title , url , desc, thumb)");
        }
    }

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        db = context.openOrCreateDatabase(DB_NAME, 0, null);
    }

    private boolean isExistTable(String tableName) {
        boolean isExist = true;

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM SQLITE_MASTER WHERE NAME= ? ", new String[]{tableName});
            if (cursor.getCount() == 0) {
                isExist = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return isExist;
    }

    public void insertBookmark(VozThread vozThread) {
        try {
            db.execSQL("INSERT INTO "
                            +TABLE_BOOKMARK
                            + " (idthread, title , url , desc, thumb)"
                            + " VALUES (?, ?, ?, ?, ?)",
                    new String[]{vozThread.mIdThread, vozThread.Thread(), vozThread.UrlThread(), "", ""});

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public String getLastDiary() {
//        String diary = "";
//        Cursor cursor = null;
//        cursor = db.rawQuery("select content from " + TABLE_EVENT, null);
//        if (cursor.moveToLast()) {
//            diary = cursor.getString(cursor.getColumnIndex("content"));
//        };
//        return diary;
//    }

    public ArrayList<VozThread> getAllBookmarkThread() {
        ArrayList<VozThread> arrDiary = new ArrayList<VozThread>();

        Cursor cursor = db.rawQuery("select * from " + TABLE_BOOKMARK, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("idthread"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            VozThread vozThread = new VozThread(title, "", "", "", null, url, "");
            vozThread.mIdThread = id;
            arrDiary.add(vozThread);
        };
        return arrDiary;
    }

    public void clearAllBookmarkThread() {
        try {
            db.delete(TABLE_BOOKMARK, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    @Override
    public void close() {
        synchronized (db) {
            if (db != null)
                db.close();
        }
        super.close();
    }
}