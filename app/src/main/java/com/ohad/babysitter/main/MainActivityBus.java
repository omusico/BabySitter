package com.ohad.babysitter.main;

import android.support.annotation.IntDef;

import com.ohad.babysitter.pojo.UserPojo;
import com.ohad.babysitter.utility.Constant;

/**
 * Created by Ohad on 28/12/2015.
 *
 */
public class MainActivityBus {

    public static final int ACTION_ADD_AD = 386;

    private @AD_TYPES int adType;
    private @ACTION_TYPE int actionType;
    private UserPojo ad;


    @IntDef({Constant.AD_TYPE_EMPLOYEE, Constant.AD_TYPE_EMPLOYER})
    public @interface AD_TYPES {}

    @IntDef({ACTION_ADD_AD})
    public @interface ACTION_TYPE {}


    public MainActivityBus(@AD_TYPES int adType, @ACTION_TYPE int actionType) {
        this.adType = adType;
        this.actionType = actionType;
    }

    public int getAdType() {
        return adType;
    }

    public void setAdType(@AD_TYPES int adType) {
        this.adType = adType;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(@ACTION_TYPE int actionType) {
        this.actionType = actionType;
    }

    public UserPojo getAd() {
        return ad;
    }

    public void setAd(UserPojo ad) {
        this.ad = ad;
    }


}
