package com.ohad.babysitter.base;

import android.app.Application;

import com.ohad.babysitter.R;
import com.ohad.babysitter.pojo.UserPojo;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Ohad on 27/11/2015.
 *
 */
public class ApplicationBase extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Parse
        initParse();
    }

    private void initParse() {
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));
        //Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(UserPojo.class);
    }
}
