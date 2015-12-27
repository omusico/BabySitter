package com.ohad.babysitter.base.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.ohad.babysitter.base.activity.ActivityBaseInterface;

/**
 * Created by Ohad on 27/11/2015.
 *
 */
public abstract class FragmentBase extends Fragment implements FragmentBaseInterface {

    public FragmentBaseInterface mInterface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInterface = this;
    }

}
