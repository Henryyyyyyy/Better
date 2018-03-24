package com.example.lib_db.core;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.lib_db.annotation.Column;
import com.example.lib_db.annotation.Table;
import com.example.lib_db.utilities.TextUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;




public class DBUtil {
//创建关联表的两个字段
    public static final String PK1 = "pk1";
    public static final String PK2 = "pk2";

    public static String getTableName(Class<?> clz) {
        if (clz.isAnnotationPresent(Table.class)) {
            String name = clz.getAnnotation(Table.class).name();
            if (TextUtil.isValidate(name)) {
                return name;
            } else {
                return clz.getSimpleName().toLowerCase();
            }
        }
        throw new IllegalArgumentException("the class " + clz.getSimpleName() + " can't map to the table");
    }

    public static String getDropTableStmt(Class<?> clz) {
        // db.execSQL("drop table if exists developer");
        return "drop table if exists " + getTableName(clz);
    }

    // "create table if not exists company (id TEXT primary key NOT NULL , name TEXT, age TEXT, company TEXT, skills TEXT)"
    // "create table if not exists company_developers (pk1 TEXT, pk2 TEXT)"

    private static ArrayList<String> getCreateTableStmt(Class<?> clz) {
        StringBuilder mColumnStmts = new StringBuilder();
        ArrayList<String> stmts = new ArrayList<String>();
        if (clz.isAnnotationPresent(Table.class)) {
            Field[] fields = clz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].isAnnotationPresent(Column.class)) {
                    //这里是为了看一下字段里面是否存在一对多关系,然后再创建不同的表
                    if (fields[i].getAnnotation(Column.class).type() == Column.ColumnType.TMANY) {
                        stmts.add("create table if not exists " + getAssociationTableName(clz, fields[i].getName()) + "(" + PK1 + " TEXT, " + PK2
                                + " TEXT)");
                    }
                    //添加一个字段的sql语句
                    mColumnStmts.append(getOneColumnStmt(fields[i]));
                    mColumnStmts.append(",");
                }
            }
            //todo 喵喵喵？，减去上面的逗号
            if (mColumnStmts.length() > 0) {
                mColumnStmts.delete(mColumnStmts.length() - 1, mColumnStmts.length());
            }
        }
        stmts.add("create table if not exists " + getTableName(clz) + " (" + mColumnStmts + ")");
        return stmts;
    }

    public static String getAssociationTableName(Class<?> clz, String association) {
        return getTableName(clz) + "_" + association;
    }

    public static String getIDColumnName(Class<?> clz) {
        if (clz.isAnnotationPresent(Table.class)) {
            Field[] fields = clz.getDeclaredFields();
            Column column = null;
            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    column = field.getAnnotation(Column.class);
                    if (column.id()) {
                        String id = column.name();
                        if (!TextUtil.isValidate(id)) {
                            id = field.getName();
                            return id;
                        }else {
                            return id;
                        }

                    }
                }
            }
        }
        return null;
    }

    public static String getOneColumnStmt(Field field) {
        if (field.isAnnotationPresent(Column.class)) {
            Column column = field.getAnnotation(Column.class);
            String name = column.name();
            if (!TextUtil.isValidate(name)) {
                name = "[" + field.getName() + "]";//有些列名可能与sql冲突，加上中括号就可以避免这个问题
            } else {
                name = "[" + name + "]";
            }
            String type = null;
            Class<?> clz = field.getType();
            if (clz == String.class || clz == boolean.class || clz == Boolean.class) {
                type = " TEXT ";
            } else if (clz == int.class || clz == Integer.class || clz == long.class || clz == Long.class) {
                type = " integer ";
            } else {
                Column.ColumnType columnType = column.type();
                if (columnType == Column.ColumnType.TONE) {
                    type = " TEXT ";
                } else if (columnType == Column.ColumnType.SERIALIZABLE) {
                    type = " BLOB ";
                } else if (columnType == Column.ColumnType.TMANY) {
                    // do nothing
                }
                // TODO TMANY
            }
            name += type;
            // TODO not null unique
            if (column.id()) {
                name += " primary key ";
            }
            if (column.auto_increment()) {
                name += " autoincrement ";
            }
            return name;
        }
        return "";
    }

    public static void dropTable(SQLiteDatabase db, Class<?> clz) throws SQLException {
        db.execSQL(getDropTableStmt(clz));
        // drop the association table
        Field[] columnFields = clz.getDeclaredFields();
        ArrayList<Field> foreignFields = DBUtil.getForeignFields(columnFields);
        if (foreignFields.size() > 0) {
            for (Field field : foreignFields) {
                db.execSQL("drop table if exists " + getAssociationTableName(clz, field.getName()));
            }
        }
    }

    public static void createTable(SQLiteDatabase db, Class<?> clz) throws SQLException {
        ArrayList<String> stmts = getCreateTableStmt(clz);
        for (String stmt : stmts) {
            db.execSQL(stmt);
        }
//		db.execSQL(getCreateTableStmt(clz));
    }

    public static String getColumnName(Field field) {
        Column column = field.getAnnotation(Column.class);
        String name = column.name();//@column旁边标记的名字
        if (!TextUtil.isValidate(name)) {
            name = field.getName();//如果没有标记名字就用默认成员变量的名字
        }
        return name;
    }

    /**
     * 获得一张表的外键列表
     *
     * @param mColumnFields
     * @return
     */
    public static ArrayList<Field> getForeignFields(Field[] mColumnFields) {
        Column column = null;
        ArrayList<Field> fields = new ArrayList<Field>();
        for (Field field : mColumnFields) {
            if (field.isAnnotationPresent(Column.class)) {
                column = field.getAnnotation(Column.class);
                if (column.type() == Column.ColumnType.TMANY) {
                    fields.add(field);
                }
            }
        }
        return fields;
    }
    /**
     * 获得主键id字段的成员变量字段
     *
     * @param clz
     * @return
     */
    public static Field getIdField(Class<?> clz) {
        Column column = null;
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                column = field.getAnnotation(Column.class);
                if (column.id()) {
                  return field;
                }
            }
        }
        return null;
    }
    public static <T> String getIdValue(T t) {
        String id_value = null;
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                field.setAccessible(true);
                Column column = field.getAnnotation(Column.class);
                if (column.id()) {
                    try {
                        id_value = String.valueOf((long)field.get(t));
                        break;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        return id_value;

    }
}
