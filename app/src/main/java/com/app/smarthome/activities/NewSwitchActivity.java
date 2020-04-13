package com.app.smarthome.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.app.smarthome.R;
import com.app.smarthome.adapter.SpinnerDeviceAdapter;
import com.app.smarthome.databinding.ActivityNewSwitchBinding;
import com.app.smarthome.retrofit.model.main.ModelSpinnerDevice;
import com.app.smarthome.util.Constants;
import com.app.smarthome.util.GlobalMethods;

import java.util.ArrayList;
import java.util.List;

public class NewSwitchActivity extends AppCompatActivity implements Constants {

    private static final String NAME = NewSwitchActivity.class.getSimpleName() + " ";
    private static final String TAG = COMMON_TAG;

    private ActivityNewSwitchBinding newSwitchBinding;
    private List<ModelSpinnerDevice> spinnerDeviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newSwitchBinding = ActivityNewSwitchBinding.inflate(getLayoutInflater());
        View view = newSwitchBinding.getRoot();
        setContentView(view);
        init();
    }

    private void init() {
        getSpinnerList();

        GlobalMethods.setToolbar(newSwitchBinding.toolbar, R.string.new_switch, R.drawable.ic_arrow_back_black_24dp);

        SpinnerDeviceAdapter spinnerDeviceAdapter = new SpinnerDeviceAdapter(this, spinnerDeviceList);
        newSwitchBinding.spNewswitchDevice.setAdapter(spinnerDeviceAdapter);
        newSwitchBinding.spNewswitchDevice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ModelSpinnerDevice modelSpinnerDevice = (ModelSpinnerDevice) parent.getItemAtPosition(position);
                String deviceSsid = modelSpinnerDevice.getSsid();
                String deviceMac = modelSpinnerDevice.getMac();
                Log.i(TAG, NAME + "device ssid is: " + deviceSsid + "\n deviceMac" + deviceMac);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String[] pinList = {"1", "2", "3", "4", "5", "6", "7", "8"};
        String[] typeList = {"light", "ic_fan", "plug"};

        ArrayAdapter<String> spinnerPin = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, pinList);
        ArrayAdapter<String> spinnerType = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, typeList);

        spinnerPin.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerType.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        newSwitchBinding.spNewswitchPin.setAdapter(spinnerPin);
        newSwitchBinding.spNewswitchType.setAdapter(spinnerType);
    }

    private void getSpinnerList() {
        spinnerDeviceList = new ArrayList<>();
        spinnerDeviceList.add(new ModelSpinnerDevice("iball-baton", "01:02:07:98:88"));
        spinnerDeviceList.add(new ModelSpinnerDevice("iball-baton", "01:02:07:98:88"));
        spinnerDeviceList.add(new ModelSpinnerDevice("iball-baton", "01:02:07:98:88"));
        spinnerDeviceList.add(new ModelSpinnerDevice("iball-baton", "01:02:07:98:88"));
        spinnerDeviceList.add(new ModelSpinnerDevice("iball-baton", "01:02:07:98:88"));
    }
}
