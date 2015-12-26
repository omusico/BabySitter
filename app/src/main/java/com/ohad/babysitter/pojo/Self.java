package com.ohad.babysitter.pojo;

import com.parse.ParseUser;

/**
 * Created by Ohad on 16/12/2015.
 *
 */
public class Self {

    private static ParseUser self;

    public static ParseUser getInstance() {
        if (self == null) {
            setInstance();
            return self;
        } else {
            return self;
        }
    }

    public static void setInstance() {
        self = ParseUser.getCurrentUser();
    }

    private Self(ParseUser parseUser) {
        self = parseUser;
    }

}
