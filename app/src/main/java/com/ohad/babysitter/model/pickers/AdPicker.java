package com.ohad.babysitter.model.pickers;

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
public class AdPicker {

    private AlertDialog mAlertDialog;
    private AdPickerCallback mCallback;
    private Context mContext;


    public interface AdPickerCallback {
        void onAdPicked(int ad);
    }

    public AdPicker(Context context, AdPickerCallback callback) {
        mContext = context;
        mCallback = callback;

        View view = View.inflate(mContext, R.layout.picker_ad, null);
        final TextView tvManuel = (TextView) view.findViewById(R.id.tvManuel);
        final TextView tvAuto = (TextView) view.findViewById(R.id.tvAuto);
        final TextView tvSos = (TextView) view.findViewById(R.id.tvSos);

        mAlertDialog = new AlertDialog.Builder(mContext)
                .setCancelable(true)
                .setView(view)
                .create();

        final OnTouchSelector selector = new OnTouchSelector(mContext, R.color.toolbar, R.color.transparent);

        tvManuel.setOnClickListener(new OnGenderPickedListener(R.string.hint_manuel));
        tvManuel.setOnTouchListener(selector);

        tvAuto.setOnClickListener(new OnGenderPickedListener(R.string.hint_auto));
        tvAuto.setOnTouchListener(selector);

        tvSos.setOnClickListener(new OnGenderPickedListener(R.string.hint_sos));
        tvSos.setOnTouchListener(selector);
    }

    public void show() {
        mAlertDialog.show();
    }

    private class OnGenderPickedListener implements View.OnClickListener{

        private int mAd;

        public OnGenderPickedListener(int ad) {
            mAd = ad;
        }

        @Override
        public void onClick(View v) {
            mCallback.onAdPicked(mAd);
            mAlertDialog.dismiss();
        }
    }


}
