package com.vilomar.ozzie.udacityprojectportfolio;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

// change view type synopsis to view type movie details
/**
 * Created by Ozzie on 8/14/16.
 */
public class MovieAdapter extends CursorAdapter {

    private final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private String viewType;
    private final String VIEW_TYPE_MOVIES = "movie_posters";
    private final String VIEW_TYPE_SYNOPSIS = "plot_synopsis";
    private String trailerValue;

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public final ImageView moviePosters;
        public final TextView movieSynopsis;

        public ViewHolder(View view) {
            moviePosters = (ImageView) view.findViewById(R.id.list_movie_poster);
            movieSynopsis = (TextView) view.findViewById(R.id.list_movie_synopsis);
        }
    }

    public MovieAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        int layoutId = -1;
        switch (viewType) {
            case VIEW_TYPE_MOVIES: {
                layoutId = R.layout.list_item_movie;
                break;
            }
            case VIEW_TYPE_SYNOPSIS: {
                layoutId = R.layout.list_item_synopsis;
                break;
            }
        }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        switch (viewType) {
            case VIEW_TYPE_MOVIES: {
                Picasso.with(context).load("http://image.tmdb.org/t/p/w500/" + cursor
                        .getString(MoviesFragment.COL_IMAGE_PATH))
                        .resize(540, 810)
                        .into(viewHolder.moviePosters);
                break;
            }
            case VIEW_TYPE_SYNOPSIS: {
                viewHolder.movieSynopsis.setText(cursor.getString(MovieDetailsFragment.COL_PLOT_SYNOPSIS));
                break;
            }
        }
    }
}