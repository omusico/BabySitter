package com.ohad.babysitter.main.Ad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ohad.babysitter.R;
import com.ohad.babysitter.base.dialog.DialogBase;
import com.ohad.babysitter.pojo.UserPojo;
import com.ohad.babysitter.model.pickers.GenderPicker;
import com.ohad.babysitter.utility.RealPathUtil;
import com.ohad.babysitter.utility.Utility;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.File;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddAdDialog extends DialogBase implements View.OnClickListener {

    private static final int ACTION_REQUEST_GALLERY = 10;
    private static final int ACTION_REQUEST_CAMERA = 20;

    private CircleImageView mCivProfile;
    private EditText mEtFirstName, mEtLastName, mEtCity, mEtAge, mEtPhone, mEtAbout, mEtSalary, mEtEmail;
    private TextView mTvGender;

    private AddUserCallback mCallback;
    private Context mContext;
    private ParseFile mImageParseFile;
    private Bitmap mCompressedBitmap;
    private String mGender;


    public interface AddUserCallback {
        void onAddUserCallbackResult(UserPojo userPojo);
    }

    public AddAdDialog(Context context, AddUserCallback callback) {
        super(context, R.style.DialogRTLTheme);
        mContext = context;
        mCallback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initViews() {
        setContentView(R.layout.dialog_add_ad);
        mCivProfile = (CircleImageView) findViewById(R.id.civProfileImage);
        mEtFirstName = (EditText) findViewById(R.id.fn_editText);
        mEtLastName = (EditText) findViewById(R.id.ln_editText);
        mEtCity = (EditText) findViewById(R.id.city_editText);
        mEtAge = (EditText) findViewById(R.id.age_editText);
        mEtPhone = (EditText) findViewById(R.id.phone_editText);
        mEtAbout = (EditText) findViewById(R.id.about_editText);
        mEtSalary = (EditText) findViewById(R.id.salary_editText);
        mEtEmail = (EditText) findViewById(R.id.email_editText);
        mTvGender = (TextView) findViewById(R.id.tvGender);
        mCivProfile.setOnClickListener(this);
        mTvGender.setOnClickListener(this);
    }

    @Override
    public void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.confirm_mToolbar);
        ImageView backIv = (ImageView) mToolbar.findViewById(R.id.confirm_back);
        ImageView doneIv = (ImageView) mToolbar.findViewById(R.id.confirm_done);
        backIv.setOnClickListener(this);
        doneIv.setOnClickListener(this);
        mToolbar.setTitle(R.string.add_ad_title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.confirm_back: //---------------------------------> Back
                dismiss();
                break;

            case R.id.confirm_done: //---------------------------------> Done
                done();
                break;

            case R.id.civProfileImage:
                showMediaChooserDialog();
                break;

            case R.id.tvGender:
                actionGenderPicker();
                break;

        }
    }

    private void actionGenderPicker() {
        new GenderPicker(mContext, new GenderPicker.GenderPickerCallback() {
            @Override
            public void onGenderPicked(String gender) {
                mTvGender.setText(gender);
                mGender = gender;
            }
        }).show();
    }

    private void done() {
        final boolean valid = checkValidation();

        if (valid) {
            final String firstName = mEtFirstName.getText().toString();
            final String lastName = mEtLastName.getText().toString();
            final String fullName = firstName + " " + lastName;

            UserPojo userPojo = new UserPojo(UserPojo.KEY_CLASS_NAME);
            userPojo.setFirstName(firstName);
            userPojo.setLastName(lastName);
            userPojo.setFullName(fullName);
            userPojo.setCity(mEtCity.getText().toString());
            userPojo.setAge(mEtAge.getText().toString());
            userPojo.setPhone(mEtPhone.getText().toString());
            userPojo.setAbout(mEtAbout.getText().toString());
            userPojo.setSalary(mEtSalary.getText().toString());
            userPojo.setEmail(mEtEmail.getText().toString());
            userPojo.setCreated(Calendar.getInstance().getTimeInMillis());
            userPojo.setBitmap(mCompressedBitmap);
            userPojo.setGender(mGender);
            userPojo.setUserId(ParseUser.getCurrentUser().getObjectId());
            userPojo.setPictureFile(mImageParseFile);
            Utility.setGeoPointToUserPojo(userPojo);
            mCallback.onAddUserCallbackResult(userPojo);
            dismiss();
        }
    }

    private void showMediaChooserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.intent_media_title);
        builder.setItems(new CharSequence[] {
                mContext.getString(R.string.gen_galleery),
                mContext.getString(R.string.gen_camera)
        }, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: //-----------------------> GET IMAGE FROM THE GALLERY
                        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        galleryIntent.setType("image/*");
                        Intent chooser = Intent.createChooser(galleryIntent, mContext.getString(R.string.intent_media_gallery_title));

                        if (galleryIntent.resolveActivity(mContext.getPackageManager()) != null) {
                            ((Activity) mContext).startActivityForResult(chooser, ACTION_REQUEST_GALLERY);
                        }
                        break;

                    case 1: //-----------------------> GET IMAGE FROM THE CAMERA
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        if (cameraIntent.resolveActivity(mContext.getPackageManager()) != null) {
                            ((Activity) mContext).startActivityForResult(cameraIntent, ACTION_REQUEST_CAMERA);
                        }

                        break;

                }
            }
        }).show();
    }

    private boolean checkValidation() {
        if (mEtFirstName.getText().toString().isEmpty()) {
            Toast.makeText(mContext, R.string.add_ad_invalid_first_name, Toast.LENGTH_SHORT).show();
            return false;
        } else if (mEtLastName.getText().toString().isEmpty()) {
            Toast.makeText(mContext, R.string.add_ad_invalid_last_name, Toast.LENGTH_SHORT).show();
            return false;
        } else if (mGender == null) {
            Toast.makeText(mContext, R.string.add_ad_invalid_gender, Toast.LENGTH_SHORT).show();
            return false;
        } else if (mEtCity.getText().toString().isEmpty()) {
            Toast.makeText(mContext, R.string.add_ad_invalid_city, Toast.LENGTH_SHORT).show();
            return false;
        } else if (mEtAge.getText().toString().isEmpty()) {
            Toast.makeText(mContext, R.string.add_ad_invalid_age, Toast.LENGTH_SHORT).show();
            return false;
        } else if (mEtPhone.getText().toString().isEmpty()) {
            Toast.makeText(mContext, R.string.add_ad_invalid_phone, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            final String fileNamePostfix = String.valueOf(Calendar.getInstance().getTimeInMillis());

            Uri imageUri = data.getData();
            mCivProfile.setImageURI(imageUri);

            switch (requestCode) {
                case ACTION_REQUEST_GALLERY:
                    try {
                        File file = new File(RealPathUtil.getRealPathFromURI(mContext, imageUri));

                        mCompressedBitmap = Utility.decodeFile(file, 150);
                        mImageParseFile = new ParseFile(file);
                        mImageParseFile = Utility.uploadParseFile(mCompressedBitmap, fileNamePostfix);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case ACTION_REQUEST_CAMERA:

                    break;
            }

        }
    }



}


