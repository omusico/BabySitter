package com.ohad.babysitter.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ohad.babysitter.R;
import com.ohad.babysitter.main.fragments.EmployeeFragment;
import com.ohad.babysitter.main.fragments.EmployerFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private String tabs[];

    public MainPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        tabs = new String[]{
                context.getString(R.string.main_pager_tab_first_name),
                context.getString(R.string.main_pager_tab_second_name)};
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {

            case 0:
                return new EmployeeFragment();

            case 1:
                return new EmployerFragment();

        }
        return null;
    }


}
