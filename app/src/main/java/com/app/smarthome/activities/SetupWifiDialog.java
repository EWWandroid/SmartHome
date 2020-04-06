package com.app.smarthome.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.app.smarthome.R;

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
        return inflater.inflate(R.layout.alertdialog_setupwifi, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText et_alertdialog_ssid = view.findViewById(R.id.et_alertdialog_ssid);
        final EditText et_alertdialog_add_password = view.findViewById(R.id.et_alertdialog_add_password);
        Button btn_alertdialog_add_add = view.findViewById(R.id.btn_alertdialog_add_add);

        btn_alertdialog_add_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
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
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(getString(R.string.setup_wifi));
        builder.setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();

    }

    interface DialogListener {
        void onFinishEditing(String ssid, String password);
    }
}
