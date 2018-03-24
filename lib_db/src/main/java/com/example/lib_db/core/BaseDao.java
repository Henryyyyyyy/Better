package com.example.lib_db.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lib_db.DBManager;
import com.example.lib_db.annotation.Column;
import com.example.lib_db.annotation.Table;
import com.example.lib_db.utilities.SerializeUtil;
import com.example.lib_db.utilities.TextUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;


/**
 * 为以后复杂业务拓展
 *
 * @param <T>
 */
public class BaseDao<T> {
    private SQLiteDatabase mDatabase;
    private Class<T> clz;
    private String mTableName;
    private String mIdName;
    private Field[] mColumnFields;
    private Field mIdField;
    private Context context;
    private ArrayList<Field> mForeignFields;


    public BaseDao(Context context, Class<T> clz, SQLiteDatabase db) {
        this.clz = clz;
        this.mDatabase = db;
        this.context = context;
        try {
            mTableName = DBUtil.getTableName(clz);
            mIdName = DBUtil.getIDColumnName(clz);
            mIdField = DBUtil.getIdField(clz);
            mIdField.setAccessible(true);
            mColumnFields = clz.getDeclaredFields();
            mForeignFields = DBUtil.getForeignFields(mColumnFields);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void beginTransaction() {
        mDatabase.beginTransaction();
    }

    public void endTransaction() {
        endTransaction(true);
    }

    public void endTransaction(boolean successful) {
        if (successful) {
            mDatabase.setTransactionSuccessful();
        }
        mDatabase.endTransaction();
    }

    public <T> long newOrUpdate(T t) {
        ContentValues values = new ContentValues();
        ArrayList<Field> toManyFields = null;
        ArrayList<Boolean> toManyAutoRefresh = null;

        try {
            for (Field field : mColumnFields) {
                if (field.isAnnotationPresent(Column.class)) {
                    field.setAccessible(true);
                    Class<?> clz = field.getType();
                    if (clz == String.class) {
                        Object value = field.get(t);
                        if (value != null) {
                            values.put(DBUtil.getColumnName(field), value.toString());
                        }
                    } else if (clz == boolean.class || clz == Boolean.class) {
                        Object value = field.get(t);
                        if (value != null) {
                            boolean boo = (boolean) value;//把布尔型转成字符串
                            values.put(DBUtil.getColumnName(field), boo ? "1" : "0");
                        }
                    } else if (clz == int.class || clz == Integer.class) {
                        values.put(DBUtil.getColumnName(field), field.getInt(t));
                    } else if (clz == long.class || clz == Long.class) {
                        //主键自增长
                        Column column = field.getAnnotation(Column.class);
                        if (!column.id()) {//如果不是id主键
                            values.put(DBUtil.getColumnName(field), field.getLong(t));
                        } else {//如果是id主键
                            if (field.getLong(t) != 0) {//如果有值
                                values.put(DBUtil.getColumnName(field), field.getLong(t));
                            }
                        }
                    } else {
                        Column column = field.getAnnotation(Column.class);
                        Column.ColumnType type = column.type();
                        if (!TextUtil.isValidate(type.name())) {
                            throw new IllegalArgumentException("you should set type to the special column:" + t.getClass().getSimpleName() + "."
                                    + field.getName());
                        }
                        if (type == Column.ColumnType.SERIALIZABLE) {
                            byte[] value = SerializeUtil.serialize(field.get(t));
                            values.put(DBUtil.getColumnName(field), value);
                        } else if (type == Column.ColumnType.TONE) {
                            Object tone = field.get(t);
                            //如果是刚刚新建的对象，没有关联对象
                            if (tone == null) {
                                continue;
                            }

                            if (column.autofresh()) {
                                //如果自动更新，且操作是更新的话，就自动更新关联对象
                                DBManager.getInstance().getDao(tone.getClass()).newOrUpdate(tone);
                            }
                            //如果是实体类型，就存他的id
                            if (tone.getClass().isAnnotationPresent(Table.class)) {
                                String idName = DBUtil.getIDColumnName(tone.getClass());
                                Field toneIdField = tone.getClass().getDeclaredField(idName);
                                toneIdField.setAccessible(true);
                                values.put(DBUtil.getColumnName(field), toneIdField.get(tone).toString());
                            }
                        } else if (type == Column.ColumnType.TMANY) {
                            if (toManyFields == null) {
                                toManyFields = new ArrayList<>();
                            }
                            if (toManyAutoRefresh == null) {
                                toManyAutoRefresh = new ArrayList<>();
                            }
                            toManyFields.add(field);
                            toManyAutoRefresh.add(column.autofresh() ? true : false);
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long id = newOrUpdate(mTableName, values);
        //插入完住表之后，再处理处理一对多关系
        if (id > 0 && toManyFields != null) {
            try {
                for (int i = 0; i < toManyFields.size(); i++) {
                    List<Object> tmany = (List<Object>) toManyFields.get(i).get(t);
                    if (tmany == null) {
                        continue;
                    }
                    String associationTable = DBUtil.getAssociationTableName(t.getClass(), toManyFields.get(i).getName());
                    //防止数据错乱
                    delete(associationTable, DBUtil.PK1 + "=?", new String[]{String.valueOf(id)});
                    if (tmany != null) {
                        ContentValues associationValues = new ContentValues();
                        long tmanyId = 0;

                        for (Object object : tmany) {
                            if (toManyAutoRefresh.get(i)) {
                                freshToOneField(object, t.getClass(), id, mIdField);//更新一对多对象之中相对应的一对一信息，保存id
                                tmanyId = DBManager.getInstance().getDao(object.getClass()).newOrUpdate(object);
                            }
                            associationValues.clear();
                            associationValues.put(DBUtil.PK1, id);// company
                            // id
                            String idName = DBUtil.getIDColumnName(object.getClass());
                            Field tmanyIdField = object.getClass().getDeclaredField(idName);
                            tmanyIdField.setAccessible(true);
                            long value = (long) tmanyIdField.get(object);
                            if (value > 0) {//如果有值，不是默认的
                                associationValues.put(DBUtil.PK2, value); // developer
                            } else {//如果是默认的
                                associationValues.put(DBUtil.PK2, tmanyId);

                            }

                            //todo insert or update
                            newOrUpdate(associationTable, associationValues);
                            // 1,2 3,-1 2,3
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return id;
    }

    private long newOrUpdate(String tableName, ContentValues values) {
        return mDatabase.replace(tableName, null, values);
    }

    private void freshToOneField(Object tomany, Class<?> mainClz, long mainIdvalue, Field mainIdField) {
        Field[] declaredFields = tomany.getClass().getDeclaredFields();
        Column column;
        Column.ColumnType type;
        try {
            for (Field field : declaredFields) {
                column = field.getAnnotation(Column.class);
                type = column.type();
                if (type == Column.ColumnType.TONE) {//如果是toone
                    Class<?> clz = field.getType();
                    if (clz == mainClz) {//而且是在一对多之中相同类型的
                        Object o = clz.newInstance();
                        mainIdField.setAccessible(true);
                        mainIdField.setLong(o, mainIdvalue);
                        field.setAccessible(true);
                        field.set(tomany, o);
                    }


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public T queryById(long id) {
        Cursor cursor = rawQuery(mTableName, mIdName + "=?", new String[]{String.valueOf(id)});
        T t = null;
        if (cursor.moveToNext()) {
            t = handleCursor(cursor, true);
        }
        return t;
    }

    public ArrayList<T> queryByField(String fieldname, String value) {
        Cursor cursor = rawQuery(mTableName, fieldname + "=?", new String[]{value});
        ArrayList<T> result = new ArrayList<>();
        T t = null;
        while (cursor.moveToNext()) {
            t = handleCursor(cursor, true);
            result.add(t);
        }
        return result;
    }

    public ArrayList<T> queryAll() {
        Cursor cursor = mDatabase.rawQuery("select * from " + mTableName, null);
        ArrayList<T> result = new ArrayList<>();
        T t = null;
        while (cursor.moveToNext()) {
            t = handleCursor(cursor, false);
            result.add(t);
        }
        return result;
    }

    public <T> void delete(T t) {
        try {
            long id = (long) mIdField.get(t);
            delete(String.valueOf(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void delete(String id) {
        try {
            delete(DBUtil.getTableName(clz), mIdName + "=?", new String[]{id});
            // delete related association data
            for (Field field : mForeignFields) {
                delete(DBUtil.getAssociationTableName(clz, field.getName()), DBUtil.PK1 + "=?", new String[]{id});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void delete(String tableName, String where, String[] args) {
        mDatabase.delete(tableName, where, args);

    }

    public Cursor rawQuery(String tableName, String where, String[] args) {
        Cursor cursor = mDatabase.rawQuery("select * from " + tableName + " where " + where, args);

        return cursor;
    }

    private T handleCursor(Cursor cursor, boolean hasId) {
        T t = null;
        try {
            t = clz.newInstance();
            for (Field field : mColumnFields) {
                if (field.isAnnotationPresent(Column.class)) {
                    field.setAccessible(true);
                    Class<?> columnType = field.getType();
                    if (columnType == Integer.class || columnType == int.class) {
                        field.setInt(t, cursor.getInt(cursor.getColumnIndex(DBUtil.getColumnName(field))));
                    } else if (columnType == long.class || columnType == Long.class) {
                        field.setLong(t, cursor.getLong(cursor.getColumnIndex(DBUtil.getColumnName(field))));
                    } else if (columnType == boolean.class || columnType == Boolean.class) {
                        String boo = cursor.getString(cursor.getColumnIndex(DBUtil.getColumnName(field)));
                        field.set(t, boo.equals("1") ? true : false);
                    } else if (columnType == String.class) {
                        field.set(t, cursor.getString(cursor.getColumnIndex(DBUtil.getColumnName(field))));
                    } else {
                        Column column = field.getAnnotation(Column.class);
                        Column.ColumnType type = column.type();
                        if (!TextUtil.isValidate(type.name())) {
                            throw new IllegalArgumentException("you should set type to the special column:" + t.getClass().getSimpleName() + "."
                                    + field.getName());
                        }
                        if (type == Column.ColumnType.SERIALIZABLE) {
                            field.set(t, SerializeUtil.deserialize(cursor.getBlob(cursor.getColumnIndex(DBUtil.getColumnName(field)))));
                            // field.set(t,
                            // JsonUtil.fromJson(cursor.getString(cursor.getColumnIndex(DBUtil.getColumnName(field))),field.getType()));
                        } else if (type == Column.ColumnType.TONE) {
                            String toneId = cursor.getString(cursor.getColumnIndex(DBUtil.getColumnName(field)));
                            if (!TextUtil.isValidate(toneId)) {
                                continue;
                            }
                            Object tone = null;
                            if (column.autofresh()) {
                                tone = DBManager.getInstance().getDao(field.getType()).queryById(Long.parseLong(toneId));
                            } else {
                                tone = field.getType().newInstance();
                                if (field.getType().isAnnotationPresent(Table.class)) {
                                    String idName = DBUtil.getIDColumnName(field.getType());
                                    Field idField = field.getType().getDeclaredField(idName);
                                    idField.setAccessible(true);
                                    idField.set(tone, Long.parseLong(toneId));
                                }
                            }
                            field.set(t, tone);
                        } else if (type == Column.ColumnType.TMANY) {
                            //		TODO
                            String id;
                            if (hasId) {
                                id = DBUtil.getIdValue(t);
                            } else {
                                id = String.valueOf(cursor.getLong(cursor.getColumnIndex(DBUtil.getColumnName(mIdField))));
                            }
                            Class relatedClass = (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                            Cursor tmanyCursor = mDatabase.rawQuery("select * from " + DBUtil.getAssociationTableName(clz, field.getName()) +
                                    " where " + DBUtil.PK1 + "=?", new String[]{id});
                            ArrayList list = new ArrayList();
                            String tmanyId = null;
                            Object tmany = null;
                            while (tmanyCursor.moveToNext()) {
                                tmanyId = tmanyCursor.getString(tmanyCursor.getColumnIndex(DBUtil.PK2));
                                if (column.autofresh()) {
                                    tmany = DBManager.getInstance().getDao(relatedClass).queryById(Long.parseLong(tmanyId));
                                } else {
                                    tmany = relatedClass.newInstance();
                                    String idName = DBUtil.getIDColumnName(relatedClass);
                                    Field idField = relatedClass.getDeclaredField(idName);
                                    idField.setAccessible(true);
                                    idField.set(tmany, Long.parseLong(tmanyId));
                                }
                                if (tmany != null) {
                                    list.add(tmany);
                                } else {
                                }
                            }
                            if (!TextUtil.isValidatelist(list)) {
                                continue;
                            }
                            field.set(t, list);
                        }
                    }
                }
            }
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getTableDataCount(String tableName) {
        int count = -1;
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + tableName, new String[]{});
        count = cursor.getCount();
        return count;

    }
}
