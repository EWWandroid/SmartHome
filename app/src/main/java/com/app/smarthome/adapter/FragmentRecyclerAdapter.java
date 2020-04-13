package com.app.smarthome.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.smarthome.R;
import com.app.smarthome.retrofit.model.sub.DevicesListData;
import com.app.smarthome.retrofit.model.sub.Switch;
import com.app.smarthome.util.Constants;

import java.util.List;

public class FragmentRecyclerAdapter extends RecyclerView.Adapter
        implements Constants {

    private Context context;
    private ClickListener clickListener;
    private int FRAGMENT_NAME;
    private List<Switch> switches;
    private List<DevicesListData> devicesListData;

    public FragmentRecyclerAdapter(Context context, ClickListener clickListener, List<Switch> switches, int FRAGMENT_NAME) {
        this.context = context;
        this.clickListener = clickListener;
        this.FRAGMENT_NAME = FRAGMENT_NAME;
        this.switches = switches;
    }

    public FragmentRecyclerAdapter(Context context, ClickListener clickListener, int FRAGMENT_NAME, List<DevicesListData> devicesListData) {
        this.context = context;
        this.clickListener = clickListener;
        this.FRAGMENT_NAME = FRAGMENT_NAME;
        this.devicesListData = devicesListData;
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

        if (holder instanceof HomeFragmentViewHolder) {
            Switch switchItem = switches.get(position);
            String name = switchItem.getName();
            String status = switchItem.getStatus();
            ((HomeFragmentViewHolder) holder).tv_recyclerswitch_switchtype.setText(name);
        }

        if (holder instanceof DeviceFragmentViewHolder) {
            DevicesListData deviceData = devicesListData.get(position);
            String name = deviceData.getName();
            String status = deviceData.getStatus();

            ((DeviceFragmentViewHolder) holder).tv_recyclerdevice_name.setText(name);
            if (status.equals(ITEM_STATUS)) {
                ImageViewCompat.setImageTintList(((DeviceFragmentViewHolder) holder).iv_recyclerdevice_status,
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green)));
            } else {
                ImageViewCompat.setImageTintList(((DeviceFragmentViewHolder) holder).iv_recyclerdevice_status,
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red)));
            }
        }
    }

    @Override
    public int getItemCount() {
        if (FRAGMENT_NAME == 1) {
            return switches.size();
        }
        return devicesListData.size();
    }

    class HomeFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ClickListener clickListener;
        RelativeLayout rl_switchitem_cardview;
        ImageView iv_recycleritem_photo;
        TextView tv_recyclerswitch_switchtype;

        public HomeFragmentViewHolder(@NonNull View itemView, ClickListener clickListener) {
            super(itemView);
            this.clickListener = clickListener;
            rl_switchitem_cardview = itemView.findViewById(R.id.rl_switchitem_cardview);
            iv_recycleritem_photo = itemView.findViewById(R.id.iv_recycleritem_photo);
            tv_recyclerswitch_switchtype = itemView.findViewById(R.id.tv_recyclerswitch_switchtype);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(getAdapterPosition());
        }
    }

    class DeviceFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ClickListener clickListener;
        TextView tv_recyclerdevice_name;
        ImageView iv_recyclerdevice_status;

        public DeviceFragmentViewHolder(@NonNull View itemView, ClickListener clickListener) {
            super(itemView);
            tv_recyclerdevice_name = itemView.findViewById(R.id.tv_recyclerdevice_name);
            iv_recyclerdevice_status = itemView.findViewById(R.id.iv_recyclerdevice_status);
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
