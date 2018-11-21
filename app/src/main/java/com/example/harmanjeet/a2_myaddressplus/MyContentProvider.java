package com.example.harmanjeet.a2_myaddressplus;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

public class MyContentProvider extends ContentProvider {

    private DatabaseHandler database;

    private static final int MYADDRS = 10;
    private static final int MYADDR_ID = 20;


    private static final String AUTHORITY = "com.example.harmanjeet.a2_myaddressplus.MyContentProvider";
    private static final String BASE_PATH = "myaddress";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/MYADDRS";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/MYADDR";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH,MYADDRS );
        sURIMatcher.addURI(AUTHORITY, BASE_PATH+ "/#", MYADDR_ID);

    }

    public boolean onCreate() {
        database = new DatabaseHandler(getContext());
        return false;
    }

    @Override
    public Cursor query( Uri uri,  String[] projection,  String selection,
                         String[] selectionArgs,  String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        checkColumns(projection);

        queryBuilder.setTables(AddressTableHandler.TABLE_MYADDR);

        int uriType = sURIMatcher.match(uri);
        switch (uriType){
            case MYADDRS:
                break;
            case MYADDR_ID:
                queryBuilder.appendWhere(AddressTableHandler.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI:" + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        // Make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;

    }



    @Override
    public String getType( Uri uri) {
        return null;
    }


    @Override
    public Uri insert( Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        //int rowsDeleted = 0;
        long id = 0;

        switch (uriType) {
            case MYADDRS:
                id = sqlDB.insert(AddressTableHandler.TABLE_MYADDR, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;

        switch (uriType) {
            case MYADDRS:
                rowsDeleted = sqlDB.delete(AddressTableHandler.TABLE_MYADDR, selection, selectionArgs);
                break;
            case MYADDR_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(AddressTableHandler.TABLE_MYADDR, AddressTableHandler.COLUMN_ID + "=" + id, null);
                } else {
                    rowsDeleted = sqlDB.delete(AddressTableHandler.TABLE_MYADDR, AddressTableHandler.COLUMN_ID + "=" + id
                            + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }



    @Override
    public int update( Uri uri,  ContentValues values, String selection,  String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;

        switch (uriType) {
            case MYADDRS:
                rowsUpdated = sqlDB.update(AddressTableHandler.TABLE_MYADDR, values, selection, selectionArgs);
                break;
            case MYADDR_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(AddressTableHandler.TABLE_MYADDR, values,
                            AddressTableHandler.COLUMN_ID + "=" + id, null);
                } else {
                    rowsUpdated = sqlDB.update(AddressTableHandler.TABLE_MYADDR,
                            values, AddressTableHandler.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = { AddressTableHandler.COLUMN_INTIALS,
                AddressTableHandler.COLUMN_FNAME, AddressTableHandler.COLUMN_LNAME,
                AddressTableHandler.COLUMN_ADDRESS, AddressTableHandler.COLUMN_COUNTRY,
                AddressTableHandler.COLUMN_PROVINCE, AddressTableHandler.COLUMN_PCODE,
                AddressTableHandler.COLUMN_ID };

        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // Check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}
