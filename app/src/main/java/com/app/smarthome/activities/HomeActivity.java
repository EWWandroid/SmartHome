package com.app.smarthome.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.smarthome.R;
import com.app.smarthome.fragments.DevicesFragment;
import com.app.smarthome.fragments.HomeFragment;
import com.app.smarthome.util.Constants;
import com.app.smarthome.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity implements Constants, View.OnClickListener {

    private static final String NAME = HomeActivity.class.getSimpleName();
    private static final String TAG = COMMON_TAG;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setToolBar();
        setNavigationDrawer();
        onDrawerItemSelected();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_home,
                    new HomeFragment(this)).commit();
            binding.nvHome.setCheckedItem(R.id.ib_nav_home);
        }

    }

    private void setToolBar() {
        binding.toolbar.ivTbStart.setImageResource(R.drawable.drawer_hamburger);
        binding.toolbar.tvTbCenter.setText(getString(R.string.home));
    }

    private void setNavigationDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.dlHome, binding.toolbar.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);

        binding.dlHome.setDrawerElevation(0);
        binding.dlHome.addDrawerListener(toggle);

        binding.toolbar.ivTbStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.dlHome.openDrawer(GravityCompat.START);
            }
        });
        toggle.syncState();
    }

    public void onDrawerItemSelected() {
        binding.ibNavAddrooms.setOnClickListener(this);
        binding.ibNavNewswitch.setOnClickListener(this);
        binding.ibNavDevices.setOnClickListener(this);
        binding.ibNavAddnewdevice.setOnClickListener(this);
        binding.ibNavLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ib_nav_addrooms:
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_home, new HomeFragment(this)).commit();
                Toast.makeText(this, "Clicked on: ib_nav_addrooms", Toast.LENGTH_SHORT).show();
                Log.i(TAG, NAME + "onClick: ib_nav_addrooms");
                binding.nvHome.setCheckedItem(R.id.ib_nav_addrooms);
                break;
            case R.id.ib_nav_newswitch:
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_home, new DevicesFragment(this)).commit();
                Toast.makeText(this, "Clicked on: ib_nav_newswitch", Toast.LENGTH_SHORT).show();
                binding.nvHome.setCheckedItem(R.id.ib_nav_newswitch);
                Log.i(TAG, NAME + "onClick: ib_nav_newswitch");
                break;
            case R.id.ib_nav_devices:
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_home, new HomeFragment(this)).commit();
                Toast.makeText(this, "Clicked on: ib_nav_devices", Toast.LENGTH_SHORT).show();
                binding.nvHome.setCheckedItem(R.id.ib_nav_devices);
                Log.i(TAG, NAME + "onClick: ib_nav_devices");
                break;
            case R.id.ib_nav_addnewdevice:
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_home, new DevicesFragment(this)).commit();
                Toast.makeText(this, "Clicked on: ib_nav_addnewdevice", Toast.LENGTH_SHORT).show();
                binding.nvHome.setCheckedItem(R.id.ib_nav_addnewdevice);
                Log.i(TAG, NAME + "onClick: ib_nav_addnewdevice");
                break;
            case R.id.ib_nav_logout:
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_home, new HomeFragment(this)).commit();
                Toast.makeText(this, "Clicked on: ib_nav_logout", Toast.LENGTH_SHORT).show();
                binding.nvHome.setCheckedItem(R.id.ib_nav_logout);
                Log.i(TAG, NAME + "onClick: ib_nav_logout");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.dlHome.isDrawerOpen(GravityCompat.START)) {
            binding.dlHome.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
