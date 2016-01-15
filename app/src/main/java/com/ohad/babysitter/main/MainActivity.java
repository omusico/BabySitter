package com.ohad.babysitter.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ohad.babysitter.main.Ad.AddAdDialog;
import com.ohad.babysitter.R;
import com.ohad.babysitter.base.ApplicationBase;
import com.ohad.babysitter.base.activity.ActivityBase;
import com.ohad.babysitter.main.fragments.FragmentMain;
import com.ohad.babysitter.pojo.Self;
import com.ohad.babysitter.pojo.UserPojo;
import com.ohad.babysitter.thirdparty.PagerSlidingTabStrip;
import com.ohad.babysitter.user.UserProfileActivity;
import com.ohad.babysitter.utility.AdPicker;
import com.ohad.babysitter.utility.Constant;
import com.ohad.babysitter.utility.ParseHandler;
import com.ohad.babysitter.utility.ProgressBarClass;
import com.ohad.babysitter.utility.Utility;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends ActivityBase implements AddAdDialog.AddUserCallback, MainActivityInterface, AdPicker.AdPickerCallback {

    private ViewPager mPager;
    private PagerSlidingTabStrip mTabs;

    private AddAdDialog mAddUserDialog;
    private boolean mWasResultReturnedToActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initViews() {
        setContentView(R.layout.activity_main);
        mPager = (ViewPager) findViewById(R.id.mPager);
        mTabs = (PagerSlidingTabStrip) findViewById(R.id.mTabs);

        MainPagerAdapter pagerAdapter = new MainPagerAdapter(this, getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);
        mTabs.setViewPager(mPager);
    }

    @Override
    public void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        mToolbar.setTitle(R.string.main_title);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.action_profile:
                UserProfileActivity.start(this, ParseUser.getCurrentUser().getObjectId());
                break;

            case R.id.action_logout: //----------------------------------> Logout
                ParseUser.logOut();
                startActivity(new Intent(this, SplashActivity.class));
                finish();
                break;

            case R.id.action_add: //-------------------------------------> Add
                new AdPicker(this, this).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddUserCallbackResult(UserPojo userPojo) {
        MainActivityBus bus = new MainActivityBus(Constant.AD_TYPE_EMPLOYEE, MainActivityBus.ACTION_ADD_AD);
        bus.setAd(userPojo);
        ApplicationBase.getInstance().getBus().post(bus);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAddUserDialog.onActivityResult(requestCode, resultCode, data);
        mWasResultReturnedToActivity = true;
    }

    @Override
    public void onFragmentResumed(FragmentMain fragment) {
        if (mWasResultReturnedToActivity) {
            mWasResultReturnedToActivity = false;
        }
    }

    @Override
    public void onAdPicked(int ad) {
        switch (ad) {
            case R.string.hint_manuel:
                mAddUserDialog = new AddAdDialog(this, this);
                mAddUserDialog.show();
                break;

            case R.string.hint_auto:
                ParseHandler.getParseUser(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser object, ParseException e) {
                        ProgressBarClass.dismissLoading();
                        final ParseUser parseUser = object == null ? Self.getInstance() : object;

                        MainActivityBus bus = new MainActivityBus(Constant.AD_TYPE_EMPLOYEE, MainActivityBus.ACTION_ADD_AD);
                        bus.setAd(new UserPojo(parseUser));
                        ApplicationBase.getInstance().getBus().post(bus);
                    }
                });
                //final UserPojo userPojo = new UserPojo(ParseUser.getCurrentUser());
                //Utility.d(userPojo.toString());
                break;

            case R.string.hint_sos:

                break;

        }
    }
}
