package com.ohad.babysitter.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.ohad.babysitter.R;
import com.ohad.babysitter.utility.LocationManagerSingleton;
import com.ohad.babysitter.utility.Utility;
import com.parse.ParseUser;

/**
 * Created by Ohad on 04/12/2015.
 *
 */
public class SplashActivity extends AppCompatActivity {

    private String TAG = SplashActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);    // Removes title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash);

        // get geo point
        LocationManagerSingleton.getInstance().findLocation();

        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null) { // if there is a user go to MainActivity.
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            SplashActivity.this.startActivity(intent);
            finish();
        } else { // show splash screen and start login process.
            IntentLauncher launcher = new IntentLauncher();
            launcher.start();
        }

    }

    private class IntentLauncher extends Thread {
        @Override
        /**
         * Sleep for some time and than start new activity.
         */
        public void run() {
            try {
                // Sleeping
                short SLEEP_TIME = 1;
                Thread.sleep(SLEEP_TIME * 1000);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            Intent parseLoginIntent = Utility.getLoginIntent(SplashActivity.this);
            startActivityForResult(parseLoginIntent, 0);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            SplashActivity.this.startActivity(intent);
        }

        SplashActivity.this.finish();
    }

}
