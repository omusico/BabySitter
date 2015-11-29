package com.ohad.babysitter;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.ohad.babysitter.base.activity.ActivityBase;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActivityBase implements AddUserDialog.AddUserCallback {

    private ListView mListView;
    private List<UserPojo> mUsers;
    private UserAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initList();

        ParseQuery<ParseObject> query = ParseQuery.getQuery(UserPojo.KEY_CLASS_NAME);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (int i = 0; i < objects.size(); i++) {
                    ParseObject parseObject = objects.get(i);
                    UserPojo userPojo = new UserPojo(parseObject);
                    mUsers.add(userPojo);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });



        /*final UserPojo userPojo = new UserPojo(UserPojo.KEY_CLASS_NAME);
        userPojo.put(UserPojo.KEY_FIRST_NAME_COLUMN, "ohad");
        userPojo.put(UserPojo.KEY_LAST_NAME_COLUMN, "shiffer");
        userPojo.put(UserPojo.KEY_FULL_NAME_COLUMN, "ohad shiffer");
        userPojo.put(UserPojo.KEY_PHONE_COLUMN, "054-2571392");
        userPojo.put(UserPojo.KEY_BIRTHDAY_COLUMN, "1000005735");
        userPojo.put(UserPojo.KEY_AGE_COLUMN, 28);
        userPojo.put(UserPojo.KEY_CITY_COLUMN, "maale shomron");
        userPojo.put(UserPojo.KEY_PROFILE_IMAGE_COLUMN, "http://static3.bigstockphoto.com/thumbs/5/1/3/large2/3153618.jpg");
        userPojo.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
            }
        });*/

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

            case R.id.action_more: //----------------------------------More

                break;

            case R.id.action_add: //-----------------------------------Add
                new AddUserDialog(this, this).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddUserCallbackResult(UserPojo userPojo) {
        mUsers.add(userPojo);
        mAdapter.notifyDataSetChanged();
    }

}
