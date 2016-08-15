package com.vilomar.ozzie.udacityprojectportfolio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieReviewsFragment extends Fragment {

    TextView movieTitleTV;
    View rootView;
    private ArrayAdapter reviewAdapter;

    public MovieReviewsFragment() {
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

        reviewAdapter = new ReviewContentAdapter(getActivity(), null);

        rootView = inflater.inflate(R.layout.fragment_movie_reviews, container, false);

        ListView reviewList = (ListView) rootView.findViewById(R.id.reviewList);
        reviewList.setAdapter(reviewAdapter);

        movieTitleTV = (TextView)rootView.findViewById(R.id.detail_movie_review_title);
        movieTitleTV.setText(getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT));

        FetchMovieTask movieTask = new FetchMovieTask(getActivity(), reviewAdapter);
        movieTask.execute("review", getActivity().getIntent().getStringExtra("movieId"));

        return rootView;
    }
}
