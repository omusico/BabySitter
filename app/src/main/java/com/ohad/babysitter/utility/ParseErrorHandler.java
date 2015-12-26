package com.ohad.babysitter.utility;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.ohad.babysitter.R;
import com.ohad.babysitter.main.SplashActivity;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by Ohad on 25/12/2015.
 *
 */
public class ParseErrorHandler {

    public static void handleParseError(Context context, ParseException e) {
        switch (e.getCode()) {
            case ParseException.INVALID_SESSION_TOKEN:
                handleInvalidSessionToken(context);
                break;

            default:
                handleDefaultExcaption(context, e);
                break;
        }
    }

    private static void handleInvalidSessionToken(Context context) {
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

    private static void handleDefaultExcaption(Context context, ParseException e) {
        new AlertDialog.Builder(context)
                .setMessage("There was an unknown error:\n" + "error code: " + e.getCode() + "\n" + "reason: " + e.getMessage())
                .setCancelable(false).setPositiveButton(context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }

}
