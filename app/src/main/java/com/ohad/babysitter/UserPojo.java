package com.ohad.babysitter;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Ohad on 26/11/2015.
 *
 */

@ParseClassName("UserPojo")
public class UserPojo extends ParseObject implements Parcelable {   //extends ParseObject

    public static final String KEY_CLASS_NAME = "UserPojo";
    public static final String KEY_FIRST_NAME_COLUMN = "first_name";
    public static final String KEY_LAST_NAME_COLUMN = "last_name";
    public static final String KEY_FULL_NAME_COLUMN = "full_name";
    public static final String KEY_CITY_COLUMN = "city";
    public static final String KEY_PHONE_COLUMN = "phone";
    public static final String KEY_PROFILE_IMAGE_COLUMN = "profile_image";
    public static final String KEY_BIRTHDAY_COLUMN = "birthday";
    public static final String KEY_AGE_COLUMN = "age";

    private String id;
    private String firstName;
    private String lastName;
    private String profilePictureUrl;
    private String city;
    private String phone;
    private String text;
    private String birthday;
    private int age;


    // Mark: Constructors

    public UserPojo(String firstName, String lastName, String profilePictureUrl, String about, String phone, String birthday, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePictureUrl = profilePictureUrl;
        this.city = about;
        this.phone = phone;
        this.birthday = birthday;
        this.age = age;
    }

    public UserPojo(String firstName, String lastName, String birthday, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.age = age;
    }

    public UserPojo() {
        super();
    }

    public UserPojo(String theClassName) {
        super(theClassName);
    }

    public UserPojo(ParseObject parseObject){
        super(KEY_CLASS_NAME);
        this.id = parseObject.getObjectId();
        this.firstName = parseObject.getString(KEY_FIRST_NAME_COLUMN);
        this.lastName = parseObject.getString(KEY_LAST_NAME_COLUMN);
        this.profilePictureUrl = parseObject.getString(KEY_PROFILE_IMAGE_COLUMN);
        this.city = parseObject.getString(KEY_CITY_COLUMN);
        this.phone = parseObject.getString(KEY_PHONE_COLUMN);
        this.birthday = parseObject.getString(KEY_BIRTHDAY_COLUMN);
        this.age = parseObject.getInt(KEY_AGE_COLUMN);
    }

    // Mark: Setters/Getters

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFullname(){
        return firstName + " " + lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    // Mark: Other

    @Override
    public String toString() {
        return "UserPojo{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", profilePictureUrl='" + profilePictureUrl + '\'' +
                ", city='" + city + '\'' +
                ", birthday=" + birthday +
                ", age=" + age +
                '}';
    }

    public void saveToParse(){
        put(UserPojo.KEY_FIRST_NAME_COLUMN, firstName);
        this.put(UserPojo.KEY_LAST_NAME_COLUMN, lastName);
        this.put(UserPojo.KEY_FULL_NAME_COLUMN, getFullname());
        this.put(UserPojo.KEY_PHONE_COLUMN, phone);
        this.put(UserPojo.KEY_BIRTHDAY_COLUMN, birthday);
        this.put(UserPojo.KEY_AGE_COLUMN, age);
        this.put(UserPojo.KEY_CITY_COLUMN, city);
        this.put(UserPojo.KEY_PROFILE_IMAGE_COLUMN, profilePictureUrl);
        this.saveInBackground();
    }

    // Mark: Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.profilePictureUrl);
        dest.writeString(this.city);
        dest.writeString(this.phone);
        dest.writeString(this.text);
        dest.writeString(this.birthday);
        dest.writeInt(this.age);
    }

    protected UserPojo(Parcel in) {
        this.id = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.profilePictureUrl = in.readString();
        this.city = in.readString();
        this.phone = in.readString();
        this.text = in.readString();
        this.birthday = in.readString();
        this.age = in.readInt();
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
