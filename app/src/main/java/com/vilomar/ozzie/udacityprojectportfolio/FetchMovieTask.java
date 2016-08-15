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
import java.util.Vector;

/**
 * Created by Ozzie on 8/14/16.
 */
public class FetchMovieTask extends AsyncTask<String, Void, String[]> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

    private Context mContext;
    private ArrayAdapter<String> trailerAdapter;
    private String[] trailerKeys = new String[5];


    public FetchMovieTask(Context context) {
        mContext = context;
    }

    public FetchMovieTask(Context context, ArrayAdapter<String> trailerAdapter) {
        mContext = context;
        this.trailerAdapter = trailerAdapter;
    }

    private String[] getMovieDataFromJson(String movieJsonStr, String apiCallParam) throws JSONException {

        String[] moviePosterLinks = new String[20];

        //Maximum of five trailer links
        String[] trailerNames = new String[5];

        Uri contractEntryColumnUri = null;

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

                String movieID = null;
                String title = null;
                String imagePath = null;
                String releaseDate = null;
                String voteAverage = null;
                String plotSynopsis = null;

                //Extract this when the apiCallParam is trailer
                String movieTrailer = null;
                String movieTrailerName = null;

                //Extract this when the apiCallParam is review
                String movieReviewAuthor = null;
                String movieReviewItself = null;

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
//                        movieValues.put(MovieContract.MostPopularEntry.COLUMN_TRAILER_LINK, "");
//                        movieValues.put(MovieContract.MostPopularEntry.COLUMN_TRAILER_NAME, "");
//                        movieValues.put(MovieContract.MostPopularEntry.COLUMN_REVIEW_AUTHOR, "");
//                        movieValues.put(MovieContract.MostPopularEntry.COLUMN_REVIEW_TEXT, "");

                        // Finally, insert location data into the database.
                        Uri insertedUri = mContext.getContentResolver().insert(
                                MovieContract.MostPopularEntry.CONTENT_URI,
                                movieValues
                        );

                        // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
                        movieMostPopularRowId = ContentUris.parseId(insertedUri);

                        moviePosterLinks[i] = "image=[" + imagePath + "]";
                    }

                    movieCursor.close();

                    Log.v(LOG_TAG, "Image Path: " + imagePath);
                    Log.v(LOG_TAG, "Most Popular Row Id just inserted: " + movieMostPopularRowId);

                } else if(apiCallParam.equals("top_rated")) {

                    movieID = movieDetail.getString(OWM_MOVIE_ID);
                    title = movieDetail.getString(OWM_Title);
                    imagePath = movieDetail.getString(OWM_Poster);
                    releaseDate = movieDetail.getString(OWM_RELEASE_DATE);
                    voteAverage = movieDetail.getString(OWM_VOTE_AVERAGE);
                    plotSynopsis = movieDetail.getString(OWM_PLOT_SYNOPSIS);

                    movieValues.put(MovieContract.TopRatedEntry.COLUMN_MOVIE_ID, movieID);
                    movieValues.put(MovieContract.TopRatedEntry.COLUMN_TITLE, title);
                    movieValues.put(MovieContract.TopRatedEntry.COLUMN_IMAGE_PATH, imagePath);
                    movieValues.put(MovieContract.TopRatedEntry.COLUMN_RELEASE_DATE, releaseDate);
                    movieValues.put(MovieContract.TopRatedEntry.COLUMN_VOTE_AVERAGE, voteAverage);
                    movieValues.put(MovieContract.TopRatedEntry.COLUMN_PLOT_SYNOPSIS, plotSynopsis);

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
                } else if(apiCallParam.equals("review")) {

                    movieReviewAuthor = movieDetail.getString(OWM_MOVIE_REVIEW_AUTHOR);
                    movieReviewItself = movieDetail.getString(OWM_MOVIE_REVIEW_ITSELF);

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
            } else if (params[0].equals("favorites")) {
                //Here, what do we do? We return nothing, because we will not be making the api call.
            } else if (params[0].equals("trailer")) {
                movieBaseUrl = "http://api.themoviedb.org/3/movie/" + params[1] + "/videos?api_key=" +
                        BuildConfig.OPEN_WEATHER_MAP_API_KEY;
                appIdParam = "APPID";
            } else if (params[0].equals("review")) {
                movieBaseUrl = "http://api.themoviedb.org/3/movie/209112/reviews?api_key=" +
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
        if (result != null && trailerAdapter != null) {
            trailerAdapter.clear();
            for(String trailerStr : result) {
                if(trailerStr != null && trailerStr != "") {
                    trailerAdapter.add(trailerStr);
                }
            }
        }
    }

    public String[] getTrailerKeys() {
        return trailerKeys;
    }
}
