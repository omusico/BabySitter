package com.ohad.babysitter.pojo;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.ohad.babysitter.utility.Utility;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import org.apache.commons.lang3.StringUtils;

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
    public static final String KEY_ABOUT_COLUMN = "about";
    public static final String KEY_SALARY_COLUMN = "salary";
    public static final String KEY_EMAIL_COLUMN = "email";
    public static final String KEY_PICTURE_COLUMN = "picture_file";

    private String id;
    private String firstName;
    private String lastName;
    private String profilePictureUrl;
    private String city;
    private String phone;
    private String about;
    private String birthday;
    private String email;
    private int age;
    private int salary;

    private Bitmap bitmap;


    // Mark: Constructors


    public UserPojo() {
        super(KEY_CLASS_NAME);
    }

    public UserPojo(String theClassName) {
        super(theClassName);
    }

    public UserPojo(ParseObject parseObject){
        super(KEY_CLASS_NAME);
        this.id = parseObject.getObjectId();
        this.firstName = parseObject.getString(KEY_FIRST_NAME_COLUMN);
        this.lastName = parseObject.getString(KEY_LAST_NAME_COLUMN);
        //this.profilePictureUrl = parseObject.getString(KEY_PROFILE_IMAGE_COLUMN);
        this.city = parseObject.getString(KEY_CITY_COLUMN);
        this.phone = parseObject.getString(KEY_PHONE_COLUMN);
        this.birthday = parseObject.getString(KEY_BIRTHDAY_COLUMN);
        this.age = parseObject.getInt(KEY_AGE_COLUMN);
        this.email = parseObject.getString(KEY_EMAIL_COLUMN);
        this.salary = parseObject.getInt(KEY_SALARY_COLUMN);

        final ParseFile parseFile = parseObject.getParseFile(KEY_PICTURE_COLUMN);
        this.profilePictureUrl = parseFile.getUrl();

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

    public void setAge(String age) {
        this.age = StringUtils.isNumeric(age) ? Integer.parseInt(age) : 0;
    }

    public String getFullname(){
        return firstName + " " + lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = Utility.isValidPhoneNumber(phone) ? phone : "X";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public void setSalary(String salary) {
        this.salary = StringUtils.isNumeric(salary) ? Integer.valueOf(salary) : 0;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = Utility.isEmailValid(email) ? email : "X";
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    // Mark: Other

    @Override
    public String toString() {
        return "UserPojo{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", profilePictureUrl='" + profilePictureUrl + '\'' +
                ", city='" + city + '\'' +
                ", phone='" + phone + '\'' +
                ", about='" + about + '\'' +
                ", birthday='" + birthday + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", salary=" + salary +
                '}';
    }

    public void saveToParse(){
        put(UserPojo.KEY_FIRST_NAME_COLUMN, firstName == null ? "" : firstName);
        put(UserPojo.KEY_LAST_NAME_COLUMN, lastName == null ? "" : lastName);
        put(UserPojo.KEY_FULL_NAME_COLUMN, getFullname() == null ? "" : getFullname());
        put(UserPojo.KEY_PHONE_COLUMN, phone == null ? "" : phone);
        put(UserPojo.KEY_BIRTHDAY_COLUMN, birthday == null ? "" : birthday);
        put(UserPojo.KEY_AGE_COLUMN, age);
        put(UserPojo.KEY_CITY_COLUMN, city == null ? "" : city);
        put(UserPojo.KEY_PROFILE_IMAGE_COLUMN, profilePictureUrl == null ? "" : profilePictureUrl);
        put(UserPojo.KEY_ABOUT_COLUMN, about == null ? "" : about);
        put(UserPojo.KEY_EMAIL_COLUMN, email == null ? "" : email);
        put(UserPojo.KEY_SALARY_COLUMN, salary);
        saveInBackground();
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
        dest.writeString(this.about);
        dest.writeString(this.birthday);
        dest.writeString(this.email);
        dest.writeInt(this.age);
        dest.writeInt(this.salary);
        dest.writeParcelable(this.bitmap, 0);
    }

    protected UserPojo(Parcel in) {
        this.id = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.profilePictureUrl = in.readString();
        this.city = in.readString();
        this.phone = in.readString();
        this.about = in.readString();
        this.birthday = in.readString();
        this.email = in.readString();
        this.age = in.readInt();
        this.salary = in.readInt();
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
