package com.app.smarthome.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.app.smarthome.R;
import com.app.smarthome.databinding.SetupwifiDialogBinding;

public class SetupWifiDialog extends DialogFragment {

    private Context mContext;
    private DialogListener listener;

    SetupWifiDialog(Context mContext) {
        this.mContext = mContext;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.setupwifi_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText et_alertdialog_ssid = view.findViewById(R.id.et_alertdialog_ssid);
        final EditText et_alertdialog_add_password = view.findViewById(R.id.et_alertdialog_add_password);
        final ImageButton iv_alertdialog_add_close = view.findViewById(R.id.iv_alertdialog_add_close);
        Button btn_alertdialog_add_add = view.findViewById(R.id.btn_alertdialog_add_add);

        iv_alertdialog_add_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btn_alertdialog_add_add.setOnClickListener(v -> {
            String ssid = getSsid(et_alertdialog_ssid);
            String password = getPassword(et_alertdialog_add_password);

            if (TextUtils.isEmpty(ssid)) {
                et_alertdialog_ssid.setError(getString(R.string.required_ssid));
                et_alertdialog_ssid.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                et_alertdialog_add_password.setError(getString(R.string.required_password));
                et_alertdialog_add_password.requestFocus();
                return;
            }
            listener.onFinishEditing(ssid, password);
            dismiss();
        });
    }

    private String getPassword(EditText et_alertdialog_add_password) {
        return et_alertdialog_add_password.getText().toString().trim();
    }

    private String getSsid(EditText et_alertdialog_ssid) {
        return et_alertdialog_ssid.getText().toString().trim();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (DialogListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        return dialog;
    }

    interface DialogListener {
        void onFinishEditing(String ssid, String password);
    }
}
