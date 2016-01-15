package com.ohad.babysitter.main.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ohad.babysitter.base.fragment.FragmentBase;
import com.ohad.babysitter.utility.Utility;

/**
 * Created by Ohad on 27/12/2015.
 *
 */
public abstract class FragmentMain extends FragmentBase {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Utility.d("FragmentMain onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.d("FragmentMain onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Utility.d("FragmentMain onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Utility.d("FragmentMain onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Utility.d("FragmentMain onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Utility.d("FragmentMain onResume");
    }

}
