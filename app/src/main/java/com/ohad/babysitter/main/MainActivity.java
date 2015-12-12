package com.ohad.babysitter.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.ohad.babysitter.R;
import com.ohad.babysitter.base.activity.ActivityBase;
import com.ohad.babysitter.pojo.UserPojo;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActivityBase implements AddUserDialog.AddUserCallback {

    private ListView mListView;
    private List<UserPojo> mUsers;
    private UserAdapter mAdapter;
    private AddUserDialog mAddUserDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initList();
        refresh();
    }

    private void refresh() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(UserPojo.KEY_CLASS_NAME);
        //query.whereLessThan("age", 50);
        //query.whereGreaterThan("age", 20);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                mUsers.clear();
                for (int i = 0; i < objects.size(); i++) {
                    ParseObject parseObject = objects.get(i);
                    UserPojo userPojo = new UserPojo(parseObject);
                    mUsers.add(userPojo);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void initViews() {
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.main_listView);
    }

    @Override
    public void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        mToolbar.setTitle(R.string.main_title);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        setSupportActionBar(mToolbar);
    }

    private void initList() {
        mUsers = new ArrayList<>();
        mAdapter = new UserAdapter(this, mUsers);
        mListView.setAdapter(mAdapter);
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
                // TODO: 12/12/2015 start profile activity
                break;

            case R.id.action_logout: //----------------------------------> Logout
                ParseUser.logOut();
                startActivity(new Intent(this, SplashActivity.class));
                finish();
                break;

            case R.id.action_add: //-------------------------------------> Add
                mAddUserDialog = new AddUserDialog(this, this);
                mAddUserDialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddUserCallbackResult(UserPojo userPojo) {
        try {
            userPojo.saveToParse();
            mUsers.add(0, userPojo);
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(this, "Cannot create ad. Reason:\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAddUserDialog.onActivityResult(requestCode, resultCode, data);
    }
}
