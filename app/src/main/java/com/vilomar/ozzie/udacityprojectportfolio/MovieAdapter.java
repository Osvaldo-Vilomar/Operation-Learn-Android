package com.vilomar.ozzie.udacityprojectportfolio;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


/**
 * Created by Ozzie on 8/14/16.
 */
public class MovieAdapter extends CursorAdapter {

    private final String LOG_TAG = MovieAdapter.class.getSimpleName();

    public MovieAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        Log.v(LOG_TAG, "In newView");
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_movie, parent, false);

        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.

        ImageView moviePoster = (ImageView) view;

        Picasso.with(context).load("http://image.tmdb.org/t/p/w500/" + cursor
                .getString(MoviesFragment.COL_IMAGE_PATH))
                .resize(540, 810)
                .into(moviePoster);
    }
}

