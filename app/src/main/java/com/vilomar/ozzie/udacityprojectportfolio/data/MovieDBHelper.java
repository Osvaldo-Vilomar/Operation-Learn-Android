package com.vilomar.ozzie.udacityprojectportfolio.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vilomar.ozzie.udacityprojectportfolio.data.MovieContract.FavoritesEntry;
import com.vilomar.ozzie.udacityprojectportfolio.data.MovieContract.MostPopularEntry;
import com.vilomar.ozzie.udacityprojectportfolio.data.MovieContract.TopRatedEntry;

/**
 * Created by Ozzie on 8/3/16.
 */


public class MovieDBHelper extends SQLiteOpenHelper {

// If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movies.db";

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " + FavoritesEntry.TABLE_NAME + " (" +
                FavoritesEntry._ID + " INTEGER PRIMARY KEY," +
                FavoritesEntry.COLUMN_MOVIE_ID + " TEXT UNIQUE NOT NULL, " +
                FavoritesEntry.COLUMN_TITLE + " TEXT UNIQUE NOT NULL, " +
                FavoritesEntry.COLUMN_IMAGE_PATH + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_PLOT_SYNOPSIS + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_TRAILER_LINK + " TEXT, " +
                FavoritesEntry.COLUMN_TRAILER_NAME + " TEXT, " +
                FavoritesEntry.COLUMN_REVIEW_AUTHOR + " TEXT, " +
                FavoritesEntry.COLUMN_REVIEW_TEXT + " TEXT);";

        final String SQL_CREATE_MOST_POPULAR_TABLE = "CREATE TABLE " + MostPopularEntry.TABLE_NAME + " (" +
                MostPopularEntry._ID + " INTEGER PRIMARY KEY," +
                MostPopularEntry.COLUMN_MOVIE_ID + " TEXT UNIQUE NOT NULL, " +
                MostPopularEntry.COLUMN_TITLE + " TEXT UNIQUE NOT NULL, " +
                MostPopularEntry.COLUMN_IMAGE_PATH + " TEXT NOT NULL, " +
                MostPopularEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MostPopularEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                MostPopularEntry.COLUMN_PLOT_SYNOPSIS + " TEXT NOT NULL, " +
                MostPopularEntry.COLUMN_TRAILER_LINK + " TEXT, " +
                MostPopularEntry.COLUMN_TRAILER_NAME + " TEXT, " +
                MostPopularEntry.COLUMN_REVIEW_AUTHOR + " TEXT, " +
                MostPopularEntry.COLUMN_REVIEW_TEXT + " TEXT);";

        final String SQL_CREATE_TOP_RATED_TABLE = "CREATE TABLE " + TopRatedEntry.TABLE_NAME + " (" +
                TopRatedEntry._ID + " INTEGER PRIMARY KEY," +
                TopRatedEntry.COLUMN_MOVIE_ID + " TEXT UNIQUE NOT NULL, " +
                TopRatedEntry.COLUMN_TITLE + " TEXT UNIQUE NOT NULL, " +
                TopRatedEntry.COLUMN_IMAGE_PATH + " TEXT NOT NULL, " +
                TopRatedEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                TopRatedEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                TopRatedEntry.COLUMN_PLOT_SYNOPSIS + " TEXT NOT NULL, " +
                TopRatedEntry.COLUMN_TRAILER_LINK + " TEXT, " +
                TopRatedEntry.COLUMN_TRAILER_NAME + " TEXT, " +
                TopRatedEntry.COLUMN_REVIEW_AUTHOR + " TEXT, " +
                TopRatedEntry.COLUMN_REVIEW_TEXT + " TEXT);";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MOST_POPULAR_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TOP_RATED_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoritesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MostPopularEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TopRatedEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}

