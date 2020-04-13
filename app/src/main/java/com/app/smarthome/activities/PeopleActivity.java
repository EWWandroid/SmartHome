package com.app.smarthome.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.app.smarthome.R;
import com.app.smarthome.Session;
import com.app.smarthome.adapter.SearchUserAdapter;
import com.app.smarthome.databinding.ActivityPeopleBinding;
import com.app.smarthome.retrofit.RetrofitClient;
import com.app.smarthome.retrofit.model.main.ModelSendInvitationResponse;
import com.app.smarthome.retrofit.model.main.ModelSimpleListItem;
import com.app.smarthome.retrofit.model.sub.SearchUserData;
import com.app.smarthome.retrofit.model.main.ModelSearchUserListResponse;
import com.app.smarthome.util.Constants;
import com.app.smarthome.util.GlobalMethods;
import com.google.gson.Gson;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeopleActivity extends AppCompatActivity implements Constants {

    private static final String NAME = PeopleActivity.class.getSimpleName() + " ";
    private static final String TAG = COMMON_TAG;
    private boolean IS_ONE_CHARACTER_SEARCHED = false;

    private ActivityPeopleBinding peopleBinding;
    private Gson gson;
    private Session session;

    private List<String> list;
    private ArrayAdapter<String> arrayAdapter;
    private List<SearchUserData> searchUserDataList;
    private SearchUserAdapter mSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        peopleBinding = ActivityPeopleBinding.inflate(getLayoutInflater());
        View view = peopleBinding.getRoot();
        setContentView(view);
        init();
    }

    private void init() {
        GlobalMethods.setToolbar(peopleBinding.toolbar, R.string.people, R.drawable.ic_arrow_back_black_24dp);
        peopleBinding.toolbar.ivTbStart.setOnClickListener(v -> finish());
        setSearchBarListeners();
        if (GlobalMethods.isNetworkAvailable(this)) {
            callToGetSearchableUserList();
        } else {
            GlobalMethods.showNetworkErrorSnackBar(peopleBinding.clPeopleRoot);
        }
    }


    private void setSearchBarListeners() {
        Log.i(TAG, NAME + "setSearchBarListeners called:  ");
        peopleBinding.svPeople.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                Log.i(TAG, NAME + "onSearchStateChanged called: enabled? " + enabled);
                if (enabled) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    mSearchAdapter = new SearchUserAdapter(inflater, PeopleActivity.this, peopleBinding.svPeople);
                    List<ModelSimpleListItem> suggestions = new ArrayList<>();
                    if (searchUserDataList != null) {
                        for (SearchUserData userData : searchUserDataList) {
                            suggestions.add(new ModelSimpleListItem(userData.getEmail()));
                        }
                    }
                    mSearchAdapter.setSuggestions(suggestions);
                    peopleBinding.svPeople.setCustomSuggestionAdapter(mSearchAdapter);
                    peopleBinding.svPeople.setSuggestionsEnabled(false);
                } else {
                    peopleBinding.svPeople.clearSuggestions();
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                makeSearchCall(text.toString());
                peopleBinding.svPeople.closeSearch();
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
        if (GlobalMethods.isNetworkAvailable(PeopleActivity.this)) {
            if (searchUserDataList != null) {
                int flag = 0;
                for (SearchUserData searchUserData : searchUserDataList) {
                    if (searchUserData.getEmail().equals(searchedEmail)) {
                        flag = 1;
                        String id = searchUserData.getId().toString().trim();
                        Log.i(TAG, NAME + "searched email id is: " + id);
                        callToSendInvitation(id);
                        break;
                    }
                }
                if (flag != 1) {
                    Toast.makeText(PeopleActivity.this, "Sorry! user with email not found", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, NAME + "user with email not found");
                }
            }
        } else {
            GlobalMethods.showNetworkErrorSnackBar(peopleBinding.clPeopleRoot);
        }
    }

    /*sets suggestions and text change listener*/
    private void setSearchBarFeatures() {
        Log.i(TAG, NAME + "suggestion visible?" + peopleBinding.svPeople.isSuggestionsVisible());

        peopleBinding.svPeople.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(TAG, NAME + "beforeTextChanged called:  ");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(TAG, NAME + " text changed " + peopleBinding.svPeople.getText());
                // send the entered text to our filter and let it manage everything
                if (!TextUtils.isEmpty(charSequence)) {
                    peopleBinding.svPeople.setSuggestionsEnabled(true);
                    mSearchAdapter.getFilter().filter(peopleBinding.svPeople.getText());
                    peopleBinding.svPeople.showSuggestionsList();
                } else {
                    peopleBinding.svPeople.hideSuggestionsList();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.i(TAG, NAME + "afterTextChanged called:  ");
            }

        });

        peopleBinding.svPeople.setSuggestionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(int position, View v) {
                Log.i(TAG, NAME + "OnItemClickListener called: clicked on "+  position);
            }

            @Override
            public void OnItemDeleteListener(int position, View v) {

            }
        });
    }

    private void callToSendInvitation(String id) {
        Log.i(TAG, NAME + "callToSendInvitation called:  ");
        GlobalMethods.showProgressBar(peopleBinding.pbAll);
        gson = new Gson();
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
                            ModelSendInvitationResponse sendInvitationResponse = gson.fromJson(in, ModelSendInvitationResponse.class);
                            Log.i(TAG, NAME + "onResponse: status is=" + sendInvitationResponse.getStatus());
                            if (sendInvitationResponse.getStatus()) {
                                Toast.makeText(PeopleActivity.this, sendInvitationResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                //according to this list show suggestions
                            } else {
                                //invalid details
                                Log.i(TAG, NAME + sendInvitationResponse.getMessage());
                                Toast.makeText(PeopleActivity.this, sendInvitationResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.i(TAG, NAME + "onResponse called" + "exception caught");
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.i(TAG, NAME + "onResponse: response code: " + response.code());
                    Toast.makeText(PeopleActivity.this, "invalid response code", Toast.LENGTH_SHORT).show();
                }
                GlobalMethods.hideProgressBar(peopleBinding.pbAll);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, NAME + "onFailure called");
                Toast.makeText(PeopleActivity.this, "unable to make connection to server", Toast.LENGTH_SHORT).show();
                GlobalMethods.hideProgressBar(peopleBinding.pbAll);
            }
        });
    }

    private void callToGetSearchableUserList() {
        searchUserDataList = new ArrayList<>();
        Log.i(TAG, NAME + "callToGetSearchableUserList called:  ");
        GlobalMethods.showProgressBar(peopleBinding.pbAll);
        gson = new Gson();
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
                            ModelSearchUserListResponse searchUserListResponse = gson.fromJson(in, ModelSearchUserListResponse.class);
                            Log.i(TAG, NAME + "onResponse: status is=" + searchUserListResponse.getStatus());
                            if (searchUserListResponse.getStatus()) {
                                searchUserDataList = searchUserListResponse.getData();
                                //according to this list show suggestions
                            } else {
                                //invalid details
                                Toast.makeText(PeopleActivity.this, "status is false", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.i(TAG, NAME + "onResponse called" + "exception caught");
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.i(TAG, NAME + "onResponse: response code: " + response.code());
                    Toast.makeText(PeopleActivity.this, "invalid response code", Toast.LENGTH_SHORT).show();
                }
                setSearchBarFeatures();
                GlobalMethods.hideProgressBar(peopleBinding.pbAll);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, NAME + "onFailure called");
                Toast.makeText(PeopleActivity.this, "unable to make connection to server", Toast.LENGTH_SHORT).show();
                GlobalMethods.hideProgressBar(peopleBinding.pbAll);
            }
        });
    }
}
