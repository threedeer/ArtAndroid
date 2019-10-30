package com.tt.art.contentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.Log;

import com.tt.art.data.DBOpenHelper;

/**
 * @author T
 * @date 2019/10/9
 * @Description 以表格心事组织数据，并且可以包含多个表，每个表格都具有行和列的层次性
 * 还支持文件数据
 */
public class BookProvider extends ContentProvider {
    private static final String TAG = "BookProvider";
    public static final String AUTHORITY = "com.tt.art.contentProvider.BookProvider";
    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/book");
    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user");

    public static final int BOOK_URI_CODE = 0;
    public static final int USER_URI_CODE = 1;

    /**
     * UriMatcher本质上是一个文本过滤器
     * 可以帮助我们方便的过滤到TableA还是TableB
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, "book", BOOK_URI_CODE);
        sUriMatcher.addURI(AUTHORITY, "user", USER_URI_CODE);
    }

    private Context mContext;
    private SQLiteDatabase mDatabase;

    @Override
    public boolean onCreate() {
        //做一些初始化操作
        // 系统回调  主线程
        Log.d(TAG, "onCreate,current thread : " + Thread.currentThread().getName());
        mContext = getContext();
        initProviderData();
        return true;
    }

    private void initProviderData() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
                mDatabase = new DBOpenHelper(mContext).getWritableDatabase();
                mDatabase.execSQL("delete from " + DBOpenHelper.BOOK_TABLE_NAME);
                mDatabase.execSQL("delete from " + DBOpenHelper.USER_TABLE_NAME);
                mDatabase.execSQL("insert into book values(3,'Android');");
                mDatabase.execSQL("insert into book values(4,'Ios');");
                mDatabase.execSQL("insert into book values(5,'jave');");
                mDatabase.execSQL("insert into user values(1,'张三',1);");
                mDatabase.execSQL("insert into user values(2,'李四',0);");
//            }
//        }).start();
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // 查询  Binder线程池
        Log.d(TAG, "query,current thread:" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        return mDatabase.query(table,projection,selection,selectionArgs,null,null,sortOrder,null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        //用来返回一个Uri请求对应的MIME类型（媒体类型）
        //  Binder线程池
        Log.d(TAG, "getType");
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        // 增加  Binder线程池
        Log.d(TAG, "insert");
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        mDatabase.insert(table,null,values);
        mContext.getContentResolver().notifyChange(uri,null);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // 删除  Binder线程池
        Log.d(TAG, "delete");
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int count = mDatabase.delete(table,selection,selectionArgs);
        if (count > 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        // 修改  Binder线程池
        Log.d(TAG, "update");
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int row = mDatabase.update(table,values,selection,selectionArgs);
        if (row > 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return row;
    }


    /**
     * 获取表名
     *
     * @param uri
     * @return
     */
    private String getTableName(Uri uri) {
        String tableName = null;
        switch (sUriMatcher.match(uri)) {
            case BOOK_URI_CODE:
                tableName = DBOpenHelper.BOOK_TABLE_NAME;
                break;
            case USER_URI_CODE:
                tableName = DBOpenHelper.USER_TABLE_NAME;
            default:
                break;
        }
        return tableName;
    }
}
