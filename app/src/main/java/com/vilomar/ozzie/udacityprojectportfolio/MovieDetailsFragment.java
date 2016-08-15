package com.vilomar.ozzie.udacityprojectportfolio;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Movie;
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

import com.squareup.picasso.Picasso;
import com.vilomar.ozzie.udacityprojectportfolio.data.MovieContract;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = MovieDetailsFragment.class.getSimpleName();
    private MovieAdapter posterAdapter;
    private ArrayAdapter<String> trailerAdapter;
    ListView movieDetailList;
    ListView movieTrailerList;
    TextView movieReviews;

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

        posterAdapter = new MovieAdapter(getActivity(), null, 0);
        posterAdapter.setViewType("plot_synopsis");

        trailerAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_trailer,
                R.id.list_movie_trailer,
                new ArrayList<String>()
        );

        rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        movieDetailList = (ListView) rootView.findViewById(R.id.movieDetailsList);
        movieDetailList.setAdapter(posterAdapter);

        movieTrailerList = (ListView) rootView.findViewById(R.id.movieTrailerList);
        movieTrailerList.setAdapter(trailerAdapter);

        movieReviews = (TextView) rootView.findViewById(R.id.read_reviews);

        movieReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MovieReviews.class);
                startActivity(intent);
            }
        });

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

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        final String[] trailerKeys;

        if(data != null && data.moveToFirst()) {

            ((TextView) getView().findViewById(R.id.detail_movie_title)).
                    setText(data.getString(COL_TITLE));

            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w500/" + data.getString(COL_IMAGE_PATH)).into((ImageView) getView()
                    .findViewById(R.id.detail_movie_poster));

            ((TextView) getView().findViewById(R.id.detail_movie_release_date))
                    .setText(data.getString(COL_RELEASE_DATE));

            ((TextView) getView().findViewById(R.id.detail_movie_rating))
                    .setText(data.getString(COL_VOTE_AVERAGE) + " / 10");

            final FetchMovieTask movieTask = new FetchMovieTask(getActivity(), trailerAdapter);
            movieTask.execute("trailer", data.getString(COL_MOVIE_ID));

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

            posterAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}
