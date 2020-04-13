package com.app.smarthome.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.smarthome.R;
import com.app.smarthome.activities.AddNewDeviceActivity;
import com.app.smarthome.adapter.FragmentRecyclerAdapter;
import com.app.smarthome.retrofit.RetrofitClient;
import com.app.smarthome.retrofit.model.main.ModelDeviceListResponse;
import com.app.smarthome.retrofit.model.sub.DevicesListData;
import com.app.smarthome.util.Constants;
import com.app.smarthome.util.GlobalMethods;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DevicesFragment extends Fragment implements Constants, FragmentRecyclerAdapter.ClickListener {

    private static final String NAME = DevicesFragment.class.getSimpleName();
    private static final String TAG = COMMON_TAG;
    private Context context;

    private Gson gson;
    private ProgressBar pb_all;
    private ConstraintLayout cl_devicesfrag_root;

    private RecyclerView rv_fragment;

    public DevicesFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_devices, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setToolbar();
        rv_fragment = view.findViewById(R.id.rv_fragment);
        pb_all = view.findViewById(R.id.pb_all);
        cl_devicesfrag_root = view.findViewById(R.id.cl_devicesfrag_root);

        if (GlobalMethods.isNetworkAvailable(context)) {
            callToListDevices();
        } else {
            GlobalMethods.showNetworkErrorSnackBar(cl_devicesfrag_root);
        }

        FloatingActionButton fab_adddevice = view.findViewById(R.id.fab_adddevice);
        fab_adddevice.setOnClickListener(v -> startActivity(new Intent(context, AddNewDeviceActivity.class)));
    }

    private void callToListDevices() {
        Log.i(TAG, NAME + "callToListDevices: called");
        GlobalMethods.showProgressBar(pb_all);
        gson = new Gson();
        String token = GlobalMethods.getToken(context);
        Log.i(TAG, NAME + "token value is: " + token);

        Call<ResponseBody> groupListCall = RetrofitClient.getServiceApi().deviceList(token);

        groupListCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, NAME + "onResponse called response code: " + response.code());
                if (response.code() == 200) {
                    if (response.body() != null) {
                        try {
                            String in = response.body().string();
                            Log.i(TAG, NAME + "onResponse : device list response is \n" + in);
                            ModelDeviceListResponse deviceListResponse = gson.fromJson(in, ModelDeviceListResponse.class);
                            Log.i(TAG, NAME + "onResponse: status is=" + deviceListResponse.getStatus());
                            if (deviceListResponse.getStatus()) {
                                List<DevicesListData> data = deviceListResponse.getData();
                                setAdapter(data);
                            } else {
                                //invalid details
                                Toast.makeText(context, "status is false", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.i(TAG, NAME + "onResponse called" + "exception caught");
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.i(TAG, NAME + "onResponse: response code: " + response.code());
                    Toast.makeText(context, "invalid response code", Toast.LENGTH_SHORT).show();
                }
                GlobalMethods.hideProgressBar(pb_all);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, NAME + "onFailure called");
                Toast.makeText(context, "unable to make connection to server", Toast.LENGTH_SHORT).show();
                GlobalMethods.hideProgressBar(pb_all);
            }
        });
    }

    private void setAdapter(List<DevicesListData> data) {
        FragmentRecyclerAdapter adapter = new FragmentRecyclerAdapter(context, this, FRAGMENT_DEVICE, data);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        rv_fragment.setLayoutManager(gridLayoutManager);
        rv_fragment.setAdapter(adapter);
    }

    @Override
    public void onClick(int position) {

    }

    public void setToolbar() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        if (toolbar != null) {
            TextView tv_tb_center = toolbar.findViewById(R.id.tv_tb_center);
            tv_tb_center.setText(R.string.devices);
        }
    }
}
