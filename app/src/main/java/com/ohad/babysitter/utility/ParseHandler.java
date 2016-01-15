package com.ohad.babysitter.utility;

import com.ohad.babysitter.pojo.UserPojo;
import com.parse.GetCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by Ohad on 16/01/2016.
 *
 */
public class ParseHandler {

    public static void getParseUser(String userId, GetCallback<ParseUser> callback) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo(UserPojo.KEY_OBJECT_ID, userId);
        query.getFirstInBackground(callback);
    }
}
