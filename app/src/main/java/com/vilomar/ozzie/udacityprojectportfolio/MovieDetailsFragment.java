package com.vilomar.ozzie.udacityprojectportfolio;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vilomar.ozzie.udacityprojectportfolio.data.MovieContract;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = MovieDetailsFragment.class.getSimpleName();
    private MovieAdapter plotAdapter;
    private ArrayAdapter<String> trailerAdapter;
    ListView movieDetailList;
    ListView movieTrailerList;
    TextView movieReviews;
    TextView movieFavorite;

    private static final int DETAIL_LOADER = 0;
    View rootView;

    private static final String[] MOVIE_COLUMNS = {

            MovieContract.MostPopularEntry._ID,
            MovieContract.MostPopularEntry.COLUMN_MOVIE_ID,
            MovieContract.MostPopularEntry.COLUMN_TITLE,
            MovieContract.MostPopularEntry.COLUMN_IMAGE_PATH,
            MovieContract.MostPopularEntry.COLUMN_RELEASE_DATE,
            MovieContract.MostPopularEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.MostPopularEntry.COLUMN_PLOT_SYNOPSIS
    };

    static final int COL_MOVIE_ID = 1;
    static final int COL_TITLE = 2;
    static final int COL_IMAGE_PATH = 3;
    static final int COL_RELEASE_DATE = 4;
    static final int COL_VOTE_AVERAGE = 5;
    static final int COL_PLOT_SYNOPSIS = 6;

    public MovieDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Window window = getActivity().getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(getActivity().getResources().getColor(R.color.colorBlack));

        plotAdapter = new MovieAdapter(getActivity(), null, 0);
        plotAdapter.setViewType("plot_synopsis");

        trailerAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_trailer,
                R.id.list_movie_trailer,
                new ArrayList<String>()
        );

        rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        movieDetailList = (ListView) rootView.findViewById(R.id.movieDetailsList);
        movieDetailList.setAdapter(plotAdapter);

        movieTrailerList = (ListView) rootView.findViewById(R.id.movieTrailerList);
        movieTrailerList.setAdapter(trailerAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "In onCreateLoader");
        Intent intent = getActivity().getIntent();
        Log.v(LOG_TAG, "Intent: " + intent);
        Log.v(LOG_TAG, "Intent Get Data: " + intent.getData());
        if (intent == null) {
            return null;
        }

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                intent.getData(),
                MOVIE_COLUMNS,
                null,
                null,
                null
        );
    }

    String[] trailerKeys;
    String movieId;
    String movieTitle;
    String imagePath;
    String releaseDate;
    String movieRating;
    String plotSynopsis;
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(data != null && data.moveToFirst()) {

            movieId = data.getString(COL_MOVIE_ID);
            movieTitle = data.getString(COL_TITLE);
            imagePath = data.getString(COL_IMAGE_PATH);
            releaseDate = data.getString(COL_RELEASE_DATE);
            movieRating = data.getString(COL_VOTE_AVERAGE);
            plotSynopsis = data.getString(COL_PLOT_SYNOPSIS);

            ((TextView) getView().findViewById(R.id.detail_movie_title)).
                    setText(movieTitle);

            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w500/" + imagePath).into((ImageView) getView()
                    .findViewById(R.id.detail_movie_poster));

            ((TextView) getView().findViewById(R.id.detail_movie_release_date))
                    .setText(releaseDate);

            ((TextView) getView().findViewById(R.id.detail_movie_rating))
                    .setText(movieRating + " / 10");

            FetchMovieTask movieTask = new FetchMovieTask(getActivity(), trailerAdapter);
            movieTask.execute("trailer", movieId);

            trailerKeys = movieTask.getTrailerKeys();

            movieTrailerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    Uri webpage = Uri.parse("https://www.youtube.com/watch?v=" + trailerKeys[position]);
                    Log.v(LOG_TAG, "WEB PAGE: " + webpage);
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                    startActivity(webIntent);
                }
            });

            movieReviews = (TextView) rootView.findViewById(R.id.read_reviews);
            movieReviews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.v(LOG_TAG, movieTitle);
                    Intent intent = new Intent(getActivity(), MovieReviews.class).
                            putExtra(Intent.EXTRA_TEXT, movieTitle).
                            putExtra("movieId", movieId);
                    startActivity(intent);
                }
            });

            movieFavorite = (TextView) rootView.findViewById(R.id.movie_favorite);
            movieFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FetchMovieTask movieTask = new FetchMovieTask(getActivity());
                    movieTask.setFavoriteMovieVariables(movieId, movieTitle, imagePath, releaseDate, movieRating, plotSynopsis);
                    movieTask.execute("favorites");

                    Context context = getActivity();
                    CharSequence text = "Movie added to Favorites";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            });

            plotAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}
