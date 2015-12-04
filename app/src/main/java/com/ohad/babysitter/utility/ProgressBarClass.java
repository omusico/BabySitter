package com.ohad.babysitter.utility;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressBarClass {

	public static ProgressDialog bar;

	public static void startLoading(Context context){
		bar = new ProgressDialog(context);
		bar.setMessage("Loading...");
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
