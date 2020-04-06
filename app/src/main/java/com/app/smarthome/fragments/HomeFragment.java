package com.app.smarthome.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.smarthome.R;
import com.app.smarthome.adapter.HomePagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    TabLayout tl_homefrag;
    ViewPager2 vp_homefrag;
    HomePagerAdapter pagerAdapter;
    private Context context;

    public HomeFragment(Context context) {
        // Required empty public constructor
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tl_homefrag = view.findViewById(R.id.tl_homefrag);
        vp_homefrag = view.findViewById(R.id.vp_homefrag);
        pagerAdapter = new HomePagerAdapter(this, context);
        vp_homefrag.setAdapter(pagerAdapter);

        new TabLayoutMediator(tl_homefrag, vp_homefrag,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                        switch (position) {
                            case 0:
                                tab.setText("Boss Office");
                                break;

                            case 1:
                                tab.setText("Developing Area");
                                break;

                            case 2:
                                tab.setText("Conference Room");
                                break;
                        }
                    }
                }).attach();
    }
}
