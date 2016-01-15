package com.ohad.babysitter.utility;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.ohad.babysitter.R;
import com.ohad.babysitter.model.OnTouchSelector;

/**
 * Created by Ohad on 26/12/2015.
 *
 */
public class GenderPicker {

    private AlertDialog mAlertDialog;
    private GenderPickerCallback mCallback;


    public interface GenderPickerCallback {
        void onGenderPicked(String gender);
    }

    public GenderPicker(Context context, GenderPickerCallback callback) {
        mCallback = callback;

        View view = View.inflate(context, R.layout.picker_gender, null);
        final TextView tvMale = (TextView) view.findViewById(R.id.tvMale);
        final TextView tvFemale = (TextView) view.findViewById(R.id.tvFemale);

        mAlertDialog = new AlertDialog.Builder(context)
                .setCancelable(true)
                .setView(view)
                .create();

        final OnTouchSelector selector = new OnTouchSelector(context, R.color.toolbar, R.color.transparent);

        tvMale.setOnClickListener(new OnGenderPickedListener(context.getString(R.string.hint_male)));
        tvMale.setOnTouchListener(selector);

        tvFemale.setOnClickListener(new OnGenderPickedListener(context.getString(R.string.hint_female)));
        tvFemale.setOnTouchListener(selector);
    }

    public void show() {
        mAlertDialog.show();
    }

    private class OnGenderPickedListener implements View.OnClickListener{

        private String mGender;

        public OnGenderPickedListener(String gender) {
            mGender = gender;
        }

        @Override
        public void onClick(View v) {
            mCallback.onGenderPicked(mGender);
            mAlertDialog.dismiss();
        }
    }


}
