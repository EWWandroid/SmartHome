package com.app.smarthome.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.smarthome.R;
import com.app.smarthome.Session;
import com.app.smarthome.fragments.DevicesFragment;
import com.app.smarthome.fragments.HomeFragment;
import com.app.smarthome.util.Constants;
import com.app.smarthome.databinding.ActivityHomeBinding;
import com.app.smarthome.util.GlobalMethods;

public class HomeActivity extends AppCompatActivity implements Constants, View.OnClickListener, SetupWifiDialog.DialogListener {

    private static final String NAME = HomeActivity.class.getSimpleName() + " ";
    private static final String TAG = COMMON_TAG;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        GlobalMethods.setToolbar(binding.toolbar, R.string.home, R.drawable.ic_drawer_hamburger);
        setNavigationDrawer();
        onDrawerItemSelected();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_home,
                    new HomeFragment(this)).commit();
        }
    }

    private void setNavigationDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.dlHome, binding.toolbar.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);

        binding.dlHome.setDrawerElevation(0);
        binding.dlHome.addDrawerListener(toggle);
        binding.dlHome.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        binding.toolbar.ivTbStart.setOnClickListener(v -> binding.dlHome.openDrawer(GravityCompat.START));
        toggle.syncState();
    }

    public void onDrawerItemSelected() {
        binding.ibNavAdddevice.setOnClickListener(this);
        binding.ibNavNewswitch.setOnClickListener(this);
        binding.ibNavPeople.setOnClickListener(this);
        binding.ibNavAddnewdevice.setOnClickListener(this);
        binding.ibNavLogout.setOnClickListener(this);
        binding.ibNavBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ib_nav_back:
                Log.i(TAG, NAME + "ib_nav_back: called");
                binding.dlHome.closeDrawer(GravityCompat.START);
                break;

            case R.id.ib_nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_home,
                        new HomeFragment(this)).commit();
                break;

            case R.id.ib_nav_people:
                startActivity(new Intent(this, PeopleActivity.class));
                break;

            case R.id.ib_nav_adddevice:
                //Screen 6 : Display list of devices + Add new device, scan and Add new device
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_home, new DevicesFragment(this)).commit();
                Log.i(TAG, NAME + "onClick: ib_nav_addrooms");
                break;

            case R.id.ib_nav_newswitch:
                //Screen 7 : Display list of switches, delete switch,
                startActivity(new Intent(this, InvitationActivity.class));
                break;

            case R.id.ib_nav_addnewdevice:
                //Screen 8 : Display list of setting options
                //getSupportFragmentManager().beginTransaction().replace(R.id.fl_home, new DevicesFragment(this)).commit();
                //Log.i(TAG, NAME + "onClick: ib_nav_addnewdevice");
                startActivity(new Intent(this, NewSwitchActivity.class));
                break;
            case R.id.ib_nav_logout:
                goToLoginActivity();
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                SetupWifiDialog setupWifiDialog = new SetupWifiDialog(this);
//                Bundle bundle = new Bundle();
//                bundle.putBoolean("wifiDialog", true);
//                setupWifiDialog.setArguments(bundle);
//                setupWifiDialog.show(ft, "dialog");
//                Log.i(TAG, NAME + "onClick: ib_nav_logout");
                break;
        }

        if (binding.dlHome.isDrawerOpen(GravityCompat.START)) {
            binding.dlHome.closeDrawer(GravityCompat.START);
        }
    }

    private void goToLoginActivity() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Session session = new Session(this);
        session.clearData();
        startActivity(loginIntent);
    }

    @Override
    public void onBackPressed() {
        if (binding.dlHome.isDrawerOpen(GravityCompat.START)) {
            binding.dlHome.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onFinishEditing(String ssid, String password) {
        Toast.makeText(this, "ssid and password are\n" + ssid + "\n" + password, Toast.LENGTH_SHORT).show();
    }
}
