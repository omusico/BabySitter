package com.ohad.babysitter.pojo;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.ohad.babysitter.utility.Constant;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Ohad on 26/11/2015.
 *
 */

@ParseClassName("UserPojo")
public class UserPojo extends ParseObject implements Parcelable, Comparable<UserPojo> {   //extends ParseObject

    public static final String KEY_CLASS_NAME = "UserPojo";

    public static final String KEY_OBJECT_ID = "objectId";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_FIRST_NAME_COLUMN = "first_name";
    public static final String KEY_LAST_NAME_COLUMN = "last_name";
    public static final String KEY_FULL_NAME_COLUMN = "full_name";
    public static final String KEY_CITY_COLUMN = "city";
    public static final String KEY_PHONE_COLUMN = "phone";
    public static final String KEY_PROFILE_IMAGE_URL_COLUMN = "profile_image";
    public static final String KEY_AGE_COLUMN = "age";
    public static final String KEY_ABOUT_COLUMN = "about";
    public static final String KEY_SALARY_COLUMN = "salary";
    public static final String KEY_EMAIL_COLUMN = "email";
    public static final String KEY_PICTURE_FILE_COLUMN = "picture_file";
    public static final String KEY_GENDER_COLUMN = "gender";
    public static final String KEY_CREATED_COLUMN = "created";

    private Bitmap bitmap;

    // TODO Mark: Constructors

    public UserPojo() {
        super(KEY_CLASS_NAME);
    }

    public UserPojo(String theClassName) {
        super(theClassName);
    }

    public UserPojo(ParseObject parseObject){
        super(KEY_CLASS_NAME);
        setObjectId(parseObject.getObjectId());

        if (parseObject.has(KEY_USER_ID))
            put(KEY_USER_ID, parseObject.getString(KEY_USER_ID));

        if (parseObject.has(KEY_FIRST_NAME_COLUMN))
            put(KEY_FIRST_NAME_COLUMN, parseObject.getString(KEY_FIRST_NAME_COLUMN));

        if (parseObject.has(KEY_LAST_NAME_COLUMN))
            put(KEY_LAST_NAME_COLUMN, parseObject.getString(KEY_LAST_NAME_COLUMN));

        if (parseObject.has(KEY_FULL_NAME_COLUMN))
            put(KEY_FULL_NAME_COLUMN, parseObject.getString(KEY_FULL_NAME_COLUMN));

        if (parseObject.has(KEY_CITY_COLUMN))
            put(KEY_CITY_COLUMN, parseObject.getString(KEY_CITY_COLUMN));

        if (parseObject.has(KEY_PHONE_COLUMN))
            put(KEY_PHONE_COLUMN, parseObject.getString(KEY_PHONE_COLUMN));

        if (parseObject.has(KEY_EMAIL_COLUMN))
            put(KEY_EMAIL_COLUMN, parseObject.getString(KEY_EMAIL_COLUMN));

        if (parseObject.has(KEY_SALARY_COLUMN))
            put(KEY_SALARY_COLUMN, parseObject.getString(KEY_SALARY_COLUMN));

        if (parseObject.has(KEY_ABOUT_COLUMN))
            put(KEY_ABOUT_COLUMN, parseObject.getString(KEY_ABOUT_COLUMN));

        if (parseObject.has(KEY_AGE_COLUMN))
            put(KEY_AGE_COLUMN, parseObject.getInt(KEY_AGE_COLUMN));

        if (parseObject.has(KEY_GENDER_COLUMN))
            put(KEY_GENDER_COLUMN, parseObject.getString(KEY_GENDER_COLUMN));

        if (parseObject.has(KEY_CREATED_COLUMN))
            put(KEY_CREATED_COLUMN, parseObject.getInt(KEY_CREATED_COLUMN));

        if (parseObject.getString(KEY_PICTURE_FILE_COLUMN) != null)
            put(KEY_PICTURE_FILE_COLUMN, parseObject.getString(KEY_PICTURE_FILE_COLUMN));

        final ParseFile parseFile = parseObject.getParseFile(KEY_PICTURE_FILE_COLUMN);
        final String profileImageUrl = parseFile != null ?
                parseFile.getUrl() :
                Constant.DEFAULT_PROFILE_IMAGE;


        put(KEY_PROFILE_IMAGE_URL_COLUMN, profileImageUrl);
    }

    public UserPojo(ParseUser parseUser){
        super(KEY_CLASS_NAME);
        setObjectId(parseUser.getObjectId());
        put(KEY_USER_ID, parseUser.getObjectId());
        put(KEY_FIRST_NAME_COLUMN, parseUser.getString(KEY_FIRST_NAME_COLUMN));
        put(KEY_LAST_NAME_COLUMN, parseUser.getString(KEY_LAST_NAME_COLUMN));
        put(KEY_FULL_NAME_COLUMN, parseUser.getString(KEY_FULL_NAME_COLUMN));
        put(KEY_CITY_COLUMN, parseUser.getString(KEY_CITY_COLUMN));
        put(KEY_PHONE_COLUMN, parseUser.getString(KEY_PHONE_COLUMN));
        put(KEY_EMAIL_COLUMN, parseUser.getString(KEY_EMAIL_COLUMN));
        put(KEY_ABOUT_COLUMN, parseUser.getString(KEY_ABOUT_COLUMN));
        put(KEY_GENDER_COLUMN, parseUser.getString(KEY_GENDER_COLUMN));
        //put(KEY_SALARY_COLUMN, parseUser.getString(KEY_SALARY_COLUMN));
        put(KEY_CREATED_COLUMN, parseUser.getInt(KEY_CREATED_COLUMN));
        put(KEY_AGE_COLUMN, parseUser.getInt(KEY_AGE_COLUMN));

        if (parseUser.getString(KEY_PICTURE_FILE_COLUMN) != null) {
            put(KEY_PICTURE_FILE_COLUMN, parseUser.getString(KEY_PICTURE_FILE_COLUMN));
        }

        final ParseFile parseFile = parseUser.getParseFile(KEY_PICTURE_FILE_COLUMN);
        final String profileImageUrl = parseFile != null ?
                parseFile.getUrl() :
                Constant.DEFAULT_PROFILE_IMAGE;

        put(KEY_PROFILE_IMAGE_URL_COLUMN, profileImageUrl);
    }

    // TODO Mark: Getters

    public String getUserId() {
        return getString(KEY_USER_ID);
    }

    public String getFirstName() {
        return getString(KEY_FIRST_NAME_COLUMN);
    }

    public String getLastName() {
        return getString(KEY_LAST_NAME_COLUMN);
    }

    public String getFullName() {
        return getString(KEY_FULL_NAME_COLUMN);
    }

    public String getCity() {
        return getString(KEY_CITY_COLUMN);
    }

    public String getPhone() {
        return getString(KEY_PHONE_COLUMN);
    }

//    public String getBirthday() {
//        return getString(KEY_BIRTHDAY_COLUMN);
//    }

    public String getEmail() {
        return getString(KEY_EMAIL_COLUMN);
    }

    public String getAbout() {
        return getString(KEY_ABOUT_COLUMN);
    }

    public String getGender() {
        return getString(KEY_GENDER_COLUMN);
    }

    public String getSalary() {
        return getString(KEY_SALARY_COLUMN);
    }

    public int getAge() {
        return getInt(KEY_AGE_COLUMN);
    }

    public long getCreated() {
        return getLong(KEY_CREATED_COLUMN);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    // TODO Mark: Setters

    public void setFirstName(String firstName) {
        put(KEY_FIRST_NAME_COLUMN, firstName);
    }

    public void setLastName(String lastName) {
        put(KEY_LAST_NAME_COLUMN, lastName);
    }

    public void setFullName(String fullName) {
        put(KEY_FULL_NAME_COLUMN, fullName);
    }

    public void setUserId(String userId) {
        put(KEY_USER_ID, userId);
    }

    public void setCity(String city) {
        put(KEY_CITY_COLUMN, city);
    }

    public void setPhone(String phone) {
        put(KEY_PHONE_COLUMN, phone);
    }

    public void setEmail(String email) {
        put(KEY_EMAIL_COLUMN, email);
    }

//    public void setBirthday(String birthday) {
//        put(KEY_BIRTHDAY_COLUMN, birthday);
//    }

    public void setAbout(String about) {
        put(KEY_ABOUT_COLUMN, about);
    }

    public void setGender(String gender) {
        put(KEY_GENDER_COLUMN, gender);
    }

    public void setAge(int age) {
        put(KEY_AGE_COLUMN, age);
    }

    public void setAge(String age) {
        if (StringUtils.isNumeric(age)) {
            put(KEY_AGE_COLUMN, Integer.valueOf(age));
        }
    }

    public void setSalary(String salary) {
        put(KEY_SALARY_COLUMN, salary);
    }

    public void setCreated(long created) {
        put(KEY_CREATED_COLUMN, created);
    }

    public void setPictureFile(ParseFile pictureFile){
        put(UserPojo.KEY_PICTURE_FILE_COLUMN, pictureFile == null ? "" : pictureFile);
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    // TODO Mark: Other

    @Override
    public int compareTo(@NonNull UserPojo another) {
        final int myCreated = getInt(KEY_CREATED_COLUMN);
        final int anotherCreated = another.getInt(KEY_CREATED_COLUMN);

        return myCreated < anotherCreated ? -1 : (myCreated == anotherCreated ? 0 : 1);
    }

    // TODO Mark: Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.bitmap, 0);
    }

    protected UserPojo(Parcel in) {
        this.bitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<UserPojo> CREATOR = new Creator<UserPojo>() {
        public UserPojo createFromParcel(Parcel source) {
            return new UserPojo(source);
        }

        public UserPojo[] newArray(int size) {
            return new UserPojo[size];
        }
    };
}
