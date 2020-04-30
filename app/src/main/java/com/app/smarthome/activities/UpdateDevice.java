package com.app.smarthome.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.app.smarthome.R;
import com.app.smarthome.databinding.ActivityUpdateDeviceBinding;
import com.app.smarthome.util.Constants;

public class UpdateDevice extends AppCompatActivity implements Constants {

    private static final String NAME = UpdateDevice.class.getSimpleName() + " ";
    private static final String TAG = COMMON_TAG;
    private ActivityUpdateDeviceBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateDeviceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.i(TAG, NAME + "onCreate " + "called: ");
        init();
    }

    private void init() {
        Log.i(TAG, NAME + "init " + "called: ");
    }
}
