package com.vilomar.ozzie.udacityprojectportfolio;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Ozzie on 8/5/16.
 */
public class Utility {

    public static String getPreferredOrderOfMovies(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_orderOfMovies_key),
                context.getString(R.string.pref_orderOfMovies_default));
    }
}
