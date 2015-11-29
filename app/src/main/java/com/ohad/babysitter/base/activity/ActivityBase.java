package com.ohad.babysitter.base.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Ohad on 27/11/2015.
 *
 */
public abstract class ActivityBase extends AppCompatActivity implements ActivityBaseInterface {

    public ActivityBaseInterface mInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInterface = this;
        mInterface.initViews();
        mInterface.initToolbar();

        /*Toolbar mToolbar = mInterface.initToolbar();

        if (mToolbar != null){
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
        }*/
    }
}
