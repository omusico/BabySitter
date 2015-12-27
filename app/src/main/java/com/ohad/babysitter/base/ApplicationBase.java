package com.ohad.babysitter.base;

import android.app.Application;

import com.ohad.babysitter.R;
import com.ohad.babysitter.pojo.UserPojo;
import com.parse.Parse;
import com.parse.ParseObject;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by Ohad on 27/11/2015.
 *
 */
public class ApplicationBase extends Application {

    private static ApplicationBase sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = (ApplicationBase) this.getApplicationContext();
        initParse(); // Initialize Parse
    }

    public static ApplicationBase getInstance() {
        return sInstance;
    }

    private void initParse() {
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));
        //Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(UserPojo.class);
    }

    // ------------------------------MARK: EventBus----------------------------------

    private final Bus mBus = new Bus(ThreadEnforcer.ANY);

    public Bus getBus() {
        return mBus;
    }

}
