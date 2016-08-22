package com.vilomar.ozzie.udacityprojectportfolio;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import com.vilomar.ozzie.udacityprojectportfolio.data.MovieContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = MoviesFragment.class.getSimpleName();

    private MovieAdapter moviesAdapter;
    private static final int MOVIES_LOADER = 0;

    private static final String[] MOST_POPULAR_COLUMNS = {
            MovieContract.MostPopularEntry._ID,
            MovieContract.MostPopularEntry.COLUMN_IMAGE_PATH,
            MovieContract.MostPopularEntry.COLUMN_MOVIE_ID
    };
    private static final String[] TOP_RATED_COLUMNS = {
            MovieContract.TopRatedEntry._ID,
            MovieContract.TopRatedEntry.COLUMN_IMAGE_PATH,
            MovieContract.TopRatedEntry.COLUMN_MOVIE_ID
    };

    private static final String[] FAVORITES_COLUMNS = {
            MovieContract.FavoritesEntry._ID,
            MovieContract.FavoritesEntry.COLUMN_IMAGE_PATH,
            MovieContract.FavoritesEntry.COLUMN_MOVIE_ID
    };

    static final int COL_IMAGE_PATH = 1;
    static final int COL_MOVIE_ID = 2;

    /**
      * A callback interface that all activities containing this fragment must
      * implement. This mechanism allows activities to be notified of item
      * selections.
     */
   public interface Callback {
            /**
              * DetailFragmentCallback for when an item has been selected.
              */
            public void onItemSelected(Uri dateUri);
    }

    public MoviesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviesfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    String orderOfMovies;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        moviesAdapter = new MovieAdapter(getActivity(), null, 0);
        moviesAdapter.setViewType("movie_posters");

        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        GridView movieGrid = (GridView) rootView.findViewById(R.id.moviesGrid);
        movieGrid.setAdapter(moviesAdapter);

        movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.h
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);

                Log.v(LOG_TAG, "Cursor: " + cursor.getString(COL_IMAGE_PATH));
                if (cursor != null) {
                    orderOfMovies = Utility.getPreferredOrderOfMovies(getActivity());
                    if(orderOfMovies.equals("most_popular")) {
                        ((Callback) getActivity()).onItemSelected(MovieContract.MostPopularEntry
                                .buildMovieMostPopularDetail(cursor.getString(COL_MOVIE_ID)));
                        Log.v(LOG_TAG, "Send this to Movie Details: " + MovieContract.MostPopularEntry.
                                buildMovieMostPopularDetail(cursor.getString(COL_MOVIE_ID)));
                    } else if(orderOfMovies.equals("top_rated")) {
                        ((Callback) getActivity()).onItemSelected(MovieContract.TopRatedEntry
                                .buildMovieTopRatedDetail(cursor.getString(COL_MOVIE_ID)));
                        Log.v(LOG_TAG, "Send this to Movie Details: " + MovieContract.TopRatedEntry.
                                buildMovieTopRatedDetail(cursor.getString(COL_MOVIE_ID)));
                    } else if(orderOfMovies.equals("favorites")) {
                        ((Callback) getActivity()).onItemSelected(MovieContract.FavoritesEntry
                                .buildMovieFavorites());
                    }
                }
            }
        });
        Window window = getActivity().getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(getActivity().getResources().getColor(R.color.colorBlack));

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String movieOrder = Utility.getPreferredOrderOfMovies(getActivity());
        Log.v(LOG_TAG, "onCreateLoader, movie order: " + movieOrder);
        Uri movieUri;

        if(movieOrder.equals("most_popular")) {
            movieUri = MovieContract.MostPopularEntry.buildMovieMostPopular();
            return new CursorLoader(getActivity(),
                    movieUri,
                    MOST_POPULAR_COLUMNS,
                    null,
                    null,
                    null);
        } else if(movieOrder.equals("top_rated")) {
            movieUri = MovieContract.TopRatedEntry.buildMovieTopRated();
            return new CursorLoader(getActivity(),
                    movieUri,
                    TOP_RATED_COLUMNS,
                    null,
                    null,
                    null);
        } else if(movieOrder.equals("favorites")) {
            movieUri = MovieContract.FavoritesEntry.buildMovieFavorites();
            return new CursorLoader(getActivity(),
                    movieUri,
                    FAVORITES_COLUMNS,
                    null,
                    null,
                    null);
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        Log.v(LOG_TAG, "Cursor onLoadFinished: " + cursor);
        moviesAdapter.swapCursor(cursor);
    }

    void onOrderOfMoviesChanged( ) {
        updateMovies();
        getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
    }

    private void updateMovies() {
        FetchMovieTask movieTask = new FetchMovieTask(getActivity());
        String orderOfMovies = Utility.getPreferredOrderOfMovies(getActivity());
        Log.v(LOG_TAG, "Order of Movies in updateMovies() method: " + orderOfMovies);
        movieTask.execute(orderOfMovies);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        moviesAdapter.swapCursor(null);
    }
}
