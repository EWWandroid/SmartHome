package com.app.smarthome.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.smarthome.R;
import com.app.smarthome.databinding.ActivityAddNewDeviceBinding;
import com.app.smarthome.retrofit.RetrofitClient;
import com.app.smarthome.retrofit.model.main.ModelCreateSwitchResponse;
import com.app.smarthome.retrofit.model.sub.CreateSwitchData;
import com.app.smarthome.util.Constants;
import com.app.smarthome.util.GlobalMethods;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewDeviceActivity extends AppCompatActivity implements Constants {

    private static final String NAME = AddNewDeviceActivity.class.getSimpleName();
    private static final String TAG = COMMON_TAG;

    private Gson mGson;
    private String name, mac, code;

    private ActivityAddNewDeviceBinding addNewDeviceBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addNewDeviceBinding = ActivityAddNewDeviceBinding.inflate(getLayoutInflater());
        View view = addNewDeviceBinding.getRoot();
        setContentView(view);
        init();
    }

    private void init() {
        setToolbar();
        setToolbarClickListeners();
        setListeners();
    }

    private void setListeners() {
        addNewDeviceBinding.ivAddnewdeviceScannow.setOnClickListener(v -> startActivity(new Intent(AddNewDeviceActivity.this, QrScannerActivity.class)));
        addNewDeviceBinding.btnUpdate.setOnClickListener(v -> {
            if (GlobalMethods.isNetworkAvailable(this)) {
                if (validateInputAndShowError()) callToCreateDeviceApi(name, mac, code);
            } else {
                GlobalMethods.showNetworkErrorSnackBar(addNewDeviceBinding.clAddnewdeviceRoot);
            }
        });
    }

    private void callToCreateDeviceApi(String name, String mac, String code) {
        Log.i(TAG, NAME + "callToCreateDeviceApi " + "called: ");
        GlobalMethods.showProgressBar(addNewDeviceBinding.pb);
        mGson = new Gson();
        String token = GlobalMethods.getToken(this);
        Call<ResponseBody> createNewDevice = RetrofitClient.getServiceApi().createDevices(token, name, mac, code);
        createNewDevice.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, NAME + "onResponse " + "called: ");
                if (response.code() == 200) {
                    if (response.body() != null) {
                        try {
                            String in = response.body().string();
                            Log.i(TAG, NAME + "onResponse : create new device response is \n" + in);
                            ModelCreateSwitchResponse createSwitchResponse = mGson.fromJson(in, ModelCreateSwitchResponse.class);
                            Log.i(TAG, NAME + "onResponse: status is=" + createSwitchResponse.getStatus());
                            if (createSwitchResponse.getStatus()) {
                                CreateSwitchData createSwitchData = createSwitchResponse.getData();
                                giveFeedback(R.string.device_created);
                            } else {
                                giveFeedback(R.string.unable_to_add_new_device);
                            }
                        } catch (Exception e) {
                            Log.i(TAG, NAME + "onResponse called" + "exception caught");
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.i(TAG, NAME + "onResponse: response code: " + response.code());
                    giveFeedback(R.string.unable_to_add_new_device);
                }
                GlobalMethods.hideProgressBar(addNewDeviceBinding.pb);
            }

            private void giveFeedback(int message) {
                Toast.makeText(AddNewDeviceActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, NAME + "onFailure " + "called: ");
                giveFeedback(R.string.unable_to_add_new_device);
            }
        });
    }

    private void setToolbarClickListeners() {
        Log.i(TAG, NAME + "setToolbarClickListeners: ");
        addNewDeviceBinding.toolbar.ivTbStart.setOnClickListener(v -> {
            Log.i(TAG, NAME + "clicked back button");
            Toast.makeText(this, "clicked back button", Toast.LENGTH_SHORT).show();
        });
    }

    private void setToolbar() {
        addNewDeviceBinding.toolbar.ivTbStart.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        addNewDeviceBinding.toolbar.tvTbCenter.setText(R.string.add_new_device);
    }

    private boolean validateInputAndShowError() {
        Log.i(TAG, NAME + "validateInputAndShowError: called");

        name = addNewDeviceBinding.etEditandshareName.getText().toString().trim();
        mac = addNewDeviceBinding.etEditandshareMac.getText().toString().trim();
        code = addNewDeviceBinding.etEditandshareCode.getText().toString().trim();

        if (!isNameValid(name)) {
            addNewDeviceBinding.etEditandshareName
                    .setError(getResources().getString(R.string.device_name_not_valid));
            addNewDeviceBinding.etEditandshareName.requestFocus();
            return false;
        }

        if (!isCodeValid(code)) {
            addNewDeviceBinding.etEditandshareCode
                    .setError(getResources().getString(R.string.code_not_valid));
            addNewDeviceBinding.etEditandshareCode.requestFocus();
            return false;
        }

        if (!isMacValid(mac)) {
            addNewDeviceBinding.etEditandshareMac
                    .setError(getResources().getString(R.string.mac_not_valid));
            addNewDeviceBinding.etEditandshareMac.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isCodeValid(String code) {
        return !TextUtils.isEmpty(code);
    }

    private boolean isMacValid(String mac) {
        return !TextUtils.isEmpty(mac);
    }

    private boolean isNameValid(String name) {
        return !TextUtils.isEmpty(name);
    }
}
