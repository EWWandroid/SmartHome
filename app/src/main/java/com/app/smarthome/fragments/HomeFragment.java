package com.app.smarthome.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.smarthome.R;
import com.app.smarthome.adapter.HomePagerAdapter;
import com.app.smarthome.databinding.FragmentHomeBinding;
import com.app.smarthome.retrofit.RetrofitClient;
import com.app.smarthome.retrofit.model.main.ModelGroupsListResponse;
import com.app.smarthome.retrofit.model.sub.GroupListData;
import com.app.smarthome.util.Constants;
import com.app.smarthome.util.GlobalMethods;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements Constants {

    private static final String NAME = HomeFragment.class.getSimpleName() + " ";
    private static final String TAG = COMMON_TAG;

    private Gson gson;

    private ProgressBar pb_all;
    private TabLayout tl_homefrag;
    private ViewPager2 vp_homefrag;
    private HomePagerAdapter pagerAdapter;

    private Context context;

    public HomeFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentHomeBinding homeBinding = FragmentHomeBinding.inflate(getLayoutInflater());
        return homeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tl_homefrag = view.findViewById(R.id.tl_homefrag);
        vp_homefrag = view.findViewById(R.id.vp_homefrag);
        ConstraintLayout cl_homefrag_root = view.findViewById(R.id.cl_homefrag_root);
        pb_all = view.findViewById(R.id.pb_all);

        if (GlobalMethods.isNetworkAvailable(context)) {
            callToListGroups();
        } else {
            GlobalMethods.showNetworkErrorSnackBar(cl_homefrag_root);
        }
    }

    private void callToListGroups() {
        Log.i(TAG, NAME + "callToListGroups: called");
        GlobalMethods.showProgressBar(pb_all);
        gson = new Gson();
        String token = GlobalMethods.getToken(context);
        Log.i(TAG, NAME + "token value is: " + token);

        Call<ResponseBody> groupListCall = RetrofitClient.getServiceApi().groupList(token);

        groupListCall.enqueue(new Callback<ResponseBody>() {
            int totalTabCount = 0;
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, NAME + "onResponse called response code: " + response.code());

                if (response.code() == 200) {
                    if (response.body() != null) {
                        try {
                            String in = response.body().string();
                            Log.i(TAG, NAME + "onResponse : group list response is \n" + in);
                            ModelGroupsListResponse listGroupsResponse = gson.fromJson(in, ModelGroupsListResponse.class);
                            Log.i(TAG, NAME + "onResponse: status is=" + listGroupsResponse.getStatus());
                            if (listGroupsResponse.getStatus()) {
                                setTabItems(listGroupsResponse.getData());
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

            private void setTabItems(List<GroupListData> dataList) {
                if (dataList != null) {
                    List<String> tabNameList = new ArrayList<>();
                    totalTabCount = dataList.size();
                    for (int tabNum = 0; tabNum < totalTabCount; tabNum++) {
                        GroupListData data = dataList.get(tabNum);
                        if (data != null) {
                            String tabName = data.getName();
                            tabNameList.add(tabName);
                        }
                    }
                    Log.i(TAG, NAME + "tabs are\n" + tabNameList);
                    setViewPager(tabNameList, dataList);
                    if (totalTabCount == 2) {
                        tl_homefrag.setTabMode(TabLayout.MODE_FIXED);
                    } else {
                        tl_homefrag.setTabMode(TabLayout.MODE_SCROLLABLE);
                    }
                }
            }

            private void setViewPager(List<String> tabNameList, List<GroupListData> dataList) {
                pagerAdapter = new HomePagerAdapter(HomeFragment.this, context, totalTabCount, dataList);
                vp_homefrag.setAdapter(pagerAdapter);
                vp_homefrag.setOffscreenPageLimit(1);

                new TabLayoutMediator(tl_homefrag, vp_homefrag,
                        (tab, position) -> {
                            for (int i = 0; i < tabNameList.size(); i++) {
                                tab.setText(tabNameList.get(position));
                            }
                        }).attach();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, NAME + "onFailure called");
                Toast.makeText(context, "unable to make connection to server", Toast.LENGTH_SHORT).show();
                GlobalMethods.hideProgressBar(pb_all);
            }
        });

    }
}
