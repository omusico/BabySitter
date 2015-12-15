package com.ohad.babysitter.pojo;

import android.os.Parcel;

import com.parse.ParseUser;

/**
 * Created by Ohad on 16/12/2015.
 *
 */
public class Self extends UserPojo {

    private static Self self;

    public static Self getInstance() {
        if (self == null) {
            setInstance();
            return self;
        } else {
            return self;
        }
    }

    public static void setInstance() {
        self = new Self(ParseUser.getCurrentUser());
    }

    private Self(ParseUser parseUser) {
        setId(parseUser.getObjectId());
        setFirstName(parseUser.getString(UserPojo.KEY_FIRST_NAME_COLUMN));
        setLastName(parseUser.getString(UserPojo.KEY_LAST_NAME_COLUMN));
        setCity(parseUser.getString(UserPojo.KEY_CITY_COLUMN));
        setPhone(parseUser.getString(UserPojo.KEY_PHONE_COLUMN));
        setEmail(parseUser.getString(UserPojo.KEY_EMAIL_COLUMN));
        setAbout(parseUser.getString(UserPojo.KEY_ABOUT_COLUMN));
        setBirthday(parseUser.getString(UserPojo.KEY_BIRTHDAY_COLUMN));
        setProfilePictureUrl(parseUser.getString(UserPojo.KEY_PICTURE_COLUMN));
        setAge(parseUser.getInt(UserPojo.KEY_AGE_COLUMN));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected Self(Parcel in) {
        super(in);
    }

    public static final Creator<Self> CREATOR = new Creator<Self>() {
        public Self createFromParcel(Parcel source) {
            return new Self(source);
        }

        public Self[] newArray(int size) {
            return new Self[size];
        }
    };
}
