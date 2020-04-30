package com.app.smarthome.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.app.smarthome.R;
import com.app.smarthome.databinding.ActivityRegisterBinding;
import com.app.smarthome.retrofit.RetrofitClient;
import com.app.smarthome.retrofit.model.main.ModelRegisterResponse;
import com.app.smarthome.util.Constants;
import com.app.smarthome.util.GlobalMethods;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements Constants {

    private static final String NAME = RegisterActivity.class.getSimpleName() + " ";
    private static final String TAG = COMMON_TAG;
    private ActivityRegisterBinding registerBinding;

    private String firstName;
    private String emailName;
    private String password;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = registerBinding.getRoot();
        setContentView(view);
        init();
    }

    private void init() {
        registerBinding.btnSignupSignup.setOnClickListener(v -> {
            if (GlobalMethods.isNetworkAvailable(this)) {
                if (validateInputsAndShowError()) {
                    callEmailRegisterApi();
                }
            } else {
                GlobalMethods.showNetworkErrorSnackBar(registerBinding.clRegisterRoot);
            }
        });

        registerBinding.tvSignupSignin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }

    private void callEmailRegisterApi() {
        Log.i(TAG, NAME + "callEmailRegisterApi called");
        GlobalMethods.showProgressBar(registerBinding.pbAll);
        gson = new Gson();

        Log.i(TAG, NAME + "register parameters:\n" + "\t" + "firstName" + "\t" + firstName + "\t" + "emailName" + "\t" + emailName + "\t" + "password" + "\t" + password);

        Call<ResponseBody> emailLoginCall = RetrofitClient.getServiceApi().register(emailName, password, firstName);

        emailLoginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, NAME + "onResponse called" + "response code: " + response.code());

                if (response.code() == 200) {
                    if (response.body() != null) {
                        try {
                            String in = response.body().string();
                            Log.i(TAG, NAME + "onResponse : email register response is \n" + in);
                            ModelRegisterResponse registerResponse = gson.fromJson(in, ModelRegisterResponse.class);

                            if (registerResponse.getStatus()) {
                                //go to login screen
                                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                //invalid details
                                Log.i(TAG, NAME + "onResponse: message is " + registerResponse.getMessage());
                                Toast.makeText(RegisterActivity.this, registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.i(TAG, NAME + "onResponse called" + "exception caught");
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.i(TAG, NAME + "onResponse: response code: " + response.code());
                    Toast.makeText(RegisterActivity.this, "invalid response code", Toast.LENGTH_SHORT).show();
                }
                GlobalMethods.hideProgressBar(registerBinding.pbAll);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, NAME + "onFailure called");
                Toast.makeText(RegisterActivity.this, "failure to register user", Toast.LENGTH_SHORT).show();
                GlobalMethods.hideProgressBar(registerBinding.pbAll);
            }
        });
    }

    private boolean validateInputsAndShowError() {
        Log.i(TAG, NAME + "validateInputAndShowError: called");

        firstName = registerBinding.etLoginFirstname.getText().toString().trim();
        emailName = registerBinding.etLoginEmail.getText().toString().trim();
        password = registerBinding.etLoginPassword.getText().toString().trim();

        if (!isFirstNameValid(firstName)) {
            registerBinding.etLoginFirstname.setError(getResources().getString(R.string.field_cannot_be_empty));
            registerBinding.etLoginFirstname.requestFocus();
            return false;
        }

        if (!isEmailValid(emailName)) {
            registerBinding.etLoginEmail.setError(getResources().getString(R.string.field_cannot_be_empty));
            registerBinding.etLoginEmail.requestFocus();
            return false;
        }

        if (!isPasswordValid(password)) {
            registerBinding.etLoginPassword.setError(getResources().getString(R.string.pass_length_error));
            registerBinding.etLoginPassword.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(String password) {
        return !TextUtils.isEmpty(password)
                && password.length() > 4;
    }

    private boolean isEmailValid(String emailName) {
        return !TextUtils.isEmpty(emailName)
                && Patterns.EMAIL_ADDRESS.matcher(emailName).matches();
    }

    private boolean isFirstNameValid(String firstName) {
        return !TextUtils.isEmpty(firstName);
    }


}
