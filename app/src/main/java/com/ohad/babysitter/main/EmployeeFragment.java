package com.ohad.babysitter.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.ohad.babysitter.R;
import com.ohad.babysitter.base.ApplicationBase;
import com.ohad.babysitter.pojo.UserPojo;
import com.ohad.babysitter.utility.Constant;
import com.ohad.babysitter.utility.ParseErrorHandler;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ohad on 27/12/2015.
 *
 */
public class EmployeeFragment extends FragmentMain {

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
    }

    private void initList() {
        mUsers = new ArrayList<>();
        mAdapter = new AdAdapter(getActivity(), mUsers);
        mListView.setAdapter(mAdapter);
    }

    private void refresh() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(UserPojo.KEY_CLASS_NAME);
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

                    Collections.reverse(mUsers);
                    //Collections.sort(mUsers);
                    mAdapter.notifyDataSetChanged();
                } else { // Query failed
                    ParseErrorHandler.handleParseError(getActivity(), e);
                }
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
                        userPojo.saveToParse();
                        mUsers.add(0, userPojo);
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Cannot create ad. Reason:\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

}
