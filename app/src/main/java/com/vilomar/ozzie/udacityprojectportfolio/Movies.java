package com.vilomar.ozzie.udacityprojectportfolio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class Movies extends AppCompatActivity {

    private final String MOVIEFRAGMENT_TAG = "MFTAG";

    private String mOrderOfMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movies_container, new MoviesFragment(), MOVIEFRAGMENT_TAG)
                    .commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String orderOfMovies = Utility.getPreferredOrderOfMovies( this );
        // update the location in our second pane using the fragment manager
        if (orderOfMovies != null && !orderOfMovies.equals(mOrderOfMovies)) {
            MoviesFragment mf = (MoviesFragment) getSupportFragmentManager().findFragmentByTag(MOVIEFRAGMENT_TAG);
            if ( null != mf) {
                mf.onOrderOfMoviesChanged();
            }
            mOrderOfMovies = orderOfMovies;
        }
    }
}
