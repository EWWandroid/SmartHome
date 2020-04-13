package com.app.smarthome.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.app.smarthome.R;
import com.app.smarthome.Session;
import com.app.smarthome.databinding.ActivityLoginBinding;
import com.app.smarthome.retrofit.RetrofitClient;
import com.app.smarthome.retrofit.model.main.ModelLoginResponse;
import com.app.smarthome.util.Constants;
import com.app.smarthome.util.GlobalMethods;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements Constants {

    private static final String NAME = LoginActivity.class.getSimpleName()+ " ";
    private static final String TAG = COMMON_TAG;

    private ActivityLoginBinding loginBinding;
    private Gson gson;
    private Session session;

    private String password;
    private String emailName;
    ModelLoginResponse loginResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = loginBinding.getRoot();
        setContentView(view);
        init();
    }

    private void init() {
        loginBinding.btnLoginSignin.setOnClickListener(v -> {
            if (GlobalMethods.isNetworkAvailable(this)) {
                if (validateInputsAndShowError()) {
                    callEmailRegisterApi();
                }
            } else {
                GlobalMethods.showNetworkErrorSnackBar(loginBinding.clLoginRoot);
            }
        });
        loginBinding.tvLoginSignup.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

    }

    private void callEmailRegisterApi() {
        Log.i(TAG, NAME + "callEmailRegisterApi called");
        GlobalMethods.showProgressBar(loginBinding.pbAll);
        gson = new Gson();
        session = new Session(this);

        Log.i(TAG, NAME + "login parameters:\n" + "emailName" + "\t" + emailName + "\t" + "password" + "\t" + password);

        Call<ResponseBody> emailLoginCall = RetrofitClient.getServiceApi().login(emailName, password);

        emailLoginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, NAME + "onResponse called" + "response code: " + response.code());

                if (response.code() == 200) {
                    if (response.body() != null) {
                        try {
                            String in = response.body().string();
                            Log.i(TAG, NAME + "onResponse : email register response is \n" + in);
                            loginResponse = gson.fromJson(in, ModelLoginResponse.class);

                            if (loginResponse.getStatus()) {
                                //go to home screen
                                storeUserDetail(loginResponse);
                                goToHomeActivity();
                            } else {
                                Log.i(TAG, NAME + "onResponse: message is " + loginResponse.getMessage());
                                Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.i(TAG, NAME + "onResponse called" + "exception caught");
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.i(TAG, NAME + "onResponse: response code: " + response.code());
                    Toast.makeText(LoginActivity.this, "invalid response code", Toast.LENGTH_SHORT).show();
                }
                GlobalMethods.hideProgressBar(loginBinding.pbAll);
            }

            private void goToHomeActivity() {
                Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(homeIntent);
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, NAME + "onFailure called");
                Toast.makeText(LoginActivity.this, "failure to register user", Toast.LENGTH_SHORT).show();
                GlobalMethods.hideProgressBar(loginBinding.pbAll);
            }
        });
    }


    private boolean validateInputsAndShowError() {
        Log.i(TAG, NAME + "validateInputAndShowError: called");

        password = loginBinding.etLoginPassword.getText().toString().trim();
        emailName = loginBinding.etLoginEmail.getText().toString().trim();

        if (!isEmailValid(emailName)) {
            loginBinding.etLoginEmail.setError(getResources().getString(R.string.email_not_valid));
            loginBinding.etLoginEmail.requestFocus();
            return false;
        }

        if (!isPasswordValid(password)) {
            loginBinding.etLoginPassword.setError(getResources().getString(R.string.field_cannot_be_empty));
            loginBinding.etLoginPassword.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(String password) {
        return !TextUtils.isEmpty(password);
    }

    private boolean isEmailValid(String emailName) {
        return !TextUtils.isEmpty(emailName);
    }

    private void storeUserDetail(ModelLoginResponse loginResponse) {
        session = new Session(LoginActivity.this);
        session.putData(SHARED_PREFERENCE_KEY_USER_DETAIL, gson.toJson(loginResponse));
    }


}