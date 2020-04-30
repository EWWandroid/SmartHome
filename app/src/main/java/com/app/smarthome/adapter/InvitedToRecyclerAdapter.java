package com.app.smarthome.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.smarthome.R;
import com.app.smarthome.retrofit.model.sub.InvitedToOther;
import com.app.smarthome.retrofit.model.sub.InviteUserData;
import com.app.smarthome.util.Constants;

import java.util.List;

public class InvitedToRecyclerAdapter extends RecyclerView.Adapter<InvitedToRecyclerAdapter.FriendsListItemViewHolder> implements Constants {

    private static final String NAME = InvitedToRecyclerAdapter.class.getSimpleName();
    private static final String TAG = COMMON_TAG;

    private Context context;
    private List<InvitedToOther> inviteDataList;
    private OnRecyclerFriendsItemClickListener listener;

    public InvitedToRecyclerAdapter(Context context, List<InvitedToOther> inviteDataList, OnRecyclerFriendsItemClickListener listener) {
        this.context = context;
        this.inviteDataList = inviteDataList;
        this.listener = listener;
    }

    public interface OnRecyclerFriendsItemClickListener {
        void onRejectClick(InvitedToOther inviteData, int position);
    }

    @NonNull
    @Override
    public FriendsListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_invitation_friends, parent, false);
        return new FriendsListItemViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsListItemViewHolder holder, int position) {
        InvitedToOther inviteData = inviteDataList.get(position);
        InviteUserData inviteUserData = inviteData.getInvite();
        Log.i(TAG, NAME + "onBindViewHolder called:  ");
        Log.i(TAG, NAME + inviteData.toString());
        if (inviteUserData != null) {
            String email = inviteUserData.getEmail();
            String firstName = inviteUserData.getName();
            holder.tv_friendsitem_email.setText(email);
            holder.tv_firendsitem_firstname.setText(firstName);
        }
    }

    @Override
    public int getItemCount() {
        return inviteDataList.size();
    }

    class FriendsListItemViewHolder extends RecyclerView.ViewHolder {

        OnRecyclerFriendsItemClickListener listener;
        ImageButton ib_friendsitem_cancel;
        TextView tv_friendsitem_email, tv_firendsitem_firstname;

        FriendsListItemViewHolder(@NonNull View itemView, OnRecyclerFriendsItemClickListener listener) {
            super(itemView);
            this.listener = listener;
            tv_firendsitem_firstname = itemView.findViewById(R.id.tv_firendsitem_firstname);
            tv_friendsitem_email = itemView.findViewById(R.id.tv_friendsitem_email);
            ib_friendsitem_cancel = itemView.findViewById(R.id.ib_friendsitem_cancel);
            ib_friendsitem_cancel.setOnClickListener(v -> listener.onRejectClick(inviteDataList.get(getAdapterPosition()), getAdapterPosition()));
        }
    }
}
