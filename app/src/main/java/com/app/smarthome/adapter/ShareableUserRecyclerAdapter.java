package com.app.smarthome.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.smarthome.databinding.RecyclerEditandshareShareBinding;
import com.app.smarthome.retrofit.model.sub.ShareableUserData;
import com.app.smarthome.util.Constants;

import java.util.List;

public class ShareableUserRecyclerAdapter extends RecyclerView.Adapter<ShareableUserRecyclerAdapter.ShareableUserViewHolder> implements Constants {

    private List<ShareableUserData> shareableUserDataList;
    private Context context;
    private OnShareableItemClickListener onShareableItemClickListener;
    private String switchId;

    public interface OnShareableItemClickListener {
        void onSwitchClicked(int adapterPosition, ShareableUserData shareableUserData, String switchId, Switch swhRecyclerShareSwitchShare);
    }

    public ShareableUserRecyclerAdapter(List<ShareableUserData> shareableUserDataList, Context context, OnShareableItemClickListener onShareableItemClickListener, String switchId) {
        this.shareableUserDataList = shareableUserDataList;
        this.context = context;
        this.onShareableItemClickListener = onShareableItemClickListener;
        this.switchId = switchId;
    }

    @NonNull
    @Override
    public ShareableUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShareableUserViewHolder(RecyclerEditandshareShareBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent,
                false), onShareableItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ShareableUserViewHolder holder, int position) {
        ShareableUserData shareableUserData = shareableUserDataList.get(position);
        if (shareableUserData != null) {
            String userId = shareableUserData.getId().toString();
            String userName = shareableUserData.getName();
            String userEmail = shareableUserData.getEmail();
            String switchId = shareableUserData.getSwitchId();

            holder.editandshareShareBinding.tvRecyclershareswitchName.setText(userName);
            holder.editandshareShareBinding.tvRecyclershareswitchEmail.setText(userEmail);
            if (TextUtils.isEmpty(switchId)) {
                holder.editandshareShareBinding.swhRecyclerShareSwitchShare.setChecked(false);
            } else {
                holder.editandshareShareBinding.swhRecyclerShareSwitchShare.setChecked(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return shareableUserDataList.size();
    }

    class ShareableUserViewHolder extends RecyclerView.ViewHolder {

        RecyclerEditandshareShareBinding editandshareShareBinding;
        OnShareableItemClickListener onShareableItemClickListener;

        ShareableUserViewHolder(@NonNull RecyclerEditandshareShareBinding editandshareShareBinding, OnShareableItemClickListener onShareableItemClickListener) {
            super(editandshareShareBinding.getRoot());
            this.editandshareShareBinding = editandshareShareBinding;
            this.onShareableItemClickListener = onShareableItemClickListener;
            this.editandshareShareBinding.swhRecyclerShareSwitchShare.setOnClickListener(v ->
                    onShareableItemClickListener.onSwitchClicked(getAdapterPosition(), shareableUserDataList.get(getAdapterPosition()), switchId,editandshareShareBinding.swhRecyclerShareSwitchShare));
        }
    }
}
