package com.vilomar.ozzie.udacityprojectportfolio.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Ozzie on 8/2/16.
 */
public class MovieContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.vilomar.ozzie.udacityprojectportfolio";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_FAVORITES = "favorites";
    public static final String PATH_MOST_POPULAR = "most_popular";
    public static final String PATH_TOP_RATED = "top_rated";

    public static String queryMovie(String movie) {
        return movie;
    }

    public static final class FavoritesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;

        public static final String TABLE_NAME = "tbFavorites";

        public static final String COLUMN_MOVIE_ID= "movieID";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IMAGE_PATH = "image_path";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_PLOT_SYNOPSIS = "plot_synopsis";
        public static final String COLUMN_TRAILER_LINK = "trailer_link";
        public static final String COLUMN_TRAILER_NAME = "trailer_name";
        public static final String COLUMN_REVIEW_AUTHOR = "review_author";
        public static final String COLUMN_REVIEW_TEXT = "review_text";

        public static Uri buildMovieFavoritesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMovieFavorites() {
            return CONTENT_URI.buildUpon().build();
        }

        public static Uri buildMovieFavoritesWithOrder(String orderOfMovies, String movie) {
            return CONTENT_URI.buildUpon().appendPath(orderOfMovies).
                    appendQueryParameter(COLUMN_IMAGE_PATH, queryMovie(movie) ).build();
        }

        public static String getMovieFavoritesIDFromURI(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static final class MostPopularEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOST_POPULAR).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOST_POPULAR;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOST_POPULAR;

        public static final String TABLE_NAME = "tbMost_Popular";

        public static final String COLUMN_MOVIE_ID= "movieID";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IMAGE_PATH = "image_path";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_PLOT_SYNOPSIS = "plot_synopsis";
        public static final String COLUMN_TRAILER_LINK = "trailer_link";
        public static final String COLUMN_TRAILER_NAME = "trailer_name";
        public static final String COLUMN_REVIEW_AUTHOR = "review_author";
        public static final String COLUMN_REVIEW_TEXT = "review_text";

        public static Uri buildMovieMostPopularUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMovieMostPopular() {
            return CONTENT_URI.buildUpon().build();
        }

        public static Uri buildMovieMostPopularDetail(String movieID) {
            return CONTENT_URI.buildUpon().appendPath(movieID).build();
        }

        public static String getMovieMostPopularIDFromURI(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static final class TopRatedEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TOP_RATED).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TOP_RATED;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TOP_RATED;

        public static final String TABLE_NAME = "tbTop_Rated";

        public static final String COLUMN_MOVIE_ID= "movieID";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IMAGE_PATH = "image_path";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_PLOT_SYNOPSIS = "plot_synopsis";
        public static final String COLUMN_TRAILER_LINK = "trailer_link";
        public static final String COLUMN_TRAILER_NAME = "trailer_name";
        public static final String COLUMN_REVIEW_AUTHOR = "review_author";
        public static final String COLUMN_REVIEW_TEXT = "review_text";

        public static Uri buildMovieTopRatedUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMovieTopRated() {
            return CONTENT_URI.buildUpon().build();
        }

        public static Uri buildMovieTopRatedDetail(String movieID) {
            return CONTENT_URI.buildUpon().appendPath(movieID).build();
        }

        public static String getMovieTopRatedIDFromURI(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
