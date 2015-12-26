package com.ohad.babysitter.utility;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.ohad.babysitter.R;

/**
 * Created by Ohad on 26/12/2015.
 *
 */
public class GenderPicker {

    private AlertDialog mAlertDialog;
    private GenderPickerCallback mCallback;
    private Context mContext;


    public interface GenderPickerCallback {
        void onGenderPicked(String gender);
    }

    public GenderPicker(Context context, GenderPickerCallback callback) {
        mContext = context;
        mCallback = callback;

        View view = View.inflate(mContext, R.layout.picker_gender, null);
        final TextView tvMale = (TextView) view.findViewById(R.id.tvMale);
        final TextView tvFemale = (TextView) view.findViewById(R.id.tvFemale);

        mAlertDialog = new AlertDialog.Builder(mContext)
                .setCancelable(true)
                .setView(view)
                .create();

        tvMale.setOnClickListener(new OnGenderPickedListener(mContext.getString(R.string.hint_male)));
        tvMale.setOnTouchListener(onTouchListener);

        tvFemale.setOnClickListener(new OnGenderPickedListener(mContext.getString(R.string.hint_female)));
        tvFemale.setOnTouchListener(onTouchListener);
    }

    public void show() {
        mAlertDialog.show();
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue_primary));
                    break;

                case MotionEvent.ACTION_UP:
                    v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));
                    break;
            }
            return false;
        }
    };

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
