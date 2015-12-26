package com.ohad.babysitter.utility;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.ohad.babysitter.R;
import com.ohad.babysitter.base.ApplicationBase;
import com.ohad.babysitter.main.SplashActivity;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by Ohad on 25/12/2015.
 *
 */
public class ParseErrorHandler {

    private final static int INVALID_SESSION_TOKEN = 209;

    public static void handleParseError(Context context, ParseException e) {
        switch (e.getCode()) {
            case INVALID_SESSION_TOKEN:
                handleInvalidSessionToken(context);
                break;

            // Other Parse API errors that you want to explicitly handle
        }
    }

    private static void handleInvalidSessionToken(Context context) {
        // If the user needs to finish what they were doing, they have the opportunity to do so.
        final Activity activity = (Activity) context;
         new AlertDialog.Builder(context)
           .setMessage(context.getString(R.string.error_session_invalid))
           .setCancelable(false).setPositiveButton(context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 ParseUser.logOut();
                 activity.startActivity(new Intent(activity, SplashActivity.class));
                 activity.finish();
             }
         }).create().show();

    }

}
