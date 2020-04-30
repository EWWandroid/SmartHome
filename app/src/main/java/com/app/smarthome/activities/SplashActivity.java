package com.app.smarthome.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.app.smarthome.R;
import com.app.smarthome.Session;
import com.app.smarthome.databinding.ActivitySplashBinding;
import com.app.smarthome.util.Constants;

public class SplashActivity extends AppCompatActivity implements Constants {

    ActivitySplashBinding splashBinding;
    private static final int SPLASH_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = splashBinding.getRoot();
        setContentView(view);

        Session session = new Session(this);

            new Handler().postDelayed(() -> {
                boolean isEmailLoggedIn = !session.getData(SHARED_PREFERENCE_KEY_USER_DETAIL).equals(SHARED_PREFERENCE_VALUE_ERROR);
                Intent intent;
                if (isEmailLoggedIn) {
                    intent = new Intent(SplashActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                }
                startActivity(intent);
            }, SPLASH_TIME);
    }
}
