package com.app.smarthome.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.app.smarthome.fragments.BossOfficeFragment;
import com.app.smarthome.fragments.ConferenceFragment;
import com.app.smarthome.fragments.DevelopingAreaFragment;

public class HomePagerAdapter extends FragmentStateAdapter {

    private Context context;

    public HomePagerAdapter(@NonNull Fragment fragment, Context context) {
        super(fragment);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new BossOfficeFragment(context);
                break;

            case 1:
                fragment = new DevelopingAreaFragment();
                break;

            case 2:
                fragment = new ConferenceFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
