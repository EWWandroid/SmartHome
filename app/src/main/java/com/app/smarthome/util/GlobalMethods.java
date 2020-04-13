package com.app.smarthome.util;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.widget.Toolbar;

import com.app.smarthome.R;
import com.app.smarthome.Session;
import com.app.smarthome.databinding.CustomToolbarBinding;
import com.app.smarthome.retrofit.model.main.ModelLoginResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import static com.app.smarthome.util.Constants.SHARED_PREFERENCE_KEY_USER_DETAIL;

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

    public static void setToolbar(CustomToolbarBinding toolbar, int title, int drawableId) {
        toolbar.ivTbStart.setImageResource(drawableId);
        toolbar.tvTbCenter.setText(title);
    }

    public static String getToken(Context context) {
        Session session = new Session(context);
        String data = session.getData(SHARED_PREFERENCE_KEY_USER_DETAIL);
        /*todo: remove static token and put dynamic token(login token and token required are different hence static token)*/
        return new Gson().fromJson(data, ModelLoginResponse.class).getAuth().getType() + " " + new Gson().fromJson(data, ModelLoginResponse.class).getAuth().getToken();
        //return "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOjIsImlhdCI6MTU4NTY0MDkwNH0.tZs0UEyLX5G4aVNDFir29bHabCdqFprRajcfhrBE4Nc";
    }

    public static void showPermissionAlertDialog(Context context,String title, String msg, String positiveLabel,
                                          DialogInterface.OnClickListener positiveOnClick,
                                          String negativeLabel, DialogInterface.OnClickListener negativeOnClick,
                                          boolean isCancelAble) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveLabel, positiveOnClick);
        builder.setNegativeButton(negativeLabel, negativeOnClick);
        builder.setCancelable(isCancelAble);

        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
