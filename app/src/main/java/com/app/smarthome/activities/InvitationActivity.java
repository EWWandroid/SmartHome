package com.app.smarthome.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.smarthome.adapter.InvitedToRecyclerAdapter;
import com.app.smarthome.adapter.InvitedByRecyclerAdapter;
import com.app.smarthome.databinding.ActivityInvitationBinding;
import com.app.smarthome.retrofit.RetrofitClient;
import com.app.smarthome.retrofit.model.main.ModelAcceptInvitationResponse;
import com.app.smarthome.retrofit.model.main.ModelInvitationListResponse;
import com.app.smarthome.retrofit.model.main.ModelInvitedListResponse;
import com.app.smarthome.retrofit.model.sub.InvitationList;
import com.app.smarthome.retrofit.model.sub.InviteData;
import com.app.smarthome.util.Constants;
import com.app.smarthome.util.GlobalMethods;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvitationActivity extends AppCompatActivity implements Constants, InvitedByRecyclerAdapter.OnRecyclerInvitationItemClickListener, InvitedToRecyclerAdapter.OnRecyclerFriendsItemClickListener {

    private ActivityInvitationBinding invitationBinding;
    private static final String TAG = COMMON_TAG;
    private static final String NAME = InvitationActivity.class.getSimpleName() + " ";

    private List<InvitationList> mFriendsDataList = new ArrayList<>();
    private InvitedByRecyclerAdapter mInvitedByRecyclerAdapter;
    private InvitedToRecyclerAdapter mInvitedToRecyclerAdapter;
    private List<InvitationList> mInvitedByOthersList;
    private List<InviteData> mInvitedToOthersList;

    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        invitationBinding = ActivityInvitationBinding.inflate(getLayoutInflater());
        View view = invitationBinding.getRoot();
        setContentView(view);

        if (GlobalMethods.isNetworkAvailable(this)) {
            callToGetInvitedToApi();
            callToGetInvitedByApi();
        } else {
            GlobalMethods.showNetworkErrorSnackBar(invitationBinding.clInvitationRoot);
        }

    }

    @Override
    public void onRejectClick(InvitationList invitationListData, int position) {
        if (invitationListData != null) {
            if (GlobalMethods.isNetworkAvailable(InvitationActivity.this)) {
                callToRejectInvitationApi(invitationListData, position);
            } else {
                GlobalMethods.showNetworkErrorSnackBar(invitationBinding.clInvitationRoot);
            }
        }
    }

    @Override
    public void onAcceptClick(InvitationList invitationListData, int position, Button btn_invitationlist_accept) {
        if (invitationListData != null) {
            if (GlobalMethods.isNetworkAvailable(InvitationActivity.this)) {
                callToAcceptInvitationApi(invitationListData, position, btn_invitationlist_accept);
            } else {
                GlobalMethods.showNetworkErrorSnackBar(invitationBinding.clInvitationRoot);
            }
        }
    }

    @Override
    public void onRejectClick(InviteData inviteData, int position) {
        if (inviteData != null) {
            if (GlobalMethods.isNetworkAvailable(this)) {
                callToRejectInvitationApi(inviteData, position);
            } else {
                GlobalMethods.showNetworkErrorSnackBar(invitationBinding.clInvitationRoot);
            }
        }
    }

    //    @Override
//    public void onClickItem(InvitationList invitationListData, int position) {
//        Toast.makeText(this, invitationListData.toString() + "\n" + position, Toast.LENGTH_SHORT).show();
//        Log.i(TAG, NAME + invitationListData.toString() + "\n" + position);
//    }

    private void callToGetInvitedByApi() {
        Log.i(TAG, NAME + "callToGetInvitedByApi called:  ");
        GlobalMethods.showProgressBar(invitationBinding.pbAll);
        gson = new Gson();
        String token = GlobalMethods.getToken(this);
        Call<ResponseBody> invitationListCall = RetrofitClient.getServiceApi().getInvitedByList(token);

        invitationListCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, NAME + "onResponse called response code: " + response.code());
                if (response.code() == 200) {
                    if (response.body() != null) {
                        try {
                            String in = response.body().string();
                            Log.i(TAG, NAME + "onResponse : invitation list response is \n" + in);
                            ModelInvitationListResponse invitationListResponse = gson.fromJson(in, ModelInvitationListResponse.class);
                            Log.i(TAG, NAME + "onResponse: status is=" + invitationListResponse.getStatus());
                            if (invitationListResponse.getStatus()) {
                                mInvitedByOthersList = invitationListResponse.getData();
                                showInvitedByOthersList(mInvitedByOthersList);
                            } else {
                                //invalid details
                                Toast.makeText(InvitationActivity.this, "status is false", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.i(TAG, NAME + "onResponse called" + "exception caught");
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.i(TAG, NAME + "onResponse: response code: " + response.code());
                    Toast.makeText(InvitationActivity.this, "invalid response code", Toast.LENGTH_SHORT).show();
                }
                GlobalMethods.hideProgressBar(invitationBinding.pbAll);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, NAME + "onFailure called");
                Toast.makeText(InvitationActivity.this, "unable to make connection to server", Toast.LENGTH_SHORT).show();
                GlobalMethods.hideProgressBar(invitationBinding.pbAll);
            }
        });
    }

    private void callToGetInvitedToApi() {
        Log.i(TAG, NAME + "callToGetInvitedToApi called:  ");
        GlobalMethods.showProgressBar(invitationBinding.pbAll);
        gson = new Gson();
        String token = GlobalMethods.getToken(this);
        Call<ResponseBody> invitedListCall = RetrofitClient.getServiceApi().getInvitedToList(token);
        invitedListCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, NAME + "onResponse called response code: " + response.code());
                if (response.code() == 200) {
                    if (response.body() != null) {
                        try {
                            String in = response.body().string();
                            Log.i(TAG, NAME + "onResponse : invited to list response is \n" + in);
                            ModelInvitedListResponse invitedListResponse = gson.fromJson(in, ModelInvitedListResponse.class);
                            Log.i(TAG, NAME + "onResponse: status is=" + invitedListResponse.getStatus());
                            if (invitedListResponse.getStatus()) {
                                mInvitedToOthersList =invitedListResponse.getData();
                                showInvitedToOthersList(mInvitedToOthersList);
                            } else {
                                //invalid details
                                Toast.makeText(InvitationActivity.this, "status is false", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.i(TAG, NAME + "onResponse called" + "exception caught");
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.i(TAG, NAME + "onResponse: response code: " + response.code());
                    Toast.makeText(InvitationActivity.this, "invalid response code", Toast.LENGTH_SHORT).show();
                }
                GlobalMethods.hideProgressBar(invitationBinding.pbAll);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, NAME + "onFailure called");
                Toast.makeText(InvitationActivity.this, "unable to make connection to server", Toast.LENGTH_SHORT).show();
                GlobalMethods.hideProgressBar(invitationBinding.pbAll);
            }
        });
    }

    private void showInvitedToOthersList(List<InviteData> inviteDataList) {
        Log.i(TAG, NAME + "showInvitedToOthersList called:  ");
        if (inviteDataList != null) {
            Log.i(TAG, NAME + "invited to list is:\n" + inviteDataList.toString());
            mInvitedToRecyclerAdapter = new InvitedToRecyclerAdapter(this, inviteDataList, this);
            invitationBinding.rvFriends.setLayoutManager(new LinearLayoutManager(this));
            invitationBinding.rvFriends.setAdapter(mInvitedToRecyclerAdapter);
        }
    }

    private void showInvitedByOthersList(List<InvitationList> invitationListDataList) {
        Log.i(TAG, NAME + "showInvitedByOthersList called:  ");
        if (invitationListDataList != null) {
            Log.i(TAG, NAME + "showInvitationList called: size: " + invitationListDataList.size());
            Log.i(TAG, NAME + "invitation list is:\n" + invitationListDataList.toString());
            mInvitedByRecyclerAdapter = new InvitedByRecyclerAdapter(this, invitationListDataList, this);
            invitationBinding.rvInvitation.setLayoutManager(new LinearLayoutManager(this));
            invitationBinding.rvInvitation.setAdapter(mInvitedByRecyclerAdapter);
        }
    }

//    private void showInvitationList(List<InvitationList> invitationListDataList) {
//        Log.i(TAG, NAME + "showInvitationList called:  ");
//        if (invitationListDataList != null) {
//            mRequestedUserList = new ArrayList<>();
//            for (InvitationList userData: invitationListDataList) {
//                if (userData.getAccept() != 1) {
//                    mRequestedUserList.add(userData);
//                }
//            }
//            Log.i(TAG, NAME + "showInvitationList called: size: " + mRequestedUserList.size());
//            Log.i(TAG, NAME + "invitation list is:\n" + mRequestedUserList.toString());
//            mInvitedByRecyclerAdapter = new InvitedByRecyclerAdapter(this, mRequestedUserList, this);
//            invitationBinding.rvInvitation.setLayoutManager(new LinearLayoutManager(this));
//            invitationBinding.rvInvitation.setAdapter(mInvitedByRecyclerAdapter);
//        }
//    }
//
//    private void showFriendsList(List<InvitationList> invitationListDataList) {
//        Log.i(TAG, NAME + "showFriendsList called:  ");
//        if (invitationListDataList != null) {
//            mAcceptedUserList = new ArrayList<>();
//            for (InvitationList userData: invitationListDataList) {
//                if (userData.getAccept() == 1) {
//                    mAcceptedUserList.add(userData);
//                }
//            }
//            Log.i(TAG, NAME + "friends list is:\n" + mAcceptedUserList.toString());
//            mInvitedToRecyclerAdapter = new InvitedToRecyclerAdapter(this, mAcceptedUserList, this);
//            invitationBinding.rvFriends.setLayoutManager(new LinearLayoutManager(this));
//            invitationBinding.rvFriends.setAdapter(mInvitedToRecyclerAdapter);
//        }
//    }

    private void callToAcceptInvitationApi(InvitationList invitationListData, int position, Button btn_invitationlist_accept) {
        Log.i(TAG, NAME + "callToAcceptInvitationApi called:  ");
        int inviteId = invitationListData.getId();
        Log.i(TAG, NAME + "invite id is: " + inviteId);
        GlobalMethods.showProgressBar(invitationBinding.pbAll);
        gson = new Gson();
        String token = GlobalMethods.getToken(this);
        Call<ResponseBody> acceptInvitationCall = RetrofitClient.getServiceApi().acceptInvitation(token, inviteId);

        acceptInvitationCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, NAME + "onResponse called response code: " + response.code());
                if (response.code() == 200) {
                    if (response.body() != null) {
                        try {
                            String in = response.body().string();
                            Log.i(TAG, NAME + "onResponse : accept invitation response is \n" + in);
                            ModelAcceptInvitationResponse acceptInvitationResponse = gson.fromJson(in, ModelAcceptInvitationResponse.class);
                            Log.i(TAG, NAME + "onResponse: status is=" + acceptInvitationResponse.getStatus());
                            if (acceptInvitationResponse.getStatus()) {
                                btn_invitationlist_accept.setVisibility(View.GONE);
                                Log.i(TAG, NAME + acceptInvitationResponse.getMessage());
                                Toast.makeText(InvitationActivity.this, acceptInvitationResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Log.i(TAG, NAME + acceptInvitationResponse.getMessage());
                                Toast.makeText(InvitationActivity.this, acceptInvitationResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.i(TAG, NAME + "onResponse called" + "exception caught");
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.i(TAG, NAME + "onResponse: response code: " + response.code());
                    Toast.makeText(InvitationActivity.this, "invalid response code", Toast.LENGTH_SHORT).show();
                }
                GlobalMethods.hideProgressBar(invitationBinding.pbAll);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, NAME + "onFailure called");
                Toast.makeText(InvitationActivity.this, "unable to make connection to server", Toast.LENGTH_SHORT).show();
                GlobalMethods.hideProgressBar(invitationBinding.pbAll);
            }
        });

    }

    private void callToRejectInvitationApi(InvitationList invitationListData, int position) {
        Log.i(TAG, NAME + "callToRejectInvitationApi called:  ");
        int inviteId = invitationListData.getId();
        Log.i(TAG, NAME + "invite id is: " + inviteId);

        GlobalMethods.showProgressBar(invitationBinding.pbAll);
        gson = new Gson();
        String token = GlobalMethods.getToken(this);
        Call<ResponseBody> rejectInvitationCall = RetrofitClient.getServiceApi().rejectInvitation(token, inviteId);

        rejectInvitationCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, NAME + "onResponse called response code: " + response.code());
                if (response.code() == 200) {
                    if (response.body() != null) {
                        try {
                            String in = response.body().string();
                            Log.i(TAG, NAME + "onResponse : invitation list response is \n" + in);
                            JSONObject responseJson = new JSONObject(in);
                            boolean status = responseJson.getBoolean("status");
                            String message = responseJson.getString("message");
                            Log.i(TAG, NAME + "onResponse: status is=" + status);
                            if (status) {
                                mInvitedByOthersList.remove(invitationListData);
                                mInvitedByRecyclerAdapter.notifyItemRemoved(position);
                                Log.i(TAG, NAME + message);
                                Toast.makeText(InvitationActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else {
                                //invalid details
                                Log.i(TAG, NAME + message);
                                Toast.makeText(InvitationActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.i(TAG, NAME + "onResponse called" + "exception caught");
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.i(TAG, NAME + "onResponse: response code: " + response.code());
                    Toast.makeText(InvitationActivity.this, "invalid response code", Toast.LENGTH_SHORT).show();
                }
                GlobalMethods.hideProgressBar(invitationBinding.pbAll);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, NAME + "onFailure called");
                Toast.makeText(InvitationActivity.this, "unable to make connection to server", Toast.LENGTH_SHORT).show();
                GlobalMethods.hideProgressBar(invitationBinding.pbAll);
            }
        });
    }

    private void callToRejectInvitationApi(InviteData inviteData, int position) {
        Log.i(TAG, NAME + "callToRejectInvitationApi called:  ");
        int inviteId = inviteData.getId();
        Log.i(TAG, NAME + "invite id is: " + inviteId);

        GlobalMethods.showProgressBar(invitationBinding.pbAll);
        gson = new Gson();
        String token = GlobalMethods.getToken(this);
        Call<ResponseBody> rejectInvitationCall = RetrofitClient.getServiceApi().rejectInvitation(token, inviteId);

        rejectInvitationCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, NAME + "onResponse called response code: " + response.code());
                if (response.code() == 200) {
                    if (response.body() != null) {
                        try {
                            String in = response.body().string();
                            Log.i(TAG, NAME + "onResponse : invitation list response is \n" + in);
                            JSONObject responseJson = new JSONObject(in);
                            boolean status = responseJson.getBoolean("status");
                            String message = responseJson.getString("message");
                            Log.i(TAG, NAME + "onResponse: status is=" + status);
                            if (status) {
                                mInvitedToOthersList.remove(inviteData);
                                mInvitedToRecyclerAdapter.notifyItemRemoved(position);
                                Log.i(TAG, NAME + message);
                                Toast.makeText(InvitationActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else {
                                //invalid details
                                Log.i(TAG, NAME + message);
                                Toast.makeText(InvitationActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.i(TAG, NAME + "onResponse called" + "exception caught");
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.i(TAG, NAME + "onResponse: response code: " + response.code());
                    Toast.makeText(InvitationActivity.this, "invalid response code", Toast.LENGTH_SHORT).show();
                }
                GlobalMethods.hideProgressBar(invitationBinding.pbAll);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, NAME + "onFailure called");
                Toast.makeText(InvitationActivity.this, "unable to make connection to server", Toast.LENGTH_SHORT).show();
                GlobalMethods.hideProgressBar(invitationBinding.pbAll);
            }
        });
    }


}

