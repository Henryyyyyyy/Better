package me.henry.versatile.model.note;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.lib_db.core.BaseDao;


/**
 * Created by henry on 2018/1/5.
 */

public class NoteDao extends BaseDao<Note> {


    public NoteDao(Context context, Class<Note> clz, SQLiteDatabase db) {
        super(context, clz, db);
    }
}
