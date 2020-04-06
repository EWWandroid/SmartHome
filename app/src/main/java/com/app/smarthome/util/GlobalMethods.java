package com.app.smarthome.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.widget.Toolbar;

import com.app.smarthome.R;
import com.google.android.material.snackbar.Snackbar;

public class GlobalMethods {

    /**
     * @param context where it is called from
     * @return true if network is available is false
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }


    /**
     * @param rootLayout root view of activity to whom snackbar is attached
     */
    public static void showNetworkErrorSnackBar(View rootLayout) {
        Snackbar.make(rootLayout, R.string.internet_not_available, Snackbar.LENGTH_SHORT).show();
    }

    public static void showProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
    }

    public static void hideProgressBar(ProgressBar progressBar) {
        progressBar.setVisibility(View.INVISIBLE);
    }

    public static void setToolbar(Toolbar toolbar) {

    }
}
