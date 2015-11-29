package com.ohad.babysitter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.ohad.babysitter.base.dialog.DialogBase;

public class AddUserDialog extends DialogBase implements View.OnClickListener {

    private AddUserCallback mCallback;
    private Context mContext;


    public interface AddUserCallback {
        void onAddUserCallbackResult(UserPojo userPojo);
    }


    public AddUserDialog(Context context, AddUserCallback callback) {
        super(context, R.style.DialogRTLTheme);
        mContext = context;
        mCallback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initViews() {
        setContentView(R.layout.dialog_add_user);
    }

    @Override
    public void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.confirm_mToolbar);
        ImageView backIv = (ImageView) mToolbar.findViewById(R.id.confirm_back);
        ImageView doneIv = (ImageView) mToolbar.findViewById(R.id.confirm_done);
        backIv.setOnClickListener(this);
        doneIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.confirm_back: //---------------------------------Back
                dismiss();
                break;

            case R.id.confirm_done: //---------------------------------Done

                break;
        }
    }


}


