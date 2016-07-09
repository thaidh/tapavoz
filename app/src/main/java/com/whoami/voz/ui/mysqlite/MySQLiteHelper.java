package com.whoami.voz.ui.mysqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {
    public static final String COLUMN_FORUM = "forum";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_IDTHREAD = "idthread";
    public static final String COLUMN_LASTPOST = "lastpost";
    public static final String COLUMN_PAGE = "page";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_URLFIRSTNEWS = "urlfirstnews";
    public static final String COLUMN_URLLASTPOST = "urllastpost";
    public static final String COLUMN_VIEW = "view";
    private static final String DATABASE_CREATE = "create table bookmark(_id integer primary key autoincrement, idthread text not null, title text not null, url text not null, forum text not null, page text not null, view text not null, lastpost text not null, urlfirstnews text not null, urllastpost text not null);";
    private static final String DATABASE_NAME = "bookmark.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_COMMENTS = "bookmark";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public MySQLiteHelper(Context context, String str, CursorFactory cursorFactory, int i) {
        super(context, str, cursorFactory, i);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(DATABASE_CREATE);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS bookmark");
        onCreate(sQLiteDatabase);
    }
}
