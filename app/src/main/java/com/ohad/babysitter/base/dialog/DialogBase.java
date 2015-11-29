package com.ohad.babysitter.base.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.ViewGroup;

/**
 * Created by Ohad on 27/11/2015.
 *
 */
public abstract class DialogBase extends AppCompatDialog implements DialogBaseInterface {

    public DialogBaseInterface mInterface;

    public DialogBase(Context context) {
        super(context);
    }

    public DialogBase(Context context, int theme) {
        super(context, theme);
    }

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
