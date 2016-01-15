package com.ohad.babysitter.model;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;

import com.ohad.babysitter.R;

/**
 * Created by Ohad on 16/01/2016.
 *
 */
public class OnTouchSelector implements View.OnTouchListener {

    private Context mContext;
    private int mActionDownColor, mActionUpColor;

    public OnTouchSelector(Context mContext, int mActionDownColor, int mActionUpColor) {
        this.mContext = mContext;
        this.mActionDownColor = mActionDownColor;
        this.mActionUpColor = mActionUpColor;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                v.setBackgroundColor(ContextCompat.getColor(mContext, mActionDownColor));
                break;

            case MotionEvent.ACTION_UP:
                v.setBackgroundColor(ContextCompat.getColor(mContext, mActionUpColor));
                break;
        }
        return false;
    }
}
