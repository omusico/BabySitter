package com.ohad.babysitter.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.ohad.babysitter.R;
import com.parse.ui.ParseLoginBuilder;

import java.util.Arrays;

/**
 * Created by Ohad on 04/12/2015.
 *
 */
public class SplashActivity extends AppCompatActivity {

    private static String TAG = SplashActivity.class.getName();
    private static long SLEEP_TIME = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);    // Removes title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash);

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(intent);
//        IntentLauncher launcher = new IntentLauncher();
//        launcher.start();
    }

    private class IntentLauncher extends Thread {
        @Override
        /**
         * Sleep for some time and than start new activity.
         */
        public void run() {
            try {
                // Sleeping
                Thread.sleep(SLEEP_TIME * 1000);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            ParseLoginBuilder builder = new ParseLoginBuilder(SplashActivity.this);
            Intent parseLoginIntent = builder.setAppLogo(R.drawable.login_logo)
                    .setParseLoginEnabled(true)
                    .setParseLoginButtonText(getString(R.string.login_go))
                    .setParseSignupButtonText(getString(R.string.login_register))
                    .setParseLoginHelpText(getString(R.string.login_forgot_pass))
                    .setParseLoginInvalidCredentialsToastText(getString(R.string.login_invalid_details))
                    .setParseLoginEmailAsUsername(true)
                    .setParseSignupSubmitButtonText(getString(R.string.login_submit_reg))
                    .setFacebookLoginEnabled(true)
                    .setFacebookLoginButtonText(getString(R.string.login_facebook))
                    .setFacebookLoginPermissions(Arrays.asList("public_profile", "user_friends"))
                    .setTwitterLoginEnabled(true)
                    .setTwitterLoginButtontext(getString(R.string.login_twitter))
                    .build();
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
