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
            view = inflater.inflate(R.layout.recycler_switch, parent, false);
            return new HomeFragmentViewHolder(view, clickListener);
        } else {
            view = inflater.inflate(R.layout.recycler_device, parent, false);
            return new DeviceFragmentViewHolder(view, clickListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HomeFragmentViewHolder) {
            Switch switchItem = switches.get(position);
            String name = switchItem.getName();
            int status = switchItem.getState();
            ((HomeFragmentViewHolder) holder).tv_recyclerswitch_switchtype.setText(name);

            if (status >= 1) {
                ((HomeFragmentViewHolder) holder).iv_state_track.setImageResource(R.drawable.status_track_up);
                ((HomeFragmentViewHolder) holder).iv_state_thumb_down.setVisibility(View.INVISIBLE);
                ((HomeFragmentViewHolder) holder).iv_state_thumb_up.setVisibility(View.VISIBLE);
                ((HomeFragmentViewHolder) holder).tv_recyclerswitch_switchtype.setTextColor(context.getResources().getColor(R.color.white));
                ImageViewCompat.setImageTintList(((HomeFragmentViewHolder) holder).iv_recycleritem_photo, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)));
                ((HomeFragmentViewHolder) holder).tv_recyclerswitch_name.setTextColor(context.getResources().getColor(R.color.white));
                ((HomeFragmentViewHolder) holder).rl_switchitem_cardview.setBackgroundResource(R.drawable.custom_recycler_item_gradient);
            }

            if (status == 0) {
                ((HomeFragmentViewHolder) holder).tv_recyclerswitch_switchtype.setText(name);
                ((HomeFragmentViewHolder) holder).iv_state_thumb_down.setVisibility(View.VISIBLE);
                ((HomeFragmentViewHolder) holder).iv_state_thumb_up.setVisibility(View.INVISIBLE);
                ((HomeFragmentViewHolder) holder).iv_state_track.setImageResource(R.drawable.status_track_down);
                ((HomeFragmentViewHolder) holder).tv_recyclerswitch_switchtype.setTextColor(context.getResources().getColor(R.color.black));
                ImageViewCompat.setImageTintList(((HomeFragmentViewHolder) holder).iv_recycleritem_photo, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black)));
                ((HomeFragmentViewHolder) holder).tv_recyclerswitch_name.setTextColor(context.getResources().getColor(R.color.black));
                ((HomeFragmentViewHolder) holder).rl_switchitem_cardview.setBackgroundResource(R.drawable.custom_white_gradient_all_btn);
            }

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

    class HomeFragmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ClickListener clickListener;
        RelativeLayout rl_switchitem_cardview;
        ImageView iv_recycleritem_photo, iv_state_thumb_down, iv_state_track, iv_state_thumb_up;
        TextView tv_recyclerswitch_switchtype, tv_recyclerswitch_name;

        HomeFragmentViewHolder(@NonNull View itemView, ClickListener clickListener) {
            super(itemView);
            this.clickListener = clickListener;
            iv_state_thumb_down = itemView.findViewById(R.id.iv_state_thumb_down);
            iv_state_track = itemView.findViewById(R.id.iv_state_track);
            iv_state_thumb_up = itemView.findViewById(R.id.iv_state_thumb_up);
            tv_recyclerswitch_switchtype = itemView.findViewById(R.id.tv_recyclerswitch_switchtype);
            iv_recycleritem_photo = itemView.findViewById(R.id.iv_recycleritem_photo);
            tv_recyclerswitch_name = itemView.findViewById(R.id.tv_recyclerswitch_name);
            rl_switchitem_cardview = itemView.findViewById(R.id.rl_switchitem_cardview);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onSingleClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onLongClick(switches.get(getAdapterPosition()).getId());
            return true;
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
            //itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onSingleClick(getAdapterPosition());
        }

    }

    public interface ClickListener {
        void onSingleClick(int position);

        void onLongClick(int switch_id);
    }
}
