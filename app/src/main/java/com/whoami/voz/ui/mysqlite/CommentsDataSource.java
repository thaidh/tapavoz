package com.whoami.voz.ui.mysqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.whoami.voz.ui.contain.Thread;
import com.whoami.voz.ui.utils.DBAdapter1;

import java.util.ArrayList;
import java.util.List;

public class CommentsDataSource {
    private String[] allColumns;
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    public CommentsDataSource(Context context) {
        this.allColumns = new String[]{DBAdapter1.KEY_ROWID, MySQLiteHelper.COLUMN_IDTHREAD, MySQLiteHelper.COLUMN_TITLE, MySQLiteHelper.COLUMN_URL, MySQLiteHelper.COLUMN_FORUM, MySQLiteHelper.COLUMN_PAGE, MySQLiteHelper.COLUMN_VIEW, MySQLiteHelper.COLUMN_LASTPOST, MySQLiteHelper.COLUMN_URLFIRSTNEWS, MySQLiteHelper.COLUMN_URLLASTPOST};
        this.dbHelper = new MySQLiteHelper(context);
    }

    private Comment cursorToComment(Cursor cursor) {
        Comment comment = new Comment();
        comment.id = cursor.getLong(0);
        comment.idThread = cursor.getString(1);
        comment.title = cursor.getString(2);
        comment.url = cursor.getString(3);
        comment.forum = cursor.getString(4);
        comment.iPage = cursor.getString(5);
        comment.View = cursor.getString(6);
        comment.LastPost = cursor.getString(7);
        comment.urlFirstNews = cursor.getString(8);
        comment.urlLastPost = cursor.getString(9);
        return comment;
    }

    public void addBookmark(Thread thread) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MySQLiteHelper.COLUMN_IDTHREAD, thread.mIdThread);
        contentValues.put(MySQLiteHelper.COLUMN_TITLE, thread.Thread());
        contentValues.put(MySQLiteHelper.COLUMN_URL, thread.UrlThread());
        contentValues.put(MySQLiteHelper.COLUMN_FORUM, "");
        contentValues.put(MySQLiteHelper.COLUMN_LASTPOST, thread.LastPost());
        if (thread.Reply() == null) {
            contentValues.put(MySQLiteHelper.COLUMN_PAGE, "");
        } else {
            contentValues.put(MySQLiteHelper.COLUMN_PAGE, thread.Reply());
        }
        if (thread.View() == null) {
            contentValues.put(MySQLiteHelper.COLUMN_VIEW, "");
        } else {
            contentValues.put(MySQLiteHelper.COLUMN_VIEW, thread.View());
        }
        if (thread.UrlLastPosst() == null) {
            contentValues.put(MySQLiteHelper.COLUMN_URLFIRSTNEWS, "");
        } else {
            contentValues.put(MySQLiteHelper.COLUMN_URLFIRSTNEWS, thread.UrlLastPosst());
        }
        contentValues.put(MySQLiteHelper.COLUMN_URLLASTPOST, thread.getUrlLastLast());
        this.database.insert(MySQLiteHelper.TABLE_COMMENTS, null, contentValues);
    }

    public void close() {
        this.dbHelper.close();
    }

    public void deleteBookmark(Thread thread) {
        try {
            this.database.delete(MySQLiteHelper.TABLE_COMMENTS, "idthread = " + thread.mIdThread, null);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    public List<Comment> getAllComments() {
        List<Comment> arrayList = new ArrayList();
        Cursor query = this.database.query(MySQLiteHelper.TABLE_COMMENTS, this.allColumns, null, null, null, null, null);
        query.moveToFirst();
        while (!query.isAfterLast()) {
            arrayList.add(cursorToComment(query));
            query.moveToNext();
        }
        query.close();
        return arrayList;
    }

    public void open() throws SQLException {
        this.database = this.dbHelper.getWritableDatabase();
    }

    public void updateBookmark(Thread thread) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MySQLiteHelper.COLUMN_IDTHREAD, thread.mIdThread);
        contentValues.put(MySQLiteHelper.COLUMN_TITLE, thread.Thread());
        contentValues.put(MySQLiteHelper.COLUMN_URL, thread.UrlThread());
        contentValues.put(MySQLiteHelper.COLUMN_FORUM, "");
        if (thread.Reply() == null) {
            contentValues.put(MySQLiteHelper.COLUMN_PAGE, "");
        } else {
            contentValues.put(MySQLiteHelper.COLUMN_PAGE, thread.Reply());
        }
        if (thread.View() == null) {
            contentValues.put(MySQLiteHelper.COLUMN_VIEW, "");
        } else {
            contentValues.put(MySQLiteHelper.COLUMN_VIEW, thread.View());
        }
        contentValues.put(MySQLiteHelper.COLUMN_LASTPOST, thread.LastPost());
        if (thread.UrlLastPosst() == null) {
            contentValues.put(MySQLiteHelper.COLUMN_URLFIRSTNEWS, "");
        } else {
            contentValues.put(MySQLiteHelper.COLUMN_URLFIRSTNEWS, thread.UrlLastPosst());
        }
        contentValues.put(MySQLiteHelper.COLUMN_URLLASTPOST, thread.getUrlLastLast());
        this.database.update(MySQLiteHelper.TABLE_COMMENTS, contentValues, "idthread = ?", new String[]{thread.mIdThread});
    }
}
