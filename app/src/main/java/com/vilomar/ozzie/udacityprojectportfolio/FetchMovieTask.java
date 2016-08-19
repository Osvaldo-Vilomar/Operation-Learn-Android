package com.vilomar.ozzie.udacityprojectportfolio;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.vilomar.ozzie.udacityprojectportfolio.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Ozzie on 8/14/16.
 */
public class FetchMovieTask extends AsyncTask<String, Void, String[]> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

    private Context mContext;
    private ArrayAdapter<String> movieAdapter;
    private ArrayAdapter<ReviewContent> reviewAdapter;
    private String[] trailerKeys = new String[5];
    private ArrayList<ReviewContent> reviewContentArrayList = new ArrayList<>();

    public FetchMovieTask(Context context) {
        mContext = context;
    }

    public FetchMovieTask(Context context, ArrayAdapter<String> movieAdapter) {
        mContext = context;
        this.movieAdapter = movieAdapter;
    }

    public FetchMovieTask(Context context, ArrayAdapter<ReviewContent> reviewAdapter, String overrideToThis) {
        mContext = context;
        this.reviewAdapter = reviewAdapter;
    }

    private String[] getMovieDataFromJson(String movieJsonStr, String apiCallParam) throws JSONException {

        String[] moviePosterLinks = new String[20];

        //Maximum of five trailer links
        String[] trailerNames = new String[5];

        if(apiCallParam.equals("favorites")){

            //delete rows inserted into db for debugging
            //int rowsDeleted = mContext.getContentResolver().delete(MovieContract.FavoritesEntry.CONTENT_URI, null, null);
            addToFavoriteDatabase();
            return null;
        }

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_Result = "results";
        final String OWM_MOVIE_ID = "id";
        final String OWM_Title = "title";
        final String OWM_Poster = "poster_path";
        final String OWM_RELEASE_DATE = "release_date";
        final String OWM_VOTE_AVERAGE = "vote_average";
        final String OWM_PLOT_SYNOPSIS = "overview";
        final String OWM_MOVIE_TRAILER = "key";
        final String OWM_MOVIE_TRAILER_NAME = "name";
        final String OWM_MOVIE_REVIEW_AUTHOR = "author";
        final String OWM_MOVIE_REVIEW_ITSELF = "content";

        try {

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(OWM_Result);

            Vector<ContentValues> cVVector = new Vector<ContentValues>(movieArray.length());
            for (int i = 0; i < movieArray.length(); i++) {

                JSONObject movieDetail = movieArray.getJSONObject(i);

                String movieID;
                String title;
                String imagePath;
                String releaseDate;
                String voteAverage;
                String plotSynopsis;

                //Extract this when the apiCallParam is trailer
                String movieTrailer;
                String movieTrailerName;

                //Extract this when the apiCallParam is review
                String movieReviewAuthor;
                String movieReviewItself;

                ContentValues movieValues = new ContentValues();

                if(apiCallParam.equals("most_popular")) {

                    movieID = movieDetail.getString(OWM_MOVIE_ID);
                    title = movieDetail.getString(OWM_Title);
                    imagePath = movieDetail.getString(OWM_Poster);
                    releaseDate = movieDetail.getString(OWM_RELEASE_DATE);
                    voteAverage = movieDetail.getString(OWM_VOTE_AVERAGE);
                    plotSynopsis = movieDetail.getString(OWM_PLOT_SYNOPSIS);

                    Cursor movieCursor = mContext.getContentResolver().query(
                            MovieContract.MostPopularEntry.CONTENT_URI,
                            new String[]{MovieContract.MostPopularEntry._ID},
                            MovieContract.MostPopularEntry.COLUMN_MOVIE_ID + " = ?",
                            new String[]{movieID},
                            null);

                    long movieMostPopularRowId;

                    if (movieCursor.moveToFirst()) {
                        int movieIdIndex = movieCursor.getColumnIndex(MovieContract.MostPopularEntry._ID);
                        movieMostPopularRowId = movieCursor.getLong(movieIdIndex);
                    } else {

                        movieValues.put(MovieContract.MostPopularEntry.COLUMN_MOVIE_ID, movieID);
                        movieValues.put(MovieContract.MostPopularEntry.COLUMN_TITLE, title);
                        movieValues.put(MovieContract.MostPopularEntry.COLUMN_IMAGE_PATH, imagePath);
                        movieValues.put(MovieContract.MostPopularEntry.COLUMN_RELEASE_DATE, releaseDate);
                        movieValues.put(MovieContract.MostPopularEntry.COLUMN_VOTE_AVERAGE, voteAverage);
                        movieValues.put(MovieContract.MostPopularEntry.COLUMN_PLOT_SYNOPSIS, plotSynopsis);

                        Uri insertedUri = mContext.getContentResolver().insert(
                                MovieContract.MostPopularEntry.CONTENT_URI,
                                movieValues
                        );

                        // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
                        movieMostPopularRowId = ContentUris.parseId(insertedUri);

                        moviePosterLinks[i] = "image=[" + imagePath + "]";
                    }

                    movieCursor.close();

                    Log.v(LOG_TAG, "Most Popular Row Id just inserted: " + movieMostPopularRowId);

                } else if(apiCallParam.equals("top_rated")) {

                    movieID = movieDetail.getString(OWM_MOVIE_ID);
                    title = movieDetail.getString(OWM_Title);
                    imagePath = movieDetail.getString(OWM_Poster);
                    releaseDate = movieDetail.getString(OWM_RELEASE_DATE);
                    voteAverage = movieDetail.getString(OWM_VOTE_AVERAGE);
                    plotSynopsis = movieDetail.getString(OWM_PLOT_SYNOPSIS);

                    Cursor movieCursor = mContext.getContentResolver().query(
                            MovieContract.TopRatedEntry.CONTENT_URI,
                            new String[]{MovieContract.TopRatedEntry._ID},
                            MovieContract.TopRatedEntry.COLUMN_MOVIE_ID + " = ?",
                            new String[]{movieID},
                            null);

                    long movieTopRatedRowId;

                    if (movieCursor.moveToFirst()) {
                        int movieIdIndex = movieCursor.getColumnIndex(MovieContract.TopRatedEntry._ID);
                        movieTopRatedRowId = movieCursor.getLong(movieIdIndex);
                    } else {

                        movieValues.put(MovieContract.TopRatedEntry.COLUMN_MOVIE_ID, movieID);
                        movieValues.put(MovieContract.TopRatedEntry.COLUMN_TITLE, title);
                        movieValues.put(MovieContract.TopRatedEntry.COLUMN_IMAGE_PATH, imagePath);
                        movieValues.put(MovieContract.TopRatedEntry.COLUMN_RELEASE_DATE, releaseDate);
                        movieValues.put(MovieContract.TopRatedEntry.COLUMN_VOTE_AVERAGE, voteAverage);
                        movieValues.put(MovieContract.TopRatedEntry.COLUMN_PLOT_SYNOPSIS, plotSynopsis);

                        Uri insertedUri = mContext.getContentResolver().insert(
                                MovieContract.TopRatedEntry.CONTENT_URI,
                                movieValues
                        );

                        movieTopRatedRowId = ContentUris.parseId(insertedUri);

                        moviePosterLinks[i] = "image=[" + imagePath + "]";
                    }

                    movieCursor.close();

                    Log.v(LOG_TAG, "Most Popular Row Id just inserted: " + movieTopRatedRowId);

                } else if(apiCallParam.equals("trailer")) {

                    if(i < 5) {

                        movieTrailer = movieDetail.getString(OWM_MOVIE_TRAILER);
                        movieTrailerName = movieDetail.getString(OWM_MOVIE_TRAILER_NAME);

                        Log.v(LOG_TAG, "Movie Trailer: " + movieTrailer);

                        movieValues.put(MovieContract.MostPopularEntry.COLUMN_TRAILER_LINK, movieTrailer);
                        movieValues.put(MovieContract.MostPopularEntry.COLUMN_TRAILER_NAME, movieTrailerName);


                        trailerKeys[i] = movieTrailer;
                        trailerNames[i] = movieTrailerName;
                    }
                } if(apiCallParam.equals("review")) {

                    movieReviewAuthor = movieDetail.getString(OWM_MOVIE_REVIEW_AUTHOR);
                    movieReviewItself = movieDetail.getString(OWM_MOVIE_REVIEW_ITSELF);

                    if(reviewAdapter != null) {
                        reviewContentArrayList.add(new ReviewContent(movieReviewItself, movieReviewAuthor));
                    }

                    movieValues.put(MovieContract.MostPopularEntry.COLUMN_REVIEW_AUTHOR, movieReviewAuthor);
                    movieValues.put(MovieContract.MostPopularEntry.COLUMN_REVIEW_TEXT, movieReviewItself);

                } else {
                    //Nothing to do?
                }

                cVVector.add(movieValues);

            }

            int inserted = 0;
            if (cVVector.size() > 0) {
                // Student: call bulkInsert to add the movieEntries to the database here
            }

            Log.d(LOG_TAG, "FetchMovieTask Complete. " + inserted + " Inserted");

            if(apiCallParam.equals("trailer")) {
                return trailerNames;
            }
            else if(apiCallParam.equals("review")) {
                return new String[] { "review" } ;
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected String[] doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        String apiCallParam = params[0];

        if(params[0].equals("favorites")) {

            Log.v(LOG_TAG, "Plot Synopsis: " + plotSynopsis);

            try {
                return getMovieDataFromJson(null, apiCallParam);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        Log.v(LOG_TAG, "Order of movies: " + params[0]);

        try {
            String movieBaseUrl = null;
            String appIdParam = null;

            if(params[0].equals("most_popular")) {
                movieBaseUrl = "http://api.themoviedb.org/3/movie/popular?api_key=" +
                        BuildConfig.OPEN_WEATHER_MAP_API_KEY;
                appIdParam = "APPID";
            } else if (params[0].equals("top_rated")) {
                movieBaseUrl = "http://api.themoviedb.org/3/movie/top_rated?api_key=" +
                        BuildConfig.OPEN_WEATHER_MAP_API_KEY;
                appIdParam = "APPID";
            } else if (params[0].equals("trailer")) {
                movieBaseUrl = "http://api.themoviedb.org/3/movie/" + params[1] + "/videos?api_key=" +
                        BuildConfig.OPEN_WEATHER_MAP_API_KEY;
                appIdParam = "APPID";
            } else if (params[0].equals("review")) {
                movieBaseUrl = "http://api.themoviedb.org/3/movie/" + params[1] + "/reviews?api_key=" +
                        BuildConfig.OPEN_WEATHER_MAP_API_KEY;
                appIdParam = "APPID";
            } else {
                movieBaseUrl = "http://api.themoviedb.org/3/movie/popular?api_key=" +
                        BuildConfig.OPEN_WEATHER_MAP_API_KEY;
                appIdParam = "APPID";
            }

            Uri builtUri = Uri.parse(movieBaseUrl).buildUpon()
                    .appendQueryParameter(appIdParam, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            movieJsonStr = buffer.toString();

            Log.v(LOG_TAG, "API Call Param: " + apiCallParam);
            Log.v(LOG_TAG, "Movie JSON String: " + movieJsonStr);

        } catch(IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getMovieDataFromJson(movieJsonStr, apiCallParam);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String[] result) {
        if (result != null && movieAdapter != null) {
            movieAdapter.clear();
            for(String trailerStr : result) {
                if(trailerStr != null && trailerStr != "") {
                    movieAdapter.add(trailerStr);
                }
            }
        }
        if(result != null && result[0].equals("review")) {
            Log.v(LOG_TAG, "Review finished");
            for(int i = 0; i < reviewContentArrayList.size(); i++) {
                reviewAdapter.add(reviewContentArrayList.get(i));
            }
        }
    }

    public String[] getTrailerKeys() {
        return trailerKeys;
    }

    String movieID;
    String movieTitle;
    String imagePath;
    String releaseDate;
    String movieRating;
    String plotSynopsis;
    public void setFavoriteMovieVariables(String movieID, String movieTitle, String imagePath, String releaseDate, String movieRating, String plotSynopsis) {
        this.movieID = movieID;
        this.movieTitle = movieTitle;
        this.imagePath = imagePath;
        this.releaseDate = releaseDate;
        this.movieRating = movieRating;
        this.plotSynopsis = plotSynopsis;
    }

    void addToFavoriteDatabase() {

        Vector<ContentValues> cVVector = new Vector(5);
        ContentValues movieValues = new ContentValues();

        movieValues.put(MovieContract.FavoritesEntry.COLUMN_MOVIE_ID, movieID);
        movieValues.put(MovieContract.FavoritesEntry.COLUMN_TITLE, movieTitle);
        movieValues.put(MovieContract.FavoritesEntry.COLUMN_IMAGE_PATH, imagePath);
        movieValues.put(MovieContract.FavoritesEntry.COLUMN_RELEASE_DATE, releaseDate);
        movieValues.put(MovieContract.FavoritesEntry.COLUMN_VOTE_AVERAGE, movieRating);
        movieValues.put(MovieContract.FavoritesEntry.COLUMN_PLOT_SYNOPSIS, plotSynopsis);

        cVVector.add(movieValues);

        int inserted = 0;
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            inserted = mContext.getContentResolver().bulkInsert(MovieContract.FavoritesEntry.CONTENT_URI, cvArray);
        }
        Log.v(LOG_TAG, "Favorites Row Id just inserted: " + inserted);
    }
}
