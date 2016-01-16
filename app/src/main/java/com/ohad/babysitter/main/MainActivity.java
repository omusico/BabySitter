package com.ohad.babysitter.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ohad.babysitter.main.Ad.AddAdDialog;
import com.ohad.babysitter.R;
import com.ohad.babysitter.base.ApplicationBase;
import com.ohad.babysitter.base.activity.ActivityBase;
import com.ohad.babysitter.main.fragments.FragmentMain;
import com.ohad.babysitter.pojo.Self;
import com.ohad.babysitter.pojo.UserPojo;
import com.ohad.babysitter.thirdparty.PagerSlidingTabStrip;
import com.ohad.babysitter.user.UserProfileActivity;
import com.ohad.babysitter.model.pickers.AdPicker;
import com.ohad.babysitter.utility.Constant;
import com.ohad.babysitter.parse.ParseHandler;
import com.ohad.babysitter.model.ProgressBarClass;
import com.ohad.babysitter.utility.Utility;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActivityBase implements AddAdDialog.AddUserCallback, MainActivityInterface, AdPicker.AdPickerCallback,
        OnMenuItemClickListener {

    private ContextMenuDialogFragment mMenuFragment;
    private AddAdDialog mAddUserDialog;
    private List<MenuObject> mMenuObjects;
    private boolean mWasResultReturnedToActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initViews() {
        setContentView(R.layout.activity_main);
        final ViewPager mPager = (ViewPager) findViewById(R.id.mPager);
        final PagerSlidingTabStrip mTabs = (PagerSlidingTabStrip) findViewById(R.id.mTabs);

        final MainPagerAdapter pagerAdapter = new MainPagerAdapter(this, getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);
        mTabs.setViewPager(mPager);
    }

    @Override
    public void initToolbar() {
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        mToolbar.setTitle(R.string.main_title);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final int color = ContextCompat.getColor(this, R.color.white);

        MenuObject profile = new MenuObject(getString(R.string.action_profile));
        profile.setResource(R.drawable.ic_menu_account);
        profile.setBgColor(color);

        MenuObject filter = new MenuObject(getString(R.string.action_filter));
        filter.setResource(R.drawable.ic_menu_filter);
        filter.setBgColor(color);

        MenuObject close = new MenuObject(getString(R.string.action_logout));
        close.setResource(R.drawable.ic_menu_logout);
        close.setBgColor(color);

        mMenuObjects = new ArrayList<>();
        mMenuObjects.add(filter);
        mMenuObjects.add(profile);
        mMenuObjects.add(close);

        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.main_menu_item_size));
        menuParams.setMenuObjects(mMenuObjects);
        menuParams.setClosableOutside(true);

        mMenuFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuFragment.setItemClickListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add: //-------------------------------------> Add
                new AdPicker(this, this).show();
                break;

            case R.id.action_more:
                mMenuFragment.show(getSupportFragmentManager(), "ContextMenuDialogFragment");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        final MenuObject menuObject = mMenuObjects.get(position);
        final int menuItemResource = menuObject.getResource();

        switch (menuItemResource) {
            case R.drawable.ic_menu_filter:
                // TODO: Ohad 16/01/2016 add filters
                break;

            case R.drawable.ic_menu_account:
                UserProfileActivity.start(this, ParseUser.getCurrentUser().getObjectId());
                break;

            case R.drawable.ic_menu_logout: //----------------------------------> Logout
                ParseUser.logOut();
                startActivity(new Intent(this, SplashActivity.class));
                finish();
                break;

        }

    }

    @Override
    public void onAddUserCallbackResult(UserPojo userPojo) {
        final MainActivityBus bus = new MainActivityBus(Constant.AD_TYPE_EMPLOYEE, MainActivityBus.ACTION_ADD_AD);
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
                        final UserPojo userPojo = new UserPojo(parseUser);
                        Utility.setGeoPointToUserPojo(userPojo);

                        final MainActivityBus bus = new MainActivityBus(Constant.AD_TYPE_EMPLOYEE, MainActivityBus.ACTION_ADD_AD);
                        bus.setAd(userPojo);
                        ApplicationBase.getInstance().getBus().post(bus);
                    }
                });
                break;

            case R.string.hint_sos:
                // TODO: Ohad 16/01/2016 handle SOS action
                break;

        }
    }
}
