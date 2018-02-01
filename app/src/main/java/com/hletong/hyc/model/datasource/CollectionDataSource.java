package com.hletong.hyc.model.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hletong.mob.architect.model.repository.DataRepository;
import com.hletong.hyc.contract.UpcomingContract;
import com.hletong.mob.util.SimpleDate;

import java.util.Calendar;
import java.util.HashSet;

/**
 * Created by ddq on 2017/4/1.
 * 货方收藏车船
 */

public class CollectionDataSource extends DataRepository implements UpcomingContract.LocalDataSource {
    private HashSet<String> mCache;
//    private CollectionDB db;

    public CollectionDataSource(Context context) {
//        db = new CollectionDB(context);
        mCache = new HashSet<>();
    }

    @Override
    public void addToCollection(String tradeUuid) {
        mCache.add(tradeUuid);
//        db.insert(tradeUuid);
    }

    @Override
    public boolean isCollected(String tradeUuid) {
//        if (!mCache.contains(tradeUuid)) {
//            if (db.isCollected(tradeUuid)) {
//                mCache.add(tradeUuid);
//                return true;
//            }
//            return false;
//        }
        return true;
    }

    public static class CollectionDB extends LocalDB {
        private static final String TABLE_NAME = "cargo_collection";
        private static final TableField TRADE_UUID = TableField.UNIQUE("tradeUuid", TableField.VARCHAR);
        private static final TableField TIME_ = TableField.NOT_NULL("time_", TableField.INTEGER);

        CollectionDB(Context context) {
            super(context);
            clearOldData();
        }

        public static String getCreateTableSql(){
            return getCreateTableSql(TABLE_NAME, TRADE_UUID, TIME_);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        //清除2个星期之前的数据
        private void clearOldData() {
            SQLiteDatabase db = getWritableDatabase();
            if (isTableExists(TABLE_NAME)) {
                Calendar calendar = Calendar.getInstance();
                db.delete(TABLE_NAME, TIME_ + "<?", new String[]{String.valueOf(calendar.getTimeInMillis() - SimpleDate.WEEK_MILLISECONDS * 2)});
            }
            db.close();
        }

        public void insert(String tradeUuid) {
            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor = db.query(TABLE_NAME, null, TRADE_UUID + "=?", new String[]{tradeUuid}, null, null, null);
            if (!cursor.moveToFirst()) {
                ContentValues values = new ContentValues();
                values.put(TRADE_UUID.getName(), tradeUuid);
                values.put(TIME_.getName(), System.currentTimeMillis());
                db.insert(TABLE_NAME, null, values);
            }
            cursor.close();
            db.close();
        }

        public boolean isCollected(String tradeUuid) {
            boolean result = false;
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME, null, TRADE_UUID + "=?", new String[]{tradeUuid}, null, null, null);
            result = cursor.moveToFirst();
            cursor.close();
            db.close();
            return result;
        }
    }
}
