package com.ohad.babysitter.main;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gregacucnik.EditTextView;
import com.ohad.babysitter.R;
import com.ohad.babysitter.base.activity.ActivityBase;
import com.ohad.babysitter.pojo.Self;
import com.ohad.babysitter.pojo.UserPojo;
import com.ohad.babysitter.utility.Constant;
import com.ohad.babysitter.utility.GenderPicker;
import com.ohad.babysitter.utility.ParseErrorHandler;
import com.ohad.babysitter.utility.ProgressBarClass;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class UserProfileActivity extends ActivityBase implements View.OnClickListener {

    //private static final int KEY_PICKER_AGE = 562;
    //private static final int KEY_PICKER_SALARY = 781;
    //private static final int KEY_PICKER_BIRTHDAY = 999;

    private FloatingActionButton mFloatingBtn;
    private CollapsingToolbarLayout mColToolbar;
    private AppBarLayout mAppBar;
    private Toolbar mToolbar;

    private ImageView mIvProfile;
    private TextView mTvGender, mTvBirthday, mTvSalary;
    private EditTextView mEtvFirstName, mEtvLastName, mEtvCity, mEtvPhone, mEtvEmail, mEtvAbout;

    private Calendar mCalendar;
    private ParseUser mUser;

    public static void start(Context context, String userId) {
        Intent starter = new Intent(context, UserProfileActivity.class);
        starter.putExtra(UserPojo.KEY_OBJECT_ID, userId);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String mUserId = getIntent().getStringExtra(UserPojo.KEY_OBJECT_ID);

        mCalendar = Calendar.getInstance();

        if (mUserId == null) {
            mUser = ParseUser.getCurrentUser();
            initPageDetails();
        } else {
            ProgressBarClass.startLoading(this);

            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo(UserPojo.KEY_OBJECT_ID, mUserId);

            query.getFirstInBackground(new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser object, ParseException e) {
                    ProgressBarClass.dismissLoading();
                    if (object != null) {
                        mUser = object;
                    } else {
                        mUser = Self.getInstance();
                    }

                    initPageDetails();
                }
            });
        }

        mFloatingBtn.setOnClickListener(this);
        mTvGender.setOnClickListener(this);
        mTvBirthday.setOnClickListener(this);
        mTvSalary.setOnClickListener(this);
    }

    @Override
    public void initViews() {
        setContentView(R.layout.activity_user_profile);
        mFloatingBtn = (FloatingActionButton) findViewById(R.id.fab);
        mColToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mAppBar = (AppBarLayout) findViewById(R.id.app_bar);
        mIvProfile = (ImageView) findViewById(R.id.ivBg);

        mTvGender = (TextView) findViewById(R.id.tvGender);
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
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
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

            case R.id.tvGender:
                actionGenderPicker();
                break;

            case R.id.tvSalary:
                actionSalaryPicker(0, 150, mUser.getInt(UserPojo.KEY_SALARY_COLUMN), getString(R.string.hint_salary));
                break;

            case R.id.tvBirth:
                actionDatePicker();
                break;
        }
    }

    private void actionSalaryPicker(int minValue, int maxValue, int currentValue, String title) {
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
                        final int salary = picker.getValue();
                        stringBuilder = new StringBuilder(getString(R.string.hint_salary));
                        stringBuilder.append(" ");
                        stringBuilder.append(salary);
                        mUser.put(UserPojo.KEY_SALARY_COLUMN, salary);
                        mTvSalary.setText(stringBuilder);
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

                final int age = getAge(year, monthOfYear, dayOfMonth);

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(dayOfMonth);
                stringBuilder.append("/");
                stringBuilder.append(monthOfYear + 1);
                stringBuilder.append("/");
                stringBuilder.append(year);

                mUser.put(UserPojo.KEY_BIRTHDAY_COLUMN, stringBuilder.toString());
                mUser.put(UserPojo.KEY_AGE_COLUMN, age);
                mUser.put(Constant.KEY_DATE_OF_BIRTH_COLUMN, String.valueOf(mCalendar.getTimeInMillis()));

                stringBuilder.append(" (");
                stringBuilder.append(age);
                stringBuilder.append(")");

                mTvBirthday.setText(stringBuilder);

            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void actionGenderPicker() {
        new GenderPicker(this, new GenderPicker.GenderPickerCallback() {
            @Override
            public void onGenderPicked(String gender) {
                mTvGender.setText(gender);
                mUser.put(UserPojo.KEY_GENDER_COLUMN, gender);
            }
        }).show();
    }

    private void actionDone() {
        ProgressBarClass.startLoading(this);

        putValueToUser(UserPojo.KEY_FIRST_NAME_COLUMN, mEtvFirstName);
        putValueToUser(UserPojo.KEY_LAST_NAME_COLUMN, mEtvLastName);
        putValueToUser(UserPojo.KEY_CITY_COLUMN, mEtvCity);
        putValueToUser(UserPojo.KEY_PHONE_COLUMN, mEtvPhone);
        putValueToUser(UserPojo.KEY_EMAIL_COLUMN, mEtvEmail);
        putValueToUser(UserPojo.KEY_ABOUT_COLUMN, mEtvAbout);

        mUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                ProgressBarClass.dismissLoading();

                if (e == null) {
                    finish();
                } else {
                    ParseErrorHandler.handleParseError(UserProfileActivity.this, e);
                }
            }
        });
    }

    private void initPageDetails() {
        StringBuilder stringBuilder;

        // init calendar
        final String dateOfBirth = mUser.getString(Constant.KEY_DATE_OF_BIRTH_COLUMN);
        if (StringUtils.isNumeric(dateOfBirth)){
            final long time = Long.parseLong(dateOfBirth);
            mCalendar.setTimeInMillis(time);
        }

        // init profile picture
        final ParseFile parseFile = mUser.getParseFile(UserPojo.KEY_PICTURE_COLUMN);
        final String picUrl = parseFile != null ? parseFile.getUrl() : Constant.DEFAULT_PROFILE_IMAGE;
        Glide.with(this).load(picUrl).into(mIvProfile);

        // init title
        setToolbarTitle(mUser.getString(Constant.KEY_NAME_COLUMN));

        // init gender
        stringBuilder = new StringBuilder(getString(R.string.hint_gender));
        stringBuilder.append(" ");
        stringBuilder.append(mUser.getString(UserPojo.KEY_GENDER_COLUMN));
        mTvGender.setText(stringBuilder);

        // init salary
        stringBuilder = new StringBuilder(getString(R.string.hint_salary));
        stringBuilder.append(" ");
        stringBuilder.append(mUser.getInt(UserPojo.KEY_SALARY_COLUMN));
        mTvSalary.setText(stringBuilder);

        // init birthday
        stringBuilder = new StringBuilder(mUser.getString(UserPojo.KEY_BIRTHDAY_COLUMN));
        stringBuilder.append(" (");
        stringBuilder.append(mUser.getInt(UserPojo.KEY_AGE_COLUMN));
        stringBuilder.append(")");
        mTvBirthday.setText(stringBuilder);

        // init first name edit text
        mEtvFirstName.setText(mUser.getString(UserPojo.KEY_FIRST_NAME_COLUMN));

        // init last name edit text
        mEtvLastName.setText(mUser.getString(UserPojo.KEY_LAST_NAME_COLUMN));

        // init city edit text
        mEtvCity.setText(mUser.getString(UserPojo.KEY_CITY_COLUMN));

        // init phone edit text
        mEtvPhone.setText(mUser.getString(UserPojo.KEY_PHONE_COLUMN));

        // init email edit text
        mEtvEmail.setText(mUser.getString(UserPojo.KEY_EMAIL_COLUMN));

        // init about edit text
        mEtvAbout.setText(mUser.getString(UserPojo.KEY_ABOUT_COLUMN));
    }

    private void setToolbarTitle(String title) {
        if (title == null || title.isEmpty() || title.contains("null"))
            return;

        mColToolbar.setTitle(title);

        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //Utility.d("verticalOffset: " + verticalOffset);
                if (verticalOffset < -200) {
                    mToolbar.setBackgroundColor(ContextCompat.getColor(UserProfileActivity.this, R.color.blue_toolbar));
                } else {
                    mToolbar.setBackgroundColor(ContextCompat.getColor(UserProfileActivity.this, R.color.transparent));
                }
            }
        });

    }

    private void putValueToUser(String key, EditTextView editTextView) {
        if (editTextView.getText().isEmpty())
            return;

        mUser.put(key, editTextView.getText());
    }

    public int getAge (int aYear, int aMonth, int aDay) {

        GregorianCalendar cal = new GregorianCalendar();
        int year, month, day, age;

        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(aYear, aMonth, aDay);
        age = year - cal.get(Calendar.YEAR);

        if ((month < cal.get(Calendar.MONTH))
                || ((month == cal.get(Calendar.MONTH))
                && (day < cal.get(Calendar.DAY_OF_MONTH)))) {
            --age;
        }

        if(age < 0)
            throw new IllegalArgumentException("Age < 0");

        return age;
    }

}
