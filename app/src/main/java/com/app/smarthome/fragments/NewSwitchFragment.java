package com.app.smarthome.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.app.smarthome.R;
import com.app.smarthome.adapter.SpinnerDeviceAdapter;
import com.app.smarthome.model.ModelSpinnerDevice;
import com.app.smarthome.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class NewSwitchFragment extends Fragment implements Constants {

    private static final String NAME = NewSwitchFragment.class.getSimpleName();
    private static final String TAG = COMMON_TAG;

    private List<ModelSpinnerDevice> spinnerDeviceList;
    private SpinnerDeviceAdapter spinnerDeviceAdapter;
    private Context context;

    public NewSwitchFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_switch, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList();
        Spinner sp_addswitch_device = view.findViewById(R.id.sp_addswitch_device);
        Spinner sp_addswitch_pin = view.findViewById(R.id.sp_addswitch_pin);
        Spinner sp_addswitch_type = view.findViewById(R.id.sp_addswitch_type);

        spinnerDeviceAdapter = new SpinnerDeviceAdapter(context, spinnerDeviceList);
        sp_addswitch_device.setAdapter(spinnerDeviceAdapter);
        sp_addswitch_device.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        ArrayAdapter spinnerPin = new ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, pinList);
        ArrayAdapter spinnerType = new ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, typeList);

        spinnerPin.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerType.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        sp_addswitch_pin.setAdapter(spinnerPin);
        sp_addswitch_type.setAdapter(spinnerType);

    }

    private void initList() {
        spinnerDeviceList = new ArrayList<>();
        spinnerDeviceList.add(new ModelSpinnerDevice("iball-baton", "01:02:07:98:88"));
        spinnerDeviceList.add(new ModelSpinnerDevice("iball-baton", "01:02:07:98:88"));
        spinnerDeviceList.add(new ModelSpinnerDevice("iball-baton", "01:02:07:98:88"));
        spinnerDeviceList.add(new ModelSpinnerDevice("iball-baton", "01:02:07:98:88"));
        spinnerDeviceList.add(new ModelSpinnerDevice("iball-baton", "01:02:07:98:88"));
    }
}

