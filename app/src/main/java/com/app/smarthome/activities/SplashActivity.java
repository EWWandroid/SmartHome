package com.app.smarthome.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.app.smarthome.R;
import com.app.smarthome.Session;
import com.app.smarthome.util.Constants;

public class SplashActivity extends AppCompatActivity implements Constants {

    private static final int SPLASH_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Session session = new Session(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isEmailLoggedIn = !session.getData(SHARED_PREFERENCE_KEY_USER_DETAIL).equals(SHARED_PREFERENCE_VALUE_ERROR);

                if (isEmailLoggedIn) {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, SPLASH_TIME);
    }
}
