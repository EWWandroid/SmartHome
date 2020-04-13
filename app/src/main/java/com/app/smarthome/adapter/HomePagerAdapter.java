package com.app.smarthome.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.app.smarthome.fragments.SwitchesFragment;
import com.app.smarthome.retrofit.model.sub.GroupListData;

import java.util.List;

public class HomePagerAdapter extends FragmentStateAdapter {

    private Context context;
    private int totalTabCount;
    private List<GroupListData> groupListData;

    public HomePagerAdapter(@NonNull Fragment fragment, Context context, int totalTabCount, List<GroupListData> groupListData) {
        super(fragment);
        this.context = context;
        this.totalTabCount = totalTabCount;
        this.groupListData = groupListData;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new SwitchesFragment(context, groupListData.get(position));
    }

    @Override
    public int getItemCount() {
        return totalTabCount;
    }
}
