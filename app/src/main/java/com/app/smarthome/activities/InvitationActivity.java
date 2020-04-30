package com.app.smarthome.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.smarthome.R;
import com.app.smarthome.adapter.InvitedToRecyclerAdapter;
import com.app.smarthome.adapter.InvitedByRecyclerAdapter;
import com.app.smarthome.adapter.SearchUserAdapter;
import com.app.smarthome.databinding.ActivityInvitationBinding;
import com.app.smarthome.retrofit.RetrofitClient;
import com.app.smarthome.retrofit.model.main.ModelAcceptInvitationResponse;
import com.app.smarthome.retrofit.model.main.ModelInvitedByOthersResponse;
import com.app.smarthome.retrofit.model.main.ModelInvitedToOtherResponse;
import com.app.smarthome.retrofit.model.main.ModelSearchUserListResponse;
import com.app.smarthome.retrofit.model.main.ModelSendInvitationResponse;
import com.app.smarthome.retrofit.model.main.ModelSimpleListItem;
import com.app.smarthome.retrofit.model.sub.InvitedByOther;
import com.app.smarthome.retrofit.model.sub.InvitedToOther;
import com.app.smarthome.retrofit.model.sub.SearchUserData;
import com.app.smarthome.util.Constants;
import com.app.smarthome.util.GlobalMethods;
import com.google.gson.Gson;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

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

    private InvitedByRecyclerAdapter mInvitedByRecyclerAdapter;
    private InvitedToRecyclerAdapter mInvitedToRecyclerAdapter;
    private List<InvitedByOther> mInvitedByOthersList;
    private List<InvitedToOther> mInvitedToOthersList;
    private List<SearchUserData> mSearchUserDataList;
    private SearchUserAdapter mSearchAdapter;
    private Gson mGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        invitationBinding = ActivityInvitationBinding.inflate(getLayoutInflater());
        View view = invitationBinding.getRoot();
        setContentView(view);
        init();
    }

    private void init() {
        GlobalMethods.setToolbar(invitationBinding.toolbar, R.string.invitation, R.drawable.ic_arrow_back_black_24dp);
        invitationBinding.toolbar.ivTbStart.setOnClickListener(v -> finish());
        setSearchBarListeners();
        if (GlobalMethods.isNetworkAvailable(this)) {
            callToGetInvitedToApi();
            callToGetInvitedByApi();
            callToGetSearchableUserList();
        } else {
            GlobalMethods.showNetworkErrorSnackBar(invitationBinding.clInvitationRoot);
        }

    }

    private void setSearchBarListeners() {
        Log.i(TAG, NAME + "setSearchBarListeners called:  ");
        invitationBinding.svInvitation.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                Log.i(TAG, NAME + "onSearchStateChanged called: enabled? " + enabled);
                if (enabled) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    mSearchAdapter = new SearchUserAdapter(inflater, InvitationActivity.this, invitationBinding.svInvitation);
                    List<ModelSimpleListItem> suggestions = new ArrayList<>();
                    if (mSearchUserDataList != null) {
                        for (SearchUserData userData : mSearchUserDataList) {
                            suggestions.add(new ModelSimpleListItem(userData.getEmail()));
                        }
                    }
                    mSearchAdapter.setSuggestions(suggestions);
                    invitationBinding.svInvitation.setCustomSuggestionAdapter(mSearchAdapter);
                    invitationBinding.svInvitation.setSuggestionsEnabled(false);
                } else {
                    invitationBinding.svInvitation.clearSuggestions();
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                makeSearchCall(text.toString());
                invitationBinding.svInvitation.closeSearch();
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void makeSearchCall(CharSequence text) {
        Log.i(TAG, NAME + "onSearchConfirmed called:  ");
        String searchedEmail = text.toString().trim();
        Log.i(TAG, NAME + "searched email is:  " + searchedEmail);
        if (GlobalMethods.isNetworkAvailable(InvitationActivity.this)) {
            if (mSearchUserDataList != null) {
                int flag = 0;
                for (SearchUserData searchUserData : mSearchUserDataList) {
                    if (searchUserData.getEmail().equals(searchedEmail)) {
                        flag = 1;
                        String id = searchUserData.getId().toString().trim();
                        Log.i(TAG, NAME + "searched email id is: " + id);
                        callToSendInvitation(id);
                        break;
                    }
                }
                if (flag != 1) {
                    Toast.makeText(InvitationActivity.this, "Sorry! user with email not found", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, NAME + "user with email not found");
                }
            }
        } else {
            GlobalMethods.showNetworkErrorSnackBar(invitationBinding.clInvitationRoot);
        }
    }

    /*sets suggestions and text change listener*/
    private void setSearchBarFeatures() {
        Log.i(TAG, NAME + "suggestion visible?" + invitationBinding.svInvitation.isSuggestionsVisible());

        invitationBinding.svInvitation.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(TAG, NAME + "beforeTextChanged called:  ");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(TAG, NAME + " text changed " + invitationBinding.svInvitation.getText());
                // send the entered text to our filter and let it manage everything
                if (!TextUtils.isEmpty(charSequence)) {
                    invitationBinding.svInvitation.setSuggestionsEnabled(true);
                    mSearchAdapter.getFilter().filter(invitationBinding.svInvitation.getText());
                    invitationBinding.svInvitation.showSuggestionsList();
                } else {
                    invitationBinding.svInvitation.hideSuggestionsList();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.i(TAG, NAME + "afterTextChanged called:  ");
            }

        });

        invitationBinding.svInvitation.setSuggestionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(int position, View v) {
                Log.i(TAG, NAME + "OnItemClickListener called: clicked on " + position);
            }

            @Override
            public void OnItemDeleteListener(int position, View v) {

            }
        });
    }

    private void callToSendInvitation(String id) {
        Log.i(TAG, NAME + "callToSendInvitation called:  ");
        GlobalMethods.showProgressBar(invitationBinding.pbAll);
        mGson = new Gson();
        String token = GlobalMethods.getToken(this);
        Call<ResponseBody> sendInvitationCall = RetrofitClient.getServiceApi().sendInvitation(token, id);
        sendInvitationCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, NAME + "onResponse called response code: " + response.code());
                if (response.code() == 200) {
                    if (response.body() != null) {
                        try {
                            String in = response.body().string();
                            Log.i(TAG, NAME + "onResponse : device list response is \n" + in);
                            ModelSendInvitationResponse sendInvitationResponse = mGson.fromJson(in, ModelSendInvitationResponse.class);
                            Log.i(TAG, NAME + "onResponse: status is=" + sendInvitationResponse.getStatus());
                            if (sendInvitationResponse.getStatus()) {
                                Toast.makeText(InvitationActivity.this, sendInvitationResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                //according to this list show suggestions
                            } else {
                                //invalid details
                                Log.i(TAG, NAME + sendInvitationResponse.getMessage());
                                Toast.makeText(InvitationActivity.this, sendInvitationResponse.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void callToGetSearchableUserList() {
        mSearchUserDataList = new ArrayList<>();
        Log.i(TAG, NAME + "callToGetSearchableUserList called:  ");
        GlobalMethods.showProgressBar(invitationBinding.pbAll);
        mGson = new Gson();
        String token = GlobalMethods.getToken(this);
        String allEmail = "";
        Call<ResponseBody> searchableUserCall = RetrofitClient.getServiceApi().searchUsers(token, allEmail);

        searchableUserCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, NAME + "onResponse called response code: " + response.code());
                if (response.code() == 200) {
                    if (response.body() != null) {
                        try {
                            String in = response.body().string();
                            Log.i(TAG, NAME + "onResponse : device list response is \n" + in);
                            ModelSearchUserListResponse searchUserListResponse = mGson.fromJson(in, ModelSearchUserListResponse.class);
                            Log.i(TAG, NAME + "onResponse: status is=" + searchUserListResponse.getStatus());
                            if (searchUserListResponse.getStatus()) {
                                mSearchUserDataList = searchUserListResponse.getData();
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
                setSearchBarFeatures();
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

    @Override
    public void onRejectClick(InvitedByOther invitedByOther, int position) {
        if (invitedByOther != null) {
            if (GlobalMethods.isNetworkAvailable(InvitationActivity.this)) {
                callToRejectInvitationApi(invitedByOther, position);
            } else {
                GlobalMethods.showNetworkErrorSnackBar(invitationBinding.clInvitationRoot);
            }
        }
    }

    @Override
    public void onAcceptClick(InvitedByOther invitedByOther, int position, Button btn_invitationlist_accept) {
        if (invitedByOther != null) {
            if (GlobalMethods.isNetworkAvailable(InvitationActivity.this)) {
                callToAcceptInvitationApi(invitedByOther, position, btn_invitationlist_accept);
            } else {
                GlobalMethods.showNetworkErrorSnackBar(invitationBinding.clInvitationRoot);
            }
        }
    }

    @Override
    public void onRejectClick(InvitedToOther inviteData, int position) {
        if (inviteData != null) {
            if (GlobalMethods.isNetworkAvailable(this)) {
                callToRejectInvitationApi(inviteData, position);
            } else {
                GlobalMethods.showNetworkErrorSnackBar(invitationBinding.clInvitationRoot);
            }
        }
    }

    private void callToGetInvitedByApi() {
        Log.i(TAG, NAME + "callToGetInvitedByApi called:  ");
        GlobalMethods.showProgressBar(invitationBinding.pbAll);
        mGson = new Gson();
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
                            ModelInvitedByOthersResponse invitedByOthersResponse = mGson.fromJson(in, ModelInvitedByOthersResponse.class);
                            Log.i(TAG, NAME + "onResponse: status is=" + invitedByOthersResponse.getStatus());
                            if (invitedByOthersResponse.getStatus()) {
                                mInvitedByOthersList = invitedByOthersResponse.getData();
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
        mGson = new Gson();
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
                            ModelInvitedToOtherResponse invitedToOtherResponse = mGson.fromJson(in, ModelInvitedToOtherResponse.class);
                            Log.i(TAG, NAME + "onResponse: status is=" + invitedToOtherResponse.getStatus());
                            if (invitedToOtherResponse.getStatus()) {
                                mInvitedToOthersList =invitedToOtherResponse.getData();
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

    private void showInvitedToOthersList(List<InvitedToOther> invitedToOthersList) {
        Log.i(TAG, NAME + "showInvitedToOthersList called:  ");
        if (invitedToOthersList != null) {
            Log.i(TAG, NAME + "invited to list is:\n" + invitedToOthersList.toString());
            mInvitedToRecyclerAdapter = new InvitedToRecyclerAdapter(this, invitedToOthersList, this);
            invitationBinding.rvFriends.setLayoutManager(new LinearLayoutManager(this));
            invitationBinding.rvFriends.setAdapter(mInvitedToRecyclerAdapter);
        }
    }

    private void showInvitedByOthersList(List<InvitedByOther> invitedByOthersList) {
        Log.i(TAG, NAME + "showInvitedByOthersList called:  ");
        if (invitedByOthersList != null) {
            Log.i(TAG, NAME + "showInvitationList called: size: " + invitedByOthersList.size());
            Log.i(TAG, NAME + "invitation list is:\n" + invitedByOthersList.toString());
            mInvitedByRecyclerAdapter = new InvitedByRecyclerAdapter(this, invitedByOthersList, this);
            invitationBinding.rvInvitation.setLayoutManager(new LinearLayoutManager(this));
            invitationBinding.rvInvitation.setAdapter(mInvitedByRecyclerAdapter);
        }
    }

    private void callToAcceptInvitationApi(InvitedByOther invitationListData, int position, Button btn_invitationlist_accept) {
        Log.i(TAG, NAME + "callToAcceptInvitationApi called:  ");
        int inviteId = invitationListData.getId();
        Log.i(TAG, NAME + "invite id is: " + inviteId);
        GlobalMethods.showProgressBar(invitationBinding.pbAll);
        mGson = new Gson();
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
                            ModelAcceptInvitationResponse acceptInvitationResponse = mGson.fromJson(in, ModelAcceptInvitationResponse.class);
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

    private void callToRejectInvitationApi(InvitedByOther invitationListData, int position) {
        Log.i(TAG, NAME + "callToRejectInvitationApi called:  ");
        int inviteId = invitationListData.getId();
        Log.i(TAG, NAME + "invite id is: " + inviteId);

        GlobalMethods.showProgressBar(invitationBinding.pbAll);
        mGson = new Gson();
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

    private void callToRejectInvitationApi(InvitedToOther inviteData, int position) {
        Log.i(TAG, NAME + "callToRejectInvitationApi called:  ");
        int inviteId = inviteData.getId();
        Log.i(TAG, NAME + "invite id is: " + inviteId);

        GlobalMethods.showProgressBar(invitationBinding.pbAll);
        mGson = new Gson();
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

