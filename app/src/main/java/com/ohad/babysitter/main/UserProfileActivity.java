package com.ohad.babysitter.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bumptech.glide.util.Util;
import com.ohad.babysitter.R;
import com.ohad.babysitter.base.activity.ActivityBase;
import com.ohad.babysitter.pojo.Self;
import com.ohad.babysitter.pojo.UserPojo;
import com.ohad.babysitter.utility.ProgressBarClass;
import com.ohad.babysitter.utility.Utility;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class UserProfileActivity extends ActivityBase implements View.OnClickListener {

    private FloatingActionButton mFloatingBtn;
    private NestedScrollView mNestedScroll;
    private CollapsingToolbarLayout mColToolbar;
    private AppBarLayout mAppBar;

    private UserPojo mUser;
    private String mUserId;

    public static void start(Context context, String userId) {
        Intent starter = new Intent(context, UserProfileActivity.class);
        starter.putExtra(UserPojo.KEY_USER_ID, userId);
        context.startActivity(starter);
    }

    public static void start(Context context, UserPojo userPojo) {
        Intent starter = new Intent(context, UserProfileActivity.class);
        starter.putExtra(UserPojo.KEY_CLASS_NAME, userPojo);
        context.startActivity(starter);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserId = getIntent().getStringExtra(UserPojo.KEY_USER_ID);
        mUser = getIntent().getParcelableExtra(UserPojo.KEY_CLASS_NAME);

        if (mUser == null) {
            //ParseUser.class
            ProgressBarClass.startLoading(this);
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo(UserPojo.KEY_USER_ID, mUserId);
            query.getFirstInBackground(new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser object, ParseException e) {
                    ProgressBarClass.dismissLoading();
                    if (object != null) {
                        mUser = new UserPojo(object);
                    } else {
                        mUser = Self.getInstance();
                    }
                    Utility.d(mUser.toString());
                }
            });
        }

        mFloatingBtn.setOnClickListener(this);
        mColToolbar.setTitle("Ohad Shiffer");

    }

    @Override
    public void initViews() {
        setContentView(R.layout.activity_user_profile);
        mFloatingBtn = (FloatingActionButton) findViewById(R.id.fab);
        mNestedScroll = (NestedScrollView) findViewById(R.id.fbcContainer);
        mColToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mAppBar = (AppBarLayout) findViewById(R.id.app_bar);
    }

    @Override
    public void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
        }
    }


}
