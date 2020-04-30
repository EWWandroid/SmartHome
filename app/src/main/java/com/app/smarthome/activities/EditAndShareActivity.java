package com.app.smarthome.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.Toast;

import com.app.smarthome.R;
import com.app.smarthome.adapter.ShareableUserRecyclerAdapter;
import com.app.smarthome.adapter.SpinnerDeviceAdapter;
import com.app.smarthome.databinding.ActivityEditAndShareBinding;
import com.app.smarthome.retrofit.RetrofitClient;
import com.app.smarthome.retrofit.model.main.ModelGetShareableUserListResponse;
import com.app.smarthome.retrofit.model.main.ModelGetSwitchResponse;
import com.app.smarthome.retrofit.model.main.ModelShareSwitchResponse;
import com.app.smarthome.retrofit.model.main.ModelSpinnerDevice;
import com.app.smarthome.retrofit.model.sub.GetSwitchData;
import com.app.smarthome.retrofit.model.sub.ShareableUserData;
import com.app.smarthome.util.Constants;
import com.app.smarthome.util.GlobalMethods;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAndShareActivity extends AppCompatActivity implements Constants, ShareableUserRecyclerAdapter.OnShareableItemClickListener {

    private static final String NAME = EditAndShareActivity.class.getSimpleName() + " ";
    private static final String TAG = COMMON_TAG;
    private ActivityEditAndShareBinding editAndShareBinding;
    private Gson mGson;
    private List<ShareableUserData> mShareableUserDataList;
    private ShareableUserRecyclerAdapter mShareableUserRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editAndShareBinding = ActivityEditAndShareBinding.inflate(getLayoutInflater());
        setContentView(editAndShareBinding.getRoot());
        init();
    }

    private void init() {
        Log.i(TAG, NAME + "init" + "called: ");
        setToolbar();
        showDetails();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        Log.i(TAG, NAME + "setOnClickListeners " + "called: ");
        editAndShareBinding.btnEditandshareUpdate.setOnClickListener(v -> {
            if (GlobalMethods.isNetworkAvailable(this)) callToUpdateSwitchApi();
            else GlobalMethods.showNetworkErrorSnackBar(editAndShareBinding.clEditandshareRoot);
        });

        editAndShareBinding.toolbar.ivTbStart.setOnClickListener(v -> onBackPressed());
    }

    private void callToUpdateSwitchApi() {
        Log.i(TAG, NAME + "callToUpdateSwitchApi " + "called: ");
        GlobalMethods.showProgressBar(editAndShareBinding.pb);
        mGson = new Gson();
        String token = GlobalMethods.getToken(this);


    }

    private void showDetails() {
        if (getIntent() != null && getIntent().hasExtra(FRAGMENT_SWITCH_INTENT_KEY_SWITCHID)) {
            int switchId = getIntent().getIntExtra(FRAGMENT_SWITCH_INTENT_KEY_SWITCHID, -1);
            if (GlobalMethods.isNetworkAvailable(this)) {
                callToGetSwitchApi(switchId);
                callToGetShareableUserList(switchId);
            } else {
                GlobalMethods.showNetworkErrorSnackBar(editAndShareBinding.clEditandshareRoot);
            }
        }
    }

    private void setToolbar() {
        GlobalMethods.setToolbar(editAndShareBinding.toolbar, R.string.title_edit_switch, R.drawable.ic_arrow_back_black_24dp);
    }

    @Override
    public void onSwitchClicked(int adapterPosition, ShareableUserData shareableUserData, String switchId, Switch swhRecyclerShareSwitchShare) {
        Log.i(TAG, NAME + "onSwitchClicked " + "called: ");
        Log.i(TAG, NAME + "onSwitchClicked " + "clicked on: " + adapterPosition);
        String userId = shareableUserData.getId().toString();
        String userSwitchId = shareableUserData.getSwitchId();
        boolean isSwitchShared = !TextUtils.isEmpty(userSwitchId);
        Log.i(TAG, NAME + "onSwitchClicked " + "is switch shared?" + isSwitchShared);
        if (isSwitchShared) {
            if (GlobalMethods.isNetworkAvailable(this)) {
                callToUnSharedSwitchApi(adapterPosition, switchId, userId, swhRecyclerShareSwitchShare);
            } else {
                GlobalMethods.showNetworkErrorSnackBar(editAndShareBinding.clEditandshareRoot);
            }
        } else {
            if (GlobalMethods.isNetworkAvailable(this)) {
                callToShareSwitchApi(adapterPosition, switchId, userId, swhRecyclerShareSwitchShare);
            } else {
                GlobalMethods.showNetworkErrorSnackBar(editAndShareBinding.clEditandshareRoot);
            }
        }
    }

    private void callToGetShareableUserList(int switchId) {
        Log.i(TAG, NAME + "callToGetShareableUserList " + "called: ");
        GlobalMethods.showProgressBar(editAndShareBinding.pb);
        mGson = new Gson();
        String token = GlobalMethods.getToken(this);
        Call<ResponseBody> shareableUserListCall = RetrofitClient.getServiceApi().getShareableUserList(token, switchId);
        shareableUserListCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, NAME + "onResponse called response code: " + response.code());
                if (response.code() == 200) {
                    if (response.body() != null) {
                        try {
                            String in = response.body().string();
                            Log.i(TAG, NAME + "onResponse : shareable user list response is \n" + in);
                            ModelGetShareableUserListResponse getShareableUserListResponse = mGson.fromJson(in, ModelGetShareableUserListResponse.class);
                            Log.i(TAG, NAME + "onResponse: status is=" + getShareableUserListResponse.getStatus());
                            if (getShareableUserListResponse.getStatus()) {
                                mShareableUserDataList = getShareableUserListResponse.getData();
                                displayShareableUserList(mShareableUserDataList, String.valueOf(switchId));
                            } else {
                                //invalid details
                                Toast.makeText(EditAndShareActivity.this, "status is false", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.i(TAG, NAME + "onResponse called" + "exception caught");
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.i(TAG, NAME + "onResponse: response code: " + response.code());
                    Toast.makeText(EditAndShareActivity.this, "invalid response code", Toast.LENGTH_SHORT).show();
                }
                GlobalMethods.hideProgressBar(editAndShareBinding.pb);
            }

            private void displayShareableUserList(List<ShareableUserData> shareableUserDataList, String switchId) {
                mShareableUserRecyclerAdapter = new ShareableUserRecyclerAdapter(shareableUserDataList, EditAndShareActivity.this, EditAndShareActivity.this, switchId);
                editAndShareBinding.rvEditandshare.setLayoutManager(new LinearLayoutManager(EditAndShareActivity.this));
                editAndShareBinding.rvEditandshare.setAdapter(mShareableUserRecyclerAdapter);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, NAME + "onFailure called");
                Toast.makeText(EditAndShareActivity.this, "unable to make connection to server", Toast.LENGTH_SHORT).show();
                GlobalMethods.hideProgressBar(editAndShareBinding.pb);
            }
        });
    }

    private void callToShareSwitchApi(int adapterPosition, String switchId, String userId, Switch swhRecyclerShareSwitchShare) {
        Log.i(TAG, NAME + "callToShareSwitchApi " + "called: ");
        Log.i(TAG, NAME + "callToShareSwitchApi " + "user id: " + userId + " switchId: " + switchId);
        GlobalMethods.showProgressBar(editAndShareBinding.pb);
        mGson = new Gson();
        String token = GlobalMethods.getToken(this);
        Call<ResponseBody> shareSwitchCall = RetrofitClient.getServiceApi().shareSwitch(token, switchId, userId);
        shareSwitchCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, NAME + "onResponse called response code: " + response.code());
                if (response.code() == 200) {
                    if (response.body() != null) {
                        try {
                            String in = response.body().string();
                            Log.i(TAG, NAME + "onResponse : share switch response is \n" + in);
                            ModelShareSwitchResponse shareSwitchResponse = mGson.fromJson(in, ModelShareSwitchResponse.class);
                            Log.i(TAG, NAME + "onResponse: status is=" + shareSwitchResponse.getStatus());
                            if (shareSwitchResponse.getStatus()) {
                                toggleSwitch(swhRecyclerShareSwitchShare);
                                Toast.makeText(EditAndShareActivity.this, shareSwitchResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.i(TAG, NAME + "onResponse " + shareSwitchResponse.getMessage());
                            } else {
                                //invalid details
                                Log.i(TAG, NAME + "onResponse " + shareSwitchResponse.getMessage());
                                Toast.makeText(EditAndShareActivity.this, shareSwitchResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.i(TAG, NAME + "onResponse called" + "exception caught");
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.i(TAG, NAME + "onResponse: response code: " + response.code());
                    Toast.makeText(EditAndShareActivity.this, "invalid response code", Toast.LENGTH_SHORT).show();
                }
                GlobalMethods.hideProgressBar(editAndShareBinding.pb);
            }

            private void toggleSwitch(Switch swhRecyclerShareSwitchShare) {
                Log.i(TAG, NAME + "toggleSwitch " + "called: ");
                //swhRecyclerShareSwitchShare.setChecked(true);
                mShareableUserDataList.get(adapterPosition).setSwitchId(switchId);
                Log.i(TAG, NAME + "toggleSwitch " + "mShareableUserDataList: position: " + adapterPosition
                        + " value: " + mShareableUserDataList.get(adapterPosition).getSwitchId());
                mShareableUserRecyclerAdapter.notifyItemChanged(adapterPosition);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, NAME + "onFailure called");
                Toast.makeText(EditAndShareActivity.this, "unable to make connection to server", Toast.LENGTH_SHORT).show();
                GlobalMethods.hideProgressBar(editAndShareBinding.pb);
            }
        });
    }

    private void callToUnSharedSwitchApi(int adapterPosition, String switchId, String userId, Switch swhRecyclerShareSwitchShare) {
        Log.i(TAG, NAME + "callToUnsharedSwitchApi " + "called: ");
        Log.i(TAG, NAME + "callToUnSharedSwitchApi " + "user id: " + userId + " switchId: " + switchId);
        GlobalMethods.showProgressBar(editAndShareBinding.pb);
        mGson = new Gson();
        String token = GlobalMethods.getToken(this);
        Call<ResponseBody> unShareSwitchCall = RetrofitClient.getServiceApi().unSharedSwitch(token, switchId, userId);
        unShareSwitchCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, NAME + "onResponse called response code: " + response.code());
                if (response.code() == 200) {
                    if (response.body() != null) {
                        try {
                            String in = response.body().string();
                            Log.i(TAG, NAME + "onResponse : unshare switch response is \n" + in);
                            ModelShareSwitchResponse shareSwitchResponse = mGson.fromJson(in, ModelShareSwitchResponse.class);
                            Log.i(TAG, NAME + "onResponse: status is=" + shareSwitchResponse.getStatus());
                            if (shareSwitchResponse.getStatus()) {
                                toggleSwitch(swhRecyclerShareSwitchShare);
                                Toast.makeText(EditAndShareActivity.this, shareSwitchResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.i(TAG, NAME + "onResponse " + shareSwitchResponse.getMessage());
                            } else {
                                //invalid details
                                Log.i(TAG, NAME + "onResponse " + shareSwitchResponse.getMessage());
                                Toast.makeText(EditAndShareActivity.this, shareSwitchResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.i(TAG, NAME + "onResponse called" + "exception caught");
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.i(TAG, NAME + "onResponse: response code: " + response.code());
                    Toast.makeText(EditAndShareActivity.this, "invalid response code", Toast.LENGTH_SHORT).show();
                }
                GlobalMethods.hideProgressBar(editAndShareBinding.pb);
            }

            private void toggleSwitch(Switch swhRecyclerShareSwitchShare) {
                Log.i(TAG, NAME + "toggleSwitch " + "called: ");
                //swhRecyclerShareSwitchShare.setChecked(false);
                mShareableUserDataList.get(adapterPosition).setSwitchId("");
                Log.i(TAG, NAME + "toggleSwitch " + "mShareableUserDataList: position: " + adapterPosition
                        + " value: " + mShareableUserDataList.get(adapterPosition).getSwitchId());
                mShareableUserRecyclerAdapter.notifyItemChanged(adapterPosition);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, NAME + "onFailure called");
                Toast.makeText(EditAndShareActivity.this, "unable to make connection to server", Toast.LENGTH_SHORT).show();
                GlobalMethods.hideProgressBar(editAndShareBinding.pb);
            }
        });
    }

    private void callToGetSwitchApi(int switchId) {
        Log.i(TAG, NAME + "callToGetSwitchApi " + "called: ");
        GlobalMethods.showProgressBar(editAndShareBinding.pb);
        mGson = new Gson();
        String token = GlobalMethods.getToken(this);
        Call<ResponseBody> getSwitchCall = RetrofitClient.getServiceApi().getSwitch(token, switchId);
        getSwitchCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, NAME + "onResponse called response code: " + response.code());
                if (response.code() == 200) {
                    if (response.body() != null) {
                        try {
                            String in = response.body().string();
                            Log.i(TAG, NAME + "onResponse : get switch response is \n" + in);
                            ModelGetSwitchResponse getSwitchResponse = mGson.fromJson(in, ModelGetSwitchResponse.class);
                            Log.i(TAG, NAME + "onResponse: status is=" + getSwitchResponse.getStatus());
                            if (getSwitchResponse.getStatus()) {
                                GetSwitchData switchData = getSwitchResponse.getData();
                                setSwitchDetails(switchData);
                            } else {
                                //invalid details
                                Toast.makeText(EditAndShareActivity.this, "status is false", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.i(TAG, NAME + "onResponse called" + "exception caught");
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.i(TAG, NAME + "onResponse: response code: " + response.code());
                    Toast.makeText(EditAndShareActivity.this, "invalid response code", Toast.LENGTH_SHORT).show();
                }
                GlobalMethods.hideProgressBar(editAndShareBinding.pb);
            }

            private void setSwitchDetails(GetSwitchData switchData) {
                setUpSpinner();
                if (switchData != null) {
                    String switchName = switchData.getName();
                    String switchPin = switchData.getPin();
                    String switchType = switchData.getType();
                    editAndShareBinding.etEditandshareName.setText(switchName);
                    editAndShareBinding.spNewswitchPin.setSelection(Integer.parseInt(switchPin));
                    int type = switchType.equals("light") ? 0 : switchType.equals("ic_fan") ? 1 : switchType.equals("plug") ? 2 : 0;
                    editAndShareBinding.spNewswitchType.setSelection(type);
                }
            }

            private void setUpSpinner() {
                SpinnerDeviceAdapter spinnerDeviceAdapter = new SpinnerDeviceAdapter(EditAndShareActivity.this, getSpinnerList());
                editAndShareBinding.spNewswitchDevice.setAdapter(spinnerDeviceAdapter);
                editAndShareBinding.spNewswitchDevice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

                ArrayAdapter<String> spinnerPin = new ArrayAdapter<String>(EditAndShareActivity.this, R.layout.support_simple_spinner_dropdown_item, pinList);
                ArrayAdapter<String> spinnerType = new ArrayAdapter<String>(EditAndShareActivity.this, R.layout.support_simple_spinner_dropdown_item, typeList);

                spinnerPin.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spinnerType.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

                editAndShareBinding.spNewswitchPin.setAdapter(spinnerPin);
                editAndShareBinding.spNewswitchType.setAdapter(spinnerType);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, NAME + "onFailure called");
                Toast.makeText(EditAndShareActivity.this, "unable to make connection to server", Toast.LENGTH_SHORT).show();
                GlobalMethods.hideProgressBar(editAndShareBinding.pb);
            }

            private List<ModelSpinnerDevice> getSpinnerList() {
                List<ModelSpinnerDevice> spinnerDeviceList = new ArrayList<>();
                spinnerDeviceList.add(new ModelSpinnerDevice("iball-baton"));
                spinnerDeviceList.add(new ModelSpinnerDevice("iball-baton"));
                spinnerDeviceList.add(new ModelSpinnerDevice("iball-baton"));
                spinnerDeviceList.add(new ModelSpinnerDevice("iball-baton"));
                spinnerDeviceList.add(new ModelSpinnerDevice("iball-baton"));
                return spinnerDeviceList;
            }
        });
    }

}
