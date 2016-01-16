package com.ohad.babysitter.main.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.ohad.babysitter.R;
import com.ohad.babysitter.base.ApplicationBase;
import com.ohad.babysitter.main.Ad.AdAdapter;
import com.ohad.babysitter.main.MainActivityBus;
import com.ohad.babysitter.main.MainActivityInterface;
import com.ohad.babysitter.parse.ParseErrorHandler;
import com.ohad.babysitter.pojo.UserPojo;
import com.ohad.babysitter.utility.Constant;
import com.ohad.babysitter.utility.LocationManagerSingleton;
import com.ohad.babysitter.utility.Utility;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ohad on 27/12/2015.
 *
 */
public class EmployeeFragment extends FragmentMain implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipe;
    private ListView mListView;
    private List<UserPojo> mUsers;
    private AdAdapter mAdapter;
    private MainActivityInterface mInterface;


    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ApplicationBase.getInstance().getBus().register(this);
        View view = View.inflate(getActivity(), R.layout.list_fragment, null);
        initViews(view);
        initList();
        refresh();
        return view;
    }

    private void initViews(View view) {
        mListView = (ListView) view.findViewById(R.id.main_listView);
        mSwipe = (SwipeRefreshLayout) view.findViewById(R.id.mSwipe);
        mSwipe.setOnRefreshListener(this);
    }

    private void initList() {
        mUsers = new ArrayList<>();
        mAdapter = new AdAdapter(getActivity(), mUsers);
        mListView.setAdapter(mAdapter);
    }

    private void refresh() {
        mSwipe.setRefreshing(true);
        ParseQuery<ParseObject> query = ParseQuery.getQuery(UserPojo.KEY_CLASS_NAME);

        final ParseGeoPoint currentGeoPoint = LocationManagerSingleton.getInstance().getParseGeoPoint();
        final int distanceKm = LocationManagerSingleton.getInstance().getDistanceKm();

        if (currentGeoPoint != null) {
            query.whereWithinKilometers(UserPojo.KEY_LOCATION_COLUMN, currentGeoPoint, distanceKm);
        } else {
            Toast.makeText(getActivity(), R.string.notify_location_wasnt_found, Toast.LENGTH_LONG).show();
        }

        query.orderByDescending(UserPojo.KEY_CREATED_COLUMN);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) { // Query successful
                    mUsers.clear();

                    for (int i = 0; i < objects.size(); i++) {
                        ParseObject parseObject = objects.get(i);
                        UserPojo userPojo = new UserPojo(parseObject);
                        mUsers.add(userPojo);
                    }

                    mAdapter.notifyDataSetChanged();
                } else { // Query failed
                    ParseErrorHandler.handleParseError(getActivity(), e);
                }
                mSwipe.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mInterface != null) {
            mInterface.onFragmentResumed(this);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mInterface = (MainActivityInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement MainActivityInterface");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ApplicationBase.getInstance().getBus().unregister(this);
    }

    @Subscribe
    public void onEventAdAdded(MainActivityBus bus) {
        if (bus.getAdType() == Constant.AD_TYPE_EMPLOYER){
            return;
        }

        switch (bus.getActionType()){
            case MainActivityBus.ACTION_ADD_AD:
                try {
                    final UserPojo userPojo = bus.getAd();
                    if (userPojo != null) {
                        userPojo.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null)
                                    Utility.d(e.getMessage());
                            }
                        });
                        mUsers.add(0, userPojo);
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), getString(R.string.error_cant_create_ad) + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    @Override
    public void onRefresh() {
        refresh();
    }

}
