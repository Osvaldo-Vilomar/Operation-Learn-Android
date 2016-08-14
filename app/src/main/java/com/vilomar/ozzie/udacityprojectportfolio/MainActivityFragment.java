package com.vilomar.ozzie.udacityprojectportfolio;

/**
 * Created by Ozzie on 8/14/16.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        loadMovies();

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    private void loadMovies() {
        FetchMovieTask movieTask = new FetchMovieTask(getActivity());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String orderOfMovies = prefs.getString(getString(R.string.pref_orderOfMovies_key),
                getString(R.string.pref_orderOfMovies_default));
        movieTask.execute(orderOfMovies);

    }
}
