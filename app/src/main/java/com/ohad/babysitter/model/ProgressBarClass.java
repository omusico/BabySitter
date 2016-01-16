package com.ohad.babysitter.model;

import android.app.ProgressDialog;
import android.content.Context;

import com.ohad.babysitter.R;

public class ProgressBarClass {

	public static ProgressDialog bar;

	public static void startLoading(Context context){
		bar = new ProgressDialog(context);
		bar.setMessage(context.getString(R.string.gen_loading));
		bar.setIndeterminate(true);
		bar.setCancelable(false);
		bar.show();
	}

	public static void dismissLoading(){
		if(bar != null)
			if(bar.isShowing())
				bar.dismiss();
	}


}
