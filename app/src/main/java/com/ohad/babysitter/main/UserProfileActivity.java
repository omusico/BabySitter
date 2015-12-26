package com.ohad.babysitter.main;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.gregacucnik.EditTextView;
import com.ohad.babysitter.R;
import com.ohad.babysitter.base.activity.ActivityBase;
import com.ohad.babysitter.pojo.Self;
import com.ohad.babysitter.pojo.UserPojo;
import com.ohad.babysitter.utility.ProgressBarClass;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Calendar;

public class UserProfileActivity extends ActivityBase implements View.OnClickListener {

    private static final int KEY_PICKER_AGE = 562;
    private static final int KEY_PICKER_SALARY = 781;
    private static final int KEY_PICKER_BIRTHDAY = 999;

    private FloatingActionButton mFloatingBtn;
    private NestedScrollView mNestedScroll;
    private CollapsingToolbarLayout mColToolbar;
    private AppBarLayout mAppBar;

    private TextView mTvAge, mTvBirthday, mTvSalary;
    private EditTextView mEtvFirstName, mEtvLastName, mEtvCity, mEtvPhone, mEtvEmail, mEtvAbout;

    private Calendar mCalendar;
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

        mCalendar = Calendar.getInstance();

        if (mUser == null) {
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

                    setToolbarTitle(mUser.getFullname());

                }
            });

        }

        mFloatingBtn.setOnClickListener(this);
        mTvAge.setOnClickListener(this);
        mTvBirthday.setOnClickListener(this);
        mTvSalary.setOnClickListener(this);
    }

    private void setToolbarTitle(String title) {
        if (title == null || title.isEmpty() || title.contains("null"))
            return;

        mColToolbar.setTitle(title);
    }

    @Override
    public void initViews() {
        setContentView(R.layout.activity_user_profile);
        mFloatingBtn = (FloatingActionButton) findViewById(R.id.fab);
        mNestedScroll = (NestedScrollView) findViewById(R.id.fbcContainer);
        mColToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mAppBar = (AppBarLayout) findViewById(R.id.app_bar);

        mTvAge = (TextView) findViewById(R.id.tvAge);
        mTvBirthday = (TextView) findViewById(R.id.tvBirth);
        mTvSalary = (TextView) findViewById(R.id.tvSalary);

        mEtvFirstName = (EditTextView) findViewById(R.id.ettFirst);
        mEtvLastName = (EditTextView) findViewById(R.id.ettLast);
        mEtvCity = (EditTextView) findViewById(R.id.ettCity);
        mEtvPhone = (EditTextView) findViewById(R.id.ettPhone);
        mEtvEmail = (EditTextView) findViewById(R.id.ettEmail);
        mEtvAbout = (EditTextView) findViewById(R.id.ettAbout);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_done:
                actionDone();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;

            case R.id.tvAge:
                actionNumberPicker(0, 120, mUser.getAge(), getString(R.string.picker_age_title), KEY_PICKER_AGE);
                break;

            case R.id.tvSalary:
                actionNumberPicker(0, 150, mUser.getSalary(), getString(R.string.hint_salary), KEY_PICKER_SALARY);
                break;

            case R.id.tvBirth:
                actionDatePicker();
                break;
        }
    }

    private void actionNumberPicker(int minValue, int maxValue, int currentValue, String title, final int type) {
        final NumberPicker picker = new NumberPicker(this);
        picker.setMinValue(minValue);
        picker.setMaxValue(maxValue);
        picker.setValue(currentValue);

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setCancelable(true)
                .setView(picker)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuilder stringBuilder;
                        switch (type) {
                            case KEY_PICKER_AGE: //-----------------> Age
                                final int age = picker.getValue();
                                stringBuilder = new StringBuilder(getString(R.string.hint_age));
                                stringBuilder.append(" ");
                                stringBuilder.append(age);
                                mUser.setAge(age);
                                mTvAge.setText(stringBuilder);
                                break;

                            case KEY_PICKER_SALARY: //-----------------> Salary
                                final int salary = picker.getValue();
                                stringBuilder = new StringBuilder(getString(R.string.hint_salary));
                                stringBuilder.append(" ");
                                stringBuilder.append(salary);
                                mUser.setSalary(salary);
                                mTvSalary.setText(stringBuilder);
                                break;
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void actionDatePicker() {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mCalendar.set(year, monthOfYear, dayOfMonth);

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(dayOfMonth);
                stringBuilder.append("/");
                stringBuilder.append(monthOfYear);
                stringBuilder.append("/");
                stringBuilder.append(year);

                mUser.setDateOfBirth(mCalendar.getTimeInMillis());
                mUser.setBirthday(stringBuilder.toString());
                mTvBirthday.setText(stringBuilder);

            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void actionDone() {

        mUser.saveToParse();
    }

}
