package com.hletong.hyc.model.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.hletong.hyc.contract.DraftContract;
import com.hletong.hyc.model.Draft;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.util.Executors;
import com.hletong.mob.architect.error.DataError;
import com.hletong.mob.architect.error.ErrorState;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.model.repository.DataCallback;
import com.hletong.mob.architect.model.repository.DataRepository;
import com.hletong.mob.architect.model.repository.UIDataCallback;
import com.xcheng.okhttp.util.ParamUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ddq on 2017/4/6.
 * 草稿数据源
 */

public class DraftDataSource extends DataRepository implements DraftContract.LocalDataSource {
    private DraftDB db;

    public DraftDataSource(Context context) {
        db = new DraftDB(context);
    }

    @Override
    public void loadItem(String cargoUuid, DataCallback<Draft> callback) {
        Executors.getExecutors().execute(new LoadItem(new UIDataCallback<>(callback), cargoUuid));
    }

    @Override
    public void loadList(int start, int limit, DataCallback<List<Draft>> callback) {
        Executors.getExecutors().execute(new loadList(new UIDataCallback<>(callback), start, limit));
    }

    @Override
    public void modify(Source draft, DataCallback<CommonResult> callback) {
        Executors.getExecutors().execute(new Modify(draft, new UIDataCallback<>(callback)));
    }

    @Override
    public void delete(ArrayList<Draft> sources, DataCallback<CommonResult> callback) {
        Executors.getExecutors().execute(new Delete(sources, new UIDataCallback<>(callback)));
    }

    private class LoadItem implements Runnable {
        private DataCallback<Draft> callback;
        private String cargoUuid;

        public LoadItem(DataCallback<Draft> callback, String cargoUuid) {
            this.callback = callback;
            this.cargoUuid = cargoUuid;
        }

        @Override
        public void run() {
            Draft draft = db.loadItem(cargoUuid);
            if (draft == null)
                callback.onError(new DataError(ErrorState.NO_DATA));
            else
                callback.onSuccess(draft);
        }
    }

    private class loadList implements Runnable {
        private DataCallback<List<Draft>> callback;
        private int start;
        private int limit;

        public loadList(DataCallback<List<Draft>> callback, int start, int limit) {
            this.callback = callback;
            this.start = start;
            this.limit = limit;
        }

        @Override
        public void run() {
            ArrayList<Draft> list = db.loadList(start, limit);
            if (ParamUtil.isEmpty(list)) {
                callback.onError(new DataError(ErrorState.NO_DATA));
            } else {
                callback.onSuccess(list);
            }
        }
    }

    private class Modify implements Runnable {
        private Source s;
        private DataCallback<CommonResult> callback;

        public Modify(Source s, DataCallback<CommonResult> callback) {
            this.s = s;
            this.callback = callback;
        }

        @Override
        public void run() {
            CommonResult cr = db.modify(s);
            if (cr.isSuccess()) {
                callback.onSuccess(cr);
            } else {
                callback.onError(new DataError(ErrorState.BUSINESS_ERROR, cr.getErrorInfo()));
            }
        }
    }

    private class Delete implements Runnable {
        private ArrayList<Draft> s;
        private DataCallback<CommonResult> callback;

        public Delete(ArrayList<Draft> s, DataCallback<CommonResult> callback) {
            this.s = s;
            this.callback = callback;
        }

        @Override
        public void run() {
            CommonResult cr = db.delete(s);
            if (cr.isSuccess()) {
                callback.onSuccess(cr);
            } else {
                callback.onError(new DataError(ErrorState.BUSINESS_ERROR, cr.getErrorInfo()));
            }
        }
    }

    /**
     * V1创建表
     */
    public static class DraftDB extends LocalDB {
        private static final String TABLE_NAME = "draft";
        private static final TableField CARGO_UUID = TableField.PRIMARY_KEY("cargo_uuid", TableField.VARCHAR);
        private static final TableField CARGO_NAME = TableField.NOT_NULL("cargo_name", TableField.VARCHAR);
        private static final TableField UPDATE_TIME = TableField.NOT_NULL("update_time", TableField.INTEGER);
        private static final TableField OTHER_ = TableField.NORMAL("other_", TableField.TEXT);
        private Gson mGson;

        public DraftDB(Context context) {
            super(context);
            mGson = new Gson();
        }

        public static String getCreateTableSql() {
            return getCreateTableSql(TABLE_NAME, CARGO_UUID, CARGO_NAME, UPDATE_TIME, OTHER_);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        public Draft loadItem(String cargoUuid) {
            Draft draft = null;
            String data = null;
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME, new String[]{OTHER_.getName()}, CARGO_UUID + "=?", new String[]{cargoUuid}, null, null, null);
            if (cursor.moveToFirst())
                data = OTHER_.getAsString(cursor);
            cursor.close();
            db.close();
            if (data != null) {
                draft = mGson.fromJson(data, Draft.class);
            }
            return draft;
        }

        public ArrayList<Draft> loadList(int start, int limit) {
            ArrayList<Draft> list = new ArrayList<>();
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME, new String[]{CARGO_UUID.getName(), CARGO_NAME.getName(), UPDATE_TIME.getName()}, null, null, null, null, UPDATE_TIME + " desc", start + "," + limit);
            if (cursor.getCount() > 0) {
                int i = 0;
                do {
                    cursor.moveToPosition(i);
                    Draft draft = new Draft();
                    list.add(draft);
                    draft.setOrgin_cargon_kind_name(CARGO_NAME.getAsString(cursor));
                    draft.setCargo_uuid(CARGO_UUID.getAsString(cursor));
                    draft.setCreate_ts(UPDATE_TIME.getTimeAsString(cursor));
                } while (++i < cursor.getCount());
            }

            cursor.close();
            db.close();
            return list;
        }

        //修改或新增
        public CommonResult modify(Source draft) {
            SQLiteDatabase db = getWritableDatabase();
            if (draft.getCargo_uuid() == null) {
                draft.setCargo_uuid(UUID.randomUUID().toString());
            } else {
                db.delete(TABLE_NAME, CARGO_UUID + "=?", new String[]{draft.getCargo_uuid()});
            }
            long current = System.currentTimeMillis();
            draft.setCreate_ts(String.valueOf(current));
            ContentValues values = new ContentValues();
            values.put(CARGO_UUID.getName(), draft.getCargo_uuid());
            values.put(CARGO_NAME.getName(), draft.getOrgin_cargon_kind_name());
            values.put(UPDATE_TIME.getName(), current);
            values.put(OTHER_.getName(), mGson.toJson(draft));
            long i = db.insert(TABLE_NAME, null, values);
            db.close();
            return CommonResult.RESULT(i != -1);
        }

        public CommonResult delete(ArrayList<Draft> sources) {
            if (ParamUtil.isEmpty(sources))
                return CommonResult.RESULT(true);
            SQLiteDatabase db = getWritableDatabase();
            int i = 0;
            for (Draft s : sources) {
                i += db.delete(TABLE_NAME, CARGO_UUID + "=?", new String[]{s.getCargo_uuid()});
            }
            db.close();
            return CommonResult.RESULT(i == sources.size());
        }
    }
}

