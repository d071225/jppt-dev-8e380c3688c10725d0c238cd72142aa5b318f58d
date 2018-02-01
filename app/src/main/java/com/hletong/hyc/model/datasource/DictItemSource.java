package com.hletong.hyc.model.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;

import com.hletong.hyc.contract.DictItemContract;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.util.Executors;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.util.SPUtils;
import com.xcheng.okhttp.callback.UICallback;
import com.xcheng.okhttp.util.ParamUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ddq on 2017/3/10.
 * 获取字典项
 */

public class DictItemSource implements DictItemContract.DictLocalSource {
    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private DictItemDataBase mDataBase;

    public DictItemSource(Context activity) {
        mDataBase = new DictItemDataBase(activity);

        if (SPUtils.getBoolean("dirtyData", false)) {
            mDataBase.clear();//清空缓存数据
            SPUtils.putBoolean("dirtyData", false);//设置缓存数据未过期
        }
    }

    @Override
    public void loadDict(String key, UICallback<List<DictionaryItem>> callback) {
        Executors.getExecutors().execute(new Runner(key, callback));
    }

    private class Runner implements Runnable {
        private String key;
        private UICallback<List<DictionaryItem>> mCallback;

        public Runner(String key, UICallback<List<DictionaryItem>> callback) {
            this.key = key;
            mCallback = callback;
        }

        @Override
        public void run() {
            final List<DictionaryItem> list = mDataBase.getDictItems(key);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onSuccess(null, list);
                }
            });
        }
    }

    @Override
    public void saveDict(Map<String, List<DictionaryItem>> map, UICallback<CommonResult> callback) {
        Executors.getExecutors().execute(new Runner2(map, callback));
    }

    private class Runner2 implements Runnable {
        private Map<String, List<DictionaryItem>> data;
        private UICallback<CommonResult> mCallback;

        public Runner2(Map<String, List<DictionaryItem>> data, UICallback<CommonResult> callback) {
            this.data = data;
            mCallback = callback;
        }

        @Override
        public void run() {
            mDataBase.save(data);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onSuccess(null, CommonResult.RESULT(true));
                }
            });
        }
    }
//    private class FetchData implements Runnable {
//        private ListRequestValue<DictionaryItem> requestValue;
//        private DataCallback<List<DictionaryItem>> callback;
//
//        public FetchData(ListRequestValue<DictionaryItem> requestValue, DataCallback<List<DictionaryItem>> callback) {
//            this.requestValue = requestValue;
//            this.callback = callback;
//        }
//
//        @Override
//        public void run() {
//            final String fieldName = requestValue.getParams().get("fieldName");
//            List<DictionaryItem> types = caches.get(fieldName);
//            if (types == null) {
//                types = mDataBase.getDictItems(fieldName);
//                if (!RequestState.isEmptyData(types)) {
//                    caches.put(fieldName, types);
//                    callback.onSuccess(types);
//                } else {
//                    if (requestValue.getUrl().endsWith(Constant.CARGO_FORECAST_DICTIONARY)) {
//                        ItemRequestValue<Dictionary> rv1=new ItemRequestValue<Dictionary>(
//                                requestValue.getHttpFlag(),
//                                requestValue.getUrl(),
//                                requestValue.getParams(),
//                                requestValue.getEntry()
//                        ) {
//                        };
//                        rv1.setRequestFlag(rv1.getRequestFlag());
//                        loadItem(rv1, new DataCallbackWrapper(callback, fieldName));
//                    } else {
//                        ListRequestValue<LinkedTreeMap<String, List<DictionaryItem>>> rv2 = new ListRequestValue<LinkedTreeMap<String, List<DictionaryItem>>>(
//                                requestValue.getHttpFlag(),
//                                requestValue.getUrl(),
//                                null,
//                                null
//                        ) {
//                        };
//                        rv2.setRequestFlag(requestValue.getRequestFlag());
//                        loadList(rv2, new DataCallbackWrapper2(callback, fieldName));
//                    }
//                }
//            } else {
//                callback.onSuccess(types);
//            }
//        }
//    }
//
//    //向服务端请求数据的Callback
//    private class DataCallbackWrapper extends DataCallback<Dictionary> {
//        private DataCallback<List<DictionaryItem>> mCallback;
//        private String fieldName;
//
//        public DataCallbackWrapper(DataCallback<List<DictionaryItem>> callback, String fieldName) {
//            mCallback = callback;
//            this.fieldName = fieldName;
//        }
//
//        @Override
//        public void onSuccess(@NonNull Dictionary response) {
//            mDataBase.saveDictItems(response);
//            SPUtils.putString("CargoOwnerInfo", response.getMemberDto());
//            List<DictionaryItem> list = response.getDictionary(fieldName);
//            if (RequestState.isEmptyData(list)) {
//                mCallback.onError(new DataError(ErrorState.NO_DATA));
//            } else {
//                caches.put(fieldName, list);//存储到缓存
//                mCallback.onSuccess(list);//返回数据
//            }
//        }
//
//        @Override
//        public void onError(@NonNull BaseError error) {
//            mCallback.onError(error);
//        }
//
//        @Override
//        public IProgress getProgress() {
//            return mCallback.getProgress();
//        }
//    }

//    private class DataCallbackWrapper2 extends DataCallback<List<LinkedTreeMap<String, List<DictionaryItem>>>> {
//        private DataCallback<List<DictionaryItem>> mCallback;
//        private String fieldName;
//
//        public DataCallbackWrapper2(DataCallback<List<DictionaryItem>> callback, String fieldName) {
//            mCallback = callback;
//            this.fieldName = fieldName;
//        }
//
//        @Override
//        public void onSuccess(@NonNull List<LinkedTreeMap<String, List<DictionaryItem>>> response) {
//            //添加性别字典项
//            {
//                LinkedTreeMap<String, List<DictionaryItem>> gender = new LinkedTreeMap<>();
//                List<DictionaryItem> di = new ArrayList<>(2);
//                di.add(new DictionaryItem("0", "男"));
//                di.add(new DictionaryItem("1", "女"));
//                gender.put("gender", di);
//                response.add(gender);
//            }
//            mDataBase.saveDictItems(response);
//            List<DictionaryItem> list = null;
//            for (LinkedTreeMap<String, List<DictionaryItem>> items : response) {
//                list = items.get(fieldName);
//                if (list != null)
//                    break;
//            }
//            if (RequestState.isEmptyData(list)) {
//                mCallback.onError(new DataError(ErrorState.NO_DATA));
//            } else {
//                caches.put(fieldName, list);//存储到缓存
//                mCallback.onSuccess(list);//返回数据
//            }
//        }
//
//        @Override
//        public void onError(@NonNull BaseError error) {
//            mCallback.onError(error);
//        }
//
//        @Override
//        public IProgress getProgress() {
//            return mCallback.getProgress();
//        }
//    }

    //使用数据库存储
    public static class DictItemDataBase extends LocalDB {
        /**
         * 数据库v1，新建表dictItem
         */
        private static final String TABLE_NAME = "dictItem";
        private static final TableField DICT_TYPE = TableField.NORMAL("dictType", TableField.VARCHAR);
        private static final TableField ITEM_ID = TableField.NORMAL("itemId", TableField.VARCHAR);
        private static final TableField ITEM_TEXT = TableField.NORMAL("itemText", TableField.VARCHAR);
        private static final TableField ITEM_INDEX = TableField.NORMAL("itemIndex", TableField.INTEGER);

        public DictItemDataBase(Context context) {
            super(context);
        }

        public static String getCreateTableSql() {
            return getCreateTableSql(TABLE_NAME, DICT_TYPE, ITEM_ID, ITEM_TEXT, ITEM_INDEX);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        List<DictionaryItem> getDictItems(String fieldName) {
            List<DictionaryItem> types = new ArrayList<>();
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME, null, DICT_TYPE + "=?", new String[]{fieldName}, null, null, ITEM_INDEX + " asc", null);
            if (cursor.moveToFirst()) {
                do {
                    types.add(new DictionaryItem(ITEM_ID.getAsString(cursor), ITEM_TEXT.getAsString(cursor)));
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return types;
        }

        void clear() {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("delete from " + TABLE_NAME);
            db.close();
        }
//
//        boolean saveDictItems(Dictionary dictionary) {
//            boolean success = true;
//            SQLiteDatabase db = getWritableDatabase();
//            db.beginTransaction();
//            try {
//                save(dictionary.getDictionaries(), db);
//            } catch (Exception e) {
//                e.printStackTrace();
//                success = false;
//            } finally {
//                db.endTransaction();
//                db.close();
//            }
//            return success;
//        }
//
//        boolean saveDictItems(List<LinkedTreeMap<String, List<DictionaryItem>>> list) {
//            boolean success = true;
//            SQLiteDatabase db = getWritableDatabase();
//            db.beginTransaction();
//            try {
//                for (LinkedTreeMap<String, List<DictionaryItem>> item : list) {
//                    save(item, db);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                success = false;
//            } finally {
//                db.endTransaction();
//                db.close();
//            }
//            return success;
//        }

        private void save(Map<String, List<DictionaryItem>> data) {
            SQLiteDatabase db = getWritableDatabase();
            for (Map.Entry<String, List<DictionaryItem>> entry : data.entrySet()) {
                saveDictItems(entry.getKey(), entry.getValue(), db);
            }
            db.close();
        }

        private void deleteIfExist(String fieldName, SQLiteDatabase db) {
            int rows = db.delete(TABLE_NAME, DICT_TYPE + "=?", new String[]{fieldName});
        }

        private boolean saveDictItems(String fieldName, List<DictionaryItem> list, SQLiteDatabase db) {
            if (ParamUtil.isEmpty(list))
                return false;

            deleteIfExist(fieldName, db);
            ContentValues values = new ContentValues();
            for (int i = 0; i < list.size(); i++) {
                values.clear();
                values.put(DICT_TYPE.getName(), fieldName);
                values.put(ITEM_ID.getName(), list.get(i).getId());
                values.put(ITEM_TEXT.getName(), list.get(i).getText());
                values.put(ITEM_INDEX.getName(), i);
                db.insert(TABLE_NAME, null, values);
            }
            return true;
        }
    }
}
