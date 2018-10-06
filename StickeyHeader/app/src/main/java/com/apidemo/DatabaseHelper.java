package com.apidemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    String TAG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 1;
    static Context context;
    SQLiteDatabase db;

    public static final String TABLE_ARTICLES = "tbl_articles";

    public static class Articles {
        public static final String AUTHOR = "author";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String URLTOIMAGE = "urlToImage";
        public static final String PUBLISHEDAT = "publishedAt";
    }

    public DatabaseHelper(Context context) {
        super(context, App.DATABASE_FOLDER_FULLPATH, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public synchronized void close() {
        if (db != null)
            db.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_ARTICLES + "(" +
                        Articles.AUTHOR + " TEXT," +
                        Articles.TITLE + " TEXT," +
                        Articles.DESCRIPTION + " TEXT," +
                        Articles.URLTOIMAGE + " TEXT," +
                        Articles.PUBLISHEDAT + " TEXT" +
                        ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + TABLE_ARTICLES + "'");
        onCreate(sqLiteDatabase);
    }

    public void dropAllTable() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS '" + TABLE_ARTICLES + "'");
            onCreate(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dbInsertDate(ArticlesModel articlesModel) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(Articles.AUTHOR, articlesModel.author);
            values.put(Articles.TITLE, articlesModel.title);
            values.put(Articles.DESCRIPTION, articlesModel.description);
            values.put(Articles.URLTOIMAGE, articlesModel.urlToImage);
            values.put(Articles.PUBLISHEDAT, articlesModel.publishedAt);

            db.insert(TABLE_ARTICLES, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dbDelete() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            //----Delete full table--->
            db.delete(TABLE_ARTICLES, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ArticlesModel> getAllData() {

        ArrayList<ArticlesModel> data = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT  * FROM " + TABLE_ARTICLES;

            Cursor cursor = null;

            cursor = db.rawQuery(selectQuery, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.moveToFirst()) {
                    do {

                        String strAuthor = cursor.getString(cursor.getColumnIndex(Articles.AUTHOR));
                        String strTitle = cursor.getString(cursor.getColumnIndex(Articles.TITLE));
                        String strDescription = cursor.getString(cursor.getColumnIndex(Articles.DESCRIPTION));
                        String strUrlToImage = cursor.getString(cursor.getColumnIndex(Articles.URLTOIMAGE));
                        String strPublishedAt = cursor.getString(cursor.getColumnIndex(Articles.PUBLISHEDAT));

                        ArticlesModel articlesModel = new ArticlesModel();

                        articlesModel.author = strAuthor;
                        articlesModel.title = strTitle;
                        articlesModel.description = strDescription;
                        articlesModel.urlToImage = strUrlToImage;
                        articlesModel.publishedAt = strPublishedAt;

                        data.add(articlesModel);

                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
            db.close(); // Closing database connection
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}