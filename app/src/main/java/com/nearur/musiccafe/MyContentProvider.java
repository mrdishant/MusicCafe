
package com.nearur.musiccafe;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.widget.Toast;

public class MyContentProvider extends ContentProvider {


    db1 helper;
    SQLiteDatabase f;

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
    String tab=uri.getLastPathSegment();

       long id= f.insert(tab,null,values);
    return Uri.parse("/dummy/"+id);
    }

    @Override
    public boolean onCreate() {
        helper=new db1(getContext(),Util.db,null,Util.dversion);
        f=helper.getWritableDatabase();
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        String tab=uri.getLastPathSegment();
        Cursor c=f.query(tab,projection,selection,selectionArgs,null,null,null);
        return c;
         }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    class db1 extends SQLiteOpenHelper{



        public db1(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(Util.query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            Toast.makeText(getContext(),"Hello",Toast.LENGTH_LONG).show();
            onCreate(sqLiteDatabase);
        }
    }
}
