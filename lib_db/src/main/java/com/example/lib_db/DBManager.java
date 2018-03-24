package com.example.lib_db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lib_db.core.BaseDao;

import java.util.HashMap;




public class DBManager {
    private static DBManager mInstance;
    private SQLiteOpenHelper mHelper;
    private SQLiteDatabase mDatabase;
    private HashMap<String, BaseDao> mCachedDaos;
    private Context context;

    private DBManager(Context context, SQLiteOpenHelper helper) {
        this.context = context;
        mHelper = helper;
        mDatabase = mHelper.getWritableDatabase();
        mCachedDaos = new HashMap<>();
    }

    public static void init(Context context, SQLiteOpenHelper helper) {
        if (mInstance == null) {
            mInstance = new DBManager(context, helper);
        }
    }

    public static DBManager getInstance() {
        return mInstance;
    }

    public void release() {
        mDatabase.close();
        mInstance = null;
    }

    public <T> BaseDao<T> getDao(Class<T> clz) {
        if (mCachedDaos.containsKey(clz.getSimpleName())) {
            return mCachedDaos.get(clz.getSimpleName());
        } else {
            BaseDao<T> dao = new BaseDao<T>(context, clz, mDatabase);
            mCachedDaos.put(clz.getSimpleName(), dao);
            return dao;
        }
    }
    //todo,如果basedao不能Note达到特定表的需求的话，这里继续添加子类dao,如下：

//    public NoteDao getNoteDao() {
//        if (mCachedDaos.containsKey(Note.class.getSimpleName())) {
//            return (NoteDao) mCachedDaos.get(Note.class.getSimpleName());
//        } else {
//            NoteDao dao = new NoteDao (context, Note.class, mDatabase);
//            mCachedDaos.put(Note.class.getSimpleName(), dao);
//            return dao;
//        }
//    }

}
