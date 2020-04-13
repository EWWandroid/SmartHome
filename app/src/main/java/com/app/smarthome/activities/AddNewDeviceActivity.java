package com.app.smarthome.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.smarthome.R;
import com.app.smarthome.Session;
import com.app.smarthome.databinding.ActivityAddNewDeviceBinding;
import com.app.smarthome.retrofit.RetrofitClient;
import com.app.smarthome.retrofit.model.main.ModelLoginResponse;
import com.app.smarthome.util.Constants;
import com.app.smarthome.util.GlobalMethods;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewDeviceActivity extends AppCompatActivity implements Constants {

    private static final String NAME = AddNewDeviceActivity.class.getSimpleName();
    private static final String TAG = COMMON_TAG;

    private Gson gson;
    private String name, mac, code;

    private ActivityAddNewDeviceBinding addNewDeviceBinding;
    private ProgressBar pb_all;
    private ConstraintLayout cl_addnewdevice_root;
    private ImageView iv_tb_start;
    private CircularImageView civ_tb_end;
    private TextView tv_tb_center;



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

    private void callToAddNewDevice() {
        Log.i(TAG, NAME + "callToAddNewDevice: ");
        gson = new Gson();
        GlobalMethods.showProgressBar(pb_all);

        if (validateInputAndShowError()) {
            /*todo: complete api integration*/
        }
    }

    private boolean validateInputAndShowError() {
        Log.i(TAG, NAME + "validateInputAndShowError: called");

        name = addNewDeviceBinding.etAddnewdeviceName.getText().toString().trim();
        mac = addNewDeviceBinding.etAddnewdeviceMac.getText().toString().trim();
        code = addNewDeviceBinding.etAddnewdeviceCode.getText().toString().trim();

        if (!isNameValid(name)) {
            addNewDeviceBinding.etAddnewdeviceName.setError(getResources().getString(R.string.email_not_valid));
            addNewDeviceBinding.etAddnewdeviceName.requestFocus();
            return false;
        }

        if (!isMacValid(mac)) {
            addNewDeviceBinding.etAddnewdeviceMac.setError(getResources().getString(R.string.field_cannot_be_empty));
            addNewDeviceBinding.etAddnewdeviceMac.requestFocus();
            return false;
        }
        if (!isCodeValid(code)) {
            addNewDeviceBinding.etAddnewdeviceCode.setError(getResources().getString(R.string.field_cannot_be_empty));
            addNewDeviceBinding.etAddnewdeviceCode.requestFocus();
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
