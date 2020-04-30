package com.app.smarthome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.smarthome.R;
import com.app.smarthome.retrofit.model.sub.InvitationDataUser;
import com.app.smarthome.retrofit.model.sub.InvitedByOther;
import com.app.smarthome.util.Constants;

import java.util.List;

public class InvitedByRecyclerAdapter extends RecyclerView.Adapter<InvitedByRecyclerAdapter.InvitationListItemViewHolder>
        implements Constants {

    private static final String NAME = InvitedByRecyclerAdapter.class.getSimpleName();
    private static final String TAG = COMMON_TAG;

    private Context context;
    private List<InvitedByOther> invitedByOthersList;
    private OnRecyclerInvitationItemClickListener listener;

    public interface OnRecyclerInvitationItemClickListener {
        void onRejectClick(InvitedByOther invitedByOther, int position);
        void onAcceptClick(InvitedByOther invitedByOther, int position, Button btn_invitationlist_accept);
    }

    public InvitedByRecyclerAdapter(Context context, List<InvitedByOther> invitedByOthersList, OnRecyclerInvitationItemClickListener listener) {
        this.context = context;
        this.invitedByOthersList = invitedByOthersList;
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull InvitationListItemViewHolder holder, int position) {
        InvitedByOther invitationListData = invitedByOthersList.get(position);
        InvitationDataUser dataUser = invitationListData.getUser();
        String email = dataUser.getEmail();
        String firstName = dataUser.getName();
        holder.tv_invitationlist_email.setText(email);
        holder.tv_invitationlist_firstname.setText(firstName);
    }

    @NonNull
    @Override
    public InvitationListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_invitation_invitationlist, parent, false);
        return new InvitationListItemViewHolder(view, listener);

    }

    @Override
    public int getItemCount() {
        return invitedByOthersList.size();
    }

    class InvitationListItemViewHolder extends RecyclerView.ViewHolder {

        OnRecyclerInvitationItemClickListener listener;
        ImageButton ib_invitationlist_cancel;
        Button btn_invitationlist_accept;
        TextView tv_invitationlist_email, tv_invitationlist_firstname;

        InvitationListItemViewHolder(@NonNull View itemView, OnRecyclerInvitationItemClickListener listener) {
            super(itemView);
            this.listener = listener;
            tv_invitationlist_firstname = itemView.findViewById(R.id.tv_invitationlist_firstname);
            tv_invitationlist_email = itemView.findViewById(R.id.tv_invitationlist_email);
            btn_invitationlist_accept = itemView.findViewById(R.id.btn_invitationlist_accept);
            ib_invitationlist_cancel = itemView.findViewById(R.id.ib_invitationlist_cancel);
            btn_invitationlist_accept.setOnClickListener(v -> listener.onAcceptClick(invitedByOthersList.get(getAdapterPosition()), getAdapterPosition(), btn_invitationlist_accept));
            ib_invitationlist_cancel.setOnClickListener(v -> listener.onRejectClick(invitedByOthersList.get(getAdapterPosition()), getAdapterPosition()));
        }
    }
}
