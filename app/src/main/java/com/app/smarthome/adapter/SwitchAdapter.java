package com.app.smarthome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.smarthome.R;

public class SwitchAdapter extends RecyclerView.Adapter<SwitchAdapter.SwitchViewHolder> {

    private Context context;

    @NonNull
    @Override
    public SwitchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_switch_item_layout, parent, false);
        return new SwitchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SwitchViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class SwitchViewHolder extends RecyclerView.ViewHolder {

        TextView tv_recyclerswitch_name, tv_recyclerswitch_switchtype;
        ImageView iv_recycleritem_photo;
        Switch sw_recyclerswitch;

        public SwitchViewHolder(@NonNull View itemView) {
            super(itemView);
             tv_recyclerswitch_name = itemView.findViewById(R.id.tv_recyclerswitch_name);
             tv_recyclerswitch_switchtype = itemView.findViewById(R.id.tv_recyclerswitch_switchtype);
             iv_recycleritem_photo = itemView.findViewById(R.id.iv_recycleritem_photo);
             sw_recyclerswitch = itemView.findViewById(R.id.iv_state_track);

        }
    }
}
