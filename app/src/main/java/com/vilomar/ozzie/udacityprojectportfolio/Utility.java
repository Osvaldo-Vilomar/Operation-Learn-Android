package com.vilomar.ozzie.udacityprojectportfolio;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by Ozzie on 8/5/16.
 */
public class Utility extends PreferenceActivity {

    public static String getPreferredOrderOfMovies(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_orderOfMovies_key),
                context.getString(R.string.pref_orderOfMovies_default));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public Intent getParentActivityIntent() {
        return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
}
