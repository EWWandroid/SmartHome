package com.app.smarthome.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.smarthome.R;
import com.app.smarthome.adapter.FragmentRecyclerAdapter;
import com.app.smarthome.util.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class DevicesFragment extends Fragment implements Constants, FragmentRecyclerAdapter.ClickListener {

    private Context context;

    public DevicesFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_devices, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv_fragment = view.findViewById(R.id.rv_fragment);
        FragmentRecyclerAdapter adapter = new FragmentRecyclerAdapter(context, this, FRAGMENT_DEVICE);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        rv_fragment.setLayoutManager(gridLayoutManager);
        rv_fragment.setAdapter(adapter);

        FloatingActionButton fab_adddevice = view.findViewById(R.id.fab_adddevice);
        fab_adddevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fl_home, new AddNewDeviceFragment()).commit();
            }
        });

    }

    @Override
    public void onClick(int position) {

    }
}
