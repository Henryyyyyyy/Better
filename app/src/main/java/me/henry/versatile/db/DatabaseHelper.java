package me.henry.versatile.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import me.henry.versatile.db.core.DBUtil;
import me.henry.versatile.model.note.Note;
import me.henry.versatile.model.note.Punch;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "henry.db";
    public static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DBUtil.createTable(db, Note.class);
        DBUtil.createTable(db, Punch.class);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //  DBUtil.dropTable(db, Developer.class);
        for (int i = oldVersion + 1; i <= newVersion; i++) {
            switch (i) {
                case 1:
                    break;
                case 2:

                    break;
                default:
                    break;

            }
        }
    }

}