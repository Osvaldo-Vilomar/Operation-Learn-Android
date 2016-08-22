package com.vilomar.ozzie.udacityprojectportfolio.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Ozzie on 8/3/16.
 */

public class MovieProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDBHelper mOpenHelper;

    static final int MOVIE_FAVORITE = 100;
    static final int MOVIE_MOST_POPULAR = 101;
    static final int MOVIE_TOP_RATED = 102;
    static final int MOVIE_MOST_POPULAR_DETAIL = 103;
    static final int MOVIE_TOP_RATED_DETAIL = 104;
    static final int MOVIE_FAVORITE_DETAIL = 105;

    static UriMatcher buildUriMatcher() {

        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_FAVORITES, MOVIE_FAVORITE);
        matcher.addURI(authority, MovieContract.PATH_FAVORITES + "/*", MOVIE_FAVORITE_DETAIL);
        matcher.addURI(authority, MovieContract.PATH_MOST_POPULAR, MOVIE_MOST_POPULAR);
        matcher.addURI(authority, MovieContract.PATH_MOST_POPULAR + "/*", MOVIE_MOST_POPULAR_DETAIL);
        matcher.addURI(authority, MovieContract.PATH_TOP_RATED, MOVIE_TOP_RATED);
        matcher.addURI(authority, MovieContract.PATH_TOP_RATED + "/*", MOVIE_TOP_RATED_DETAIL);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE_FAVORITE:
                return MovieContract.FavoritesEntry.CONTENT_TYPE;
            case MOVIE_MOST_POPULAR:
                return MovieContract.MostPopularEntry.CONTENT_TYPE;
            case MOVIE_TOP_RATED:
                return MovieContract.TopRatedEntry.CONTENT_TYPE;
            case MOVIE_MOST_POPULAR_DETAIL:
                return MovieContract.MostPopularEntry.CONTENT_ITEM_TYPE;
            case MOVIE_TOP_RATED_DETAIL:
                return MovieContract.TopRatedEntry.CONTENT_ITEM_TYPE;
            case MOVIE_FAVORITE_DETAIL:
                return MovieContract.FavoritesEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    /*
        Student: Add the ability to insert Locations to the implementation of this function.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE_FAVORITE: {
                long _id = db.insert(MovieContract.FavoritesEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.FavoritesEntry.buildMovieFavoritesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case MOVIE_MOST_POPULAR: {
                long _id = db.insert(MovieContract.MostPopularEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.MostPopularEntry.buildMovieMostPopularUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case MOVIE_TOP_RATED: {
                long _id = db.insert(MovieContract.TopRatedEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.TopRatedEntry.buildMovieTopRatedUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE_FAVORITE:
                db.beginTransaction();
                int returnCount1 = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.FavoritesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount1++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount1;
            case MOVIE_MOST_POPULAR:
                db.beginTransaction();
                int returnCount2 = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.FavoritesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount2++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount2;
            case MOVIE_TOP_RATED:
                db.beginTransaction();
                int returnCount3 = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.FavoritesEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount3++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount3;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case MOVIE_FAVORITE:
                rowsDeleted = db.delete(
                        MovieContract.FavoritesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_MOST_POPULAR:
                rowsDeleted = db.delete(
                        MovieContract.MostPopularEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_TOP_RATED:
                rowsDeleted = db.delete(
                        MovieContract.TopRatedEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIE_FAVORITE:
                rowsUpdated = db.update(MovieContract.FavoritesEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case MOVIE_MOST_POPULAR:
                rowsUpdated = db.update(MovieContract.MostPopularEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case MOVIE_TOP_RATED:
                rowsUpdated = db.update(MovieContract.TopRatedEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    private static final String[] MOVIE_COLUMNS = {

            MovieContract.MostPopularEntry._ID,
            MovieContract.MostPopularEntry.COLUMN_MOVIE_ID,
            MovieContract.MostPopularEntry.COLUMN_TITLE,
            MovieContract.MostPopularEntry.COLUMN_IMAGE_PATH,
            MovieContract.MostPopularEntry.COLUMN_RELEASE_DATE,
            MovieContract.MostPopularEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.MostPopularEntry.COLUMN_PLOT_SYNOPSIS
    };

    private String sMovieIDSelectionMostPopular =
            MovieContract.MostPopularEntry.TABLE_NAME +
                    "." + MovieContract.MostPopularEntry.COLUMN_MOVIE_ID + " = ? ";

    private String sMovieIDSelectionTopRated =
            MovieContract.TopRatedEntry.TABLE_NAME +
                    "." + MovieContract.TopRatedEntry.COLUMN_MOVIE_ID + " = ? ";

    private String sMovieIDSelectionFavorite =
            MovieContract.FavoritesEntry.TABLE_NAME +
                    "." + MovieContract.FavoritesEntry.COLUMN_MOVIE_ID + " = ? ";

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {

            case MOVIE_FAVORITE:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.FavoritesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MOVIE_MOST_POPULAR:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MostPopularEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MOVIE_TOP_RATED:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TopRatedEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MOVIE_MOST_POPULAR_DETAIL:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MostPopularEntry.TABLE_NAME,
                        MOVIE_COLUMNS,
                        sMovieIDSelectionMostPopular,
                        new String[]{MovieContract.MostPopularEntry.getMovieMostPopularIDFromURI(uri)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case MOVIE_TOP_RATED_DETAIL:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TopRatedEntry.TABLE_NAME,
                        MOVIE_COLUMNS,
                        sMovieIDSelectionTopRated,
                        new String[]{MovieContract.TopRatedEntry.getMovieTopRatedIDFromURI(uri)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case MOVIE_FAVORITE_DETAIL:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.FavoritesEntry.TABLE_NAME,
                        MOVIE_COLUMNS,
                        sMovieIDSelectionFavorite,
                        new String[]{MovieContract.FavoritesEntry.getMovieFavoritesIDFromURI(uri)},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}

