package com.app.smarthome.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.app.smarthome.activities.EditAndShareActivity;
import com.app.smarthome.adapter.FragmentRecyclerAdapter;
import com.app.smarthome.databinding.FragmentRecyclerBinding;
import com.app.smarthome.retrofit.RetrofitClient;
import com.app.smarthome.retrofit.model.main.ModelUpdateSwitchResponse;
import com.app.smarthome.retrofit.model.sub.GroupListData;
import com.app.smarthome.util.Constants;
import com.app.smarthome.util.GlobalMethods;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SwitchesFragment extends Fragment implements Constants, FragmentRecyclerAdapter.ClickListener {

    private static final String NAME = SwitchesFragment.class.getSimpleName() + " ";
    private static final String TAG = COMMON_TAG;
    private FragmentRecyclerBinding recyclerBinding;
    private Context context;
    private GroupListData groupListData;
    private Gson mGson;
    FragmentRecyclerAdapter mAdapter;

    public SwitchesFragment() {
    }

    public SwitchesFragment(Context context, GroupListData groupListData) {
        this.context = context;
        this.groupListData = groupListData;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recyclerBinding = FragmentRecyclerBinding.inflate(getLayoutInflater());
        return recyclerBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new FragmentRecyclerAdapter(context, this, groupListData.getSwitches(), FRAGMENT_HOME);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerBinding.rvFragment.setLayoutManager(gridLayoutManager);
        recyclerBinding.rvFragment.setAdapter(mAdapter);
    }

    @Override
    public void onSingleClick(int position) {
        Log.i(TAG, NAME + "onClick: " + position);
        callToUpdateSwitchStateApi(position);
    }

    @Override
    public void onLongClick(int switch_id) {
        Log.i(TAG, NAME + "onDoubleClick " + "called: position is: " + switch_id);
        Intent editAndShareIntent = new Intent(context, EditAndShareActivity.class);
        editAndShareIntent.putExtra(FRAGMENT_SWITCH_INTENT_KEY_SWITCHID, switch_id);
        getActivity().startActivity(editAndShareIntent);
    }

    private void callToUpdateSwitchStateApi(int position) {
        Log.i(TAG, NAME + "callToUpdateSwitchStateApi" + "called: ");
        GlobalMethods.showProgressBar(recyclerBinding.pbAll);
        mGson = new Gson();
        String token = GlobalMethods.getToken(context);
        int switchId = groupListData.getSwitches().get(position).getId();
        int currentState = groupListData.getSwitches().get(position).getState();
        Log.i(TAG, NAME + "callToUpdateSwitchStateApi " + "current state is: " + currentState);
        int requestState = currentState == 1 ? 0 : 1;
        Log.i(TAG, NAME + "callToUpdateSwitchStateApi " + "request state is: " + requestState);

        Call<ResponseBody> updateSwitchStateCall = RetrofitClient.getServiceApi().updateSwitchState(token, switchId, requestState);
        updateSwitchStateCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, NAME + "onResponse called response code: " + response.code());
                if (response.code() == 200) {
                    if (response.body() != null) {
                        try {
                            String in = response.body().string();
                            Log.i(TAG, NAME + "onResponse : switch changed response is \n" + in);
                            ModelUpdateSwitchResponse updateSwitchResponse = mGson.fromJson(in, ModelUpdateSwitchResponse.class);
                            Log.i(TAG, NAME + "onResponse: status is=" + updateSwitchResponse.getStatus());
                            if (updateSwitchResponse.getStatus()) {
                                updateSwitchData();
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
                GlobalMethods.hideProgressBar(recyclerBinding.pbAll);
            }

            private void updateSwitchData() {
                groupListData.getSwitches().get(position).setState(requestState);
                mAdapter.notifyItemChanged(position);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, NAME + "onFailure called");
                Toast.makeText(context, "unable to make connection to server", Toast.LENGTH_SHORT).show();
                GlobalMethods.hideProgressBar(recyclerBinding.pbAll);
            }
        });
    }

}
