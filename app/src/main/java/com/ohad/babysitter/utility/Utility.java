package com.ohad.babysitter.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ohad on 01/12/2015.
 * Helpful static methods.
 */
public class Utility {

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

    public static ParseFile uploadParseFile(Context context, Bitmap bitmap) {
        // Convert it to byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();

        // Create the ParseFile
        ParseFile file = new ParseFile("baby_sitter_temp.png", image);
        // Upload the image into Parse Cloud
        file.saveInBackground();

        // Create a New Class called "ImageUpload" in Parse
        ParseObject imgupload = new ParseObject("ImageUpload");

        // Create a column named "ImageName" and set the string
        imgupload.put("ImageName", "AndroidBegin Logo");

        // Create a column named "ImageFile" and insert the image
        imgupload.put("ImageFile", file);

        // Create the class and the columns
        imgupload.saveInBackground();

        // Show a simple toast message
        Toast.makeText(context, "Image Uploaded", Toast.LENGTH_SHORT).show();

        return file;
    }


}


