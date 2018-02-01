package com.hletong.hyc.model.datasource;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Locale;

/**
 * Created by ddq on 2017/4/1.
 * 哪些情况下数据库需要升级：
 * 1.现有表结构发生变化
 * 2.需要创建新表
 * 如果是表结构发生变化，更改数据库表结构之后，记得做好数据迁移
 * <p>
 * 为了便于维护，在每一个子类里面，做好版本变动的纪录
 */

public abstract class LocalDB extends SQLiteOpenHelper {
    private static final String DB_NAME = "hlet";
    private static final int VERSION = 2;

    public LocalDB(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public final void onCreate(SQLiteDatabase db) {
        /**
         * 数据库版本管理：
         * v1 创建字典项数据库表，创建货方收藏自主交易表
         *    创建草稿表
         */
        db.execSQL(DraftDataSource.DraftDB.getCreateTableSql());
        db.execSQL(DictItemSource.DictItemDataBase.getCreateTableSql());
        db.execSQL(CollectionDataSource.CollectionDB.getCreateTableSql());
        //后续加表，要注意区分用户有没有安装过APP，详见onCreate和onUpgrade的调用
    }

    protected final void createTable(SQLiteDatabase db, String tableName, TableField... fields) {
        db.execSQL(getCreateTableSql(tableName, fields));
    }

    protected static String getCreateTableSql(String tableName, TableField... fields){
        if (fields == null) {
            return null;
        }
        String s = "";
        for (int i = 0; i < fields.length; i++) {
            s += fields[i].getPartString();
            if (i != fields.length - 1)
                s += ",";
        }
        return String.format(Locale.getDefault(), "create table %s(%s)", tableName, s);
    }

    /**
     * 数据迁移
     *
     * @param db
     * @param from   旧表
     * @param to     新表
     * @param fields 旧表中需要迁移的数据字段
     */
    protected final void migrateData(SQLiteDatabase db, String from, String to, TableField... fields) {
        if (fields != null) {
            String s = "";
            for (int i = 0; i < fields.length; i++) {
                s += fields[i].getName();
                if (i != fields.length - 1)
                    s += ",";
            }
            //数据迁移
            db.execSQL(String.format(Locale.getDefault(), "insert into %s(%s) select %s from %s", to, s, s, from));
            dropTable(db, from);//删除旧表
        }
    }

    protected final boolean isTableExists(SQLiteDatabase db, String tableName) {
        boolean exist;
        Cursor cursor = db.query("sqlite_master", null, "name=?", new String[]{tableName}, null, null, null);
        exist = cursor.moveToFirst();
        cursor.close();
        return exist;
    }

    protected final boolean isTableExists(String tableName) {
        boolean exist;
        SQLiteDatabase db = getReadableDatabase();
        exist = isTableExists(db, tableName);
        db.close();
        return exist;
    }

    //删除数据库表
    protected final void dropTable(SQLiteDatabase db, String table) {
        db.execSQL("drop table " + table);
    }

    static class TableField {
        public static final String TINYINT = "tinyint";
        public static final String INTEGER = "integer";
        public static final String VARCHAR = "varchar";
        public static final String BOOLEAN = "tinyint";
        public static final String TEXT = "text";

        private boolean primaryKey = false;
        private String name;
        private String type;
        private boolean notNull = false;
        private boolean unique = false;

        private TableField(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public static TableField NORMAL(String name, String type) {
            return new TableField(name, type);
        }

        public static TableField NOT_NULL(String name, String type) {
            TableField tableField = NORMAL(name, type);
            tableField.notNull = true;
            return tableField;
        }

        public static TableField UNIQUE(String name, String type) {
            TableField tableField = NORMAL(name, type);
            tableField.unique = true;
            return tableField;
        }

        public static TableField PRIMARY_KEY(String name, String type) {
            TableField tableField = NORMAL(name, type);
            tableField.primaryKey = true;
            return tableField;
        }

        public String getName() {
            return name;
        }

        public String getAsString(Cursor c) {
            if (VARCHAR.equals(type) || TEXT.equals(type)) {
                return c.getString(c.getColumnIndex(name));
            }
            return null;
        }

        public String getTimeAsString(Cursor c){
            if (INTEGER.equals(type)){
                return String.valueOf(c.getLong(c.getColumnIndex(name)));
            }
            return null;
        }

        public String getPartString() {
            StringBuilder builder = new StringBuilder();
            builder.append(name);
            builder.append(" ");
            builder.append(type);
            if (primaryKey) {
                builder.append(" primary key");
            } else if (unique) {
                builder.append(" unique");
            } else if (notNull) {
                builder.append(" not null");
            }
            return builder.toString();
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
