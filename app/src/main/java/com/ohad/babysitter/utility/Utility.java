package com.ohad.babysitter.utility;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.text.TextUtils;
import android.util.Log;

import com.ohad.babysitter.R;
import com.ohad.babysitter.pojo.Self;
import com.ohad.babysitter.pojo.UserPojo;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ohad on 01/12/2015.
 * Helpful static methods.
 */
public class Utility {




    // TODO Mark: Log

    private static String TAG = "ohad";

    public static void e(String where, String message) {
        Log.e(TAG, "Error in: " + where + " Reason: " + message);
    }

    public static void d(String message) {
        Log.d(TAG, message);
    }




    // TODO Mark: Validation

    /**
     * method is used for checking valid phone format.
     * @param phone string
     * @return boolean true for valid false for invalid
     */
    public static boolean isValidPhoneNumber(CharSequence phone) {
        return !(phone == null || TextUtils.isEmpty(phone)) && android.util.Patterns.PHONE.matcher(phone).matches();
    }

    /**
     * method is used for checking valid email id format.
     * @param email string
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        boolean isValid = false;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }




    // TODO Mark: Files

    /**
     * @param f the image file which you want to rescale.
     * @param size the size (first from height or width) of the scaled bitmap.
     * @return scaled bitmap of the *required size*. <br>
     * *required size is changable.
     */
    public static Bitmap decodeFile(File f, int size){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while(true){
                if(width_tmp / 2 < size || height_tmp / 2 < size)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            d("decodeFile size = " + scale);
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            e("utility-decodeFile", e.getMessage());
        }
        return null;
    }




    // TODO Mark: Parse API

    public static Intent getLoginIntent(Context context) {
        ParseLoginBuilder builder = new ParseLoginBuilder(context);

        return builder.setAppLogo(R.drawable.login_logo)
                .setParseLoginEnabled(true)
                .setParseLoginButtonText(context.getString(R.string.login_go))
                .setParseSignupButtonText(context.getString(R.string.login_register))
                .setParseLoginHelpText(context.getString(R.string.login_forgot_pass))
                .setParseLoginInvalidCredentialsToastText(context.getString(R.string.login_invalid_details))
                .setParseLoginEmailAsUsername(true)
                .setParseSignupSubmitButtonText(context.getString(R.string.login_submit_reg))
                .setFacebookLoginEnabled(true)
                .setFacebookLoginButtonText(context.getString(R.string.login_facebook))
                .setFacebookLoginPermissions(Arrays.asList("public_profile", "user_friends"))
                .setTwitterLoginEnabled(true)
                .setTwitterLoginButtontext(context.getString(R.string.login_twitter))
                .build();
    }

    public static ParseFile uploadParseFile(Bitmap bitmap, String name) {
        // Convert it to byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();

        return new ParseFile("baby_sitter_" + name + ".png", image);
    }

    public static void setGeoPointToUserPojo(UserPojo userPojo) {
        final Location location = LocationManagerSingleton.getInstance().getLatestLocation();
        if (location == null) {
            return;
        }

        final ParseGeoPoint geoPoint = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
        userPojo.setLocation(geoPoint);
    }

}


