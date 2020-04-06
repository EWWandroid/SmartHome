package com.app.smarthome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.smarthome.R;
import com.app.smarthome.model.ModelSpinnerDevice;

import java.util.List;

public class SpinnerDeviceAdapter extends ArrayAdapter<ModelSpinnerDevice> {

    private Context mContext;
    private List<ModelSpinnerDevice> devicesList;

    public SpinnerDeviceAdapter(Context mContext, List<ModelSpinnerDevice> devicesList) {
        super(mContext, 0, devicesList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.custom_spinner_device, parent, false
            );
        }

        TextView spinner_device_mac = convertView.findViewById(R.id.spinner_device_mac);
        TextView spinner_device_ssid = convertView.findViewById(R.id.spinner_device_ssid);

        ModelSpinnerDevice spinnerDevice = getItem(position);

        if (spinnerDevice != null) {
            spinner_device_mac.setText(spinnerDevice.getMac());
            spinner_device_ssid.setText(spinnerDevice.getSsid());
        }
        return convertView;
    }
}
