package com.sunian.baselib.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.sunian.baselib.util.DesCryptoUtils;
import com.sunian.baselib.util.StringUtil;

/**
 * Created by fujun on 2018/4/8.
 */

public class DBHelperImp extends SQLiteOpenHelper implements IDBHelper {
    private Context context;
    private static final String DB_NAME = "zhishang.db";
    private static final int DB_VERSION = 1;
    static final String CACHE_TABLE = "cache";
    private final SQLiteDatabase mDatabase;
    private String keyDBHelperImp = "DBHelper";

    public DBHelperImp(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        mDatabase = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + CACHE_TABLE +
                " (url text, params text,time integer, response text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + CACHE_TABLE;
        db.execSQL(sql);
        onCreate(db);
    }


    @Override
    @Nullable
    public String queryResponse(@NonNull String urlKey, @Nullable String params, long time) {
        String result = null;
        try {
            if (urlKey == null)
                return null;
            time = System.currentTimeMillis() - time;
            String query;
            Cursor cursor;
            if (params != null) {
                query = "select response from " + CACHE_TABLE + " where url=? and params=? and time>=?";
                cursor = mDatabase.rawQuery(query, new String[]{urlKey, params, time + ""});
            } else {
                query = "select response from " + CACHE_TABLE + " where url=? and time>=?";
                cursor = mDatabase.rawQuery(query, new String[]{urlKey, time + ""});
            }
            if (cursor != null && cursor.moveToNext()) {
                result = cursor.getString(0);
            }
        } catch (Throwable ignored) {
        }

        return DesCryptoUtils.decode(keyDBHelperImp, result);
    }

    @Override
    public void insertResponse(@NonNull String urlKey, String params, @NonNull String value) {

        new Thread() {
            @Override
            public void run() {
                try {
                    if (urlKey == null || StringUtil.isNull(value))
                        return;
                    deleteResponse(urlKey, params);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("url", urlKey);
                    if (params != null)
                        contentValues.put("params", params);
                    contentValues.put("response", DesCryptoUtils.encode(keyDBHelperImp, value));
                    contentValues.put("time", System.currentTimeMillis());
                    mDatabase.insert(CACHE_TABLE, null, contentValues);
                } catch (Throwable ignored) {
                }
            }
        }.start();


    }


    @Override
    public void deleteResponse(@NonNull String urlKey, String params) {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (urlKey == null)
                        return;
                    String query;
                    String whereArgs[];
                    if (params != null) {
                        query = "url=? and params=?";
                        whereArgs = new String[]{urlKey, params};

                    } else {
                        query = "url=?";
                        whereArgs = new String[]{urlKey};
                    }
                    mDatabase.delete(CACHE_TABLE, query, whereArgs);

                } catch (Throwable ignored) {

                }
            }
        }.start();

    }

    @Override
    public void insertResponseJsonBean(@NonNull String urlKey, @Nullable String params, @NonNull Object object) {

        new Thread() {
            @Override
            public void run() {
                try {
                    if (urlKey == null || object == null)
                        return;
                    deleteResponse(urlKey, params);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("url", urlKey);
                    if (params != null)
                        contentValues.put("params", params);
                    contentValues.put("response", DesCryptoUtils.encode(keyDBHelperImp, new Gson().toJson(object)));
                    contentValues.put("time", System.currentTimeMillis());
                    mDatabase.insert(CACHE_TABLE, null, contentValues);
                } catch (Throwable ignored) {
                }
            }
        }.start();

    }

    @Override
    public void deleteAllResponse() {
        new Thread() {
            @Override
            public void run() {
                try {
                    mDatabase.delete(CACHE_TABLE, null, null);
                } catch (Throwable ignored) {
                }

            }
        }.start();

    }

}
