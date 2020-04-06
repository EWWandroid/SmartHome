package com.app.smarthome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.smarthome.R;
import com.app.smarthome.util.Constants;

public class FragmentRecyclerAdapter extends RecyclerView.Adapter
        implements Constants {

    private Context context;
    private ClickListener clickListener;
    private int FRAGMENT_NAME;

    public FragmentRecyclerAdapter(Context context, ClickListener clickListener, int FRAGMENT_NAME) {
        this.context = context;
        this.clickListener = clickListener;
        this.FRAGMENT_NAME = FRAGMENT_NAME;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = null;
        if (FRAGMENT_NAME == FRAGMENT_HOME) {
            view = inflater.inflate(R.layout.recycler_switch_item_layout, parent, false);
            return new HomeFragmentViewHolder(view, clickListener);
        } else {
            view = inflater.inflate(R.layout.recycler_device_item_layout, parent, false);
            return new DeviceFragmentViewHolder(view, clickListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class HomeFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ClickListener clickListener;

        public HomeFragmentViewHolder(@NonNull View itemView, ClickListener clickListener) {
            super(itemView);
            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(getAdapterPosition());
        }
    }

    class DeviceFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ClickListener clickListener;

        public DeviceFragmentViewHolder(@NonNull View itemView, ClickListener clickListener) {
            super(itemView);
            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(getAdapterPosition());
        }
    }

    public interface ClickListener {
        void onClick(int position);
    }
}
